package com.example.n8engine.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.File;

@ShellComponent
public class CrawlCommand {

    public CrawlCommand() {
    }


    @ShellMethod("Crawl semantic web documents and index them in TDB store.")
    public void crawl() throws Exception {
        File crawlStorage = new File("src/resources/crawler");
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorage.getAbsolutePath());
        config.setMaxDepthOfCrawling(2);

        int numCrawlers = 12;

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        controller.addSeed("http://dbpedia.org/");

        CrawlerStatistics stats = new CrawlerStatistics();
        CrawlController.WebCrawlerFactory<SemanticDocumentsCrawler> factory = () -> new SemanticDocumentsCrawler(stats);

        controller.start(factory, numCrawlers);
        System.out.printf("Crawled %d pages %n", stats.getProcessedPageCount());
        System.out.printf("Total Number of outbound links = %d %n", stats.getTotalLinksCount());
    }
}
