package me.p3074098.bukkitserializationmock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationSerialization {

    protected static final String SERIALIZATION_KEY = "==";
    private static final Map<String, Class<? extends ConfigurationSerializable>> registered = new HashMap<>();
    
    public static void registerClass(Class<? extends ConfigurationSerializable> clazz) {
        registered.put(clazz.getName(), clazz);
    }
    
    protected static Constructor<? extends ConfigurationSerializable> getConstructor(String className) {
        Class<? extends ConfigurationSerializable> clazz = registered.get(className);
        
        try {
            return clazz.getConstructor(Map.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected static <T extends ConfigurationSerializable> T load(Constructor<T> constructor, Map<String, Object> map) {
        try {
            return constructor.newInstance(map);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
