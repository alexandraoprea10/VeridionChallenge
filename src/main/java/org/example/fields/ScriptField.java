package org.example.fields;

import org.example.CreateRules;
import org.example.Technology;
import org.json.JSONObject;

import java.util.List;

public class ScriptField implements Fields {
    @Override
    public void createRule(JSONObject JSONRule, Technology newTech, CreateRules ruleClass) {
        List<String> foundRules = ruleClass.addFirstRule(JSONRule, "scriptSrc");
        for (int i = 0; i < foundRules.size(); i++) {
            newTech.addScriptRule(foundRules.get(i));
        }
    }
}
