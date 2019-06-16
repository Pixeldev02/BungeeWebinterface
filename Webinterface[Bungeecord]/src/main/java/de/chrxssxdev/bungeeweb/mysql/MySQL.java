package de.chrxssxdev.bungeeweb.mysql;

import de.chrxssxdev.bungeeweb.BungeeWeb;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class MySQL {

    private BungeeWeb plugin;

    private static Connection con;

    public MySQL() {
        createDatabaseFile();
        connect();
    }

    private void connect() {
        try {
            File f2 = new File(BungeeWeb.getInstance().getDataFolder(), "mysql.yml");
            Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f2);
            con = DriverManager.getConnection("jdbc:mysql://" + cfg.getString("mysql.host") + ":" + cfg.getString("mysql.port") + "/" + cfg.getString("mysql.database"), cfg.getString("mysql.username"), cfg.getString("mysql.password"));
            this.setUp();
            BungeeWeb.getInstance().getLogger().log(Level.INFO, "[SYSTEM-SQL] CONNECTION OPENED");
        } catch (IOException | SQLException e) {
            BungeeWeb.getInstance().getLogger().log(Level.WARNING, "[SYSTEM-SQL] ERROR: " + e.getMessage());

        }
    }

    public void setUp() {
        try {
            PreparedStatement ps1 = con.prepareStatement("CREATE TABLE IF NOT EXISTS accounts(ID INT NOT NULL AUTO_INCREMENT, USERNAME VARCHAR(32), PASSWORD VARCHAR(255), USERGROUP VARCHAR(255), PRIMARY KEY(ID))");
            ps1.execute();
            ps1.closeOnCompletion();
        } catch (SQLException e) {
            BungeeWeb.getInstance().getLogger().log(Level.WARNING, "[SYSTEM-SQL] ERROR: " + e.getMessage());
        }
    }

    private void disconnect() {
        if(con != null) {
            try {
                con.close();
                BungeeWeb.getInstance().getLogger().log(Level.INFO, "[SYSTEM-SQL] CONNECTION CLOSED");
            } catch (SQLException e) {
                BungeeWeb.getInstance().getLogger().log(Level.WARNING, "[SYSTEM-SQL] ERROR: " + e.getMessage());
            }
        }
    }

    public void update(String qry) {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(qry);
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            disconnect();
            connect();
        }
    }

    public ResultSet query(String qry) {
        try {
            PreparedStatement ps = this.con.prepareStatement(qry);
            return ps.executeQuery();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isCon() {
        return con != null;
    }

    public Connection getConnection() {
        return con;
    }

    private void createDatabaseFile() {
        File file;
        if (!BungeeWeb.getInstance().getDataFolder().exists()) {
            BungeeWeb.getInstance().getDataFolder().mkdirs();
        }
        if (!(file = new File(BungeeWeb.getInstance().getDataFolder(), "mysql.yml")).exists()) {
            try {
                file.createNewFile();
                Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                cfg.set("mysql.host", (Object)"host");
                cfg.set("mysql.port", (Object)"3306");
                cfg.set("mysql.database", (Object)"database");
                cfg.set("mysql.username", (Object)"username");
                cfg.set("mysql.password", (Object)"password");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
