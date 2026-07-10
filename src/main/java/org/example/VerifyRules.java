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
import java.nio.file.Path;
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
    // method that checks rules in html response(html, script, meta, header)
    public void checkCurrentHtmlRule(List<String> ruleList, String newURL, Set<String> foundTech, Technology currentTech, String htmlResponse) {
        for (int j = 0; j < ruleList.size(); j++) {
            String currentHtml = ruleList.get(j).toLowerCase();
            if (newURL.toLowerCase().contains(currentHtml)) {
                foundTech.add(currentTech.getName());
            }
            if (!currentHtml.isEmpty() && htmlResponse.contains(currentHtml)) {
                foundTech.add(currentTech.getName());
            }
        }
    }
    public void checkCurrentCookieRule(List<String> ruleList, String newURL, Set<String> foundTech, Technology currentTech, String cookieResponse) {
        for (int j = 0; j < ruleList.size(); j++) {
            String currentCookie = ruleList.get(j).toLowerCase();
            if (!currentCookie.isEmpty() && cookieResponse.contains(currentCookie)) {
                foundTech.add(currentTech.getName());
            }
        }
    }
    public void checkCurrentHeaderRule(List<String> ruleList, String newURL, Set<String> foundTech, Technology currentTech, HttpHeaders headerResponse) {
        for (int j = 0; j < ruleList.size(); j++) {
            String currentHeader = ruleList.get(j).toLowerCase();
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
    public void verifyTechnologies(HttpClient client, String domain, List<Technology> technologyList,
                                   Set<String> allTechnologies, String newURL) throws IOException, InterruptedException {

        // 20 second timeout to prevent the program from blocking
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(newURL))
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String htmlResponse = response.body().toLowerCase();
        HttpHeaders headerResponse = response.headers();

        // efficient memory managment - merging all cokies into a single string
        List<String> cookieList = headerResponse.allValues("set-cookie");
        String cookiesResponse = String.join("; ", cookieList).toLowerCase();

        Set<String> foundTech = new HashSet<>();
        // searching for html, meta, text, script, cookie, header rules
        for (int i = 0; i < technologyList.size(); i++) {
            Technology currentTech = technologyList.get(i);

            List<String> htmlRules = currentTech.getHtmlRules();
            checkCurrentHtmlRule(htmlRules, newURL, foundTech, currentTech, htmlResponse);

            List<String> metaRules = currentTech.getMetaRules();
            checkCurrentHtmlRule(metaRules, newURL, foundTech, currentTech, htmlResponse);

            List<String> textRules = currentTech.getTextRules();
            checkCurrentHtmlRule(textRules, newURL, foundTech, currentTech, htmlResponse);

            List<String> scriptRules = currentTech.getScriptRules();
            checkCurrentHtmlRule(scriptRules, newURL, foundTech, currentTech, htmlResponse);

            List<String> cookieRules = currentTech.getCookieRules();
            checkCurrentCookieRule(cookieRules, newURL, foundTech, currentTech, cookiesResponse);

            List<String> headerRules = currentTech.getHeaderRules();
            checkCurrentHeaderRule(headerRules, newURL, foundTech, currentTech, headerResponse);
        }
//        System.out.println("For domain: " + domain + " I found: " + foundTech);
        List<String> techList = new ArrayList<>(foundTech);
        JSONArray techArray = new JSONArray(techList);

        Path outputDirectory = Paths.get("outputs");
        if (Files.notExists(outputDirectory)) {
            Files.createDirectories(outputDirectory);
        }

        JSONObject createDomain = new JSONObject();
        createDomain.put("technologies", techArray);

        String createFile = "outputs/" + domain +".json";
        Files.writeString(Paths.get(createFile), createDomain.toString(4), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        for (int i = 0; i < techList.size(); i++) {
            String tech = techList.get(i);
            allTechnologies.add(tech);
        }
    }
}
