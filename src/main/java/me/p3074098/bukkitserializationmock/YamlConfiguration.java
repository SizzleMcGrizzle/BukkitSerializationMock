package me.p3074098.bukkitserializationmock;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class YamlConfiguration extends MemorySection {

    private final File file;

    public YamlConfiguration(File file) {
        this.file = file;
    }

    public void load() {
        try {
            InputStream in = new FileInputStream(file);
            Yaml yaml = new Yaml();

            Map<String, Object> map = yaml.load(in);

            setMap(map);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        save(file);
    }

    public void save(File file) {
        try {

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            
            FileWriter writer = new FileWriter(file);

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);

            Yaml yaml = new Yaml(options);
            yaml.dump(getMap(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
