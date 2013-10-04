
package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Rectangle extends Shape
{
	// Variables
	private Point upperLeftCorner = null;
	private int height = 0;
	private int width = 0;

	// Constructor
	public Rectangle(Color col, Point p, int h, int w)
	{
		super(col);
		this.upperLeftCorner = p;
		this.height = h;
		this.width = w;
	}

	public int getHeight()
	{
		return this.height;
	}

	// Methods
	public Point getUpperLeftCorner()
	{
		return this.upperLeftCorner;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setHeight(int h)
	{
		this.height = h;
	}

	public void setUpperLeftCorner(Point p)
	{
		this.upperLeftCorner = p;
	}

	public void setWidth(int w)
	{
		this.width = w;
	}

	@Override
	public String toString()
	{
		return "Rectangle [upperLeftCorner=" + upperLeftCorner + ", height=" + height + ", width=" + width + "]";
	}
}
