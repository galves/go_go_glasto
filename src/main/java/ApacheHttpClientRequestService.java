import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.apache.commons.cli.CommandLine;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;

/**
 * Created by gavin on 16/09/2014.
 */
public class ApacheHttpClientRequestService implements RequestService {

    private CookieStore cookieStore;
    HttpClient httpClient;
    HttpGet httpGet;

    int connectionTimeout;
    int replyTimeout;

    String url;
    String userAgent;

    @Inject
    public ApacheHttpClientRequestService(@Assisted CommandLine commandLine){

        cookieStore = new BasicCookieStore();
        userAgent = commandLine.getOptionValue("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");
        url = commandLine.getOptionValue("url");
        connectionTimeout = Integer.valueOf(commandLine.getOptionValue("time-out", "1000"));
        replyTimeout = Integer.valueOf(commandLine.getOptionValue("response-time-out", "1000"));

        httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", userAgent);

        RequestConfig conf = RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(replyTimeout)
                //.setSocketTimeout(connectionTimeout)
                .build();

        httpClient = HttpClientBuilder
                .create().setDefaultRequestConfig(conf)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultCookieStore(cookieStore)
                .build();
    }

    public GlastoResponse execute() {

        Long start = System.currentTimeMillis();

        HttpEntity httpEntity;
        String res = "";

        try {
            HttpResponse response = httpClient.execute(httpGet);
            httpEntity = response.getEntity();
            res = EntityUtils.toString(httpEntity);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Status Code " + statusCode + " received in " + (System.currentTimeMillis() - start) + "ms");

            // System.out.println(res);

        } catch (Exception e) {
            System.out.println("Aborted after " + (System.currentTimeMillis() - start) + "ms. " + e.getMessage());
        }

        return new GlastoResponse(res, cookieStore.getCookies());

    }
}
