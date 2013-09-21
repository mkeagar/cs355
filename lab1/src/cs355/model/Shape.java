package cs355.model;

import java.awt.Color;

public abstract class Shape
{
	//Variables
	private Color color;
	
	//Constructor
	public Shape (Color col)
	{
		this.color = col;	
	}
	
	//Methods
	public Color getColor()
	{
		return this.color;
	}
	
	public void setColor(Color col)
	{
		this.color = col;
	}
	
}
