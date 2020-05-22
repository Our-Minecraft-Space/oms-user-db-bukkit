package space.ourmc.userdb.api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

public final class UuidWithoutDashesAdapter extends TypeAdapter<UUID> {

    @Override
    public void write(JsonWriter out, UUID value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.toString().replace("-", ""));
    }

    @Override
    public UUID read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return uuidFromStringWithoutDashes(in.nextString());
    }

    public static UUID uuidFromStringWithoutDashes(String s) {
        if (s.length() != 32) {
            throw new IllegalArgumentException("Illegal UUID");
        }
        return UUID.fromString(s.substring(0, 8) + '-' + s.substring(8, 12) + '-' + s.substring(12, 16) + '-' + s.substring(16, 20) + '-' + s.substring(20, 32));
    }

}
