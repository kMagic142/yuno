package ro.kmagic.utils.pagination;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import ro.kmagic.types.ModuleType;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class HelpPaginator extends Paginator {

    protected HashMap<ModuleType, HashMap<String, String>> fields;

    public HelpPaginator(int itemsOnPage, Consumer<Message> finalAction, int timeout, TimeUnit timeunit) {
        super(itemsOnPage, finalAction, timeout, timeunit);
    }

    public void setHelpFields(HashMap<ModuleType, HashMap<String, String>> fields) {
        this.fields = fields;
        super.fieldsLength = fields.size();
    }

    @Override
    public Message renderPage(int page) {
        MessageBuilder mbuilder = new MessageBuilder();
        EmbedBuilder ebuilder = new EmbedBuilder();
        HashMap<String, String> pageCmds = new HashMap<>();
        int start = (page - 1) * itemsOnPage;
        int end = Math.min(fieldsLength, page * itemsOnPage);

        for(ModuleType type : ModuleType.values()) {
            for(ModuleType key : fields.keySet()) {
                if (key != type) continue;

                for(String cmd : fields.get(key).keySet()) {
                    pageCmds.put(cmd, fields.get(key).get(cmd));
                }

            }
        }

        for(int i = start; i < end; i++)
            ebuilder.addField("" + pageCmds.keySet().toArray()[i], "" + pageCmds.values().toArray()[i], inline);

        ebuilder.setFooter((footer != null ? footer + " | " : "") + "Page " + page + "/" + pages, null);

        if(title != null) ebuilder.setAuthor(title, url, iconUrl);

        mbuilder.setEmbed(ebuilder.build());
        return mbuilder.build();
    }
}
