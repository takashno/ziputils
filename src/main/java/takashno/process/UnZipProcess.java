package takashno.process;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import takashno.bean.ExecuteOption;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class UnZipProcess implements Consumer<ExecuteOption> {

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
            FileSystem fs = FileSystems.getDefault();
            PathMatcher matcher = fs.getPathMatcher("glob:**/*.zip");
            Files.walk(inputRootDir).filter(matcher::matches).forEach(x -> {
                var targetFileName = x.getFileName().toString();
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
                                + distribution.get().getUnzipPath();
                    }
                }
                try {
                    var oParentPath = Paths.get(oParent);
                    // 出力ファイルの親ディレクトリを作成する
                    if (Files.notExists(oParentPath)) {
                        Files.createDirectories(oParentPath);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (option.isVerbose()) {
                    System.out.println("from : " + x.toAbsolutePath().toString() + ", out to : " + oParent);
                }

                // UnZip
                try {
                    var zipFile = new ZipFile(x.toAbsolutePath().toFile());
                    if (zipFile.isEncrypted()) {
                        zipFile.setPassword(option.getPassword() != null ? option.getPassword().toCharArray() : null);
                    }
                    zipFile.extractAll(oParent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
