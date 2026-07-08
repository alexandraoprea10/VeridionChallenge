package org.example;

import java.util.ArrayList;
import java.util.List;

public class Technology {
    private String name;
    private List<String> htmlRules = new ArrayList<>();
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
    public List<String> getCookieRules() {
        return cookieRules;
    }
    public List<String> getHeaderRules() {
        return headerRules;
    }
    public void addHtmlRule(String newRule) {
        htmlRules.add(newRule.toLowerCase());
    }
    public void addCookieRule(String newRule) {
        cookieRules.add((newRule.toLowerCase()));
    }
    public void addHeaderRule(String newRule) {
        headerRules.add(newRule.toLowerCase());
    }
}
