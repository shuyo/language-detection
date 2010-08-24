package com.cybozu.labs.langdetect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.cybozu.labs.langdetect.util.LangProfile;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

public class Command {
    private static final double ALPHA = 1;
    private HashMap<String, String> opt_with_value = new HashMap<String, String>();
    private HashMap<String, String> values = new HashMap<String, String>();
    private HashSet<String> opt_without_value = new HashSet<String>();
    private ArrayList<String> arglist = new ArrayList<String>();

    private void add(String opt, String key, String value) {
        opt_with_value.put(opt, key);
        values.put(key, value);
    }
    private String get(String key) {
        return values.get(key);
    }
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

    private boolean hasOpt(String opt) {
        return opt_without_value.contains(opt);
    }

    private void generateProfile() {
        String directory = get("directory") + "/"; 
        for (String lang: arglist) {
            String filename = directory + lang + "wiki-latest-abstract.xml.gz";

            LangProfile profile = GenProfile.load(lang, filename);
            profile.omitLessFreq();

            File file = new File(directory + "profiles/" + lang);
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file);
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

    private void detect() {
        String profileDirectory = get("directory") + "/"; 
        DetectorFactory.loadProfile(profileDirectory);
        for (String filename: arglist) {
            Detector detector = DetectorFactory.create(ALPHA);
            BufferedReader is = null;
            try {
                is = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
                char[] buf = new char[1024]; 
                while (is.ready()) {
                    int length = is.read(buf);
                    for(int i=0;i<length;++i) {
                        detector.append(buf[i]);
                        if (detector.isConvergence()) break;
                    }
                }
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

    private void batchtest() {
        String profileDirectory = get("directory") + "/"; 
        DetectorFactory.loadProfile(profileDirectory);
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        for (String filename: arglist) {
            BufferedReader is = null;
            try {
                is = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
                while (is.ready()) {
                    String line = is.readLine();
                    int i = line.indexOf('\t');
                    if (i<=0) continue;
                    String correctLang = line.substring(0, i);
                    String text = line.substring(i+1);
                    
                    Detector detector = DetectorFactory.create(ALPHA);
                    for(int j=0;j<text.length();++j) {
                        detector.append(text.charAt(j));
                        if (detector.isConvergence()) break;
                    }
                    String lang = detector.detect();
                    if (!result.containsKey(correctLang)) result.put(correctLang, new ArrayList<String>());
                    result.get(correctLang).add(lang);
                }
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (is!=null) is.close();
                } catch (IOException e) {}
            }
            System.out.println(result);
            
        }
        
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Command command = new Command();
        command.add("-d", "directory", "./");
        command.parse(args);

        if (command.hasOpt("-gp")) {
            command.generateProfile();
        } else if (command.hasOpt("-ld")) {
            command.detect();
        } else if (command.hasOpt("-bt")) {
            command.batchtest();
        }
    }
}
