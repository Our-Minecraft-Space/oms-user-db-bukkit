package space.ourmc.userdb.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import space.ourmc.userdb.api.OmsUserDb;
import space.ourmc.userdb.api.UuidWithoutDashesAdapter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class OmsUserDbCommand implements CommandExecutor  {

    public final Plugin plugin;

    public OmsUserDbCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /" + label + " search <\"username\"/\"uuid\"> <username or uuid>");
            return true;
        }
        if (args[0].equalsIgnoreCase("search")) {
            return search(sender, command, label, args);
        }
        sender.sendMessage("Usage: /" + label + " search <\"username\"/\"uuid\"> <username or uuid>");
        return true;
    }

    public boolean search(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("Usage: /" + label + " search <\"username\"/\"uuid\"> <username or uuid>");
            return true;
        }
        if (args[1].equalsIgnoreCase("username")) {
            try {
                OmsUserDb.getReportsOfUsername(args[2]).thenAccept(result -> plugin.getServer().getScheduler().runTask(plugin, () -> {
                    System.out.println(result.list.size());
                    if (result.count == 0) {
                        sender.sendMessage(ChatColor.GREEN + "No results found for " + ChatColor.YELLOW + args[2]);
                        return;
                    }
                    sender.sendMessage(result.count + " result(s) found for " + ChatColor.YELLOW + args[2]);
                    sender.sendMessage("=======================================");
                    var list = result.list;
                    if (list.size() > 10) {
                        list = list.subList(0, 10);
                    }
                    list.forEach(report -> {
                        sender.sendMessage(ChatColor.AQUA.toString() + report.reportDate.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace('T', ' ') + ChatColor.WHITE + " at " + ChatColor.GOLD + report.serverName + ChatColor.WHITE);
                        sender.sendMessage("         | Reason: " + ChatColor.YELLOW + report.specificReason);
                    });
                    sender.sendMessage("More details at https://userdb.ourmc.space/user/" + result.list.get(0).user.uuid.toString().replace("-", "").toLowerCase());
                    sender.sendMessage("=======================================");
                })).exceptionally(throwable -> {
                    System.out.println(throwable);
                    return null;
                });
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Illegal argument!");
            }
            return true;
        } else if (args[1].equalsIgnoreCase("uuid")) {
            UUID uuidTmp;
            try {
                if (args[2].contains("-")) {
                    uuidTmp = UUID.fromString(args[2]);
                } else {
                    uuidTmp = UuidWithoutDashesAdapter.uuidFromStringWithoutDashes(args[2]);
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Illegal UUID!");
                return true;
            }
            final var uuid = uuidTmp;
            try {
                OmsUserDb.getReportsOfUser(uuid).thenAccept(result -> {
                    plugin.getServer().getScheduler().runTask(plugin, () -> {
                        if (result.count == 0) {
                            sender.sendMessage(ChatColor.GREEN + "No results found for " + ChatColor.YELLOW + result.user.username);
                            return;
                        }
                        sender.sendMessage(ChatColor.GOLD.toString() + result.count + ChatColor.GREEN + " result(s) found for " + ChatColor.YELLOW + result.user.username);
                        sender.sendMessage("=======================================");
                        var list = result.list;
                        if (list.size() > 10) {
                            list = list.subList(0, 10);
                        }
                        list.subList(0, 10).forEach(report -> {
                            sender.sendMessage(ChatColor.AQUA.toString() + report.reportDate.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace('T', ' ') + ChatColor.WHITE + " at " + ChatColor.GOLD + report.serverName + ChatColor.WHITE);
                            sender.sendMessage("         | Reason: " + ChatColor.YELLOW + report.specificReason);
                        });
                        sender.sendMessage("More details at https://userdb.ourmc.space/user/" + uuid.toString().replace("-", "").toLowerCase());
                        sender.sendMessage("=======================================");
                    });
                });
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Illegal argument!");
            }
            return true;
        }
        sender.sendMessage("Usage: /" + label + " search <\"username\"/\"uuid\"> <username or uuid>");
        return true;
    }

}
