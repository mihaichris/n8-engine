package com.example.n8engine.fuseki;

import com.example.n8engine.service.JenaService;
import org.apache.jena.fuseki.FusekiException;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.query.Dataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class InitializingFusekiServer {

    private static final Logger LOG  = LoggerFactory.getLogger(InitializingFusekiServer.class);
    private Environment environment;
    private JenaService jenaService;

    public InitializingFusekiServer(Environment environment, JenaService jenaService) {
        this.environment = environment;
        this.jenaService = jenaService;
    }

    @EventListener
    public void onApplicationStart(ContextRefreshedEvent event) {
        LOG.info("Starting Fuseki server using dataset: " + this.jenaService.getDataset());
        try {
            startFusekiServer();
        } catch (FusekiException fusekiException) {
            LOG.error("Error starting Fuseki server: " + fusekiException.getMessage());
        }
    }

    private void startFusekiServer() {
        Dataset dataset = this.jenaService.getDataset();
        FusekiServer server = FusekiServer.create()
                .add("/dataset", dataset)
                .build() ;
        server.start() ;
    }
}
