package ro.kmagic.commands.admin;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.Command;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.types.Module;
import ro.kmagic.types.ModuleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModulesCommand implements CommandListener {

    private final SimpleConfig messages = Main.getMessages();

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        List<Module> modules = Main.getModules();
        List<Command> commands = Main.getCommands();
        StringBuilder string = new StringBuilder(String.format(messages.getString("COMMANDS.ADMIN.listmodules.modules-available"), (modules.size() + commands.size())) + "\n```\n");

        String enabled = messages.getString("GENERAL.enabled");
        String disabled = messages.getString("GENERAL.disabled");

        for(ModuleType type : ModuleType.values()) {
            string.append(type).append(":\n");

            for (Module module : modules) {
                if(module.getModuleType() != type) continue;
                string.append("   " + module.getModuleName() + ": " + (module.isEnabled() ? enabled : disabled)).append("\n");
            }

            for(Command module : Main.getCommands()) {
                if(module.getModuleType() != type) continue;
                string.append("   " + module.getModuleName() + ": " + (module.isEnabled() ? enabled : disabled)).append("\n");
            }

            string.append("\n");
        }

        string.append("```");

        channel.sendMessage(string.toString()).queue();


    }

    private boolean enabled;

    @Override
    public String getCommandAlias() {
        return "modules";
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return new ArrayList<>(Collections.singleton(Permission.ADMINISTRATOR));
    }

    @Override
    public String getCommandDescription() {
        return "Lists all modules";
    }

    @Override
    public String getModuleName() {
        return "ModulesList";
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
