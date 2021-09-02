package me.revoltix.cloudsystem.utils.port;

import me.revoltix.cloudsystem.RevoltixCloud;

public class PortManager {

    public PortManager() {

    }

    public int getNewPort() {
        if(RevoltixCloud.port >= 40000) {
            RevoltixCloud.port = 30000;
        }
        return (RevoltixCloud.port = RevoltixCloud.port++);
    }

}
