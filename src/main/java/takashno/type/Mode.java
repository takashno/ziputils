package takashno.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Mode {

    ZIP("zip"),
    UNZIP("unzip");

    private String name;

}
