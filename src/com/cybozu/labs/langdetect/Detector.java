package com.cybozu.labs.langdetect;

import java.util.ArrayList;
import java.util.HashMap;

import com.cybozu.labs.langdetect.util.NGram;

public class Detector {
    private static final double PROB_THRESHOLD = 0.1;
    private static final double CONV_THRESHOLD = 0.99999;
    public static final int BASE = 1000;
    private final HashMap<String, HashMap<String, Double>> p_ik;
    private final ArrayList<String> langlist;
    private HashMap<String, Double> prob;
    private NGram ngram;
    private double alpha;
    private boolean convergence;

    public Detector(DetectorFactory instance_) {
        p_ik = instance_.p_ik;
        langlist = instance_.langlist;
        prob = new HashMap<String, Double>();
        for(String lang: langlist) {
            prob.put(lang, 1.0);
        }
        ngram = new NGram();
        alpha = 1.0;
        convergence = false;
    }

    public void append(char ch) {
        ngram.addChar(ch);
        for(int length=1;length<=NGram.N_GRAM;++length){
            String word = ngram.get(length);
            if (word == null || !p_ik.containsKey(word)) continue;
            HashMap<String, Double> wordprob = p_ik.get(word);
            double amount = 0;
            for(String lang: prob.keySet()) {
                double p = prob.get(lang);
                if (wordprob.containsKey(lang)) {
                    p *= alpha + wordprob.get(lang) * BASE;
                } else {
                    p *= alpha;
                }
                //p /= p_ik.size() * alpha + BASE;
                p /= alpha + BASE;
                prob.put(lang, p);
                amount += p;
            }
            // normalization & maximun probability
            double maxProb = 0.0;
            for(String lang: prob.keySet()) {
                double p = prob.get(lang) / amount;
                if (maxProb < p) maxProb = p;
                prob.put(lang, p);
            }
            if (maxProb > CONV_THRESHOLD) convergence = true; 
        }
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
