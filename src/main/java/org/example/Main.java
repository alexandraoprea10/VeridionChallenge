package org.example;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    void main() throws IOException, InterruptedException {
        String domains = "domenii.txt";
        String rules = "technologies.json";

        CreateRulesAndDomains helper = new CreateRulesAndDomains();
        List<Technology> technologyList = helper.createRules(rules);
        List<String> domainList = helper.createDomains(domains);
        Set<String> allTechnologies = new HashSet<>();

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        VerifyRules secondHelper = new VerifyRules();
        for (int i = 0; i < domainList.size(); i++) {
            String currentDomain = domainList.get(i);
            try {
                secondHelper.verifyTechnologies(client, currentDomain, technologyList, allTechnologies);
            } catch (Exception e) {
                System.out.println("Error for domain: "+ currentDomain);
            }
        }
        System.out.println("I found: " + allTechnologies.size());
    }
}
