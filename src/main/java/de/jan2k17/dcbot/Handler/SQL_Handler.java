package de.jan2k17.dcbot.Handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SQL_Handler {
    public static String getAutoRole(String GuildID) {
        try {
            PreparedStatement st = MySQL.con.prepareStatement("SELECT autorole FROM settings WHERE guild = " + GuildID + ";");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("autorole");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }
    public static void updateAutoRole(String GuildID, String RoleID) {
        try {
            PreparedStatement st = MySQL.con.prepareStatement("UPDATE settings SET autorole = " + RoleID + " WHERE guild = " + GuildID + ";");
            st.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static String getLogChannel(String GuildID) {
        try {
            PreparedStatement st = MySQL.con.prepareStatement("SELECT logch FROM settings WHERE guild = " + GuildID + ";");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("logch");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }
    public static void updateLogChannel(String GuildID, String chID) {
        try {
            PreparedStatement st = MySQL.con.prepareStatement("UPDATE settings SET logch = " + chID + " WHERE guild = " + GuildID + ";");
            st.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void addGW(String GuildID, String message, String endDate, String hoster, String msgID){
        try {
            PreparedStatement st = MySQL.con.prepareStatement("INSERT INTO giveaways (guild,message,enddate,hoster,msgid) VALUES (?,?,?,?,?);");
            st.setString(1, GuildID);
            st.setString(2, message);
            st.setString(3, endDate);
            st.setString(4, hoster);
            st.setString(5, msgID);
            st.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /*
    *
    * USERSTATS
    *
    */
    public static void createUserStats(String guild, String userID){
        try{
            PreparedStatement st = MySQL.con.prepareStatement("INSERT INTO userstats (guild,user) VALUES (?,?);");
            st.setString(1, guild);
            st.setString(2, userID);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void addStats(String guild, String userID, String stat, int amount){
        try{
            String[] stats = getUserStats(guild, userID).split("-");
            PreparedStatement st;
            if(stat.equalsIgnoreCase("messages")){
                int i = Integer.parseInt(stats[0]) + amount;
                st = MySQL.con.prepareStatement("UPDATE userstats SET messages = ? WHERE guild = ? AND user = ?;");
                st.setInt(1, i);
                st.setString(2, guild);
                st.setString(3, userID);
                st.executeUpdate();
            } else if(stat.equalsIgnoreCase("reactions")){
                int i = Integer.parseInt(stats[1]) + amount;
                st = MySQL.con.prepareStatement("UPDATE userstats SET reactions = ? WHERE guild = ? AND user = ?;");
                st.setInt(1, i);
                st.setString(2, guild);
                st.setString(3, userID);
                st.executeUpdate();
            } else if(stat.equalsIgnoreCase("voice")){
                int i = Integer.parseInt(stats[2]) + amount;
                st = MySQL.con.prepareStatement("UPDATE userstats SET voice = ? WHERE guild = ? AND user = ?;");
                st.setInt(1, i);
                st.setString(2, guild);
                st.setString(3, userID);
                st.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String getUserStats(String guild, String userID){
        try{
            PreparedStatement st = MySQL.con.prepareStatement("SELECT * FROM userstats WHERE guild = ? AND user = ?;");
            st.setString(1, guild);
            st.setString(2, userID);

            ResultSet rs = st.executeQuery();
            if(rs.next()){
                int s1 = rs.getInt("messages");
                int s2 = rs.getInt("reactions");
                int s3 = rs.getInt("voice");
                return s1 + "-" + s2 + "-" + s3;
            } else {
                createUserStats(guild, userID);
                return "0-0-0";
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return "0-0-0";
    }
    /*
    *
    * COINS
    *
    */
    public static int getCoins(String guild, String userID){
        try{
            PreparedStatement st = MySQL.con.prepareStatement("SELECT coin FROM usercoins WHERE guild = ? AND user = ?;");
            st.setString(1, guild);
            st.setString(2, userID);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getInt("coin");
            } else {
                st = MySQL.con.prepareStatement("INSERT INTO usercoins (guild,user,coin) VALUES (?,?,?);");
                st.setString(1, guild);
                st.setString(2, userID);
                st.setInt(3, 0);
                st.executeUpdate();

                return 0;
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public static void addCoin(String guild, String userID, int amount){
        try{
            PreparedStatement st = MySQL.con.prepareStatement("UPDATE usercoins SET coin = ? WHERE guild = ? AND user = ?;");
            st.setInt(1, (getCoins(guild, userID) + amount));
            st.setString(2, guild);
            st.setString(3, userID);
            st.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void delCoin(String guild, String userID, int amount){
        try{
            PreparedStatement st = MySQL.con.prepareStatement("UPDATE usercoins SET coin = ? WHERE guild = ? AND user = ?;");
            st.setInt(1, (getCoins(guild, userID) - amount));
            st.setString(2, guild);
            st.setString(3, userID);
            st.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    /*
     *
     * SHOP
     *
     */
    public static void addShopItem(String guild, String itemname, String cost){
        try{
            PreparedStatement st = MySQL.con.prepareStatement("INSERT INTO shop (guild,item_name,item_cost) VALUES (?,?,?)");
            st.setString(1, guild);
            st.setString(2, itemname);
            st.setInt(3, Integer.parseInt(cost));
            st.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void delShopItem(String guild, String itemname, int cost){
        try{
            PreparedStatement st = MySQL.con.prepareStatement("DELETE FROM shop WHERE item_name = ? AND item_cost = ? AND guild = ?");
            st.setString(1, itemname);
            st.setInt(2, cost);
            st.setString(3, guild);
            st.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static int getCountItems(String guild){
        try{
            PreparedStatement st = MySQL.con.prepareStatement("SELECT COUNT(*) as \"items\" FROM shop WHERE guild = ?;");
            st.setString(1, guild);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getInt("items");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public static HashMap<String, Integer> getShopItems(String guild){
        try{
            PreparedStatement st = MySQL.con.prepareStatement("SELECT * FROM shop WHERE guild = ?;");
            st.setString(1, guild);
            ResultSet rs = st.executeQuery();
            HashMap<String, Integer> shopItems = new HashMap<>();
            while(rs.next()){
                shopItems.put(rs.getString("item_name"), rs.getInt("item_cost"));
            }
            return shopItems;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
