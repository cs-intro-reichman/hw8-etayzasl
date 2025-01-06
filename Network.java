/** Represents a social network. The network has users, who follow other users.
 *  Each user is an instance of the User class. */
public class Network {

    // Fields
    private User[] users;  // the users in this network (an array of User objects)
    private int userCount; // actual number of users in this network

    /** Creates a network with a given maximum number of users. */
    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    /** Creates a network with some users. The only purpose of this constructor is 
     *  to allow testing the toString and getUser methods, before implementing other methods. */
    public Network(int maxUserCount, boolean gettingStarted) {
        this(maxUserCount);
        users[0] = new User("Foo");
        users[1] = new User("Bar");
        users[2] = new User("Baz");
        userCount = 3;
    }

    public int getUserCount() {
        return this.userCount;
    }

    /**
     * Finds in this network, and returns, the user that has the given name.
     * If there is no such user, returns null.
     */
    public User getUser(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equals(name)) {
                return users[i];
            }
        }
        return null;
    }

    /**
     * Adds a new user with the given name to this network.
     * If the network is full, does nothing and returns false;
     * If the given name is already a user in this network, does nothing and returns false;
     * Otherwise, creates a new user with the given name, adds the user to this network, and returns true.
     */
    public boolean addUser(String name) {
        // Check if network is full
        if (userCount >= users.length) {
            return false;
        }
        // Check if user already exists
        if (getUser(name) != null) {
            return false;
        }
        // Add new user
        users[userCount] = new User(name);
        userCount++;
        return true;
    }

    /**
     * Makes the user with name1 follow the user with name2. If successful, returns true.
     * If any of the two names is not a user in this network,
     * or if the "follows" addition failed for some reason, returns false.
     */
    public boolean addFollowee(String name1, String name2) {
        User user1 = getUser(name1);
        User user2 = getUser(name2);
        if (user1 == null || user2 == null) {
            return false;
        }
        // Attempt to add user2 as a followee to user1
        return user1.addFollowee(name2);
    }

    /**
     * For the user with the given name, recommends another user to follow. 
     * The recommended user is the one that has the maximum number of mutual followees
     * with the given user.
     * 
     * If there's a tie or if no valid recommendation is found (e.g., if user doesn't exist),
     * we return null for simplicity, or you could decide on some tie-breaking strategy.
     */
    public String recommendWhoToFollow(String name) {
        User user = getUser(name);
        if (user == null) {
            return null;
        }
        int maxMutual = -1;
        User recommended = null;
        
        for (int i = 0; i < userCount; i++) {
            User candidate = users[i];
            // Don't recommend the same user or someone already followed
            if (!candidate.getName().equals(name) && !user.follows(candidate.getName())) {
                int mutual = user.countMutual(candidate);
                if (mutual > maxMutual) {
                    maxMutual = mutual;
                    recommended = candidate;
                }
            }
        }
        
        return (recommended != null) ? recommended.getName() : null;
    }

    /**
     * Computes and returns the name of the most popular user in this network: 
     * The user who appears the most in the follow lists of all the users.
     * If there's a tie, returns one of them (depending on who appears first in iteration).
     * If no users in network, returns null.
     */
    public String mostPopularUser() {
        if (userCount == 0) {
            return null;
        }
        String mostPopularName = null;
        int maxCount = -1;
        for (int i = 0; i < userCount; i++) {
            String name = users[i].getName();
            int count = followeeCount(name);
            if (count > maxCount) {
                maxCount = count;
                mostPopularName = name;
            }
        }
        return mostPopularName;
    }

    /**
     * Returns the number of times that the given name appears in the follows lists
     * of all the users in this network. Note: A name can appear 0 or 1 times in each list.
     */
    private int followeeCount(String name) {
        int count = 0;
        for (int i = 0; i < userCount; i++) {
            if (users[i].follows(name)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns a textual description of all the users in this network, and who they follow.
     * Each line will show: "UserName -> follow1 follow2 ..."
     */
    public String toString() {
        if (userCount == 0) {
            return "No users in network.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < userCount; i++) {
            sb.append(users[i].toString()).append("\n");
        }
        return sb.toString();
    }
}
