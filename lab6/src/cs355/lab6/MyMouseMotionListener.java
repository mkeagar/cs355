package cs355.lab6;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import cs355.lab6.Controller.MouseButtonState;

public class MyMouseMotionListener implements MouseMotionListener
{
	// Variables

	// Singleton Stuff
	private static MyMouseMotionListener instance = null;

	// Singleton Method
	public static MyMouseMotionListener inst()
	{
		if (instance == null)
		{
			instance = new MyMouseMotionListener();
		}
		return instance;
	}

	// Constructor
	protected MyMouseMotionListener()
	{
		// Exists only to defeat instantiation
	}

	// Methods

	@Override
	public void mouseDragged(MouseEvent me)
	{
		// System.out.println(me.getButton());
		if (Controller.inst().getMouseButtonState() == MouseButtonState.LEFT)
		{
			Controller.inst().processDragged(me.getPoint());
		}

	}

	@Override
	public void mouseMoved(MouseEvent me)
	{
		// Not implemented
	}

}
