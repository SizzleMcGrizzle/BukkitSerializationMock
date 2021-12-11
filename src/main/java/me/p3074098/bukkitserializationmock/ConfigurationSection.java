package me.p3074098.bukkitserializationmock;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConfigurationSection {

    boolean contains(String path);

    Object get(String path, Object def);

    Object get(String path);

    String getString(String path);

    String getString(String path, String def);

    double getDouble(String path);

    double getDouble(String path, double def);

    boolean getBoolean(String path);

    boolean getBoolean(String path, boolean def);

    int getInt(String path);

    int getInt(String path, double def);

    boolean isInstance(String path, Class<?> clazz);

    ConfigurationSection getConfigurationSection(String path);

    boolean hasConfigurationSection(String path);

    <T> List<T> getList(String path);

    <T> List<T> getList(String path, List<T> def);

    ConfigurationSection createConfigurationSection(String path);

    ConfigurationSection createConfigurationSection(String path, Map<String, Object> map);

    Set<String> getKeys();

    /**
     *
     * @param path key to be set at
     * @param obj object to be set
     * @return the previous object, or null if non existant.
     */
    Object set(String path, Object obj);

}
