package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VerifyRules {
    public String createHttpsURL(String domain) {
        return "https://" + domain;
    }
    public String createHttpURL(String domain) {
        return "http://" + domain;
    }
    public void verifyTechnologies(HttpClient client, String domain, List<Technology> technologyList,
                                   Set<String> allTechnologies, String newURL, JSONObject newTechJSON) throws IOException, InterruptedException {

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
                String currentHtml = htmlRules.get(j).toLowerCase();
                if (newURL.toLowerCase().contains(currentHtml)) {
                    foundTech.add(currentTech.getName());
                }
                if (!currentHtml.isEmpty() && htmlResponse.contains(currentHtml)) {
                    foundTech.add(currentTech.getName());
                }
            }

            List<String> metaRules = currentTech.getMetaRules();
            for (int j = 0; j < metaRules.size(); j++) {
                String currentHtml = metaRules.get(j).toLowerCase();
                if (newURL.toLowerCase().contains(currentHtml)) {
                    foundTech.add(currentTech.getName());
                }
                if (!currentHtml.isEmpty() && htmlResponse.contains(currentHtml)) {
                    foundTech.add(currentTech.getName());
                }
            }

            List<String> textRules = currentTech.getTextRules();
            for (int j = 0; j < textRules.size(); j++) {
                String currentHtml = textRules.get(j).toLowerCase();
                if (newURL.toLowerCase().contains(currentHtml)) {
                    foundTech.add(currentTech.getName());
                }
                if (!currentHtml.isEmpty() && htmlResponse.contains(currentHtml)) {
                    foundTech.add(currentTech.getName());
                }
            }

            List<String> scriptRules = currentTech.getScriptRules();
            for (int j = 0; j < scriptRules.size(); j++) {
                String currentHtml = scriptRules.get(j).toLowerCase();
                if (newURL.toLowerCase().contains(currentHtml)) {
                    foundTech.add(currentTech.getName());
                }
                if (!currentHtml.isEmpty() && htmlResponse.contains(currentHtml)) {
                    foundTech.add(currentTech.getName());
                }
            }

            List<String> cookieRules = currentTech.getCookieRules();
            for (int j = 0; j < cookieRules.size(); j++) {
                String currentCookie = cookieRules.get(j).toLowerCase();
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
                    if (header.equals(currentHeader)) {
                        foundTech.add(currentTech.getName());
                    }
                    List<String> allValues = headerResponse.map().get(allHeaders.get(k));
                    for (int l = 0; l < allValues.size(); l++) {
                        String singleHeader = allValues.get(l).toLowerCase();
                        if (singleHeader.contains(currentHeader)) {
                            foundTech.add(currentTech.getName());
                        }
                    }
                }
            }
        }
        System.out.println("For domain: " + domain + " I found: " + foundTech);
        List<String> techList = new ArrayList<>(foundTech);
        JSONArray techArray = new JSONArray(techList);
        String createLine =  "\"" + domain + " \":\n " + techArray.toString() + ",\n";
        Files.writeString(Paths.get("tech_explained.json"), createLine, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        for (int i = 0; i < techList.size(); i++) {
            String tech = techList.get(i);
            allTechnologies.add(tech);
        }
    }
}
