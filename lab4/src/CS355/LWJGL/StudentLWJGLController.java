package CS355.LWJGL;

//You might notice a lot of imports here.
//You are probably wondering why I didn't just import org.lwjgl.opengl.GL11.*
//Well, I did it as a hint to you.
//OpenGL has a lot of commands, and it can be kind of intimidating.
//This is a list of all the commands I used when I implemented my project.
//Therefore, if a command appears in this list, you probably need it.
//If it doesn't appear in this list, you probably don't.
//Of course, your mileage may vary. Don't feel restricted by this list of imports.
import java.util.Iterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.test.applet.OpenGL;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 *
 * @author Brennan Smith
 */
public class StudentLWJGLController implements CS355LWJGLController 
{
	// Variables
	private Camera camera = new Camera(new Point3D(0f, -5f, -25f));
	private final float zNearPlane = 0.001f;
	private final float zFarPlane = 1000.0f;
	private final float aspectRatio = (float) LWJGLSandbox.DISPLAY_WIDTH / (float) LWJGLSandbox.DISPLAY_HEIGHT;
	private final float fieldOfView = 45.0f;
	private final float orthoNeg = -10.0f;
	private final float orthoPos = 10.0f;
	private boolean perspective = true;

	// This is a model of a house.
	// It has a single method that returns an iterator full of Line3Ds.
	// A "Line3D" is a wrapper class around two Point3Ds.
	// It should all be fairly intuitive if you look at those classes.
	// If not, I apologize.
	private final WireFrame model = new HouseModel();

	// This method is called to "resize" the viewport to match the screen.
	// When you first start, have it be in perspective mode.
	@Override
	public void resizeGL()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		if (this.perspective)
		{
			gluPerspective(this.fieldOfView, this.aspectRatio, this.zNearPlane, this.zFarPlane);
		}
		else
		{
			glOrtho(this.orthoNeg, this.orthoPos, this.orthoNeg, this.orthoPos, this.zNearPlane, this.zFarPlane);
		}
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	@Override
	public void update()
	{

	}

	// This is called every frame, and should be responsible for keyboard updates.
	// An example keyboard event is captured below.
	// The "Keyboard" static class should contain everything you need to finish
	// this up.
	@Override
	public void updateKeyboard()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8))
		{
			this.camera.moveForward(1.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4))
		{
			this.camera.strafe(1.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2))
		{
			this.camera.moveBackward(1.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6))
		{
			this.camera.strafe(-1.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7))
		{
			this.camera.yaw(-1.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9))
		{
			this.camera.yaw(1.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_R) || Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT))
		{
			this.camera.changeAltitude(-1.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F) || Keyboard.isKeyDown(Keyboard.KEY_ADD))
		{
			this.camera.changeAltitude(1.0f);
		}
		// if (Keyboard.isKeyDown(Keyboard.KEY_Z) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1))
		// {
		// this.camera.roll(1.0f);
		// }
		// if (Keyboard.isKeyDown(Keyboard.KEY_C) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3))
		// {
		// this.camera.roll(-1.0f);
		// }
		if (Keyboard.isKeyDown(Keyboard.KEY_H) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5))
		{
			this.camera = new Camera(new Point3D(0f, -5f, -25f));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_O) || Keyboard.isKeyDown(Keyboard.KEY_DIVIDE))
		{
			this.perspective = false;
			this.resizeGL();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_P) || Keyboard.isKeyDown(Keyboard.KEY_MULTIPLY))
		{
			this.perspective = true;
			this.resizeGL();
		}
	}

	// This method is the one that actually draws to the screen.
	@Override
	public void render()
	{
		// This clears the screen.
		glClear(GL_COLOR_BUFFER_BIT);

		// load identity matrix
		glLoadIdentity();

		// move view to camera
		this.moveToCamera();

		// Get ready to draw lines
		glBegin(GL_LINES);

		// Set color
		glColor3f(0.2f, 1.0f, 0f);

		// Get lines from model
		Iterator<Line3D> modelIter = this.model.getLines();
		while (modelIter.hasNext())
		{
			// Draw all the lines
			Line3D line = modelIter.next();
			glVertex3d(line.start.x, line.start.y, line.start.z);
			glVertex3d(line.end.x, line.end.y, line.end.z);
		}

		// End drawing lines
		glEnd();
	}

	public void moveToCamera()
	{
		Point3D camLoc = this.camera.getLocation();
		float yaw = this.camera.getYaw();
		// float roll = this.camera.getRoll();

		glRotatef(yaw, 0.0f, 1.0f, 0.0f);
		// glRotatef(roll, 0.0f, 0.0f, 1.0f);

		glTranslatef((float) camLoc.x, (float) camLoc.y, (float) camLoc.z);
	}

}
