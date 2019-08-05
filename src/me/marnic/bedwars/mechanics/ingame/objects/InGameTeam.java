package me.marnic.bedwars.mechanics.ingame.objects;

import me.marnic.bedwars.mechanics.bwgame.BedWarsMap;
import me.marnic.bedwars.mechanics.bwgame.BedWarsSpawner;
import me.marnic.bedwars.mechanics.bwgame.BedWarsTeam;
import me.marnic.bedwars.mechanics.ingame.BedWarsInGameHandler;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;

/*
 * Copyright (c) 04.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */


/**
 * In game functionality of a team
 */
public class InGameTeam {
    private BedWarsMap map;
    private BedWarsTeam team;
    private ArrayList<Player> players;
    private ArrayList<InGameSpawner> spawners;
    private boolean canRespawn = true;

    /**
     *
     * @param map BedWars map it belongs to
     * @param team BedWars team it belongs to
     */
    public InGameTeam(BedWarsMap map, BedWarsTeam team) {
        this.map = map;
        this.team = team;
        this.players = new ArrayList<>();
        this.spawners = new ArrayList<>();
        for (BedWarsSpawner spawner : team.getSpawners()) {
            spawners.add(new InGameSpawner(spawner));
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Teleports players to their teams
     */
    public void start() {
        for (Player player : players) {
            player.teleport(team.getSpawnLocation());
            player.getInventory().clear();
            player.setFallDistance(0);
            player.setHealth(20);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    /**
     * Removes player from team
     * Example: If player dies
     * @param player
     */
    public void removePlayer(Player player) {
        this.players.remove(player);
        update();
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    private void update() {
        if (players.isEmpty()) {
            BedWarsInGameHandler.INSTANCE.getInGameWorld().update();
        }
    }

    public void handleRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(team.getSpawnLocation());
        if (!canRespawn) {
            BedWarsInGameHandler.INSTANCE.getInGameWorld().getPlayersToTeam().remove(e.getPlayer().getUniqueId());
        }
    }

    public void handleDeath(PlayerDeathEvent e) {
        if (!canRespawn) {
            e.getEntity().setGameMode(GameMode.SPECTATOR);
            removePlayer(e.getEntity());
        }
    }

    public void handleDamage(Player player) {
        player.teleport(team.getSpawnLocation());
        player.getInventory().clear();
        player.setHealth(20);
        if (!canRespawn) {
            player.setGameMode(GameMode.SPECTATOR);
            removePlayer(player);
        }
    }

    public void updateSecond(World world) {
        for (InGameSpawner spawner : spawners) {
            spawner.updateSecond(world);
        }
    }

    public BedWarsTeam getTeam() {
        return team;
    }

    public void setCanRespawn(boolean canRespawn) {
        this.canRespawn = canRespawn;
    }
}
