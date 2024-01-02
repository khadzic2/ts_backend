package ba.unsa.etf.ts.backend.security.authorization;

import ba.unsa.etf.ts.backend.security.entity.User;

public class UserDetails {

    private User user;

    public UserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
