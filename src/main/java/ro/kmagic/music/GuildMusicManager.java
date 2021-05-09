package ro.kmagic.music;

import lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.entities.Guild;
import ro.kmagic.Main;

public class GuildMusicManager {
    public final LavalinkPlayer audioPlayer;
    public final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    public GuildMusicManager(Guild guild) {
        this.audioPlayer = Main.getLavalink().getLink(guild).getPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer, null);
        this.audioPlayer.addListener(this.scheduler);
        this.audioPlayer.getFilters().setVolume(1.0f);
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }
}
