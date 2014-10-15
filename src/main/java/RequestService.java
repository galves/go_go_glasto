import com.google.inject.ImplementedBy;
import org.apache.commons.cli.CommandLine;

/**
 * Created by gavin on 15/09/2014.
 */
@ImplementedBy(ApacheHttpClientRequestService.class)
public interface RequestService {

    interface Factory {
            RequestService create(CommandLine opts);
    }

    public GlastoResponse execute();
}
