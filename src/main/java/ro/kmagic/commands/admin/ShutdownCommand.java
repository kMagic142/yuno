package ro.kmagic.commands.admin;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.types.ModuleType;
import ro.kmagic.users.UserManager;

import java.util.ArrayList;

public class ShutdownCommand implements CommandListener {
    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        channel.sendMessage("Yuno is now shutting down...").queue();
        UserManager.saveAllUsers();
        Main.getJDA().shutdown();
        Runtime.getRuntime().exit(1);
    }

    private boolean enabled;

    @Override
    public String getCommandAlias() {
        return "die";
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return "Shut down the bot.";
    }

    @Override
    public String getModuleName() {
        return "shutdown";
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
