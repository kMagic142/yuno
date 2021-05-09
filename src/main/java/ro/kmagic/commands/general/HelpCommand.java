package ro.kmagic.commands.general;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.Command;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.types.ModuleType;
import ro.kmagic.utils.Utils;
import ro.kmagic.utils.pagination.HelpPaginator;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class HelpCommand implements CommandListener {

    private final HelpPaginator paginator;
    private final HashMap<ModuleType, HashMap<String, String>> fields;

    public HelpCommand() {
        this.fields = new HashMap<>();
        Consumer<Message> finalAction = m -> {
            try {
                m.clearReactions().queue();
            } catch (InsufficientPermissionException err) {
                m.delete().queue();
            }
        };

        int timeout = 1;
        TimeUnit timeunit = TimeUnit.MINUTES;

        paginator = new HelpPaginator(5, finalAction, timeout, timeunit);
    }

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        int page = 1;

        if(args.length >= 1) {
            page = Utils.parseInt(args[0], 0);
        }

        for (ModuleType type : ModuleType.values()) {
            HashMap<String, String> cmdFields = new HashMap<>();

            for (Command c : Main.getCommands()) {
                if(c.getModuleType() != type) continue;
                if (!c.isEnabled()) continue;
                if (c.getCommandPermissions() != null && !sender.hasPermission(c.getCommandPermissions().get(0)))
                    continue;

                cmdFields.put(Utils.PREFIX + c.getCommandName().toLowerCase(),
                        c.getCommandDescription() + (c.getCommandAlias() != null ? "\nAlias: " + c.getCommandAlias() : ""));
            }

            if(cmdFields.size() >= 1)
                fields.put(type, cmdFields);

        }

        paginator.setAuthor("Yuno - Available Commands", null, Main.getJDA().getSelfUser().getAvatarUrl());
        paginator.setHelpFields(fields);
        paginator.setColor(Color.BLACK);
        paginator.setFooter("Use " + Utils.PREFIX + "help [page] to start at a specific page");

        paginator.paginate(channel, page);

    }

    private boolean enabled;

    @Override
    public String getCommandAlias() {
        return "commands";
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return "Show all available commands";
    }

    @Override
    public String getModuleName() {
        return "Help";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.GENERAL;
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
