package takashno.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FailureFile {

    private Path path;

    private Exception e;
}
