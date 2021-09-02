package me.revoltix.cloudsystem.utils.files;

import me.revoltix.cloudsystem.utils.instances.CloudStatus;
import me.revoltix.cloudsystem.utils.instances.GroupTypes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

public class CloudInstanceConfiguration {

    private static final FileBuilder fb = new FileBuilder("services.temp");
    private static final YamlConfiguration yml = fb.getYaml();

    public static void registerNewInstance(String id, String dir, GroupTypes type) {
        // ONLINE NETZWERK & CURRENT, SERVER ONLINE (ID, GRUPPE)
        yml.set(id+".online", CloudStatus.STARTING.name());
        yml.set(id+".players", 0);
        yml.set(id+".group", "dyn");
        yml.set(id+".type", type.name());
        fb.save();
    }

    public static void registerNewInstance(String id, GroupTypes type) {
        // ONLINE NETZWERK & CURRENT, SERVER ONLINE (ID, GRUPPE)
        yml.set(id+".online", CloudStatus.STARTING.name());
        yml.set(id+".players", 0);
        yml.set(id+".group", "dyn");
        yml.set(id+".type", type.name());
        fb.save();
    }

    public static void registerNewInstance(String id, GroupTypes type, String group) {
        // ONLINE NETZWERK & CURRENT, SERVER ONLINE (ID, GRUPPE)
        yml.set(id+".online", CloudStatus.STARTING.name());
        yml.set(id+".players", 0);
        yml.set(id+".group", group);
        yml.set(id+".type", type.name());
        fb.save();
        CloudInstanceGroupConfiguration.addServer(group, id);
    }

    public static void removeInstance(String id) {
        CloudInstanceGroupConfiguration.removeServer(yml.getString(id+".group"), id);
        saveInstance(id);
    }

    public static HashMap getInstanceInfo(String id) {
        HashMap<String, String> r = new HashMap<>();
        r.put("status", yml.getString(id+".online"));
        r.put("players", yml.getString(id+".players"));
        r.put("group", yml.getString(id+".group"));
        r.put("type", yml.getString(id+".type"));
        return r;
    }

    public static void updateStatus(String id, CloudStatus status) {
        yml.set(id+".online", status.name());
        fb.save();
    }

    public static void updatePlayerCount(String id, int players) {
        yml.set(id+".players", players);
        fb.save();
    }

    public static int getPlayerCount(String id) {
        return yml.getInt(id+".players");
    }

    public static String getGroup(String id) {
        return yml.getString(id+".group");
    }

    public static String getType(String id) {
        return yml.getString(id+".type");
    }

    public static CloudStatus getStatus(String id) {
        return CloudStatus.valueOf(yml.getString(id+".online"));
    }

    public static void saveInstance(String id) {
        if(yml.getString(id+".group").equalsIgnoreCase("dyn")) {
            String database = "savedServers/"+id+"/";
            try {
                copyDirectory(new File("servers/"+id+"/"), new File(database));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void copyDirectory(File sourceDirectory, File destinationDirectory) throws IOException {
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir();
        }
        for (String f : Objects.requireNonNull(sourceDirectory.list())) {
            copyDirectoryCompatibityMode(new File(sourceDirectory, f), new File(destinationDirectory, f));
        }
    }

    private static void copyDirectoryCompatibityMode(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            copyDirectory(source, destination);
        } else {
            copyFile(source, destination);
        }
    }

    public static String getSavedDirectory(String id) {
        return "savedServers/"+id+"/";
    }

    public static void setOwner(String id, String owner) {
        FileBuilder fb = new FileBuilder("ServerOwners.dbs");
        YamlConfiguration yml = fb.getYaml();
        yml.set(id+".owner", owner);
        fb.save();
    }

    public static String getOwner(String id) {
        FileBuilder fb = new FileBuilder("ServerOwners.dbs");
        YamlConfiguration yml = fb.getYaml();
        return yml.getString(id+".owner");
    }

    private static void copyFile(File sourceFile, File destinationFile) throws IOException {
        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(destinationFile)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }

}
