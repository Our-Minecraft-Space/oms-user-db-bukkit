package space.ourmc.userdb.api.mojang;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import space.ourmc.userdb.api.OmsUserDb;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class Mojang {

    private Mojang() {}

    public static CompletableFuture<Optional<UUID>> getUuidFromUsername(String username) {
        if (!OmsUserDb.USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException("Illegal username");
        }
        final JsonArray usernameArray = new JsonArray();
        usernameArray.add(username);

        return CompletableFuture.supplyAsync(() -> {
            URLConnection connection;
            try {
                connection = new URL("https://api.mojang.com/profiles/minecraft").openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            final byte[] content = usernameArray.toString().getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Length", Integer.toString(content.length));
            connection.setDoOutput(true);
            try (final OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (final InputStream inputStream = connection.getInputStream()) {
                final InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                final Profile[] profiles = new Gson().fromJson(reader, Profile[].class);
                if (profiles.length == 0) return Optional.empty();
                return Optional.of(profiles[0].id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
