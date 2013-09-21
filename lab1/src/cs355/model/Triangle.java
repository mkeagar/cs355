
package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Triangle extends Shape
{
	// Variables
	private Point pointA = null;
	private Point pointB = null;
	private Point pointC = null;

	// Constructor
	public Triangle(Color color, Point a, Point b, Point c)
	{
		super(color);
		this.pointA = a;
		this.pointB = b;
		this.pointC = c;
	}

	// Methods
	public Point getPointA()
	{
		return this.pointA;
	}

	public Point getPointB()
	{
		return this.pointB;
	}

	public Point getPointC()
	{
		return this.pointC;
	}

	public void setPointA(Point pointA)
	{
		this.pointA = pointA;
	}

	public void setPointB(Point pointB)
	{
		this.pointB = pointB;
	}

	public void setPointC(Point pointC)
	{
		this.pointC = pointC;
	}
}
