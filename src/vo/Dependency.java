package vo;

import java.util.List;

public class Dependency {
	public final double id;
	public final String text;
	public final double head;
	public final String label;
	public final List<Double> mod;
	
	public Dependency(double id, String text, double head, String label, List<Double> mod) {
		this.id = id;
		this.text = text;
		this.head = head;
		this.label = label;
		this.mod = mod;
	}
	
}
