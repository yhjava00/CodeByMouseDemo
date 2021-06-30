package vo;

public class Morpheme {
	public final double id;
	public final String text;
	public final String type;
	
    public Morpheme (double id, String text, String type) {
        this.id = id;
    	this.text = text;
        this.type = type;
    }
    
    @Override
    public String toString() {
    	return text;
    }
}
