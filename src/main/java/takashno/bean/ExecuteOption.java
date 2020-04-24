package takashno.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import takashno.type.Mode;

import java.util.List;

@Getter
@Setter
@Builder
public class ExecuteOption {

    @NonNull
    private String input;

    private String output;

    @NonNull
    private Mode mode;

    private boolean recursive;

    private String password;

    private String distributionFile;

    private String fileNameCharset;

    private boolean verbose;

    private List<Distribution> distributions;
}
