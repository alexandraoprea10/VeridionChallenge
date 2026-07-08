package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VerifyRules {
    public void verifyTechnologies(HttpClient client, String domain, List<Technology> technologyList,
                                   Set<String> allTechnologies) throws IOException, InterruptedException {
        String newURL = "https://" + domain;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(newURL))
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String htmlResponse = response.body().toLowerCase();
        HttpHeaders headerResponse = response.headers();

        List<String> cookieList = headerResponse.allValues("set-cookie");
        String cookiesResponse = String.join("; ", cookieList).toLowerCase();

        Set<String> foundTech = new HashSet<>();

        for (int i = 0; i < technologyList.size(); i++) {
            Technology currentTech = technologyList.get(i);

            List<String> htmlRules = currentTech.getHtmlRules();
            for (int j = 0; j < htmlRules.size(); j++) {
                String currentHtml = htmlRules.get(j);
                if (!currentHtml.isEmpty() && htmlResponse.contains(currentHtml)) {
                    foundTech.add(currentTech.getName());
                }
            }

            List<String> cookieRules = currentTech.getCookieRules();
            for (int j = 0; j < cookieRules.size(); j++) {
                String currentCookie = cookieRules.get(j);
                if (!currentCookie.isEmpty() && cookiesResponse.contains(currentCookie)) {
                    foundTech.add(currentTech.getName());
                }
            }

            List<String> headerRules = currentTech.getHeaderRules();
            for (int j = 0; j < headerRules.size(); j++) {
                String currentHeader = headerRules.get(j).toLowerCase();
                List<String> allHeaders = new ArrayList<>(headerResponse.map().keySet());
                for (int k = 0; k < allHeaders.size(); k++) {
                    String header = allHeaders.get(k).toLowerCase();
                    List<String> allValues = headerResponse.map().get(allHeaders.get(k));
                    for (int l = 0; l < allValues.size(); l++) {
                        String singleHeader = allValues.get(l).toLowerCase();
                        if (header.equals(currentHeader)) {
                            foundTech.add(currentTech.getName());
                        }
                        if (singleHeader.contains(currentHeader)) {
                            foundTech.add(currentTech.getName());
                        }
                    }
                }
            }
        }
        System.out.println("For domain: " + domain + " we found: " + foundTech);
        List<String> techList = new ArrayList<>(foundTech);
        for (int i = 0; i < techList.size(); i++) {
            String tech = techList.get(i);
            allTechnologies.add(tech);
        }
    }
}
