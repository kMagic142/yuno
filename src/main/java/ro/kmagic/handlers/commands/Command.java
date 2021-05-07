package ro.kmagic.handlers.commands;

import net.dv8tion.jda.api.Permission;
import ro.kmagic.types.ModuleType;

import java.util.ArrayList;

public class Command {

    private final CommandBuilder commandBuilder;

    public Command(CommandBuilder commandBuilder) {
        this.commandBuilder = commandBuilder;
    }

    public String getCommandName() {
        return commandBuilder.commandName;
    }

    public String getCommandAlias() {
        return commandBuilder.commandAlias;
    }

    public String getCommandDescription() {
        return commandBuilder.commandDescription;
    }

    public ArrayList<Permission> getCommandPermissions() {
        return commandBuilder.commandPermissions;
    }

    public CommandListener getHandlerListener() {
        return commandBuilder.handlerListener;
    }

    public ModuleType getModuleType() {
        return this.getHandlerListener().getModuleType();
    }

    public void setEnabled(Boolean enabled) {
        this.getHandlerListener().setEnabled(enabled);
    }
    public Boolean isEnabled() {
        return this.getHandlerListener().isEnabled();
    }

    public String getModuleName() {
        return this.getHandlerListener().getModuleName();
    }

}
