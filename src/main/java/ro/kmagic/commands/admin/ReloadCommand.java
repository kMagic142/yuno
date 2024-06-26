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
import java.util.concurrent.CompletableFuture;

public class ReloadCommand implements CommandListener {

    private final SimpleConfig messages = Main.getMessages();

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        channel.sendMessage(messages.getString("COMMANDS.ADMIN.reload.reloading")).queue();


        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> Main.reloadModules(channel.getGuild().getId(),channel.getId()));

        future.thenRun(() -> channel.sendMessage(messages.getString("COMMANDS.ADMIN.reload.reloaded")).queue());
    }

    private boolean enabled;

    @Override
    public String getModuleName() {
        return "Reload";
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
        return "Reloads the bot";
    }
}
