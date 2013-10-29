package cs355.common;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import cs355.lab3.Controller;
import cs355.model.Shape;

public abstract class ConvertCoordinates
{
	// Variables
	private static Controller controller = Controller.inst();

	// Methods
	public static Point objectToWorld(Point op, Shape shape)
	{
		double theta = shape.getRotation();
		double cX = shape.getXOffset();
		double cY = shape.getYOffset();

		AffineTransform objectWorld = new AffineTransform(cos(theta), sin(theta), -sin(theta), cos(theta), cX, cY);
		Point2D wp2d = objectWorld.transform(op, null);

		Point wp = new Point((int) wp2d.getX(), (int) wp2d.getY());

		return wp;
	}

	public static Point worldToObject(Point wp, Shape shape)
	{
		double theta = shape.getRotation();
		double cX = shape.getXOffset();
		double cY = shape.getYOffset();

		AffineTransform worldObject = new AffineTransform(cos(theta), -sin(theta), sin(theta), cos(theta), -cos(theta) * cX - sin(theta) * cY,
				sin(theta) * cX - cos(theta) * cY);
		Point2D op2d = worldObject.transform(wp, null);

		Point op = new Point((int) op2d.getX(), (int) op2d.getY());

		return op;
	}

	public static Point viewToWorld(Point vP)
	{
		double scaleFactor = controller.getScaleFactor();
		Point diff = controller.getWorldViewDiff();
		double zero = 0.0d;

		AffineTransform viewWorld = new AffineTransform(1 / scaleFactor, zero, zero, 1 / scaleFactor, diff.x, diff.y);
		Point2D wp2d = viewWorld.transform(vP, null);
		Point wp = new Point((int) wp2d.getX(), (int) wp2d.getY());

		return wp;
	}

	public static Point worldToView(Point wP)
	{
		double scaleFactor = controller.getScaleFactor();
		Point diff = controller.getWorldViewDiff();
		double zero = 0.0d;

		AffineTransform worldView = new AffineTransform(scaleFactor, zero, zero, scaleFactor, -scaleFactor * diff.x, -scaleFactor * diff.y);

		Point2D vp2d = worldView.transform(wP, null);
		Point vp = new Point((int) vp2d.getX(), (int) vp2d.getY());

		return vp;
	}

}
