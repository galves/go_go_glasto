import org.apache.commons.cli.CommandLine;
import org.apache.http.cookie.Cookie;

import java.util.List;

public class GlastoResponse {

    public String body;
    public List<Cookie> cookies;
    public int statusCode;

    public GlastoResponse(String body, List<Cookie> cookies) {
        this.body = body;
        this.cookies = cookies;
    }

    public boolean bypassedHoldingPage(CommandLine options)
    {
        if (body.equals(""))
        {
           return false;
        }

        String expectedText = options.getOptionValue("expected-text");

        String holdingText = options.getOptionValue("holding-text");

        if (holdingText != null && !this.body.contains(holdingText)){

            System.out.println("\n[\"" + holdingText +  "\" not found on page!]\n");
            printBookmarkletHelperText();
            return true;
        }
        else if (expectedText != null && this.body.toLowerCase().contains(expectedText.toLowerCase())){

            System.out.println("\n[\"" + expectedText +  "\" found on page!]\n");
            printBookmarkletHelperText();
            return true;
        }
        else {

          return false;
        }
    }

    public void printBookmarkletHelperText(){

        System.out.println("Paste the javascript line below into your address bar to copy the cookies:\n");

        String c = "";
        for (Cookie cookie : cookies) {
            c += "document.cookie=\"";
            c += cookie.getName() + "=" + cookie.getValue();
            // TODO support cookie expiry, path, domain.
            c += "\";";
        }

        System.out.println("javascript:(function(){" + c + "alert(\"cookie set!\")})()");

        System.out.println("\n\nDebug:" + cookies.toString() + "\n\n");
    }

}
