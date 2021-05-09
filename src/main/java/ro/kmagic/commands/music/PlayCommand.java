package ro.kmagic.commands.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import io.sentry.Sentry;
import lavalink.client.io.jda.JdaLavalink;
import lavalink.client.io.jda.JdaLink;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.music.PlayerManager;
import ro.kmagic.types.ModuleType;
import ro.kmagic.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PlayCommand implements CommandListener {

    private final YouTube youtube;

    public PlayCommand() {
        YouTube temp = null;

        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null)
                    .setApplicationName("yuno")
                    .build();
        } catch (Exception e){
            Sentry.captureException(e);
        }

        youtube = temp;
    }

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        VoiceChannel vc = sender.getVoiceState() != null ? sender.getVoiceState().getChannel() : null;

        if(vc == null || args.length < 1) return;

        String track = String.join(" ", args);

        JdaLavalink lavalink = Main.getLavalink();
        JdaLink link = lavalink.getLink(vc.getGuild());

        link.connect(vc);

        if(!Utils.isUrl(track)) {
            SearchResult ytSearched = searchYoutube(track);

            if(ytSearched == null) {
                channel.sendMessage("Couldn't find anything on youtube.").queue();
                return;
            }

            track = "https://youtube.com/watch?v=" + ytSearched.getId().getVideoId();
        }

        PlayerManager.getInstance().loadAndPlay(channel, track);

    }

    private SearchResult searchYoutube(String input) {
        try {

            List<SearchResult> results = youtube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(10L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey("AIzaSyB7Exub9zO-lUg_hkwOAFP94w_oD-FyL_Y")
                    .execute()
                    .getItems();

            if(!results.isEmpty()) {
                return results.get(0);
            }

        } catch(Exception e){
            Sentry.captureException(e);
        }
        return null;
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
        return "Play a song";
    }

    @Override
    public String getModuleName() {
        return "play";
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
