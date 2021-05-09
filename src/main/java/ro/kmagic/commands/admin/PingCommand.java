package ro.kmagic.commands.admin;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.types.ModuleType;

import java.util.ArrayList;

public class PingCommand implements CommandListener {

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        long ping = message.getJDA().getRestPing().complete();

        channel.sendMessage("Bot's ping is **" + ping + "ms**.").queue();
    }

    private boolean enabled;

    @Override
    public String getModuleName() {
        return "Ping";
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
        this.enabled = bool;
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
        return "Pong!";
    }
}
