module space.ourmc.userdb.bukkit {
    requires org.bukkit;
    requires com.google.gson;
    requires java.net.http;
    exports space.ourmc.userdb.api;
    exports space.ourmc.userdb.bukkit;
    exports space.ourmc.userdb.bukkit.command;
}

