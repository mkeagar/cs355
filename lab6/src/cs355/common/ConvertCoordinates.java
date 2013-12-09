package cs355.common;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import cs355.Camera;
import cs355.Point3D;
import cs355.lab6.Controller;
import cs355.model.Shape;

public abstract class ConvertCoordinates
{
	// Variables
	private static Controller controller = Controller.inst();
	private static final float nearPlane = 1.0f;
	private static final float farPlane = 250.0f;

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

	public static double[] threeDWorldToClip(Point3D point)
	{
		Camera camera = Controller.inst().getCamera();
		float theta = camera.getYaw();
		double c_x = camera.getLocation().x;
		double c_y = camera.getLocation().y;
		double c_z = camera.getLocation().z;
		double e = (farPlane + nearPlane) / (farPlane - nearPlane);
		double f = (-2 * nearPlane * farPlane) / (farPlane - nearPlane);
		double zoom = (1 / Math.tan(Math.PI / 6));

//		double x = (-c_x * zoom) * cos(theta) + zoom * point.x * cos(theta) + c_z * zoom * sin(theta) - zoom * point.z * sin(theta);
//		double y = zoom * point.y - c_y * zoom;
//		double z = (f) + (e) * point.z * cos(theta) - c_z * (e) * cos(theta) + e * point.x * sin(theta) - c_x * (e) * sin(theta);
//		double bigW = -c_z * cos(theta) + point.z * cos(theta) - c_x * sin(theta) + point.x * sin(theta);

		 double x = (sqrt(3) * point.x * cos(theta) + sqrt(3) * point.z * sin(theta) + sqrt(3) * (-c_x * cos(theta) - c_z * sin(theta)));
		 double y = (sqrt(3) * point.y - sqrt(3) * c_y);
		 double z = (f + e * point.z * cos(theta) - e * x * sin(theta) + e * (c_x * sin(theta) - c_z * cos(theta)));
		 double bigW = (-c_z * cos(theta) + point.z * cos(theta) + c_x * sin(theta) - point.x * sin(theta));

		double[] result = {x, y, z, bigW};

		return result;
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

	public static boolean clipTest(double[] start, double[] end)
	{
		double startX = start[0];
		double startY = start[1];
		double startZ = start[2];
		double startW = start[3];

		double endX = end[0];
		double endY = end[1];
		double endZ = end[2];
		double endW = end[3];

		if ((startX > startW && endX > endW) || (startX < -startW && endX < -endW)) return true;

		if ((startY > startW && endY > endW) || (startY < -startW && endY < -endW)) return true;

		if ((startZ > startW && endZ > endW)) return true;

		if (startZ <= -startW || endZ <= -endW) return true;

		return false;
	}

}
