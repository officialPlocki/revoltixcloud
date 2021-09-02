package me.revoltix.cloudsystem.utils.mysql;

import me.revoltix.cloudsystem.RevoltixCloud;

import java.sql.Connection;

public class Communication {

    private final MySQL mysql = new MySQL();
    private final Connection con = mysql.getConnection();

    private String id;

    public Communication(String id) {
        this.id = id;
    }

    public void stopServer(String id) {

    }

    public void startServer(String group, int amount) {

    }

    public void getServers() {

    }

    public void getGroups() {

    }

    public void getServerStatus() {

    }

    public void createNewGroup(String name, int ram, int maxplayers, String type) {

    }

    public void sendPlayer(String player, String id) {

    }

    public void teleportPlayer(String from, String to) {

    }

}
