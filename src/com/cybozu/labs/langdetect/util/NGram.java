package com.cybozu.labs.langdetect.util;

import java.lang.Character.UnicodeBlock;
import java.util.HashMap;

public class NGram {
    private static final String LATIN1_EXCLUDED = Messages.getString("NGram.LATIN1_EXCLUDE"); //$NON-NLS-1$
    public final static int N_GRAM = 3;
    public static HashMap<Character, Character> cjk_map; 
    
    private StringBuffer grams_;
    private boolean capitalword_;

    public void addChar(char ch) {
        ch = normalize(ch);
        char lastchar = grams_.charAt(grams_.length() - 1);
        if (lastchar == ' ') {
            grams_ = new StringBuffer(" "); //$NON-NLS-1$
            capitalword_ = false;
            if (ch==' ') return;
        } else if (grams_.length() >= N_GRAM) {
            grams_.deleteCharAt(0);
        }
        grams_.append(ch);

        if (Character.isUpperCase(ch)){
            if (Character.isUpperCase(lastchar)) capitalword_ = true;
        } else {
            capitalword_ = false;
        }
    }

    public NGram() {
        grams_ = new StringBuffer(" "); //$NON-NLS-1$
        capitalword_ = false;
    }

    public String get(int n) {
        if (capitalword_) return null;
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
        } else if (block == UnicodeBlock.BOPOMOFO || block == UnicodeBlock.BOPOMOFO_EXTENDED) {
            ch = '\u3105';
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
        Messages.getString("NGram.KANJI_1_13"),
        Messages.getString("NGram.KANJI_1_15"),
        Messages.getString("NGram.KANJI_1_18"),
        Messages.getString("NGram.KANJI_1_20"),
        Messages.getString("NGram.KANJI_1_30"),
        Messages.getString("NGram.KANJI_1_40"),
        Messages.getString("NGram.KANJI_1_41"),
        Messages.getString("NGram.KANJI_1_54"),
        Messages.getString("NGram.KANJI_1_61"),
        Messages.getString("NGram.KANJI_1_67"),
        Messages.getString("NGram.KANJI_1_83"),
        Messages.getString("NGram.KANJI_1_92"),
        Messages.getString("NGram.KANJI_2_13"),
        Messages.getString("NGram.KANJI_2_17"),
        Messages.getString("NGram.KANJI_2_22"),
        Messages.getString("NGram.KANJI_2_23"),
        Messages.getString("NGram.KANJI_2_24"),
        Messages.getString("NGram.KANJI_2_25"),
        Messages.getString("NGram.KANJI_2_50"),
        Messages.getString("NGram.KANJI_2_52"),
        Messages.getString("NGram.KANJI_2_53"),
        Messages.getString("NGram.KANJI_2_65"),
        Messages.getString("NGram.KANJI_2_66"),
        Messages.getString("NGram.KANJI_2_68"),
        Messages.getString("NGram.KANJI_2_72"),
        Messages.getString("NGram.KANJI_2_77"),
        Messages.getString("NGram.KANJI_2_96"),
        Messages.getString("NGram.KANJI_3_8"),
        Messages.getString("NGram.KANJI_3_13"),
        Messages.getString("NGram.KANJI_3_15"),
        Messages.getString("NGram.KANJI_3_18"),
        Messages.getString("NGram.KANJI_3_20"),
        Messages.getString("NGram.KANJI_3_22"),
        Messages.getString("NGram.KANJI_3_23"),
        Messages.getString("NGram.KANJI_3_24"),
        Messages.getString("NGram.KANJI_3_25"),
        Messages.getString("NGram.KANJI_3_30"),
        Messages.getString("NGram.KANJI_3_33"),
        Messages.getString("NGram.KANJI_3_37"),
        Messages.getString("NGram.KANJI_3_41"),
        Messages.getString("NGram.KANJI_3_49"),
        Messages.getString("NGram.KANJI_3_50"),
        Messages.getString("NGram.KANJI_3_52"),
        Messages.getString("NGram.KANJI_3_53"),
        Messages.getString("NGram.KANJI_3_54"),
        Messages.getString("NGram.KANJI_3_61"),
        Messages.getString("NGram.KANJI_3_65"),
        Messages.getString("NGram.KANJI_3_66"),
        Messages.getString("NGram.KANJI_3_67"),
        Messages.getString("NGram.KANJI_3_68"),
        Messages.getString("NGram.KANJI_3_70"),
        Messages.getString("NGram.KANJI_3_71"),
        Messages.getString("NGram.KANJI_3_77"),
        Messages.getString("NGram.KANJI_3_86"),
        Messages.getString("NGram.KANJI_3_92"),
        Messages.getString("NGram.KANJI_3_96"),
        Messages.getString("NGram.KANJI_3_99"),
        Messages.getString("NGram.KANJI_4_3"),
        Messages.getString("NGram.KANJI_4_10"),
        Messages.getString("NGram.KANJI_4_11"),
        Messages.getString("NGram.KANJI_4_13"),
        Messages.getString("NGram.KANJI_4_19"),
        Messages.getString("NGram.KANJI_4_23"),
        Messages.getString("NGram.KANJI_4_27"),
        Messages.getString("NGram.KANJI_4_31"),
        Messages.getString("NGram.KANJI_4_36"),
        Messages.getString("NGram.KANJI_4_43"),
        Messages.getString("NGram.KANJI_4_44"),
        Messages.getString("NGram.KANJI_4_48"),
        Messages.getString("NGram.KANJI_4_56"),
        Messages.getString("NGram.KANJI_4_69"),
        Messages.getString("NGram.KANJI_4_73"),
        Messages.getString("NGram.KANJI_5_3"),
        Messages.getString("NGram.KANJI_5_13"),
        Messages.getString("NGram.KANJI_5_18"),
        Messages.getString("NGram.KANJI_5_23"),
        Messages.getString("NGram.KANJI_5_31"),
        Messages.getString("NGram.KANJI_5_36"),
        Messages.getString("NGram.KANJI_5_41"),
        Messages.getString("NGram.KANJI_5_45"),
        Messages.getString("NGram.KANJI_5_48"),
        Messages.getString("NGram.KANJI_5_61"),
        Messages.getString("NGram.KANJI_5_78"),
        Messages.getString("NGram.KANJI_6_2"),
        Messages.getString("NGram.KANJI_6_3"),
        Messages.getString("NGram.KANJI_6_13"),
        Messages.getString("NGram.KANJI_6_23"),
        Messages.getString("NGram.KANJI_6_44"),
        Messages.getString("NGram.KANJI_6_48"),
        Messages.getString("NGram.KANJI_6_50"),
        Messages.getString("NGram.KANJI_6_96"),
        Messages.getString("NGram.KANJI_7_2"),
        Messages.getString("NGram.KANJI_7_3"),
        Messages.getString("NGram.KANJI_7_4"),
        Messages.getString("NGram.KANJI_7_6"),
        Messages.getString("NGram.KANJI_7_9"),
        Messages.getString("NGram.KANJI_7_12"),
        Messages.getString("NGram.KANJI_7_13"),
        Messages.getString("NGram.KANJI_7_14"),
        Messages.getString("NGram.KANJI_7_15"),
        Messages.getString("NGram.KANJI_7_16"),
        Messages.getString("NGram.KANJI_7_18"),
        Messages.getString("NGram.KANJI_7_20"),
        Messages.getString("NGram.KANJI_7_23"),
        Messages.getString("NGram.KANJI_7_24"),
        Messages.getString("NGram.KANJI_7_27"),
        Messages.getString("NGram.KANJI_7_29"),
        Messages.getString("NGram.KANJI_7_30"),
        Messages.getString("NGram.KANJI_7_33"),
        Messages.getString("NGram.KANJI_7_35"),
        Messages.getString("NGram.KANJI_7_36"),
        Messages.getString("NGram.KANJI_7_37"),
        Messages.getString("NGram.KANJI_7_38"),
        Messages.getString("NGram.KANJI_7_40"),
        Messages.getString("NGram.KANJI_7_41"),
        Messages.getString("NGram.KANJI_7_44"),
        Messages.getString("NGram.KANJI_7_45"),
        Messages.getString("NGram.KANJI_7_47"),
        Messages.getString("NGram.KANJI_7_48"),
        Messages.getString("NGram.KANJI_7_49"),
        Messages.getString("NGram.KANJI_7_50"),
        Messages.getString("NGram.KANJI_7_51"),
        Messages.getString("NGram.KANJI_7_52"),
        Messages.getString("NGram.KANJI_7_54"),
        Messages.getString("NGram.KANJI_7_55"),
        Messages.getString("NGram.KANJI_7_58"),
        Messages.getString("NGram.KANJI_7_59"),
        Messages.getString("NGram.KANJI_7_60"),
        Messages.getString("NGram.KANJI_7_61"),
        Messages.getString("NGram.KANJI_7_63"),
        Messages.getString("NGram.KANJI_7_64"),
        Messages.getString("NGram.KANJI_7_66"),
        Messages.getString("NGram.KANJI_7_67"),
        Messages.getString("NGram.KANJI_7_68"),
        Messages.getString("NGram.KANJI_7_70"),
        Messages.getString("NGram.KANJI_7_71"),
        Messages.getString("NGram.KANJI_7_74"),
        Messages.getString("NGram.KANJI_7_76"),
        Messages.getString("NGram.KANJI_7_77"),
        Messages.getString("NGram.KANJI_7_78"),
        Messages.getString("NGram.KANJI_7_79"),
        Messages.getString("NGram.KANJI_7_80"),
        Messages.getString("NGram.KANJI_7_83"),
        Messages.getString("NGram.KANJI_7_84"),
        Messages.getString("NGram.KANJI_7_85"),
        Messages.getString("NGram.KANJI_7_86"),
        Messages.getString("NGram.KANJI_7_92"),
        Messages.getString("NGram.KANJI_7_93"),
        Messages.getString("NGram.KANJI_7_94"),
        Messages.getString("NGram.KANJI_7_95"),
        Messages.getString("NGram.KANJI_7_96"),
        Messages.getString("NGram.KANJI_7_97"),
        Messages.getString("NGram.KANJI_7_98"),
        Messages.getString("NGram.KANJI_7_99")
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
