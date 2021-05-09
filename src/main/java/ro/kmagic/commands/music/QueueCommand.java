package ro.kmagic.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueCommand implements CommandListener {
    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if(queue.isEmpty()) {
            channel.sendMessage("The queue is currently empty.").queue();
            return;
        }

        final int trackCount = Math.min(queue.size(), 20);
        final List<AudioTrack> tracks = new ArrayList<>(queue);
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Currently in Queue (" + queue.size() + " in total)")
                .setColor(Color.decode("#36393f"))
                .setAuthor("Yuno - Queue List", Main.getJDA().getSelfUser().getAvatarUrl());

        for (int i = 0; i < trackCount; i++) {
            AudioTrack track = tracks.get(i);
            AudioTrackInfo info = track.getInfo();

            builder.appendDescription(String.format(
                    "`%s - %s`\n",
                    info.title,
                    info.author
            ));
        }

        channel.sendMessage(builder.build()).queue();
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
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
        return "See the guild's queue";
    }

    @Override
    public String getModuleName() {
        return "queue";
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
