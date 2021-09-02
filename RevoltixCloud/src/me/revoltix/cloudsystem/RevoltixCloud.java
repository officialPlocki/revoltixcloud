package me.revoltix.cloudsystem;

import me.revoltix.cloudsystem.utils.files.FileBuilder;
import me.revoltix.cloudsystem.utils.files.YamlConfiguration;
import me.revoltix.cloudsystem.utils.mysql.MySQL;
import me.revoltix.cloudsystem.utils.mysql.Updater;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RevoltixCloud {

    public static int port = 30000;

    public static void main(String[] args) throws SQLException {
        FileBuilder fb = new FileBuilder("mysql.cfg");
        fb.getYaml().set("mysql.host", "localhost");
        fb.getYaml().set("mysql.port", 3306);
        fb.getYaml().set("mysql.user", "root");
        fb.getYaml().set("mysql.password", "password");
        fb.getYaml().set("mysql.database", "db");
        fb.save();
        MySQL.connect(fb.getYaml().getString("mysql.host"), fb.getYaml().getString("mysql.user"), fb.getYaml().getString("mysql.database"), fb.getYaml().getString("mysql.password"), fb.getYaml().getString("mysql.port"));
        MySQL mysql = new MySQL();
        Connection con = mysql.getConnection();
        mysql.executeUpdate(con.prepareStatement("CREATE TABLE IF NOT EXISTS msg(fromS TEXT, toS TEXT, msg TEXT)"));
        mysql.executeUpdate(con.prepareStatement("CREATE TABLE IF NOT EXISTS proxys(list TEXT)"));
        mysql.executeUpdate(con.prepareStatement("CREATE TABLE IF NOT EXISTS servers(list TEXT)"));
        mysql.executeUpdate(con.prepareStatement("CREATE TABLE IF NOT EXISTS Cgroups(list TEXT)"));
        mysql.executeUpdate(con.prepareStatement("CREATE TABLE IF NOT EXISTS info(toS TEXT, info TEXT)"));
        mysql.executeUpdate(con.prepareStatement("CREATE TABLE IF NOT EXISTS serverOwn(list TEXT)"));
        Updater.update();
    }

}
