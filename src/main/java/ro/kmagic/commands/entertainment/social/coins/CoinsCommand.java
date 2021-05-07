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

import java.util.ArrayList;

public class CoinsCommand implements CommandListener {

    private final SimpleConfig messages = Main.getMessages();

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        int balance = UserManager.getUser(sender.getId()).getCoins();

        channel.sendMessage(String.format(messages.getString("COMMANDS.SOCIAL.COINS.balance.message"), balance)).queue();
    }

    private boolean enabled;

    @Override
    public String getCommandAlias() {
        return "balance";
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return "See your balance";
    }

    @Override
    public String getModuleName() {
        return "Coins";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.ECONOMY;
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
