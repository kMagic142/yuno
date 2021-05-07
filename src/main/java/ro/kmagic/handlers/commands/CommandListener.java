package ro.kmagic.handlers.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import ro.kmagic.types.Module;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CommandListener extends Module {

    void onCommand(Member sender, TextChannel channel, Message message, String[] args);

    String getCommandAlias();
    ArrayList<Permission> getCommandPermissions();
    String getCommandDescription();

}
