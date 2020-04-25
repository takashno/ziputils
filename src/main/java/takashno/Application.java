package takashno;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import takashno.bean.Distribution;
import takashno.bean.ExecuteOption;
import takashno.exception.UnZipFailureException;
import takashno.exception.ZipFailureException;
import takashno.process.UnZipProcess;
import takashno.process.ZipProcess;
import takashno.type.Args;
import takashno.type.ExitCode;
import takashno.type.Mode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Application {

    private static final Options OPS = new Options();

    private static final Pattern DISTRIBUTION_FILE_PATTERN = Pattern.compile("^([^,]+),([^,]+),([^,]+)$");

    static {
        Stream.of(Args.values()).forEach(x -> OPS.addOption(x.option()));
    }

    public static void main(String[] args) {

        var parser = new DefaultParser();
        var helpFormatter = new HelpFormatter();

        try {
            var cmd = parser.parse(OPS, args);

            // Argument Required Check.
            if (Stream.of(Args.values()).filter(Args::isRequired).anyMatch(x -> !cmd.hasOption(x.getShortName()))) {
                System.out.println("Argument Error Occurred...");
                helpFormatter.printHelp("ziputils -m [MODE] -i [INPUT] [OPTION]...", OPS);
                System.out.println("failure.");
                System.exit(ExitCode.ERROR.getValue());
            }

            // Resolve execute mode.
            Mode mode = null;
            try {
                mode = Mode.valueOf(cmd.getOptionValue(Args.MODE.getShortName()).toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Argument Error Occurred... [MODE] value is invalid : "
                        + cmd.getOptionValue(Args.MODE.getShortName()));
                helpFormatter.printHelp("ziputils -m [MODE] -i [INPUT] [OPTION]...", OPS);
                System.out.println("failure.");
                System.exit(ExitCode.ERROR.getValue());
            }

            // Set model from CommandLine.
            var executeOption = ExecuteOption.builder()
                    .mode(mode)
                    .input(cmd.getOptionValue(Args.INPUT.getShortName()))
                    .output(cmd.getOptionValue(Args.OUTPUT.getShortName()))
                    .password(cmd.getOptionValue(Args.PASSWORD.getShortName()))
                    .distributionFile(cmd.getOptionValue(Args.DISTRIBUTION_FILE.getShortName()))
                    .recursive(cmd.hasOption(Args.RECURSIVE.getShortName()))
                    .fileNameCharset(cmd.getOptionValue(Args.FILENAME_CHARSET.getShortName()))
                    .verbose(cmd.hasOption(Args.VERBOSE.getShortName()))
                    .build();

            // Resolve distribution config file.
            if (executeOption.getDistributionFile() != null) {
                executeOption.setDistributions(new ArrayList<>());
                var distributionFile = Paths.get(executeOption.getDistributionFile());
                if (Files.notExists(distributionFile)) {
                    throw new RuntimeException("not exists distribution file : " + distributionFile.toAbsolutePath().toString());
                }
                try {
                    var counter = new AtomicInteger();
                    Files.readAllLines(distributionFile).stream().forEach(x -> {
                        var lineMatcher = DISTRIBUTION_FILE_PATTERN.matcher(x);
                        if (lineMatcher.find()) {
                            executeOption.getDistributions()
                                    .add(new Distribution(lineMatcher.group(1),
                                            Pattern.compile(lineMatcher.group(1)),
                                            lineMatcher.group(2),
                                            lineMatcher.group(3)));
                        } else {
                            throw new RuntimeException(
                                    "distribution file is not valid format at line:" + counter.addAndGet(1));
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(
                            "can't open distribution file : " + distributionFile.toAbsolutePath().toString(), e);
                }
            }

            // Resolve output root directory.
            if (executeOption.getOutput() == null) {
                System.out.println("output root directory is designation.use '" + System.getProperty("user.dir") + File.separator + "dist' instead.");
                executeOption.setOutput(System.getProperty("user.dir") + File.separator + "dist");
            }

            // Call Process.
            try {
                if (mode == Mode.ZIP) {
                    new ZipProcess().accept(executeOption);
                } else if (mode == Mode.UNZIP) {
                    new UnZipProcess().accept(executeOption);
                }
            } catch (UnZipFailureException uzfe) {
                System.out.println("***********************************************");
                System.out.println("unzip process error occurred...");
                System.out.println("***********************************************");
                System.out.println("unzip failure files is this.");
                uzfe.getFailures().forEach(x->System.out.println(x.getPath().toAbsolutePath().toString()));
                System.out.println("failure.");
                System.exit(ExitCode.ERROR.getValue());
            } catch (ZipFailureException zfe) {
                System.out.println("***********************************************");
                System.out.println("zip process error occurred...");
                System.out.println("***********************************************");
                System.out.println("unzip failure files is this.");
                zfe.getFailures().forEach(x->System.out.println(x.getPath().toAbsolutePath().toString()));
                System.out.println("failure.");
                System.exit(ExitCode.ERROR.getValue());
            } catch (Exception e) {
                System.out.println("***********************************************");
                System.out.println("process error occurred...");
                System.out.println("***********************************************");
                e.printStackTrace();
                System.out.println("failure.");
                System.exit(ExitCode.ERROR.getValue());
            }

            System.out.println("success.");
            System.exit(ExitCode.NORMAL.getValue());

        } catch (ParseException e) {
            System.out.println("argument error occurred...");
            helpFormatter.printHelp("ziputils -m [MODE] -i [INPUT] [OPTION]...", OPS);
            System.out.println("failure.");
            System.exit(ExitCode.ERROR.getValue());
        }

    }
}
