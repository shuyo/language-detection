/**
 * 
 */
package com.cybozu.labs.langdetect;

import static org.junit.Assert.*;

import java.util.ArrayList;

import net.arnx.jsonic.JSON;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cybozu.labs.langdetect.util.LangProfile;

/**
 * Multi Profile Test
 * 
 * @author Nakatani Shuyo
 */
public class MultipleProfileTest {

    private DetectorProfiles profile1;
    private DetectorProfiles profile2;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        ArrayList<String> sample_data1 = new ArrayList<String>();
        ArrayList<String> sample_data2 = new ArrayList<String>();

        LangProfile profile_en = new LangProfile("en");
        profile_en.update("This is a pen.");
        String json_en = JSON.encode(profile_en);
        sample_data1.add(json_en);
        sample_data2.add(json_en);

        LangProfile profile_it = new LangProfile("it");
        profile_it.update("Sono un studente.");
        String json_it = JSON.encode(profile_it);
        sample_data1.add(json_it);
        
        LangProfile profile_fr = new LangProfile("fr");
        profile_fr.update("Je suis japonais.");
        String json_fr = JSON.encode(profile_fr);
        sample_data2.add(json_fr);

        profile1 = DetectorFactory.loadProfile(sample_data1);
        profile2 = DetectorFactory.loadProfile(sample_data2);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMultiProfile1() throws LangDetectException {
        Detector detector1 = DetectorFactory.create(profile1);
        Detector detector2 = DetectorFactory.create(profile2);
        String text = "is";
        detector1.append(text);
        detector2.append(text);
        assertEquals("en", detector1.detect());
        assertEquals("en", detector2.detect());
    }

    @Test
    public void testMultiProfile2() throws LangDetectException {
        Detector detector1 = DetectorFactory.create(profile1);
        Detector detector2 = DetectorFactory.create(profile2);
        String text = "sono";
        detector1.append(text);
        detector2.append(text);
        assertEquals("it", detector1.detect());
        assertEquals("fr", detector2.detect());
    }

    @Test
    public void testMultiProfile3() throws LangDetectException {
        Detector detector1 = DetectorFactory.create(profile1);
        Detector detector2 = DetectorFactory.create(profile2);
        String text = "suis";
        detector1.append(text);
        detector2.append(text);
        assertEquals("en", detector1.detect());
        assertEquals("fr", detector2.detect());
    }
}
