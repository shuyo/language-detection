/**
 * 
 */
package com.cybozu.labs.langdetect.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Nakatani Shuyo
 *
 */
public class TagExtractorTest {

    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link TagExtractor#TagExtractor(String, int)}.
     */
    @Test
    public final void testTagExtractor() {
        TagExtractor extractor = new TagExtractor(null, 0);
        assertEquals(extractor.target_, null);
        assertEquals(extractor.threshold_, 0);

        TagExtractor extractor2 = new TagExtractor("abstract", 10);
        assertEquals(extractor2.target_, "abstract");
        assertEquals(extractor2.threshold_, 10);
}

    /**
     * Test method for {@link TagExtractor#setTag(String)}.
     */
    @Test
    public final void testSetTag() {
        TagExtractor extractor = new TagExtractor(null, 0);
        extractor.setTag("");
        assertEquals(extractor.tag_, "");
        extractor.setTag(null);
        assertEquals(extractor.tag_, null);
    }

    /**
     * Test method for {@link TagExtractor#add(String)}.
     */
    @Test
    public final void testAdd() {
        TagExtractor extractor = new TagExtractor(null, 0);
        extractor.add("");
        extractor.add(null);    // ignore
    }

    /**
     * Test method for {@link TagExtractor#closeTag(LangProfile)}.
     */
    @Test
    public final void testCloseTag() {
        TagExtractor extractor = new TagExtractor(null, 0);
        extractor.closeTag();    // ignore
    }

    
    /**
     * Scenario Test of extracting &lt;abstract&gt; tag from Wikipedia database.
     */
    @Test
    public final void testNormalScenario() {
        TagExtractor extractor = new TagExtractor("abstract", 10);
        assertEquals(extractor.count(), 0);

        LangProfile profile = new LangProfile("en");

        // normal
        extractor.setTag("abstract");
        extractor.add("This is a sample text.");
        profile.update(extractor.closeTag());
        assertEquals(extractor.count(), 1);
        assertEquals(profile.n_words[0], 17);  // Thisisasampletext
        assertEquals(profile.n_words[1], 22);  // _T, Th, hi, ...
        assertEquals(profile.n_words[2], 17);  // _Th, Thi, his, ...

        // too short
        extractor.setTag("abstract");
        extractor.add("sample");
        profile.update(extractor.closeTag());
        assertEquals(extractor.count(), 1);

        // other tags
        extractor.setTag("div");
        extractor.add("This is a sample text which is enough long.");
        profile.update(extractor.closeTag());
        assertEquals(extractor.count(), 1);
    }

    /**
     * Test method for {@link TagExtractor#clear()}.
     */
    @Test
    public final void testClear() {
        TagExtractor extractor = new TagExtractor("abstract", 10);
        extractor.setTag("abstract");
        extractor.add("This is a sample text.");
        assertEquals(extractor.buf_.toString(), "This is a sample text.");
        assertEquals(extractor.tag_, "abstract");
        extractor.clear();
        assertEquals(extractor.buf_.toString(), "");
        assertEquals(extractor.tag_, null);
    }


}
