/**
 * 
 */
package com.cybozu.labs.langdetect.util;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Nakatani Shuyo
 *
 */
public class NGramTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for constants
     */
    @Test
    public final void testConstants() {
        assertThat(NGram.N_GRAM, is(3));
        assertEquals(NGram.N_GRAM, 3);
    }

    /**
     * Test method for {@link NGram#normalize(char)} with Latin characters
     */
    @Test
    public final void testNormalizeWithLatin() {
        assertEquals(NGram.normalize('\u0000'), ' ');
        assertEquals(NGram.normalize('\u0009'), ' ');
        assertEquals(NGram.normalize('\u0020'), ' ');
        assertEquals(NGram.normalize('\u0030'), ' ');
        assertEquals(NGram.normalize('\u0040'), ' ');
        assertEquals(NGram.normalize('\u0041'), '\u0041');
        assertEquals(NGram.normalize('\u005a'), '\u005a');
        assertEquals(NGram.normalize('\u005b'), ' ');
        assertEquals(NGram.normalize('\u0060'), ' ');
        assertEquals(NGram.normalize('\u0061'), '\u0061');
        assertEquals(NGram.normalize('\u007a'), '\u007a');
        assertEquals(NGram.normalize('\u007b'), ' ');
        assertEquals(NGram.normalize('\u007f'), ' ');
        assertEquals(NGram.normalize('\u0080'), '\u0080');
        assertEquals(NGram.normalize('\u00a0'), ' ');
        assertEquals(NGram.normalize('\u00a1'), '\u00a1');
    }

    /**
     * Test method for {@link NGram#normalize(char)} with CJK Kanji characters
     */
    @Test
    public final void testNormalizeWithCJKKanji() {
        assertEquals(NGram.normalize('\u4E00'), '\u4E00');
        assertEquals(NGram.normalize('\u4E01'), '\u4E01');
        assertEquals(NGram.normalize('\u4E02'), '\u4E02');
        assertEquals(NGram.normalize('\u4E03'), '\u4E01');
        assertEquals(NGram.normalize('\u4E04'), '\u4E04');
        assertEquals(NGram.normalize('\u4E05'), '\u4E05');
        assertEquals(NGram.normalize('\u4E06'), '\u4E06');
        assertEquals(NGram.normalize('\u4E07'), '\u4E07');
        assertEquals(NGram.normalize('\u4E08'), '\u4E08');
        assertEquals(NGram.normalize('\u4E09'), '\u4E09');
        assertEquals(NGram.normalize('\u4E10'), '\u4E10');
        assertEquals(NGram.normalize('\u4E11'), '\u4E11');
        assertEquals(NGram.normalize('\u4E12'), '\u4E12');
        assertEquals(NGram.normalize('\u4E13'), '\u4E13');
        assertEquals(NGram.normalize('\u4E14'), '\u4E14');
        assertEquals(NGram.normalize('\u4E15'), '\u4E15');
        assertEquals(NGram.normalize('\u4E1e'), '\u4E1e');
        assertEquals(NGram.normalize('\u4E1f'), '\u4E1f');
        assertEquals(NGram.normalize('\u4E20'), '\u4E20');
        assertEquals(NGram.normalize('\u4E21'), '\u4E21');
        assertEquals(NGram.normalize('\u4E22'), '\u4E22');
        assertEquals(NGram.normalize('\u4E23'), '\u4E23');
        assertEquals(NGram.normalize('\u4E24'), '\u4E13');
        assertEquals(NGram.normalize('\u4E25'), '\u4E13');
        assertEquals(NGram.normalize('\u4E30'), '\u4E30');
    }

    /**
     * Test method for {@link NGram#normalize(char)} for Romanian characters
     */
    @Test
    public final void testNormalizeForRomanian() {
        assertEquals(NGram.normalize('\u015f'), '\u015f');
        assertEquals(NGram.normalize('\u0163'), '\u0163');
        assertEquals(NGram.normalize('\u0219'), '\u015f');
        assertEquals(NGram.normalize('\u021b'), '\u0163');
    }

    /**
     * Test method for {@link NGram#get(int)} and {@link NGram#addChar(char)}
     */
    @Test
    public final void testNGram() {
        NGram ngram = new NGram();
        assertEquals(ngram.get(0), null);
        assertEquals(ngram.get(1), null);
        assertEquals(ngram.get(2), null);
        assertEquals(ngram.get(3), null);
        assertEquals(ngram.get(4), null);
        ngram.addChar(' ');
        assertEquals(ngram.get(1), null);
        assertEquals(ngram.get(2), null);
        assertEquals(ngram.get(3), null);
        ngram.addChar('A');
        assertEquals(ngram.get(1), "A");
        assertEquals(ngram.get(2), " A");
        assertEquals(ngram.get(3), null);
        ngram.addChar('\u06cc');
        assertEquals(ngram.get(1), "\u064a");
        assertEquals(ngram.get(2), "A\u064a");
        assertEquals(ngram.get(3), " A\u064a");
        ngram.addChar('\u1ea0');
        assertEquals(ngram.get(1), "\u1ec3");
        assertEquals(ngram.get(2), "\u064a\u1ec3");
        assertEquals(ngram.get(3), "A\u064a\u1ec3");
        ngram.addChar('\u3044');
        assertEquals(ngram.get(1), "\u3042");
        assertEquals(ngram.get(2), "\u1ec3\u3042");
        assertEquals(ngram.get(3), "\u064a\u1ec3\u3042");

        ngram.addChar('\u30a4');
        assertEquals(ngram.get(1), "\u30a2");
        assertEquals(ngram.get(2), "\u3042\u30a2");
        assertEquals(ngram.get(3), "\u1ec3\u3042\u30a2");
        ngram.addChar('\u3106');
        assertEquals(ngram.get(1), "\u3105");
        assertEquals(ngram.get(2), "\u30a2\u3105");
        assertEquals(ngram.get(3), "\u3042\u30a2\u3105");
        ngram.addChar('\uac01');
        assertEquals(ngram.get(1), "\uac00");
        assertEquals(ngram.get(2), "\u3105\uac00");
        assertEquals(ngram.get(3), "\u30a2\u3105\uac00");
        ngram.addChar('\u2010');
        assertEquals(ngram.get(1), null);
        assertEquals(ngram.get(2), "\uac00 ");
        assertEquals(ngram.get(3), "\u3105\uac00 ");

        ngram.addChar('a');
        assertEquals(ngram.get(1), "a");
        assertEquals(ngram.get(2), " a");
        assertEquals(ngram.get(3), null);

    }
 
    /**
     * Test method for {@link NGram#get(int)} and {@link NGram#addChar(char)}
     */
    @Test
    public final void testNGram3() {
        NGram ngram = new NGram();

        ngram.addChar('A');
        assertEquals(ngram.get(1), "A");
        assertEquals(ngram.get(2), " A");
        assertEquals(ngram.get(3), null);

        ngram.addChar('1');
        assertEquals(ngram.get(1), null);
        assertEquals(ngram.get(2), "A ");
        assertEquals(ngram.get(3), " A ");
        
        ngram.addChar('B');
        assertEquals(ngram.get(1), "B");
        assertEquals(ngram.get(2), " B");
        assertEquals(ngram.get(3), null);
       
    }
 
    /**
     * Test method for {@link NGram#get(int)} and {@link NGram#addChar(char)}
     */
    @Test
    public final void testNormalizeVietnamese() {
        assertEquals(NGram.normalize_vi(""), "");
        assertEquals(NGram.normalize_vi("ABC"), "ABC");
        assertEquals(NGram.normalize_vi("012"), "012");
        assertEquals(NGram.normalize_vi("\u00c0"), "\u00c0");

        assertEquals(NGram.normalize_vi("\u0041\u0300"), "\u00C0");
        assertEquals(NGram.normalize_vi("\u0045\u0300"), "\u00C8");
        assertEquals(NGram.normalize_vi("\u0049\u0300"), "\u00CC");
        assertEquals(NGram.normalize_vi("\u004F\u0300"), "\u00D2");
        assertEquals(NGram.normalize_vi("\u0055\u0300"), "\u00D9");
        assertEquals(NGram.normalize_vi("\u0059\u0300"), "\u1EF2");
        assertEquals(NGram.normalize_vi("\u0061\u0300"), "\u00E0");
        assertEquals(NGram.normalize_vi("\u0065\u0300"), "\u00E8");
        assertEquals(NGram.normalize_vi("\u0069\u0300"), "\u00EC");
        assertEquals(NGram.normalize_vi("\u006F\u0300"), "\u00F2");
        assertEquals(NGram.normalize_vi("\u0075\u0300"), "\u00F9");
        assertEquals(NGram.normalize_vi("\u0079\u0300"), "\u1EF3");
        assertEquals(NGram.normalize_vi("\u00C2\u0300"), "\u1EA6");
        assertEquals(NGram.normalize_vi("\u00CA\u0300"), "\u1EC0");
        assertEquals(NGram.normalize_vi("\u00D4\u0300"), "\u1ED2");
        assertEquals(NGram.normalize_vi("\u00E2\u0300"), "\u1EA7");
        assertEquals(NGram.normalize_vi("\u00EA\u0300"), "\u1EC1");
        assertEquals(NGram.normalize_vi("\u00F4\u0300"), "\u1ED3");
        assertEquals(NGram.normalize_vi("\u0102\u0300"), "\u1EB0");
        assertEquals(NGram.normalize_vi("\u0103\u0300"), "\u1EB1");
        assertEquals(NGram.normalize_vi("\u01A0\u0300"), "\u1EDC");
        assertEquals(NGram.normalize_vi("\u01A1\u0300"), "\u1EDD");
        assertEquals(NGram.normalize_vi("\u01AF\u0300"), "\u1EEA");
        assertEquals(NGram.normalize_vi("\u01B0\u0300"), "\u1EEB");

        assertEquals(NGram.normalize_vi("\u0041\u0301"), "\u00C1");
        assertEquals(NGram.normalize_vi("\u0045\u0301"), "\u00C9");
        assertEquals(NGram.normalize_vi("\u0049\u0301"), "\u00CD");
        assertEquals(NGram.normalize_vi("\u004F\u0301"), "\u00D3");
        assertEquals(NGram.normalize_vi("\u0055\u0301"), "\u00DA");
        assertEquals(NGram.normalize_vi("\u0059\u0301"), "\u00DD");
        assertEquals(NGram.normalize_vi("\u0061\u0301"), "\u00E1");
        assertEquals(NGram.normalize_vi("\u0065\u0301"), "\u00E9");
        assertEquals(NGram.normalize_vi("\u0069\u0301"), "\u00ED");
        assertEquals(NGram.normalize_vi("\u006F\u0301"), "\u00F3");
        assertEquals(NGram.normalize_vi("\u0075\u0301"), "\u00FA");
        assertEquals(NGram.normalize_vi("\u0079\u0301"), "\u00FD");
        assertEquals(NGram.normalize_vi("\u00C2\u0301"), "\u1EA4");
        assertEquals(NGram.normalize_vi("\u00CA\u0301"), "\u1EBE");
        assertEquals(NGram.normalize_vi("\u00D4\u0301"), "\u1ED0");
        assertEquals(NGram.normalize_vi("\u00E2\u0301"), "\u1EA5");
        assertEquals(NGram.normalize_vi("\u00EA\u0301"), "\u1EBF");
        assertEquals(NGram.normalize_vi("\u00F4\u0301"), "\u1ED1");
        assertEquals(NGram.normalize_vi("\u0102\u0301"), "\u1EAE");
        assertEquals(NGram.normalize_vi("\u0103\u0301"), "\u1EAF");
        assertEquals(NGram.normalize_vi("\u01A0\u0301"), "\u1EDA");
        assertEquals(NGram.normalize_vi("\u01A1\u0301"), "\u1EDB");
        assertEquals(NGram.normalize_vi("\u01AF\u0301"), "\u1EE8");
        assertEquals(NGram.normalize_vi("\u01B0\u0301"), "\u1EE9");

        assertEquals(NGram.normalize_vi("\u0041\u0303"), "\u00C3");
        assertEquals(NGram.normalize_vi("\u0045\u0303"), "\u1EBC");
        assertEquals(NGram.normalize_vi("\u0049\u0303"), "\u0128");
        assertEquals(NGram.normalize_vi("\u004F\u0303"), "\u00D5");
        assertEquals(NGram.normalize_vi("\u0055\u0303"), "\u0168");
        assertEquals(NGram.normalize_vi("\u0059\u0303"), "\u1EF8");
        assertEquals(NGram.normalize_vi("\u0061\u0303"), "\u00E3");
        assertEquals(NGram.normalize_vi("\u0065\u0303"), "\u1EBD");
        assertEquals(NGram.normalize_vi("\u0069\u0303"), "\u0129");
        assertEquals(NGram.normalize_vi("\u006F\u0303"), "\u00F5");
        assertEquals(NGram.normalize_vi("\u0075\u0303"), "\u0169");
        assertEquals(NGram.normalize_vi("\u0079\u0303"), "\u1EF9");
        assertEquals(NGram.normalize_vi("\u00C2\u0303"), "\u1EAA");
        assertEquals(NGram.normalize_vi("\u00CA\u0303"), "\u1EC4");
        assertEquals(NGram.normalize_vi("\u00D4\u0303"), "\u1ED6");
        assertEquals(NGram.normalize_vi("\u00E2\u0303"), "\u1EAB");
        assertEquals(NGram.normalize_vi("\u00EA\u0303"), "\u1EC5");
        assertEquals(NGram.normalize_vi("\u00F4\u0303"), "\u1ED7");
        assertEquals(NGram.normalize_vi("\u0102\u0303"), "\u1EB4");
        assertEquals(NGram.normalize_vi("\u0103\u0303"), "\u1EB5");
        assertEquals(NGram.normalize_vi("\u01A0\u0303"), "\u1EE0");
        assertEquals(NGram.normalize_vi("\u01A1\u0303"), "\u1EE1");
        assertEquals(NGram.normalize_vi("\u01AF\u0303"), "\u1EEE");
        assertEquals(NGram.normalize_vi("\u01B0\u0303"), "\u1EEF");

        assertEquals(NGram.normalize_vi("\u0041\u0309"), "\u1EA2");
        assertEquals(NGram.normalize_vi("\u0045\u0309"), "\u1EBA");
        assertEquals(NGram.normalize_vi("\u0049\u0309"), "\u1EC8");
        assertEquals(NGram.normalize_vi("\u004F\u0309"), "\u1ECE");
        assertEquals(NGram.normalize_vi("\u0055\u0309"), "\u1EE6");
        assertEquals(NGram.normalize_vi("\u0059\u0309"), "\u1EF6");
        assertEquals(NGram.normalize_vi("\u0061\u0309"), "\u1EA3");
        assertEquals(NGram.normalize_vi("\u0065\u0309"), "\u1EBB");
        assertEquals(NGram.normalize_vi("\u0069\u0309"), "\u1EC9");
        assertEquals(NGram.normalize_vi("\u006F\u0309"), "\u1ECF");
        assertEquals(NGram.normalize_vi("\u0075\u0309"), "\u1EE7");
        assertEquals(NGram.normalize_vi("\u0079\u0309"), "\u1EF7");
        assertEquals(NGram.normalize_vi("\u00C2\u0309"), "\u1EA8");
        assertEquals(NGram.normalize_vi("\u00CA\u0309"), "\u1EC2");
        assertEquals(NGram.normalize_vi("\u00D4\u0309"), "\u1ED4");
        assertEquals(NGram.normalize_vi("\u00E2\u0309"), "\u1EA9");
        assertEquals(NGram.normalize_vi("\u00EA\u0309"), "\u1EC3");
        assertEquals(NGram.normalize_vi("\u00F4\u0309"), "\u1ED5");
        assertEquals(NGram.normalize_vi("\u0102\u0309"), "\u1EB2");
        assertEquals(NGram.normalize_vi("\u0103\u0309"), "\u1EB3");
        assertEquals(NGram.normalize_vi("\u01A0\u0309"), "\u1EDE");
        assertEquals(NGram.normalize_vi("\u01A1\u0309"), "\u1EDF");
        assertEquals(NGram.normalize_vi("\u01AF\u0309"), "\u1EEC");
        assertEquals(NGram.normalize_vi("\u01B0\u0309"), "\u1EED");

        assertEquals(NGram.normalize_vi("\u0041\u0323"), "\u1EA0");
        assertEquals(NGram.normalize_vi("\u0045\u0323"), "\u1EB8");
        assertEquals(NGram.normalize_vi("\u0049\u0323"), "\u1ECA");
        assertEquals(NGram.normalize_vi("\u004F\u0323"), "\u1ECC");
        assertEquals(NGram.normalize_vi("\u0055\u0323"), "\u1EE4");
        assertEquals(NGram.normalize_vi("\u0059\u0323"), "\u1EF4");
        assertEquals(NGram.normalize_vi("\u0061\u0323"), "\u1EA1");
        assertEquals(NGram.normalize_vi("\u0065\u0323"), "\u1EB9");
        assertEquals(NGram.normalize_vi("\u0069\u0323"), "\u1ECB");
        assertEquals(NGram.normalize_vi("\u006F\u0323"), "\u1ECD");
        assertEquals(NGram.normalize_vi("\u0075\u0323"), "\u1EE5");
        assertEquals(NGram.normalize_vi("\u0079\u0323"), "\u1EF5");
        assertEquals(NGram.normalize_vi("\u00C2\u0323"), "\u1EAC");
        assertEquals(NGram.normalize_vi("\u00CA\u0323"), "\u1EC6");
        assertEquals(NGram.normalize_vi("\u00D4\u0323"), "\u1ED8");
        assertEquals(NGram.normalize_vi("\u00E2\u0323"), "\u1EAD");
        assertEquals(NGram.normalize_vi("\u00EA\u0323"), "\u1EC7");
        assertEquals(NGram.normalize_vi("\u00F4\u0323"), "\u1ED9");
        assertEquals(NGram.normalize_vi("\u0102\u0323"), "\u1EB6");
        assertEquals(NGram.normalize_vi("\u0103\u0323"), "\u1EB7");
        assertEquals(NGram.normalize_vi("\u01A0\u0323"), "\u1EE2");
        assertEquals(NGram.normalize_vi("\u01A1\u0323"), "\u1EE3");
        assertEquals(NGram.normalize_vi("\u01AF\u0323"), "\u1EF0");
        assertEquals(NGram.normalize_vi("\u01B0\u0323"), "\u1EF1");

    }
}