package me.revoltix.cloudsystem.utils.mysql;

import me.revoltix.cloudsystem.utils.files.CloudInstanceConfiguration;
import me.revoltix.cloudsystem.utils.files.CloudInstanceGroupConfiguration;
import me.revoltix.cloudsystem.utils.instances.GroupTypes;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Updater {

    public static void update() {
        final MySQL mysql = new MySQL();
        final Connection con = mysql.getConnection();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            //OUTGOING

            //INCOMING
            try {
                /*
                mysql.executeUpdate(con.prepareStatement("CREATE TABLE IF NOT EXISTS msg(fromS TEXT, toS TEXT, msg TEXT)"));
                mysql.executeUpdate(con.prepareStatement("CREATE TABLE IF NOT EXISTS proxys(list TEXT)"));
                mysql.executeUpdate(con.prepareStatement("CREATE TABLE IF NOT EXISTS servers(list TEXT)"));
                mysql.executeUpdate(con.prepareStatement("CREATE TABLE IF NOT EXISTS Cgroups(list TEXT)"));
                mysql.executeUpdate(con.prepareStatement("CREATE TABLE IF NOT EXISTS info(toS TEXT, info TEXT)"));*/

                ResultSet rs = mysql.getResult(con.prepareStatement("SELECT * FROM msg WHERE toS = 'cloud'"));
                if(rs.next()) {
                    String msg = rs.getString("msg");
                    String[] slist = msg.split(" ");

                    if(slist[0].equalsIgnoreCase("startServer")) {
                        String l = slist[1];
                        boolean r = false;
                        for(String g : CloudInstanceGroupConfiguration.getGroups()) {
                            if(l.equalsIgnoreCase(g)) {
                                r = true;
                            }
                        }
                        if(r) {
                            int i = Integer.parseInt(slist[2]);
                            for(int a = 0; a<i; a++) {
                                CloudInstanceConfiguration.registerNewInstance(UUID.randomUUID().toString(), GroupTypes.valueOf(CloudInstanceGroupConfiguration.getGroupInfo(l).get("type")), l);
                            }
                        } else {
                            if(slist[2].isEmpty()) {
                                CloudInstanceConfiguration.registerNewInstance(UUID.randomUUID().toString(), GroupTypes.valueOf(l));
                            } else {
                                CloudInstanceConfiguration.registerNewInstance(slist[2], CloudInstanceConfiguration.getSavedDirectory(slist[2]), GroupTypes.DYNAMIC);
                            }
                        }
                    } else if(slist[0].equalsIgnoreCase("createGroup")) {
                        CloudInstanceGroupConfiguration.createGroup(slist[1], Integer.parseInt(slist[2]), Integer.parseInt(slist[3]), GroupTypes.valueOf(slist[4]));
                    } else if(slist[0].equalsIgnoreCase("getGroupInfo")) {
                        mysql.executeUpdate(con.prepareStatement("INSERT INTO info(toS, info) VALUES ('"+rs.getString("fromS")+"', '"+CloudInstanceGroupConfiguration.getGroupInfo(slist[1]).toString()+"')"));
                    } else if(slist[0].equalsIgnoreCase("getServerInfo")) {
                        mysql.executeUpdate(con.prepareStatement("INSERT INTO info(toS, info) VALUES ('"+rs.getString("fromS")+"', '"+CloudInstanceConfiguration.getInstanceInfo(slist[1])+"')"));
                    } else if(slist[0].equalsIgnoreCase("getServers")) {
                        if(slist[1].isEmpty()) {
                            List<String> l = new ArrayList<>();
                            for(String s : CloudInstanceGroupConfiguration.getGroups()) {
                                l.addAll(CloudInstanceGroupConfiguration.getOnlineServers(s));
                            }
                            mysql.executeUpdate(con.prepareStatement("INSERT INTO info(toS, info) VALUES ('"+rs.getString("fromS")+"', '"+String.join(", ", l)+"')"));
                        } else {
                            mysql.executeUpdate(con.prepareStatement("INSERT INTO info(toS, info) VALUES ('"+rs.getString("fromS")+"', '"+String.join(", ", CloudInstanceGroupConfiguration.getOnlineServers(slist[1]))+"')"));
                        }
                    } else if(slist[0].equalsIgnoreCase("getServerGroups")) {
                        mysql.executeUpdate(con.prepareStatement("INSERT INTO info(toS, info) VALUES ('"+rs.getString("fromS")+"', '"+String.join(", ", CloudInstanceGroupConfiguration.getGroups())+"')"));
                    } else if(slist[0].equalsIgnoreCase("deleteGroup")) {
                        CloudInstanceGroupConfiguration.deleteGroup(slist[1]);
                    } else if(slist[0].equalsIgnoreCase("stopServer")) {
                        CloudInstanceConfiguration.removeInstance(slist[1]);
                    } else if(slist[0].equalsIgnoreCase("getOwner")) {
                        mysql.executeUpdate(con.prepareStatement("INSERT INTO info(toS, info) VALUES ('"+rs.getString("fromS")+"', '"+CloudInstanceConfiguration.getOwner(slist[1])+"')"));
                    } else if(slist[0].equalsIgnoreCase("setOwner")) {
                        CloudInstanceConfiguration.setOwner(slist[1], slist[2]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 20, 20, TimeUnit.MILLISECONDS);
    }

    private static String serialize(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static Object deserialize(String s) throws IOException,
            ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

}
