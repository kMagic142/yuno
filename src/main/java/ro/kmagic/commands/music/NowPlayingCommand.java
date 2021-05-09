package ro.kmagic.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.music.GuildMusicManager;
import ro.kmagic.music.PlayerManager;
import ro.kmagic.types.ModuleType;
import ro.kmagic.utils.Utils;

import java.awt.*;
import java.util.ArrayList;

public class NowPlayingCommand implements CommandListener {

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getMusicManager(channel.getGuild());
        LavalinkPlayer player = musicManager.audioPlayer;

        if(player.getPlayingTrack() == null) {
            channel.sendMessage("Not playing anything at the moment.").queue();
            return;
        }

        AudioTrackInfo info = player.getPlayingTrack().getInfo();

        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor(info.author, info.uri, Main.getJDA().getSelfUser().getAvatarUrl())
                .addField("Now playing " + info.title, (player.isPaused() ? "\u23F8" : "▶") + " " + Utils.formatTime(player.getTrackPosition()) + " - " + Utils.formatTime(player.getPlayingTrack().getDuration()), false)
                .setColor(Color.decode("#36393f"));

        channel.sendMessage(builder.build()).queue();
    }

    private boolean enabled;

    @Override
    public String getCommandAlias() {
        return "np";
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return "See the song that's currently playing";
    }

    @Override
    public String getModuleName() {
        return "nowplaying";
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
