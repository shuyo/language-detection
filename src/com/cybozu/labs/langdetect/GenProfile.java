package com.cybozu.labs.langdetect;

import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.cybozu.labs.langdetect.util.LangProfile;
import com.cybozu.labs.langdetect.util.TagExtractor;

/**
 * Load Wikipedia's abstract XML as corpus and
 * generate its language profile in JSON format.
 * 
 * @author Nakatani Shuyo
 * 
 */
public class GenProfile {

    /**
     * Load Wikipedia abstract database file and generate its language profile
     * @param lang target language name
     * @param file target database file path
     * @return Language profile instance
     * @throws LangDetectException 
     */
    public static LangProfile load(String lang, File file) throws LangDetectException {

        LangProfile profile = new LangProfile(lang);

        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            if (file.getName().endsWith(".gz")) is = new GZIPInputStream(is);

            TagExtractor tagextractor = new TagExtractor("abstract", 100);

            XMLStreamReader reader = null;
            try {
                XMLInputFactory factory = XMLInputFactory.newInstance();
                reader = factory.createXMLStreamReader(is);
                while (reader.hasNext()) {
                    switch (reader.next()) {
                    case XMLStreamReader.START_ELEMENT:
                        tagextractor.setTag(reader.getName().toString());
                        break;
                    case XMLStreamReader.CHARACTERS:
                        tagextractor.add(reader.getText());
                        break;
                    case XMLStreamReader.END_ELEMENT:
                        tagextractor.closeTag(profile);
                        break;
                    }
                }
            } catch (XMLStreamException e) {
                throw new LangDetectException(ErrorCode.TrainDataFormatError, "Training database file '" + file.getName() + "' is an invalid XML.");
            } finally {
                try {
                    if (reader != null) reader.close();
                } catch (XMLStreamException e) {}
            }
            System.out.println(lang + ":" + tagextractor.count());

        } catch (IOException e) {
            throw new LangDetectException(ErrorCode.CantOpenTrainData, "Can't open training database file '" + file.getName() + "'");
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {}
        }
        return profile;
    }
}
