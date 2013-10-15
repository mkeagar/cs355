package cs355.lab2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import cs355.ViewRefresher;
import cs355.model.*;

public class View implements ViewRefresher
{
	// Variables
	private enum ShapeType
	{
		NONE, LINE, RECTANGLE, ELLIPSE, TRIANGLE
	};
	private ShapeType selectedShapeType = ShapeType.NONE;
	private Shape selectedShape = null;
	public final static int ROTATION_HANDLE_Y_OFFSET = 30;
	private final int HANDLE_WIDTH = 10;
	public final static int HANDLE_RADIUS = 5;
	private final int HANDLE_OFFSET = 5;
	private final int SELECT_STROKE = 2;
	private Color selectColor = Color.WHITE;
	private final Color WHITE_SELECT_COLOR = new Color(250, 50, 0);
	private final Color ROTATION_HANDLE_COLOR = new Color(200, 25, 200);

	// Singleton Stuff
	private static View instance = null;

	// Singleton Method
	public static View inst()
	{
		if (instance == null)
		{
			instance = new View();
		}
		return instance;
	}

	// Constructor
	protected View()
	{
		// Exists only to defeat instantiation
	}

	// Methods

	@Override
	public void refreshView(Graphics2D g2d)
	{
		this.selectedShapeType = ShapeType.NONE;

		for (int i = 0; i < Model.inst().size(); i++)
		{
			Shape currentShape = Model.inst().getShape(i);
			this.selectedShape = Controller.inst().getSelectedShape();
			g2d.translate(currentShape.getXOffset(), currentShape.getYOffset());
			g2d.rotate(currentShape.getRotation());

			if (currentShape instanceof Line)
			{
				Line line = (Line) currentShape;
				g2d.setColor(line.getColor());
				g2d.drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y);
				// currentShape = null;

				if (currentShape == selectedShape)
				{
					this.selectColor = this.invertColor(currentShape.getColor());
					g2d.setColor(this.selectColor);
					g2d.drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y);
					this.selectedShapeType = ShapeType.LINE;
				}
			}
			else if (currentShape instanceof Rectangle)
			{
				Rectangle rectangle = (Rectangle) currentShape;
				g2d.setColor(rectangle.getColor());
				g2d.fillRect(rectangle.getUpperLeftCorner().x, rectangle.getUpperLeftCorner().y, rectangle.getWidth(), rectangle.getHeight());
				// currentShape = null;

				if (currentShape == selectedShape)
				{
					this.selectColor = this.invertColor(currentShape.getColor());
					g2d.setColor(this.selectColor);
					g2d.setStroke(new BasicStroke(SELECT_STROKE));
					g2d.drawRect(rectangle.getUpperLeftCorner().x, rectangle.getUpperLeftCorner().y, rectangle.getWidth(), rectangle.getHeight());
					g2d.setStroke(new BasicStroke());
					this.selectedShapeType = ShapeType.RECTANGLE;
				}

			}
			else if (currentShape instanceof Ellipse)
			{
				Ellipse ellipse = (Ellipse) currentShape;
				g2d.setColor(ellipse.getColor());
				g2d.fillOval(ellipse.getUpperLeftCorner().x, ellipse.getUpperLeftCorner().y, ellipse.getWidth(), ellipse.getHeight());
				// currentShape = null;

				if (currentShape == selectedShape)
				{
					this.selectColor = this.invertColor(currentShape.getColor());
					g2d.setColor(this.selectColor);
					g2d.setStroke(new BasicStroke(SELECT_STROKE));
					g2d.drawOval(ellipse.getUpperLeftCorner().x, ellipse.getUpperLeftCorner().y, ellipse.getWidth(), ellipse.getHeight());
					g2d.setStroke(new BasicStroke());
					this.selectedShapeType = ShapeType.ELLIPSE;
				}
			}
			else if (currentShape instanceof Triangle)
			{
				int numPoints = 3;

				Triangle triangle = (Triangle) currentShape;
				int[] xArr = new int[numPoints];
				xArr[0] = triangle.getPointA().x;
				xArr[1] = triangle.getPointB().x;
				xArr[2] = triangle.getPointC().x;

				int[] yArr = new int[numPoints];
				yArr[0] = triangle.getPointA().y;
				yArr[1] = triangle.getPointB().y;
				yArr[2] = triangle.getPointC().y;

				g2d.setColor(triangle.getColor());
				g2d.fillPolygon(xArr, yArr, numPoints);

				if (currentShape == selectedShape)
				{
					this.selectColor = this.invertColor(currentShape.getColor());
					g2d.setColor(this.selectColor);
					g2d.setStroke(new BasicStroke(SELECT_STROKE));
					g2d.drawPolygon(xArr, yArr, numPoints);
					g2d.setStroke(new BasicStroke());
					this.selectedShapeType = ShapeType.TRIANGLE;
				}
			}

