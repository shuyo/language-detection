package com.cybozu.labs.langdetect;

import java.io.IOException;
import java.io.Reader;
import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

import com.cybozu.labs.langdetect.util.NGram;

/**
 * Language Detector Class
 * <p>
 * 
 * This class is able to be constructed via the factory class {@link DetectorFactory}.
 * 
 * <pre>
 * import java.util.ArrayList;
 * import com.cybozu.labs.langdetect.Detector;
 * import com.cybozu.labs.langdetect.DetectorFactory;
 * import com.cybozu.labs.langdetect.Language;
 * 
 * class LangDetectSample {
 *     public void init(String profileDirectory) {
 *         DetectorFactory.loadProfile(profileDirectory);
 *     }
 *     public String detect(String text) {
 *         Detector detector = DetectorFactory.create();
 *         detector.append(text);
 *         return detector.detect();
 *     }
 *     public ArrayList<Language> detectLangs(String text) {
 *         Detector detector = DetectorFactory.create();
 *         detector.append(text);
 *         return detector.getProbabilities();
 *     }
 * }
 * </pre>
 * 
 * @author Nakatani Shuyo
 * @see DetectorFactory
 */
public class Detector {
    private static final double ALPHA_DEFAULT = 0.5;
    private static final double ALPHA_WIDTH = 0.05;

    private static final String UNKNOWN_LANG = "unknown";
    private static final int N_TRIALS = 7;
    private static final int ITERATION_LIMIT = 1000;
    private static final int MAX_BLOCK_TEXT = 10000;
    private static final double PROB_THRESHOLD = 0.1;
    private static final double CONV_THRESHOLD = 0.99999;
    private static final int BASE = 10000;

    private static final Pattern URL_REGEX = Pattern.compile("https?://[-_.?&~;+=/#0-9A-Za-z]+");
    private static final Pattern MAIL_REGEX = Pattern.compile("[-_.0-9A-Za-z]+@[-_0-9A-Za-z]+[-_.0-9A-Za-z]+");
    
    private final HashMap<String, HashMap<String, Double>> wordLangProbMap;
    private final ArrayList<String> langlist;
    private HashMap<String, Double> langprob = null;
    private double alpha;
    private boolean verbose;
    private StringBuffer text;
    private ArrayList<ArrayList<String>> ngrams;

    /**
     * @param p_ik
     * @param langlist
     */
    public Detector(HashMap<String, HashMap<String, Double>> p_ik, ArrayList<String> langlist) {
        this.wordLangProbMap = p_ik;
        this.langlist = langlist;
        alpha = ALPHA_DEFAULT;
        verbose = false;
        text = new StringBuffer();
        ngrams = new ArrayList<ArrayList<String>>();
        for(int i=0;i<NGram.N_GRAM;++i) ngrams.add((new ArrayList<String>()));
    }

    /**
     * 
     */
    public void setVerbose() {
        verbose = true;
    }

    /**
     * @param alpha
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * @param is
     * @throws IOException
     */
    public void append(Reader is) throws IOException {
        char[] buf = new char[MAX_BLOCK_TEXT/2];
        while (text.length() < MAX_BLOCK_TEXT && is.ready()) {
            int length = is.read(buf);
            append(new String(buf, 0, length));
        }
    }

    /**
     * @param buf
     */
    public void append(String buf) {
        buf = URL_REGEX.matcher(buf).replaceAll(" ");
        buf = MAIL_REGEX.matcher(buf).replaceAll(" ");
        char pre = 0;
        for (int i = 0; i < buf.length() && text.length() < MAX_BLOCK_TEXT; ++i) {
            char c = NGram.normalize(buf.charAt(i));
            if (c != ' ' || pre != ' ') text.append(c);
            pre = c;
        }
    }

