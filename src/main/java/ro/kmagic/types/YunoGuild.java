package ro.kmagic.types;

import net.dv8tion.jda.api.entities.Guild;

public class YunoGuild {

    private final Long id;
    private final Guild guildData;
    private Long experience, coins, level;

    public YunoGuild(Long id, Guild guildData) {
        this.id = id;
        this.guildData = guildData;
    }

    public YunoGuild(Long id, Guild guildData, Long experience, Long coins, Long level) {
        this.id = id;
        this.guildData = guildData;

    }

}
