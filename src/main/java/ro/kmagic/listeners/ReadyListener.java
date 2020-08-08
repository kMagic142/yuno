package ro.kmagic.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;

import static ro.kmagic.Main.jda;

public class ReadyListener implements EventListener {


    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if(event instanceof ReadyEvent) {
            System.out.println("Botul este pornit!");
            System.out.println("");
            System.out.println("------------------------");
            System.out.println("- Tag: " + jda.getSelfUser().getAsTag());
            System.out.println("- Useri: " + jda.getUsers().size());
            System.out.println("- Servere: " + jda.getGuilds().size());
            System.out.println("------------------------");
        }

    }
}
