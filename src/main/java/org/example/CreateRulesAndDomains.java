package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


public class CreateRulesAndDomains {
    public List<Technology> createRules(String file) throws IOException {
        List<Technology> technologyList = new ArrayList<>();
        String jsonFile = Files.readString(Paths.get(file));
        JSONObject root = new JSONObject(jsonFile);
        JSONObject techJson = root.getJSONObject("technologies");
        List<String> nameTechList = new ArrayList<>(techJson.keySet());

        for (int i = 0; i < nameTechList.size(); i++) {
            String nameTech = nameTechList.get(i);
            JSONObject JSONRule = techJson.getJSONObject(nameTech);
            Technology newTech = new Technology(nameTech);

            if (JSONRule.has("html") && !JSONRule.isNull("html")) {
                Object htmlRule = JSONRule.get("html");
                if (htmlRule instanceof String) {
                    newTech.addHtmlRule((String) htmlRule);
                }
            }

            if (JSONRule.has("cookies") && !JSONRule.isNull("cookies")) {
                JSONObject cookies = JSONRule.getJSONObject("cookies");
                List<String> cookieList = new ArrayList<>(cookies.keySet());
                for (int j = 0 ; j < cookieList.size(); j++) {
                    String cookieRule = cookieList.get(j);
                    newTech.addCookieRule(cookieRule);
                }
            }

            if (JSONRule.has("headers") && !JSONRule.isNull("headers")) {
                JSONObject headers = JSONRule.getJSONObject("headers");
                List<String> headersList = new ArrayList<>(headers.keySet());
                for (int j = 0 ; j < headersList.size(); j++) {
                    String cookieRule = headersList.get(j);
                    newTech.addHeaderRule(cookieRule);
                }
            }
            technologyList.add(newTech);
        }
        return technologyList;
    }

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