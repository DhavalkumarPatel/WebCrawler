package ir.hw1;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler 
{
	/**
	 * Web crawling using BFS approach.
	 * Crawling will be done from lower depth (shallower) URLs to higher depth (deeper) URLS 
	 * and from top to bottom for same depth URLs
	 * @param seedURL
	 * @param maxUrlToCrawl
	 * @param maxDepth
	 * @param keyword - null for non focused crawling and keyword for focused crawling
	 * @param outputFileLocation - location for output file to store unique URLs, in case of null it will write output to default location
	 * @param documentDataStoreLocation - location for storing documents, in case of null it will not store the documents
	 * @return set of crawled URLs
	 * @throws Exception
	 */
	public static Set<String> breadthFirstCrawling(String seedURL, int maxUrlToCrawl, int maxDepth, String keyword, String outputFileLocation, String documentDataStoreLocation) throws Exception
	{
		// Set for maintaining crawled URLs for uniqueness purpose and Queue for BFS approach (FIFO)
		Set<String> crawledURLSet = new LinkedHashSet<String>();
		Queue<URL> toCrawlURLQueue = new LinkedList<URL>();
		
		// Start crawling from the seed URL by adding it to queue
		toCrawlURLQueue.add(new URL(seedURL, 1));
			
		// Open the output file to write the crawled URLs
		outputFileLocation = null != outputFileLocation ? outputFileLocation : "output.txt";
		PrintWriter writeCrawledURLs = new PrintWriter(new FileOutputStream(outputFileLocation), true);
		
		// Iterate until we are done with all the URLs from web or we reaches to max URLs to crawl
		while(!toCrawlURLQueue.isEmpty() && crawledURLSet.size() < maxUrlToCrawl)
		{
			//take the First Inserted URL from Queue
			URL urlObj = toCrawlURLQueue.poll();
			
			// If the URL is not already crawled than crawl it
			if(null != urlObj.getUrl() && !crawledURLSet.contains(urlObj.getUrl()))
			{
				// Crawl the URL
				Document htmlDoc = crawl(urlObj.getUrl());
				if(null != htmlDoc)
				{
					// Get the redirected URL from canonical tags and check for duplication
					String redirectedURL = getRedirectedURLFromCanonicalTags(htmlDoc, urlObj.getUrl());
					if(crawledURLSet.contains(redirectedURL))
					{
						continue;
					}
					urlObj.setUrl(redirectedURL);
					
					// Add this unique new URL to crawledURLSet and write it into output file
					crawledURLSet.add(urlObj.getUrl());
					writeCrawledURLs.println(urlObj.getUrl());
					
					// Store the down-loaded document in document data store along with its URL and depth
					if(null != documentDataStoreLocation)
					{
						PrintWriter docWriter = new PrintWriter(new FileOutputStream(documentDataStoreLocation + "Document-" + crawledURLSet.size()), true);
						docWriter.println("URL : " + urlObj.getUrl());
						docWriter.println("Depth : " + urlObj.getDepth());
						docWriter.write(htmlDoc.html());
						docWriter.close();
					}

					// if depth of the crawled URL is lower than max depth than collect all the valid links from 
					// the document and enqueue them into queue (if they are already not crawled). 
					// i.e. Top URL of the page will be enqueued first and bottom URL will be enqueued last.
					if(urlObj.getDepth() < maxDepth)
					{
						List<String> linksInDoc = collectLinks(htmlDoc, keyword);
						for(String link : linksInDoc)
						{
							if(!crawledURLSet.contains(link))
							{
								toCrawlURLQueue.add(new URL(link, urlObj.getDepth() + 1));
							}
						}
					}
				}
			}
		}
		
		// close the file and return crawled URL set
		writeCrawledURLs.close();
		return crawledURLSet;
	}
	
	/**
	 * Web crawling using DFS approach.
	 * Crawling will be done by going to the maximum depth of the first URL of the seed page
	 * and than the second URL of the seed page will be crawled in the same fashion. 
	 * For the same depth, it traverses from top to bottom manner.
	 * @param seedURL
	 * @param maxUrlToCrawl
	 * @param maxDepth
	 * @param keyword - null for non focused crawling and keyword for focused crawling
	 * @param outputFileLocation - location for output file to store unique URLs, in case of null it will write output to default location
	 * @param documentDataStoreLocation - location for storing documents, in case of null it will not store the documents
	 * @return set of crawled URLs
	 * @throws Exception
	 */
	public static Set<String> depthFirstCrawling(String seedURL, int maxUrlToCrawl, int maxDepth, String keyword, String outputFileLocation, String documentDataStoreLocation) throws Exception
	{
		// Set for maintaining crawled URLs for uniqueness purpose and Stack for DFS approach (LIFO)
		Set<String> crawledURLSet = new LinkedHashSet<String>();
		Stack<URL> toCrawlURLStack = new Stack<URL>();
		
		// Start crawling from the seed URL by adding it to queue
		toCrawlURLStack.add(new URL(seedURL, 1));
		
		// Open the output file to write the crawled URLs
		outputFileLocation = null != outputFileLocation ? outputFileLocation : "output.txt";
		PrintWriter writeCrawledURLs = new PrintWriter(new FileOutputStream(outputFileLocation), true);
			
		// Iterate until we are done with all the URLs from web or we reaches to max URLs to crawl
		while(!toCrawlURLStack.isEmpty() && crawledURLSet.size() < maxUrlToCrawl)
		{
			//take the Last Inserted URL from Stack
			URL urlObj = toCrawlURLStack.pop();
			
			// If the URL is not already crawled than crawl it
			if(null != urlObj.getUrl() && !crawledURLSet.contains(urlObj.getUrl()))
			{
				// Crawl the URL
				Document htmlDoc = crawl(urlObj.getUrl());
				if(null != htmlDoc)
				{
					// Get the redirected URL from canonical tags and check for duplication
					String redirectedURL = getRedirectedURLFromCanonicalTags(htmlDoc, urlObj.getUrl());
					if(crawledURLSet.contains(redirectedURL))
					{
						continue;
					}
					urlObj.setUrl(redirectedURL);
					
					// Add this unique new URL to crawledURLSet and write it into output file
					crawledURLSet.add(urlObj.getUrl());
					writeCrawledURLs.println(urlObj.getUrl());
					
					// Store the down-loaded document in document data store along with its URL and depth
					if(null != documentDataStoreLocation)
					{
						PrintWriter docWriter = new PrintWriter(new FileOutputStream(documentDataStoreLocation + "Document-" + crawledURLSet.size()), true);
						docWriter.println("URL : " + urlObj.getUrl());
						docWriter.println("Depth : " + urlObj.getDepth());
						docWriter.write(htmlDoc.html());
						docWriter.close();
					}
					
					// if depth of the crawled URL is lower than max depth than collect all the valid links from 
					// the document and push them into stack (if they are already not crawled) in reverse order. 
					// i.e. bottom URL of the page will be pushed first and top URL will be pushed last.
					if(urlObj.getDepth() < maxDepth)
					{
						List<String> linksInDoc = collectLinks(htmlDoc, keyword);
						for(int i = linksInDoc.size() - 1; i >= 0; i--)
						{
							String link = linksInDoc.get(i);
							if(!crawledURLSet.contains(link))
							{
								toCrawlURLStack.push(new URL(link, urlObj.getDepth() + 1));
							}
						}
					}
				}
			}
		}
		
		// close the file and return crawled URL set
		writeCrawledURLs.close();
		return crawledURLSet;
	}
	
	/**
	 * crawl method connects to the given url using Jsoup API and if the downloaded document
	 * from web is of HTML type then returns document else returns null. 
	 * @param url 
	 * @return document from web
	 */
	private static Document crawl(String url)
	{
		Document doc = null;
		try
        {	
			// Delay of 1 second to respect the politeness policy 
			Thread.sleep(1000);
			
			// Connects to the given url (decode if encoded) and download the document
            Connection connection = Jsoup.connect(URLDecoder.decode(url, "UTF-8")).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
            doc = connection.get();
            
            // set document to null if content type is not text/html
            if(!connection.response().contentType().contains("text/html"))
            {
            	doc = null;
            }
        }
        catch(Exception e)
        {
        	System.out.println("Error while connecting to URL (" + url + ") : ");
        	e.printStackTrace();
        }
		return doc;
	}
	
	/**
	 * This method collects all the links which follows our crawling restrictions from 
	 * the given document, if the keyword is passed then it will perform focused search
	 * and if keyword is null then it will perform normal search. It searches for links 
	 * in top to bottom fashion.
	 * @param document
	 * @param keyword (null or real keyword of focused crawling)
	 * @return list of valid links present in document 
	 */
	private static List<String> collectLinks(Document document, String keyword)
	{
		List<String> linksInDoc = new ArrayList<String>();
		
		if(null != document)
		{
			Elements linkElementsInDoc = document.select("a[href]");
			
			// To remove all the other links (not in paragraph tag) like side links we 
			// can select hyper links only inside paragraph tag but this gives very low 
			// count running Focused Crawling so commented below code and collected all the 
			// valid links from the document.
			// Elements linkElementsInDoc = document.select("p a[href]");
        
			for(Element link : linkElementsInDoc)
			{
				String linkURL = link.absUrl("href");
				
				if(validateLinkURL(linkURL))
				{
					// update the URL if it contains # by removing the book mark from the URL
					if(linkURL.contains("#"))
					{
						linkURL = linkURL.substring(0, linkURL.indexOf("#"));
					}
					
					// check for a keyword if its a focused crawling
					if(null != keyword)
					{
						// match a keyword against anchor text or against URL (case insensitive)
						if(StringUtils.containsIgnoreCase(link.text(), keyword) || StringUtils.containsIgnoreCase(linkURL, keyword))
						{
							linksInDoc.add(linkURL);
						}
					}
					else
					{
						linksInDoc.add(linkURL);
					}
				}
			}
        }
			
		return linksInDoc;
	}
	
	/**
	 * This method validates the given linkURL with below required conditions:
	 * 1. It should be wiki page only (starts with "https://en.wikipedia.org/wiki")
	 * 2. It should not be administrative links (containing :)
	 * 3. It should not be main Wikipedia page.
	 * @param linkURL
	 * @return
	 */
	private static boolean validateLinkURL(String linkURL)
	{
		return linkURL.startsWith("https://en.wikipedia.org/wiki") 
				&& (linkURL.length() - linkURL.replace(":", "").length() <=1) 
				&& !linkURL.equalsIgnoreCase("https://en.wikipedia.org/wiki/Main_Page");
	}
	
	/**
	 * Retrieve the redirected URL from canonical tags of head section and return it
	 * if not found then return the real URL.
	 * @param document
	 * @param realURL
	 * @return redirected URL from canonical tag if found else realURL
	 */
	private static String getRedirectedURLFromCanonicalTags(Document document, String realURL)
	{
		String redirectedURL = realURL;
		
		Elements linkElements = document.head().select("link");
    	for(Element link : linkElements)
    	{
    		if(null != link.attr("rel") && link.attr("rel").equalsIgnoreCase("canonical"))
    		{
    			redirectedURL = link.attr("href");
    		}
    	}
    	
    	return redirectedURL;
	}
}
