package at.fhv.teamd.rest.auth;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class User {

    private String token;

    public User(String token) {
        this.token = token;
    }

    public String authToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
