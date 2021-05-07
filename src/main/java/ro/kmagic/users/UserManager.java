package ro.kmagic.users;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import ro.kmagic.Main;
import ro.kmagic.handlers.data.DBManager;
import ro.kmagic.types.YunoUser;

import java.sql.SQLException;
import java.util.HashMap;

public class UserManager {

    private static HashMap<String, YunoUser> cache;
    private static final DBManager db = Main.getDBManager();

    public static void addUser(String id, User jdaUser) {
        YunoUser user = new YunoUser(id, jdaUser);
        cache.put(id, user);
        loadUser(user);
    }

    public static void addUser(String id, User jdaUser, int experience, int coins, int level) {
        YunoUser user = new YunoUser(id, jdaUser, experience, coins, level);
        cache.put(id, user);
    }

    public static YunoUser getUser(String id) {
        return cache.get(id);
    }

    public static boolean userExists(String id) {
        return cache.get(id) != null;
    }

    public static void removeUser(String id) {
        cache.remove(id);
    }

    public static void removeUser(YunoUser user) {
        cache.remove(user.getId());
    }

    public static void saveUserdata(YunoUser user) {
        if(!db.exists("yuno_users", "id", user.getId())) {
            db.insert("yuno_users", new String[]{"id", "experience", "level", "coins"}, new Object[]{user.getId(), 0, 1, 0});
            user.setExperience(1);
            user.setLevel(1);
            user.setCoins(0);
            cache.put(user.getId(), user);
        }

        db.setInt("yuno_users", "coins", "id", user.getCoins(), user.getId());
        db.setInt("yuno_users", "experience", "id", user.getExperience(), user.getId());
        db.setInt("yuno_users", "level", "id", user.getLevel(), user.getId());
    }

    public static void saveAllUsers() {
        cache.forEach((id, user) -> {
            db.setInt("yuno_users", "coins", "id", user.getCoins(), id);
            db.setInt("yuno_users", "experience", "id", user.getExperience(), id);
            db.setInt("yuno_users", "level", "id", user.getLevel(), id);
        });
    }

    public static void loadUser(YunoUser user) {
        try {
            user.setLevel(db.getInt("yuno_users", "level", "id", user.getId()));
            user.setExperience(db.getInt("yuno_users", "experience", "id", user.getId()));
            user.setCoins(db.getInt("yuno_users", "coins", "id", user.getId()));
        } catch (SQLException | NullPointerException err) {
            saveUserdata(new YunoUser(user.getId(), user.getJdaUser(), 0, 0, 1));
        }
    }

    public static void loadUsers(JDA jda) {
        cache = new HashMap<>();
        for(User user : jda.getUsers()) {
            addUser(user.getId(), user);
        }
    }

    public static HashMap<String, YunoUser> getUsers() {
        return cache;
    }


}
