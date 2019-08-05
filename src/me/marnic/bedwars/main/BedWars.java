package me.marnic.bedwars.main;

import me.marnic.bedwars.api.commands.CommandHandler;
import me.marnic.bedwars.api.util.WorldUtil;
import me.marnic.bedwars.mechanics.builder.BedWarsBuilder;
import me.marnic.bedwars.mechanics.bwgame.BedWarsMap;
import me.marnic.bedwars.mechanics.bwgame.BedWarsSpawner;
import me.marnic.bedwars.mechanics.bwgame.BedWarsTeam;
import me.marnic.bedwars.mechanics.commands.BedWarsCommands;
import me.marnic.bedwars.mechanics.event.BedWarsEventHandler;
import me.marnic.bedwars.mechanics.ingame.BedWarsInGameHandler;
import me.marnic.bedwars.mechanics.ingame.shop.ShopHandler;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Main class of Plugin
 */
public class BedWars extends JavaPlugin {

    public static BedWars INSTANCE;

    @Override
    public void onEnable() {

        INSTANCE = this;

        BedWarsCommands.INSTANCE.init();
        BedWarsCommands.INSTANCE.register();


        Bukkit.getPluginManager().registerEvents(new BedWarsEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(BedWarsInGameHandler.INSTANCE, this);

        List<String> list = WorldUtil.getWorldsToLoad();

        for (String world : list) {
            World world1 = Bukkit.getServer().createWorld(new WorldCreator(world));
            world1.setDifficulty(Difficulty.PEACEFUL);
        }

        ConfigurationSerialization.registerClass(BedWarsMap.class);
        ConfigurationSerialization.registerClass(BedWarsTeam.class);
        ConfigurationSerialization.registerClass(BedWarsSpawner.class);

        saveDefaultConfig();

        ShopHandler.INSTANCE.init();
        BedWarsBuilder.INSTANCE.checkConfig(getConfig());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            CommandHandler.INSTANCE.handleCommand(player, command.getName(), args);
        }

        return super.onCommand(sender, command, label, args);
    }
}
