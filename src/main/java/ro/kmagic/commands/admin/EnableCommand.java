package ro.kmagic.commands.admin;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import net.notfab.spigot.simpleconfig.standalone.StandaloneConfigManager;
import ro.kmagic.Main;
import ro.kmagic.Utils;
import ro.kmagic.modules.Module;
import ro.kmagic.modules.ModuleType;

import javax.annotation.Nonnull;
import java.io.File;

import static ro.kmagic.Main.configManager;

public class EnableCommand extends Module implements EventListener {

    @Override
    public void onEvent(@Nonnull GenericEvent genericEvent) {
        if(genericEvent instanceof GuildMessageReceivedEvent) {
            GuildMessageReceivedEvent event = new GuildMessageReceivedEvent(genericEvent.getJDA(), genericEvent.getResponseNumber(), ((GuildMessageReceivedEvent) genericEvent).getMessage());

            if(!Main.isEnabledForGuild(event.getGuild().getId(), this)) return;
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
            if (event.getAuthor().isBot()) return;

            String[] args = event.getMessage().getContentRaw().split(" ");

            if (event.getMessage().getContentRaw().startsWith(Utils.PREFIX + "enable")) {

                if(args.length < 3) {
                    event.getChannel().sendMessage("Not enough arguments provided.").queue();
                    return;
                }

                SimpleConfig moduleConfig = configManager.getNewConfig("\\guilds\\" + event.getGuild().getId() + ".yml");

                Main.commands.forEach(module -> {
                    if(module.getModuleName().equalsIgnoreCase(args[2])) {
                        moduleConfig.set(module.getModuleType().toString() + "." + module.getModuleName(), true);
                        moduleConfig.save();
                        Main.reloadModules(event.getGuild().getId(), event.getChannel().getId());
                    }
                });
            }
        }

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
}
