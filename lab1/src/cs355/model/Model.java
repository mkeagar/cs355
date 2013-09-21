package cs355.model;

import java.util.ArrayList;

public class Model
{
	// Variables
	private ArrayList<Shape> shapes = new ArrayList<Shape>();	
	
	// Singleton Stuff
	private static Model instance = null;
	
	// Singleton Method
	public static Model inst()
	{
		if (instance == null)
		{
			instance = new Model();
		}
		return instance;
	}
	
	// Constructor
	protected Model()
	{
		// Exists only to defeat instantiation
	}
	
	// Methods	
	public int addShape(Shape shape)
	{
		this.shapes.add(shape);
		return this.shapes.indexOf(shape);
	}
	
	public Shape getShape(int index)
	{
		return this.shapes.get(index);
	}
	
	public int size()
	{
		return this.shapes.size();
	}
}
