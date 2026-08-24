package server;

import java.lang.management.OperatingSystemMXBean;

public class Server {
    private final OperatingSystemMXBean mxBean;

    public Server(OperatingSystemMXBean mxBean) {

        this.mxBean = mxBean;
    }
}
