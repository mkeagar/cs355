package cs355.lab3;

import static common.ConvertCoordinates.objectToWorld;
import static common.ConvertCoordinates.viewToWorld;
import static common.ConvertCoordinates.worldToObject;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

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
	private Point viewOrigin = new Point();
	private boolean pressedResizeHandle = false;
	private boolean pressedRotateHandle = false;
	private int triCornCount = 0;
	private double scaleFactor = 1.0d;
	private final int VIEWSIZE = 512;

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
	public void lineButtonHit()
	{
		this.resetState();
		this.currentState = ControllerState.LINE;
	}

	public void processClicked(Point p)
	{
		this.startPoint = viewToWorld(p);

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

	public void selectShape(Point wP)
	{
		Shape temp = null;
		for (int i = Model.inst().size() - 1; i >= 0; --i)
		{
			temp = Model.inst().getShape(i);
			if (this.containsPoint(temp, wP))
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

	public Point getWorldViewDiff()
	{
		return this.viewOrigin;
	}

	public double getScaleFactor()
	{
		return this.scaleFactor;
	}

	public boolean containsPoint(Shape shape, Point wP)
	{
		boolean response = false;
		Point convertedPoint = worldToObject(wP, shape);

		if (shape instanceof Line)
		{
			Line temp = (Line) shape;
			boolean passedDistanceTest = this.lineDistanceTest(temp, wP);
			boolean passedSegmentPointTest = this.lineSegmentPointTest(temp, wP);

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
				if (pow(convertedPoint.x / (double) ellipse.getHalfWidth(), 2) + pow(convertedPoint.y / (double) ellipse.getHalfHeight(), 2) <= 1)
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

	public boolean lineSegmentPointTest(Line line, Point wP)
	{
		boolean response = false;
		double vX = wP.x - line.getStart().x;
		double vY = wP.y - line.getStart().y;
		double abSqrt = sqrt(pow((line.getEnd().x - line.getStart().x), 2) + pow((line.getEnd().y - line.getStart().y), 2));
		double dHatX = (line.getEnd().x - line.getStart().x) / abSqrt;
		double dHatY = (line.getEnd().y - line.getStart().y) / abSqrt;
		double d = ((vX * dHatX) + (vY * dHatY)) / abSqrt;

		if ((0 <= d) && (d <= abSqrt))
		{
			response = true;
		}
		return response;
	}

	public boolean lineDistanceTest(Line line, Point wP)
	{
		boolean response = true;
		double dist = abs(((line.getEnd().x - line.getStart().x) * (line.getStart().y - wP.y))
				- ((line.getStart().x - wP.x) * (line.getEnd().y - line.getStart().y)))
				/ sqrt(pow((line.getEnd().x - line.getStart().x), 2) + pow((line.getEnd().y - line.getStart().y), 2));

		if (dist > 4.0)
		{
			response = false;
		}

		return response;
	}

	public void processDragged(Point p)
	{
		Point wP = viewToWorld(p);

		switch (this.currentState)
		{
			case TRIANGLE :
				break;

			case SQUARE :
				this.updateSquare(wP);
				break;

			case RECTANGLE :
				updateRectangle(wP);
				break;

			case CIRCLE :
				this.updateCircle(wP);
				break;

			case ELLIPSE :
				this.updateEllipse(wP);
				break;

			case LINE :
				((Line) this.currentShape).setEnd(wP);

				break;

			case SELECT :
				if (this.selectedShape == null) break;
				if (pressedResizeHandle)
				{
					if (this.selectedShape instanceof Line)
					{
						((Line) this.selectedShape).setEnd(wP);
					}
					else if (this.selectedShape instanceof Rectangle)
					{
						this.currentShape = this.selectedShape;
						if (this.selectedShape instanceof Square)
						{
							this.updateSquare(wP);
							break;
						}
						this.updateRectangle(wP);
					}
					else if (this.selectedShape instanceof Ellipse)
					{
						this.currentShape = this.selectedShape;
						if (this.selectedShape instanceof Circle)
						{
							this.updateCircle(wP);
							break;
						}
						this.updateEllipse(wP);
					}
					else if (this.selectedShape instanceof Triangle)
					{
						Triangle triangle = (Triangle) this.selectedShape;
						Point op = worldToObject(wP, triangle);

						this.selectedHandle.x = op.x;
						this.selectedHandle.y = op.y;

						Point aObject = triangle.getPointA();
						Point bObject = triangle.getPointB();
						Point cObject = triangle.getPointC();

						Point aWorld = objectToWorld(aObject, triangle);
						Point bWorld = objectToWorld(bObject, triangle);
						Point cWorld = objectToWorld(cObject, triangle);

						double newXOffset = (aWorld.x + bWorld.x + cWorld.x) / 3.0;
						double newYOffset = (aWorld.y + bWorld.y + cWorld.y) / 3.0;

						Point newOffset = new Point((int) newXOffset, (int) newYOffset);
						triangle.setOffset(newOffset);

						if (this.selectedHandle != triangle.getPointA())
						{
							triangle.setPointA(worldToObject(aWorld, triangle));
						}
						else if (this.selectedHandle != triangle.getPointB())
						{
							triangle.setPointB(worldToObject(bWorld, triangle));
						}
						else if (this.selectedHandle != triangle.getPointC())
						{
							triangle.setPointC(worldToObject(cWorld, triangle));
						}
					}
				}
				else if (pressedRotateHandle)
				{
					if (this.selectedShape instanceof Rectangle)
					{
						Rectangle rectangle = (Rectangle) this.selectedShape;

						int deltaX = rectangle.getXOffset() - wP.x;
						int deltaY = rectangle.getYOffset() - wP.y;

						double theta = atan2(deltaY, deltaX) - PI / 2.0;
						rectangle.setRotation(theta);
					}
					else if (this.selectedShape instanceof Ellipse)
					{
						Ellipse ellipse = (Ellipse) this.selectedShape;

						int deltaX = ellipse.getXOffset() - wP.x;
						int deltaY = ellipse.getYOffset() - wP.y;

						double theta = atan2(deltaY, deltaX) - PI / 2.0;
						ellipse.setRotation(theta);
					}
					else if (this.selectedShape instanceof Triangle)
					{
						Triangle triangle = (Triangle) this.selectedShape;

						double deltaX = (double) triangle.getXOffset() - wP.x;
						double deltaY = (double) triangle.getYOffset() - wP.y;

						double theta = atan2(deltaY, deltaX) - PI / 2.0;
						triangle.setRotation(theta);
					}
				}
				else
				{
					this.updateOffset(this.selectedShape, wP);
				}
				break;

			case FREE :
				break;

			default :

		}

		GUIFunctions.refresh();
	}

	public void updateOffset(Shape shape, Point wP)
	{
		int deltaX = wP.x - this.lastDragPoint.x;
		int deltaY = wP.y - this.lastDragPoint.y;

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
		this.lastDragPoint = wP;
	}

	public void updateCircle(Point wP)
	{
		Circle circle = (Circle) currentShape;

		Point oP = worldToObject(wP, circle);
		Point objectStartPoint = worldToObject(this.startPoint, circle);

		int deltaY = abs(objectStartPoint.y - oP.y);
		int deltaX = abs(objectStartPoint.x - oP.x);
		int diameter = (deltaX > deltaY) ? deltaY : deltaX;
		double radius = diameter / 2.0;

		circle.setRadius((int) radius);

		if (objectStartPoint.x > oP.x)
			if (objectStartPoint.y > oP.y) // new point is to the upper left of start point
			{
				circle.setOffset(objectToWorld(new Point(objectStartPoint.x - (int) radius, objectStartPoint.y - (int) radius), circle));
			}
			else
			// new point is to the lower left of start point
			{
				circle.setOffset(objectToWorld(new Point(objectStartPoint.x - (int) radius, objectStartPoint.y + (int) radius), circle));
			}
		else
		{
			if (objectStartPoint.y > oP.y) // new point is to upper right of start point
			{
				circle.setOffset(objectToWorld(new Point(objectStartPoint.x + (int) radius, objectStartPoint.y - (int) radius), circle));
			}
			else
			// new point is to the lower right of the start point
			{
				circle.setOffset(objectToWorld(new Point(objectStartPoint.x + (int) radius, objectStartPoint.y + (int) radius), circle));
			}
		}
	}

	public void updateEllipse(Point wP)
	{
		Ellipse ellipse = (Ellipse) currentShape;
		Point oP = worldToObject(wP, ellipse);
		Point objectStartPoint = worldToObject(this.startPoint, ellipse);

		double halfHeight = abs((objectStartPoint.y - oP.y)) / 2.0;
		double halfWidth = abs((objectStartPoint.x - oP.x)) / 2.0;

		ellipse.setHalfHeight((int) halfHeight);
		ellipse.setHalfWidth((int) halfWidth);

		if (objectStartPoint.x > oP.x)
			if (objectStartPoint.y > oP.y) // new point is to the upper left of start point
			{
				ellipse.setOffset(objectToWorld(new Point(objectStartPoint.x - (int) halfWidth, objectStartPoint.y - (int) halfHeight), ellipse));
			}
			else
			// new point is to the lower left of start point
			{
				ellipse.setOffset(objectToWorld(new Point(objectStartPoint.x - (int) halfWidth, objectStartPoint.y + (int) halfHeight), ellipse));
			}
		else
		{
			if (objectStartPoint.y > oP.y) // new point is to upper right of start point
			{
				ellipse.setOffset(objectToWorld(new Point(objectStartPoint.x + (int) halfWidth, objectStartPoint.y - (int) halfHeight), ellipse));
			}
			else
			// new point is to the lower right of the start point
			{
				ellipse.setOffset(objectToWorld(new Point(objectStartPoint.x + (int) halfWidth, objectStartPoint.y + (int) halfHeight), ellipse));
			}
		}
	}

	public void updateRectangle(Point wP)
	{
		Rectangle rectangle = (Rectangle) currentShape;

		Point oP = worldToObject(wP, rectangle);
		Point objectStartPoint = worldToObject(this.startPoint, rectangle);

		double halfHeight = abs((objectStartPoint.y - oP.y)) / 2.0;
		double halfWidth = abs((objectStartPoint.x - oP.x)) / 2.0;
		rectangle.setHalfHeight((int) halfHeight);
		rectangle.setHalfWidth((int) halfWidth);

		if (objectStartPoint.x > oP.x)
		{
			if (objectStartPoint.y > oP.y) // new point is to the upper left of start point
			{
				rectangle.setOffset(objectToWorld(new Point(objectStartPoint.x - (int) halfWidth, objectStartPoint.y - (int) halfHeight), rectangle));
			}
			else
			// new point is to the lower left of start point
			{
				rectangle.setOffset(objectToWorld(new Point(objectStartPoint.x - (int) halfWidth, objectStartPoint.y + (int) halfHeight), rectangle));
			}
		}
		else
		{
			if (objectStartPoint.y > oP.y) // new point is to upper right of start point
			{
				rectangle.setOffset(objectToWorld(new Point(objectStartPoint.x + (int) halfWidth, objectStartPoint.y - (int) halfHeight), rectangle));
			}
			else
			// new point is to the lower right of start point
			{
				rectangle.setOffset(objectToWorld(new Point(objectStartPoint.x + (int) halfWidth, objectStartPoint.y + (int) halfHeight), rectangle));
			}
		}
	}

	public void updateSquare(Point wP)
	{
		Square square = (Square) currentShape;

		Point oP = worldToObject(wP, square);
		Point objectStartPoint = worldToObject(this.startPoint, square);

		int deltaY = abs(objectStartPoint.y - oP.y);
		int deltaX = abs(objectStartPoint.x - oP.x);
		int side = (deltaX > deltaY) ? deltaY : deltaX;
		double halfSide = side / 2.0;

		square.setHalfSize((int) halfSide);

		if (objectStartPoint.x > oP.x)
		{
			if (objectStartPoint.y > oP.y) // new point is to the upper left of start point
			{
				square.setOffset(objectToWorld(new Point(objectStartPoint.x - (int) halfSide, objectStartPoint.y - (int) halfSide), square));
			}
			else
			// new point is to the lower left of start point
			{
				square.setOffset(objectToWorld(new Point(objectStartPoint.x - (int) halfSide, objectStartPoint.y + (int) halfSide), square));
			}
		}
		else
		{
			if (objectStartPoint.y > oP.y) // new point is to upper right of start point
			{
				square.setOffset(objectToWorld(new Point(objectStartPoint.x + (int) halfSide, objectStartPoint.y - (int) halfSide), square));
			}
			else
			{
				square.setOffset(objectToWorld(new Point(objectStartPoint.x + (int) halfSide, objectStartPoint.y + (int) halfSide), square));
			}
		}
	}

	public void processPressed(Point p)
	{
		Point wP = viewToWorld(p);
		this.startPoint = wP;

		switch (this.currentState)
		{
			case TRIANGLE :
				return;

			case SQUARE :
				this.currentShape = new Square(this.currentColor, wP, 0);
				break;

			case RECTANGLE :
				this.currentShape = new Rectangle(this.currentColor, wP, 0, 0);
				break;

			case CIRCLE :
				this.currentShape = new Circle(this.currentColor, wP, 0);
				break;

			case ELLIPSE :
				this.currentShape = new Ellipse(this.currentColor, wP, 0, 0);
				break;

			case LINE :
				this.currentShape = new Line(this.currentColor, wP, wP);
				break;

			case SELECT :
				if (this.selectedShape != null)
				{
					if (this.pressedResizeHandle(wP))
					{
						System.out.println("Pressed Resize Handle");
						this.pressedResizeHandle = true;
						this.pressedRotateHandle = false;
						this.setHandleAnchor(wP);
						return;
					}
					else if (this.pressedRotateHandle(wP))
					{
						System.out.println("Pressed Rotate Handle");
						this.pressedResizeHandle = false;
						this.pressedRotateHandle = true;
						this.startPoint = wP;
						return;
					}
					else
					{
						this.pressedResizeHandle = false;
						this.pressedRotateHandle = false;
					}
				}
				this.lastDragPoint = wP;
				this.selectShape(wP);
				return;

			case FREE :
				return;

			default :

		}

		Model.inst().addShape(this.currentShape);
		GUIFunctions.refresh();
	}

	public Point getSelectedHandle()
	{
		return this.selectedHandle;
	}

	public void setHandleAnchor(Point wP)
	{
		Point convertedPoint = worldToObject(wP, this.selectedShape); // converted Point is in Object Space

		if (this.selectedShape instanceof Line)
		{
			Line line = (Line) this.selectedShape;
			if (line.getStart().distance(wP) <= View.HANDLE_RADIUS)
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

			Point upLeft = new Point(-hw, -hh);
			Point upRight = new Point(+hw, -hh);
			Point lowLeft = new Point(-hw, +hh);
			Point lowRight = new Point(+hw, +hh);

			if (convertedPoint.distance(upLeft) <= View.HANDLE_RADIUS)
			{
				this.startPoint = objectToWorld(lowRight, rectangle);
			}
			else if (convertedPoint.distance(upRight) <= View.HANDLE_RADIUS)
			{
				this.startPoint = objectToWorld(lowLeft, rectangle);
			}
			else if (convertedPoint.distance(lowLeft) <= View.HANDLE_RADIUS)
			{
				this.startPoint = objectToWorld(upRight, rectangle);
			}
			else if (convertedPoint.distance(lowRight) <= View.HANDLE_RADIUS)
			{
				this.startPoint = objectToWorld(upLeft, rectangle);
			}
		}
		else if (this.selectedShape instanceof Ellipse)
		{
			Ellipse ellipse = (Ellipse) this.selectedShape;
			int hh = ellipse.getHalfHeight();
			int hw = ellipse.getHalfWidth();

			Point upLeft = new Point(-hw, -hh);
			Point upRight = new Point(+hw, -hh);
			Point lowLeft = new Point(-hw, +hh);
			Point lowRight = new Point(+hw, +hh);

			if (convertedPoint.distance(upLeft) <= View.HANDLE_RADIUS)
			{
				this.startPoint = objectToWorld(lowRight, ellipse);
			}
			else if (convertedPoint.distance(upRight) <= View.HANDLE_RADIUS)
			{
				this.startPoint = objectToWorld(lowLeft, ellipse);
			}
			else if (convertedPoint.distance(lowLeft) <= View.HANDLE_RADIUS)
			{
				this.startPoint = objectToWorld(upRight, ellipse);
			}
			else if (convertedPoint.distance(lowRight) <= View.HANDLE_RADIUS)
			{
				this.startPoint = objectToWorld(upLeft, ellipse);
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

	public boolean pressedRotateHandle(Point wP)  // Doesn't work properly yet. Too tired to work on it any longer.
	{
		boolean response = false;
		Point convertedPoint = worldToObject(wP, this.selectedShape);

		if (this.selectedShape instanceof Rectangle)
		{
			Rectangle rectangle = (Rectangle) this.selectedShape;

			if ((convertedPoint.distance(new Point(0, (int) (View.HANDLE_RADIUS / this.scaleFactor) - rectangle.getHalfHeight()
					- (int) (View.ROTATION_HANDLE_Y_OFFSET / this.scaleFactor))) <= (View.HANDLE_RADIUS) / this.scaleFactor))
			{
				response = true;
			}
		}
		else if (this.selectedShape instanceof Ellipse)
		{
			Ellipse ellipse = (Ellipse) this.selectedShape;
			if ((convertedPoint.distance(new Point(0, (int) (View.HANDLE_RADIUS / this.scaleFactor) - ellipse.getHalfHeight()
					- (int) (View.ROTATION_HANDLE_Y_OFFSET / this.scaleFactor))) <= (int) (View.HANDLE_RADIUS / this.scaleFactor)))
			{
				response = true;
			}
		}
		else if (this.selectedShape instanceof Triangle)
		{
			Triangle triangle = (Triangle) this.selectedShape;
			int highestY = max(abs(triangle.getPointA().y), max(abs(triangle.getPointB().y), abs(triangle.getPointC().y)));

			if ((convertedPoint.distance(new Point(0, (int) (View.HANDLE_RADIUS / this.scaleFactor) - highestY
					- (int) (View.ROTATION_HANDLE_Y_OFFSET / this.scaleFactor))) <= (int) (View.HANDLE_RADIUS / this.scaleFactor)))
			{
				response = true;
			}
		}
		return response;
	}

	public boolean pressedResizeHandle(Point wP) // World point passed in - Think this is done.
	{
		boolean response = false;
		Point convertedPoint = worldToObject(wP, this.selectedShape);

		if (this.selectedShape instanceof Line)
		{
			Line line = (Line) this.selectedShape;
			if ((line.getStart().distance(wP) <= (View.HANDLE_RADIUS / this.scaleFactor))
					|| (line.getEnd().distance(wP) <= (View.HANDLE_RADIUS / this.scaleFactor)))
			{
				response = true;
			}
		}
		else if (this.selectedShape instanceof Rectangle)
		{
			Rectangle rectangle = (Rectangle) this.selectedShape;

			if ((convertedPoint.distance(new Point(rectangle.getHalfWidth(), rectangle.getHalfHeight())) <= (View.HANDLE_RADIUS / this.scaleFactor))
					|| (convertedPoint.distance(new Point(rectangle.getHalfWidth(), -rectangle.getHalfHeight())) <= (View.HANDLE_RADIUS / this.scaleFactor))
					|| (convertedPoint.distance(new Point(-rectangle.getHalfWidth(), rectangle.getHalfHeight())) <= (View.HANDLE_RADIUS / this.scaleFactor))
					|| (convertedPoint.distance(new Point(-rectangle.getHalfWidth(), -rectangle.getHalfHeight())) <= (View.HANDLE_RADIUS / this.scaleFactor)))
			{
				response = true;
			}

		}
		else if (this.selectedShape instanceof Ellipse)
		{
			Ellipse ellipse = (Ellipse) this.selectedShape;
			if ((convertedPoint.distance(new Point(ellipse.getHalfWidth(), ellipse.getHalfHeight())) <= (View.HANDLE_RADIUS / this.scaleFactor))
					|| (convertedPoint.distance(new Point(ellipse.getHalfWidth(), -ellipse.getHalfHeight())) <= (View.HANDLE_RADIUS / this.scaleFactor))
					|| (convertedPoint.distance(new Point(-ellipse.getHalfWidth(), ellipse.getHalfHeight())) <= (View.HANDLE_RADIUS / this.scaleFactor))
					|| (convertedPoint.distance(new Point(-ellipse.getHalfWidth(), -ellipse.getHalfHeight())) <= (View.HANDLE_RADIUS / this.scaleFactor)))
			{
				response = true;
			}
		}
		else if (this.selectedShape instanceof Triangle)
		{
			Triangle triangle = (Triangle) this.selectedShape;
			if ((convertedPoint.distance(triangle.getPointA()) <= (View.HANDLE_RADIUS / this.scaleFactor))
					|| (convertedPoint.distance(triangle.getPointB()) <= (View.HANDLE_RADIUS / this.scaleFactor))
					|| (convertedPoint.distance(triangle.getPointC()) <= (View.HANDLE_RADIUS / this.scaleFactor)))
			{
				response = true;
			}
		}

		return response;
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
		this.viewOrigin = new Point(this.viewOrigin.x, value);

		GUIFunctions.refresh();
	}

	@Override
	public void hScrollbarChanged(int value)
	{
		this.viewOrigin = new Point(value, this.viewOrigin.y);

		GUIFunctions.refresh();
	}

	@Override
	public void zoomInButtonHit()
	{
		if (this.scaleFactor != 4.0)
		{
			this.scaleFactor *= 2;
			GUIFunctions.setHScrollBarMax((int) (this.VIEWSIZE * this.scaleFactor));
			GUIFunctions.setVScrollBarMax((int) (this.VIEWSIZE * this.scaleFactor));
		}

		GUIFunctions.refresh();
	}

	@Override
	public void zoomOutButtonHit()
	{
		if (this.scaleFactor != 0.25)
		{
			this.scaleFactor /= 2;
			GUIFunctions.setHScrollBarMax((int) (this.VIEWSIZE * this.scaleFactor));
			GUIFunctions.setVScrollBarMax((int) (this.VIEWSIZE * this.scaleFactor));
		}

		GUIFunctions.refresh();
	}
}
