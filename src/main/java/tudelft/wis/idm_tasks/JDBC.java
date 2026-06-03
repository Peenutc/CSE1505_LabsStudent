package tudelft.wis.idm_tasks;

import tudelft.wis.idm_tasks.basicJDBC.interfaces.JDBCManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class JDBC implements JDBCManager {
    private Connection connection;
    private String username = "postgres";
    private String password = "Huanchen6!";

    @Override
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if(connection== null){
            String connectionString =  "jdbc:postgresql://localhost:5432/imdb";
            Properties connectionProps = new Properties();
            connectionProps.put("user", this.username);
            connectionProps.put("password", this.password);
            connection = DriverManager.getConnection(connectionString, connectionProps);
        }

        return connection;
    }

    /**
     * Query 1: List all primary titles for a specific start year.
     */
    public Collection<String> getTitlesByYear(int year) throws SQLException{
        Collection<String> titles = new ArrayList();
        String sql = "SELECT primary_title FROM titles WHERE start_year = ?";

        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                titles.add(rs.getString("primary_title"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return titles;

    }

    /**
     * List all the job categories for titles that include a specific string in their primary title.
     */
    public Collection<String> getJobCategoriesForTitles(String title) throws SQLException{
        Collection<String> jobCategories = new ArrayList();
        String sql = "SELECT DISTINCT job_category " +
                    "FROM cast_info ci JOIN titles t ON ci.title_id = t.title_id " +
                    "WHERE t.primary_title = LIKE ?";

        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, "%"+title+"%");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                jobCategories.add(rs.getString("job_category"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return jobCategories;
    }

    /**
     * List the average runtime of a specified genre.
     * The method takes as a parameter a string of the specified genre and returns a double
     * corresponding to the average runtime of that genre
     */
    public double getAverageRuntime(String genre)  throws SQLException{
        double avgRuntime = 0.0;
        String sql = "SELECT AVG(t.runtime)" +
                "FROM titles t JOIN titles_genres tg" +
                "ON t.title_id = tg.title_id" +
                "WHERE t.genre = ?";

        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,genre);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                avgRuntime = rs.getDouble("AVG(t.runtime)");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return avgRuntime;
    }
    /**
     * Given a person’s full name, list all the characters they have played.
     * The method takes as a parameter a string of the person’s full name and returns a
     * collection of strings corresponding to the character names that person has played
     */

    public Collection<String> getPlayedCharactersGivenFullName(String fullname) throws SQLException{
        Collection<String> characters = new ArrayList();
        String sql = "SELECT tpc.character_name" +
                "FROM title_person_character tpc JOIN persons p" +
                "tpc.person_id = p.person_id";

        try(Connection conn =getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql))   {
            stmt.setString(1,fullname);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                characters.add(rs.getString("character_name"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return characters;
    }
}
