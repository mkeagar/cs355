package cs355.common;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import cs355.Camera;
import cs355.Point3D;
import cs355.lab5.Controller;
import cs355.model.Shape;

public abstract class ConvertCoordinates
{
	// Variables
	private static Controller controller = Controller.inst();
	private static final float nearPlane = 1.0f;
	private static final float farPlane = 100.0f;

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

	public static Point3D threeDWorldToClip(Point3D point)
	{
		Camera camera = Controller.inst().getCamera();
		float theta = camera.getYaw();
		double c_x = camera.getLocation().x;
		double c_y = camera.getLocation().y;
		double c_z = camera.getLocation().z;

		double x = (sqrt(3) * point.x * cos(theta) + sqrt(3) * point.z * sin(theta) + sqrt(3) * (-c_x * cos(theta) - c_z * sin(theta)))
				/ (-c_z * cos(theta) + point.z * cos(theta) + c_x * sin(theta) - point.x * sin(theta));
		double y = (sqrt(3) * point.y - sqrt(3) * c_y) / (-c_z * cos(theta) + point.z * cos(theta) + c_x * sin(theta) - point.x * sin(theta));
		double z = ((-2 * nearPlane * farPlane / (farPlane - nearPlane)) + ((farPlane + nearPlane) / (farPlane - nearPlane)) * point.z * cos(theta)
				- ((farPlane + nearPlane) / (farPlane - nearPlane)) * x * sin(theta) + ((farPlane + nearPlane) / (farPlane - nearPlane))
				* (c_x * sin(theta) - c_z * cos(theta)))
				/ (-c_z * cos(theta) + point.z * cos(theta) + c_x * sin(theta) - point.x * sin(theta));

		return new Point3D(x, y, z);
	}

	public static Point3D clipToScreen(Point3D point)
	{
		double scale = controller.getScaleFactor();
		double w_x = controller.getWorldViewDiff().getX();
		double w_y = controller.getWorldViewDiff().getY();

		double x = -w_x + (1024 + 1024 * point.x) * scale;
		double y = -w_y + 1024 * scale - 1024 * point.y * scale;

		return new Point3D(x, y, 1);
	}

	public static boolean clipTest(Point3D start, Point3D end)
	{
		if ((start.x > 1.0 && end.x > 1.0) || (start.x < -1.0 && end.x < -1.0)) return true;

		if ((start.y > 1.0 && end.y > 1.0) || (start.y < -1.0 && end.y < -1.0)) return true;

		if ((start.z > 1.0 && end.z > 1.0)) return true;

		if (start.z <= 0.0 || end.z <= 0.0) return true;

		return false;
	}

}
