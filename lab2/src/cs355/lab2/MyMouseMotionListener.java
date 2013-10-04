
package cs355.lab2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import cs355.lab2.Controller.MouseButtonState;

public class MyMouseMotionListener implements MouseMotionListener
{
	// Variables

	// Singleton Stuff
	private static MyMouseMotionListener instance = null;

	// Singleton Method
	public static MyMouseMotionListener inst()
	{
		if(instance == null)
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
		if(Controller.inst().getMouseButtonState() == MouseButtonState.LEFT)
		{
			switch(me.getButton())
			{
			case 0:
				Controller.inst().processDragged(me.getPoint());
				break;

			case 1:
				break;
			case 2:
				break;

			case 3:
				break;

			}
		}

	}

	@Override
	public void mouseMoved(MouseEvent me)
	{
		// Not implemented
	}

}
