package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Circle extends Ellipse
{
	// Variables
	private int radius = 0;

	// Constructor
	public Circle(Color color, Point center, int radius)
	{
		super(color, center, radius, radius);
		this.radius = radius;

	}

	// Methods
	public int getRadius()
	{
		return this.radius;
	}

	public void setRadius(int radius)
	{
		this.radius = radius;
		super.setHalfHeight(radius);
		super.setHalfWidth(radius);
	}

	@Override
	public String toString()
	{
		return "Circle [radius=" + radius + "]";
	}

}
