package ro.kmagic.commands.admin;

import io.sentry.Sentry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.types.ModuleType;
import ro.kmagic.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HardwareInfoCommand implements CommandListener {
    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        EmbedBuilder builder = new EmbedBuilder();

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long freeMemory = runtime.freeMemory();

        com.sun.management.OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        builder.addField(":bar_chart: CPU Usage",
                "Bot \u00BB " + decimalFormat.format(bean.getProcessCpuLoad() * 100) + "%" + "\n" +
                        "System \u00BB " + decimalFormat.format(bean.getSystemCpuLoad() * 100) + "%", false);

        builder.addField(":bar_chart: RAM Memory Usage",
                "Bot \u00BB " + Utils.humanReadableByteCount(freeMemory) + "/" + Utils.humanReadableByteCount(maxMemory) + " (" + decimalFormat.format(freeMemory * 1.0 / maxMemory * 100) + "%)\n" +
                        "System \u00BB " + Utils.humanReadableByteCount(bean.getTotalPhysicalMemorySize() - bean.getFreePhysicalMemorySize()) + "/" + Utils.humanReadableByteCount(bean.getTotalPhysicalMemorySize()) + " (" + decimalFormat.format((bean.getTotalPhysicalMemorySize() - bean.getFreePhysicalMemorySize()) * 1.0 / bean.getTotalPhysicalMemorySize() * 100) + "%)",
                false);

        File serverFolder = new File(new File("./data/").getParentFile().getAbsolutePath()).getParentFile();
        long size = 0;
        try {
            size = Files.walk(serverFolder.toPath()).mapToLong(p -> p.toFile().length()).sum();
        } catch (IOException e) {
            Sentry.captureException(e);
        }

        long totalSpace = serverFolder.getTotalSpace();
        long used = serverFolder.getFreeSpace();
        builder.addField(":bar_chart: Physical Storage Usage",
                "Occupies \u00BB " + Utils.humanReadableByteCount(size) + "\n" +
                        "Disk \u00BB " + Utils.humanReadableByteCount(totalSpace - used) + "/" + Utils.humanReadableByteCount(totalSpace) + " (" + decimalFormat.format((totalSpace - used) * 1.0 / totalSpace * 100) + "%)", false);

        double nodeCpu = Main.getLavalink().getNodes().get(0).getStats().getLavalinkLoad();
        double systemCpu = Main.getLavalink().getNodes().get(0).getStats().getSystemLoad();
        int cpuCores = Main.getLavalink().getNodes().get(0).getStats().getCpuCores();
        long allocatedMemory = Main.getLavalink().getNodes().get(0).getStats().getMemAllocated();
        long freeMem = Main.getLavalink().getNodes().get(0).getStats().getMemFree();
        long uptime = Main.getLavalink().getNodes().get(0).getStats().getUptime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(uptime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateString = sdf.format(calendar.getTime());

        builder.addField(":bar_chart: Lavalink Stats",
                "Node \u00BB " + decimalFormat.format(nodeCpu) + "%\n" +
                "System \u00BB " + decimalFormat.format(systemCpu) + "%\n" +
                "CPU Cores \u00BB " + cpuCores + "\n" +
                "Memory \u00BB " + Utils.humanReadableByteCount(freeMem) + "/" + Utils.humanReadableByteCount(allocatedMemory) +
                        " (" + decimalFormat.format(freeMem * 1.0 / allocatedMemory * 100) + "%)\n" +
                "Uptime \u00BB " + dateString, false);

        channel.sendMessage(builder.build()).queue();
    }

    private boolean enabled;

    @Override
    public String getCommandAlias() {
        return "hwinfo";
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return "See hardware information.";
    }

    @Override
    public String getModuleName() {
        return "hardware info";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.ADMIN;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean bool) {
        enabled = bool;
    }
}
