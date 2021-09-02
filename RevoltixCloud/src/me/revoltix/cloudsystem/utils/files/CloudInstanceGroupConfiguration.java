package me.revoltix.cloudsystem.utils.files;

import me.revoltix.cloudsystem.utils.instances.GroupTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudInstanceGroupConfiguration {

    private static final FileBuilder fb = new FileBuilder("groups.list");
    private static final YamlConfiguration yml = fb.getYaml();

    public static void createGroup(String name, int ram, int maxplayers, GroupTypes type) {
        yml.set(name+".ram", ram);
        yml.set(name+".maxPlayers", maxplayers);
        List<String> onlineServerIDs = new ArrayList<String>();
        yml.set(name+".onlineServers", onlineServerIDs);
        yml.set(name+".groupType", type.name());
        List<String> groups = getGroups();
        groups.add(name);
        setGroups(groups);
        fb.save();
    }

    public static void deleteGroup(String name) {
        List<String> groups = getGroups();
        groups.remove(name);
        setGroups(groups);
    }

    public static void setGroups(List<String> list) {
        yml.set("list", list);
        fb.save();
    }

    public static List<String> getGroups() {
        if(yml.isSet("list")) {
            return yml.getStringList("list");
        } else {
            return new ArrayList<String>();
        }
    }

    public static HashMap<String, String> getGroupInfo(String name) {
        if(!getGroups().contains(name)) {
            return new HashMap<String, String>();
        }
        HashMap<String, String> info = new HashMap<String, String>();
        info.put("ram", String.valueOf(yml.getInt(name+".ram")));
        info.put("maxPlayers", String.valueOf(yml.getInt(name+".maxPlayers")));
        info.put("type", yml.getString(name+".groupType"));
        return info;
    }

    public static void addServer(String name, String id) {
        List<String> servers = getOnlineServers(name);
        servers.add(id);
        yml.set(name+".onlineServers", servers);
        fb.save();
    }

    public static void removeServer(String name, String id) {
        List<String> servers = getOnlineServers(name);
        servers.remove(id);
        yml.set(name+".onlineServers", servers);
        fb.save();
    }

    public static List<String> getOnlineServers(String name) {
        if(!getGroups().contains(name)) {
            return new ArrayList<String>();
        }
        return yml.getStringList(name+".onlineServers");
    }

}
