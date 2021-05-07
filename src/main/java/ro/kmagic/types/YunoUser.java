package ro.kmagic.types;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;

public class YunoUser {

    @Getter
    private final String id;
    @Getter
    private final User jdaUser;
    @Getter @Setter
    private int experience, coins, level = 0;

    public YunoUser(String id, User userData) {
        this.id = id;
        this.jdaUser = userData;
    }

    public YunoUser(String id, User userData, int experience, int coins, int level) {
        this.id = id;
        this.jdaUser = userData;
        this.experience = experience;
        this.coins = coins;
        this.level = level;
    }

}
