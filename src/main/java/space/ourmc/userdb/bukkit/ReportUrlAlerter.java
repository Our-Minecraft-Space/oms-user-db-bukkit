package space.ourmc.userdb.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.UUID;

public final class ReportUrlAlerter {

    private ReportUrlAlerter() {}

    public static void register(Plugin plugin) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                WrappedChatComponent chat = packet.getChatComponents().read(0);
                JsonElement json = new JsonParser().parse(chat.getJson());
                if (!json.isJsonObject()) return;
                JsonObject object = (JsonObject) json;
                if (!object.has("translate")) return;
                if (!object.getAsJsonPrimitive("translate").getAsString().equals("commands.ban.success")) return;

                UUID uuid = event.getPlayer().getUniqueId();
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    Player player = plugin.getServer().getPlayer(uuid);
                    if (player != null) {
                        player.sendMessage(ChatColor.GOLD + "Report to UserDb at: " + ChatColor.AQUA + ChatColor.UNDERLINE + "https://userdb.ourmc.space/report");
                    }
                });
            }
        });
    }

}
