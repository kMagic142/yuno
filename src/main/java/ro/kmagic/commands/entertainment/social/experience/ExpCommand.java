package ro.kmagic.commands.entertainment.social.experience;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.users.UserManager;
import ro.kmagic.types.ModuleType;
import ro.kmagic.types.YunoUser;

import java.util.ArrayList;

public class ExpCommand implements CommandListener {

    private final SimpleConfig messages = Main.getMessages();

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {

        YunoUser user = UserManager.getUser(sender.getId());

        int exp = user.getExperience();
        int level = user.getLevel();

        int nextExp = 25 * level * (1 + level);

        channel.sendMessage(String.format(messages.getString("COMMANDS.SOCIAL.EXPERIENCE.exp.message"), exp, level, exp, nextExp)).queue();
    }

    private boolean enabled;

    @Override
    public String getCommandAlias() {
        return "exp";
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return "See your gained EXP";
    }

    @Override
    public String getModuleName() {
        return "Experience";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.EXPERIENCE;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }
}
