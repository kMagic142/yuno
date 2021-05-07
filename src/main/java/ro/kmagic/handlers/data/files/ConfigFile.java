package ro.kmagic.handlers.data.files;

import net.notfab.spigot.simpleconfig.SimpleConfig;
import net.notfab.spigot.simpleconfig.SimpleConfigManager;
import net.notfab.spigot.simpleconfig.standalone.StandaloneConfigManager;
import ro.kmagic.Main;

import java.io.File;

public class ConfigFile {

    private File folder;
    private final SimpleConfigManager scm = new StandaloneConfigManager(new File(Main.getDataFolder()));
    private SimpleConfig sc;
    private File file;

    public ConfigFile() {
        getFile();
    }

    public File getFile() {
        if(file == null || folder == null) {
            folder = new File(Main.getDataFolder());
            file = new File(Main.getDataFolder(), "config.yml");
            if(!file.exists()) scm.copyResource(Main.class.getResourceAsStream("/config.yml"), file);
        }
        return file;
    }

    public SimpleConfig getData() {
        if(sc == null) sc = scm.getNewConfig("config.yml");
        return sc;
    }

    public void reload() {
        sc = scm.getNewConfig("config.yml");
    }

}
