package ro.kmagic.handlers.commands;

import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;

public class CommandBuilder {

    String commandName;
    String commandAlias = "";
    String commandDescription;
    ArrayList<Permission> commandPermissions;
    CommandListener handlerListener;

    public CommandBuilder(CommandListener handlerListener) {
        if (handlerListener == null) {
            throw new IllegalArgumentException("Listener must not be null!");
        }

        this.commandName = handlerListener.getModuleName();
        this.commandAlias = handlerListener.getCommandAlias();
        this.commandDescription = handlerListener.getCommandDescription();
        this.commandPermissions = handlerListener.getCommandPermissions();
        this.handlerListener = handlerListener;
    }

    public CommandBuilder setAlias(String commandAlias) {
        this.commandAlias = commandAlias;

        return this;
    }

    public CommandBuilder setDescription(String commandDescription) {
        this.commandDescription = commandDescription;

        return this;
    }

    public CommandBuilder addPermission(Permission permissionId) {
        commandPermissions.add(permissionId);

        return this;
    }

    public Command build() {
        return new Command(this);
    }

}
