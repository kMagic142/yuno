package ro.kmagic.utils.pagination;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.requests.RestAction;
import ro.kmagic.Main;
import ro.kmagic.types.ModuleType;
import ro.kmagic.utils.Utils;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Paginator {

    protected final int itemsOnPage;
    protected int pages;
    protected final Consumer<Message> finalAction;
    protected final int timeout;
    protected final TimeUnit timeunit;
    protected final EventWaiter waiter = Main.getWaiter();

    @Setter
    protected HashMap<ModuleType, HashMap<String, String>> helpFields;
    @Setter
    protected int fieldsLength;
    @Setter
    protected String description;
    @Setter
    protected Color color;
    @Setter
    protected boolean inline;
    @Setter
    protected HashMap<String, String> fields;
    @Setter
    protected String footer;
    protected String title, url, iconUrl;

    public static final String LEFT = "\u25C0";
    public static final String STOP = "\u23F9";
    public static final String RIGHT = "\u25B6";

    public Paginator(int itemsOnPage, Consumer<Message> finalAction, int timeout, TimeUnit timeunit) {
        this.finalAction = finalAction;
        this.timeout = timeout;
        this.timeunit = timeunit;
        this.itemsOnPage = itemsOnPage;

        this.inline = false;
        this.fieldsLength = fields != null ? fields.size() : 0;
    }

    private void init(RestAction<Message> action, int page) {
        action.queue(m -> {
            if(pages > 1) {
                m.addReaction(LEFT).queue();
                m.addReaction(STOP).queue();
                m.addReaction(RIGHT)
                        .queue(v -> pagination(m, page), t -> pagination(m, page));
            } else {
                finalAction.accept(m);
            }
        });
    }

    public void paginate(MessageChannel channel, int page) {
        for(HashMap<String, String> cmdField : helpFields.values()) {
            pages += cmdField.size() / itemsOnPage;
            fieldsLength += cmdField.size();
        }

        this.pages = (int) Math.floor(this.pages) + (fieldsLength / itemsOnPage);

        System.out.println("Pages:"+pages+" | fL:"+fieldsLength+" | iOP:"+itemsOnPage);


        if(page < 1)
            page = 1;
        else if (page > pages)
            page = pages;

        Message msg = renderPage(page);
        init(channel.sendMessage(msg), page);
    }

    private void pagination(Message message, int page) {
        waiter.waitForEvent(MessageReactionAddEvent.class,
                event -> checkReaction(event, message.getIdLong()),
                event -> handleEvent(event, message, page),
                timeout, timeunit, () -> finalAction.accept(message));
    }

    private boolean checkReaction(MessageReactionAddEvent event, long messageId) {
        if(event.getMessageIdLong() != messageId)
            return false;
        switch(event.getReactionEmote().getName()) {
            case LEFT:
            case STOP:
            case RIGHT:
                return Utils.isValidUser(event.getUser(), event.isFromGuild() ? event.getGuild() : null);
            default:
                return false;
        }
    }

    private void handleEvent(MessageReactionAddEvent event, Message message, int page) {
        int newPageNum = page;
        switch(event.getReaction().getReactionEmote().getName()) {
            case LEFT:
                if(newPageNum == 1)
                    newPageNum = pages + 1;
                if(newPageNum > 1)
                    newPageNum--;
                break;
            case RIGHT:
                if(newPageNum == pages)
                    newPageNum = 0;
                if(newPageNum < pages)
                    newPageNum++;
                break;
            case STOP:
                finalAction.accept(message);
                return;
        }

        try {
            event.getReaction().removeReaction(event.getUser()).queue();
        } catch(PermissionException ignored) {}

        int n = newPageNum;
        message.editMessage(renderPage(newPageNum)).queue(m -> pagination(m, n));
    }

    public void setAuthor(String title, String url, String iconUrl) {
        this.title = title;
        this.url = url;
        this.iconUrl = iconUrl;
    }


    public Message renderPage(int page) {
        MessageBuilder mbuilder = new MessageBuilder();
        EmbedBuilder ebuilder = new EmbedBuilder();
        int start = (page - 1) * itemsOnPage;
        int end = Math.min(fields.size(), page * itemsOnPage);

        for(int i = start; i < end; i++)
            ebuilder.addField("" + fields.keySet().toArray()[i], "" + fields.values().toArray()[i], inline);

        ebuilder.setColor(color);
        ebuilder.setDescription(description);
        ebuilder.setFooter((footer != null ? footer + " | " : "") + "Page " + page + "/" + pages, null);

        if(title != null) ebuilder.setAuthor(title, url, iconUrl);

        mbuilder.setEmbed(ebuilder.build());
        return mbuilder.build();
    }






}
