package space.ourmc.userdb.api;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public final class User {

    @JsonAdapter(UuidWithoutDashesAdapter.class) public final UUID uuid;
    @SerializedName("userName") public final String username;

    private User() {
        uuid = null;
        username = null;
    }

    public User(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

}
