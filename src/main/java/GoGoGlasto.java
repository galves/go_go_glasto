import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.*;

import java.util.concurrent.*;

/**
 * Created by gavin on 15/09/2014.
 */
public class GoGoGlasto {

    public static void main(String[] args){

        CommandLineParser parser = new GnuParser();
        CommandLine line;

        Options options = getOptions();

        try {
            line = parser.parse(options, args);
            launchWorkers(line);
        }
        catch(ParseException e) {
            System.err.println( "Parsing failed.  Reason: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("url [options]", options);
            System.exit(1);
        }

        System.out.println("App exited normally.");
    }

    public static void launchWorkers(CommandLine opts)
    {
        Injector injector = Guice.createInjector(new GlastoModule());

        String threadsOpt = opts.getOptionValue("num-workers","1");
        int threads = Integer.parseInt(threadsOpt);

        System.out.println("Starting up " + threads + " threads.");

        ExecutorService taskExecutor = Executors.newFixedThreadPool(10);

        CompletionService<WorkerThread> completionService = new ExecutorCompletionService<WorkerThread>(taskExecutor);

        for (int i = 0; i < threads; i++) {

            // Inject ApacheHttpClientRequestService with Guice because that's what we do now.
            RequestService service = injector.getInstance(RequestService.Factory.class).create(opts);

            completionService.submit(new WorkerThread(opts, service));
        }
        try {
            int completed = 0;

            while(completed < threads - 1) {
                Future<WorkerThread> r = completionService.take();
                taskExecutor.shutdownNow();
                completed++;
            }

        } catch (InterruptedException e){
            System.out.println("interrupted by another thread");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-access")
    private static Options getOptions() {

        Options options = new Options();

        options.addOption( OptionBuilder.withLongOpt("connection-time-out")
                .withDescription( "connection must be made in MILLISECONDS (1000)")
                .hasArg()
                .withArgName("MILLISECONDS")
                .withType(Number.class)
                .create("t"));

        options.addOption( OptionBuilder.withLongOpt("response-time-out")
                .withDescription( "server must reply in MILLISECONDS (1000)")
                .hasArg()
                .withArgName("MILLISECONDS")
                .withType(Number.class)
                .create("r"));

        options.addOption( OptionBuilder.withLongOpt("expected-text")
                .withDescription( "stop if page contains TEXT. (case insensitive)")
                .hasArg()
                .withArgName("TEXT")
                .create("e"));

        options.addOption( OptionBuilder.withLongOpt("holding-text")
                .withDescription( "stop if page does not contain TEXT. (case sensitive)")
                .hasArg()
                .withArgName("TEXT")
                .create("h"));

        options.addOption( OptionBuilder.withLongOpt("user-agent")
                .hasArg()
                .withArgName("AGENT")
                .withDescription( "User agent of web browser. AGENT Should be wrapped in quotes")
                .withArgName("AGENT")
                .create("A"));

        options.addOption( OptionBuilder.withLongOpt("num-workers")
                .withDescription( "Number of worker threads")
                .hasArg()
                .withArgName("THREADS")
                .withType(Number.class)
                .create("n"));

        options.addOption( OptionBuilder.withLongOpt("url")
                .isRequired(true)
                .withDescription( "URL of the page to test")
                .hasArg()
                .withArgName("URL")
                .create());

        return options;
    }

}
