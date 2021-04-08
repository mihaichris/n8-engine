package com.example.n8engine.indexer;

import com.example.n8engine.domain.Resource;
import com.example.n8engine.repository.ResourceRepository;
import com.example.n8engine.searcher.Searcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.assembler.exceptions.AssemblerException;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.core.env.Environment;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ShellComponent
public class IndexCommands {
    private final Environment environment;
    private Searcher searcher;
    private ResourceRepository resourceRepository;

    public IndexCommands(Environment environment, Searcher searcher, ResourceRepository resourceRepository) {
        this.environment = environment;
        this.resourceRepository = resourceRepository;
        this.searcher = searcher;
    }

    @ShellMethod("Index content from a source.")
    public Integer index(@ShellOption() String source) {
        log.info("Indexing " + source + " content started.");
        try {
            Dataset ds = this.searcher.getDataset();
            loadData(ds, source);
            saveIFNotExists(source);
        } catch (AssemblerException exception) {
            log.error("Operation failed: " + exception.getMessage());
            return 1;
        }
        return 0;
    }

    @ShellMethod("Index content from resource table.")
    public Integer indexFromDb() {
        log.info("Indexing all from resource table.");
        List<Resource> resourceList = resourceRepository.findAll();
        Dataset ds = this.searcher.getDataset();
        for (Resource resource: resourceList) {
            try {
                loadData(ds, resource.getUri());
                saveIFNotExists(resource.getUri());
                log.info("Indexed: " + resource.getUri());
            } catch (Exception exception) {
                log.error("Operation failed for resource: " + resource +  ":" + exception.toString());
            }
        }
        return 0;
    }

    @ShellMethod("Index content from resource files.")
    public Integer indexFromFiles() throws IOException {
        log.info("Indexing all from resource files.");
        List<Path> txtFiles = Files.walk(Paths.get("E:\\Java Projects\\n8-engine\\src\\main\\resources\\data"))
                //use to string here, otherwise checking for path segments
                .filter(p -> p.toString().endsWith(".nt"))
                .collect(Collectors.toList());
        Dataset ds = this.searcher.getDataset();
        for (Path path: txtFiles) {
            try {
                loadData(ds, path.getFileName().toString());
                log.info("Indexed: " + path.getFileName().toString());
            } catch (Exception exception) {
                log.error("Operation failed for resource: " + path.getFileName().toString() +  ":" + exception.toString());
            }
        }
        return 0;
    }

    private static void loadData(Dataset dataset, String file) {
        log.info("Start loading data");
        long startTime = System.nanoTime();
        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();
            RDFDataMgr.read(model, file);
            dataset.commit();
        } finally {
            dataset.end();
        }
        long finishTime = System.nanoTime();
        double time = (finishTime - startTime) / 1.0e6;
        log.info(String.format("Finish loading - %.2fms", time));
    }

    private void saveIFNotExists(String source) {
        Optional<Resource> resource = resourceRepository.findByUri(source);
        if (resource.isEmpty()) {
            Resource newResource = new Resource();
            newResource.setUri(source);
            resourceRepository.save(newResource);
        }
    }
}
