package ro.kmagic.commands.entertainment.social.coins;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.users.UserManager;
import ro.kmagic.types.ModuleType;
import ro.kmagic.utils.Utils;

import java.util.ArrayList;

public class GiveCoinsCommand implements CommandListener {

    private final SimpleConfig messages = Main.getMessages();

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        if(args.length < 2 || message.getMentionedMembers().isEmpty()) {
            return;
        }

        Member user = message.getMentionedMembers().get(0);

        int balance = UserManager.getUser(user.getId()).getCoins();
        balance += Utils.parseInt(args[1], 0);

        UserManager.getUser(user.getId()).setCoins(balance);

        channel.sendMessage(String.format(messages.getString("COMMANDS.SOCIAL.COINS.givecoins.message"), args[1], user.getAsMention(), balance)).queue();

    }

    private boolean enabled;

    @Override
    public String getCommandAlias() {
        return null;
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return "Gives coins to a user";
    }

    @Override
    public String getModuleName() {
        return "givecoins";
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
        enabled = bool;
    }
}
