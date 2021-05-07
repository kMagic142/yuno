package ro.kmagic.handlers.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ro.kmagic.types.ModuleType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

class CommandHandlerListener extends ListenerAdapter {

    private final CommandHandlerBuilder commandHandlerBuilder;

    CommandHandlerListener(CommandHandlerBuilder commandHandlerBuilder) {
        this.commandHandlerBuilder = commandHandlerBuilder;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String message = event.getMessage().getContentRaw();
        String[] args = message.split("\\s+");
        String commandPrefix = commandHandlerBuilder.prefix;
        if (!message.startsWith(commandPrefix)) return;

        String argresult = String.join(" ", args).replace(commandPrefix, "");
        String[] finalArgs = argresult.split(" ");

        Optional<Command> cmd = commandHandlerBuilder.commandList.stream().filter(c -> {
            return argresult.startsWith(c.getModuleName().toLowerCase()) || (c.getCommandAlias() != null && argresult.startsWith(c.getCommandAlias().toLowerCase())); // String.join(" ", Arrays.copyOfRange(finalArgs, 0, 2)).toLowerCase()
        }).findFirst();
        cmd.ifPresent(command -> handleCommand(command, event.getMember(), event.getChannel(), event.getMessage(), finalArgs));
    }

    private void handleCommand(Command command, Member sender, TextChannel channel, Message message, String[] args) {
        ArrayList<Permission> neededPermissions = command.getCommandPermissions();
        if (neededPermissions != null && !neededPermissions.isEmpty() && !sender.getPermissions().containsAll(neededPermissions)) {
            return;
        }

        if(command.getModuleType() == ModuleType.ADMIN && !sender.getId().equals("731227820784877678"))
            return;

        command.getHandlerListener().onCommand(sender, channel, message, Arrays.copyOfRange(args, 1, args.length));
    }

}
