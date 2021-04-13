package com.example.n8engine.indexer;

import com.example.n8engine.domain.Resource;
import com.example.n8engine.repository.ResourceRepository;
import com.example.n8engine.searcher.Searcher;
import com.example.n8engine.semantic.analyzer.SemanticAutosuggestionAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.DC;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
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
    private IndexWriter autocompleteDirectoryWriter;

    public IndexCommands(Environment environment, Searcher searcher, ResourceRepository resourceRepository) {
        this.environment = environment;
        this.resourceRepository = resourceRepository;
        this.searcher = searcher;
        String autosuggestionIndexPath = environment.getProperty("lucene.suggestion.index.path");
        try {
            Directory autosuggestionDirectory = FSDirectory.open(Path.of(autosuggestionIndexPath));
            this.autocompleteDirectoryWriter = new IndexWriter(autosuggestionDirectory, new IndexWriterConfig(new SemanticAutosuggestionAnalyzer()));
        } catch (IOException exception) {
            log.info("Error reading index:" + exception.getMessage());
        }

    }

    @ShellMethod("Index content from a source.")
    public Integer index(@ShellOption() String source) {
        log.info("Indexing " + source + " content started.");
        try {
            Dataset ds = this.searcher.getDataset();
            loadData(ds, source);
            loadSuggests(source);
            saveIFNotExists(source);
        } catch (Exception exception) {
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
        for (Resource resource : resourceList) {
            try {
                log.info("Indexing: " + resource.getUri());
                loadData(ds, resource.getUri());
                loadSuggests(resource.getUri());
                saveIFNotExists(resource.getUri());
            } catch (Exception exception) {
                log.error("Operation failed for resource: " + resource + ":" + exception.toString());
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
        for (Path path : txtFiles) {
            try {
                loadData(ds, path.getFileName().toString());
                log.info("Indexed: " + path.getFileName().toString());
            } catch (Exception exception) {
                log.error("Operation failed for resource: " + path.getFileName().toString() + ":" + exception.toString());
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

    private void loadSuggests(String source) {
        try {
            Model model = ModelFactory.createDefaultModel();
            model.read(source);
            NodeIterator nodeIterator = model.listObjectsOfProperty(DC.description);
            while (nodeIterator.hasNext()) {
                RDFNode r = nodeIterator.nextNode();
                String description = r.asLiteral().getString();
                Document document = new Document();
                document.add(new TextField("description", description, Field.Store.YES));
                autocompleteDirectoryWriter.addDocument(document);
                autocompleteDirectoryWriter.commit();
            }
        } catch (IOException exception) {
            log.info("Error reading index:" + exception.getMessage());
        }

    }
}
