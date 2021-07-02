package vo;

public class KeyWord {
	
	public final String text;
	public final Object info;
	public final int priority;
	
	public KeyWord(String text, Object info,  int priority) {
		this.text = text;
		this.info = info;
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		return text;
	}

}
