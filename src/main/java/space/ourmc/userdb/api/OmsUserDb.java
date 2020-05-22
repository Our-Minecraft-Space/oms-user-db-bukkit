package space.ourmc.userdb.api;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public final class OmsUserDb {

    private OmsUserDb() {}

    public static final String ENDPOINT = "https://userdb.ourmc.space/api/v1/";
    public static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9_]{3,16}");

    public static Future<Reports> getAllReports() {
        final var client = HttpClient.newHttpClient();
        final var request = HttpRequest.newBuilder(URI.create(ENDPOINT + "report/all"))
                .timeout(Duration.ofSeconds(15))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    final var jsonResponse = new JsonParser().parse(response.body()).getAsJsonObject();
                    if (!jsonResponse.get("success").getAsBoolean()) {
                        throw new OmsUserDbException(jsonResponse.get("code").getAsInt(), jsonResponse.get("reason").getAsString());
                    }

                    final var value = jsonResponse.getAsJsonObject("value");
                    return new Gson().fromJson(value, Reports.class);
                });
    }

    public static CompletableFuture<Reports> getReportsOfUsername(String username) {
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException("Illegal username");
        }
        final var client = HttpClient.newHttpClient();
        final var request = HttpRequest.newBuilder(URI.create(ENDPOINT + "report/search/" + username))
                .timeout(Duration.ofSeconds(15))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    final var jsonResponse = new JsonParser().parse(response.body()).getAsJsonObject();
                    if (!jsonResponse.get("success").getAsBoolean()) {
                        throw new OmsUserDbException(jsonResponse.get("code").getAsInt(), jsonResponse.get("reason").getAsString());
                    }

                    final var value = jsonResponse.getAsJsonObject("value");
                    return new Gson().fromJson(value, Reports.class);
                });
    }

    public static CompletableFuture<ReportsOfUser> getReportsOfUser(UUID uuid) {
        final var client = HttpClient.newHttpClient();
        final var request = HttpRequest.newBuilder(URI.create(ENDPOINT + "report/user/" + uuid.toString().replace("-", "").toLowerCase(Locale.ENGLISH)))
                .timeout(Duration.ofSeconds(15))
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    final var jsonResponse = new JsonParser().parse(response.body()).getAsJsonObject();
                    if (!jsonResponse.get("success").getAsBoolean()) {
                        throw new OmsUserDbException(jsonResponse.get("code").getAsInt(), jsonResponse.get("reason").getAsString());
                    }

                    final var value = jsonResponse.getAsJsonObject("value");
                    return new Gson().fromJson(value, ReportsOfUser.class);
                });
    }

}
