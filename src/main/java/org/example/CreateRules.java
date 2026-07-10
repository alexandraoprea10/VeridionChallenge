package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.example.fields.*;
import org.json.JSONArray;
import org.json.JSONObject;


public class CreateRules {
    public List<String> addFirstRule(JSONObject JSONRule, String field) {
        List<String> techList = new ArrayList<>();
        if (!JSONRule.isNull(field) && JSONRule.has(field)) {
            Object rule = JSONRule.get(field);
            if (rule instanceof String) {
                techList.add((String) rule);
            } else if (rule instanceof JSONArray) {
                JSONArray array = (JSONArray) rule;
                for (int j = 0; j < array.length(); j++) {
                    techList.add(array.getString(j));
                }
            }
        }
        return techList;
    }
    public List<String> addSecondRule(JSONObject JSONRule, String field) {
        List<String> techList = new ArrayList<>();
        if (!JSONRule.isNull(field) && JSONRule.has(field)) {
            JSONObject fields = JSONRule.getJSONObject(field);
            List<String> fieldList = new ArrayList<>(fields.keySet());
            for (int j = 0; j < fieldList.size(); j++) {
                String cookieRule = fieldList.get(j);
                techList.add(cookieRule);
            }
        }
        return techList;
    }
    public List<String> addMetaRule(JSONObject JSONRule) {
        List<String> techList = new ArrayList<>();
        if (!JSONRule.isNull("meta") && JSONRule.has("meta")) {
            JSONObject metas = JSONRule.getJSONObject("meta");
            List<String> metaValues = new ArrayList<>(metas.keySet());
            for (int j = 0; j < metaValues.size(); j++) {
                String currentValue = metaValues.get(j);
                Object metaRule = metas.get(currentValue);
                if (metaRule instanceof String) {
                    techList.add((String) metaRule);
                } else if (metaRule instanceof JSONArray) {
                    JSONArray array = (JSONArray) metaRule;
                    for (int k = 0; k < array.length(); k++) {
                        techList.add(array.getString(k));
                    }
                }
            }
        }
        return techList;
    }
    public List<Technology> createAllRules(String file) throws IOException {
        List<Technology> technologyList = new ArrayList<>();
        String jsonFile = Files.readString(Paths.get(file));
        JSONObject root = new JSONObject(jsonFile);
        JSONObject techJson = root.getJSONObject("technologies");
        List<String> nameTechList = new ArrayList<>(techJson.keySet());

        // creating a list with each field
        List<Fields> fields = new ArrayList<>();
        fields.add(new HtmlField());
        fields.add(new MetaField());
        fields.add(new TextField());
        fields.add(new ScriptField());
        fields.add(new CookieField());
        fields.add(new HeaderField());

        for (int i = 0; i < nameTechList.size(); i++) {
            String nameTech = nameTechList.get(i);
            JSONObject JSONRule = techJson.getJSONObject(nameTech);
            Technology newTech = new Technology(nameTech);

            for (int j = 0; j < fields.size(); j++) {
                fields.get(j).createRule(JSONRule, newTech, this);
            }
            technologyList.add(newTech);
        }
        return technologyList;
    }

}