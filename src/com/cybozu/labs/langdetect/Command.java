package com.cybozu.labs.langdetect;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import com.cybozu.labs.langdetect.util.LangProfile;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

/**
 * 
 * LangDetect Command Line Interface
 * 
 * This is a command line interface of Language Detection Library "LandDetect".
 * 
 * 
 * @author Nakatani Shuyo / Cybozu Labs, Inc.
 *
 */
public class Command {
    /** smoothing default parameter (ELE) */
    private static final double DEFAULT_ALPHA = 0.5;

    /** for Command line easy parser */
    private HashMap<String, String> opt_with_value = new HashMap<String, String>();
    private HashMap<String, String> values = new HashMap<String, String>();
    private HashSet<String> opt_without_value = new HashSet<String>();
    private ArrayList<String> arglist = new ArrayList<String>();

    /**
     * Command line easy parser
     * @param args comandline arguments
     */
    private void parse(String[] args) {
        for(int i=0;i<args.length;++i) {
            if (opt_with_value.containsKey(args[i])) {
                String key = opt_with_value.get(args[i]);
                values.put(key, args[i+1]);
                ++i;
            } else if (args[i].startsWith("-")) {
                opt_without_value.add(args[i]);
            } else {
                arglist.add(args[i]);
            }
        }
    }

    private void addOpt(String opt, String key, String value) {
        opt_with_value.put(opt, key);
        values.put(key, value);
    }
    private String get(String key) {
        return values.get(key);
    }
    private double getDouble(String key, double defaultValue) {
        try {
            return Double.valueOf(values.get(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private boolean hasOpt(String opt) {
        return opt_without_value.contains(opt);
    }

        
    /**
     * File search (easy glob)
     * @param directory directory path
     * @param pattern   searching file pattern with regular representation
     * @return matched file
     */
    private File searchFile(File directory, String pattern) {
        for(File file : directory.listFiles()) {
            if (file.getName().matches(pattern)) return file;
        }
        return null;
    }

    
    /**
     * Generate Language Profile from Wikipedia Abstract Database File
     * 
     * usage: --genprofile -d [abstracts directory] [language names]
     * 
     */
    public void generateProfile() {
        File directory = new File(get("directory"));
        for (String lang: arglist) {
            File file = searchFile(directory, lang + "wiki-.*-abstract\\.xml.*");
            if (file == null) {
                System.err.println("Not Found abstract xml : lang = " + lang);
                continue;
            }
            LangProfile profile = GenProfile.load(lang, file);
            profile.omitLessFreq();

            File profile_path = new File(get("directory") + "/profiles/" + lang);
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(profile_path);
                JSON.encode(profile, os);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os!=null) os.close();
                } catch (IOException e) {}
            }
        }        
    }

    /**
     * Language detection test for each file (--detectlang option)
     * 
     * usage: --detectlang -d [profile directory] -a [alpha] [test file(s)]
     * 
     */
    public void detectLang() {
        String profileDirectory = get("directory") + "/"; 
        DetectorFactory.loadProfile(profileDirectory);
        for (String filename: arglist) {
            Detector detector = DetectorFactory.create(getDouble("alpha", DEFAULT_ALPHA));
            if (hasOpt("--debug")) detector.setVerbose();
            BufferedReader is = null;
            try {
                is = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
                detector.append(is);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (is!=null) is.close();
                } catch (IOException e) {}
            }
            System.out.println(filename + ":" + detector.getProbabilities());

        }
    }

    /**
     * Batch Test of Language Detection (--batchtest option)
     * 
     * usage: --batchtest -d [profile directory] -a [alpha] [test data(s)]
     * 
     * The format of test data(s):
     *   [correct language name]\t[text body for test]\n
     *  
     */
    public void batchTest() {
        String profileDirectory = get("directory") + "/"; 
        DetectorFactory.loadProfile(profileDirectory);
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        for (String filename: arglist) {
            BufferedReader is = null;
            try {
                is = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
                while (is.ready()) {
                    String line = is.readLine();
                    int idx = line.indexOf('\t');
                    if (idx <= 0) continue;
                    String correctLang = line.substring(0, idx);
                    String text = line.substring(idx + 1);
                    
                    Detector detector = DetectorFactory.create(getDouble("alpha", DEFAULT_ALPHA));
                    detector.append(text);
/*
                    for(int j=0;j<text.length();++j) {
                        detector.append(text.charAt(j));
                        if (detector.isConvergence()) break;
                    }
*/
                    String lang = detector.detect();
                    if (!result.containsKey(correctLang)) result.put(correctLang, new ArrayList<String>());
                    result.get(correctLang).add(lang);
                    if (hasOpt("--debug")) System.out.println(correctLang + "," + lang + "," + (text.length()>100?text.substring(0, 100):text));
                }
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (is!=null) is.close();
                } catch (IOException e) {}
            }

            ArrayList<String> langlist = new ArrayList<String>(result.keySet());
            Collections.sort(langlist);

            int totalCount = 0, totalCorrect = 0;
            for ( String lang :langlist) {
                HashMap<String, Integer> resultCount = new HashMap<String, Integer>();
                int count = 0;
                ArrayList<String> list = result.get(lang);
                for (String detectedLang: list) {
                    ++count;
                    if (resultCount.containsKey(detectedLang)) {
                        resultCount.put(detectedLang, resultCount.get(detectedLang) + 1);
                    } else {
                        resultCount.put(detectedLang, 1);
                    }
                }
                int correct = resultCount.containsKey(lang)?resultCount.get(lang):0;
                double rate = correct / (double)count;
                System.out.println(String.format("%s (%d/%d=%.2f): %s", lang, correct, count, rate, resultCount));
                totalCorrect += correct;
                totalCount += count;
            }
            System.out.println(String.format("total: %d/%d = %.3f", totalCorrect, totalCount, totalCorrect / (double)totalCount));
            
        }
        
    }

    /**
     * Command Line Interface
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Command command = new Command();
        command.addOpt("-d", "directory", "./");
        command.addOpt("-a", "alpha", "" + DEFAULT_ALPHA);
        command.parse(args);

        if (command.hasOpt("--genprofile")) {
            command.generateProfile();
        } else if (command.hasOpt("--detectlang")) {
            command.detectLang();
        } else if (command.hasOpt("--batchtest")) {
            command.batchTest();
        }
    }
}
