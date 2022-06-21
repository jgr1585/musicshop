package at.fhv.teamd.playlist.rest.auth;

import lombok.NoArgsConstructor;

import javax.enterprise.context.RequestScoped;

@RequestScoped
@NoArgsConstructor
public class Tokenholder {

    private String token;

    public String authToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
