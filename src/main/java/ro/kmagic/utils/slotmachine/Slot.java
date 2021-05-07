package ro.kmagic.utils.slotmachine;

public enum Slot {
    SEVEN("Seven", ":seven:", 30, 4, 1),
    CROWN("Crown", ":crown:", 10),
    BELL("Bell", ":bell:", 10),
    BAR("Bar", ":chocolate_bar:", 10),
    CHERRY("Cherry", ":cherries:", 10),
    MELON("Melon", ":melon:", 10);

    private final String name;
    private final String emote;
    private final int triplePayout;
    private final int doublePayout;
    private final int singlePayout;

    Slot(String name, String emote, int triplePayout) {
        this(name, emote, triplePayout, 0, 0);
    }

    Slot(String name, String emote, int triplePayout, int doublePayout, int singlePayout) {

        this.name = name;
        this.emote = emote;
        this.triplePayout = triplePayout;
        this.doublePayout = doublePayout;
        this.singlePayout = singlePayout;
    }

    public int getTriplePayout() {
        return triplePayout;
    }

    public String getEmote() {
        return emote;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return emote;
    }

    public int getDoublePayout() {
        return doublePayout;
    }

    public int getSinglePayout() {
        return singlePayout;
    }
}
