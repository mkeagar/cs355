package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Circle extends Ellipse
{
	// Variables
	private int radius = 0;
	// Constructor
	public Circle(Color col, Point c, int r)
	{
		super(col, c, r, r);
		this.radius = r;
	}
	
	// Methods
	public int getRadius()
	{
		return this.radius;
	}
	
	public void setRadius(int r)
	{
		this.radius = r;
	}
}
