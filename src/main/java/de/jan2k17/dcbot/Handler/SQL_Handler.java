package de.jan2k17.dcbot.Handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public static void removeGW(String GuildID, String msgID){
        try{
            PreparedStatement st = MySQL.con.prepareStatement("DELETE FROM giveaways WHERE msgid = ? AND guild = ?;");
            st.setString(1, msgID);
            st.setString(2, GuildID);
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
                return "" + s1 + "-" + s2 + "-" + s3 + "";
            } else {
                createUserStats(guild, userID);
                return "0-0-0";
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return "0-0-0";
    }
}
