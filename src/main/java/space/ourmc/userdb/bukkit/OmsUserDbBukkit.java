package space.ourmc.userdb.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import space.ourmc.userdb.bukkit.command.OmsUserDbCommand;

public final class OmsUserDbBukkit extends JavaPlugin  {

    @Override
    public void onEnable() {
        getCommand("userdb").setExecutor(new OmsUserDbCommand(this));
    }

}
