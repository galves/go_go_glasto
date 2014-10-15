import com.google.inject.Inject;

import java.lang.Thread;
import java.util.concurrent.Callable;

import com.google.inject.assistedinject.Assisted;
import org.apache.commons.cli.*;

public class WorkerThread implements Callable<WorkerThread> {

    @Inject
    private CommandLine commandLine;

    @Inject
    private RequestService requestService;

    @Inject
    public WorkerThread(@Assisted CommandLine commandLine, @Assisted RequestService requestService) {
        this.commandLine = commandLine;
        this.requestService = requestService;
    }

    @Override
    public WorkerThread call() throws Exception {

        while(!Thread.currentThread().isInterrupted()) {
            try{
                if (loadPage()) {
                    Thread.currentThread().interrupt();
                }
            }
            catch (InterruptedException e){
                //System.out.println("Thread " + Thread.currentThread().getId() + " killed because signup was found");
                break;
            }
        }

        return this;
    }

    public boolean loadPage() throws Exception {

        return requestService.execute().bypassedHoldingPage(this.commandLine);
    }
}
