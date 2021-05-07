package ro.kmagic;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.sentry.Sentry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import net.notfab.spigot.simpleconfig.SimpleConfigManager;
import net.notfab.spigot.simpleconfig.standalone.StandaloneConfigManager;
import org.reflections.Reflections;
import ro.kmagic.events.ReadyEvent;
import ro.kmagic.handlers.commands.*;
import ro.kmagic.handlers.data.DBConnection;
import ro.kmagic.handlers.data.DBManager;
import ro.kmagic.handlers.data.files.ConfigFile;
import ro.kmagic.handlers.data.files.MessagesFile;
import ro.kmagic.types.DataType;
import ro.kmagic.types.Module;
import ro.kmagic.types.ModuleType;
import ro.kmagic.users.SaveManager;
import ro.kmagic.users.UserManager;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {
    // bot
    private static JDA jda;
    private static CommandHandler commandHandler;
    private static final List<Module> modules = new ArrayList<>();
    // config
    private static ConfigFile config;
    private static MessagesFile messages;
    private static SimpleConfig moduleConfig;
    private static final SimpleConfigManager configManager = new StandaloneConfigManager(new File( getDataFolder() + "/modules"));
    // db
    private static DBManager dbManager;
    // thread scheduler
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    private static final EventWaiter waiter = new EventWaiter(scheduler, false);


    public static void main(String[] args) throws LoginException, InterruptedException, InstantiationException, IllegalAccessException {
        Sentry.init(options -> {
            options.setDsn("https://d2fa64e086c545629529135460d1a8d8@o625146.ingest.sentry.io/5753695");
        });

        initData();

        JDABuilder jdaBuilder = JDABuilder.createDefault(getConfig().getString("BOT.token"))
                .setActivity(Activity.of(Activity.ActivityType.valueOf(getConfig().getString("BOT.activity")), getConfig().getString("BOT.activityMessage")))
                .setStatus(OnlineStatus.valueOf(getConfig().getString("BOT.status")))
                .addEventListeners(new ReadyEvent())
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));


        jda = jdaBuilder.build();
        jda.awaitReady();

        registerModules();

    }

    private static void registerModules() throws InstantiationException, IllegalAccessException {
        commandHandler = new CommandHandlerBuilder(jda)
                .setPrefix(getConfig().getString("BOT.prefix"))
                .build();

        // commands initialization
        Reflections cmdPackage = new Reflections("ro.kmagic.commands");
        Set<Class<? extends CommandListener>> commands = cmdPackage.getSubTypesOf(CommandListener.class);

        for (Class<?> cmd : commands) {
            commandHandler.addCommand(new CommandBuilder((CommandListener) cmd.newInstance()).build());
        }

        // listeners initialization
        Reflections listenersPackage = new Reflections("ro.kmagic.events.listeners");
        Set<Class<? extends Module>> listeners = listenersPackage.getSubTypesOf(Module.class);

        for (Class<?> cmd : listeners) {
            Module listener = (Module) cmd.newInstance();
            modules.add(listener);
            jda.addEventListener(listener, getWaiter());
        }

        // command, listener and user data initialization
        for (Guild guild : jda.getGuilds()) {
            moduleConfig = configManager.getNewConfig(guild.getId() + ".yml");

            for (ModuleType type : ModuleType.values()) {
                if (moduleConfig.getSection(type.toString()) == null) {
                    moduleConfig.createSection(type.toString());
                }

                for (Command module : commandHandler.getCommands()) {
                    if (moduleConfig.get(module.getModuleType().toString() + "." + module.getModuleName()) == null) {
                        moduleConfig.set(module.getModuleType().toString() + "." + module.getModuleName(), true);
                        moduleConfig.save();
                    }
                    module.setEnabled(moduleConfig.getBoolean(module.getModuleType().toString() + "." + module.getModuleName()));
                }

                for (Module module : modules) {
                    if (moduleConfig.get(module.getModuleType().toString() + "." + module.getModuleName()) == null) {
                        moduleConfig.set(module.getModuleType().toString() + "." + module.getModuleName(), true);
                        moduleConfig.save();
                    }

                    module.setEnabled(moduleConfig.getBoolean(module.getModuleType().toString() + "." + module.getModuleName()));
                }
            }

            UserManager.loadUsers(jda);

        }
    }

    public static boolean isEnabledForGuild(String id, Module module) {
        moduleConfig = configManager.getNewConfig(jda.getGuildById(id).getId() + ".yml");
        return !moduleConfig.getBoolean(module.getModuleType().toString() + "." + module.getModuleName());

    }

    public static Boolean reloadModules(String guildID, String channelID) {
        List<Command> alreadyEnabledCommands = new ArrayList<>();
        List<Module> alreadyEnabledModules = new ArrayList<>();
        moduleConfig = configManager.getNewConfig(guildID + ".yml");

        for (Command module : commandHandler.getCommands()) {
            if (module.isEnabled()) {
                alreadyEnabledCommands.add(module);
            }
        }

        for (Module module : modules) {
            if (module.isEnabled()) {
                alreadyEnabledModules.add(module);
            }
        }

        for (Module module : modules) {
            if (moduleConfig.getBoolean(module.getModuleType().toString() + "." + module.getModuleName()) == module.isEnabled()) {
                if ((module.getModuleType().equals(ModuleType.EVENT)) && module.isEnabled()) {
                    module.setEnabled(false);
                    module.setEnabled(true);
                }
            }
        }

        moduleConfig.reload();
        for (Command module : commandHandler.getCommands()) {
            module.setEnabled(moduleConfig.getBoolean(module.getModuleType().toString() + "." + module.getModuleName()));
            if (module.isEnabled() && !alreadyEnabledCommands.contains(module)) {
                jda.getTextChannelById(channelID).sendMessage("Yuno - Enabled " + module.getModuleType().toString().toLowerCase() + " module named: " + module.getModuleName()).queue();
            } else if(!module.isEnabled() && alreadyEnabledCommands.contains(module)) {
                jda.getTextChannelById(channelID).sendMessage("Yuno - Disabled " + module.getModuleType().toString().toLowerCase() + " module named: " + module.getModuleName()).queue();
            }
        }

        for (Module module : modules) {
            module.setEnabled(moduleConfig.getBoolean(module.getModuleType().toString() + "." + module.getModuleName()));
            if (module.isEnabled() && !alreadyEnabledModules.contains(module)) {
                jda.getTextChannelById(channelID).sendMessage("Yuno - Enabled " + module.getModuleType().toString().toLowerCase() + " module named: " + module.getModuleName()).queue();
            } else if(!module.isEnabled() && alreadyEnabledModules.contains(module)) {
                jda.getTextChannelById(channelID).sendMessage("Yuno - Disabled " + module.getModuleType().toString().toLowerCase() + " module named: " + module.getModuleName()).queue();
            }
        }

        return true;
    }

    public static ScheduledFuture<?> scheduleRepeat(Runnable task, long startDelay, long repeatDelay) {
        return scheduler.scheduleWithFixedDelay(task, startDelay, repeatDelay, TimeUnit.MILLISECONDS);
    }

    public static void initData() {
        config = new ConfigFile();
        messages = new MessagesFile();

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/fonts/JosefinSans-Bold.ttf")).deriveFont(12f));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/fonts/JosefinSans-Regular.ttf")).deriveFont(12f));
        } catch (IOException | FontFormatException e) {
            Sentry.captureException(e);
        }

        switch(getConfig().getString("DATABASE.type").toLowerCase()) {
            case "sqlite":
                dbManager = new DBManager(new DBConnection(DataType.SQLite));
                dbManager.createTable("yuno_users", new String[]{"id", "experience", "level", "coins"});
                break;
            case "mysql":
                dbManager = new DBManager(new DBConnection(DataType.MySQL,
                        getConfig().getString("database.host"),
                        getConfig().getString("database.database"),
                        getConfig().getString("database.username"),
                        getConfig().getString("database.password"),
                        getConfig().getInt("database.port")
                ));

                dbManager.createTable("yuno_users", new String[]{"id", "experience", "level", "coins"});
                break;
        }

        new SaveManager();
    }

    public static SimpleConfig getConfig() {
        return config.getData();
    }

    public static SimpleConfig getMessages() {
        return messages.getData();
    }

    public static JDA getJDA() {
        return jda;
    }

    public static SimpleConfigManager getConfigManager() {
        return configManager;
    }

    public static ArrayList<Command> getCommands() {
        return commandHandler.getCommands();
    }

    public static DBManager getDBManager() {
        return dbManager;
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static String getDataFolder() {
        return "./data/";
    }

    public static ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public static EventWaiter getWaiter() {
        return waiter;
    }


}
