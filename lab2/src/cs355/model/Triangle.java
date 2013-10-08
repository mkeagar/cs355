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
		double x = (a.x + b.x + c.x) / 3.0;
		double y = (a.y + b.y + c.y) / 3.0;
		this.setOffset(new Point((int) x, (int) y));
		this.pointA = new Point(a.x - this.getOffset().x, a.y - this.getOffset().y);
		this.pointB = new Point(b.x - this.getOffset().x, b.y - this.getOffset().y);
		this.pointC = new Point(c.x - this.getOffset().x, c.y - this.getOffset().y);
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
		this.pointA = new Point(pointA.x - this.getOffset().x, pointA.y - this.getOffset().y);
	}

	public void setPointB(Point pointB)
	{
		this.pointB = new Point(pointB.x - this.getOffset().x, pointB.y - this.getOffset().y);
	}

	public void setPointC(Point pointC)
	{
		this.pointC = new Point(pointC.x - this.getOffset().x, pointC.y - this.getOffset().y);
	}

	@Override
	public String toString()
	{
		return "Triangle [pointA=" + pointA + ", pointB=" + pointB + ", pointC=" + pointC + "]";
	}

}
