# Can langdetect handle short texts? #

This library requires that a detection text has some length, almost 10-20 words over.

It may return a wrong language for very short text with 1-10 words.

See also [Issue 8](http://code.google.com/p/language-detection/issues/detail?id=8) and [Issue 12](http://code.google.com/p/language-detection/issues/detail?id=12).




# I'd like to restrict detection languages or give priority over some languages. #

There are several ways.
The simplest one is to remain only your necessary languages in profiles directory.

If you want different range for each detection, you can use Detector#setPriorMap method.
This method is to set the prior probabilities of languages.

```
// detect en, fr and ja only (don't detect others)
Detector detector = DetectorFactory.create();
HashMap priorMap = new HashMap();
priorMap.put("en", new Double(0.1));
priorMap.put("fr", new Double(0.1));
priorMap.put("ja", new Double(0.1));
detector.setPriorMap(priorMap);
```

```
// give priority over en
Detector detector = DetectorFactory.create();
HashMap priorMap = new HashMap();
priorMap.put("en", new Double(0.5));
priorMap.put("fr", new Double(0.1));
detector.setPriorMap(priorMap);
```

See also [Issue 13](http://code.google.com/p/language-detection/issues/detail?id=13).



# Each detected language differs for the same document. #

Langdetect uses random sampling for avoiding local noises(person name, place name and so on), so the language detections of the same document might differ for every time.

If you wouldn't like it, specify random seed with DetectorFactory.setSeed() after loadProfile().

Then all detections for the same document return the same language and probability.

```
DetectorFactory.loadProfile(profileDirectory);
DetectorFactory.setSeed(0);
```