import java.util.ArrayList;
public class AppControl { // These hashmaps  store users and posts by their respective IDs.
    private HashMap<String, User> Allusers;
    private HashMap<String, Post> Allposts;


    public AppControl() {
        this.Allusers = new HashMap<>();
        this.Allposts = new HashMap<>();

    }

    public String CreateUser(String userId) {// Creates a new user.
        if (Allusers.containsKey(userId)) {// If the userId already exists, returns an error message.
            return "Some error occurred in create_user.";
        }
        Allusers.put(userId, new User(userId));//It creates user.Puts it in users hashmap.
        return "Created user with Id " + userId + ".";
    }

    public String FollowUser(String followerId, String followedId) {
        if (!Allusers.containsKey(followerId) || !Allusers.containsKey(followedId)) { // It checks if both the follower and the followed users exist.
            return "Some error occurred in follow_user.";
        }

        User follower = Allusers.get(followerId);
        if (follower.followingIDs.contains(followedId)||followedId.equals(followerId)) {//If user already follows followed ID or user tries to follow itself gives an error.
            return "Some error occurred in follow_user.";
        }

        follower.followingIDs.add(followedId);//It adds followed Id to follower's followings.
        Allusers.get(followedId).followerIDs.add(followerId);//It adds follower ıd to followed's followers.
        return followerId + " followed " + followedId + ".";
    }

    public String UnfollowUser(String followerId, String followedId) {
        if (!Allusers.containsKey(followerId) || !Allusers.containsKey(followedId)) {//If follower or followed is not exist gives error.
            return "Some error occurred in unfollow_user.";
        }

        User follower = Allusers.get(followerId);
        if (!follower.followingIDs.contains(followedId)) {//If follower is not following followed ıd gives an error.
            return "Some error occurred in unfollow_user.";
        }

        follower.followingIDs.remove(followedId);//It removes followed ıd from follower's following.
        Allusers.get(followedId).followerIDs.remove(followerId);//It removes follower ıd from followed's followers
        return followerId + " unfollowed " + followedId + ".";
    }

    public String CreatePost(String userId, String postId, String content) {
        if (!Allusers.containsKey(userId)) {//If user not exist gives an error.
            return "Some error occurred in create_post.";
        }

        if (Allposts.containsKey(postId)) {//If post already exist gives an error.
            return "Some error occurred in create_post.";
        }

        Post post = new Post(postId, userId, content);// It creates post.
        Allposts.put(postId, post);
        Allusers.get(userId).posts.add(postId);//adds post to user's posts.
        return userId + " created a post with Id " + postId + ".";
    }

    public String SeePost(String userId, String postId) {
        if (!Allusers.containsKey(userId) || !Allposts.containsKey(postId)) {//If user or post does not exist gives an error
            return "Some error occurred in see_post.";
        }

        Allusers.get(userId).SeenPosts.add(postId);//Adds post to user's seenposts.
        return userId + " saw " + postId + ".";
    }
    public String SeeAllPostsFromUser(String viewerId, String viewedId) {
        if (!Allusers.containsKey(viewerId) || !Allusers.containsKey(viewedId)) { // It Checks if both users exist.
            return "Some error occurred in see_all_posts_from_user.";
        }

        User viewer = Allusers.get(viewerId);
        User viewed = Allusers.get(viewedId);

        for (String postId : viewed.posts) { // It marks all posts from viewed user as seen by viewer
            viewer.SeenPosts.add(postId);
        }

        return viewerId + " saw all posts of " + viewedId + ".";
    }


    public String ToggleLike(String userId, String postId) {
        if (!Allusers.containsKey(userId) || !Allposts.containsKey(postId)) {//If user or post is not exist gives an error.
            return "Some error occurred in toggle_like.";
        }

        Post post = Allposts.get(postId);

        String result;
        if (post.likes.contains(userId)) {//If post was liked by user.Remove user ıd from post's likes.
            post.likes.remove(userId);
            result = userId + " unliked " + postId + ".";
        } else {
            post.likes.add(userId);//otherwise add user ıd to post's likes.
            result = userId + " liked " + postId + ".";
        }

        Allusers.get(userId).SeenPosts.add(postId);//adds post to user's seen posts.
        return result;
    }

