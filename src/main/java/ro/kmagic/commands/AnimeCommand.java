package ro.kmagic.commands;

import com.schalar.jikan.JikanClient;
import com.schalar.jikan.model.anime.Anime;
import com.schalar.jikan.request.anime.AnimeRequest;
import com.schalar.jikan.request.search.AnimeSearchRequest;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import ro.kmagic.Utils;
import ro.kmagic.Main;
import ro.kmagic.modules.Module;
import ro.kmagic.modules.ModuleType;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class AnimeCommand extends Module implements EventListener {

    JikanClient jikan = new JikanClient();

    private Anime anime;

    @Override
    public void onEvent(@Nonnull GenericEvent genericEvent) {
        if(genericEvent instanceof GuildMessageReceivedEvent) {
            GuildMessageReceivedEvent event = new GuildMessageReceivedEvent(genericEvent.getJDA(), genericEvent.getResponseNumber(), ((GuildMessageReceivedEvent) genericEvent).getMessage());

            if(!Main.isEnabledForGuild(event.getGuild().getId(), this)) return;

            if (event.getAuthor().isBot()) return;
            String[] args = event.getMessage().getContentRaw().split(" ");


            if (event.getMessage().getContentRaw().startsWith(Utils.PREFIX + "anime")) {
                if (args.length < 3) {
                    event.getChannel().sendMessage("You need to provide me with something to search for!").queue();
                    return;
                }

                try {

                    AnimeSearchRequest req = new AnimeSearchRequest(event.getMessage().getContentRaw().replace(Utils.PREFIX + "anime", ""));

                    Integer animeid = jikan.getAnimeSearch(req).getResults().get(0).getMalId();
                    anime = jikan.getAnime(new AnimeRequest(animeid));

                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor("Yuno ~ Anime found!", null, Main.jda.getSelfUser().getAvatarUrl())
                            .setTitle(anime.getTitle() + " (" + anime.getTitleJapanese() + ")")
                            .setColor(Color.BLACK)
                            .setDescription(getSynopsis())
                            .addField("Premiered", anime.getPremiered(), true)
                            .addField("Rating", anime.getRating(), true)
                            .addField("Rank", String.valueOf(anime.getRank()), true)
                            .addField("Status", anime.getStatus(), true)
                            .addField("Studios", getStudios(), true)
                            .addField("Type", anime.getType(), true)
                            //.addField("Synopsis", anime.getSynopsis(), true)
                            .addField("Aired", getAiredDate(), true)
                            .addField("Duration", anime.getDuration(), true)
                            .addField("Episodes", String.valueOf(anime.getEpisodes()), true)
                            .addField("Broadcasted", anime.getBroadcast(), true)
                            .addField("Is airing?", anime.isAiring() ? "Yes" : "No", true)
                            .setThumbnail(anime.getImageUrl());
                    //.setFooter(anime.getUrl());

                    event.getChannel().sendMessage(embed.build()).queue();
                } catch (NullPointerException | IndexOutOfBoundsException e) {
                    //e.printStackTrace();
                    event.getChannel().sendMessage("An error occured. Make sure you provided me a name, not an url or number. If this is not the case, please contact the developer.").queue();
                }
            }
        }
    }

    public String getSynopsis() {
        try {
            String synopsis = anime.getSynopsis();
            if (synopsis.length() <= 962) { return synopsis; }
            String subString = synopsis.substring(0, 962-1);
            subString.substring(0, subString.lastIndexOf(" "));
            return subString + "... [more](" + anime.getUrl() + ")";
        } catch(NullPointerException e) {
            return "There is no synopsis written for this anime on MAL.";
        }
    }

    public String getStudios() {
        try {
            String bracket1 = "\\[";
            String bracket2 = "]";
            return anime.getStudios().toString().replaceAll(bracket1, "").replaceAll(bracket2, "");
        } catch(NullPointerException e) {
            //e.printStackTrace();
            return "boomer";
        }
    }

    public String getAiredDate() {
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("LLL dd, yyyy");
            return anime.getAired().get("from").format(dateFormat) + " to " + anime.getAired().get("to").format(dateFormat);
        } catch(NullPointerException e) {
            //e.printStackTrace();
            return "boomer";
        }
    }

    private boolean enabled;

    @Override
    public String getModuleName() {
        return "Anime";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.COMMAND;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean bool) {
        this.enabled = bool;
    }
}
