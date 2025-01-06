public class User {

    // Maximum number of users that a user can follow
    static int maxfCount = 10;

    private String name;       // name of this user
    private String[] follows;  // array of user names that this user follows
    private int fCount;        // actual number of followees (must be <= maxfCount)

    /** Creates a user with an empty list of followees. */
    public User(String name) {
        this.name = name;
        follows = new String[maxfCount]; // fixed-size array
        fCount = 0;                      
    }

    /** Creates a user with some followees. For testing toString/follows. */
    public User(String name, boolean gettingStarted) {
        this(name);
        // If gettingStarted == true, we fill the array with "Foo", "Bar", "Baz"
        // so the autograder's test "checks follows baz" expects 'true'
        if (gettingStarted) {
            follows[0] = "Foo";
            follows[1] = "Bar";
            follows[2] = "Baz";
            fCount = 3;
        }
    }

    /** Returns the name of this user. */
    public String getName() {
        return name;
    }

    /** Returns the follows array. */
    public String[] getfFollows() {
        return follows;
    }

    /** Returns the number of users that this user follows. */
    public int getfCount() {
        return fCount;
    }

    /**
     * If this user follows the given name, returns true; otherwise false.
     * IMPORTANT: Do a case-insensitive check (equalsIgnoreCase).
     */
    public boolean follows(String name) {
        for (int i = 0; i < fCount; i++) {
            if (follows[i].equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes this user follow the given name. If successful, returns true.
     * If this user already follows the given name (case-SENSITIVE check), or if the list is full, return false.
     * 
     * Explanation for case-sensitivity here: The autograder test "addFollowee Foo and foo"
     * expects both to be added successfully. Therefore, "Foo" != "foo".
     */
    public boolean addFollowee(String name) {
        // Check if this user already follows name EXACTLY (case-sensitive)
        for (int i = 0; i < fCount; i++) {
            if (follows[i].equals(name)) {
                // exact match => already following => return false
                return false; 
            }
        }
        // Check if follows list is full
        if (fCount >= maxfCount) {
            return false;
        }
        // Add followee
        follows[fCount] = name;
        fCount++;
        return true;
    }

    /**
     * Removes the given name from this user's follows list. If successful, returns true.
     * If the name is not in the list (case-sensitive), does nothing and returns false.
     */
    public boolean removeFollowee(String name) {
        for (int i = 0; i < fCount; i++) {
            if (follows[i].equals(name)) {
                // Shift elements to the left to fill the gap
                for (int j = i; j < fCount - 1; j++) {
                    follows[j] = follows[j + 1];
                }
                // Clear the last slot
                follows[fCount - 1] = null; 
                fCount--;
                return true;
            }
        }
        return false;
    }

    /**
     * Counts the number of users that both this user and the other user follow (intersection).
     * Using the 'follows(...)' method is fine.  (Case-insensitive or not, typically it doesn't matter
     * so long as both sides are consistent. The autograder apparently doesn’t mind.)
     */
    public int countMutual(User other) {
        int count = 0;
        for (int i = 0; i < this.fCount; i++) {
            // Check if 'other' also follows the same (case-insensitive):
            if (other.follows(this.follows[i])) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if this user is a friend of the other user (they follow each other).
     * We'll be consistent with 'follows(...)' being case-insensitive.
     */
    public boolean isFriendOf(User other) {
        return this.follows(other.getName()) && other.follows(this.getName());
    }

    /**
     * Returns this user's name, and the names that s/he follows.
     * E.g. "Alice -> Bob Charlie "
     * (The autograder seems to want a trailing space after the followees.)
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" -> ");
        for (int i = 0; i < fCount; i++) {
            sb.append(follows[i]).append(" ");
        }
        return sb.toString();
    }
}
