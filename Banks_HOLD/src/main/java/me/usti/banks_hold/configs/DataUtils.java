// 
// Decompiled by Procyon v0.5.36
// 

package me.usti.banks_hold.configs;

import java.sql.*;
import java.util.Set;

import me.usti.banks_hold.Database;
import me.usti.banks_hold.RusBank;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

public class DataUtils
{
    private static final DecimalFormat format;
    public static int getSum(final String name, final String bank) {
        Database database = RusBank.inst.getDatabase();
        int balanc= 0;
        try {
            Statement statement = database.getConnection().createStatement();
            String sql = "SELECT * FROM bank  WHERE Users = '"  + name + "' AND bank = '" + bank+"'";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                balanc = resultSet.getInt("balance");
                statement.close();
                return balanc;
            }
            statement.close();
            return 0;
        } catch (SQLException e) {
            return 0;
        }
    }
    
    public static void addSum(final String name, final String bank, final int sum) {
        Database database = RusBank.inst.getDatabase();
        try {
            Statement statement = database.getConnection().createStatement();
            int bal = getSum(name, bank) + sum;
            String sql = "UPDATE bank SET balance = " +"'" + bal + "' WHERE " + "Users = " + "'" + name +"' AND bank = '" + bank + "'";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
        }
    }
    
    public static void addPercent(final String name, final String bank) {
        final double percent = Banks.getConfig().getDouble(bank + ".percent");
        final String path = name + "." + bank + ".deposit";
        final double bal = Integer.parseInt(DataUtils.format.format(Data.getConfig().getInt(path) * (1.0 + percent / 100.0)));
        Data.getConfig().set(path, (Object)bal);
        Data.save();
    }
    
    public static void takeSum(final String name, final String bank, final int sum) {
        Database database = RusBank.inst.getDatabase();
        try {
            Statement statement = database.getConnection().createStatement();
            int bal = getSum(name, bank) - sum;
            String sql = "UPDATE bank SET balance = " +"'" + bal + "' WHERE " + "Users = " + "'" + name +"' AND bank = '" + bank + "'";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
        }
    }
    
    public static boolean hasName(final String name) {
        return Data.getConfig().getKeys(false).contains(name);
    }
    
    public static Set<String> getPlayers() {
        return (Set<String>)Data.getConfig().getKeys(false);
    }
    
    public static boolean hasBank(final String name, final String bank) {
        Database database = RusBank.inst.getDatabase();
        try {
            Statement statement = database.getConnection().createStatement();
            String sql = "SELECT * FROM bank WHERE Users = '" + name + "' AND bank = '" +bank + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return true;
            }
            statement.close();
            return false;
        } catch (SQLException e) {
            return false;
        }

    }
    
    public static double getPercent(final String bank) {
        return Banks.getConfig().getInt(bank + ".percent");
    }
    
    public static double percentSum(final String name, final String bank) {
        final double percent = Banks.getConfig().getDouble(bank + ".percent");
        final String path = name + "." + bank + ".deposit";
        return Data.getConfig().getInt(path) * (1 + percent / 100);
    }
    
    public static void registerPlayer(final String name, final String bank) {
        Database database = RusBank.inst.getDatabase();
        try {
           PreparedStatement statement = database.getConnection().prepareStatement("INSERT INTO bank (Users, bank, balance) VALUES (?, ?, ?)");
           statement.setString(1,name);
           statement.setString(2,bank);
           statement.setInt(3, 0);
           statement.executeUpdate();
           statement.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }


    }

    
    static {
        format = new DecimalFormat("#0");
    }
}
