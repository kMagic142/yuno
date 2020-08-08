package ro.kmagic.commands;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import ro.kmagic.Main;
import ro.kmagic.Utils;
import ro.kmagic.modules.Module;
import ro.kmagic.modules.ModuleType;

import javax.annotation.Nonnull;

public class PingCommand extends Module implements EventListener {

    @Override
    public void onEvent(@Nonnull GenericEvent genericEvent) {
        if(genericEvent instanceof GuildMessageReceivedEvent) {
            GuildMessageReceivedEvent event = new GuildMessageReceivedEvent(genericEvent.getJDA(), genericEvent.getResponseNumber(), ((GuildMessageReceivedEvent) genericEvent).getMessage());

            if (!Main.isEnabledForGuild(event.getGuild().getId(), this)) return;
            if (event.getAuthor().isBot()) return;

            if (event.getMessage().getContentRaw().startsWith(Utils.PREFIX + "ping")) {
                long ping = event.getJDA().getRestPing().complete();

                event.getChannel().sendMessage("Bot's ping is **" + ping + "ms**.").queue();
            }
        }

    }

    private boolean enabled;

    @Override
    public String getModuleName() {
        return "Ping";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.COMMAND;
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
