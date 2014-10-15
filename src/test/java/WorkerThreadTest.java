import org.apache.commons.cli.CommandLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkerThreadTest {

    @Mock
    RequestService requestService;

    @Mock
    CommandLine commandLine;

    @InjectMocks
    WorkerThread workerThread;

    @Test
    public void testLoadPage_shouldReturnFalse_WhenHoldingPageIsFound() throws Exception {

        GlastoResponse stubHoldingPage = new GlastoResponse("holding", null);

        when(requestService.execute())
                .thenReturn(stubHoldingPage);

        assertFalse(workerThread.loadPage());

    }
}