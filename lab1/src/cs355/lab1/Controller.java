package cs355.lab1;

import java.awt.Color;
import java.awt.Point;

import cs355.CS355Controller;
import cs355.GUIFunctions;
import cs355.model.*;

public class Controller implements CS355Controller
{	
	// Variables
	private enum controllerState { Color, Triangle, Square, Rectangle, Circle, Ellipse, Line, Select, ZoomIn, ZoomOut, HScrollBar, VScrollBar, Free };
	private controllerState currentState = controllerState.Free;
	private Color currentColor = Color.white;
	private Shape currentShape = null;
	
	
	// Singleton Stuff
	private static Controller instance = null;
	
	// Singleton Methods
	public static Controller inst()
	{
		if (instance == null)
		{
			instance = new Controller();
		}
		return instance;
	}
	
	// Constructor
	protected Controller()
	{
		// Exists only to defeat instantiation
	}
	
	// Methods
	private void resetState()
	{
		this.currentShape = null;
		this.currentState = null;
	}
	
	public void processClicked(Point p)
	{

	}
	
	public void processDragged(Point p)
	{
		switch (this.currentState)
		{
			case Triangle:
				break;
				
			case Square:
				break;
				
			case Rectangle:
				break;
			
			case Circle:
				break;
				
			case Ellipse:
				break;
				
			case Line:
				((Line)currentShape).setEnd(p);
				
				break;
				
			case Select:
				break;
				
			case Free:
				break;
				
			default:
			
		}
		
		Model.inst().addShape((Line)currentShape);
		GUIFunctions.refresh();
	}
	
	public void processPressed(Point p)
	{		
		switch (this.currentState)
		{
			case Triangle:
				break;
				
			case Square:
				break;
				
			case Rectangle:
				break;
			
			case Circle:
				break;
				
			case Ellipse:
				break;
				
			case Line:
				currentShape = new Line(currentColor, p, p);
				break;
				
			case Select:
				break;
				
			case Free:
				break;
				
			default:
			
		}
	}
	
	public void processReleased(Point p)
	{
		
	}
	
	@Override
	public void colorButtonHit(Color c)
	{
//		this.currentState = controllerState.Color;
		if (c != null)
		{
			this.currentColor = c;
			GUIFunctions.changeSelectedColor(this.currentColor);
		}
	}

	@Override
	public void triangleButtonHit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void squareButtonHit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void rectangleButtonHit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void circleButtonHit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void ellipseButtonHit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void lineButtonHit()
	{
		this.resetState();
		this.currentState = controllerState.Line;
		System.out.println("Current State: Line\n");
	}

	@Override
	public void selectButtonHit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void zoomInButtonHit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void zoomOutButtonHit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void hScrollbarChanged(int value)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void vScrollbarChanged(int value)
	{
		// TODO Auto-generated method stub

	}

}
