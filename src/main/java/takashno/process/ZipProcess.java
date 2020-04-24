package takashno.process;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import takashno.bean.ExecuteOption;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class ZipProcess implements Consumer<ExecuteOption> {

    @Override
    public void accept(ExecuteOption option) {

        ZipParameters zipParameters = new ZipParameters();
        if (option.getPassword() != null) {
            zipParameters.setEncryptFiles(true);
        }
        zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);

        try {
            var inputRootDir = Paths.get(option.getInput());
            var outputRootDir = Paths.get(option.getOutput());
            Files.walk(inputRootDir).forEach(x -> {
                if (!Files.isDirectory(x)) {
                    var targetFileName = x.getFileName().toString();
                    // 出力ファイル名
                    var oFileName = targetFileName.substring(0, targetFileName.indexOf(".")) + ".zip";
                    // 対象ファイルの親ディレクトリ
                    var iParent = x.getParent().toAbsolutePath().toString();
                    // 出力ファイルの親ディレクトリ
                    var oParent = iParent.replace(
                            inputRootDir.toAbsolutePath().toString(),
                            outputRootDir.toAbsolutePath().toString());
                    if (option.getDistributions() != null) {
                        // 整列対象のファイルであるか確認
                        var distribution =
                                option.getDistributions().stream()
                                        .filter(y -> y.getPattern().asMatchPredicate().test(targetFileName))
                                        .findFirst();
                        // 整列対象のファイルであれば出力先を調整
                        if (distribution.isPresent()) {
                            oParent = outputRootDir.toAbsolutePath().toString()
                                    + File.separator
                                    + distribution.get().getZipPath();
                        }
                    }
                    // 出力ファイル
                    var oFile = Paths.get(oParent, oFileName);

                    try {
                        // 出力ファイルの親ディレクトリを作成する
                        if (Files.notExists(oFile.getParent())) {
                            Files.createDirectories(oFile.getParent());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (option.isVerbose()) {
                        System.out.println("from : " + x.toAbsolutePath().toString() + ", out : " + oFile.toAbsolutePath().toString());
                    }

                    var zipFile = new ZipFile(oFile.toAbsolutePath().toFile(),
                            option.getPassword() != null ? option.getPassword().toCharArray() : null);
                    if (option.getFileNameCharset() != null) {
                        zipFile.setCharset(Charset.forName(option.getFileNameCharset()));
                    }

                    try {
                        zipFile.addFile(x.toString(), zipParameters);
                    } catch (ZipException e) {
                        throw new RuntimeException(e);
                    }
                }

            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
