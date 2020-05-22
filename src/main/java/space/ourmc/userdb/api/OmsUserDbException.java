package space.ourmc.userdb.api;

public final class OmsUserDbException extends RuntimeException {

    public final int code;
    public final String reason;

    public OmsUserDbException(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

}
