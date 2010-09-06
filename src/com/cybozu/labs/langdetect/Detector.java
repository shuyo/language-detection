package com.cybozu.labs.langdetect;

import java.util.ArrayList;
import java.util.HashMap;

import com.cybozu.labs.langdetect.util.NGram;

public class Detector {
    private static final double PROB_THRESHOLD = 0.1;
    private static final double CONV_THRESHOLD = 0.99999;
    private static final int BASE = 100;

    private final HashMap<String, HashMap<String, Double>> wordLangProbMap;
    private final ArrayList<String> langlist;
    private HashMap<String, Double> prob;
    private NGram ngram;
    private double alpha;
    private boolean convergence;
    private boolean debug;

    public Detector(HashMap<String, HashMap<String, Double>> p_ik, ArrayList<String> langlist) {
        this.wordLangProbMap = p_ik;
        this.langlist = langlist;
        prob = new HashMap<String, Double>();
        for(String lang: langlist) {
            prob.put(lang, 1.0 / langlist.size());
        }
        ngram = new NGram();
        alpha = 1.0;
        convergence = false;
        debug = false;
    }
    public void setDebug() {
        debug = true;
    }

    public void append(char ch) {
        ngram.addChar(ch);
        double amount = 1;
        for(int length=1;length<=NGram.N_GRAM;++length){
            String word = ngram.get(length);
            if (word == null || !wordLangProbMap.containsKey(word)) continue;

            HashMap<String, Double> langProbMap = wordLangProbMap.get(word);
            if (debug) System.out.println(word + ":" + langProbMap.toString());

            amount = 0;
            for(String lang: langlist) {
                double p = prob.get(lang);
                if (langProbMap.containsKey(lang)) {
                    p *= alpha + langProbMap.get(lang) * BASE;
                } else {
                    p *= alpha;
                }
                p /= alpha * wordLangProbMap.size() + BASE;
                //p /= alpha + BASE;
                prob.put(lang, p);
                amount += p;
            }
        }

        // normalization & maximun probability
        double maxProb = 0.0;
        for(String lang: langlist) {
            double p = prob.get(lang) / amount;
            if (maxProb < p) maxProb = p;
            prob.put(lang, p);
        }
        if (maxProb > CONV_THRESHOLD) convergence = true; 
        if (debug) System.out.println("=> " + ch + ":" + getProbabilities());
    }
    
    public ArrayList<Language> getProbabilities() {
        ArrayList<Language> list = new ArrayList<Language>();
        for(String lang: prob.keySet()) {
            double p = prob.get(lang);
            if (p > PROB_THRESHOLD) {
                for (int i=0;i<=list.size();++i) {
                    if (i==list.size() || list.get(i).prob < p) {
                        list.add(i, new Language(lang, p));
                        break;
                    }
                }
                
            }
        }
        return list ;
    }

    public String detect() {
        return getProbabilities().get(0).lang;
    }
    
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public boolean isConvergence() {
        return convergence;
    }

}
