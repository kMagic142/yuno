package ro.kmagic.commands.music;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.music.GuildMusicManager;
import ro.kmagic.music.PlayerManager;
import ro.kmagic.types.ModuleType;

import java.util.ArrayList;

public class StopCommand implements CommandListener {

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getMusicManager(channel.getGuild());

        if(musicManager.audioPlayer.getPlayingTrack() == null) {
            channel.sendMessage("Not playing anything at the moment.").queue();
            return;
        }

        musicManager.scheduler.queue.clear();
        musicManager.audioPlayer.stopTrack();
        musicManager.audioPlayer.getLink().disconnect();
        musicManager.audioPlayer.getLink().destroy();

        channel.sendMessage("Ending current music session...").queue();
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
        return "Stop the current playing music";
    }

    @Override
    public String getModuleName() {
        return "stop";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.MUSIC;
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
