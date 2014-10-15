import org.apache.commons.cli.CommandLine;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GlastoResponseTest {

    GlastoResponse glastoResponse;

    String HOLDING_TEXT = "holding";
    String EXPECTED_TEXT = "registration page";

    @Mock
    CommandLine commandLine;

    @Before
    public void setUp() throws Exception {

        glastoResponse = new GlastoResponse(null, null);
        glastoResponse.cookies = new ArrayList<Cookie>();
        glastoResponse.cookies.add(new BasicClientCookie("foo", "bar"));

        /* fake CLI options */
        when(commandLine.getOptionValue("holding-text")).thenReturn(HOLDING_TEXT);
        when(commandLine.getOptionValue("expected-text")).thenReturn(EXPECTED_TEXT);
    }

    @Test
    public void testBypassedHoldingPage_returnsTrue_WhenHoldingTextIsNotFound(){

        glastoResponse.body = "success!";
        assertTrue(glastoResponse.bypassedHoldingPage(commandLine));

    }

    @Test
    public void testBypassedHoldingPage_returnsFalse_WhenHoldingTextIsIsFound() {

        glastoResponse.body = HOLDING_TEXT;
        assertFalse(glastoResponse.bypassedHoldingPage(commandLine));
    }

    @Test
    public void testBypassedHoldingPage_returnsTrue_WhenHoldingAndExpectedTextAreFound() {

        glastoResponse.body = HOLDING_TEXT + " " + EXPECTED_TEXT;
        assertTrue(glastoResponse.bypassedHoldingPage(commandLine));
    }
}