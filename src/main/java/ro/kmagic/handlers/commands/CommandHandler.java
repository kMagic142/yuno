package ro.kmagic.handlers.commands;

import java.util.ArrayList;

public class CommandHandler {
    private final CommandHandlerBuilder commandHandlerBuilder;

    CommandHandler(CommandHandlerBuilder commandHandlerBuilder) {
        this.commandHandlerBuilder = commandHandlerBuilder;
        commandHandlerBuilder.jda
                .addEventListener(new CommandHandlerListener(commandHandlerBuilder));
    }

    public void addCommand(Command commandLClass) {
        this.commandHandlerBuilder.commandList.add(commandLClass);
    }

    public ArrayList<Command> getCommands() {
        return this.commandHandlerBuilder.commandList;
    }
}
