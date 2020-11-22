package com.example.n8engine.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.Set;
import java.util.regex.Pattern;

public class SemanticDocumentsCrawler extends WebCrawler {

    private final static Pattern ONLY
            = Pattern.compile(".*(\\.(rdf|owl|ntriples|n3|ttl))$");

    private final CrawlerStatistics stats;

    public SemanticDocumentsCrawler(CrawlerStatistics stats) {
        this.stats = stats;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL().toLowerCase();
        return ONLY.matcher(urlString).matches()
                && urlString.startsWith("http://dbpedia.org/");
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        stats.incrementProcessedPageCount();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String title = htmlParseData.getTitle();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            stats.incrementTotalLinksCount(links.size());

            System.out.printf("Page with title '%s' %n", title);
            System.out.printf("Text length: %d %n", text.length());
            System.out.printf("HTML length: %d %n", html.length());
            System.out.printf("%d outbound links %n", links.size());
        }
    }
}
