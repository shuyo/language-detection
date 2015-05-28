# Tutorial #

This 'language-detection' library is to detect an language in which the specified text is written.
For language detection with this library, the necessary procedures are the following.

  * Initialize library
  * Set target text
  * Retrieve detection result


## Initialize library ##

Before using this library, call DetectorFactory#loadProfile() once to initialize.

```
DetectorFactory.loadProfile(profileDirectory);
```

The parameter of this method is a directory which has files of language profiles.
The language profiles are bundled with this library, so specify "trunk/profile" in repository as the parameter of loadProfile().


## Set target text ##

Detector class is an interface of this language detection library.
This class can be only instanced via DetectorFactory class.

Due to set target text to detect language, use Detector#append() method.

```
Detector detector = DetectorFactory.create();
detector.append(text);
```

The Detector#append() method has 2 versions of parameter.
One is to set whole text with java.lang.String parameter or another is to set text stream with java.io.Reader parameter.
Use whichever suit to your application.

Create a new Detector instance if you need to detect again.


## Retrieve detection result ##

There are 2 ways of retriving result after setting target text.

### Detector#detect ###

The Detector#detect method returns a single language name which has the most probability.
Language names are represented by language codes. See also LanguageList .

```
Detector detector = DetectorFactory.create();
detector.append(text);

String lang = detector.detect();
```


### Detector#getProbabilities ###

The Detector#getProbabilities method returns a languages list with their probabilities.
The list is in order of probability.


```
Detector detector = DetectorFactory.create();
detector.append(text);

ArrayList<Language> langlist = detector.getProbabilities();
```


See also:
  * http://language-detection.googlecode.com/svn/trunk/doc/com/cybozu/labs/langdetect/DetectorFactory.html
  * http://language-detection.googlecode.com/svn/trunk/doc/com/cybozu/labs/langdetect/Detector.html