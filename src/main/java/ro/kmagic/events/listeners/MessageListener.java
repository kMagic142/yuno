package ro.kmagic.events.listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ro.kmagic.Main;
import ro.kmagic.types.YunoGuild;
import ro.kmagic.types.YunoUser;
import ro.kmagic.users.UserManager;
import ro.kmagic.types.Module;
import ro.kmagic.types.ModuleType;
import ro.kmagic.utils.Utils;

import javax.annotation.Nonnull;
import java.sql.SQLException;

public class MessageListener extends ListenerAdapter implements Module {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(event.getMessage().getContentRaw().startsWith(Utils.PREFIX)) return;

        if(!UserManager.userExists(event.getMessage().getAuthor().getId()))
            UserManager.saveUserdata(new YunoUser(event.getMessage().getAuthor().getId(), event.getMessage().getAuthor()));

        YunoUser user = UserManager.getUser(event.getMessage().getAuthor().getId());

        // exp handling
        int exp = user.getExperience();
        int level = user.getLevel();

        int newExp = exp + 1;
        double newLevel = Math.floor(25 + (Math.sqrt(625 + 100 * newExp))) / 50;
        double nextExp = 25 * newLevel * (1 + newLevel);

        if(level < Math.floor(newLevel)) {
            user.setExperience((int) Math.floor(newLevel));
            event.getChannel().sendMessage("You just leveled up to **Level " + Math.floor(newLevel) + "**! Experience to next level up: **" + Math.floor(nextExp) + " EXP**").queue();
        }

        user.setExperience(newExp);

    }


    private boolean enabled;

    @Override
    public String getModuleName() {
        return "MessageReceived";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.EXPERIENCE;
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
