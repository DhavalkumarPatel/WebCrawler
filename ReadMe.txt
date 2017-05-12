			
==============================================================================================================

The WebCrawler is build in Java and works with the help of 3 Classes:

1. CrawlerRunner:
This class is used to run the crawler. I have created four calls to the Crawler to perform all four
given task of assignment. Documentation in the class will help you if you want to change any parameters.

2. Crawler:
This class has all the crawling logic.

3. URL:
Normal java class to store URL with depth

==============================================================================================================

There 2 external libraries used for this project:

A. jsoup-1.10.2 - This is used for scraping and parsing HTML from a URL and find, extract data, using DOM traversal for URL's
B. commons-lang3-3.5 - This is for using StringUtils.containsIgnoreCase that searches the keyphrase in a URL html response. This  method is faster than conventional String.contains()



==============================================================================================================
HOW TO RUN:
==============================================================================================================

1. The WebCrawler is zipped in WebCrawler.zip. The same can imported as a project in Eclipse.( The Project name is WebCrawler)
2. The 2 libraries that are required to be imported are placed inside the lib directory inside the WebCrawler.zip
3. If there are error seen with the usage of libraries, please have the libraries added in the build path of the project by specifying the path of lib directory.
4. The Crawler can be started by running CrawlerRunner.java. I have already configured the four method calls in main method.

==============================================================================================================

The results retrieved for the Crawler are saved in output directory for reference.
