package space.ourmc.userdb.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public final class ReportUrlAlerter {

    private ReportUrlAlerter() {}

    public static void register(Plugin plugin) {
        var protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                var packet = event.getPacket();
                var chat = packet.getChatComponents().read(0);
                var handle = chat.getHandle();
                var handleClass = handle.getClass();
                Field keyField;
                Field argsField;
                try {
                    keyField = handleClass.getDeclaredField("key");
                    argsField = handleClass.getDeclaredField("args");
                } catch (NoSuchFieldException e) {
                    try {
                        keyField = handleClass.getDeclaredField("f");
                        argsField = handleClass.getDeclaredField("g");
                    } catch (NoSuchFieldException noSuchFieldException) {
                        return;
                    }
                }
                keyField.setAccessible(true);
                argsField.setAccessible(true);
                String key;
                Object[] args;
                try {
                    key = (String) keyField.get(handle);
                    args = (Object[]) argsField.get(handle);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }
                if (!key.equals("commands.ban.success") || args.length != 2) {
                    return;
                }
                var uuid = event.getPlayer().getUniqueId();
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    var player = plugin.getServer().getPlayer(uuid);
                    if (player != null) {
                        player.sendMessage(ChatColor.GOLD + "Report to UserDb at: " + ChatColor.AQUA + ChatColor.UNDERLINE + "https://userdb.ourmc.space/report");
                    }
                });
            }
        });
    }

}
