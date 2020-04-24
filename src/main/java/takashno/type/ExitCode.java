package takashno.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExitCode {

    NORMAL(0),
    ERROR(1),
    WARNING(2);

    private int value;
}
