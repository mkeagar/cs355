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
	public Triangle(Color col, Point a, Point b, Point c)
	{
		super(col);
		this.pointA = a;
		this.pointB = b;
		this.pointC = c;
	}
	
	// Methods
	public Point getPointA()
	{
		return this.pointA;
	}

	public void setPointA(Point pointA)
	{
		this.pointA = pointA;
	}

	public Point getPointB()
	{
		return this.pointB;
	}

	public void setPointB(Point pointB)
	{
		this.pointB = pointB;
	}

	public Point getPointC()
	{
		return this.pointC;
	}

	public void setPointC(Point pointC)
	{
		this.pointC = pointC;
	}	
}
