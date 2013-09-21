package cs355.lab1;

import java.awt.Graphics2D;

import cs355.ViewRefresher;
import cs355.model.*;

public class View implements ViewRefresher
{
	// Variables
	
	// Singleton Stuff
	private static View instance = null;
	
	// Singleton Method
	public static View inst()
	{
		if (instance == null)
		{
			instance = new View();
		}
		return instance;
	}
	
	// Constructor
	protected View()
	{
		// Exists only to defeat instantiation
	}

	@Override
	public void refreshView(Graphics2D g2d)
	{
		for (int i = 0; i < Model.inst().size(); i++)
		{	
			Shape currentShape = Model.inst().getShape(i);
			
			if (currentShape instanceof Line)
			{
				Line temp = (Line) currentShape;
				g2d.setColor(temp.getColor());
				g2d.drawLine(temp.getStart().x, temp.getStart().y, temp.getEnd().x, temp.getEnd().y);
			}
		}
	}

}
