package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Ellipse extends Shape
{
	// Variables
	private int halfHeight = 0;
	private int halfWidth = 0;

	// Constructor
	public Ellipse(Color color, Point p, int hh, int hw)
	{
		super(color);
		super.setOffset(p);
		this.halfHeight = hh;
		this.halfWidth = hw;
	}

	// Methods

	public Point getUpperLeftCorner()
	{
		return new Point(-this.halfWidth, -this.halfHeight);
	}

	public int getHalfHeight()
	{
		return this.halfHeight;
	}

	public int getHeight()
	{
		return (this.halfHeight * 2);
	}

	public void setHalfHeight(int hh)
	{
		this.halfHeight = hh;
	}

	public int getHalfWidth()
	{
		return this.halfWidth;
	}

	public int getWidth()
	{
		return (this.halfWidth * 2);
	}

	public void setHalfWidth(int hw)
	{
		this.halfWidth = hw;
	}

	@Override
	public String toString()
	{
		return "Ellipse [height=" + halfHeight + ", width=" + halfWidth + "]";
	}

}
