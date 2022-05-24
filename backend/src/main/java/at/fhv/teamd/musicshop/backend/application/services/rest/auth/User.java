package at.fhv.teamd.musicshop.backend.application.services.rest.auth;


public class User {

    private String username;

    public User() {}

    public User(String username) {
        this.username = username;
    }

    public String name() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }
}
