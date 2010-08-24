package com.cybozu.labs.langdetect.util;


/**
 * Extract inner texts of specified tag
 * 
 */
public class TagExtractor {
    private String target_;
    private int threshold_;
    private StringBuffer buf_;
    private String tag_;
    private int count_;

    public TagExtractor(String tag, int threshold) {
        target_ = tag;
        threshold_ = threshold;
        count_ = 0;
        clear();
    }
    public int count() {
        return count_;
    }
    public void clear() {
        buf_ = new StringBuffer();
        tag_ = null;
    }
    public void setTag(String tag){
        tag_ = tag;
    }
    public void add(String line) {
        if (tag_ == target_) {
            buf_.append(line);
        }
    }
    public void closeTag(LangProfile profile) {
        if (tag_ == target_ && buf_.length() > threshold_) {
            NGram gram = new NGram();
            for(int i=0; i<buf_.length(); ++i) {
                gram.addChar(buf_.charAt(i));
                for(int n=1; n<=NGram.N_GRAM; ++n) {
                    profile.add(gram.get(n));
                }
            }
            ++count_;
        }
        clear();
    }

}
