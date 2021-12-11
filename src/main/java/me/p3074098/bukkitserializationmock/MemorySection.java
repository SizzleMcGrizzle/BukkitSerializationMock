package me.p3074098.bukkitserializationmock;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

class MemorySection implements ConfigurationSection{

    private final Map<String, Object> map = new HashMap<>();

    protected MemorySection() {

    }

    protected MemorySection(Map<String, Object> keys) {
        setMap(keys);
    }

    protected void setMap(Map<String, Object> keys) {

        if (keys == null)
            return;

        for (Map.Entry<String, Object> entry : keys.entrySet()) {
            if (entry.getValue() instanceof Map map1)
                map.put(entry.getKey(), deserializeMap(map1, true));
            else if (entry.getValue() instanceof List<?> list)
                map.put(entry.getKey(), deserializeList(list));
            else
                map.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean contains(String path) {
        return map.containsKey(path);
    }

    @Override
    public Object get(String path, Object def) {
        return map.getOrDefault(path, def);
    }

    @Override
    public Object get(String path) {
        return map.get(path);
    }

    @Override
    public String getString(String path) {
        return (String) map.get(path);
    }

    @Override
    public String getString(String path, String def) {
        return (String) map.getOrDefault(path, def);
    }

    @Override
    public double getDouble(String path) {
        return (double) map.get(path);
    }

    @Override
    public double getDouble(String path, double def) {
        return (double) map.getOrDefault(path, def);
    }

    @Override
    public int getInt(String path) {
        return (int) map.get(path);
    }

    @Override
    public int getInt(String path, double def) {
        return (int) map.getOrDefault(path, def);
    }

    @Override
    public boolean getBoolean(String path) {
        return (boolean) map.get(path);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return (boolean) map.getOrDefault(path, def);
    }

    @Override
    public boolean isInstance(String path, Class<?> clazz) {
        return clazz.isInstance(map.get(path));
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path) {
        return (ConfigurationSection) map.get(path);
    }

    @Override
    public boolean hasConfigurationSection(String path) {
        return map.get(path) instanceof ConfigurationSection;
    }

    @Override
    public Set<String> getKeys() {
        return Collections.unmodifiableSet(map.keySet());
    }

    @Override
    public Object set(String path, Object obj) {
        return map.put(path, obj);
    }

    @Override
    public ConfigurationSection createConfigurationSection(String path) {
        return createConfigurationSection(path, new HashMap<>());
    }

    @Override
    public ConfigurationSection createConfigurationSection(String path, Map<String, Object> map) {
        MemorySection section = new MemorySection(map);
        map.put(path, section);
        return section;
    }

    @Override
    public <T> List<T> getList(String path) {
        return (List<T>) map.get(path);
    }

    @Override
    public <T> List<T> getList(String path, List<T> def) {
        return (List<T>) map.getOrDefault(path, def);
    }

    protected Map<String, Object> getMap() {
        HashMap<String, Object> newMap = new HashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object object = entry.getValue();
            String key = entry.getKey();

            newMap.put(key, serializeObject(object));
        }

        return newMap;
    }

    private Object serializeObject(Object object) {
        if (object instanceof MemorySection section)
            return section.getMap();
        if (object instanceof ConfigurationSerializable serializable) {
            return serializeConfigurationSerializable(serializable);
        }
        if (object instanceof List<?> list)
            return serializeList(list);

        return object;
    }

    private List<?> serializeList(List<?> list) {
        return list.stream().map(element -> {
            if (element instanceof ConfigurationSerializable serializable)
                return serializeConfigurationSerializable(serializable);
            if (element instanceof List<?> list1)
                return serializeList(list1);
            return element;
        }).collect(Collectors.toList());
    }

    private Object deserializeMap(Map<String, Object> map, boolean newMemorySection) {
        if (map.containsKey(ConfigurationSerialization.SERIALIZATION_KEY)) {
            Constructor<? extends ConfigurationSerializable> constructor = ConfigurationSerialization.getConstructor((String) map.get(ConfigurationSerialization.SERIALIZATION_KEY));
            map.remove(ConfigurationSerialization.SERIALIZATION_KEY);
            return ConfigurationSerialization.load(constructor, map);
        }

        if (newMemorySection)
        return new MemorySection(map);
        else
            return map;
    }

    private Object deserializeList(List<?> list) {
        return list.stream().map(element -> {
            if (element instanceof Map map)
                return deserializeMap(map, false);

            return element;
        }).collect(Collectors.toList());
    }

    private Map<String, Object> serializeConfigurationSerializable(ConfigurationSerializable configurationSerialization) {
        Map<String, Object> map = configurationSerialization.serialize();

        map.put(ConfigurationSerialization.SERIALIZATION_KEY, configurationSerialization.getClass().getName());

        return map;
    }




}
