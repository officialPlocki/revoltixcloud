package me.revoltix.cloudsystem.utils.files.bukkit;

import java.util.Map;

public interface ConfigurationSerializable {

    Map<String, Object> serialize();
}