			g2d.rotate(-currentShape.getRotation());
			g2d.translate(-currentShape.getXOffset(), -currentShape.getYOffset());
			currentShape = null;
		}

		if (this.selectedShape != null)
		{
			this.selectColor = this.invertColor(this.selectedShape.getColor());
			g2d.setColor(this.selectColor);
			g2d.setStroke(new BasicStroke(SELECT_STROKE));
			g2d.translate(this.selectedShape.getXOffset(), this.selectedShape.getYOffset());
			g2d.rotate(this.selectedShape.getRotation());
			this.drawHandles(g2d);
			g2d.translate(-this.selectedShape.getXOffset(), -this.selectedShape.getYOffset());
			g2d.rotate(-this.selectedShape.getRotation());
			g2d.setStroke(new BasicStroke());
			g2d.setColor(this.selectedShape.getColor());
		}
	}

	public void drawHandles(Graphics2D g2d)
	{
		int hw = 0;
		int hh = 0;

		switch (selectedShapeType)
		{
			case NONE :

				break;

			case LINE :
				Line line = (Line) this.selectedShape;
				g2d.drawOval(line.getStart().x - this.HANDLE_OFFSET, line.getStart().y - this.HANDLE_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				g2d.drawOval(line.getEnd().x - this.HANDLE_OFFSET, line.getEnd().y - this.HANDLE_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				break;

			case RECTANGLE :
				Rectangle rectangle = (Rectangle) this.selectedShape;
				hw = rectangle.getHalfWidth();
				hh = rectangle.getHalfHeight();
				g2d.drawOval(-hw - this.HANDLE_OFFSET, -hh - this.HANDLE_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				g2d.drawOval(-hw - this.HANDLE_OFFSET, hh - this.HANDLE_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				g2d.drawOval(hw - this.HANDLE_OFFSET, -hh - this.HANDLE_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				g2d.drawOval(hw - this.HANDLE_OFFSET, hh - this.HANDLE_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				g2d.setColor(this.ROTATION_HANDLE_COLOR);
				g2d.drawOval(-this.HANDLE_OFFSET, -hh - View.ROTATION_HANDLE_Y_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				break;

			case ELLIPSE :
				Ellipse ellipse = (Ellipse) this.selectedShape;
				hw = ellipse.getHalfWidth();
				hh = ellipse.getHalfHeight();
				g2d.drawOval(-hw - this.HANDLE_OFFSET, -hh - this.HANDLE_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				g2d.drawOval(-hw - this.HANDLE_OFFSET, hh - this.HANDLE_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				g2d.drawOval(hw - this.HANDLE_OFFSET, -hh - this.HANDLE_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				g2d.drawOval(hw - this.HANDLE_OFFSET, hh - this.HANDLE_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				if (!(this.selectedShape instanceof Circle))
				{
					g2d.setColor(this.ROTATION_HANDLE_COLOR);
					g2d.drawOval(-this.HANDLE_OFFSET, -hh - View.ROTATION_HANDLE_Y_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				}
				break;

			case TRIANGLE :
				Triangle triangle = (Triangle) this.selectedShape;
				int highestY = Math.max(Math.abs(triangle.getPointA().y),
						Math.max(Math.abs(triangle.getPointB().y), Math.abs(triangle.getPointC().y)));
				g2d.drawOval(triangle.getPointA().x - this.HANDLE_OFFSET, triangle.getPointA().y - this.HANDLE_OFFSET, this.HANDLE_WIDTH,
						this.HANDLE_WIDTH);
				g2d.drawOval(triangle.getPointB().x - this.HANDLE_OFFSET, triangle.getPointB().y - this.HANDLE_OFFSET, this.HANDLE_WIDTH,
						this.HANDLE_WIDTH);
				g2d.drawOval(triangle.getPointC().x - this.HANDLE_OFFSET, triangle.getPointC().y - this.HANDLE_OFFSET, this.HANDLE_WIDTH,
						this.HANDLE_WIDTH);
				g2d.setColor(this.ROTATION_HANDLE_COLOR);
				g2d.drawOval(-this.HANDLE_OFFSET, -highestY - View.ROTATION_HANDLE_Y_OFFSET, this.HANDLE_WIDTH, this.HANDLE_WIDTH);
				break;

			default :
				break;

		}
	}

	public Color invertColor(Color color)
	{
		if (!color.equals(Color.white))
		{
			return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
		}
		else return this.WHITE_SELECT_COLOR;
	}

}
