package ro.kmagic.events;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ro.kmagic.Main;
import ro.kmagic.types.Module;
import ro.kmagic.types.ModuleType;

import javax.annotation.Nonnull;

public class ReadyEvent extends ListenerAdapter implements Module {


    @Override
    public void onReady(@Nonnull net.dv8tion.jda.api.events.ReadyEvent event) {
        System.out.println("Botul este pornit!");
        System.out.println("");
        System.out.println("------------------------");
        System.out.println("- Tag: " + Main.getJDA().getSelfUser().getAsTag());
        System.out.println("- Useri: " + Main.getJDA().getUsers().size());
        System.out.println("- Servere: " + Main.getJDA().getGuilds().size());
        System.out.println("------------------------");
    }

    private boolean enabled;

    @Override
    public String getModuleName() {
        return "Ready";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.EVENT;
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
