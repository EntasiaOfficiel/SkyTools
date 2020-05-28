package fr.entasia.skytools.objs;

public class Color {

	public double r;
	public double g;
	public double b;


	public Color(int r, int g, int b){
		this.r = r/255f;
		this.g = g/255f;
		this.b = b/255f;
	}

	public Color(org.bukkit.Color color){
		this.r = color.getRed()/255f;
		this.g = color.getGreen()/255f;
		this.b = color.getBlue()/255f;
	}
}
