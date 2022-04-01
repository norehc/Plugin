package fr.norehc.test.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.norehc.test.main.Main;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class NPCManager {

	public static void createNPC(Player player) {
		ServerPlayer craftPlayer = ((CraftPlayer) player).getHandle();
		
		Property textures = (Property) craftPlayer.getGameProfile().getProperties().get("textures").toArray()[0];
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
		gameProfile.getProperties().put("textures", new Property("textures", textures.getValue(), textures.getSignature()));

		System.out.println(((CraftWorld) player.getWorld()).getHandle());

		execute(gameProfile, (CraftWorld)player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), true, player.getName(), "none");
	}

	public static void createNPC(Player player, String name, String namePlayerTexture) {
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
		gameProfile = sendSetNPCSkinPacket(namePlayerTexture, gameProfile);

		execute(gameProfile, (CraftWorld) player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), true, namePlayerTexture, "none");
	}

	public static void createNPC(String name, String namePlayerTexture, String world, double posX, double posY, double posZ) {
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
		gameProfile = sendSetNPCSkinPacket(namePlayerTexture, gameProfile);

		execute(gameProfile, (CraftWorld) Bukkit.getWorld(world), posX, posY, posZ, true, namePlayerTexture, "none");
	}

	public static void createNPC(String name, String namePlayerTexture, String world, double posX, double posY, double posZ, boolean isNew) {
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
		gameProfile = sendSetNPCSkinPacket(namePlayerTexture, gameProfile);

		execute(gameProfile, (CraftWorld) Bukkit.getWorld(world), posX, posY, posZ, isNew, namePlayerTexture, "none");
	}

	public static void createNPC(String name, String world, double posX, double posY, double posZ, String skin, String skinName, String signature, boolean isNew, String function) {
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
		gameProfile.getProperties().put("textures", new Property("textures", skin, signature));

		execute(gameProfile, (CraftWorld) Bukkit.getWorld(world), posX, posY, posZ, isNew, skinName, function);
	}
	
	private static void execute(GameProfile gameProfile, CraftWorld world, double posX, double posY, double posZ, boolean isNew, String skinName, String function) {
		
		ServerPlayer npc = new ServerPlayer(((CraftServer)Bukkit.getServer()).getServer(), world.getHandle(), gameProfile);
		npc.setPos(posX, posY, posZ);

		for(Player player : Bukkit.getOnlinePlayers()) {
			ServerGamePacketListenerImpl connection = ((CraftPlayer)player).getHandle().connection;
			
			connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
			
			connection.send(new ClientboundAddPlayerPacket(npc));
		}

		Main.getMain().getDataNPCs().put(new NPC(posX, posY, posZ, gameProfile.getName(), world.getName(), ((Property) gameProfile.getProperties().get("textures").toArray()[0]).getValue(), ((Property) gameProfile.getProperties().get("textures").toArray()[0]).getSignature(), isNew, function, skinName), npc);
	}

	public static void removeNPC(ServerPlayer npc) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
			connection.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
		}

		NPC NPC = Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
			if(entry.getValue() == npc) return true;
			return false;
		}).findFirst().get().getKey();
		if(NPC.isNew()) {
			Main.getMain().getDataNPCs().remove(NPC, npc);
		}else {
			Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
				if(entry.getValue() == npc) return true;
				return false;
			}).findFirst().get().getKey().delete();
		}
	}

	//Ajouter le fait que le nom peut-etre deja pris et aussi la suppression du NPC doit etre modifier en fonction des variables du NPC
	public static void updateNameNPC(ServerPlayer npc, String name, boolean isNew) {
		NPC NPC = Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
			if(entry.getValue() == npc) return true;
			return false;
		}).findFirst().get().getKey();

		System.out.println(isNew);

		deleteNPC(npc);

		GameProfile gameProfile = new GameProfile(npc.getUUID(), name);
		gameProfile.getProperties().put("textures", new Property("textures", NPC.getSkin(), NPC.getSignatures()));

		execute(gameProfile, (CraftWorld) Bukkit.getWorld(NPC.getWorld()), NPC.getPosX(), NPC.getPosY(), NPC.getPosZ(), isNew, NPC.getSkinName(), NPC.getFunction());

		Main.getMain().getDataNPCs().entrySet().forEach(entry -> {
			if(entry.getKey().getName().equals(name) && entry.getKey().exist()) {
				entry.getKey().setOldName(NPC.getOldName());
			}
		});
	}

	public static void updateSkinNPC(ServerPlayer npc, String namePlayerSkin) {
		NPC NPC = Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
			if(entry.getValue() == npc) return true;
			return false;
		}).findFirst().get().getKey();

		deleteNPC(npc);

		createNPC(NPC.getName(), namePlayerSkin, NPC.getWorld(), NPC.getPosX(), NPC.getPosY(), NPC.getPosZ(), NPC.isNew());

		Main.getMain().getDataNPCs().entrySet().forEach(entry -> {
			if(entry.getKey().getName().equals(NPC.getName()) && entry.getKey().exist()) {
				entry.getKey().setFunction(NPC.getFunction());
			}
		});
	}

	public static void deleteNPC(ServerPlayer npc) {
		//NPC NPC = Main.getMain().getDataNPC().get(Main.getMain().getNPC().indexOf(npc));
		for(Player player : Bukkit.getOnlinePlayers()) {
			ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
			connection.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
		}
		Main.getMain().getDataNPCs().remove(Main.getMain().getDataNPCs().entrySet().stream().filter(entry -> {
			if(entry.getValue() == npc) return true;
			return false;
		}).findFirst().get().getKey());
	}

	private static GameProfile sendSetNPCSkinPacket(String username, GameProfile gameProfile) { // The username is the name for the player that has the skin.


		try {
			HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://api.ashcon.app/mojang/v2/user/%s", username)).openConnection();
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				ArrayList<String> lines = new ArrayList<>();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				reader.lines().forEach(lines::add);

				String reply = String.join(" ",lines);
				int indexOfValue = reply.indexOf("\"value\": \"");
				int indexOfSignature = reply.indexOf("\"signature\": \"");
				String skin = reply.substring(indexOfValue + 10, reply.indexOf("\"", indexOfValue + 10));
				String signature = reply.substring(indexOfSignature + 14, reply.indexOf("\"", indexOfSignature + 14));

				gameProfile.getProperties().put("textures", new Property("textures", skin, signature));
			} else {
				Bukkit.getConsoleSender().sendMessage("Connection could not be opened when fetching player skin (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return gameProfile;
	}
}
