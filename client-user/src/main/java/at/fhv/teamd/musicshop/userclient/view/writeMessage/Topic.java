package at.fhv.teamd.musicshop.userclient.view.writeMessage;

public enum Topic {
    ADMINISTRATIVE("Administrative"),
    ORDER("Order"),
    HIP_HOP("Hip Hop"),
    POP("Pop"),
    ROCK_N_ROLL("Rock 'n' Roll"),
    SOUL("Soul");

    private String name;

    Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
