/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package cs355;

import java.awt.Color;

/**
 * 
 * @author Talonos
 */
public interface CS355Controller
{

	void circleButtonHit();

	void colorButtonHit(Color c);

	void ellipseButtonHit();

	public void hScrollbarChanged(int value);

	public void lineButtonHit();

	void rectangleButtonHit();

	public void selectButtonHit();

	void squareButtonHit();

	void triangleButtonHit();

	public void vScrollbarChanged(int value);

	public void zoomInButtonHit();

	public void zoomOutButtonHit();
}
