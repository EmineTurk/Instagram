import java.io.IOException;
import java.io.*;

public class Main {
    public static void main(String[] args) {

        AppControl manager = new AppControl();
        try (BufferedReader br = new BufferedReader(new FileReader("type1_small.txt"));
             BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String result = "";

                switch (parts[0]) {
                    case "create_user":
                        result = manager.CreateUser(parts[1]);
                        break;
                    case "follow_user":
                        result = manager.FollowUser(parts[1], parts[2]);
                        break;
                    case "unfollow_user":
                        result = manager.UnfollowUser(parts[1], parts[2]);
                        break;
                    case "create_post":
                        result = manager.CreatePost(parts[1], parts[2], parts[3]);
                        break;
                    case "see_post":
                        result = manager.SeePost(parts[1], parts[2]);
                        break;
                    case "toggle_like":
                        result = manager.ToggleLike(parts[1], parts[2]);
                        break;
                    case "generate_feed":
                        result = manager.GenerateFeed(parts[1], Integer.parseInt(parts[2]));
                        break;
                    case "scroll_through_feed":
                        int num = Integer.parseInt(parts[2]);
                        boolean[] likes = new boolean[num];
                        for (int i = 0; i < num; i++) {
                            likes[i] = parts[i + 3].equals("1");
                        }
                        result = manager.ScrollThroughFeed(parts[1], num, likes);
                        break;
                    case "sort_posts":
                        result = manager.SortPosts(parts[1]);
                        break;
                    case "see_all_posts_from_user":
                        result = manager.SeeAllPostsFromUser(parts[1], parts[2]);
                        break;
                }

                bw.write(result);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            System.err.println("File reading/writing error: " + e.getMessage());
        }


    }







}