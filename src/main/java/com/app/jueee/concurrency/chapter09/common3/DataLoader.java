package com.app.jueee.concurrency.chapter09.common3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static List<Person> load(String path, String fileName) {
        Path file = Paths.get(path, fileName);
        List<Person> dataSet = new ArrayList<>();
        try (InputStream in = Files.newInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String data[] = line.split("-");
                Person person = new Person();
                person.setId(data[0].intern());
                data = data[1].split(",");
                for (String contact : data) {
                    person.addContact(contact.intern());
                }
                dataSet.add(person);
            }
        } catch (IOException x) {
            x.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSet;

    }

}
