
public class Post {
    String postId;
    String userId;
    String content;
    HashSet<String> likes;

    public Post(String postId, String userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.likes = new HashSet<>();
    }

    public int getLikeCount() {
        return likes.size();
    }
}