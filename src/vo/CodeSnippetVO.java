package vo;

public class CodeSnippetVO {
	
	public final String text;
	public final Object info;
	public final int priority;
	
	public CodeSnippetVO(String text, Object info, int priority) {
		this.text = text;
		this.info = info;
		this.priority = priority;
	}
}
