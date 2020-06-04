/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.birds;

import java.awt.Dimension;
import java.awt.Window;

/**
 *
 * @author Carl
 */
public class FrameUtil {

    public static void centerFrame(Window window) {
        Dimension screenSize = Util.getScreenResolution();
        window.setLocation((int) ((screenSize.getWidth() / 2) - (window.getWidth() / 2)), (int) ((screenSize.getHeight() / 2) - (window.getHeight() / 2)));
    }
}
