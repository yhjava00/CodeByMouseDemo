package vo;

public class KeyWord {
	
	public final String text;
	public final String info;
	public final int priority;
	
	public KeyWord(String text, int priority) {
		this.text = text;
		this.priority = priority;
		this.info = "";
	}
	
	public KeyWord(String text, String info,  int priority) {
		this.text = text;
		this.info = info;
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		return text;
	}

}
