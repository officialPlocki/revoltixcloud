package me.revoltix.cloudsystem.utils.mysql;

import java.sql.*;

public class MySQL {
    public static Connection con;
    public static void connect(String host, String user, String database, String password, String port){
        if(isConnected()){
            return;
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + password + "&autoReconnect=true");
        } catch (SQLException throwables) {
        }
    }

    public static void setMaxConnections() {
        try {
            PreparedStatement st = con.prepareStatement("SET GLOBAL MAX_CONNECTIONS = 500");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        if(isConnected()){
            try {
                con.close();
            } catch (SQLException throwables) {
            }
        } else {
        }
    }

    public static boolean isConnected(){
        return (con ==null ? false : true);
    }

    public MySQL() {}

    private PreparedStatement ps;

    public Connection getConnection() {
        return con;
    }

    public ResultSet getResult(PreparedStatement sql) {
        try {
            return sql.executeQuery();
        } catch (SQLException throwables) {
        }
        return null;
    }

    public void executeUpdate(PreparedStatement sql) {
        try {
            sql.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
