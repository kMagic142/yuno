package ro.kmagic.commands.music;

import lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.music.GuildMusicManager;
import ro.kmagic.music.PlayerManager;
import ro.kmagic.music.TrackScheduler;
import ro.kmagic.types.ModuleType;

import java.util.ArrayList;

public class SkipCommand implements CommandListener {

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getMusicManager(channel.getGuild());
        LavalinkPlayer player = musicManager.audioPlayer;
        TrackScheduler scheduler = musicManager.scheduler;

        if(player.getPlayingTrack() == null) {
            channel.sendMessage("Not playing anything at the moment.").queue();
            return;
        }

        scheduler.nextTrack();
        channel.sendMessage("Skipping current playing song.").queue();
    }

    private boolean enabled;

    @Override
    public String getCommandAlias() {
        return "forward";
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return "Skip the current song";
    }

    @Override
    public String getModuleName() {
        return "skip";
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
