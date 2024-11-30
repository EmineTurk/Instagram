import java.util.ArrayList;
public class User {
    String userId;
    HashSet<String> followingIDs;
    HashSet<String> followerIDs;
    ArrayList<String> posts;
    HashSet<String> SeenPosts;

    public User(String userId) {
        this.userId = userId;
        this.followingIDs = new HashSet<>();
        this.followerIDs = new HashSet<>();
        this.posts = new ArrayList<>();
        this.SeenPosts = new HashSet<>();
    }
}