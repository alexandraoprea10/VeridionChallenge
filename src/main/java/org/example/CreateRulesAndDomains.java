package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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

            if (!JSONRule.isNull("html") && JSONRule.has("html")) {
                Object htmlRule = JSONRule.get("html");
                if (htmlRule instanceof String) {
                    newTech.addHtmlRule((String) htmlRule);
                } else if (htmlRule instanceof JSONArray) {
                    JSONArray array = (JSONArray) htmlRule;
                    for (int j = 0; j < array.length(); j++) {
                        newTech.addHtmlRule(array.getString(j));
                    }
                }
            }


            if (!JSONRule.isNull("meta") && JSONRule.has("meta")) {
                JSONObject metas = JSONRule.getJSONObject("meta");
                List<String> metaValues = new ArrayList<>(metas.keySet());
                for (int j = 0; j < metaValues.size(); j++) {
                    String currentValue = metaValues.get(j);
                    Object metaRule = metas.get(currentValue);
                    if (metaRule instanceof String) {
                        newTech.addMetaRule((String) metaRule);
                    } else if (metaRule instanceof JSONArray) {
                        JSONArray array = (JSONArray) metaRule;
                        for (int k = 0; k < array.length(); k++) {
                            newTech.addMetaRule(array.getString(k));
                        }
                    }
                }
            }

            if (!JSONRule.isNull("text") && JSONRule.has("text")) {
                Object textRule = JSONRule.get("text");
                if (textRule instanceof String) {
                    newTech.addTextRule((String) textRule);
                } else if (textRule instanceof JSONArray) {
                    JSONArray array = (JSONArray) textRule;
                    for (int j = 0; j < array.length(); j++) {
                        newTech.addTextRule(array.getString(j));
                    }
                }
            }

            if (!JSONRule.isNull("scriptSrc") && JSONRule.has("scriptSrc")) {
                Object scriptRule = JSONRule.get("scriptSrc");
                if (scriptRule instanceof String) {
                    newTech.addScriptRule((String) scriptRule);
                } else if (scriptRule instanceof JSONArray) {
                    JSONArray array = (JSONArray) scriptRule;
                    for (int j = 0; j < array.length(); j++) {
                        newTech.addScriptRule(array.getString(j));
                    }
                }
            }

            if (!JSONRule.isNull("cookies") && JSONRule.has("cookies")) {
                JSONObject cookies = JSONRule.getJSONObject("cookies");
                List<String> cookieList = new ArrayList<>(cookies.keySet());
                for (int j = 0 ; j < cookieList.size(); j++) {
                    String cookieRule = cookieList.get(j);
                    newTech.addCookieRule(cookieRule);
                }
            }

            if (!JSONRule.isNull("headers") && JSONRule.has("headers")) {
                JSONObject headers = JSONRule.getJSONObject("headers");
                List<String> headersList = new ArrayList<>(headers.keySet());
                for (int j = 0 ; j < headersList.size(); j++) {
                    String headerRule = headersList.get(j);
                    newTech.addHeaderRule(headerRule);
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