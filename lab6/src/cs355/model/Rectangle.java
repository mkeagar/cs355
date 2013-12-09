package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Rectangle extends Shape
{
	// Variables
	// private Point upperLeftCorner = null;
	private int halfHeight = 0;
	private int halfWidth = 0;

	// private int height = 0;
	// private int width = 0;

	// Constructor
	public Rectangle(Color col, Point p, int hh, int hw)
	{
		super(col);
		// this.upperLeftCorner = p;
		super.setOffset(p);
		this.halfHeight = hh;
		this.halfWidth = hw;
	}

	// Methods

	public Point getUpperLeftCorner()
	{
		return new Point(-this.halfWidth, -this.halfHeight);
	}

	public int getHalfWidth()
	{
		return this.halfWidth;
	}

	public void setHalfWidth(int hw)
	{
		this.halfWidth = hw;
	}

	public int getWidth()
	{
		return (this.halfWidth * 2);
	}

	public void setWidth(int w)
	{
		this.halfWidth = (w / 2);
	}

	public int getHalfHeight()
	{
		return this.halfHeight;
	}

	public void setHalfHeight(int hh)
	{
		this.halfHeight = hh;
	}

	public int getHeight()
	{
		return (this.halfHeight * 2);
	}

	public void setHeight(int h)
	{
		this.halfHeight = (h / 2);
	}

	// public void setUpperLeftCorner(Point p)
	// {
	// // this.upperLeftCorner = p;
	// }

	@Override
	public String toString()
	{
		return "Rectangle [halfheight=" + halfHeight + ", halfwidth=" + halfWidth + "]";
	}

}
