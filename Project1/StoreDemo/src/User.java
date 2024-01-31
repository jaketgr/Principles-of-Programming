public class User {
    private int userID;
    private String userName;
    private String password;
    private String displayName;
    private boolean isManager;

    public int getUserID() {
        return userID;
    }
// function for userID
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }
//function for username
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }
//function for displayname
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }
// function for password
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isManager() {
        return isManager;
    }
// function for manager
    public void setManager(boolean manager) {
        isManager = manager;
    }
}
