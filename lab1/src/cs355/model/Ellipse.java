
package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Ellipse extends Shape
{
	// Variables
	private Point center = null;
	private int height = 0;
	private int width = 0;

	// Constructor
	public Ellipse(Color color, Point center, int height, int width)
	{
		super(color);
		this.center = center;
		this.height = height;
		this.width = width;
	}

	public Point getCenter()
	{
		return this.center;
	}

	public int getHeight()
	{
		return this.height;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setCenter(Point center)
	{
		this.center = center;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

}
