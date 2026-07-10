package org.example.fields;

import org.example.CreateRules;
import org.example.Technology;
import org.json.JSONObject;

import java.util.List;

public class HeaderField implements Fields {
    @Override
    public void createRule(JSONObject JSONRule, Technology newTech, CreateRules ruleClass) {
        List<String> foundRules = ruleClass.addSecondRule(JSONRule, "headers");
        for (int i = 0; i < foundRules.size(); i++) {
            newTech.addHeaderRule(foundRules.get(i));
        }
    }
}
