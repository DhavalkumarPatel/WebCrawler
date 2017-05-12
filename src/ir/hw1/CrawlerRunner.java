package ir.hw1;

public class CrawlerRunner 
{
	public static void main(String[] args) 
	{
		try
		{
			// Task 1-E (Do not Store the documents in Document Data Store)
			Crawler.breadthFirstCrawling("https://en.wikipedia.org/wiki/Sustainable_energy", 1000, 5, null, "output/Task 1-E.txt", null);
			
			//Task 1-E (Store the documents in Document Data Store)
			//Crawler.breadthFirstCrawling("https://en.wikipedia.org/wiki/Sustainable_energy", 1000, 5, null, "output/Task 1-E.txt", "output/DocumentDataStore/");
			
			// Task 2-A
			Crawler.breadthFirstCrawling("https://en.wikipedia.org/wiki/Sustainable_energy", 1000, 5, "solar", "output/Task 2-A.txt", null);
			
			// Task 2-B
			Crawler.depthFirstCrawling("https://en.wikipedia.org/wiki/Sustainable_energy", 1000, 5, "solar", "output/Task 2-B.txt", null);
			
			// Task 3
			Crawler.breadthFirstCrawling("https://en.wikipedia.org/wiki/Solar_power", 1000, 5, null, "output/Task 3.txt", null);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
