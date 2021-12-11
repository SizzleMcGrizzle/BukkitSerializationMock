package me.p3074098.bukkitserializationmock;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class YamlConfigurationTest {

    @Test
    void basicTest() {
        File file = ConfigurationFileUtils.getApplicationFile("BukkitSerializationMock", "test.yml");

        YamlConfiguration serialize = new YamlConfiguration(file);

        List<String> list = Arrays.asList("John", "Sam", "Henry");

        serialize.set("int", 1);
        serialize.set("string", "John");
        serialize.set("list", list);

        serialize.save();

        YamlConfiguration deserialize = new YamlConfiguration(file);

        deserialize.load();

        int i = deserialize.getInt("int");
        String s = deserialize.getString("string");
        List<String> list1 = deserialize.getList("list");

        assertEquals(1, i);
        assertEquals("John", s);
        assertEquals(list, list1);
    }

    @Test
    void configSerializableTest() {
        File file = ConfigurationFileUtils.getApplicationFile(
                "BukkitSerializationMock",
                "configSerializableTest.yml");

        Person person0 = new Person("John", 15);
        Person person1 = new Person("Andrew", 15);
        Person person2 = new Person("Samuel", 15);

        List<Person> list = Arrays.asList(person0, person1, person2);

        YamlConfiguration serialize = new YamlConfiguration(file);

        serialize.set("person", person0);
        serialize.set("people", list);

        serialize.save();

        YamlConfiguration deserialize = new YamlConfiguration(file);

        deserialize.load();

        Object testPerson = deserialize.get("person");
        List<Object> testList = deserialize.getList("people");

        assertTrue(testPerson instanceof Person);
        assertEquals(testPerson, person0);
        assertTrue(() -> {
            for (int i = 0; i < testList.size(); i++) {
                Person expected = list.get(i);
                Person actual = (Person) testList.get(i);

                if (!expected.equals(actual))
                    return false;
            }

            return true;
        });
    }

}