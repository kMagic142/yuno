package ro.kmagic.handlers.data.files;

import net.notfab.spigot.simpleconfig.SimpleConfig;
import net.notfab.spigot.simpleconfig.SimpleConfigManager;
import net.notfab.spigot.simpleconfig.standalone.StandaloneConfigManager;
import ro.kmagic.Main;

import java.io.File;
import java.io.IOException;

public class MessagesFile {

    private File folder;
    private final SimpleConfigManager scm = new StandaloneConfigManager(new File(Main.getDataFolder()));
    private SimpleConfig sc;
    private File file;

    public MessagesFile() throws IOException {
        getFile();
    }

    public File getFile() throws IOException {
        if(folder == null) {
            folder = new File(Main.getDataFolder());
            folder.mkdir();
            if(file == null) {
                file = new File(Main.getDataFolder(), "messages.yml");
                file.createNewFile();
            }
        }

        if(file.length() == 0) scm.copyResource(Main.class.getResourceAsStream("/messages.yml"), file);

        return file;
    }

    public SimpleConfig getData() {
        if(sc == null) sc = scm.getNewConfig("messages.yml");
        return sc;
    }

    public void reload() {
        sc = scm.getNewConfig("messages.yml");
    }

}
