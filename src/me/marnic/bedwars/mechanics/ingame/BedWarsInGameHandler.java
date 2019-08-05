package me.marnic.bedwars.mechanics.ingame;

import me.marnic.bedwars.api.util.WorldUtil;
import me.marnic.bedwars.mechanics.bwgame.BedWarsMap;
import me.marnic.bedwars.mechanics.ingame.objects.InGameWaitingLobby;
import me.marnic.bedwars.mechanics.ingame.objects.InGameWorld;
import me.marnic.bedwars.mechanics.ingame.shop.ShopHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * Handles all in game stuff
 */
public class BedWarsInGameHandler implements Listener {
    public static BedWarsInGameHandler INSTANCE = new BedWarsInGameHandler();

    private BedWarsMap map;
    private GameState state = GameState.OFF;

    private InGameWaitingLobby waitingLobby;
    private InGameWorld inGameWorld;

    /**
     * Used to load BedWars map from config
     * @param config plugin config (getConfig())
     */
    public void load(FileConfiguration config) {
        this.map = (BedWarsMap) config.get("bw.map");

        this.state = GameState.LOBBY;
        this.waitingLobby = new InGameWaitingLobby(map);
        this.inGameWorld = new InGameWorld(map);

        WorldUtil.backupWorld(map.getLobbyWorld());
        WorldUtil.backupWorld(map.getGameWorld());
    }

    public void startGame() {
        this.state = GameState.INGAME;
        this.inGameWorld.setPlayers(waitingLobby.getPlayers());
        this.inGameWorld.start();
    }

    public void stopGame() {
        waitingLobby.getPlayers().clear();
        waitingLobby.getPlayers().addAll(inGameWorld.getPlayers());
        waitingLobby.stopGame();
        this.state = GameState.CLOSED;
    }

    /**
     * Formerly used to recreate worlds.
     * Now it only stops the server
     */
    public void recreateMap() {
        Bukkit.shutdown();
    }

    @EventHandler
    public void loggedIn(PlayerLoginEvent e) {
        if (this.state == GameState.CLOSED) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is recreating map");
        } else if (this.state == GameState.LOBBY || this.state == GameState.OFF) {
            e.allow();
        } else {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Game is running");
        }
    }

    @EventHandler
    public void joinServerEvent(PlayerJoinEvent e) {
        if (state == GameState.LOBBY) {
            e.setJoinMessage("");
            if (waitingLobby.getPlayers().size() < map.getMaxPlayers()) {
                waitingLobby.addPlayer(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void leaveServerEvent(PlayerQuitEvent e) {
        if (state == GameState.LOBBY) {
            e.setQuitMessage("");
            waitingLobby.removePlayer(e.getPlayer());
        } else if (state == GameState.INGAME) {
            e.setQuitMessage("");
            inGameWorld.removePlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void breakBlockEvent(BlockBreakEvent e) {
        if (state == GameState.LOBBY | state == GameState.CLOSED) {
            e.setCancelled(true);
        } else if (state == GameState.INGAME) {
            inGameWorld.handleBlockBreak(e);
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        if (state == GameState.LOBBY | state == GameState.CLOSED) {
            e.setCancelled(true);
        } else if (state == GameState.INGAME) {
            inGameWorld.handleInteract(e);
        }
    }

    @EventHandler
    public void interactEntity(PlayerInteractEntityEvent e) {
        if (state == GameState.INGAME) {
            inGameWorld.handleEntityInteract(e);
        }
    }

    @EventHandler
    public void invClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("BedWars Shop") && e.getCurrentItem() != null) {
            ShopHandler.INSTANCE.handleInvClickEvent(e);
        }
    }

    @EventHandler
    public void damage(EntityDamageEvent e) {
        if (state == GameState.LOBBY | state == GameState.CLOSED) {
            e.setCancelled(true);
            if (e.getEntityType() == EntityType.PLAYER) {
                if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    e.getEntity().teleport(map.getLobbySpawn());
                }
            }
        } else if (state == GameState.INGAME) {
            if (e.getEntityType() == EntityType.PLAYER) {
                if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    e.setCancelled(true);
                    Player player = (Player) e.getEntity();
                    player.setFallDistance(0);
                    inGameWorld.handleDamage(player);
                }
            }
        }
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e) {
        if (state == GameState.LOBBY | state == GameState.CLOSED) {
            e.setRespawnLocation(map.getLobbySpawn());
        } else {
            inGameWorld.handleRespawn(e);
        }
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        if (state == GameState.INGAME) {
            inGameWorld.handleDeath(e);
        }
    }

    @EventHandler
    public void spawn(CreatureSpawnEvent e) {
        if (state == GameState.LOBBY | state == GameState.CLOSED) {
            if (e.getEntityType() != EntityType.PLAYER) {
                e.setCancelled(true);
            }
        }
    }

    public InGameWorld getInGameWorld() {
        return inGameWorld;
    }

    public InGameWaitingLobby getWaitingLobby() {
        return waitingLobby;
    }

    public BedWarsMap getMap() {
        return map;
    }

    /**
     * Used to set game state
     * GameState has an impact on many things: Joining,Leaving,Breaking,Killing,Death,Respawn
     * @param state GameState enum
     */
    public void setState(GameState state) {
        this.state = state;
    }


    public enum GameState {
        OFF, LOBBY, INGAME, CLOSED
    }
}
