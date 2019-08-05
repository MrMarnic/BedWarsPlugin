package me.marnic.bedwars.mechanics.commands;

import me.marnic.bedwars.api.commands.BasicCommand;
import me.marnic.bedwars.api.commands.CommandHandler;
import me.marnic.bedwars.api.log.BedWarsLogger;
import me.marnic.bedwars.api.util.BWBuilderUtil;
import me.marnic.bedwars.mechanics.builder.BedWarsBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * All BedWars commands
 */
public class BedWarsCommands {
    public static BedWarsCommands INSTANCE = new BedWarsCommands();

    public BasicCommand defaultCommand;
    public BasicCommand bwCreate;
    public BasicCommand bwGameWorld;
    public BasicCommand bwLobbyWorld;
    public BasicCommand bwSpawn;

    public BasicCommand teamColor;
    public BasicCommand teamSize;
    public BasicCommand teamSpawn;
    public BasicCommand teamBedLoc;
    public BasicCommand teamSpawner;

    public BasicCommand bwMapFinish;

    public BasicCommand tpWorld;

    public void init() {
        this.defaultCommand = new BasicCommand("bw", null) {
            @Override
            public void execute(Player player, String cmd, String[] args) {
                BedWarsLogger.log(player, "BedWars Commands:");
                for (BasicCommand command : CommandHandler.INSTANCE.getCOMMANDS()) {
                    BedWarsLogger.log(player, command.getHelpMessage());
                }
            }

            @Override
            public String getHelpMessage() {
                return getName();
            }
        };


        //BedWars map commands

        this.bwCreate = new BasicCommand("bw", new String[]{"create", "%"}) {
            @Override
            public void execute(Player player, String cmd, String[] args) {

                BedWarsBuilder.INSTANCE.startCreating(args[1]);
                BedWarsLogger.log(player, "Starting creation of BedWars map " + args[1]);
            }

            @Override
            public String getHelpMessage() {
                return "bw create [mapName]";
            }
        };

        this.bwLobbyWorld = new BasicCommand("bw", new String[]{"map", "set", "lobbyworld", "%"}) {
            @Override
            public void execute(Player player, String cmd, String[] args) {

                if (!BWBuilderUtil.checkCreation(player)) {
                    return;
                }

                String worldName = args[3];
                World world = Bukkit.getWorld(worldName);

                if (world != null) {
                    BedWarsBuilder.INSTANCE.setLobbyWorld(world);
                    BedWarsLogger.log(player, "Lobby world was set to " + worldName);
                } else {
                    BedWarsLogger.warn(player, "World " + worldName + " does not exist!");
                }
            }

            @Override
            public String getHelpMessage() {
                return "bw map set lobbyworld [worldName]";
            }
        };

        this.bwGameWorld = new BasicCommand("bw", new String[]{"map", "set", "gameworld", "%"}) {
            @Override
            public void execute(Player player, String cmd, String[] args) {

                if (!BWBuilderUtil.checkCreation(player)) {
                    return;
                }

                String worldName = args[3];
                World world = Bukkit.getWorld(worldName);

                if (world != null) {
                    BedWarsBuilder.INSTANCE.setGameWorld(world);
                    BedWarsLogger.log(player, "Game world was set to " + worldName);
                } else {
                    BedWarsLogger.warn(player, "World " + worldName + " does not exist!");
                }
            }

            @Override
            public String getHelpMessage() {
                return "bw map set gameworld [worldName]";
            }
        };

        this.bwSpawn = new BasicCommand("bw", new String[]{"map", "set", "lobbyspawn"}) {
            @Override
            public void execute(Player player, String cmd, String[] args) {
                if (!BWBuilderUtil.checkCreation(player)) {
                    return;
                }

                BedWarsBuilder.INSTANCE.setLobbySpawn(player.getLocation());
                BedWarsLogger.log(player, "Lobby spawn was set");
            }

            @Override
            public String getHelpMessage() {
                return "bw map set lobbyspawn";
            }
        };

        //BedWars team commands
        this.teamColor = new BasicCommand("bw", new String[]{"team", "create", "%"}) {
            @Override
            public void execute(Player player, String cmd, String[] args) {
                if (!BWBuilderUtil.checkCreation(player)) {
                    return;
                }

                ChatColor color = BWBuilderUtil.chatColorFromString(args[2]);

                if (color != null) {
                    BedWarsBuilder.INSTANCE.createTeam(color);
                    BedWarsLogger.log(player, "Team " + color.name() + " was created");
                } else {
                    BedWarsLogger.warn(player, "The color " + args[2] + " is not valid");
                }
            }

            @Override
            public String getHelpMessage() {
                return "bw team create [color]";
            }
        };

        this.teamSize = new BasicCommand("bw", new String[]{"team", "%", "set", "size", "%"}) {
            @Override
            public void execute(Player player, String cmd, String[] args) {
                if (!BWBuilderUtil.checkCreationAndTeam(player, args[1])) {
                    return;
                }

                BedWarsBuilder.INSTANCE.setTeamPlayerCount(args[1], Integer.parseInt(args[4]));
                BedWarsLogger.log(player, "Player count of team " + args[1] + " was set to " + Integer.parseInt(args[4]));
            }

            @Override
            public String getHelpMessage() {
                return "bw team [teamName] set size [teamSize]";
            }
        };

        this.teamSpawn = new BasicCommand("bw", new String[]{"team", "%", "set", "spawn"}) {
            @Override
            public void execute(Player player, String cmd, String[] args) {
                if (!BWBuilderUtil.checkCreationAndTeam(player, args[1])) {
                    return;
                }

                BedWarsBuilder.INSTANCE.setTeamSpawnPos(args[1], player.getLocation());
                BedWarsLogger.log(player, "Spawn of team " + args[1] + " was set");
            }

            @Override
            public String getHelpMessage() {
                return "bw team [teamName] set spawn";
            }
        };

        this.teamBedLoc = new BasicCommand("bw", new String[]{"team", "%", "set", "bedlocation"}) {
            @Override
            public void execute(Player player, String cmd, String[] args) {
                if (!BWBuilderUtil.checkCreationAndTeam(player, args[1])) {
                    return;
                }

                BedWarsBuilder.INSTANCE.setPlacer(args[1]);

                BedWarsLogger.log(player, "Location of the bed from team " + args[1] + " can now be set.");
                BedWarsLogger.log(player, "Please place a bed");
            }

            @Override
            public String getHelpMessage() {
                return "bw team [teamName] set bedlocation";
            }
        };

        this.teamSpawner = new BasicCommand("bw", new String[]{"team", "%", "spawner", "add", "%"}) {
            @Override
            public void execute(Player player, String cmd, String[] args) {
                if (!BWBuilderUtil.checkCreationAndTeam(player, args[1])) {
                    return;
                }


                String material = args[4];

                if (!BWBuilderUtil.checkMaterial(player, material)) {
                    return;
                }

                BedWarsBuilder.INSTANCE.addTeamSpawner(args[1], material, player.getLocation());
                BedWarsLogger.log(player, material + " spawner was added to team " + args[1]);
            }

            @Override
            public String getHelpMessage() {
                return "bw team [teamName] spawner add [materialName]";
            }
        };

        //BedWars finish command
        this.bwMapFinish = new BasicCommand("bw", new String[]{"map", "finish"}) {
            @Override
            public void execute(Player player, String cmd, String[] args) {
                if (!BWBuilderUtil.checkCreation(player)) {
                    return;
                }

                if (BedWarsBuilder.INSTANCE.finishMap()) {
                    BedWarsLogger.log(player, "Map is finished");
                    BedWarsBuilder.INSTANCE.saveMap();
                } else {
                    BedWarsLogger.warn(player, "Missing things for finish:");
                    for (String txt : BedWarsBuilder.INSTANCE.getCurrentBuildingObject().getNotFinished()) {
                        BedWarsLogger.warn(player, txt);
                    }
                }
            }

            @Override
            public String getHelpMessage() {
                return "bw map finish";
            }
        };


        this.tpWorld = new BasicCommand("bw", new String[]{"tp", "%"}) {
            @Override
            public void execute(Player player, String cmd, String[] args) {
                String world = args[1];

                Location location = player.getLocation().clone();

                World world1 = Bukkit.getWorld(world);

                if (world1 == null) {
                    world1 = Bukkit.getServer().createWorld(new WorldCreator(world));
                }

                location.setWorld(world1);

                player.teleport(location);
            }

            @Override
            public String getHelpMessage() {
                return null;
            }
        };
    }

    public void register() {
        CommandHandler.INSTANCE.registerCommand(defaultCommand);
        CommandHandler.INSTANCE.registerCommand(bwCreate);
        CommandHandler.INSTANCE.registerCommand(bwGameWorld);
        CommandHandler.INSTANCE.registerCommand(bwLobbyWorld);
        CommandHandler.INSTANCE.registerCommand(bwSpawn);

        CommandHandler.INSTANCE.registerCommand(teamBedLoc);
        CommandHandler.INSTANCE.registerCommand(teamColor);
        CommandHandler.INSTANCE.registerCommand(teamSize);
        CommandHandler.INSTANCE.registerCommand(teamSpawn);
        CommandHandler.INSTANCE.registerCommand(teamSpawner);

        CommandHandler.INSTANCE.registerCommand(bwMapFinish);

        CommandHandler.INSTANCE.registerCommand(tpWorld);
    }
}
