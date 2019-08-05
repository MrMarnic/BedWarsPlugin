package me.marnic.bedwars.api.commands;

import me.marnic.bedwars.api.log.BedWarsLogger;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * CommandHandler is used for all command related stuff
 */
public class CommandHandler {
    public static final CommandHandler INSTANCE = new CommandHandler();

    private ArrayList<BasicCommand> COMMANDS;

    public CommandHandler() {
        COMMANDS = new ArrayList<>();
    }

    /**
     * Used to register a command to the handler
     * @param command BasicCommand object not bukkit Command
     */
    public void registerCommand(BasicCommand command) {
        COMMANDS.add(command);
    }

    /**
     * Used when player executes command
     * Checks all registered commands for match
     * @param player command executor
     * @param cmd name of the command
     * @param args string array of command arguments
     */
    public void handleCommand(Player player, String cmd, String[] args) {
        boolean a = false;
        for (BasicCommand command : COMMANDS) {
            if (command.isCommand(cmd, args)) {
                command.execute(player, cmd, args);
                a = true;
                break;
            }
        }

        if (!a) {
            BedWarsLogger.warn(player, "Command " + cmd + " not found! Use /bw for help");
        }
    }

    /**
     * Returns all registered commands
     * @return BasicCommand ArrayList
     */
    public ArrayList<BasicCommand> getCOMMANDS() {
        return COMMANDS;
    }
}
