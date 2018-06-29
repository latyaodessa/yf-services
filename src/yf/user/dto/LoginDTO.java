package yf.user.dto;

public class LoginDTO {
    private String user;
    private String password;

    public LoginDTO(String user, String password) {
        this.user = user;
        this.password = password;
    }

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
