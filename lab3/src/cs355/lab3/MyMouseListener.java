
package cs355.lab3;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import cs355.lab3.Controller.MouseButtonState;

public class MyMouseListener implements MouseListener
{
	// Variables

	// Singleton Stuff
	private static MyMouseListener instance = null;

	// Singleton Method
	public static MyMouseListener inst()
	{
		if(instance == null)
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
		// System.out.println(me.getButton());
		switch(me.getButton())
		{
		case 0:
			Controller.inst().setMouseButtonState(MouseButtonState.NONE);
			break;

		case 1:
			Controller.inst().setMouseButtonState(MouseButtonState.LEFT);
			Controller.inst().processClicked(me.getPoint());
			break;

		case 2:
			break;

		case 3:
			break;
		}
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
		// System.out.println(me.getButton());
		switch(me.getButton())
		{
		case 0:
			Controller.inst().setMouseButtonState(MouseButtonState.NONE);
			break;

		case 1:
			Controller.inst().setMouseButtonState(MouseButtonState.LEFT);
			Controller.inst().processPressed(me.getPoint());
			break;

		case 2:
			;
			break;

		case 3:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent me)
	{
		// System.out.println(me.getButton());
		switch(me.getButton())
		{
		case 0:
			break;

		case 1:
			Controller.inst().setMouseButtonState(MouseButtonState.NONE);
			Controller.inst().processReleased(me.getPoint());
			break;

		case 2:
			break;

		case 3:
			break;
		}
	}

}
