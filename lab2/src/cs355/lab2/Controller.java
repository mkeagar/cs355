
package cs355.lab2;

import java.awt.Color;
import java.awt.Point;

import cs355.CS355Controller;
import cs355.GUIFunctions;
import cs355.model.*;

public class Controller implements CS355Controller
{
	// Variables
	private enum ControllerState
	{
		TRIANGLE, SQUARE, RECTANGLE, CIRCLE, ELLIPSE, LINE, SELECT, ZOOM_IN, ZOOM_OUT, H_SCROLL_BAR, V_SCROLL_BAR, FREE
	};

	public enum MouseButtonState
	{
		NONE, LEFT, MIDDLE, RIGHT
	};

	private MouseButtonState mouseButtonState = MouseButtonState.NONE;
	private ControllerState currentState = ControllerState.FREE;
	private Color currentColor = Color.white;
	private Shape currentShape = null;
	private Point startPoint = null;
	private int triCornCount = 0;
	private Point[] triCornArray = new Point[3];

	// Singleton Stuff
	private static Controller instance = null;

	// Singleton Methods
	public static Controller inst()
	{
		if(instance == null)
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

	public void setMouseButtonState(MouseButtonState mbs)
	{
		this.mouseButtonState = mbs;
	}

	public MouseButtonState getMouseButtonState()
	{
		return this.mouseButtonState;
	}

	@Override
	public void circleButtonHit()
	{
		this.resetState();
		this.currentState = ControllerState.CIRCLE;
	}

	@Override
	public void colorButtonHit(Color color)
	{
		// this.currentState = controllerState.Color;
		if(color != null)
		{
			this.currentColor = color;
			GUIFunctions.changeSelectedColor(this.currentColor);
		}
	}

	@Override
	public void ellipseButtonHit()
	{
		this.resetState();
		this.currentState = ControllerState.ELLIPSE;
	}

	public Shape getCurrentShape()
	{
		return this.currentShape;
	}

	public ControllerState getCurrentState()
	{
		return this.currentState;
	}

	@Override
	public void hScrollbarChanged(int value)
	{
		// this.resetState();
		// this.currentState = ControllerState.H_SCROLL_BAR;
	}

	@Override
	public void lineButtonHit()
	{
		this.resetState();
		this.currentState = ControllerState.LINE;
	}

	public void processClicked(Point p)
	{
		this.startPoint = p;

		switch(this.currentState)
		{
		case TRIANGLE:
			this.triCornArray[this.triCornCount] = p;
			this.triCornCount++;
			if(this.triCornCount == 3)
			{
				this.triCornCount = 0;
				this.currentShape = new Triangle(this.currentColor, this.triCornArray[0], this.triCornArray[1], this.triCornArray[2]);
				Model.inst().addShape(this.currentShape);
				this.currentShape = null;
			}
			break;

		case SQUARE:
			break;

		case RECTANGLE:
			break;

		case CIRCLE:
			break;

		case ELLIPSE:
			break;

		case LINE:
			break;

		case SELECT:
			break;

		case FREE:
			break;

		default:

		}

		GUIFunctions.refresh();
	}

	public void processDragged(Point p)
	{
		switch(this.currentState)
		{
		case TRIANGLE:
			break;

		case SQUARE:
			this.updateSquare(p);
			break;

		case RECTANGLE:
			updateRectangle(p);
			break;

		case CIRCLE:
			this.updateCircle(p);
			break;

		case ELLIPSE:
			this.updateEllipse(p);
			break;

		case LINE:
			((Line) this.currentShape).setEnd(p);

			break;

		case SELECT:
			break;

		case FREE:
			break;

		default:

		}

		GUIFunctions.refresh();
	}

	public void updateCircle(Point p)
	{
		int deltaY = Math.abs(this.startPoint.y - p.y);
		int deltaX = Math.abs(this.startPoint.x - p.x);

		Circle circle = (Circle) currentShape;

		int diameter = (deltaX > deltaY) ? deltaY : deltaX;
		int radius = diameter / 2;

		if(this.startPoint.x > p.x)
			if(this.startPoint.y > p.y) // new point is to the upper left of start point
				circle.setCenter(new Point(this.startPoint.x - radius, this.startPoint.y - radius));
			else
				// new point is to the lower left of start point
				circle.setCenter(new Point(this.startPoint.x - radius, this.startPoint.y + radius));

		else
		{
			if(this.startPoint.y > p.y) // new point is to upper right of start point
				circle.setCenter(new Point(this.startPoint.x + radius, this.startPoint.y - radius));
			else
				circle.setCenter(new Point(this.startPoint.x + radius, this.startPoint.y + radius));
		}

		circle.setRadius(radius);

	}

	public void updateEllipse(Point p)
	{
		int height = Math.abs(this.startPoint.y - p.y);
		int width = Math.abs(this.startPoint.x - p.x);
		Ellipse ellipse = (Ellipse) currentShape;
		ellipse.setHeight(height);
		ellipse.setWidth(width);

		if(this.startPoint.x > p.x)
			if(this.startPoint.y > p.y) // new point is to the upper left of start point
				ellipse.setCenter(new Point(p.x + width / 2, p.y + height / 2));
			else
				// new point is to the lower left of start point
				ellipse.setCenter(new Point(p.x + width / 2, this.startPoint.y + height / 2));

		else
		{
			if(this.startPoint.y > p.y) // new point is to upper right of start point
				ellipse.setCenter(new Point(this.startPoint.x + width / 2, p.y + height / 2));
			else
				ellipse.setCenter(new Point(this.startPoint.x + width / 2, this.startPoint.y + height / 2));
		}

	}

	public void updateRectangle(Point p)
	{
		Rectangle rectangle = (Rectangle) currentShape;
		rectangle.setHeight(Math.abs(this.startPoint.y - p.y));
		rectangle.setWidth(Math.abs(this.startPoint.x - p.x));

		if(this.startPoint.x > p.x)
			if(this.startPoint.y > p.y) // new point is to the upper left of start point
				rectangle.setUpperLeftCorner(p);
			else
				// new point is to the lower left of start point
				rectangle.setUpperLeftCorner(new Point(p.x, this.startPoint.y));

		else if(this.startPoint.y > p.y) // new point is to upper right of start point
			rectangle.setUpperLeftCorner(new Point(this.startPoint.x, p.y));

	}

	public void updateSquare(Point p)
	{
		int deltaY = Math.abs(this.startPoint.y - p.y);
		int deltaX = Math.abs(this.startPoint.x - p.x);
		int side = (deltaX > deltaY) ? deltaY : deltaX;

		Square square = (Square) currentShape;

		if(this.startPoint.x > p.x)
			if(this.startPoint.y > p.y) // new point is to the upper left of start point
				square.setUpperLeftCorner(new Point(this.startPoint.x - side, this.startPoint.y - side));
			else
				// new point is to the lower left of start point
				square.setUpperLeftCorner(new Point(this.startPoint.x - side, this.startPoint.y));
		else if(this.startPoint.y > p.y) // new point is to upper right of start point
			square.setUpperLeftCorner(new Point(this.startPoint.x, this.startPoint.y - side));

		square.setSideLength(side);
	}

	public void processPressed(Point p)
	{
		this.startPoint = p;

		switch(this.currentState)
		{
		case TRIANGLE:
			break;

		case SQUARE:
			this.currentShape = new Square(this.currentColor, p, 0);
			break;

		case RECTANGLE:
			this.currentShape = new Rectangle(this.currentColor, p, 0, 0);
			break;

		case CIRCLE:
			this.currentShape = new Circle(this.currentColor, p, 0);
			break;

		case ELLIPSE:
			this.currentShape = new Ellipse(this.currentColor, p, 0, 0);
			break;

		case LINE:
			this.currentShape = new Line(this.currentColor, p, p);
			break;

		case SELECT:
			break;

		case FREE:
			break;

		default:

		}

		if(this.currentState != ControllerState.TRIANGLE)
		{
			Model.inst().addShape(this.currentShape);
		}
		GUIFunctions.refresh();
	}

	public void processReleased(Point p)
	{
		GUIFunctions.refresh();
	}

	@Override
	public void rectangleButtonHit()
	{
		this.resetState();
		this.currentState = ControllerState.RECTANGLE;
	}

	// Methods
	private void resetState()
	{
		this.currentShape = null;
		this.currentState = ControllerState.FREE;
	}

	@Override
	public void selectButtonHit()
	{
		this.resetState();
		this.currentState = ControllerState.SELECT;
	}

	@Override
	public void squareButtonHit()
	{
		this.resetState();
		this.currentState = ControllerState.SQUARE;
	}

	@Override
	public void triangleButtonHit()
	{
		this.resetState();
		this.triCornCount = 0;
		this.triCornArray = new Point[3];
		this.currentState = ControllerState.TRIANGLE;
	}

	@Override
	public void vScrollbarChanged(int value)
	{
		// this.resetState();
		// this.currentState = ControllerState.V_SCROLL_BAR;
	}

	@Override
	public void zoomInButtonHit()
	{
		// this.resetState();
		// this.currentState = ControllerState.ZOOM_IN;
	}

	@Override
	public void zoomOutButtonHit()
	{
		// this.resetState();
		// this.currentState = ControllerState.ZOOM_OUT;
	}

}
