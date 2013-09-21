package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Ellipse extends Shape
{
	//Variables
	private Point center = null;
	private int height = 0;
	private int width = 0;
	
	//Constructor
	public Ellipse(Color col, Point c, int h, int w)
	{
		super(col);
		this.center = c;
		this.height = h;
		this.width = w;		
	}

	public Point getCenter()
	{
		return this.center;
	}

	public void setCenter(Point c)
	{
		this.center = c;
	}

	public int getHeight()
	{
		return this.height;
	}

	public void setHeight(int h)
	{
		this.height = h;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setWidth(int w)
	{
		this.width = w;
	}
	
	

}
