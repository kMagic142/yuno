package ro.kmagic.commands.entertainment.games;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.notfab.spigot.simpleconfig.Section;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.users.UserManager;
import ro.kmagic.types.YunoUser;
import ro.kmagic.utils.Utils;
import ro.kmagic.utils.slotmachine.SlotMachine;
import ro.kmagic.types.ModuleType;

import java.util.ArrayList;
import java.util.concurrent.Future;

public class SlotMachineCommand implements CommandListener {

    private final SimpleConfig messages = Main.getMessages();
    private final Section section = messages.getSection("COMMANDS.ENTERTAINMENT.GAMES.slotmachine");

    // got code from:
    // https://github.com/Kaaz/DiscordBot/blob/master/src/main/java/emily/command/fun/SlotMachineCommand.java
    // slightly modified. (the code was took for use in personal purposes, not for production)

    @Override
    public void onCommand(Member sender, TextChannel channel, Message msg, String[] args) {
        if(args.length >= 1) {
            int bet;
            YunoUser user = UserManager.getUser(sender.getId());

            if (args[0].matches("\\d+"))
                bet = Math.min(Utils.parseInt(args[0], 0), 1000);
            else
                bet = 0;

            if(bet > 0) {
                if(user.getCoins() < bet) {
                    channel.sendMessage(section.getString("insufficient-funds")).queue();
                    return;
                }
                user.setCoins(user.getCoins() - bet);
            }

            SlotMachine slotMachine = new SlotMachine();
            Message message = channel.sendMessage(slotMachine.toString()).complete();

            final Future<?>[] f = {null};
            long SPIN_INTERVAL = 2000L;
            f[0] = Main.scheduleRepeat(() -> {
                try {
                    if (slotMachine.gameInProgress()) {
                        slotMachine.spin();
                    }
                    String gameResult;
                    if (!slotMachine.gameInProgress()) {
                        int winMulti = slotMachine.getWinMultiplier();
                        if (winMulti > 0) {
                            if (bet > 0) {
                                gameResult = String.format(section.getString("won"), slotMachine.getWinSlotTimes(), slotMachine.getWinSlot().getEmote(), bet * winMulti, messages.getString("GENERAL.coins"));
                                user.setCoins(user.getCoins() + bet * winMulti);
                            } else {
                                gameResult = String.format(section.getString("rolled"), slotMachine.getWinSlotTimes(), slotMachine.getWinSlot().getEmote());
                            }
                        } else {
                            gameResult = section.getString("lost");
                        }

                        message.editMessage(slotMachine + "\n" + gameResult).queue();
                        f[0].cancel(false);
                    } else {
                        message.editMessage(slotMachine.toString()).queue();
                    }
                } catch (Exception e) {
                    f[0].cancel(false);
                }
            }, 1000L, SPIN_INTERVAL);
        }
    }

    private boolean enabled;

    @Override
    public String getModuleName() {
        return "SlotMachine";
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

    @Override
    public String getCommandAlias() {
        return "slot";
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return "Gamble your money away on a slot machine";
    }
}
