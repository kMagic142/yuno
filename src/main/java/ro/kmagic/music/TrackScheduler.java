package ro.kmagic.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lavalink.client.player.IPlayer;
import lavalink.client.player.LavalinkPlayer;
import lavalink.client.player.event.*;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends PlayerEventListenerAdapter {
    public final IPlayer player;
    public final BlockingQueue<AudioTrack> queue;
    public boolean repeating = false;
    public TextChannel channel;


    public TrackScheduler(LavalinkPlayer player, TextChannel channel) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.channel = channel;
    }

    public void setChannel(TextChannel channel) {
        this.channel = channel;
    }

    public void queue(AudioTrack track) {
        if (this.player.getPlayingTrack() != null) {
            this.queue.offer(track);
        } else {
            if(channel != null) channel.sendMessage("Now playing **" + track.getInfo().title + "**.").queue();
            this.player.playTrack(track);
        }
    }

    public void nextTrack() {
        this.player.playTrack(this.queue.poll());
    }

    @Override
    public void onTrackEnd(IPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (this.repeating) {
                this.player.playTrack(track.makeClone());
                return;
            }
            if(queue.size() < 1) {
                PlayerManager.getInstance().getMusicManager(channel.getGuild()).audioPlayer.getLink().disconnect();
                PlayerManager.getInstance().getMusicManager(channel.getGuild()).audioPlayer.getLink().destroy();
                return;
            }

            nextTrack();
        }
    }
}
