package tudelft.wis.idm_tasks.basicJDBC;

import tudelft.wis.idm_tasks.JDBC;

import java.util.Collection;

public class main {
    public static void main(String[] args) {
        // 1. Instantiate your concrete manager class
        JDBC manager = new JDBC();

        System.out.println("--- Starting IMDB Database Queries --- \n");

        try {
            // 2. Test Query 1: Titles by Year
            int testYear = 2023;
            System.out.println("Query 1: Fetching titles from " + testYear + "...");
            Collection<String> titles = manager.getTitlesByYear(testYear);
            // Print out the first 5 results so your console isn't flooded
            titles.stream().limit(5).forEach(title -> System.out.println(" - " + title));
            System.out.println("Total found: " + titles.size() + "\n");

            // 3. Test Query 2: Job Categories by Title Keyword
            String keyword = "Wars";
            System.out.println("Query 2: Fetching job categories for titles containing '" + keyword + "'...");
            Collection<String> categories = manager.getJobCategoriesForTitles(keyword);
            categories.forEach(cat -> System.out.println(" - " + cat));
            System.out.println("\n");

            // 4. Test Query 3: Average Runtime
            String genre = "Sci-Fi";
            System.out.println("Query 3: Calculating average runtime for genre: " + genre);
            double avgRuntime = manager.getAverageRuntime(genre);
            System.out.printf("Average runtime: %.2f minutes\n\n", avgRuntime);

            // 5. Test Query 4: Characters by Actor Name
            String actor = "Tom Hanks";
            System.out.println("Query 4: Fetching characters played by " + actor + "...");
            Collection<String> characters = manager.getPlayedCharactersGivenFullName(actor);
            characters.stream().limit(5).forEach(character -> System.out.println(" - " + character));
            System.out.println("Total characters found: " + characters.size() + "\n");

        } catch (Exception e) {
            System.err.println("❌ An error occurred while running queries:");
            e.printStackTrace();
        }
    }
}