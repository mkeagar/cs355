package cs355.lab2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

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
			Shape selectedShape = Controller.inst().getSelectedShape();
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
					Color selectColor = this.invertColor(currentShape.getColor());
					if (line.getColor() == Color.WHITE) selectColor = new Color(255, 0, 0);
					g2d.setColor(selectColor);
					g2d.drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y);
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
					Color selectColor = this.invertColor(currentShape.getColor());
					if (rectangle.getColor() == Color.WHITE) selectColor = new Color(255, 0, 0);
					g2d.setColor(selectColor);
					g2d.setStroke(new BasicStroke(3));
					g2d.drawRect(rectangle.getUpperLeftCorner().x, rectangle.getUpperLeftCorner().y, rectangle.getWidth(), rectangle.getHeight());
					g2d.setStroke(new BasicStroke());
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
					Color selectColor = this.invertColor(currentShape.getColor());
					if (ellipse.getColor() == Color.WHITE) selectColor = new Color(255, 0, 0);
					g2d.setColor(selectColor);
					g2d.setStroke(new BasicStroke(3));
					g2d.drawOval(ellipse.getUpperLeftCorner().x, ellipse.getUpperLeftCorner().y, ellipse.getWidth(), ellipse.getHeight());
					g2d.setStroke(new BasicStroke());
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
					Color selectColor = this.invertColor(currentShape.getColor());
					if (triangle.getColor() == Color.WHITE) selectColor = new Color(255, 0, 0);
					g2d.setColor(selectColor);
					g2d.setStroke(new BasicStroke(3));
					g2d.drawPolygon(xArr, yArr, numPoints);
					g2d.setStroke(new BasicStroke());
				}
			}

			g2d.rotate(-currentShape.getRotation());
			g2d.translate(-currentShape.getXOffset(), -currentShape.getYOffset());
			currentShape = null;
		}
	}

	public Color invertColor(Color color)
	{
		return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
	}

}
