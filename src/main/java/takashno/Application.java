package takashno;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import takashno.type.Args;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Application {


    private static final Options OPS = new Options();
    static {
        Stream.of(Args.values()).forEach(x->OPS.addOption(x.option()));
    }

    public static void main(String[] args) {

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;


    }
}
