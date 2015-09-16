package hw3;

public class Frontier
{
    private String url;
    private int inLinkCount;

    /**
     * Constructors.
     */
    public Frontier()
    {
        url = "";
        setInLinkCount(0);
    }

    public Frontier(String url, int inLinkCount)
    {
        this.url = url;
        this.setInLinkCount(inLinkCount);
    }

    public String getUrl()
    {
        return url;
    }

	public int getInLinkCount() {
		return inLinkCount;
	}

	public void setInLinkCount(int inLinkCount) {
		this.inLinkCount = inLinkCount;
	}

}