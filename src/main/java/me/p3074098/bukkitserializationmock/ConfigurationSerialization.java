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
            
            HashMap<String, Object> newMap = new HashMap<>();
            
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                
                newMap.put(key, calculateMap(value));
            }
            
            return constructor.newInstance(map);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Object calculateMap(Object value) {
        if (value instanceof Map m) {
            if (m.containsKey(SERIALIZATION_KEY)) {
                Object object = m.get(SERIALIZATION_KEY);
            
                if (object instanceof String className) {
                    Constructor<? extends ConfigurationSerializable> constructor = getConstructor(className);
                    m.remove(SERIALIZATION_KEY);
                    return load(constructor, m);
                }
            }
        }
        
        return value;
    }
}
