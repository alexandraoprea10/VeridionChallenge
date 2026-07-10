package org.example;


import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    void main() throws IOException, InterruptedException {
        String domains = "domains.txt";
        String rules = "technologies.json";

        CreateRules helper = new CreateRules();
        CreateDomains helper2 = new CreateDomains();
        List<Technology> technologyList = helper.createAllRules(rules);
        List<String> domainList = helper2.createDomains(domains);
        // using a set instead of list to eliminate duplicates
        Set<String> allTechnologies = new HashSet<>();

        VerifyRules thirdHelper = new VerifyRules();
        for (int i = 0; i < domainList.size(); i++) {
            int currentNumber = i + 1;
            System.out.printf("Loading domain number " + currentNumber + ". Please Wait! ");
            // 20 second timeout to prevent the program from blocking
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();
            String currentDomain = domainList.get(i);
            try {
                thirdHelper.verifyTechnologies(client, currentDomain, technologyList, allTechnologies, thirdHelper.createHttpsURL(currentDomain));
            } catch (Exception e) {
                try {
                    thirdHelper.verifyTechnologies(client, currentDomain, technologyList, allTechnologies, thirdHelper.createHttpURL(currentDomain));
                } catch (Exception e2) {
                    System.out.println("Error for domain: "+ currentDomain +". The error is: " + e2.toString());
                }
            }
            System.out.println("Done scanning.");
        }
        Files.writeString(Paths.get("numberOfTechnologies"), String.valueOf(allTechnologies.size()), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
//        System.out.println("I found: " + allTechnologies.size());
    }
}
