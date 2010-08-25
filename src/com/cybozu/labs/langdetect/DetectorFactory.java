package com.cybozu.labs.langdetect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import com.cybozu.labs.langdetect.util.LangProfile;

public class DetectorFactory {
    private HashMap<String, HashMap<String, Double>> wordLangProbMap;
    private ArrayList<String> langlist;
    private DetectorFactory() {
        wordLangProbMap = new HashMap<String, HashMap<String, Double>>();
        langlist = new ArrayList<String>();
    }
    static private DetectorFactory instance_ = new DetectorFactory();

    public static void loadProfile(String profileDirectory) {
        File dir = new File(profileDirectory);
        for (File file: dir.listFiles()) {
            if (file.getName().startsWith(".") || !file.isFile()) continue;
            FileInputStream is = null;
            try {
                is = new FileInputStream(file);
                LangProfile profile = JSON.decode(is, LangProfile.class);
                addProfile(profile);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                System.out.println(file.getName());
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println(file.getName());
                e.printStackTrace();
            } finally {
                try {
                    if (is!=null) is.close();
                } catch (IOException e) {}
            }
        }
    }

    static public void addProfile(LangProfile profile) {
        String lang = profile.name;
        if (instance_.langlist.contains(lang)) {
            // TODO:
            throw new RuntimeException();
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
    public static Detector create() {
        return new Detector(instance_.wordLangProbMap, instance_.langlist);
    }

    public static Detector create(double alpha) {
        Detector detector = new Detector(instance_.wordLangProbMap, instance_.langlist);
        detector.setAlpha(alpha);
        return detector;
    }
}
