
package cs355.lab1;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

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
		Controller.inst().processDragged(me.getPoint());
	}

	@Override
	public void mouseMoved(MouseEvent me)
	{
		// Not implemented
	}

}
