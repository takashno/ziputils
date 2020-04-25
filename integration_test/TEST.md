# テスト

Unitテストが組めていないので、手動で実施した内容を記載。

## ZIP圧縮

1. 試験手順
   1. integration_test/zipinput に圧縮対象のファイルを用意。
   1. integration_test/distribution.csv に振り分け定義ファイルを用意。
   1. ツール実行引数は以下
   ```
   -m zip 
   -i ./integration_test/zipinput 
   -o ./integration_test/zipoutput
   -p password 
   -r 
   -v 
   -d ./integration_test/distribution.csv
   ```
1. 確認手順
   1. ./integration_test/zipoutput/hoge 配下にZIPファイルがパスワード付きで保存されていること
   1. ./integration_test/zipoutput/fuga 配下にZIPファイルがパスワード付きでファイル名が文字化けせず保存されていること
   1. ./integration_test/zipoutput/piyo/piyo 配下にZIPファイルがパスワード付きで保存されていること


## ZIP解凍

1. 試験手順
   1. integration_test/zipoutput に圧縮対象のファイルを用意。（ZIP圧縮テストで作成したものを利用）
   1. integration_test/distribution.csv に振り分け定義ファイルを用意。
   1. ツール実行引数は以下
   ```
   -m unzip 
   -i ./integration_test/zipoutput 
   -o ./integration_test/unzipoutput
    -p password   
   -r 
   -v 
   -d ./integration_test/distribution.csv
   ```
1. 確認手順
   1. ./integration_test/unzipoutput/hoge 配下にZIPを解凍したファイルが保存されていること
   1. ./integration_test/unzipoutput/fuga 配下にZIPを解凍したファイルがファイル名が文字化けせず保存されていること
   1. ./integration_test/unzipoutput/piyo/piyo 配下にZIPを解凍したファイルが保存されていること

