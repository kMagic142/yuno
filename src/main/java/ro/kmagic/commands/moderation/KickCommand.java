package ro.kmagic.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.types.ModuleType;

import java.util.ArrayList;

public class KickCommand implements CommandListener {
    @Override
    public String getModuleName() {
        return "Kick";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.MODERATION;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean bool) {}

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        // nmc
    }

    @Override
    public String getCommandAlias() {
        return null;
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return "Kicks a user from a guild";
    }
}
