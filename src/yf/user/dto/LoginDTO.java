package yf.user.dto;

public class LoginDTO {
    String user;
    String password;

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setUser(final String user) {
        this.user = user;
    }
}
