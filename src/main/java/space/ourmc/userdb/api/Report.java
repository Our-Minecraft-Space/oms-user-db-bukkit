package space.ourmc.userdb.api;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public final class Report {

    public final int id;
    public final User user;
    public final String serverName;
    public final String serverAddress;
    public final String serverCommunity;
    public final ReporterType reporterType;
    public final Reason reason;
    public final String specificReason;
    @JsonAdapter(InstantAdapter.class)
    public final Instant reportDate;
    public final List<String> files;

    private Report() {
        this.id = 0;
        this.user = null;
        this.serverName = null;
        this.serverAddress = null;
        this.serverCommunity = null;
        this.reporterType = null;
        this.reason = null;
        this.specificReason = null;
        this.reportDate = null;
        this.files = null;
    }

    public Report(int id, User user, String serverName, String serverAddress, String serverCommunity, ReporterType reporterType, Reason reason, String specificReason, Instant reportDate, List<String> files) {
        this.id = id;
        this.user = user;
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.serverCommunity = serverCommunity;
        this.reporterType = reporterType;
        this.reason = reason;
        this.specificReason = specificReason;
        this.reportDate = reportDate;
        this.files = files;
    }

    @JsonAdapter(ReporterType.ReporterTypeAdapter.class)
    public enum ReporterType {
        USER,
        OPERATOR,
        OWNER;

        public static final class ReporterTypeAdapter extends TypeAdapter<ReporterType> {
            @Override
            public void write(JsonWriter out, ReporterType value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                switch (value) {
                    case USER:
                        out.value(0);
                        break;
                    case OPERATOR:
                        out.value(1);
                        break;
                    case OWNER:
                        out.value(2);
                        break;
                }
            }

            @Override
            public ReporterType read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    return null;
                }
                switch (in.nextInt()) {
                    case 0:
                        return USER;
                    case 1:
                        return OPERATOR;
                    case 2:
                        return OWNER;
                    default:
                        throw new IllegalStateException("Unexpected value: " + in.nextInt());
                }
            }
        }
    }

    @JsonAdapter(Reason.ReasonTypeAdapter.class)
    public enum Reason {
        DISALLOWED_SOFTWARE,
        VANDALISM,
        FRAUD,
        HACKING,
        DISPUTE,
        SPEECH,
        ETC;

        public static final class ReasonTypeAdapter extends TypeAdapter<Reason> {
            @Override
            public void write(JsonWriter out, Reason value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                switch (value) {
                    case DISALLOWED_SOFTWARE:
                        out.value(0);
                        break;
                    case VANDALISM:
                        out.value(1);
                        break;
                    case FRAUD:
                        out.value(2);
                        break;
                    case HACKING:
                        out.value(3);
                        break;
                    case DISPUTE:
                        out.value(4);
                        break;
                    case SPEECH:
                        out.value(5);
                        break;
                    case ETC:
                        out.value(6);
                        break;
                }
            }

            @Override
            public Reason read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    return null;
                }
                switch (in.nextInt()) {
                    case 0:
                        return DISALLOWED_SOFTWARE;
                    case 1:
                        return VANDALISM;
                    case 2:
                        return FRAUD;
                    case 3:
                        return HACKING;
                    case 4:
                        return DISPUTE;
                    case 5:
                        return SPEECH;
                    case 6:
                        return ETC;
                    default:
                        throw new IllegalStateException("Unexpected value: " + in.nextInt());
                }
            }
        }
    }

}
