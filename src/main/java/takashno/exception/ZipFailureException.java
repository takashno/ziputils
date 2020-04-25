package takashno.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import takashno.bean.FailureFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ZipFailureException extends RuntimeException {

    private List<FailureFile> failures;

}
