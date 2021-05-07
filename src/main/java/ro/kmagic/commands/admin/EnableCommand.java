package ro.kmagic.commands.admin;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.types.ModuleType;

import java.util.ArrayList;
import java.util.Collections;

public class EnableCommand implements CommandListener {

    private static SimpleConfig messages = Main.getMessages();

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        if(args[0] == null) {
            channel.sendMessage(messages.getString("GENERAL.not-enough-arguments")).queue();
            return;
        }

        SimpleConfig moduleConfig = Main.getConfigManager().getNewConfig(channel.getGuild().getId() + ".yml");

        Main.getCommands().forEach(module -> {
            if(module.getModuleName().equalsIgnoreCase(args[0])) {
                moduleConfig.set(module.getModuleType().toString() + "." + module.getModuleName(), true);
                moduleConfig.save();
                Main.reloadModules(channel.getGuild().getId(), channel.getId());
            }
        });
    }

    private boolean enabled;

    @Override
    public String getModuleName() {
        return "Enable";
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
        return new ArrayList<>(Collections.singleton(Permission.ADMINISTRATOR));
    }

    @Override
    public String getCommandDescription() {
        return "Enables a module";
    }
}
