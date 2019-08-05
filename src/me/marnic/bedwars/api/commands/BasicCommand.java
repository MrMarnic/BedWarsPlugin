package me.marnic.bedwars.api.commands;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */

/**
 * The BasicCommand class represents a server command with its args
 */
public abstract class BasicCommand {
    private String name;
    private List<String> argsList;

    /**
     *
     * @param name name of the command
     * @param args string array for the arguments
     *             "%" represents anything
     */
    public BasicCommand(String name, String[] args) {
        this.name = name;
        if (args == null) {
            this.argsList = new ArrayList<>();
        } else {
            this.argsList = Arrays.asList(args);
        }
    }

    /**
     * Returns true if the given command equals this command
     * @param cmd name of the command
     * @param args string array for the arguments
     * @return true if command matches
     */
    public boolean isCommand(String cmd, String[] args) {

        boolean argsEquals = true;

        if (argsList.size() == args.length) {
            if (argsList.size() > 0) {
                for (int i = 0; i < argsList.size(); i++) {
                    if (!stringEquals(this.argsList.get(i), args[i])) {
                        argsEquals = false;
                        break;
                    }
                }
            }
        } else {
            argsEquals = false;
        }

        return cmd.equalsIgnoreCase(getName()) && argsEquals;
    }

    /**
     * Used to check if two strings equalsIgnoreCase
     * Special: "%" value : always true
     * @param s1 can be any string ("%")
     * @param s2 can be any string except ("%")
     * @return true if string equalsIgnoreCase
     */
    private boolean stringEquals(String s1, String s2) {
        if (!s1.equalsIgnoreCase("%")) {
            return s1.equalsIgnoreCase(s2);
        }
        return true;
    }

    /**
     * Used to execute the command
     * @param player player that executes the command
     * @param cmd name of the command
     * @param args string array of arguments
     */
    public abstract void execute(Player player, String cmd, String[] args);

    /**
     * Returns a help message normally used to send help if user doesn't know a command
     * @return help message
     */
    public abstract String getHelpMessage();

    public String getName() {
        return name;
    }
}
