package takashno.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class Distribution {

    private String fileNamePattern;

    private Pattern pattern;

    private String zipPath;

    private String unzipPath;

}
