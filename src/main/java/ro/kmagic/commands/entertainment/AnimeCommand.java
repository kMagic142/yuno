package ro.kmagic.commands.entertainment;

import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch;
import com.github.doomsdayrs.jikan4java.enums.SortBy;
import com.github.doomsdayrs.jikan4java.enums.search.animemanga.orderby.AnimeOrderBy;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import com.github.doomsdayrs.jikan4java.types.main.anime.Studios;
import com.github.doomsdayrs.jikan4java.types.main.anime.animePage.AnimePage;
import io.sentry.Sentry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.types.ModuleType;
import ro.kmagic.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AnimeCommand implements CommandListener {

    private Anime anime;
    private final SimpleConfig messages = Main.getMessages();

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        if (args.length < 1) {
            channel.sendMessage(messages.getString("GENERAL.not-enough-arguments")).queue();
            return;
        }

        try {
            String req = message.getContentRaw().replace(Utils.PREFIX + "anime", "");
            AnimeSearch search = new AnimeSearch().setQuery(req);
            search.setLimit(5);
            search.orderBy(AnimeOrderBy.TITLE);
            search.sortBy(SortBy.ASC);

            CompletableFuture<AnimePage> completedFuture = search.get();

            anime = new AnimeSearch().getByID(completedFuture.get().animes.get(0).mal_id).get();

            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Yuno ~ Anime found!", null, Main.getJDA().getSelfUser().getAvatarUrl())
                    .setTitle(anime.title_english + " (" + anime.title_japanese + ")")
                    .setColor(Color.BLACK)
                    .setDescription(getSynopsis())
                    .addField("Premiered", (anime.premiered != null) ? anime.premiered : "Not aired yet.", true)
                    .addField("Rating", String.valueOf(anime.rating), true)
                    .addField("Rank", String.valueOf(anime.rank), true)
                    .addField("Status", String.valueOf(anime.status), true)
                    .addField("Studios", getStudios(), true)
                    .addField("Type", String.valueOf(anime.type), true)
                    .addField("Aired", getAiredDate(), true)
                    .addField("Duration", anime.duration, true)
                    .addField("Episodes", String.valueOf(anime.episodes), true)
                    .addField("Broadcast", (anime.broadcast != null) ? anime.broadcast : "Not aired yet.", true)
                    .setThumbnail(anime.imageURL);

            channel.sendMessage(embed.build()).queue();
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            Sentry.captureException(e);
            channel.sendMessage(messages.getString("GENERAL.error")).queue();
        } catch (InterruptedException | ExecutionException e) {
            Sentry.captureException(e);
        }
    }

    private String getSynopsis() {
        try {
            String synopsis = anime.synopsis;
            if (synopsis.length() <= 962) { return synopsis; }
            String subString = synopsis.substring(0, 962-1);
            subString = subString.substring(0, subString.lastIndexOf(" "));
            return subString + "... [more](" + anime.url + ")";
        } catch(NullPointerException e) {
            return messages.getString("COMMANDS.ENTERTAINMENT.anime.no-synopsis");
        }
    }

    private String getStudios() {
        try {
            List<String> studios = new ArrayList<>();
            for (Studios studio : anime.studios) {
                studios.add(studio.name);
            }

            return String.join(", ", studios);

        } catch(NullPointerException e) {
            return messages.getString("COMMANDS.ENTERTAINMENT.anime.no-studios");
        }
    }

    private String getAiredDate() {
        try {
            return anime.aired.prop.from.year + "." + anime.aired.prop.from.month + "." + anime.aired.prop.from.day + " to " + anime.aired.prop.to.year + "." + anime.aired.prop.to.month + "." + anime.aired.prop.to.day;
        } catch(NullPointerException e) {
            return messages.getString("COMMANDS.ENTERTAINMENT.anime.no-date");
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
        return "Get info about your favourite anime";
    }
}
