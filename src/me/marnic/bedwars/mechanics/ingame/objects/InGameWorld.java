package me.marnic.bedwars.mechanics.ingame.objects;

import me.marnic.bedwars.api.log.BedWarsLogger;
import me.marnic.bedwars.main.BedWars;
import me.marnic.bedwars.mechanics.bwgame.BedWarsMap;
import me.marnic.bedwars.mechanics.ingame.BedWarsInGameHandler;
import me.marnic.bedwars.mechanics.ingame.shop.ShopHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * Copyright (c) 04.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * In game functionality of the game world
 */
public class InGameWorld {
    private BedWarsMap map;
    private ArrayList<Player> players;
    private ArrayList<InGameTeam> teams;
    private HashMap<UUID, InGameTeam> playersToTeam;

    private int idSpawnerTask;

    public InGameWorld(BedWarsMap map) {
        this.map = map;
        this.players = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.playersToTeam = new HashMap<>();
    }

    /**
     * Used to set the players of a game world from the InGameWaitingLobby
     * @param players arraylist of players
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = new ArrayList<>(players);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);

        InGameTeam team = playersToTeam.get(player.getUniqueId());

        if (team != null) {
            team.removePlayer(player);
        }
    }

    /**
     * Used to put players in their teams
     * and starts spawner
     */
    public void start() {
        int teamSize = map.getTeamPlayers();

        AtomicInteger index = new AtomicInteger();

        map.getTeams().forEach((k, v) -> {
            InGameTeam team = new InGameTeam(map, v);
            for (int i = 0; i < teamSize; i++) {
                team.getPlayers().add(players.get(index.get() + i));
                playersToTeam.put(players.get(index.get() + i).getUniqueId(), team);
            }
            teams.add(team);
            index.getAndIncrement();
        });

        for (InGameTeam team : teams) {
            team.start();
        }

        this.idSpawnerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(BedWars.INSTANCE, new Runnable() {
            @Override
            public void run() {
                for (InGameTeam team : teams) {
                    team.updateSecond(map.getGameWorld());
                }
            }
        }, 0, 20);
    }

    /**
     * Update spawner
     */
    public void update() {
        int count = teams.size();
        for (InGameTeam team : teams) {
            if (team.isEmpty()) {
                count--;
            }
        }
        if (count <= 1) {
            if (count == 1) {
                BedWarsLogger.logAll(players, "Team " + teams.get(0).getTeam().getChatColor().name() + " has won");
            }
            beginStopping();
        }
    }

    int id;

    /**
     * Initiates countdown for game finish
     */
    private void beginStopping() {
        BedWarsInGameHandler.INSTANCE.setState(BedWarsInGameHandler.GameState.CLOSED);
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(BedWars.INSTANCE, new Runnable() {

            int time = 0;

            @Override
            public void run() {

                if (time == 1) {
                    BedWarsLogger.logAll(players, "Game ends in 10s");
                } else if (time == 6) {
                    BedWarsLogger.logAll(players, "Game ends in 5s");
                } else if (time == 8) {
                    BedWarsLogger.logAll(players, "Game ends in 3s");
                } else if (time == 9) {
                    BedWarsLogger.logAll(players, "Game ends in 2s");
                } else if (time == 10) {
                    BedWarsLogger.logAll(players, "Game ends in 1s");
                } else if (time == 11) {
                    BedWarsLogger.logAll(players, "Game is ending...");
                    Bukkit.getScheduler().cancelTask(id);
                    Bukkit.getScheduler().cancelTask(idSpawnerTask);
                    BedWarsInGameHandler.INSTANCE.stopGame();
                }

                time++;
            }
        }, 0, 20);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void handleRespawn(PlayerRespawnEvent e) {
        playersToTeam.get(e.getPlayer().getUniqueId()).handleRespawn(e);
    }

    public void handleDeath(PlayerDeathEvent e) {
        playersToTeam.get(e.getEntity().getUniqueId()).handleDeath(e);
    }

    public void handleInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (e.hasItem()) {
                if (e.getMaterial() == Material.BLAZE_ROD) {
                    if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Home Teleporter")) {
                        BedWarsSpecialItems.handleTeleporter(e.getPlayer(), playersToTeam.get(e.getPlayer().getUniqueId()).getTeam());
                        e.getItem().setAmount(e.getItem().getAmount() - 1);
                    }
                } else if (e.getMaterial() == Material.SLIME_BALL) {
                    if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Saving Platform")) {
                        BedWarsSpecialItems.handlePlatform(e);
                        e.getItem().setAmount(e.getItem().getAmount() - 1);
                    }
                }
            }
        }
    }

    public void handleBlockBreak(BlockBreakEvent e) {
        if (!BedWarsBlocks.MATERIALS.contains(e.getBlock().getBlockData().getMaterial())) {
            e.setCancelled(true);
        }

        if (e.getBlock().getBlockData() instanceof Bed) {
            InGameTeam team = getTeamForBedPos(e.getBlock().getLocation());
            if (team != null) {
                if (team != playersToTeam.get(e.getPlayer().getUniqueId())) {
                    e.setDropItems(false);
                    team.setCanRespawn(false);
                    BedWarsLogger.logAll(players, "The bed of team " + team.getTeam().getChatColor().name() + " was destroyed by " + e.getPlayer().getName());
                    BedWarsLogger.logAll(team.getPlayers(), "Your bed was destroyed");
                } else {
                    BedWarsLogger.log(e.getPlayer(), "You can not destroy you own bed");
                    e.setCancelled(true);
                }
            }
        }
    }

    public void handleEntityInteract(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.VILLAGER) {
            ShopHandler.INSTANCE.handleInteractEvent(e);
            return;
        }
        e.setCancelled(true);
    }

    public void handleDamage(Player player) {
        playersToTeam.get(player.getUniqueId()).handleDamage(player);
    }

    public InGameTeam getTeamForBedPos(Location location) {
        for (InGameTeam team : teams) {
            if (team.getTeam().getBedBlockPos().equals(location) | team.getTeam().getBedBlockPos2().equals(location)) {
                return team;
            }
        }
        return null;
    }

    public HashMap<UUID, InGameTeam> getPlayersToTeam() {
        return playersToTeam;
    }
}
