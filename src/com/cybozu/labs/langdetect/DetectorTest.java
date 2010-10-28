package com.cybozu.labs.langdetect;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cybozu.labs.langdetect.util.LangProfile;

/**
 * Unit test for {@link Detector} and {@link DetectorFactory}.
 * @author Nakatani Shuyo
 *
 */
public class DetectorTest {

    private static final String TRAINING_EN = "a a a b b c c d e";
    private static final String TRAINING_JA = "a b b c c c d d d";

    @Before
    public void setUp() throws Exception {
        DetectorFactory.clear();
        
        LangProfile profile_en = new LangProfile("en");
        for (String w : TRAINING_EN.split(" "))
            profile_en.add(w);
        DetectorFactory.addProfile(profile_en);

        LangProfile profile_ja = new LangProfile("ja");
        for (String w : TRAINING_JA.split(" "))
            profile_ja.add(w);
        DetectorFactory.addProfile(profile_ja);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testDetector1() throws LangDetectException {
        Detector detect = DetectorFactory.create();
        detect.append("a");
        assertEquals(detect.detect(), "en");
    }

    @Test
    public final void testDetector2() throws LangDetectException {
        Detector detect = DetectorFactory.create();
        detect.append("b d");
        assertEquals(detect.detect(), "ja");
    }

    @Test
    public final void testDetector3() throws LangDetectException {
        Detector detect = DetectorFactory.create();
        detect.append("d e");
        assertEquals(detect.detect(), "en");
    }

}
