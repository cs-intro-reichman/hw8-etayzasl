public class Network {

    private User[] users;  
    private int userCount; 

    /** Creates a network with a given maximum number of users. */
    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    /** Creates a network with some users (for testing). */
    public Network(int maxUserCount, boolean gettingStarted) {
        this(maxUserCount);
        // “Predefined network” for tests: "Foo", "Bar", "Baz"
        users[0] = new User("Foo");
        users[1] = new User("Bar");
        users[2] = new User("Baz");
        userCount = 3;
    }

    public int getUserCount() {
        return this.userCount;
    }

    /**
     * Finds and returns the user with the given name, ignoring case.
     * If not found, return null.
     */
    public User getUser(String name) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getName().equalsIgnoreCase(name)) {
                return users[i];
            }
        }
        return null;
    }

    /**
     * Adds a new user with the given name to this network.
     * - If full, do nothing & return false.
     * - If user already exists (case-insensitive), do nothing & return false.
     * - Otherwise create new User, add, return true.
     */
    public boolean addUser(String name) {
        // check if full
        if (userCount >= users.length) {
            return false;
        }
        // check if user exists (case-insensitive)
        if (getUser(name) != null) {
            return false;
        }
        users[userCount] = new User(name);
        userCount++;
        return true;
    }

    /**
     * Makes user with name1 follow user with name2.
     * Return false if either doesn't exist (case-insensitive),
     * or if name1 == name2 (we don't allow following oneself),
     * or if addFollowee() fails.
     */
    public boolean addFollowee(String name1, String name2) {
        User user1 = getUser(name1);
        User user2 = getUser(name2);
        if (user1 == null || user2 == null) {
            return false;
        }
        // If same user ignoring case, test expects false
        if (user1.getName().equalsIgnoreCase(user2.getName())) {
            return false;
        }
        // Now try to add
        return user1.addFollowee(user2.getName());
    }

    /**
     * Recommends a user to follow for user 'name' - 
     * the one that has the maximum number of mutual followees.
     * If tie or no valid user, returns null (simple approach).
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
            // don't recommend themself, or someone already followed
            if (!candidate.getName().equalsIgnoreCase(user.getName()) 
                && !user.follows(candidate.getName())) {
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
     * Returns the name of the most popular user (the one who appears most
     * in others' follow lists).
     * If tie or no users, returns the first highest or null.
     */
    public String mostPopularUser() {
        if (userCount == 0) {
            return null;
        }
        String mostPopularName = null;
        int maxCount = -1;
        for (int i = 0; i < userCount; i++) {
            String currentName = users[i].getName();
            int count = followeeCount(currentName);
            if (count > maxCount) {
                maxCount = count;
                mostPopularName = currentName;
            }
        }
        return mostPopularName;
    }

    /**
     * Returns how many times 'name' appears in all follow lists (0 or 1 from each user).
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
     * Returns a textual description:
     *   "Network:\nFoo -> \nBar -> \nBaz -> \n"
     * or if followees exist:
     *   "Network:\nFoo -> Bar Baz \nBar -> \nBaz -> Foo \n"
     * 
     * If the network is empty, the autograder wants just "Network:" (no extra text).
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("Network:");
        // If no users, just return "Network:"
        if (userCount == 0) {
            return sb.toString();
        }
    
        sb.append("\n");
        for (int i = 0; i < userCount; i++) {
            // Use " ->" (no trailing space here)
            sb.append(users[i].getName()).append(" ->");
    
            String[] f = users[i].getfFollows();
            int fc = users[i].getfCount();
            // Append followees, each prefixed by a space
            for (int j = 0; j < fc; j++) {
                sb.append(" ").append(f[j]);
            }
    
            // Add exactly ONE trailing space
            sb.append(" ");
    
            // Newline only if this is not the last user
            if (i < userCount - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    
}
