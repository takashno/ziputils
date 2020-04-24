package takashno.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.cli.Option;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Args {

    MODE("m", "mode", true, "*required* execute mode. zip or unzip", true),

    RECURSIVE("r", "recursive", false, "enable search file recursive  in the input directory.", false),

    PASSWORD("p", "password", true, "encryption password.", false),

    INPUT("i", "input", true, "*required* process target root directory.", true),

    OUTPUT("o", "output", true, "output target root directory. if nothing this, use 'execute directory/dist'.", false),

    DISTRIBUTION_FILE("d", "distribution", true, "directory containing the target file.", false),

    FILENAME_CHARSET("c", "charset", true, "file name charset.", false),

    VERBOSE("v", "verbose", false, "verbose output.", false),

    HELP("h", "help", false, "print help.", false);

    private String shortName;

    private String longName;

    private boolean hasArgs;

    private String description;

    private boolean required;

    public Option option() {
        return new Option(this.shortName, this.longName, this.hasArgs, this.description);
    }

}
