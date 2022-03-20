package fr.norehc.test.main;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.norehc.test.npc.DataNPC;
import fr.norehc.test.npc.NPC;
import fr.norehc.test.packet.PacketReader;
import fr.norehc.test.permission.PermissionManager;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.apache.commons.dbcp2.BasicDataSource;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.norehc.test.command.CommandsManager;
import fr.norehc.test.gestion.Account;
import fr.norehc.test.gestion.BanManager;
import fr.norehc.test.gestion.Guilds;
import fr.norehc.test.gestion.unit.GradeUnit;
import fr.norehc.test.gestion.unit.RankUnit;
import fr.norehc.test.listenner.ListenersManager;
import fr.norehc.test.mysql.MySQL;
import fr.norehc.test.scoreboard.ScoreboardTeam;
import fr.norehc.test.tablist.Tablist;

public class Main extends JavaPlugin {
	
	private static Main main;
	
	private BasicDataSource connectionPool;
	
	private MySQL mysql;
	
	private Tablist tablist;
	
	private List<Account> accounts;
	private List<ScoreboardTeam> teams = new ArrayList<>();
	
	private Guilds guilds;
	
	public BanManager bm;

	private List<ServerPlayer> NPC = new ArrayList<>();
	private List<NPC> dataNPC = new ArrayList<>();

	private PermissionManager permissionManager = new PermissionManager();
	
	
	@Override
	public void onEnable() {
		
		main = this;
		
		initConnection();
		
		accounts = new ArrayList<Account>();
		
		bm = new BanManager();
		
		new ListenersManager().registerListeners();
		
		new CommandsManager().registerCommands();
        
        for(GradeUnit grade : GradeUnit.values()) {
        	for(RankUnit rank : RankUnit.values()) {
        		teams.add(new ScoreboardTeam(rank, grade));
        	}
        }
        
        guilds = new Guilds();
        
        tablist = new Tablist();

		DataNPC.setupNPCs();

		readerEnable();

		permissionManager.setupPermission();
        
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers()) {
		   			tablist.setTab(player);
					ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
					NPC.stream().forEach(npc -> {
						connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc));
					});
		   		}
		    }
		}, 0L, 300L);
	}
	
	@Override
	public void onDisable() {
		guilds.onLogout();
		DataNPC.saveNPCs();
		readerDisable();
		permissionManager.savePermission();
	}
	
	public static Main getMain() {
		return main;
	}

	private void readerEnable() {
		if(!Bukkit.getOnlinePlayers().isEmpty()) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				PacketReader reader = new PacketReader();
				reader.inject(player);
			}
		}
	}

	private void readerDisable() {
		if(!Bukkit.getOnlinePlayers().isEmpty()) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				PacketReader reader = new PacketReader();
				reader.unInject(player);
			}
		}
	}

	private void initConnection() {
		connectionPool = new BasicDataSource();
		connectionPool.setDriverClassName("com.mysql.jdbc.Driver");
		connectionPool.setUsername("norehc");
		connectionPool.setPassword("timcheron2002");
		connectionPool.setUrl("jdbc:mysql://163.172.80.63:3306/test?autoReconnect=true");
		
		connectionPool.setInitialSize(1);
        connectionPool.setMaxTotal(10);
		
		mysql = new MySQL(connectionPool);
		
		mysql.createTables();
	}
	
	public MySQL getMySQL() {
		return mysql;
	}
	
	public List<Account> getAccounts() {
		return accounts;
	}

	public Optional<Account> getAccount(Player player) {
		return new ArrayList<>(accounts).stream().filter(a -> a.getUUID().equals(player.getUniqueId().toString())).findFirst();
	}
    
    public List<ScoreboardTeam> getTeams() {
    	return teams;
    }
    
    public Optional<ScoreboardTeam> getSbTeam(RankUnit rank, GradeUnit grade) {
    	return teams.stream().filter(t -> t.getGrade() == grade && t.getRank() == rank).findFirst();
    }

	public List<ServerPlayer> getNPC() {
		return NPC;
	}

	public List<NPC> getDataNPC() {
		return dataNPC;
	}
    
    public Tablist getTablist() {
    	return tablist;
    }
    
    public Guilds getGuilds() {
    	return guilds;
    }

	public PermissionManager getPermissionManager() {
		return permissionManager;
	}
}
