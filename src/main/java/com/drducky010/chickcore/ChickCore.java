package com.drducky010.chickcore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChickCore extends JavaPlugin implements Listener, CommandExecutor {

    @Override
    public void onEnable() {
        getLogger().info("ChickCore has been enabled successfully!");
        
        // Register events and commands
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("chick").setExecutor(this);
        getCommand("offend").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("ChickCore has been disabled.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.YELLOW + "Welcome to ChickMC, " + player.getName() + "!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("chick")) {
            sender.sendMessage(ChatColor.AQUA + "You are running ChickMC - powered by custom code!");
            return true;
        }

        if (command.getName().equalsIgnoreCase("offend")) {
            if (!sender.hasPermission("chick.offend")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }

            // Usage: /offend <player> <duration> [reason...]
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /offend <player> <duration> [reason]");
                sender.sendMessage(ChatColor.GRAY + "Valid durations: 1s, 1m, 10m, 30m, 1hr, 2hr... up to 9hr, 10hr, or perm");
                return true;
            }

            String targetName = args[0];
            String duration = args[1].toLowerCase();

            // Build reason string from remaining arguments
            StringBuilder reasonBuilder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                reasonBuilder.append(args[i]).append(" ");
            }
            String reason = reasonBuilder.toString().trim();
            if (reason.isEmpty()) {
                reason = "No reason specified";
            }

            // Validate duration format
            if (!isValidDuration(duration)) {
                sender.sendMessage(ChatColor.RED + "Invalid duration! Use 1s, 1m, 10m, 30m, 1hr through 10hr, or perm.");
                return true;
            }

            // Action feedback
            sender.sendMessage(ChatColor.GREEN + "Successfully processed offense for " + targetName + 
                    " | Duration: " + duration + " | Reason: " + reason);

            Player target = Bukkit.getPlayerExact(targetName);
            if (target != null) {
                target.sendMessage(ChatColor.RED + "You have been penalized. Duration: " + duration + ". Reason: " + reason);
            }

            return true;
        }

        return false;
    }

    private boolean isValidDuration(String duration) {
        if (duration.equals("perm")) {
            return true;
        }
        // Matches seconds (s), minutes (m), or hours (hr) with flexible numbers like 1-10hr
        return duration.matches("^\\d+(s|m|hr)$");
    }
}