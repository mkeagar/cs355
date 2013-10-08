package cs355.lab1;

import java.awt.Graphics2D;
import java.awt.Point;

import cs355.ViewRefresher;
import cs355.model.*;

public class View implements ViewRefresher
{
	// Variables

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

	@Override
	public void refreshView(Graphics2D g2d)
	{
		for (int i = 0; i < Model.inst().size(); i++)
		{
			Shape currentShape = Model.inst().getShape(i);

			if (currentShape instanceof Line)
			{
				Line temp = (Line) currentShape;
				g2d.setColor(temp.getColor());
				g2d.drawLine(temp.getStart().x, temp.getStart().y, temp.getEnd().x, temp.getEnd().y);
				currentShape = null;
			}
			else
				if (currentShape instanceof Rectangle)
				{
					Rectangle rectangle = (Rectangle) currentShape;
					g2d.setColor(rectangle.getColor());
					g2d.fillRect(rectangle.getUpperLeftCorner().x, rectangle.getUpperLeftCorner().y, rectangle.getWidth(), rectangle.getHeight());
					currentShape = null;
				}
				else
					if (currentShape instanceof Ellipse)
					{
						Ellipse ellipse = (Ellipse) currentShape;
						Point upLeft = new Point(ellipse.getCenter().x - (ellipse.getWidth() / 2), ellipse.getCenter().y - (ellipse.getHeight() / 2));
						g2d.setColor(ellipse.getColor());
						g2d.fillOval(upLeft.x, upLeft.y, ellipse.getWidth(), ellipse.getHeight());
						currentShape = null;
					}
					else
						if (currentShape instanceof Triangle)
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
						}

		}
	}

}
