package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Line extends Shape
{
	// Variables
	private Point start = null;
	private Point end = null;
	
	// Constructor
	public Line(Color col, Point s, Point e)
	{
		super(col);
		this.start = s;
		this.end = e;
	}

	// Methods
	public Point getStart()
	{
		return this.start;
	}

	public void setStart(Point s)
	{
		this.start = s;
	}

	public Point getEnd()
	{
		return this.end;
	}

	public void setEnd(Point e)
	{
		this.end = e;
	}
	
}
