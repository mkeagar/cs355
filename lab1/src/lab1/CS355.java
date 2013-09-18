/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs355.lab1;

import cs355.GUIFunctions;
import java.awt.Color;

/**
 *
 * @author <Put your name here>
 */
public class CS355 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        GUIFunctions.createCS355Frame(null,null,null,null);
        GUIFunctions.refresh();
        GUIFunctions.changeSelectedColor(Color.WHITE);
        GUIFunctions.setHScrollBarMin(0);
        GUIFunctions.setVScrollBarMin(0);
        GUIFunctions.setHScrollBarMax(512);
        GUIFunctions.setVScrollBarMax(512);
        GUIFunctions.setHScrollBarKnob(256);
        GUIFunctions.setVScrollBarKnob(256);
    }
}