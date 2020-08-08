package ro.kmagic.commands.admin;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import ro.kmagic.Main;
import ro.kmagic.Utils;
import ro.kmagic.modules.Module;
import ro.kmagic.modules.ModuleType;

import javax.annotation.Nonnull;

public class ReloadCommand extends Module implements EventListener {

    @Override
    public void onEvent(@Nonnull GenericEvent genericEvent) {
        if(genericEvent instanceof GuildMessageReceivedEvent) {
            GuildMessageReceivedEvent event = new GuildMessageReceivedEvent(genericEvent.getJDA(), genericEvent.getResponseNumber(), ((GuildMessageReceivedEvent) genericEvent).getMessage());

            if(!Main.isEnabledForGuild(event.getGuild().getId(), this)) return;
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
            if (event.getAuthor().isBot()) return;


            if (event.getMessage().getContentRaw().startsWith(Utils.PREFIX + "reload")) {
                event.getChannel().sendMessage("Yuno is now reloading all modules...").queue();

                Main.reloadModules(event.getGuild().getId(), event.getChannel().getId());

            }
        }

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
}
