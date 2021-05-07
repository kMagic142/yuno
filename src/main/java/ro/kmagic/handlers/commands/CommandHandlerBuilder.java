package ro.kmagic.handlers.commands;

import net.dv8tion.jda.api.JDA;
import org.apache.commons.lang.NullArgumentException;

import java.util.ArrayList;

public class CommandHandlerBuilder {

    protected ArrayList<Command> commandList = new ArrayList<>();
    String prefix;
    JDA jda;

    public CommandHandlerBuilder(JDA jda) {
        if(jda == null) {
            throw new NullArgumentException("JDA Instance is null, probably not provided.");
        }

        this.jda = jda;
    }

    public CommandHandlerBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public CommandHandlerBuilder addCommand(Command cmd) {
        commandList.add(cmd);
        return this;
    }

    public CommandHandler build() {
        return new CommandHandler(this);
    }

}
