package at.fhv.teamd.musicshop.backend.rest.auth;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class User {

    private String username;

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
