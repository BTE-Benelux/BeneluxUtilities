package be.kyanvde.beneluxutilities.commands;

import be.kyanvde.beneluxutilities.api.OpenStreetMapAPI;
import be.kyanvde.beneluxutilities.conversion.CoordinateConversion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GoToCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            Bukkit.getLogger().info("This command can only be executed by a Player!");
            return true;
        }

        Player player = (Player) sender;

        if(args.length < 1) {
            player.sendMessage("Please provide an adress.");
            return false;
        }

        String address = String.join(" ", args);

        OpenStreetMapAPI.getCoordinatesFromAdress(address).whenComplete((geoCoordinates, throwable) -> {
            if(throwable != null) {
                player.sendMessage("Something went wrong while converting the address to coordinates!");
                return;
            }

            double[] coordinates = CoordinateConversion.convertFromGeo(geoCoordinates[0], geoCoordinates[1]);

            Location newLocation = player.getLocation();
            newLocation.setX(coordinates[0]);
            newLocation.setZ(coordinates[1]);

            World world = newLocation.getWorld();
            double newY = world.getHighestBlockYAt(newLocation.getBlockX(), newLocation.getBlockZ());
            newLocation.setY(newY);

            player.teleportAsync(newLocation);
            player.sendMessage(String.format("You successfully teleported to %s!", address));
            return;
        });
        return true;
    }
}
