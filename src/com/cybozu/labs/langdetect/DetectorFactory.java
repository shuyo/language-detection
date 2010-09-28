package com.cybozu.labs.langdetect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import com.cybozu.labs.langdetect.util.LangProfile;

/**
 * Language Detector Factory Class
 * 
 * This class manages an initialization and constructions of {@link Detector}. 
 * 
 * Before using language detection library, 
 * load profiles with {@link DetectorFactory#loadProfile(String)} method
 * and set initialization parameters (TODO)
 * 
 * When the language detection,
 * construct Detector instance via {@link DetectorFactory#create()}.
 * See also {@link Detector}'s sample code.
 * 
 * @see Detector
 * @author Nakatani Shuyo
 */
public class DetectorFactory {
    private HashMap<String, HashMap<String, Double>> wordLangProbMap;
    private ArrayList<String> langlist;
    private DetectorFactory() {
        wordLangProbMap = new HashMap<String, HashMap<String, Double>>();
        langlist = new ArrayList<String>();
    }
    static private DetectorFactory instance_ = new DetectorFactory();

    /**
     * 
     * @param profileDirectory profile directory path
     * @throws LangDetectException 
     */
    public static void loadProfile(String profileDirectory) throws LangDetectException {
        File dir = new File(profileDirectory);
        for (File file: dir.listFiles()) {
            if (file.getName().startsWith(".") || !file.isFile()) continue;
            FileInputStream is = null;
            try {
                is = new FileInputStream(file);
                LangProfile profile = JSON.decode(is, LangProfile.class);
                addProfile(profile);
            } catch (JSONException e) {
                throw new LangDetectException(ErrorCode.FormatError, "profile format error in '" + file.getName() + "'");
            } catch (IOException e) {
                throw new LangDetectException(ErrorCode.FileLoadError, "can't open '" + file.getName() + "'");
            } finally {
                try {
                    if (is!=null) is.close();
                } catch (IOException e) {}
            }
        }
    }

    /**
     * @param profile
     * @throws LangDetectException 
     */
    static public void addProfile(LangProfile profile) throws LangDetectException {
        String lang = profile.name;
        if (instance_.langlist.contains(lang)) {
            throw new LangDetectException(ErrorCode.DuplicateLangError, "duplicate the same language profile");
        }
        instance_.langlist.add(lang);
        for (String word: profile.freq.keySet()) {
            if (!instance_.wordLangProbMap.containsKey(word)) {
                instance_.wordLangProbMap.put(word, new HashMap<String, Double>());
            }
            double prob = profile.freq.get(word).doubleValue() / profile.n_words[word.length()-1];
            instance_.wordLangProbMap.get(word).put(lang, prob);
        }
    }

    /**
     * Construct Detector instance
     * 
     * @return Detector instance
     */
    public static Detector create() {
        return new Detector(instance_.wordLangProbMap, instance_.langlist);
    }

    /**
     * Construct Detector instance with smoothing parameter 
     * 
     * @param alpha smoothing parameter (default value = 0.5)
     * @return Detector instance
     */
    public static Detector create(double alpha) {
        Detector detector = new Detector(instance_.wordLangProbMap, instance_.langlist);
        detector.setAlpha(alpha);
        return detector;
    }
}
