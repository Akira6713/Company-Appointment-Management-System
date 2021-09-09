package companyappointmentmanagementsystem.model;

/**
 * This class represents a single record in users table
 */
public class User {

    private int userId;
    private String userName;
    private String password;

    /**
     * Constructor method to create a new instance of an existing record
     *
     * @param userId user id
     * @param userName user name
     * @param password password
     */
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Returns user id of this user
     *
     * @return the user id
     */
    public int getUserId() { return userId; }

    /**
     * Returns username of this user
     *
     * @return the username
     */
    public String getUserName() { return userName; }

    /**
     * Returns password of this user
     *
     * @return the password
     */
    public String getPassword() { return password; }
}
