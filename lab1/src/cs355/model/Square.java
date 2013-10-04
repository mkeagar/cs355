
package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Square extends Rectangle
{
	// Variables
	private int sideLength = 0;

	// Constructor
	public Square(Color col, Point p, int s)
	{
		super(col, p, s, s);
		this.sideLength = s;
	}

	// Methods
	public int getSideLength()
	{
		return this.sideLength;
	}

	public void setSideLength(int s)
	{
		this.sideLength = s;
		super.setHeight(this.sideLength);
		super.setWidth(this.sideLength);
	}

	@Override
	public String toString()
	{
		return "Square [sideLength=" + sideLength + "]";
	}
}
