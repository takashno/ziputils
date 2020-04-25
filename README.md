# ziputils

Java製のZIPファイルに関する便利な機能を提供するツールです。  
[zip4j](https://github.com/srikanth-lingala/zip4j) を利用しています。

## 機能一覧

- 指定ディレクトリ配下のファイルを全てZIP化する（ZIPファイル以外の全てのファイルを対象とする）
- 指定ディレクトリ配下のZIPファイルを全て解凍する(ZIPファイルのみを対象とする)
- パスワードによる暗号化に対応(圧縮・解凍)
- ファイルの出力ディレクトリを指定可能
- ファイルの出力時に出力ディレクトリの中のどのパスに配置するか指定可能

### ユースケース

- あるディレクトリに配置されたファイルを一律暗号化をかけてZIP化したい
- あるディレクトリに配置されたファイルを一律暗号化をかけてZIP化し、ファイル名によって特定ディレクトリに振り分けたい

----

## 実行方法

本ツールはJava11以降に対応している。  
Jarからの実行方法は、Javaコマンドによる実行を行う。

```
jar -jar ziputils.jar [OPTIONS]
```

[こちら](https://github.com/takashno/ziputils/releases/tag/0.0.1) に多少使いやすいように、実行するシェルなどのスクリプトを用意しています。  
Jarから実行したくない場合は参照ください。

#### オプション

|オプション名|指定文字|必須|値の指定有|指定内容|
|:---|:---|:---:|:---:|:---|
|mode|m|○|○|zip,unzipのいずれかを指定する。
|input|i|○|○|対象とするファイルが格納されているディレクトリ。
|output|o| |○|処理後のファイルを格納したいディレクトリ。
|recursive|r| | |指定したディレクトリを再帰的に探索するかの指定。
|password|p| |○|暗号化パスワード。
|distribution|d| |○|ディレクトリ振り分け設定ファイルのパスを指定。
|charset|ｃ| |○|ファイル名にマルチバイトを含む場合、文字コードを指定。
|verbose|v| | |詳細な出力を行いたい場合に指定。
|help|h| | |ヘルプを出力したい場合に指定。

#### 振り分けファイルについて

振り分け設定ファイルは特殊な要件であるため、オリジナルのCSVを用意する必要がある。  
CSVは以下の形式・フォーマットで記載する

|形式|内容|
|:---|:---|
|ヘッダ|なし
|囲い文字|なし
|区切り文字|なし
|文字コード|UTF-8
|改行コード|LF

|列|内容|
|:---|:---|
|ファイル名のパターン|対象としたいファイル名の [正規表現パターン](https://docs.oracle.com/javase/jp/11/docs/api/java.base/java/util/regex/Pattern.html) を指定。
|ZIP圧縮時の振り分けディレクトリ|指定したパターンに合致したファイルを振り分けるディレクトリ。<br>出力ルートディレクトリ（outputオプション）以降のパスを指定する。
|ZIP解凍時の振り分けディレクトリ|指定したパターンに合致したファイルを振り分けるディレクトリ。<br>出力ルートディレクトリ（outputオプション）以降のパスを指定する。


##### 記載例

```csv
hoge.txt,hoge,hoge
fuga[0-9]{2}.csv,fuga/zipout/nest/path,/fuga/unzipout/nest/path
[0-9]{6}_.+,piyo,piyo
```

#### オプションを指定した実行例

- ZIP圧縮（ディレクトリ再帰）
```
java -jar ziputils.jar -m zip -r -p password -i /input/dir -o /output/dir
```

- パスワード付きZIP圧縮（ディレクトリ再帰）
```
java -jar ziputils.jar -m zip -r -p password -i /input/dir -o /output/dir
```

- ZIP解凍（ディレクトリ再帰）
```
java -jar ziputils.jar -m unzip -r -p password -i /input/dir -o /output/dir
```

- パスワード付きZIP解凍（ディレクトリ再帰）
```
java -jar ziputils.jar -m unzip -r -p password -i /input/dir -o /output/dir
```