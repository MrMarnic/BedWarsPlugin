package me.marnic.bedwars.mechanics.ingame.objects;

import me.marnic.bedwars.api.log.BedWarsLogger;
import me.marnic.bedwars.main.BedWars;
import me.marnic.bedwars.mechanics.bwgame.BedWarsMap;
import me.marnic.bedwars.mechanics.ingame.BedWarsInGameHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/*
 * Copyright (c) 04.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * In game functionality of waiting lobby
 */
public class InGameWaitingLobby {
    private BedWarsMap map;
    private ArrayList<Player> players;
    private boolean starting;
    private int id;

    public InGameWaitingLobby(BedWarsMap map) {
        this.map = map;
        this.players = new ArrayList<>();
    }

    /**
     * Used to add a player to the waiting lobby
     * - Teleport him/ Set gamemode
     * @param player player to add
     */
    public void addPlayer(Player player) {
        players.add(player);
        player.teleport(map.getLobbySpawn());
        player.setGameMode(GameMode.SURVIVAL);
        BedWarsLogger.logAll(players, "Player " + player.getName() + " joined the game. (" + players.size() + "/" + map.getMaxPlayers() + ")");
        update();
    }

    private void update() {
        if (players.size() == map.getMaxPlayers()) {
            beginStarting();
        } else {
            stopStarting();
        }
    }

    /**
     * Used to initiate a countdown for the game start
     */
    public void beginStarting() {
        this.starting = true;
        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(BedWars.INSTANCE, new Runnable() {

            int time = 0;

            @Override
            public void run() {
                time++;
                if (time == 1) {
                    BedWarsLogger.logAll(getPlayers(), "Game starts in 10s");
                } else if (time == 6) {
                    BedWarsLogger.logAll(getPlayers(), "Game starts in 5s");
                } else if (time == 8) {
                    BedWarsLogger.logAll(getPlayers(), "Game starts in 3s");
                } else if (time == 9) {
                    BedWarsLogger.logAll(getPlayers(), "Game starts in 2s");
                } else if (time == 10) {
                    BedWarsLogger.logAll(getPlayers(), "Game starts in 1s");
                } else if (time == 11) {
                    BedWarsLogger.logAll(getPlayers(), "Game is starting...");
                    Bukkit.getScheduler().cancelTask(id);
                    BedWarsInGameHandler.INSTANCE.startGame();
                }
            }
        }, 0, 20);
    }

    public void stopStarting() {
        if (starting) {
            Bukkit.getScheduler().cancelTask(id);
            BedWarsLogger.logAll(getPlayers(), "Game start stopped");
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        BedWarsLogger.logAll(players, "Player " + player.getName() + " left the game. (" + players.size() + "/" + map.getMaxPlayers() + ")");
        update();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Used to initiate a countdown for the game finish
     */
    public void stopGame() {
        for (Player player : players) {
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(map.getLobbySpawn());
        }
        BedWarsLogger.logAll(players, "Game has ended");

        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(BedWars.INSTANCE, new Runnable() {

            int time = 0;

            @Override
            public void run() {
                time++;
                if (time == 1) {
                    BedWarsLogger.logAll(getPlayers(), "Server closes in 10s");
                } else if (time == 6) {
                    BedWarsLogger.logAll(getPlayers(), "Server closes in 5s");
                } else if (time == 8) {
                    BedWarsLogger.logAll(getPlayers(), "Server closes in 3s");
                } else if (time == 9) {
                    BedWarsLogger.logAll(getPlayers(), "Server closes in 2s");
                } else if (time == 10) {
                    BedWarsLogger.logAll(getPlayers(), "Server closes in 1s");
                } else if (time == 11) {
                    BedWarsLogger.logAll(getPlayers(), "Server closes...");
                    Bukkit.getScheduler().cancelTask(id);
                    kickAll();
                    BedWarsInGameHandler.INSTANCE.recreateMap();
                }
            }
        }, 0, 20);
    }

    private void kickAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer("Server is recreating map");
        }
    }
}
