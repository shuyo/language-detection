# Tutorial #

language-detection は指定されたテキストが何語で書かれたものかを判定するライブラリです。
language-detection を使って言語判定を行うには、以下の手順が必要です。

  * ライブラリの初期化
  * 対象テキストの設定
  * 判定結果の取得


## ライブラリの初期化 ##

このライブラリを使用する前に、DetectorFactory#loadProfile() を一度呼び出す必要があります。

```
DetectorFactory.loadProfile(profileDirectory);
```

このメソッドには、言語プロファイルを格納したディレクトリパスを引数に与えます。
言語プロファイルは、リポジトリの "trunk/profile" 以下にて配布されています。2010/10 現在、47言語分のプロファイルが収められています。

また、Wikipedia の abstract database file から言語プロファイルを作成することも可能です。詳しくは ToolsJa をご覧ください。


## 対象テキストの設定 ##

このライブラリの操作は Detector クラスを通じて行います。
Detector クラスのインスタンスは DetectorFactory#create() によって得られます。

判定対象テキストの設定は、Detector#append() メソッドで行います。

```
String text;

Detector detector = DetectorFactory.create();
detector.append(text);
```

Detector#append() メソッドには２種類の引数があります。
１つは java.lang.String を引数とするものです。対象テキスト全体を(あるいは分割されたものを複数回に分けて)設定する場合に使用します。
もう１つは java.io.Reader を引数とするものです。対象テキストを開いた Reader ストリームを渡します。
アプリケーションにあわせて、適した引数を用いてください。


## 判定結果の取得 ##

判定結果の取得方法は２種類あります。

### Detector#detect ###

Detector#detect メソッドは、最も確率の高い言語の名称を１つ返します。
言語名は言語コードで表されます。LanguageListJa もご覧ください。

```
Detector detector = DetectorFactory.create();
detector.append(text);

String lang = detector.detect();
```


### Detector#getProbabilities ###

Detector#getProbabilities メソッドは、確率の高い順に複数の候補(言語名とその確率)を返します。

```
Detector detector = DetectorFactory.create();
detector.append(text);

ArrayList<Language> langlist = detector.getProbabilities();
```


それぞれのクラスの仕様もご確認ください。
  * http://language-detection.googlecode.com/svn/trunk/doc/com/cybozu/labs/langdetect/DetectorFactory.html
  * http://language-detection.googlecode.com/svn/trunk/doc/com/cybozu/labs/langdetect/Detector.html