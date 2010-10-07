package com.cybozu.labs.langdetect.util;

/**
 * {@link TagExtractor} is a class which extracts inner texts of specified tag.
 * Users don't use this class directly.
 * @author Nakatani Shuyo
 */
public class TagExtractor {
    /* package scope */ String target_;
    /* package scope */ int threshold_;
    /* package scope */ StringBuffer buf_;
    /* package scope */ String tag_;
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
        if (tag_ == target_ && line != null) {
            buf_.append(line);
        }
    }
    public void closeTag(LangProfile profile) {
        if (profile != null && tag_ == target_ && buf_.length() > threshold_) {
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
