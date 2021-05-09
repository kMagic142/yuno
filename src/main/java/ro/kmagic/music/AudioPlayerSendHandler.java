package ro.kmagic.music;

import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final LavalinkPlayer audioPlayer;
    private final ByteBuffer buffer;

    public AudioPlayerSendHandler(LavalinkPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(1024);
        MutableAudioFrame frame = new MutableAudioFrame();
        frame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        return this.audioPlayer.isConnected();
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return (ByteBuffer) this.buffer.flip();
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
