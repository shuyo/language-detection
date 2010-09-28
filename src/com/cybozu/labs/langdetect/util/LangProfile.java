package com.cybozu.labs.langdetect.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Language Profile Class for Profile Generator
 *
 * This class is not used when language detection.
 * @author Nakatani Shuyo
 */
public class LangProfile {
    private static final int MINIMUM_FREQ = 3;
    private static final int LESS_FREQ_RATIO = 10000;
    public String name;
    public HashMap<String, Integer> freq;
    public int[] n_words;

    /**
     * Constructor for JSONIC 
     */
    public LangProfile() {}

    /**
     * Normal Constructor
     * @param name language name
     * @param n_gram maximum length of n-gram
     */
    public LangProfile(String name, int n_gram) {
        this.name = name;
        n_words = new int[n_gram];
        freq = new HashMap<String, Integer>();
    }
    
    /**
     * @param word
     */
    public void add(String word) {
        if (word == null) return;
        ++n_words[word.length() - 1];
        if (freq.containsKey(word)) {
            freq.put(word, freq.get(word) + 1);
        } else {
            freq.put(word, 1);
        }
    }

    /**
     * Eliminate below less frequency n-grams and noise Latin alphabets
     */
    public void omitLessFreq() {
        int threshold = n_words[0] / LESS_FREQ_RATIO;
        if (threshold < MINIMUM_FREQ) threshold = MINIMUM_FREQ;
        
        Set<String> keys = freq.keySet();
        int roman = 0;
        for(Iterator<String> i = keys.iterator(); i.hasNext(); ){
            String key = i.next();
            int count = freq.get(key);
            if (count <= threshold) {
                n_words[key.length()-1] -= count; 
                i.remove();
            } else {
                if (key.matches("^[A-Za-z]$")) {
                    roman += count;
                }
            }
        }

        // roman check
        if (roman < n_words[0] / 3) {
            Set<String> keys2 = freq.keySet();
            for(Iterator<String> i = keys2.iterator(); i.hasNext(); ){
                String key = i.next();
                if (key.matches(".*[A-Za-z].*")) {
                    n_words[key.length()-1] -= freq.get(key); 
                    i.remove();
                }
            }
            
        }
    }
}
