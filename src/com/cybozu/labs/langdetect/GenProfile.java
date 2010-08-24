package com.cybozu.labs.langdetect;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.cybozu.labs.langdetect.util.LangProfile;
import com.cybozu.labs.langdetect.util.NGram;
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
     * @param lang
     * @param filename
     * @return
     */
    public static LangProfile load(String lang, String filename) {

        LangProfile profile = new LangProfile(lang, NGram.N_GRAM);

        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(new File(filename)));
            if (filename.endsWith(".gz")) is = new GZIPInputStream(is);

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
            } finally {
                try {
                    if (reader != null) reader.close();
                } catch (XMLStreamException e) {}
            }
            System.out.println(lang + ":" + tagextractor.count());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {}
        }
        return profile;
    }
}
