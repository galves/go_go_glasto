import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Created by gavin on 16/09/2014.
 */
public class GlastoModule extends AbstractModule {

    public void configure() {

        install(new FactoryModuleBuilder().implement(RequestService.class, ApacheHttpClientRequestService.class)
                .build(RequestService.Factory.class));
    }
}
