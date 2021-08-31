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
        super.helpFields = fields;
    }

    @Override
    public Message renderPage(int page) {
        MessageBuilder mbuilder = new MessageBuilder();
        EmbedBuilder ebuilder = new EmbedBuilder();
        HashMap<String, String> pageCmds = new HashMap<>();
        ModuleType pageModule = (ModuleType) fields.keySet().toArray()[page];

        //int start = (page - 1) * itemsOnPage;
        //int end = page * itemsOnPage;

        int start = (int) Math.ceil((double) fields.get(pageModule).size() / itemsOnPage);
        int end = fields.get(pageModule).size();

        for(String cmd : fields.get(pageModule).keySet()) {
            pageCmds.put(cmd, fields.get(pageModule).get(cmd));
        }


        System.out.println("S:"+start+" | E:"+end+" | iOP:"+itemsOnPage+" | fL:"+fieldsLength+" | p:"+page);

        for(int i = start; i < end; i++) {
            ebuilder.addField("" + pageCmds.keySet().toArray()[i], "" + pageCmds.values().toArray()[i], inline);
        }

        ebuilder.setDescription(pageModule.toString());
        ebuilder.setFooter((footer != null ? footer + " | " : "") + "Page " + page + "/" + pages, null);

        if(title != null) ebuilder.setAuthor(title, url, iconUrl);

        mbuilder.setEmbed(ebuilder.build());
        return mbuilder.build();
    }
}
