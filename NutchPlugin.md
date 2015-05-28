# Introduction #

Apache Nutch ( http://nutch.apache.org/ ) is a web search engine solution.
It consists of Lucene, Solr, web crawler, page scoring(Page Rank) and plugable distributed system with Hadoop.

Nutch also has <a href='http://wiki.apache.org/nutch/LanguageIdentifierPlugin'>a Language Identifer plugin</a> which can add a search by languages, but it supports only 14 languages (Danish, German, Greek, English, Spanish, Finnish, French, Hungarian, Italian, Dutch, Polish, Portuguese, Russian and Swedish) and doesn't have sufficient performance.
So we provides as Apache Nutch's plugin with our Language Detection Library.


# Language Detector Plugin for Apache Nutch #

  * 1. Get the package of Language Detection Library from <a href='http://code.google.com/p/language-detection/downloads/list'>Download list</a>
  * 2. Extract the following files from the package on a certain directory.
    * lib/`*`
    * profiles/`*`
    * nutch-plugin/`*`
  * 3. Copy the files of nutch-plugin/ into the plugin directory of Apache Nutch (installed previously)
```
cp nutch-plugin/* $NUTCH_HOME/plugins/language-identifier
```
  * 4. Edit the Nutch's configuration ($NUTCH\_HOME/conf/nutch-site.xml) to enable the plugin.
```
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>

<property>
  <name>http.agent.name</name>
  <value>[(TO REPLACE)Your agent name]</value>
</property>

<property>
  <name>http.robots.agents</name>
  <value>[(TO REPLACE)Your agent name],*</value>
</property>

<property>
  <name>plugin.includes</name>
  <value>protocol-http|urlfilter-regex|parse-(text|html|js|tika)|index-(basic|anchor)|query-(basic|site|url)|response-(json|xml)|summary-basic|scoring-opic|urlnormalizer-(pass|regex|basic)|language-identifier</value>
</property>

<property>
  <name>langdetect.profile.dir</name>
  <value>[(TO REPLACE)language profile directory path]</value>
</property>

</configuration>
```