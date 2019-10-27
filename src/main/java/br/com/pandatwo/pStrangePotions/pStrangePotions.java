package br.com.pandatwo.pStrangePotions;

import br.com.pandatwo.pStrangePotions.commands.SPotionCommand;
import br.com.pandatwo.pStrangePotions.listeners.PotionListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class pStrangePotions extends JavaPlugin {

    private static pStrangePotions instance;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new PotionListener(), this);
        Bukkit.getPluginCommand("spotion").setExecutor(new SPotionCommand());
    }


    public static pStrangePotions getInstance() {
        return instance;
    }

}
