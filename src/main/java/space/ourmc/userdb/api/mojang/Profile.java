package space.ourmc.userdb.api.mojang;

import com.google.gson.annotations.JsonAdapter;
import space.ourmc.userdb.api.UuidWithoutDashesAdapter;

import java.util.UUID;

public final class Profile {

    @JsonAdapter(UuidWithoutDashesAdapter.class) public final UUID id;
    public final String name;
    public final boolean legacy;
    public final boolean demo;

    private Profile() {
        id = null;
        name = null;
        legacy = false;
        demo = false;
    }

    public Profile(UUID id, String name, boolean legacy, boolean demo) {
        this.id = id;
        this.name = name;
        this.legacy = legacy;
        this.demo = demo;
    }
}
