package ro.kmagic.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import ro.kmagic.Main;
import ro.kmagic.modules.Module;
import ro.kmagic.modules.ModuleType;

import javax.annotation.Nonnull;

import static ro.kmagic.Main.config;

public class MemberLeaveListener extends Module implements EventListener {


    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if(event instanceof GuildMemberLeaveEvent) {

            if (!Main.isEnabledForGuild(((GuildMemberLeaveEvent) event).getGuild().getId(), this)) return;


            if(config.get("LISTENER0S.joinJeave.channel") == null) {
                ((GuildMemberLeaveEvent) event).getGuild().getTextChannels().get(0).sendMessage("A channel name for the **Join / Leave listener** hasn't been set. Please reffer to the bot's config to set a channel for this function, or disable the module by typing **yuno disable joinleave**.").queue();
                return;
            }

            String leaveMessage = config.getString("LISTENERS.joinLeave.leave-message");
            try {
                leaveMessage.replaceAll("%member%", ((GuildMemberJoinEvent) event).getMember().getEffectiveName());
                leaveMessage.replaceAll("%mention%", "<@" + ((GuildMemberJoinEvent) event).getMember().getId() + ">");
            } catch (NullPointerException e) {
                // ignore
            }

            ((GuildMemberLeaveEvent) event).getGuild().getTextChannels().forEach(textChannel -> {
                if(textChannel.getName() == config.get("LISTENERS.joinLeave.channel")) {
                    textChannel.sendMessage(leaveMessage).queue();
                }
            });

        }
    }

    private boolean enabled;

    @Override
    public String getModuleName() {
        return "JoinLeave";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.LISTENER;
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
