# Introduction #

Apache Nutch ( http://nutch.apache.org/ ) はオープンソースの Web 検索エンジンです。「検索エンジン(Lucene) ＋全文検索(Solr) ＋ Web クローラー ＋ スコアリング(Page Rank) ＋ プラグイン可能な分散実行の仕組み(Hadoop)」という構成になっています。

Apache の TLP(トップレベルプロジェクト) であり、最新バージョンは v1.2 です(2010/12 現在)。

Nutch は言語識別プラグインを持っており、それを使うことでクロールした web ページの言語をインデックスに追加、検索時に言語によるしぼり込みを有効にすることが出来ます。

しかし、Nutch 標準の言語判定プラグインは１４言語(デンマーク語、ドイツ語、ギリシャ語、英語、スペイン語、フィンランド語、フランス語、ハンガリー語、イタリア語、オランダ語、ポーランド語、ポルトガル語、ロシア語、スウェーデン語)しか対応しておらず、特にアジア系の言語については未サポートであるため、日本語などで利用する場合は判定部分をつぶす(常に日本語と返すようプラグインを書き換え＆ビルド)などの対応が行われている現状です。

また、識別に用いている言語ごとのプロファイルが jar ファイルの中に同梱されてしまっており、対応言語の追加や削除が容易には出来ないという難点もあります。

そこで、本言語判定ライブラリを利用した Apache Nutch 用の言語判定プラグインを用意しました。


# 言語判定プラグイン for Apache Nutch #

言語判定プラグインは言語判定ライブラリのパッケージに同梱されています。
利用するための手順は以下の通りです

  * 1. 言語判定ライブラリのパッケージを <a href='http://code.google.com/p/language-detection/downloads/list'>Download list</a> からダウンロードします。
  * 2. パッケージを展開し、以下のファイルを適当なディレクトリに設置します。
    * lib/`*`
    * profiles/`*`
    * nutch-plugin/`*`
  * 3. パッケージの nutch-plugin/ 以下のファイルを、あらかじめインストール済みの Apache Nutch の language-identifier プラグインのディレクトリ $NUTCH\_HOME/plugins/language-identifier にコピーします。plugin.xml は上書きしますので、必要ならデフォルトのファイルはバックアップしてください。
```
cp nutch-plugin/* $NUTCH_HOME/plugins/language-identifier
```
  * 4. Nutch の設定ファイル $NUTCH\_HOME/conf/nutch-site.xml を編集し、言語判定プラグインを有効化します。 langdetect.profile.dir には言語プロファイル profiles/ のディレクトリパスを設定してください。
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