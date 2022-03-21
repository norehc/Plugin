package fr.norehc.test.npc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.norehc.test.main.Main;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;

import javax.net.ssl.HttpsURLConnection;

public class NPCManager {

	public static void createNPC(Player player) {
		ServerPlayer craftPlayer = ((CraftPlayer) player).getHandle();
		
		Property textures = (Property) craftPlayer.getGameProfile().getProperties().get("textures").toArray()[0];
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
		gameProfile.getProperties().put("textures", new Property("textures", textures.getValue(), textures.getSignature()));
		
		execute(gameProfile, (CraftWorld)player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), true, player.getName());
	}

	public static void createNPC(Player player, String name, String namePlayerTexture) {
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
		gameProfile = sendSetNPCSkinPacket(namePlayerTexture, gameProfile);

		execute(gameProfile, (CraftWorld) player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), true, namePlayerTexture);
	}

	public static void createNPC(String name, String namePlayerTexture, String world, double posX, double posY, double posZ) {
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
		gameProfile = sendSetNPCSkinPacket(namePlayerTexture, gameProfile);

		execute(gameProfile, (CraftWorld) Bukkit.getWorld(world), posX, posY, posZ, true, namePlayerTexture);
	}

	public static void createNPC(String name, String namePlayerTexture, String world, double posX, double posY, double posZ, boolean isNew) {
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
		gameProfile = sendSetNPCSkinPacket(namePlayerTexture, gameProfile);

		execute(gameProfile, (CraftWorld) Bukkit.getWorld(world), posX, posY, posZ, isNew, namePlayerTexture);
	}

	public static void createNPC(String name, String world, double posX, double posY, double posZ, String skin, String skinName, String signature, boolean isNew) {
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
		gameProfile.getProperties().put("textures", new Property("textures", skin, signature));

		execute(gameProfile, (CraftWorld) Bukkit.getWorld(world), posX, posY, posZ, isNew, skinName);
	}
	
	private static int execute(GameProfile gameProfile, CraftWorld world, double posX, double posY, double posZ, boolean isNew, String skinName) {
		
		ServerPlayer npc = new ServerPlayer(((CraftServer)Bukkit.getServer()).getServer(), world.getHandle(), gameProfile);
		npc.setPos(posX, posY, posZ);
		
		for(Player on : Bukkit.getOnlinePlayers()) {
			ServerGamePacketListenerImpl connection = ((CraftPlayer)on).getHandle().connection;
			
			connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
			
			connection.send(new ClientboundAddPlayerPacket(npc));
		}
		
		Main.getMain().getNPC().add(npc);
		Main.getMain().getDataNPC().add(new NPC(posX, posY, posZ, gameProfile.getName(), world.getName(), ((Property) gameProfile.getProperties().get("textures").toArray()[0]).getValue(), ((Property) gameProfile.getProperties().get("textures").toArray()[0]).getSignature(), isNew, "none", skinName));
		return Main.getMain().getDataNPC().indexOf(Main.getMain().getNPC().indexOf(npc));
	}

	public static void removeNPC(ServerPlayer npc) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
			connection.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
		}
		NPC npc1 = Main.getMain().getDataNPC().get(Main.getMain().getNPC().indexOf(npc));
		System.out.println(npc1.isNew());
		if(npc1.isNew()) {
			Main.getMain().getDataNPC().remove(npc1);
			Main.getMain().getNPC().remove(npc);
		}else {
			Main.getMain().getDataNPC().get(Main.getMain().getNPC().indexOf(npc)).delete();
		}
	}

	public static void updateNameNPC(ServerPlayer npc, String name) {
		NPC NPC = Main.getMain().getDataNPC().get(Main.getMain().getNPC().indexOf(npc));

		removeNPC(npc);
		deleteNPC(npc);

		GameProfile gameProfile = new GameProfile(npc.getUUID(), name);
		gameProfile.getProperties().put("textures", new Property("textures", NPC.getSkin(), NPC.getSignatures()));

		int i = execute(gameProfile, (CraftWorld) Bukkit.getWorld(NPC.getWorld()), NPC.getPosX(), NPC.getPosY(), NPC.getPosZ(), false, NPC.getSkinName());

		Main.getMain().getDataNPC().get(i).setOldName(NPC.getOldName());
	}

	public static void updateSkinNPC(ServerPlayer npc, String namePlayerSkin) {
		NPC NPC = Main.getMain().getDataNPC().get(Main.getMain().getNPC().indexOf(npc));

		removeNPC(npc);
		deleteNPC(npc);

		createNPC(NPC.getName(), namePlayerSkin, NPC.getWorld(), NPC.getPosX(), NPC.getPosY(), NPC.getPosZ(), false);
	}

	public static void deleteNPC(ServerPlayer npc) {
		Main.getMain().getDataNPC().remove(Main.getMain().getDataNPC().get(Main.getMain().getNPC().indexOf(npc)));
		Main.getMain().getNPC().remove(npc);
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
