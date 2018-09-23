package org.barrak.immocrawler.batch.crawler.impl.immoregion;

import org.barrak.crawler.database.document.SearchResultDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

public class ImmoRegionArticleCrawler {

    private SearchResultDocument searchResult;

    public ImmoRegionArticleCrawler(String url) {
        Element page = getPage(url);

        parsePage(page);
    }

    private Element getPage(String url) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            File fileXML = new File("test.xml");
            Document xml = builder.parse(fileXML);

            return xml.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parsePage(Element page) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            String description = xPath.evaluate("//div[contains(@class,'description')]/div/div/div/p/text()", page);

            System.out.println("description : " + description);
        } catch (XPathExpressionException ex) {

        }
    }

    public SearchResultDocument getSearchResult() {
        return searchResult;
    }
}
