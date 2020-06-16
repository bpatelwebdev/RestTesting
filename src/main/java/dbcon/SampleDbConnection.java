package dbcon;

import java.sql.*;

public class SampleDbConnection {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String conStr = "jdbc:mysql://localhost:3306/world";
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection(conStr,"root","admin");
        Statement statement = con.createStatement();
        ResultSet result = statement.executeQuery("select * from country where continent = 'Asia'");
        while (result.next()){
            System.out.println(result.getString("name"));
        }
        statement.close();
        con.close();

    }
}
