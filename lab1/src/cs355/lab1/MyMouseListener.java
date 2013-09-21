package cs355.lab1;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyMouseListener implements MouseListener
{
	// Variables

	// Singleton Stuff
	private static MyMouseListener instance = null;
	
	// Singleton Method
	public static MyMouseListener inst()
	{
		if (instance == null)
		{
			instance = new MyMouseListener();
		}
		return instance;
	}
	
	// Constructor
	protected MyMouseListener()
	{
		// Exists only to defeat instantiation
	}
	
	// Methods
	@Override
	public void mouseClicked(MouseEvent me)
	{
		Controller.inst().processClicked(me.getPoint());
	}

	@Override
	public void mouseEntered(MouseEvent me)
	{

	}

	@Override
	public void mouseExited(MouseEvent me)
	{

	}

	@Override
	public void mousePressed(MouseEvent me)
	{
		Controller.inst().processPressed(me.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent me)
	{
		Controller.inst().processReleased(me.getPoint());
	}

}
