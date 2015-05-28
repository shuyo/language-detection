# Introduction #

Language-Detection Project は、テキストの言語判定(言語識別|言語判別)を行う Java ライブラリです。
検索機能に言語の絞り込みを付けたい場合や、テキストに対して言語別のフィルタ(スパムフィルタなど)を適用したい場合に利用していただけます。

  * 53 言語を 99% 以上の精度で判定
  * Wikipedia データベースの要約ファイルから言語プロファイルを作成
  * ベイジアンフィルタを用いて、言語ごとの確率を出力
  * ライセンスは Apache License 2.0

参考:
  * 自然言語処理勉強会＠東京での本ライブラリについて発表資料
  * http://d.hatena.ne.jp/n_shuyo/20100925/language_detection

# News #

  * 2012/01/12
    * リポジトリを subversion から git に移行(Maven サポートのため)
  * 2011/09/13
    * エストニア語、リトアニア語、ラトビア語、スロベニア語の言語プロファイルを追加
    * ロード済みの言語プロファイル名一覧を取得する getLangList() をサポート ( [issue 20](https://code.google.com/p/language-detection/issues/detail?id=20) )
    * プレインテキストから言語プロファイルを生成するツールを追加 ( [issue 23](https://code.google.com/p/language-detection/issues/detail?id=23) )
    * [issue 21](https://code.google.com/p/language-detection/issues/detail?id=21) で指摘されたバグを修正
  * 2011/02/02
    * バグ修正 (no profile directory / long text detectation)
  * 2011/01/24
    * 判定速度を4倍速に (thanks to [elmer.garduno](http://code.google.com/u/elmer.garduno/))
  * 2010/12/22
    * Apache Nutch 用プラグインのサポート
  * 2010/11/18
    * パッケージファイルの提供

# Requires #

  * Java SDK 1.6
  * JSONIC < http://sourceforge.jp/projects/jsonic/devel/ > (Apache License 2.0 / bundled)

# チュートリアル #

  * TutorialJa
  * ToolsJa : 付属するツールのリファレンス

# 導入マニュアル #

  * GettingStartedJa : 言語判定ライブラリの導入
  * NutchPluginJa : Apache Nutch 用の言語判定プラグイン

# API リファレンス #

  * http://language-detection.googlecode.com/git/doc/index.html

# 判定可能な言語 #

  * LanguageListJa

# Sample Code #

```
import java.util.ArrayList;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.Language;

class LangDetectSample {
    public void init(String profileDirectory) throws LangDetectException {
        DetectorFactory.loadProfile(profileDirectory);
    }
    public String detect(String text) throws LangDetectException {
        Detector detector = DetectorFactory.create();
        detector.append(text);
        return detector.detect();
    }
    public ArrayList<Language> detectLangs(String text) throws LangDetectException {
        Detector detector = DetectorFactory.create();
        detector.append(text);
        return detector.getProbabilities();
    }
}
```