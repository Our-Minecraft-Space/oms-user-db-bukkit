package space.ourmc.userdb.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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
        return CompletableFuture.supplyAsync(() -> {
            URLConnection connection;
            try {
                connection = new URL(ENDPOINT + "report/all").openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            try (final InputStream inputStream = connection.getInputStream()) {
                final InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                final JsonObject jsonResponse = new JsonParser().parse(reader).getAsJsonObject();
                if (!jsonResponse.get("success").getAsBoolean()) {
                    throw new OmsUserDbException(jsonResponse.get("code").getAsInt(), jsonResponse.get("reason").getAsString());
                }
                final JsonObject value = jsonResponse.getAsJsonObject("value");
                return new Gson().fromJson(value, Reports.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static CompletableFuture<Reports> searchReports(String username) {
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException("Illegal username");
        }
        return CompletableFuture.supplyAsync(() -> {
            URLConnection connection;
            try {
                connection = new URL(ENDPOINT + "report/search/" + username).openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            try (final InputStream inputStream = connection.getInputStream()) {
                final InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                final JsonObject jsonResponse = new JsonParser().parse(reader).getAsJsonObject();
                if (!jsonResponse.get("success").getAsBoolean()) {
                    throw new OmsUserDbException(jsonResponse.get("code").getAsInt(), jsonResponse.get("reason").getAsString());
                }

                final JsonObject value = jsonResponse.getAsJsonObject("value");
                return new Gson().fromJson(value, Reports.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static CompletableFuture<ReportCountOfUser> getReportCountOfUser(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            URLConnection connection;
            try {
                connection = new URL(ENDPOINT + "report/reportcount/" + uuid.toString().replace("-", "").toLowerCase(Locale.ENGLISH)).openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            try (final InputStream inputStream = connection.getInputStream()) {
                final InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                final JsonObject jsonResponse = new JsonParser().parse(reader).getAsJsonObject();
                if (!jsonResponse.get("success").getAsBoolean()) {
                    throw new OmsUserDbException(jsonResponse.get("code").getAsInt(), jsonResponse.get("reason").getAsString());
                }

                final JsonObject value = jsonResponse.getAsJsonObject("value");
                return new Gson().fromJson(value, ReportCountOfUser.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static CompletableFuture<ReportsOfUser> getReportsOfUser(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            URLConnection connection;
            try {
                connection = new URL(ENDPOINT + "report/user/" + uuid.toString().replace("-", "").toLowerCase(Locale.ENGLISH)).openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            try (final InputStream inputStream = connection.getInputStream()) {
                final InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                final JsonObject jsonResponse = new JsonParser().parse(reader).getAsJsonObject();
                if (!jsonResponse.get("success").getAsBoolean()) {
                    throw new OmsUserDbException(jsonResponse.get("code").getAsInt(), jsonResponse.get("reason").getAsString());
                }

                final JsonObject value = jsonResponse.getAsJsonObject("value");
                return new Gson().fromJson(value, ReportsOfUser.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
