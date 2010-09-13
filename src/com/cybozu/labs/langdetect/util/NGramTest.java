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
        assertEquals(NGram.normalize('\u4E01'), '\u4E00');
        assertEquals(NGram.normalize('\u4E02'), '\u4E02');
        assertEquals(NGram.normalize('\u4E03'), '\u4E00');
        assertEquals(NGram.normalize('\u4E04'), '\u4E04');
        assertEquals(NGram.normalize('\u4E05'), '\u4E05');
        assertEquals(NGram.normalize('\u4E06'), '\u4E06');
        assertEquals(NGram.normalize('\u4E07'), '\u4E07');
        assertEquals(NGram.normalize('\u4E08'), '\u4E00');
        assertEquals(NGram.normalize('\u4E09'), '\u4E00');
        assertEquals(NGram.normalize('\u4E10'), '\u4E10');
        assertEquals(NGram.normalize('\u4E11'), '\u4E00');
        assertEquals(NGram.normalize('\u4E12'), '\u4E12');
        assertEquals(NGram.normalize('\u4E13'), '\u4E13');
        assertEquals(NGram.normalize('\u4E14'), '\u4E00');
        assertEquals(NGram.normalize('\u4E15'), '\u4E15');
        assertEquals(NGram.normalize('\u4E1e'), '\u4E1e');
        assertEquals(NGram.normalize('\u4E1f'), '\u4E15');
        assertEquals(NGram.normalize('\u4E20'), '\u4E20');
        assertEquals(NGram.normalize('\u4E21'), '\u4E21');
        assertEquals(NGram.normalize('\u4E22'), '\u4E13');
        assertEquals(NGram.normalize('\u4E23'), '\u4E23');
        assertEquals(NGram.normalize('\u4E24'), '\u4E13');
        assertEquals(NGram.normalize('\u4E25'), '\u4E13');
        assertEquals(NGram.normalize('\u4E30'), '\u4E10');
    }

}