    /**
     * Cleaning text to detect
     * (eliminate URL, e-mail address and Latin sentence if it is not written in Latin alphabet)
     */
    private void cleaningText() {
        int latinCount = 0, nonLatinCount = 0;
        for(int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c <= 'z' && c >= 'A') {
                ++latinCount;
            } else if (c >= '\u0300' && UnicodeBlock.of(c) != UnicodeBlock.LATIN_EXTENDED_ADDITIONAL) {
                ++nonLatinCount;
            }
        }
        if (latinCount * 2 < nonLatinCount) {
            StringBuffer textWithoutLatin = new StringBuffer();
            for(int i = 0; i < text.length(); ++i) {
                char c = text.charAt(i);
                if (c > 'z' || c < 'A') textWithoutLatin.append(c);
            }
            text = textWithoutLatin;
        }

    }

    /**
     * 
     * @return detected language name which has most probability.
     * @throws LangDetectException 
     */
    public String detect() throws LangDetectException {
        ArrayList<Language> probabilities = getProbabilities();
        if (probabilities.size() > 0) return probabilities.get(0).lang;
        return UNKNOWN_LANG;
    }

    /**
     * Get language candidates which have high probabilities
     * @return possible languages list (whose probabilities are over PROB_THRESHOLD, ordered by probabilities descendently
     * @throws LangDetectException 
     */
    public ArrayList<Language> getProbabilities() throws LangDetectException {
        if (langprob == null) detectBlock();

        ArrayList<Language> list = sortProbability(langprob);
        return list;
    }
    
    /**
     * @throws LangDetectException 
     * 
     */
    private void detectBlock() throws LangDetectException {
        cleaningText();
        if (text.length()==0) {
            throw new LangDetectException(ErrorCode.NoTextError, "no text error");
        }

        ArrayList<String> ngrams = extractNGrams();
        
        langprob = new HashMap<String, Double>();
        for (String lang: langlist) langprob.put(lang, 0.0);

        //double alpha = this.alpha - ALPHA_WIDTH;
        Random rand = new Random();
        for (int t = 0; t < N_TRIALS; ++t) {
            HashMap<String, Double> prob = new HashMap<String, Double>();
            for(String lang: langlist) {
                prob.put(lang, 1.0 / langlist.size());
            }
    
            double alpha = this.alpha + rand.nextGaussian() * ALPHA_WIDTH;

            for (int i = 0;; ++i) {
                int r = rand.nextInt(ngrams.size());
                updateLangProb(prob, ngrams.get(r), alpha);
                if (i % 5 == 0) {
                    if (normalizeProb(prob) > CONV_THRESHOLD || i>=ITERATION_LIMIT) break;
                    if (verbose) System.out.println("> " + sortProbability(prob));
                }
            }
            for(String lang: langlist) langprob.put(lang, langprob.get(lang) + prob.get(lang));
            if (verbose) System.out.println("==> " + sortProbability(prob));

            //alpha += ALPHA_WIDTH * 2 / (N_TRIALS - 1);
        }
        for(String lang: langlist) langprob.put(lang, langprob.get(lang) / N_TRIALS);
    }

    /**
     * @return
     */
    private ArrayList<String> extractNGrams() {
        ArrayList<String> list = new ArrayList<String>();
        NGram ngram = new NGram();
        for(int i=0;i<text.length();++i) {
            ngram.addChar(text.charAt(i));
            for(int n=1;n<=NGram.N_GRAM;++n){
                String w = ngram.get(n);
                if (w!=null && wordLangProbMap.containsKey(w)) list.add(w);
            }
        }
        return list;
    }

    /**
     * update language probabilities with N-gram string(N=1,2,3)
     * @param word N-gram string
     */
    private boolean updateLangProb(HashMap<String, Double> prob, String word, double alpha) {
        if (word == null || !wordLangProbMap.containsKey(word)) return false;

        HashMap<String, Double> langProbMap = wordLangProbMap.get(word);
        if (verbose) System.out.println(word + "(" + unicodeEncode(word) + "):" + langProbMap.toString());

        for (String lang : langlist) {
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

    /**
     * unicode encoding (for debug dump)
     * @param word
     * @return
     */
    static private String unicodeEncode(String word) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < word.length(); ++i) {
            char ch = word.charAt(i);
            if (ch >= '\u0080') {
                String st = Integer.toHexString(0x10000 + (int) ch);
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
     * @param probabilities HashMap
     * @return lanugage candidates order by probabilities descendently
     */
    static private ArrayList<Language> sortProbability(HashMap<String, Double> prob) {
        ArrayList<Language> list = new ArrayList<Language>();
        for(String lang: prob.keySet()) {
            double p = prob.get(lang);
            if (p > PROB_THRESHOLD) {
                for (int i = 0; i <= list.size(); ++i) {
                    if (i == list.size() || list.get(i).prob < p) {
                        list.add(i, new Language(lang, p));
                        break;
                    }
                }
            }
        }
        return list;
    }
}
