package org.example;

import java.util.ArrayList;
import java.util.List;

public class Technology {
    private String name;
    private List<String> htmlRules = new ArrayList<>();
    private List<String> metaRules = new ArrayList<>();
    private List<String> textRules = new ArrayList<>();
    private List<String> scriptRules = new ArrayList<>();
    private List<String> cookieRules = new ArrayList<>();
    private List<String> headerRules = new ArrayList<>();

    public Technology(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public List<String> getHtmlRules() {
        return htmlRules;
    }
    public List<String> getMetaRules() {
        return metaRules;
    }
    public List<String> getTextRules() {
        return textRules;
    }
    public List<String> getScriptRules() {
        return scriptRules;
    }
    public List<String> getCookieRules() {
        return cookieRules;
    }
    public List<String> getHeaderRules() {
        return headerRules;
    }

    public void addHtmlRule(String newRule) {
        htmlRules.add(newRule.toLowerCase());
    }
    public void addMetaRule(String newRule) {
        metaRules.add(newRule.toLowerCase());
    }
    public void addTextRule(String newRule) {
        textRules.add(newRule.toLowerCase());
    }
    public void addScriptRule(String newRule) {
        scriptRules.add(newRule.toLowerCase());
    }
    public void addCookieRule(String newRule) {
        cookieRules.add((newRule.toLowerCase()));
    }
    public void addHeaderRule(String newRule) {
        headerRules.add(newRule.toLowerCase());
    }
}
