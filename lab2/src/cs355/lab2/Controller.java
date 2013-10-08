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
	private Shape selectedShape = null;
	private Point startPoint = null;
	private Point[] triCornArray = new Point[3];
	private int triCornCount = 0;

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
		if (color != null)
		{
			if (this.selectedShape != null)
			{
				this.selectedShape.setColor(color);
			}
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

		switch (this.currentState)
		{
			case TRIANGLE :
				this.triCornArray[this.triCornCount] = p;
				this.triCornCount++;
				if (this.triCornCount == 3)
				{
					this.triCornCount = 0;
					this.currentShape = new Triangle(this.currentColor, this.triCornArray[0], this.triCornArray[1], this.triCornArray[2]);
					Model.inst().addShape(this.currentShape);
					this.currentShape = null;
				}
				break;

			case SQUARE :
				break;

			case RECTANGLE :
				break;

			case CIRCLE :
				break;

			case ELLIPSE :
				break;

			case LINE :
				break;

			case SELECT :
				this.selectShape(p);
				break;

			case FREE :
				break;

			default :

		}

		GUIFunctions.refresh();
	}

	public void selectShape(Point p)
	{
		Shape temp = null;
		for (int i = Model.inst().size() - 1; i >= 0; --i)
		{
			temp = Model.inst().getShape(i);
			if (this.containsPoint(temp, p))
			{
				this.selectedShape = temp;
				break;
			}

			if (i == 0)
			{
				temp = null;
				this.selectedShape = null;
			}
		}

		if (temp != null)
		{
			this.currentColor = temp.getColor();
			GUIFunctions.changeSelectedColor(this.currentColor);
		}
	}

	public Shape getSelectedShape()
	{
		return this.selectedShape;
	}

	public boolean containsPoint(Shape shape, Point p)
	{
		boolean response = false;

		if (shape instanceof Line)
		{
			Line temp = (Line) shape;
			boolean passedDistanceTest = this.lineDistanceTest(temp, p);
			boolean passedSegmentPointTest = this.lineSegmentPointTest(temp, p);

			System.out.println("passedDistanceTest: " + passedDistanceTest);
			System.out.println("passedSegmentPointTest: " + passedSegmentPointTest);

			return (passedDistanceTest && passedSegmentPointTest);

		}
		else if (shape instanceof Rectangle)
		{
			Rectangle temp = (Rectangle) shape;
			if ((p.x < temp.getOffset().x + temp.getHalfWidth()) && (p.x > temp.getOffset().x - temp.getHalfWidth())
					&& (p.y < temp.getOffset().y + temp.getHalfHeight()) && (p.y > temp.getOffset().y - temp.getHalfHeight()))
			{
				response = true;
			}
		}
		else if (shape instanceof Ellipse)
		{
			Ellipse temp = (Ellipse) shape;

			if ((Math.abs(p.x - temp.getOffset().x) <= temp.getHalfWidth()) && (Math.abs(p.y - temp.getOffset().y) <= temp.getHalfHeight()))
				if (Math.pow((p.x - temp.getOffset().x) / (double) temp.getHalfWidth(), 2)
						+ Math.pow((p.y - temp.getOffset().y) / (double) temp.getHalfHeight(), 2) <= 1)
				{
					response = true;
				}
		}
		else if (shape instanceof Triangle)
		{
			Triangle temp = (Triangle) shape;
			Point convertedPoint = new Point(p.x - temp.getOffset().x, p.y - temp.getOffset().y);

			double inAB = ((convertedPoint.x - temp.getPointA().x) * (temp.getPointB().y - temp.getPointA().y) - (convertedPoint.y - temp.getPointA().y)
					* (temp.getPointB().x - temp.getPointA().x));
			double inBC = ((convertedPoint.x - temp.getPointB().x) * (temp.getPointC().y - temp.getPointB().y) - (convertedPoint.y - temp.getPointB().y)
					* (temp.getPointC().x - temp.getPointB().x));
			double inCA = ((convertedPoint.x - temp.getPointC().x) * (temp.getPointA().y - temp.getPointC().y) - (convertedPoint.y - temp.getPointC().y)
					* (temp.getPointA().x - temp.getPointC().x));

			if (((inAB >= 0) && (inBC >= 0) && (inCA >= 0)) || ((inAB <= 0) && (inBC <= 0) && (inCA <= 0)))
			{
				response = true;
			}

		}

		return response;
	}
	public boolean lineSegmentPointTest(Line line, Point p)
	{
		boolean response = false;
		double vX = p.x - line.getStart().x;
		double vY = p.y - line.getStart().y;
		double abSqrt = Math.sqrt(Math.pow((line.getEnd().x - line.getStart().x), 2) + Math.pow((line.getEnd().y - line.getStart().y), 2));
		double dHatX = (line.getEnd().x - line.getStart().x) / abSqrt;
		double dHatY = (line.getEnd().y - line.getStart().y) / abSqrt;
		double d = ((vX * dHatX) + (vY * dHatY)) / abSqrt;

		System.out.println("abSqrt was: " + abSqrt);
		System.out.println("d was: " + d);

		if ((0 <= d) && (d <= abSqrt))
		{
			response = true;
			System.out.println("Passed line segment Point test?");
		}

		return response;
	}

	public boolean lineDistanceTest(Line line, Point p)
	{
		boolean response = true;
		double dist = Math.abs(((line.getEnd().x - line.getStart().x) * (line.getStart().y - p.y))
				- ((line.getStart().x - p.x) * (line.getEnd().y - line.getStart().y)))
				/ Math.sqrt(Math.pow((line.getEnd().x - line.getStart().x), 2) + Math.pow((line.getEnd().y - line.getStart().y), 2));

		if (dist > 4.0)
		{
			response = false;
		}

		return response;
	}
	public void processDragged(Point p)
	{
		switch (this.currentState)
		{
			case TRIANGLE :
				break;

			case SQUARE :
				this.updateSquare(p);
				break;

			case RECTANGLE :
				updateRectangle(p);
				break;

			case CIRCLE :
				this.updateCircle(p);
				break;

			case ELLIPSE :
				this.updateEllipse(p);
				break;

			case LINE :
				((Line) this.currentShape).setEnd(p);

				break;

			case SELECT :
				break;

			case FREE :
				break;

			default :

		}

		GUIFunctions.refresh();
	}

	public void updateCircle(Point p)
	{
		int deltaY = Math.abs(this.startPoint.y - p.y);
		int deltaX = Math.abs(this.startPoint.x - p.x);
		int diameter = (deltaX > deltaY) ? deltaY : deltaX;
		double radius = diameter / 2;

		Circle circle = (Circle) currentShape;
		circle.setRadius((int) radius);

		if (this.startPoint.x > p.x)
			if (this.startPoint.y > p.y) // new point is to the upper left of start point
			{
				circle.setOffset(new Point(this.startPoint.x - (int) radius, this.startPoint.y - (int) radius));
			}
			else
			// new point is to the lower left of start point
			{
				circle.setOffset(new Point(this.startPoint.x - (int) radius, this.startPoint.y + (int) radius));
			}
		else
		{
			if (this.startPoint.y > p.y) // new point is to upper right of start point
			{
				circle.setOffset(new Point(this.startPoint.x + (int) radius, this.startPoint.y - (int) radius));
			}
			else
			// new point is to the lower right of the start point
			{
				circle.setOffset(new Point(this.startPoint.x + (int) radius, this.startPoint.y + (int) radius));
			}
		}
	}

	public void updateEllipse(Point p)
	{
		double halfHeight = Math.abs((this.startPoint.y - p.y)) / 2;
		double halfWidth = Math.abs((this.startPoint.x - p.x)) / 2;
		Ellipse ellipse = (Ellipse) currentShape;
		ellipse.setHalfHeight((int) halfHeight);
		ellipse.setHalfWidth((int) halfWidth);

		if (this.startPoint.x > p.x)
			if (this.startPoint.y > p.y) // new point is to the upper left of start point
			{
				ellipse.setOffset(new Point(this.startPoint.x - (int) halfWidth, this.startPoint.y - (int) halfHeight));
			}
			else
			// new point is to the lower left of start point
			{
				ellipse.setOffset(new Point(this.startPoint.x - (int) halfWidth, this.startPoint.y + (int) halfHeight));
			}

		else
		{
			if (this.startPoint.y > p.y) // new point is to upper right of start point
			{
				ellipse.setOffset(new Point(this.startPoint.x + (int) halfWidth, this.startPoint.y - (int) halfHeight));
			}
			else
			// new point is to the lower right of the start point
			{
				ellipse.setOffset(new Point(this.startPoint.x + (int) halfWidth, this.startPoint.y + (int) halfHeight));
			}
		}
	}

	public void updateRectangle(Point p)
	{
		double halfHeight = Math.abs((this.startPoint.y - p.y)) / 2;
		double halfWidth = Math.abs((this.startPoint.x - p.x)) / 2;
		Rectangle rectangle = (Rectangle) currentShape;
		rectangle.setHalfHeight((int) halfHeight);
		rectangle.setHalfWidth((int) halfWidth);

		if (this.startPoint.x > p.x)
		{
			if (this.startPoint.y > p.y) // new point is to the upper left of start point
			{
				rectangle.setOffset(new Point(this.startPoint.x - (int) halfWidth, this.startPoint.y - (int) halfHeight));
			}
			else
			// new point is to the lower left of start point
			{
				rectangle.setOffset(new Point(this.startPoint.x - (int) halfWidth, this.startPoint.y + (int) halfHeight));
			}
		}
		else
		{
			if (this.startPoint.y > p.y) // new point is to upper right of start point
			{
				rectangle.setOffset(new Point(this.startPoint.x + (int) halfWidth, this.startPoint.y - (int) halfHeight));
			}
			else
			// new point is to the lower right of start point
			{
				rectangle.setOffset(new Point(this.startPoint.x + (int) halfWidth, this.startPoint.y + (int) halfHeight));
			}
		}
	}

	public void updateSquare(Point p)
	{
		int deltaY = Math.abs(this.startPoint.y - p.y);
		int deltaX = Math.abs(this.startPoint.x - p.x);
		int side = (deltaX > deltaY) ? deltaY : deltaX;
		double halfSide = side / 2;
		Square square = (Square) currentShape;
		square.setHalfSize((int) halfSide);

		if (this.startPoint.x > p.x)
		{
			if (this.startPoint.y > p.y) // new point is to the upper left of start point
			{
				square.setOffset(new Point(this.startPoint.x - (int) halfSide, this.startPoint.y - (int) halfSide));
			}
			else
			// new point is to the lower left of start point
			{
				square.setOffset(new Point(this.startPoint.x - (int) halfSide, this.startPoint.y + (int) halfSide));
			}
		}
		else
		{
			if (this.startPoint.y > p.y) // new point is to upper right of start point
			{
				square.setOffset(new Point(this.startPoint.x + (int) halfSide, this.startPoint.y - (int) halfSide));
			}
			else
			{
				square.setOffset(new Point(this.startPoint.x + (int) halfSide, this.startPoint.y + (int) halfSide));
			}
		}
	}

	public void processPressed(Point p)
	{
		this.startPoint = p;

		switch (this.currentState)
		{
			case TRIANGLE :
				return;

			case SQUARE :
				this.currentShape = new Square(this.currentColor, p, 0);
				break;

			case RECTANGLE :
				this.currentShape = new Rectangle(this.currentColor, p, 0, 0);
				break;

			case CIRCLE :
				this.currentShape = new Circle(this.currentColor, p, 0);
				break;

			case ELLIPSE :
				this.currentShape = new Ellipse(this.currentColor, p, 0, 0);
				break;

			case LINE :
				this.currentShape = new Line(this.currentColor, p, p);
				break;

			case SELECT :
				return;

			case FREE :
				return;

			default :

		}

		Model.inst().addShape(this.currentShape);
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
		this.selectedShape = null;
		this.currentState = ControllerState.FREE;
		GUIFunctions.refresh();
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
