# Tools #

本言語判別ライブラリには３つのツールが用意されています。

  * プロファイル生成ツール: Wikipedia abstract database ファイルから言語プロファイルを作成
  * バッチテストツール: 複数のテスト(正解あり)を行い、集計結果を出力
  * 言語判別ツール: テキストの言語判別を行い、その言語と確率を出力

lib/detectlang.jar ファイルは実行可能な jar ファイルとなっており、適切なオプションを与えて実行することでこれらのツールを利用することが出来ます。


## プロファイル生成ツール ##

Wikipedia の abstract database file やテキストファイルから本ライブラリ用のプロファイルを生成するツールです。

Wikipedia の abstract database file は '(language code)wiki-(version)-abstract.xml' (e.g. 'enwiki-20101004-abstract.xml' ) という形をしており、"Wikipedia Downloads" ( http://download.wikimedia.org/ ) から取得できます。
'language code' については LanguageListJa もご覧ください。

使い方:
```
java -jar lib/langdetect.jar --genprofile -d [directory path] [language codes]
```

  * abstract database file を設置したディレクトリは -d オプションで指定します。
  * 本ツールは gzip で圧縮されたファイルもそのまま扱うことが出来ます。

注意: 中国語の abstract database file は 'zhwiki-(version)-abstract-zh-cn.xml'(簡体字版) もしくは 'zhwiki-(version)-abstract-zh-tw.xml'(繁体字版) というファイル名になっていますので、それぞれ 'zh-cnwiki-(version)-abstract.xml' or 'zh-twwiki-(version)-abstract.xml' のようにリネームして使用してください。


テキストファイルから言語プロファイルを生成するには、genprofile-text コマンドを使います。

使い方:
```
java -jar lib/langdetect.jar --genprofile-text -l [language code] [text file path]
```

language code に指定する言語で書かれたテキストファイルを与えてください。


## バッチテストツール ##

複数の正解付きテキストデータの言語を判定し、その結果のサマリーを出力するツールです。
主に精度の測定に使用することを想定しています。

使い方:
```
java -jar lib/langdetect.jar --batchtest -d [profile directory] [test data(s)]
```

  * 言語プロファイルを設置したディレクトリは -d オプションで指定します。

その他のオプション:
  * --debug : デバッグ情報の出力
  * `-a [alpha]` : 加算スムージングパラメータの指定 (デフォルト値 = 0.5)

### バッチテスト用のテストデータの作り方 ###
テストデータは１行に１つのデータが記述された通常のテキストファイルです。
各行は次のようなフォーマットになります。

```
[正解の言語コード]\t[テスト用のテキスト本体]
```

  * \t は TAB コード(0x09)です
  * テキストに改行を含むことは出来ません(改行をスペースに変えてもテスト結果に影響ありません)

## 言語判別ツール ##

This tools is to test a single file for unsupervised text.

usage:
```
java -jar lib/langdetect.jar --detectlang -d [profile directory] [test file(s)]
```

  * Specify the directory which has language profile files by -d option.

その他のオプション:
  * --debug : デバッグ情報の出力
  * `-a [alpha]` : 加算スムージングパラメータの指定 (デフォルト値 = 0.5)