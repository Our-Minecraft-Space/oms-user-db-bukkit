package space.ourmc.userdb.api;

import java.util.List;

public final class Reports {

    public final int count;
    public final List<Report> list;

    private Reports() {
        this.count = 0;
        this.list = null;
    }

    public Reports(int count, List<Report> list) {
        this.count = count;
        this.list = list;
    }

}
