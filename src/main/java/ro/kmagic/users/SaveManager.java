package ro.kmagic.users;

import ro.kmagic.Main;

import java.util.concurrent.TimeUnit;

public class SaveManager {

    public SaveManager() {
        startSaving();
    }

    private static void startSaving() {
        Main.getScheduler().scheduleWithFixedDelay(UserManager::saveAllUsers, 5, 30, TimeUnit.MINUTES);
    }

}
