package ro.kmagic;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import net.notfab.spigot.simpleconfig.SimpleConfigManager;
import net.notfab.spigot.simpleconfig.standalone.StandaloneConfigManager;
import ro.kmagic.commands.AnimeCommand;
import ro.kmagic.commands.PingCommand;
import ro.kmagic.commands.admin.DisableCommand;
import ro.kmagic.commands.admin.EnableCommand;
import ro.kmagic.commands.admin.ReloadCommand;
import ro.kmagic.listeners.MemberJoinListener;
import ro.kmagic.listeners.MemberLeaveListener;
import ro.kmagic.listeners.ReadyListener;
import ro.kmagic.modules.Module;
import ro.kmagic.modules.ModuleType;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static JDA jda;
    public static List<Module> commands;
    public static SimpleConfigManager configManager;
    public static SimpleConfigManager moduleLoader;
    public static SimpleConfig config;
    public static SimpleConfig moduleConfig;

    private static Module joinListener = new MemberJoinListener();
    private static Module leaveListener = new MemberLeaveListener();


    public static void main(String[] args) throws LoginException, InterruptedException {

        configManager = new StandaloneConfigManager(new File("C:\\Users\\PC\\IdeaProjects\\yuno\\src\\main\\resources\\"));
        config = configManager.getNewConfig("config.yml");

        JDABuilder jdaBuilder = JDABuilder.createDefault(config.getString("BOT.token"))
                .setActivity(Activity.of(Activity.ActivityType.valueOf(config.getString("BOT.activity")), config.getString("BOT.activityMessage")))
                .setStatus(OnlineStatus.valueOf(config.getString("BOT.status")))
                .addEventListeners(new ReadyListener(), joinListener, leaveListener);


        jda = jdaBuilder.build();


        jda.awaitReady();

        registerModules();

    }

    private static void registerModules() {
        commands = new ArrayList<>();

        // casual commands
        Module animeCommand = new AnimeCommand();
        commands.add(animeCommand);
        jda.addEventListener(animeCommand);

        Module pingCommand = new PingCommand();
        commands.add(pingCommand);
        jda.addEventListener(pingCommand);

        // admin commands
        Module reloadCommand = new ReloadCommand();
        commands.add(reloadCommand);
        jda.addEventListener(reloadCommand);

        Module disableCommand = new DisableCommand();
        commands.add(disableCommand);
        jda.addEventListener(disableCommand);

        Module enableCommand = new EnableCommand();
        commands.add(enableCommand);
        jda.addEventListener(enableCommand);

        // listeners
        commands.add(joinListener);
        jda.addEventListener(joinListener);

        commands.add(leaveListener);
        jda.addEventListener(leaveListener);

        for (Guild guild : jda.getGuilds()) {
            moduleConfig = configManager.getNewConfig("\\guilds\\" + guild.getId() + ".yml");

            for (ModuleType type : ModuleType.values()) {
                if (moduleConfig.getSection(type.toString()) == null) {
                    moduleConfig.createSection(type.toString());
                    moduleConfig.save();
                }

                for (Module module : commands) {
                    if (moduleConfig.get(module.getModuleType().toString() + "." + module.getModuleName()) == null) {
                        moduleConfig.set(module.getModuleType().toString() + "." + module.getModuleName(), true);
                        moduleConfig.save();
                    }
                    module.setEnabled(moduleConfig.getBoolean(module.getModuleType().toString() + "." + module.getModuleName()));
                }
            }
        }
    }

    public static boolean isEnabledForGuild(String id, Module module) {
        moduleConfig = configManager.getNewConfig("\\guilds\\" + jda.getGuildById(id).getId() + ".yml");


        return moduleConfig.getBoolean(module.getModuleType().toString() + "." + module.getModuleName());

    }

    public static void reloadModules(String guildID, String channelID) {
        List<Module> alreadyEnabled = new ArrayList<>();
        moduleConfig = configManager.getNewConfig("\\guilds\\" + jda.getGuildById(guildID).getId() + ".yml");

        for (Module module : commands) {
            if (module.isEnabled()) {
                alreadyEnabled.add(module);
            }
        }

        moduleConfig.reload();
        for (Module module : commands) {
            if (moduleConfig.getBoolean(module.getModuleType().toString() + "." + module.getModuleName()) == module.isEnabled()) {
                continue;
            }
            module.setEnabled(moduleConfig.getBoolean(module.getModuleType().toString() + "." + module.getModuleName()));
            if (module.isEnabled() && !alreadyEnabled.contains(module)) {
                jda.getTextChannelById(channelID).sendMessage("Yuno - Enabled " + module.getModuleType().toString().toLowerCase() + " module named: " + module.getModuleName()).queue();
            } else {
                jda.getTextChannelById(channelID).sendMessage("Yuno - Disabled " + module.getModuleType().toString().toLowerCase() + " module named: " + module.getModuleName()).queue();
            }
        }

    }

}
