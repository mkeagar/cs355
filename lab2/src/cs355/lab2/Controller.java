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
	private Point lastDragPoint = null;
	private Point[] triCornArray = new Point[3];
	private Point selectedHandle = null;
	private boolean pressedResizeHandle = false;
	private boolean pressedRotateHandle = false;
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

	// Methods

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

		GUIFunctions.refresh();
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
		Point convertedPoint = worldToObject(p, shape);

		if (shape instanceof Line)
		{
			Line temp = (Line) shape;
			boolean passedDistanceTest = this.lineDistanceTest(temp, p);
			boolean passedSegmentPointTest = this.lineSegmentPointTest(temp, p);

			return (passedDistanceTest && passedSegmentPointTest);

		}
		else if (shape instanceof Rectangle)
		{
			Rectangle rectangle = (Rectangle) shape;
			if ((convertedPoint.x < rectangle.getHalfWidth()) && (convertedPoint.x > -rectangle.getHalfWidth())
					&& (convertedPoint.y < rectangle.getHalfHeight()) && (convertedPoint.y > -rectangle.getHalfHeight()))
			{
				response = true;
			}
		}
		else if (shape instanceof Ellipse)
		{
			Ellipse ellipse = (Ellipse) shape;

			if ((convertedPoint.x <= ellipse.getHalfWidth()) && (convertedPoint.y <= ellipse.getHalfHeight()))
				if (Math.pow(convertedPoint.x / (double) ellipse.getHalfWidth(), 2)
						+ Math.pow(convertedPoint.y / (double) ellipse.getHalfHeight(), 2) <= 1)
				{
					response = true;
				}
		}
		else if (shape instanceof Triangle)
		{
			Triangle triangle = (Triangle) shape;

			double inAB = ((convertedPoint.x - triangle.getPointA().x) * (triangle.getPointB().y - triangle.getPointA().y) - (convertedPoint.y - triangle
					.getPointA().y) * (triangle.getPointB().x - triangle.getPointA().x));
			double inBC = ((convertedPoint.x - triangle.getPointB().x) * (triangle.getPointC().y - triangle.getPointB().y) - (convertedPoint.y - triangle
					.getPointB().y) * (triangle.getPointC().x - triangle.getPointB().x));
			double inCA = ((convertedPoint.x - triangle.getPointC().x) * (triangle.getPointA().y - triangle.getPointC().y) - (convertedPoint.y - triangle
					.getPointC().y) * (triangle.getPointA().x - triangle.getPointC().x));

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

		if ((0 <= d) && (d <= abSqrt))
		{
			response = true;
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
				if (this.selectedShape == null) break;
				if (pressedResizeHandle)
				{
					if (this.selectedShape instanceof Line)
					{
						((Line) this.selectedShape).setEnd(p);
					}
					else if (this.selectedShape instanceof Rectangle)
					{
						this.currentShape = this.selectedShape;
						if (this.selectedShape instanceof Square)
						{
							this.updateSquare(p);
							break;
						}
						this.updateRectangle(p);
					}
					else if (this.selectedShape instanceof Ellipse)
					{
						this.currentShape = this.selectedShape;
						if (this.selectedShape instanceof Circle)
						{
							this.updateCircle(p);
							break;
						}
						this.updateEllipse(p);
					}
					else if (this.selectedShape instanceof Triangle)
					{
						Triangle triangle = (Triangle) this.selectedShape;

						Point op = this.worldToObject(p, triangle);

						this.selectedHandle.x = op.x;
						this.selectedHandle.y = op.y;

						Point aWorld = objectToWorld(triangle.getPointA(), triangle);
						Point bWorld = objectToWorld(triangle.getPointB(), triangle);
						Point cWorld = objectToWorld(triangle.getPointC(), triangle);

						double newXOffset = (aWorld.x + bWorld.x + cWorld.x) / 3.0;
						double newYOffset = (aWorld.y + bWorld.y + cWorld.y) / 3.0;

						Point newOffset = new Point((int) newXOffset, (int) newYOffset);
						triangle.setOffset(newOffset);

						if (this.selectedHandle != triangle.getPointA())
						{
							triangle.setPointA(this.worldToObject(aWorld, triangle));
						}
						if (this.selectedHandle != triangle.getPointB())
						{
							triangle.setPointB(this.worldToObject(bWorld, triangle));
						}
						if (this.selectedHandle != triangle.getPointC())
						{
							triangle.setPointC(this.worldToObject(cWorld, triangle));
						}
					}
				}
				else if (pressedRotateHandle)
				{
					if (this.selectedShape instanceof Rectangle)
					{
						Rectangle rectangle = (Rectangle) this.selectedShape;

						int deltaX = rectangle.getXOffset() - p.x;
						int deltaY = rectangle.getYOffset() - p.y;

						double theta = Math.atan2(deltaY, deltaX) - Math.PI / 2;
						rectangle.setRotation(theta);
					}
					else if (this.selectedShape instanceof Ellipse)
					{
						Ellipse ellipse = (Ellipse) this.selectedShape;

						int deltaX = ellipse.getXOffset() - p.x;
						int deltaY = ellipse.getYOffset() - p.y;

						double theta = Math.atan2(deltaY, deltaX) - Math.PI / 2;
						ellipse.setRotation(theta);
					}
					else if (this.selectedShape instanceof Triangle)
					{
						Triangle triangle = (Triangle) this.selectedShape;

						int deltaX = triangle.getXOffset() - p.x;
						int deltaY = triangle.getYOffset() - p.y;

						double theta = Math.atan2(deltaY, deltaX) - Math.PI / 2;
						triangle.setRotation(theta);
					}
				}
				else
				{
					this.updateOffset(this.selectedShape, p);
				}
				break;

			case FREE :
				break;

			default :

		}

		GUIFunctions.refresh();
	}

	public void updateOffset(Shape shape, Point p)
	{
		int deltaX = p.x - this.lastDragPoint.x;
		int deltaY = p.y - this.lastDragPoint.y;

		if (shape instanceof Line)
		{
			Line line = (Line) shape;
			line.setStart(new Point(line.getStart().x + deltaX, line.getStart().y + deltaY));
			line.setEnd(new Point(line.getEnd().x + deltaX, line.getEnd().y + deltaY));
		}
		else
		{
			shape.setOffset(new Point(shape.getOffset().x + deltaX, shape.getOffset().y + deltaY));
		}
		this.lastDragPoint = p;
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
				if (this.selectedShape != null)
				{
					if (this.pressedResizeHandle(p))
					{
						System.out.println("Pressed Resize Handle");
						this.pressedResizeHandle = true;
						this.pressedRotateHandle = false;
						this.setHandleAnchor(p);
						return;
					}
					else if (this.pressedRotateHandle(p))
					{
						System.out.println("Pressed Rotate Handle");
						this.pressedResizeHandle = false;
						this.pressedRotateHandle = true;
						this.startPoint = p;
						return;
					}
					else
					{
						this.pressedResizeHandle = false;
						this.pressedRotateHandle = false;
					}
				}
				this.lastDragPoint = p;
				this.selectShape(p);
				return;

			case FREE :
				return;

			default :

		}

		Model.inst().addShape(this.currentShape);
		GUIFunctions.refresh();
	}

	public void setHandleAnchor(Point p)
	{
		Point convertedPoint = worldToObject(p, this.selectedShape);

		if (this.selectedShape instanceof Line)
		{
			Line line = (Line) this.selectedShape;
			if (line.getStart().distance(p) <= View.HANDLE_RADIUS)
			{
				Point temp = line.getStart();
				line.setStart(line.getEnd());
				line.setEnd(temp);
			}
		}
		else if (this.selectedShape instanceof Rectangle)
		{
			Rectangle rectangle = (Rectangle) this.selectedShape;
			int hh = rectangle.getHalfHeight();
			int hw = rectangle.getHalfWidth();
			int xOff = rectangle.getXOffset();
			int yOff = rectangle.getYOffset();

			Point upLeft = new Point(xOff - hw, yOff - hh);
			Point upRight = new Point(xOff + hw, yOff - hh);
			Point lowLeft = new Point(xOff - hw, yOff + hh);
			Point lowRight = new Point(xOff + hw, yOff + hh);

			if (p.distance(upLeft) <= View.HANDLE_RADIUS)
			{
				this.startPoint = lowRight;
			}
			else if (p.distance(upRight) <= View.HANDLE_RADIUS)
			{
				this.startPoint = lowLeft;
			}
			else if (p.distance(lowLeft) <= View.HANDLE_RADIUS)
			{
				this.startPoint = upRight;
			}
			else if (p.distance(lowRight) <= View.HANDLE_RADIUS)
			{
				this.startPoint = upLeft;
			}

		}
		else if (this.selectedShape instanceof Ellipse)
		{
			Ellipse ellipse = (Ellipse) this.selectedShape;
			int hh = ellipse.getHalfHeight();
			int hw = ellipse.getHalfWidth();
			int xOff = ellipse.getXOffset();
			int yOff = ellipse.getYOffset();

			Point upLeft = new Point(xOff - hw, yOff - hh);
			Point upRight = new Point(xOff + hw, yOff - hh);
			Point lowLeft = new Point(xOff - hw, yOff + hh);
			Point lowRight = new Point(xOff + hw, yOff + hh);

			if (p.distance(upLeft) <= View.HANDLE_RADIUS)
			{
				this.startPoint = lowRight;
			}
			else if (p.distance(upRight) <= View.HANDLE_RADIUS)
			{
				this.startPoint = lowLeft;
			}
			else if (p.distance(lowLeft) <= View.HANDLE_RADIUS)
			{
				this.startPoint = upRight;
			}
			else if (p.distance(lowRight) <= View.HANDLE_RADIUS)
			{
				this.startPoint = upLeft;
			}
		}
		else if (this.selectedShape instanceof Triangle)
		{
			Triangle triangle = (Triangle) this.selectedShape;

			if (convertedPoint.distance(triangle.getPointA()) <= View.HANDLE_RADIUS)
			{
				this.selectedHandle = triangle.getPointA();
			}
			else if (convertedPoint.distance(triangle.getPointB()) <= View.HANDLE_RADIUS)
			{
				this.selectedHandle = triangle.getPointB();
			}
			else if (convertedPoint.distance(triangle.getPointC()) <= View.HANDLE_RADIUS)
			{
				this.selectedHandle = triangle.getPointC();
			}
		}
	}

	public boolean pressedRotateHandle(Point p)  // Doesn't work properly yet. Too tired to work on it any longer.
	{
		boolean response = false;
		Point convertedPoint = this.worldToObject(p, this.selectedShape);

		if (this.selectedShape instanceof Rectangle)
		{
			Rectangle rectangle = (Rectangle) this.selectedShape;

			if ((convertedPoint.distance(new Point(0, View.HANDLE_RADIUS - rectangle.getHalfHeight() - View.ROTATION_HANDLE_Y_OFFSET)) <= View.HANDLE_RADIUS))
			{
				response = true;
			}
		}
		else if (this.selectedShape instanceof Ellipse)
		{
			Ellipse ellipse = (Ellipse) this.selectedShape;
			if ((convertedPoint.distance(new Point(0, View.HANDLE_RADIUS - ellipse.getHalfHeight() - View.ROTATION_HANDLE_Y_OFFSET)) <= View.HANDLE_RADIUS))
			{
				response = true;
			}
		}
		else if (this.selectedShape instanceof Triangle)
		{
			Triangle triangle = (Triangle) this.selectedShape;
			int highestY = Math.max(Math.abs(triangle.getPointA().y), Math.max(Math.abs(triangle.getPointB().y), Math.abs(triangle.getPointC().y)));

			if ((convertedPoint.distance(new Point(0, View.HANDLE_RADIUS - highestY - View.ROTATION_HANDLE_Y_OFFSET)) <= View.HANDLE_RADIUS))
			{
				response = true;
			}
		}
		return response;
	}

	public boolean pressedResizeHandle(Point p)
	{
		boolean response = false;
		Point convertedPoint = this.worldToObject(p, this.selectedShape);

		if (this.selectedShape instanceof Line)
		{
			Line line = (Line) this.selectedShape;
			if ((line.getStart().distance(p) <= View.HANDLE_RADIUS) || (line.getEnd().distance(p) <= View.HANDLE_RADIUS))
			{
				response = true;
			}
		}
		else if (this.selectedShape instanceof Rectangle)
		{
			Rectangle rectangle = (Rectangle) this.selectedShape;

			if ((convertedPoint.distance(new Point(rectangle.getHalfWidth(), rectangle.getHalfHeight())) < View.HANDLE_RADIUS)
					|| (convertedPoint.distance(new Point(rectangle.getHalfWidth(), -rectangle.getHalfHeight())) <= View.HANDLE_RADIUS)
					|| (convertedPoint.distance(new Point(-rectangle.getHalfWidth(), rectangle.getHalfHeight())) <= View.HANDLE_RADIUS)
					|| (convertedPoint.distance(new Point(-rectangle.getHalfWidth(), -rectangle.getHalfHeight())) <= View.HANDLE_RADIUS))
			{
				response = true;
			}

		}
		else if (this.selectedShape instanceof Ellipse)
		{
			Ellipse ellipse = (Ellipse) this.selectedShape;
			if ((convertedPoint.distance(new Point(ellipse.getHalfWidth(), ellipse.getHalfHeight())) < View.HANDLE_RADIUS)
					|| (convertedPoint.distance(new Point(ellipse.getHalfWidth(), -ellipse.getHalfHeight())) <= View.HANDLE_RADIUS)
					|| (convertedPoint.distance(new Point(-ellipse.getHalfWidth(), ellipse.getHalfHeight())) <= View.HANDLE_RADIUS)
					|| (convertedPoint.distance(new Point(-ellipse.getHalfWidth(), -ellipse.getHalfHeight())) <= View.HANDLE_RADIUS))
			{
				response = true;
			}
		}
		else if (this.selectedShape instanceof Triangle)
		{
			Triangle triangle = (Triangle) this.selectedShape;
			if ((convertedPoint.distance(triangle.getPointA()) <= View.HANDLE_RADIUS)
					|| (convertedPoint.distance(triangle.getPointB()) <= View.HANDLE_RADIUS)
					|| (convertedPoint.distance(triangle.getPointC()) <= View.HANDLE_RADIUS))
			{
				response = true;
			}
		}

		return response;
	}

	public Point objectToWorld(Point op, Shape shape)
	{
		double theta = shape.getRotation();
		double opX = op.x;
		double opY = op.y;
		double cX = shape.getXOffset();
		double cY = shape.getYOffset();

		double wX = Math.cos(theta) * opX - Math.sin(theta) * opY + cX;
		double wY = Math.sin(theta) * opX + Math.cos(theta) * opY + cY;

		Point wp = new Point((int) wX, (int) wY);
		return wp;
	}

	public Point worldToObject(Point wp, Shape shape)
	{
		double theta = shape.getRotation();
		double cX = shape.getXOffset();
		double cY = shape.getYOffset();
		double wpX = wp.x;
		double wpY = wp.y;

		double x = Math.cos(theta) * wpX + Math.sin(theta) * wpY + (-Math.cos(theta) * cX - Math.sin(theta) * cY);
		double y = -Math.sin(theta) * wpX + Math.cos(theta) * wpY + (Math.sin(theta) * cX - Math.cos(theta) * cY);

		Point op = new Point((int) x, (int) y);
		return op;
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