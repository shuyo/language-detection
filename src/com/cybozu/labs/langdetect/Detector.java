package com.cybozu.labs.langdetect;

import java.io.IOException;
import java.io.Reader;
import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.cybozu.labs.langdetect.util.NGram;

public class Detector {
    private static final String UNKNOWN_LANG = "unknown";
    private static final double ALPHA_WIDTH = 0.1;
    private static final int N_TRIALS = 5;
    private static final int ITERATION_LIMIT = 1000;
    private static final int MAX_BLOCK_TEXT = 10000;
    private static final double PROB_THRESHOLD = 0.1;
    private static final double CONV_THRESHOLD = 0.99999;
    private static final int BASE = 100;

    private final HashMap<String, HashMap<String, Double>> wordLangProbMap;
    private final ArrayList<String> langlist;
    private HashMap<String, Double> langprob = null;
    private double alpha;
    private boolean debug;
    private StringBuffer text;

    public Detector(HashMap<String, HashMap<String, Double>> p_ik, ArrayList<String> langlist) {
        this.wordLangProbMap = p_ik;
        this.langlist = langlist;
        alpha = 1.0;
        debug = false;
        text = new StringBuffer();
    }
    public void setDebug() {
        debug = true;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void append(Reader is) throws IOException {
        char[] buf = new char[1024]; 
        while (is.ready()) {
            int length = is.read(buf);
            append(new String(buf,0,length));
        }
    }

    public void append(String buf) {
        int upperWordOffset = -1, latinCount = 0, nonLatinCount = 0;
        
        for(int i=0;i<buf.length();++i) {
            int len = text.length();
            char c = NGram.normalize(buf.charAt(i));
/*
            if (c>='A' && c<='Z') {
                if (upperWordOffset<0) upperWordOffset=len;
            } else {
                if (upperWordOffset>=2) {
                    text.delete(upperWordOffset, len);
                    len = upperWordOffset;
                }
                upperWordOffset=-1;
            }
*/
            if (len < MAX_BLOCK_TEXT && (c!=' ' || len==0 || text.charAt(len-1) != ' ')) {
                text.append(c);
                if (c<='z' && c>='A') {
                    ++latinCount;
                } else if (c>='\u0300' && UnicodeBlock.of(c)!=UnicodeBlock.LATIN_EXTENDED_ADDITIONAL) {
                    ++nonLatinCount;
                }
            }
        }

        if (latinCount * 2 < nonLatinCount) {
            StringBuffer textWithoutLatin = new StringBuffer();
            for(int i=0;i<text.length();++i) {
                char c = text.charAt(i);
                if (c>'z' || c<'A') textWithoutLatin.append(c);
            }
            text = textWithoutLatin;
        }

    }

    public String detect() {
        ArrayList<Language> probabilities = getProbabilities();
        if (probabilities.size() > 0) return probabilities.get(0).lang;
        return UNKNOWN_LANG;
    }
    
    /**
     * 
     * @return possible languages list (whose probabilities are over PROB_THRESHOLD, ordered by probability descendently)
     */
    public ArrayList<Language> getProbabilities() {
        if (langprob==null) detectBlock();

        ArrayList<Language> list = sortProbability(langprob);
        return list ;
    }

    
    private void detectBlock() {
        int length = text.length();
        if (length==0) return; 
        // TODO throw exception

        langprob = new HashMap<String, Double>();
        for(String lang: langlist) langprob.put(lang, 0.0);
        //double alpha = this.alpha - ALPHA_WIDTH;
        for(int t=0;t<N_TRIALS;++t) {
                
            HashMap<String, Double> prob = new HashMap<String, Double>();
            for(String lang: langlist) {
                prob.put(lang, 1.0 / langlist.size());
            }
    
            Random rand = new Random();
            for(int i=0;;++i) {
                int r, w;
                do {
                    r = rand.nextInt(length);
                    w = rand.nextInt(NGram.N_GRAM);
                } while(r + w <= length && !updateLangProb(prob, text.substring(r, r + w), alpha));
                if ( i % 5 == 0) {
                    if (normalizeProb(prob) > CONV_THRESHOLD || i>=ITERATION_LIMIT) break;
                    if (debug) System.out.println("> " + sortProbability(prob));
                }
            }
            for(String lang: langlist) langprob.put(lang, langprob.get(lang) + prob.get(lang));
            if (debug) System.out.println("==> " + sortProbability(prob));

            //alpha += ALPHA_WIDTH * 2 / (N_TRIALS - 1);
        }
        for(String lang: langlist) langprob.put(lang, langprob.get(lang) / N_TRIALS);
    }

    /**
     * update language probabilities with N-gram string(N=1,2,3)
     * @param word N-gram string
     */
    private boolean updateLangProb(HashMap<String, Double> prob, String word, double alpha) {
        if (word == null || !wordLangProbMap.containsKey(word)) return false;

        HashMap<String, Double> langProbMap = wordLangProbMap.get(word);
        if (debug) System.out.println(word + "(" + unicodeEncode(word) + "):" + langProbMap.toString());

        for(String lang: langlist) {
            double p = prob.get(lang);
            if (langProbMap.containsKey(lang)) {
                p *= alpha + langProbMap.get(lang) * BASE;
            } else {
                p *= alpha;
            }
            p /= alpha * wordLangProbMap.size() + BASE;
            prob.put(lang, p);
        }
        return true;
    }

    static private String unicodeEncode(String word) {
        StringBuffer buf = new StringBuffer();
        for (int i=0;i<word.length();++i) {
            char ch = word.charAt(i);
            if (ch>='\u0080'){
                String st = Integer.toHexString(0x10000 + (int)ch);
                while (st.length() < 4) st = "0" + st;
                buf.append("\\u").append(st.subSequence(1, 5));
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
    /**
     * normalize probabilities and check convergence by the maximun probability
     * @return maximum of probabilities
     */
    static private double normalizeProb(HashMap<String, Double> prob) {
        double maxp = 0, sump = 0;
        for(String lang: prob.keySet()) sump += prob.get(lang);
        for(String lang: prob.keySet()) {
            double p = prob.get(lang) / sump;
            if (maxp < p) maxp = p;
            prob.put(lang, p);
        }
        return maxp;
    }

    /**
     * @param prob 
     * @return
     */
    static private ArrayList<Language> sortProbability(HashMap<String, Double> prob) {
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
        return list;
    }
}
