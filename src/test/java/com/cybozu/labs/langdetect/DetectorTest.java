package com.cybozu.labs.langdetect;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link Detector} and {@link DetectorFactory}.
 * @author Nakatani Shuyo
 *
 */
public class DetectorTest {

    private static final String JSON_LANG1 = "{\"freq\":{\"A\":3,\"B\":6,\"C\":3,\"AB\":2,\"BC\":1,\"ABC\":2,\"BBC\":1,\"CBA\":1},\"n_words\":[12,3,4],\"name\":\"lang1\"}";
    private static final String JSON_LANG2 = "{\"freq\":{\"A\":6,\"B\":3,\"C\":3,\"AA\":3,\"AB\":2,\"ABC\":1,\"ABA\":1,\"CAA\":1},\"n_words\":[12,5,3],\"name\":\"lang2\"}";


    @Before
    public void setUp() throws Exception {
        
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testFactoryFromJsonString() throws LangDetectException {
        DetectorFactory.clear();
        ArrayList<String> profiles = new ArrayList<String>();
        profiles.add(JSON_LANG1);
        profiles.add(JSON_LANG2);
        DetectorFactory.loadProfile(profiles);
        List<String> langList = DetectorFactory.getLangList();
        assertEquals(langList.size(), 2);
        assertEquals(langList.get(0), "lang1");
        assertEquals(langList.get(1), "lang2");
    }

    @Test
    public final void testFactoryFromJsonStringForMultiProfiles() throws LangDetectException {
        DetectorFactory.clear();
        ArrayList<String> profiles = new ArrayList<String>();
        profiles.add(JSON_LANG1);
        profiles.add(JSON_LANG2);
        DetectorProfiles profiles2 = DetectorFactory.loadProfile(profiles);
        Detector detector = DetectorFactory.create(profiles2);
        detector.append("A");
        String lang = detector.detect();
        assertEquals("lang2", lang);
    }
}