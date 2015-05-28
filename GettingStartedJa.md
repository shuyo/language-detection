# 動作要件 #

  * Java 1.6
  * JSONIC (JSON ライブラリ、同梱) < http://sourceforge.jp/projects/jsonic/devel/ , Apache License 2.0 >


# ダウンロード #

言語判定ライブラリのパッケージをダウンロードリストからダウンロードして展開して適当なディレクトリに設置してください。特別なインストールは特に不要です。
  * http://code.google.com/p/language-detection/downloads/list
パッケージのファイル名は 'langdetect---.zip' です( には日付が入ります)。

パッケージは以下の構成となっています。

  * lib/langdetect.jar : ライブラリ本体
  * profiles/: 言語プロファイル(判定に必要なデータ)
  * doc/ : API リファレンス
  * src/ : ライブラリのソース


# テスト実行 #

ライブラリファイル本体 'langdetect.jar' は実行可能な jar ファイルになっており、コマンドラインから言語判定処理を簡単に試すことが出来ます。

```
$ java -jar lib/langdetect.jar --detectlang -d profiles [判定させたいテキストファイル(複数可)]

sample.txt:[de:0.999999863588238]
```

langdetect.jar には、ほかにもコマンドがあります。 [ToolsJa](ToolsJa.md) をご覧ください。


# チュートリアル #

  * [TutorialJa](TutorialJa.md)