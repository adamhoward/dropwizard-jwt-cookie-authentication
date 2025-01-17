package org.dhatim.dropwizard.jwt.cookie.authentication;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.logging.DefaultLoggingFactory;
import io.dropwizard.server.SimpleServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TestApplication extends Application<Configuration> {

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(JwtCookieAuthBundle.getDefault());
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        ((DefaultLoggingFactory)configuration.getLoggingFactory()).setLevel("DEBUG");

        //choose a random port
        SimpleServerFactory serverConfig = new SimpleServerFactory();
        configuration.setServerFactory(serverConfig);
        HttpConnectorFactory connectorConfig = (HttpConnectorFactory) serverConfig.getConnector();
        connectorConfig.setPort(0);

        //Dummy health check to suppress the startup warning.
        environment.healthChecks().register("dummy", new HealthCheck() {
            @Override
            protected HealthCheck.Result check() {
                return HealthCheck.Result.healthy();
            }
        });

        environment.jersey().register(new TestResource());
        System.out.println("*** started");
    }

}