package cs355.model;

import java.awt.Color;
import java.awt.Point;

public class Square extends Rectangle
{
	// Variables
	private int halfSize = 0;

	// Constructor
	public Square(Color col, Point p, int hs)
	{
		super(col, p, hs, hs);
		this.halfSize = hs;
	}

	// Methods
	public int getHalfSize()
	{
		return this.halfSize;
	}

	public void setHalfSize(int hs)
	{
		this.halfSize = hs;
		super.setHalfHeight(this.halfSize);
		super.setHalfWidth(this.halfSize);
	}

	public int getSize()
	{
		return (this.halfSize * 2);
	}

	@Override
	public String toString()
	{
		return "Square [halfSize=" + halfSize + "]";
	}
}
