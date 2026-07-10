package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CreateDomains {
    public List<String> createDomains(String file) throws IOException {
        List<String> domainsList = new ArrayList<>();
        List<String> allFile = Files.readAllLines(Paths.get(file));

        for (int i = 0 ; i < allFile.size(); i++) {
            String line = allFile.get(i).trim();
            if (!line.isEmpty()) {
                domainsList.add(line);
            }
        }
        return domainsList;
    }
}
