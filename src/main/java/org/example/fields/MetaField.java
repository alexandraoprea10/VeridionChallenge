package org.example.fields;

import org.example.CreateRules;
import org.example.Technology;
import org.json.JSONObject;

import java.util.List;

public class MetaField implements Fields {
    @Override
    public void createRule(JSONObject JSONRule, Technology newTech, CreateRules ruleClass) {
        List<String> foundRules = ruleClass.addMetaRule(JSONRule);
        for (int i = 0; i < foundRules.size(); i++) {
            newTech.addMetaRule(foundRules.get(i));
        }
    }
}
