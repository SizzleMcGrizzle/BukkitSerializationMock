package me.p3074098.bukkitserializationmock;

import me.p3074098.bukkitserializationmock.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class YamlConfigurationTest {

    @Test
    void testKeys() {
        File file = FileUtil.getApplicationFile("BukkitSerializationMock", "test.yml");

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
    void testConfigurationSerializable() {
        File file = FileUtil.getApplicationFile(
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
    
    @Test
    void testNestedKeys() {
        createTestNestedFile();
    
        File file = FileUtil.getApplicationFile(
                "BukkitSerializationMock",
                "nestedTest.yml");
        
        YamlConfiguration config = new YamlConfiguration(file);
        
        config.load();
        
        ConfigurationSection section = config.getConfigurationSection("persons");
        
        assertNotNull(section);
        
        Object testPerson0 = section.get("person0");
        List<Object> testPeople1 = section.getList("people");
        
        ConfigurationSection section2 = section.getConfigurationSection("individuals");
        
        assertNotNull(section2);
        
        Object testPerson1 = section2.get("person1");
        List<Object> testPeople2 = section2.getList("people");
        
        assertTrue(testPerson0 instanceof Person);
        assertTrue(isCollectionOfType(testPeople1, Person.class));
        assertTrue(testPerson1 instanceof Person);
        assertTrue(isCollectionOfType(testPeople2, Person.class));
        assertTrue(() -> {
            Person p = (Person) testPerson1;
            return p.getFather() != null && p.getFather().getFather() != null;
        });
    }
    
    private void createTestNestedFile() {
        File file = FileUtil.getApplicationFile(
                "BukkitSerializationMock",
                "nestedTest.yml");
        
        YamlConfiguration config = new YamlConfiguration(file);
        
        Person person0 = new Person("John", 5);
        Person person1 = new Person("Sarah", 16);
        Person person2 = new Person("Terrance", 50);
        Person person3 = new Person("Jackie", 23);
        Person person4 = new Person("Magster", 68);
        
        List<Person> personList = Arrays.asList(person0, person1, person2);
        
        ConfigurationSection section = config.createSection("persons");
        
        section.set("person0", person0);
        section.set("people", personList);
        
        ConfigurationSection section2 = section.createSection("individuals");
        
        person3.setFather(person4);
        person1.setFather(person3);
        
        section2.set("person1", person1);
        section2.set("people", personList);
        
        config.save();
    }
    
    private boolean isCollectionOfType(Collection<?> collection, Class<?> clazz) {
        return collection.stream().allMatch(clazz::isInstance);
    }

}