package fr.norehc.test.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.dbcp2.BasicDataSource;

public class MySQL {
	
	
	private BasicDataSource connectionPool;
	
	
	public MySQL(BasicDataSource connectionPool) {
		this.connectionPool = connectionPool;
	}
	
	public Connection getConnection() throws SQLException {
		return connectionPool.getConnection();
	}
	
	public void createTables(){
        update("CREATE TABLE IF NOT EXISTS player (" +
                "`index` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "UUID VARCHAR(255), " +
                "grade VARCHAR(255), " +
                "grade_end BIGINT, " +
                "`rank` varchar(255), " +
                "money BIGINT)");
        update("CREATE TABLE IF NOT EXISTS ban (" +
                "`index` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "UUID VARCHAR(255), " +
                "`end` BIGINT, " +
                "reason VARCHAR(255), " +
                "actif tinyint(1))");
        update("CREATE TABLE IF NOT EXISTS guild (" +
        		"`index` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        		"nameGuild varchar(255)," +
        		"prefixGuild varchar(255)," +
        		"moneyGuild BIGINT)");
        update("CREATE TABLE IF NOT EXISTS guildPlayer (" +
        		"`index` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
        		"nameGuild varchar(255)," +
        		"namePlayer varchar(255)," +
        		"`role` varchar(255))");
        update("CREATE TABLE IF NOT EXISTS npcs (" +
                "`index` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "posX DOUBLE," +
                "posY DOUBLE," +
                "posZ DOUBLE," +
                "name varchar(255)," +
                "world varchar(255)," +
                "skin varchar(1023)," +
                "signature varchar(1023)," +
                "function varchar(255))"
                );
        update("CREATE TABLE IF NOT EXISTS permissions(" +
                "`index` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "role varchar(255)," +
                "permission varchar(255))");
    }
	
	public void update(String qry){
        try {
        	Connection c = getConnection();
            PreparedStatement s = c.prepareStatement(qry);
            s.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
	
	public Object query(String qry, Function<ResultSet, Object> consumer){
        try {
        	Connection c = getConnection();
            PreparedStatement s = c.prepareStatement(qry);
            ResultSet rs = s.executeQuery();
            return consumer.apply(rs);
        } catch(SQLException e){
            throw new IllegalStateException(e.getMessage());
        }
    }
 
    public void query(String qry, Consumer<ResultSet> consumer){
        try {
        	Connection c = getConnection();
        	PreparedStatement s = c.prepareStatement(qry);
            ResultSet rs = s.executeQuery();
            consumer.accept(rs);
        } catch(SQLException e){
            throw new IllegalStateException(e.getMessage());
        }
    }
	
}
