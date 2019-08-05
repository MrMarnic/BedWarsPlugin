package me.marnic.bedwars.mechanics.event;

import me.marnic.bedwars.api.log.BedWarsLogger;
import me.marnic.bedwars.mechanics.builder.BedWarsBuilder;
import org.bukkit.block.data.type.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/*
 * Copyright (c) 03.08.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BedWarsEventHandler implements Listener {

    @EventHandler
    public void blockPlaced(BlockPlaceEvent e) {
        if (e.getBlock().getBlockData() instanceof Bed) {
            BedWarsBuilder.BedPlacer placer = BedWarsBuilder.INSTANCE.getPlacer();
            if (placer != null) {
                BedWarsBuilder.INSTANCE.setTeamBedPos(placer.getTeam().getChatColor().name(), e.getBlock().getLocation(), e.getBlock());
                BedWarsBuilder.INSTANCE.setPlacer(null);
                BedWarsLogger.log(e.getPlayer(), "Bed location of team " + placer.getTeam().getChatColor().name() + " was set");
            }
        }
    }
}
