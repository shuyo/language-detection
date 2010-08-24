package com.cybozu.labs.langdetect;

public class Language {
    public Language(String lang, double prob) {
        this.lang = lang;
        this.prob = prob;
    }
    public String lang;
    public double prob;
    public String toString() {
        return lang + ":" + prob;
    }
}
