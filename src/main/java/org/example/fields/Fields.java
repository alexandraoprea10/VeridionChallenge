package org.example.fields;

import org.example.CreateRules;
import org.example.Technology;
import org.json.JSONObject;

public interface Fields {
    // extracting from JSON file html, meta, text, script, cookie, header rules
    public void createRule(JSONObject JSONRule, Technology newTech, CreateRules ruleClass);
}
