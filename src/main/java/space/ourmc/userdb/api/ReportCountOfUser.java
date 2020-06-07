package space.ourmc.userdb.api;

import java.util.List;

public final class ReportCountOfUser {

    public final User user;
    public final int count;

    private ReportCountOfUser() {
        this.user = null;
        this.count = 0;
    }

    public ReportCountOfUser(User user, int count) {
        this.user = user;
        this.count = count;
    }

}
