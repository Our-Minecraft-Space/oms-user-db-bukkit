package space.ourmc.userdb.api;

import java.util.List;

public final class ReportsOfUser {

    public final User user;
    public final int count;
    public final List<Report> list;

    private ReportsOfUser() {
        this.user = null;
        this.count = 0;
        this.list = null;
    }

    public ReportsOfUser(User user, int count, List<Report> list) {
        this.user = user;
        this.count = count;
        this.list = list;
    }

}
