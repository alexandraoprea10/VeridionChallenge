package org.example;

import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    void main() throws IOException, InterruptedException {
        String domains = "domains.txt";
        String rules = "technologies.json";

        CreateRulesAndDomains helper = new CreateRulesAndDomains();
        List<Technology> technologyList = helper.createRules(rules);
        List<String> domainList = helper.createDomains(domains);
        Set<String> allTechnologies = new HashSet<>();

        VerifyRules secondHelper = new VerifyRules();
        for (int i = 0; i < domainList.size(); i++) {
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();
            String currentDomain = domainList.get(i);
            try {
                secondHelper.verifyTechnologies(client, currentDomain, technologyList, allTechnologies, secondHelper.createHttpsURL(currentDomain));
            } catch (Exception e) {
                try {
                    secondHelper.verifyTechnologies(client, currentDomain, technologyList, allTechnologies, secondHelper.createHttpURL(currentDomain));
                } catch (Exception e2) {
                    System.out.println("Error for domain: "+ currentDomain +". The error is: " + e2.toString());
                }
            }
        }
        System.out.println("I found: " + allTechnologies.size());
    }
}
