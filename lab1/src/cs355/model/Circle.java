
package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Circle extends Ellipse
{
	// Variables
	private int radius = 0;

	// Constructor
	public Circle(Color color, Point center, int diameter)
	{
		super(color, center, diameter, diameter);
		this.radius = diameter / 2;
	}

	// Methods
	public int getRadius()
	{
		return this.radius;
	}

	public void setRadius(int radius)
	{
		this.radius = radius;
		super.setHeight(2 * radius);
		super.setWidth(2 * radius);
	}
}
