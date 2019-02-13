# language-detection
This is a language detection library implemented in plain Java. (aliases: language identification, language guessing)

* Presentation : http://www.slideshare.net/shuyo/language-detection-library-for-java
* ProjectHomeJa : Project Home in Japanese
* Abstract
* Generate language profiles from Wikipedia abstract xml
* Detect language of a text using naive Bayesian filter
* 99% over precision for 53 languages

### Downloads
Available packages are on [Old site > Downloads](https://code.google.com/archive/p/language-detection/downloads).

### Requires
* Java 1.6 or later
* JSONIC (bundled) < http://sourceforge.jp/projects/jsonic/devel/ , Apache License 2.0 >
### Documents
* GettingStarted
* Tutorial
* Tools : Document of Support Tools
* [Old site > API Reference](http://language-detection.googlecode.com/git/doc/index.html)
* NutchPlugin : Language Detector Plugin for Apache Nutch
* LanguageList : List of Available Languages
* FrequentlyAskedQuestion

### Sample
```
import java.util.ArrayList;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.Language;

class LangDetectSample { 
public void init(String profileDirectory) throws LangDetectException { 
DetectorFactory.loadProfile(profileDirectory); 
} public String detect(String text) throws LangDetectException { 
Detector detector = DetectorFactory.create(); 
detector.append(text); 
return detector.detect(); 
} public ArrayList detectLangs(String text) throws LangDetectException { 
Detector detector = DetectorFactory.create(); 
detector.append(text); 
return detector.getProbabilities(); 
} 
}```

### Bibtex
```@misc{
nakatani2010langdetect, 
title = {Language Detection Library for Java}, 
author = {Shuyo, Nakatani}, 
url = {http://code.google.com/p/language-detection/}, 
year = {2010}
}```

### Copyrights and License
Copyright (c) 2010-2014 Cybozu Labs, Inc. All rights reserved.

> Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
> http://www.apache.org/licenses/LICENSE-2.0
> Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
