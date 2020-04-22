package takashno.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.cli.Option;

@Getter
@AllArgsConstructor
public enum Args {

    MODE("m", "mode", true, "execute mode. zip, unzip, pzip, punzip", true),

    PASSWORD("p", "password", true, "encryption password.", true),

    INPUT("i", "input", true, "input.", true),

    OUTPUT("o", "output", true, "output.", true);


    private String shortName;

    private String longName;

    private  boolean hasArgs;

    private String description;

    private boolean required;

    public Option option() {
        return new Option(this.shortName,this.longName,this.hasArgs,this.description);
    }

}
