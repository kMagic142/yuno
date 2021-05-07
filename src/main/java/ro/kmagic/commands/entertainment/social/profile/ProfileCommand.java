package ro.kmagic.commands.entertainment.social.profile;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.users.UserManager;
import ro.kmagic.types.ModuleType;
import ro.kmagic.utils.profile.ProfileImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProfileCommand implements CommandListener {
    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        try {
            File file;
            ProfileImage pfp = new ProfileImage(UserManager.getUser(sender.getId()));
            file = pfp.getProfileImage();

            channel.sendFile(file).queue(m -> file.delete());

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return "See your profile";
    }

    @Override
    public String getModuleName() {
        return "profile";
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
