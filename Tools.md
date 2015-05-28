# Tools #

This package provides some utilities.

  * Generate language profile from Wikipedia abstract database file
  * Batch test for test file which has multiple text with current language
  * Single test for each text file in any languages

The library lib/detectlang.jar is an executable jar so that these tools are available immediately.


## Generate language profile ##

This tool generates language profiles from Wikipedia abstract database files or plain text.

Wikipedia abstract database files can be retrieved from "Wikipedia Downloads" ( http://download.wikimedia.org/ ).
They form '(language code)wiki-(version)-abstract.xml' (e.g. 'enwiki-20101004-abstract.xml' ). See also LanguageList about language code.

usage:
```
java -jar lib/langdetect.jar --genprofile -d [directory path] [language codes]
```

  * Specify the directory which has abstract databases by -d option.
  * This tool can handle gzip compressed file.

Remark: The database filename in Chinese is like 'zhwiki-(version)-abstract-zh-cn.xml' or zhwiki-(version)-abstract-zh-tw.xml', so that it must be modified 'zh-cnwiki-(version)-abstract.xml' or 'zh-twwiki-(version)-abstract.xml'.


To generate language profile from a plain text, use the genprofile-text command.

usage:
```
java -jar lib/langdetect.jar --genprofile-text -l [language code] [text file path]
```

The text is written in the specified language.


## Batch test ##

This tool is to test multiple supervised data and output summary of result.
It is mainly used for precision measurement.

usage:
```
java -jar lib/langdetect.jar --batchtest -d [profile directory] [test data(s)]
```

  * Specify the directory which has language profile files by -d option.

### Test data for batch test ###
The test data is described one supervised data one line.
Each line forms the following.

```
[correct language code]\t[text body for test]
```

other options:
  * --debug : output debug information
  * -a [alpha](alpha.md) : additional smoothing parameter (default = 0.5)

## Single test ##

This tools is to test a single file for unsupervised text.

usage:
```
java -jar lib/langdetect.jar --detectlang -d [profile directory] [test file(s)]
```

  * Specify the directory which has language profile files by -d option.

other options:
  * --debug : output debug information
  * -a [alpha](alpha.md) : additional smoothing parameter (default = 0.5)