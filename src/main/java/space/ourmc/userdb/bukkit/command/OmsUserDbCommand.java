package space.ourmc.userdb.bukkit.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import space.ourmc.userdb.api.OmsUserDb;
import space.ourmc.userdb.api.OmsUserDbException;
import space.ourmc.userdb.api.Report;
import space.ourmc.userdb.api.UuidWithoutDashesAdapter;
import space.ourmc.userdb.api.mojang.Mojang;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public final class OmsUserDbCommand implements CommandExecutor  {

    public final Plugin plugin;

    public OmsUserDbCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /" + label + " [search <username>/get <UUID/username>]");
            return true;
        }
        if (args[0].equalsIgnoreCase("search")) {
            return search(sender, command, label, args);
        }
        if (args[0].equalsIgnoreCase("get")) {
            return get(sender, command, label, args);
        }
        sender.sendMessage("Usage: /" + label + " [search <username>/get <UUID/username>]");
        return true;
    }

    public boolean search(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Usage: /" + label + " search <username>");
            return true;
        }
        try {
            OmsUserDb.searchReports(args[1]).thenAccept(result -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (result.count == 0) {
                    sender.sendMessage(ChatColor.GREEN + "No results found for " + ChatColor.YELLOW + args[1]);
                    return;
                }
                sender.sendMessage(result.count + " result(s) found for " + ChatColor.YELLOW + args[1]);
                sender.sendMessage("================================================================");
                List<Report> list = result.list;
                if (list.size() > 10) {
                    list = list.subList(0, 10);
                }
                list.forEach(report -> {
                    sender.sendMessage(ChatColor.AQUA.toString() + report.reportDate.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace('T', ' ') + ChatColor.WHITE + " at " + ChatColor.GOLD + report.serverName + ChatColor.WHITE);
                    sender.sendMessage("         | Reason: " + ChatColor.YELLOW + report.specificReason);
                });
                if (result.count > 10) {
                    sender.sendMessage("And more...");
                }
                sender.sendMessage("More details at " + ChatColor.UNDERLINE + "https://userdb.ourmc.space/user/" + result.list.get(0).user.uuid.toString().replace("-", "").toLowerCase());
                sender.sendMessage("================================================================");
            })).exceptionally(throwable -> {
                if (throwable instanceof CompletionException && throwable.getCause() instanceof OmsUserDbException) {
                    OmsUserDbException e = (OmsUserDbException) throwable.getCause();
                    if (e.code == 404) {
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            sender.sendMessage(ChatColor.GREEN + "No results found for " + ChatColor.YELLOW + args[1]);
                        });
                    }
                }
                return null;
            });
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Illegal argument!");
        }
        return true;
    }

    public boolean get(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Usage: /" + label + " get <UUID/username>");
            return true;
        }
        CompletableFuture<Optional<UUID>> uuidFuture;
        try {
            if (args[1].contains("-")) {
                uuidFuture = CompletableFuture.completedFuture(Optional.of(UUID.fromString(args[1])));
            } else {
                uuidFuture = CompletableFuture.completedFuture(Optional.of(UuidWithoutDashesAdapter.uuidFromStringWithoutDashes(args[1])));
            }
        } catch (IllegalArgumentException e) {
            if (OmsUserDb.USERNAME_PATTERN.matcher(args[1]).matches()) {
                uuidFuture = Mojang.getUuidFromUsername(args[1]);
            } else {
                sender.sendMessage(ChatColor.RED + "Illegal UUID!");
                return true;
            }
        }
        uuidFuture.thenApply(optionalUuid -> {
            if (!optionalUuid.isPresent()) {
                sender.sendMessage(ChatColor.RED + "Player with that username doesn't exist!");
                return null;
            }
            final UUID uuid = optionalUuid.get();

            try {
                OmsUserDb.getReportsOfUser(uuid).thenAccept(result -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                    if (result.count == 0) {
                        sender.sendMessage(ChatColor.GREEN + "No results found for " + ChatColor.YELLOW + result.user.username);
                        return;
                    }
                    sender.sendMessage(ChatColor.GOLD.toString() + result.count + ChatColor.GREEN + " result(s) found for " + ChatColor.YELLOW + result.user.username);
                    sender.sendMessage("================================================================");
                    List<Report> list = result.list;
                    if (list.size() > 10) {
                        list = list.subList(0, 10);
                    }
                    list.forEach(report -> {
                        sender.sendMessage(ChatColor.AQUA.toString() + report.reportDate.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace('T', ' ') + ChatColor.WHITE + " at " + ChatColor.GOLD + report.serverName + ChatColor.WHITE);
                        sender.sendMessage("         | Reason: " + ChatColor.YELLOW + report.specificReason);
                    });
                    if (result.count > 10) {
                        sender.sendMessage("And more...");
                    }
                    sender.sendMessage("More details at " + ChatColor.UNDERLINE + "https://userdb.ourmc.space/user/" + uuid.toString().replace("-", "").toLowerCase());
                    sender.sendMessage("================================================================");
                })).exceptionally(throwable -> {
                    if (throwable instanceof CompletionException && throwable.getCause() instanceof OmsUserDbException) {
                        OmsUserDbException e = (OmsUserDbException) throwable.getCause();
                        if (e.code == 404) {
                            plugin.getServer().getScheduler().runTask(plugin, () -> {
                                sender.sendMessage(ChatColor.GREEN + "No results found for " + ChatColor.YELLOW + args[1]);
                            });
                        }
                    }
                    return null;
                });
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Illegal argument!");
            }
            return null;
        });
        return true;
    }


}
