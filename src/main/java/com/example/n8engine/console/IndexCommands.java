package com.example.n8engine.console;

import org.apache.jena.assembler.exceptions.AssemblerException;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

@ShellComponent
public class IndexCommands {
    private static final Logger logger = LoggerFactory.getLogger(IndexCommands.class);
    private final Environment environment;

    public IndexCommands(Environment environment) {
        this.environment = environment;
    }

    @ShellMethod("Create assembler file for RDF.")
    public Integer index() {
        logger.info("Started creating Lucene index and TDB.");
        URL assemblerResource = getClass().getClassLoader().getResource("assemble/config/assembler.ttl");
        URL rdfDataResource = getClass().getClassLoader().getResource("assemble/config/data.ttl");
        String assemblerFileRelativePath = "";
        String rdfDataFileRelativePath = "";
        try {
            assert assemblerResource != null;
            File assemblerFile = Paths.get(assemblerResource.toURI()).toFile();
            assert rdfDataResource != null;
            File rdfDataFile = Paths.get(rdfDataResource.toURI()).toFile();
            assemblerFileRelativePath = assemblerFile.getPath();
            rdfDataFileRelativePath = rdfDataFile.getPath();
        } catch (URISyntaxException exception) {
            logger.error("Operation failed: " + exception.getMessage());
            return 1;
        }
        String dataset = environment.getProperty("jena.resource.dataset");
        String namespace = environment.getProperty("jena.resource.namespace");
        assert namespace != null;
        assert dataset != null;
        String resourceUri = new String(namespace).concat("/#").concat(dataset);
        try {
            Dataset ds = DatasetFactory.assemble(assemblerFileRelativePath, resourceUri);
            loadData(ds, rdfDataFileRelativePath);
        } catch (AssemblerException exception) {
            logger.error("Operation failed: " + exception.getMessage());
            return 1;
        }
        return 0;
    }

    private static void loadData(Dataset dataset, String file) {
        logger.info("Start loading data");
        long startTime = System.nanoTime();
        dataset.begin(ReadWrite.WRITE);
        try {
            Model m = dataset.getDefaultModel();
            RDFDataMgr.read(m, file);
            dataset.commit();
        } finally {
            dataset.end();
        }
        long finishTime = System.nanoTime();
        double time = (finishTime - startTime) / 1.0e6;
        logger.info(String.format("Finish loading - %.2fms", time));
    }
}