    public String GenerateFeed(String userId, int num) {
        if (!Allusers.containsKey(userId)) {//If user not exist gives an error.
            return "Some error occurred in generate_feed.";
        }

        User user = Allusers.get(userId);
        ArrayList<Post> feed = new ArrayList<>();//creates a list to hold feed posts

        PriorityQueue<Post> followedUserPosts = new PriorityQueue<>((p1, p2) -> { // It creates a  priority queue for posts from  followed users.
            int likeDifference = p2.getLikeCount() - p1.getLikeCount();// Primary , It sorts by like count
            if (likeDifference == 0) { // If like counts are equal,It sorts by lexicographical post ID
                return p2.postId.compareTo(p1.postId);
            }
            return likeDifference;
        });

        for (String followedUserId : user.followingIDs) {// It collects only posts from followed users.
            User followedUser = Allusers.get(followedUserId);//It finds a followed user.
            for (String postId : followedUser.posts) {
                Post post = Allposts.get(postId);
                if (!user.SeenPosts.contains(postId)) {// It only adds posts not seen by the user
                    followedUserPosts.offer(post);
                }
            }
        }

        while (!followedUserPosts.isEmpty() && feed.size() < num) {// It generates feed from collected posts
            feed.add(followedUserPosts.poll());
        }

        StringBuilder result = new StringBuilder("Feed for " + userId + ":\n");
        for (Post post : feed) {
            result.append(String.format("Post ID: %s, Author: %s, Likes: %d\n",
                    post.postId, post.userId, post.getLikeCount()));
        }

        if (feed.size() < num) {// It adds message if not enough posts
            result.append("No more posts available for " + userId + ".");
        }

        return result.toString().trim();
    }
    public String ScrollThroughFeed(String userId, int num, boolean[] likes) {
        if (!Allusers.containsKey(userId)) {//It checks if user exist.
            return "Some error occurred in scroll_through_feed.";
        }
        User user = Allusers.get(userId);
        ArrayList<Post> feed = new ArrayList<>();
        PriorityQueue<Post> followedUserPosts = new PriorityQueue<>((p1, p2) -> { // It creates a  priority queue for posts from followed users
            int likeDifference = p2.getLikeCount() - p1.getLikeCount();// Primary  it sorts by like count .

            if (likeDifference == 0) { // If like counts are equal, sorts by lexicographical post ıd.
                return p2.postId.compareTo(p1.postId);
            }

            return likeDifference;
        });

        for (String followedUserId : user.followingIDs) {// It collects only posts from followed users
            User followedUser = Allusers.get(followedUserId);//finds a followed user.
            for (String postId : followedUser.posts) {//Traverses this user's posts.
                Post post = Allposts.get(postId);
                if (!user.SeenPosts.contains(postId)) { // It only adds posts not seen by the user
                    followedUserPosts.offer(post);
                }
            }
        }
        while (!followedUserPosts.isEmpty() && feed.size() < num) {// It generates feed from collected posts.
            feed.add(followedUserPosts.poll());
        }

        StringBuilder result = new StringBuilder(userId + " is scrolling through feed:\n");
        for (int i = 0; i < Math.min(num, feed.size()); i++) {
            Post post = feed.get(i);//takes a post from feed.
            user.SeenPosts.add(post.postId);//It marks it as seen by user.

            if (likes[i]) {
                if (!post.likes.contains(userId)) {//If post was not liked by user before then add user ıd to post's likes.
                    post.likes.add(userId);
                    result.append(userId).append(" saw ").append(post.postId)
                            .append(" while scrolling and clicked the like button.\n");
                }
            } else {
                result.append(userId).append(" saw ").append(post.postId)
                        .append(" while scrolling.\n");
            }
        }

        if (feed.size() < num) {//If there is not enough posts writes no more posts.
            result.append("No more posts in feed.");
        }

        return result.toString().trim();
    }
    public String SortPosts(String userId) {
        if (!Allusers.containsKey(userId)) {// It checks if user exists.
            return "Some error occurred in sort_posts.";
        }
        User user = Allusers.get(userId);

        if (user.posts.isEmpty()) {// It checks if user has no posts
            return "No posts from " + userId + ".";
        }

        PriorityQueue<Post> sortedPosts = new PriorityQueue<>((p1, p2) -> {// It creates a priority queue .
            int likeDifference = p2.getLikeCount() - p1.getLikeCount();//It primarily sort by like difference.
            if (likeDifference == 0) {//then it sorts according to lexicographical id's
                return p2.postId.compareTo(p1.postId);
            }
            return likeDifference;
        });

        for (String postId : user.posts) { // It adds all user's posts to the priority queue
            sortedPosts.offer(Allposts.get(postId));
        }

        StringBuilder result = new StringBuilder("Sorting " + userId + "'s posts:\n");

        while (!sortedPosts.isEmpty()) { // It polls posts from priority queue to get sorted order
            Post post = sortedPosts.poll();
            result.append(post.postId)
                    .append(", Likes: ")
                    .append(post.getLikeCount())
                    .append("\n");
        }
        return result.toString().trim();
    }




}