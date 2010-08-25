package com.cybozu.labs.langdetect.util;

import java.lang.Character.UnicodeBlock;
import java.util.HashMap;

public class NGram {
    private static final String LATIN1_EXCLUDED = Messages.getString("NGram.LATIN1_EXCLUDE"); //$NON-NLS-1$
    public final static int N_GRAM = 3;
    public static HashMap<Character, Character> cjk_map; 
    
    private StringBuffer grams_;

    public void addChar(char ch) {
        ch = normalize(ch);
        if (grams_.charAt(grams_.length() - 1) == ' ') {
            grams_ = new StringBuffer(" "); //$NON-NLS-1$
            if (ch==' ') return;
        } else if (grams_.length() >= N_GRAM) {
            grams_.deleteCharAt(0);
        }
        grams_.append(ch);
    }

    public NGram() {
        grams_ = new StringBuffer(" "); //$NON-NLS-1$
    }

    public String get(int n) {
        int len = grams_.length(); 
        if (len < n) return null;
        if (n == 1) {
            char ch = grams_.charAt(len - 1);
            if (ch == ' ') return null;
            return Character.toString(ch);
        } else {
            return grams_.substring(len - n, len);
        }
    }
    
    public char normalize(char ch) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
        if (block == UnicodeBlock.BASIC_LATIN) {
            if (ch<'A' || (ch<'a' && ch >'Z') || ch>'z') ch = ' ';
        } else if (block == UnicodeBlock.LATIN_1_SUPPLEMENT) {
            if (LATIN1_EXCLUDED.indexOf(ch)>=0) ch = ' ';
        } else if (block == UnicodeBlock.GENERAL_PUNCTUATION) {
            ch = ' ';
        } else if (block == UnicodeBlock.LATIN_EXTENDED_ADDITIONAL) {
            if (ch >= '\u1ea0') ch = '\u1ec3';
        } else if (block == UnicodeBlock.HIRAGANA) {
            ch = '\u3042';
        } else if (block == UnicodeBlock.KATAKANA) {
            ch = '\u30a2';
        } else if (block == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
            if (cjk_map.containsKey(ch)) ch = cjk_map.get(ch);
        } else if (block == UnicodeBlock.HANGUL_SYLLABLES) {
            ch = '\uac00';
        }
        return ch;
    }
    

    
    /**
     * CJK Kanji List for General Use
     */
    static final String[] CJK_CLASS = {
        Messages.getString("NGram.JA_CN"), //$NON-NLS-1$
        Messages.getString("NGram.JA_ONLY"), //$NON-NLS-1$
        Messages.getString("NGram.CN_TW"), //$NON-NLS-1$
        Messages.getString("NGram.JA_TW"), //$NON-NLS-1$
        Messages.getString("NGram.CN_ONLY"), //$NON-NLS-1$
        Messages.getString("NGram.COMMON"), //$NON-NLS-1$
        Messages.getString("NGram.TW_ONLY") //$NON-NLS-1$
    };
    static {
        cjk_map = new HashMap<Character, Character>();
        for (String cjk_list : CJK_CLASS) {
            char representative = cjk_list.charAt(0);
            for (int i=0;i<cjk_list.length();++i) {
                cjk_map.put(cjk_list.charAt(i), representative);
            }
        }
    }

}
