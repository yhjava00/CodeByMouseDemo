package vo;

public class Morpheme {
	public final double id;
	public final String text;
	public final String type;
	public final double position;
	
    public Morpheme (double id, String text, String type, double position) {
        this.id = id;
    	this.text = text;
        this.type = type;
        this.position = position;
    }
    
}
