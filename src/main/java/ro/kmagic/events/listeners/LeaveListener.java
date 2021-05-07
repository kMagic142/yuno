package ro.kmagic.events.listeners;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import org.jetbrains.annotations.NotNull;
import ro.kmagic.Main;
import ro.kmagic.types.Module;
import ro.kmagic.types.ModuleType;

public class LeaveListener extends ListenerAdapter implements Module {

    private final SimpleConfig config = Main.getConfig();
    private final SimpleConfig messages = Main.getMessages();
    
    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        if (Main.isEnabledForGuild(event.getGuild().getId(), this)) return;

        String channel = config.getString("EVENTS.joinLeave.channel");

        if (channel == null) {
            event.getGuild().getTextChannels().get(0).sendMessage("A channel name for the **Join / Leave listener** hasn't been set. Please reffer to the bot's config to set a channel for this function, or disable the module by typing **yuno disable joinleave**.").queue();
            return;
        }

        String leaveMessage = messages.getString("EVENTS.joinLeave.leave-message");
        try {
            leaveMessage = leaveMessage.replace("%member%", event.getUser().getAsTag());
        } catch (NullPointerException e) {
            // ignore
        }

        String finalLeaveMessage = leaveMessage;
        TextChannel textChannel = event.getGuild().getTextChannelsByName(channel, true).get(0);
        if (textChannel.getName().equals(channel))
            textChannel.sendMessage(finalLeaveMessage).queue();

    }
        
    private boolean enabled;

    @Override
    public String getModuleName() {
        return "MemberLeave";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.EVENT;
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
