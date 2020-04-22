package takashno;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UtilZip {

    public static void main(String[] args) {

        var inputDir = "";
        var outputDir = "";
        var password = "";


        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);

        try {
            Files.list(Paths.get(inputDir)).forEach(x -> {
                try {
                    var fileName = x.getFileName().toString().substring(0,x.getFileName().toString().indexOf("."))+ ".zip";
                    var zipFile = new ZipFile(Paths.get(outputDir, fileName).toString(), password.toCharArray());
                    zipFile.setCharset(Charset.forName("windows-31j"));
                    zipFile.addFile(x.toString(), zipParameters);
                } catch (ZipException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
