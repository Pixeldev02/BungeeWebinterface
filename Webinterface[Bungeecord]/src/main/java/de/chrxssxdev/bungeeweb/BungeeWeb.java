package de.chrxssxdev.bungeeweb;

import com.google.common.io.ByteStreams;
import de.chrxssxdev.bungeeweb.commands.AccountCommand;
import de.chrxssxdev.bungeeweb.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;

public class BungeeWeb extends Plugin {

    private static BungeeWeb instance;
    private static MySQL mySQL;

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "Das Plugin: BungeeWeb wurde gestartet!");
        instance = this;
        init();
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Das Plugin: BungeeWeb wurde gestoppt!");
    }

    private void init() {
        mySQL = new MySQL();
        registerClasses();
        intializeConfig();
    }

    private void intializeConfig() {
        if(!getDataFolder().exists()) getDataFolder().mkdir();

        try {
            File file = new File(getDataFolder().getPath(), "config.yml");
            if(!file.exists()) {
                file.createNewFile();
            }

            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            // CONFIG DATA
            config.set("General.Prefix", "§7Webinterface §8× §7");

            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            getLogger().log(Level.INFO, "[CONFIG] ERROR: " + e.getMessage());
        }
    }

    private void registerClasses() {
        PluginManager pm = this.getProxy().getPluginManager();

        pm.registerCommand(this, new AccountCommand());
    }

    public static BungeeWeb getInstance() {
        return instance;
    }

    public static MySQL getMySQL() {
        return mySQL;
    }
}
