package ir.hw1;

public class URL 
{
	private String url;
	private int depth;
	
	URL(String url, int depth)
	{
		this.url = url;
		this.depth = depth;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
}
