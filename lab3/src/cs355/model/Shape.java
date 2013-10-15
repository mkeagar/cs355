package cs355.model;

import java.awt.Color;
import java.awt.Point;

public abstract class Shape
{
	// Variables
	private Color color;	// R, G, B, and Alpha channels
	private double theta;	// in Radians
	private Point offset;	// location in world coordinates

	// Constructor
	public Shape(Color col)
	{
		this.color = col;
		this.theta = 0.0d;
		this.offset = new Point();
	}

	// Methods
	public Color getColor()
	{
		return this.color;
	}

	public void setColor(Color col)
	{
		this.color = col;
	}

	public double getRotation()
	{
		return this.theta;
	}

	public void setRotation(double theta)
	{

		this.theta = theta % (2 * Math.PI);
	}

	public Point getOffset()
	{
		return this.offset;
	}

	public int getXOffset()
	{
		return this.offset.x;
	}

	public int getYOffset()
	{
		return this.offset.y;
	}

	public void setOffset(Point offset)
	{
		this.offset = offset;
	}

	public void setXOffset(int x)
	{
		this.offset.x = x;
	}

	public void setYOffset(int y)
	{
		this.offset.y = y;
	}

}
