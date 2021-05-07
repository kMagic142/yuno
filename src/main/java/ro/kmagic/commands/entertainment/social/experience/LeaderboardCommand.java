package ro.kmagic.commands.entertainment.social.experience;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.notfab.spigot.simpleconfig.Section;
import net.notfab.spigot.simpleconfig.SimpleConfig;
import ro.kmagic.Main;
import ro.kmagic.handlers.commands.CommandListener;
import ro.kmagic.users.UserManager;
import ro.kmagic.types.ModuleType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class LeaderboardCommand implements CommandListener {

    private HashMap<Integer, User> map;
    private final SimpleConfig messages = Main.getMessages();
    private final Section section = messages.getSection("COMMANDS.SOCIAL.EXPERIENCE.leaderboard");

    @Override
    public void onCommand(Member sender, TextChannel channel, Message message, String[] args) {
        EmbedBuilder embed = new EmbedBuilder();
        String description = "";

        int selfRank = 0;
        try {
            switch (args[1].toLowerCase()) {
                case "server": {
                    for (Member member : message.getGuild().getMembers()) {
                        if (member.getUser().isBot()) continue;

                        int exp = UserManager.getUser(member.getId()).getExperience();

                        map = new HashMap<>();
                        map.putIfAbsent(exp, member.getUser());
                    }

                    TreeMap<Integer, User> sorted = new TreeMap<>(map);
                    sorted.putAll(map);

                    for (int i = 0; i < map.size(); i++) {
                        int exp = (int) sorted.keySet().toArray()[i];
                        User user = sorted.get(exp);

                        int rank = i + 1;
                        int level = (int) (Math.floor(25 + (Math.sqrt(625 + 100 * exp))) / 50);

                        if (user.getId().equals(sender.getId())) {
                            selfRank = rank;
                        }


                        description += "`#" + rank + " " + user.getAsTag() + " [" + exp + " EXP - LEVEL " + level + "]`\n";
                    }

                    embed.setAuthor("Server Experience Leaderboard", null, Main.getJDA().getSelfUser().getAvatarUrl());
                    break;
                }
                case "global": {
                    UserManager.getUsers().forEach((k, member) -> {
                        if (member.getJdaUser().isBot()) return;

                        int exp = UserManager.getUser(member.getId()).getExperience();

                        map = new HashMap<>();
                        map.putIfAbsent(exp, member.getJdaUser());
                    });

                    TreeMap<Integer, User> sorted = new TreeMap<>(map);
                    sorted.putAll(map);

                    for (int i = 0; i < map.size(); i++) {
                        int exp = (int) sorted.keySet().toArray()[i];
                        User user = sorted.get(exp);


                        int rank = i + 1;
                        int level = (int) (Math.floor(25 + (Math.sqrt(625 + 100 * exp))) / 50);

                        if (user.getId().equals(sender.getId())) {
                            selfRank = rank;
                        }

                        description += "`#" + rank + " " + user.getAsTag() + " [" + exp + " EXP - LEVEL " + level + "]`\n";
                    }

                    embed.setAuthor("Global Experience Leaderboard", null, Main.getJDA().getSelfUser().getAvatarUrl());
                    break;
                }
            }
        } catch(ArrayIndexOutOfBoundsException arrayerr) {
            for (Member member : message.getGuild().getMembers()) {
                if (member.getUser().isBot()) continue;

                int exp = UserManager.getUser(member.getId()).getExperience();

                map = new HashMap<>();
                map.putIfAbsent(exp, member.getUser());

            }

            TreeMap<Integer, User> sorted = new TreeMap<>(map);
            sorted.putAll(map);

            for (int i = 0; i < map.size(); i++) {
                int exp = (int) sorted.keySet().toArray()[i];
                User user = sorted.get(exp);


                int rank = i + 1;
                int level = (int) (Math.floor(25 + (Math.sqrt(625 + 100 * exp))) / 50);

                if (user.getId().equals(sender.getId())) {
                    selfRank = rank;
                }
                description += "`#" + rank + " " + user.getAsTag() + " [" + exp + " EXP - LEVEL " + level + "]`\n";
            }

            embed.setAuthor("Server Experience Leaderboard", null, Main.getJDA().getSelfUser().getAvatarUrl());
        }

        embed.setColor(Color.BLACK);
        embed.setFooter(String.format(section.getString("rank"), selfRank));
        embed.setDescription(description);

        channel.sendMessage(embed.build()).queue();

    }

    private boolean enabled;

    @Override
    public String getCommandAlias() {
        return "exp leaderboard";
    }

    @Override
    public ArrayList<Permission> getCommandPermissions() {
        return null;
    }

    @Override
    public String getCommandDescription() {
        return "See who has the most EXP";
    }

    @Override
    public String getModuleName() {
        return "Experience Leaderboard";
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.EXPERIENCE;
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
