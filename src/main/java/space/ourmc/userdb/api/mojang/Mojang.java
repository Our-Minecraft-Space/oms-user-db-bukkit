package space.ourmc.userdb.api.mojang;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import space.ourmc.userdb.api.OmsUserDb;
import space.ourmc.userdb.api.OmsUserDbException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Mojang {

    public static CompletableFuture<Optional<UUID>> getUuidFromUsername(String username) {
        if (!OmsUserDb.USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException("Illegal username");
        }
        final var client = HttpClient.newHttpClient();
        var usernameArray = new JsonArray();
        usernameArray.add(username);
        final var request = HttpRequest.newBuilder(URI.create("https://api.mojang.com/profiles/minecraft"))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(usernameArray.toString()))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    final var profiles = new Gson().fromJson(response.body(), Profile[].class);
                    if (profiles.length == 0) return Optional.empty();
                    return Optional.of(profiles[0].id);
                });
    }

}
