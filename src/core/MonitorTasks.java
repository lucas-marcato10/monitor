package core;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class MonitorTasks {
    private final OperatingSystemMXBean osBean;

    public MonitorTasks() {
        this.osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    }

    public String collectCpu() {
        double usoCpu = osBean.getCpuLoad() * 100;
        if (usoCpu < 0) usoCpu = 0.0;
        return String.format("[Monitor] Uso de CPU: %.2f%%", usoCpu);
    }

    public String collectRam() {
        long totalMemOS = osBean.getTotalMemorySize() / (1024 * 1024);
        long freeMemOS = osBean.getFreeMemorySize() / (1024 * 1024);
        long usedMemOS = totalMemOS - freeMemOS;
        return String.format("[Monitor] Memoria OS Usada: %d MB / Total: %d MB", usedMemOS, totalMemOS);
    }
}