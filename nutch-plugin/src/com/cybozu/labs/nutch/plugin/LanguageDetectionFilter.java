package com.cybozu.labs.nutch.plugin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.indexer.lucene.LuceneWriter;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.Parse;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

public class LanguageDetectionFilter implements IndexingFilter {
	private Configuration conf = null;
	private LangDetectException cause = null;

	public LanguageDetectionFilter() {
		System.err.println("LanguageDetectionFilter");
		Throwable t = new Throwable();
		t.printStackTrace(System.err);
	}

	public NutchDocument filter(NutchDocument doc, Parse parse, Text url,
			CrawlDatum datum, Inlinks inlinks) throws IndexingException {
		if (conf == null) {
			throw new IndexingException("Not Yet Initialization.");
		}
		if (cause != null) {
			throw new IndexingException("Initialization Failed.", cause);
		}

		String lang = parse.getData().getParseMeta().get(Metadata.LANGUAGE);
		if (lang == null) {
			StringBuilder text = new StringBuilder();
			text.append(parse.getData().getTitle()).append(" ")
					.append(parse.getText());
			try {
				Detector detector = DetectorFactory.create();
				detector.append(text.toString());
				lang = detector.detect();
			} catch (LangDetectException e) {
				throw new IndexingException("Detection failed.", e);
			}
		}
		if (lang == null) lang = "unknown";

		doc.add("lang", lang);
		return doc;
	}

	public void addIndexBackendOptions(Configuration conf) {
		LuceneWriter.addFieldOptions("lang", LuceneWriter.STORE.YES,
				LuceneWriter.INDEX.UNTOKENIZED, conf);
	}

	public void setConf(Configuration conf) {
		System.err.println("LanguageDetectionFilter#setConf");
		Throwable t = new Throwable();
		t.printStackTrace(System.err);
		this.conf = conf;
		try {
			DetectorFactory.loadProfile(conf.get("langdetect.profile.dir"));
		} catch (LangDetectException e) {
			// throw when filter() is called
			cause = e;
		}
	}

	public Configuration getConf() {
		return this.conf;
	}
}
