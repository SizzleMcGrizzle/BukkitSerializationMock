package me.p3074098.bukkitserializationmock;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Person implements ConfigurationSerializable {

    static {
        ConfigurationSerialization.registerClass(Person.class);
    }

    private String name;
    private int age;
    private Person father;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.age = (int) map.get("age");
        this.father = map.containsKey("father") ? (Person) map.get("father") : null;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
    
    public Person getFather() {
        return father;
    }
    
    public void setFather(Person father) {
        this.father = father;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("name", name);
        map.put("age", age);
        
        if (father != null)
            map.put("father", father);

        return map;
    }


}
