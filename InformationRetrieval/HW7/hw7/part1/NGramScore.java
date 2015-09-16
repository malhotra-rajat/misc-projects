package hw7.part1;

public class NGramScore {

	private String nGram;
	private float score;
	
	public NGramScore(String nGram, float score) {
		super();
		this.nGram = nGram;
		this.score = score;
	}

	public String getnGram() {
		return nGram;
	}

	public void setnGram(String nGram) {
		this.nGram = nGram;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nGram == null) ? 0 : nGram.hashCode());
		result = prime * result + Float.floatToIntBits(score);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NGramScore other = (NGramScore) obj;
		if (nGram == null) {
			if (other.nGram != null)
				return false;
		} else if (!nGram.equals(other.nGram))
			return false;
		if (Float.floatToIntBits(score) != Float.floatToIntBits(other.score))
			return false;
		return true;
	}
	
	
	
	
}
