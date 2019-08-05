package me.marnic.bedwars.api.log;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Used to send messages to player
 */
public class BedWarsLogger {

    /**
     * Used to send the player a info message
     * Example: [BedWars/Info]msg
     * @param player receiver of the message
     * @param msg msg to send
     */
    public static void log(Player player, String msg) {
        player.sendMessage("[" + ChatColor.RED + "BedWars/Info" + ChatColor.RESET + "]" + msg);
    }

    /**
     * Used to send the player a warning message
     * Example: [BedWars/WARN]msg
     * @param player receiver of the message
     * @param msg msg to send
     */
    public static void warn(Player player, String msg) {
        player.sendMessage("[" + ChatColor.RED + "BedWars/" + ChatColor.YELLOW + "WARN" + ChatColor.RESET + "]" + msg);

    }

    /**
     * Used to send a list of players a message
     * Example: [BedWars/WARN]msg
     * @param playerList list of receiver
     * @param msg msg to send
     */
    public static void logAll(List<Player> playerList, String msg) {
        for (Player player : playerList) {
            log(player, msg);
        }
    }
}
