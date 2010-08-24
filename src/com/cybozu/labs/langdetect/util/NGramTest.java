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
     * Test method for {@link com.cybozu.labs.langdetect.util.NGram#addChar(char)}.
     */
    @Test
    public final void testAddChar() {
        assertThat(NGram.N_GRAM, is(3));
        assertEquals(NGram.N_GRAM, 3);
    }

    /**
     * Test method for {@link com.cybozu.labs.langdetect.util.NGram#NGram()}.
     */
    @Test
    public final void testNGram() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link com.cybozu.labs.langdetect.util.NGram#get(int)}.
     */
    @Test
    public final void testGet() {
        fail("Not yet implemented"); // TODO
    }
}
