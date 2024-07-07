package me.usti.banks_hold;

import me.usti.banks_hold.configs.Data;
import me.usti.banks_hold.utils.Expansions;
import org.bukkit.Bukkit;

import java.sql.*;

public class Database {
    private Connection connection;
    public Connection getConnection() throws SQLException{
        if (connection != null){
            return this.connection;
        }
        String url = (String) Data.getConfig().get("url");
        String user = (String) Data.getConfig().get("user");
        String password = (String) Data.getConfig().get("password");

        connection = DriverManager.getConnection(url,user,password);
        return this.connection;
    }
    public void Tablecreate() throws SQLException{
        Statement statement = getConnection().createStatement();
        String sqlsbe = "CREATE TABLE IF NOT EXISTS bank(Users varchar(16)  ,bank varchar(16), balance int)";
        statement.execute(sqlsbe);
        String sqlpay = "CREATE TABLE IF NOT EXISTS pay(Users varchar(16) , action varchar(5) , num int ,sum int, target varchar(16) , bank varchar(16) , targetbank varchar(16), messeg varchar(100))";
        statement.execute(sqlpay);

            statement.close();

    }
    public int resultSetCount(ResultSet resultSet) throws SQLException{
        try{
            int i = 0;
            while (resultSet.next()) {
                i++;
            }
            return i;
        } catch (Exception e){
            System.out.println("Error getting row count");
            e.printStackTrace();
        }
        return 0;
    }
}
