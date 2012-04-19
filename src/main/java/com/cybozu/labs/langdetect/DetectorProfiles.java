package com.cybozu.labs.langdetect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.cybozu.labs.langdetect.util.LangProfile;

/**
 * Language Profiles for {@link DetectorFactory}
 * 
 * 
 * for multiple profiles.
 * 
 * @see DetectorFactory
 * @author Nakatani Shuyo
 *
 */
public class DetectorProfiles {
    /* package scope */ HashMap<String, double[]> wordLangProbMap;
    /* package scope */ ArrayList<String> langlist;
    /* package scope */ Long seed = null;

    /**
     * Constructor
     */
    /* package scope */ DetectorProfiles() {
        wordLangProbMap = new HashMap<String, double[]>();
        langlist = new ArrayList<String>();
    }

    /* package scope */ void addProfile(LangProfile profile, int index, int langsize) throws LangDetectException {
        String lang = profile.name;
        if (langlist.contains(lang)) {
            throw new LangDetectException(ErrorCode.DuplicateLangError, "duplicate the same language profile");
        }
        langlist.add(lang);
        for (String word: profile.freq.keySet()) {
            if (!wordLangProbMap.containsKey(word)) {
                wordLangProbMap.put(word, new double[langsize]);
            }
            int length = word.length();
            if (length >= 1 && length <= 3) {
                double prob = profile.freq.get(word).doubleValue() / profile.n_words[length - 1];
                wordLangProbMap.get(word)[index] = prob;
            }
        }
    }

    /**
     * Clear profiles 
     */
    public void clear() {
        langlist.clear();
        wordLangProbMap.clear();
    }
    
    /**
     * Set random seed for coherent detection.
     * 
     * Hence language-detection draws random features to reduce bias, 
     * it will return incoherent language whenever it detects.
     * When you need coherent detection, you should set a fixed number into seed. 
     * 
     * @param seed random seed to set
     */
    public void setSeed(long seed) {
        this.seed = seed;
    }

    /**
     * @return languages list in profiles
     */
    public final List<String> getLangList() {
        return Collections.unmodifiableList(langlist);
    }
}
