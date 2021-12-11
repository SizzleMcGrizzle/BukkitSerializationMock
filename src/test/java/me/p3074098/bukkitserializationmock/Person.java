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

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.age = (int) map.get("age");
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
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

        return map;
    }


}
