/*
 *  SimpleRenderer.java
 *  ARToolKit5
 *
 *  Disclaimer: IMPORTANT:  This Daqri software is supplied to you by Daqri
 *  LLC ("Daqri") in consideration of your agreement to the following
 *  terms, and your use, installation, modification or redistribution of
 *  this Daqri software constitutes acceptance of these terms.  If you do
 *  not agree with these terms, please do not use, install, modify or
 *  redistribute this Daqri software.
 *
 *  In consideration of your agreement to abide by the following terms, and
 *  subject to these terms, Daqri grants you a personal, non-exclusive
 *  license, under Daqri's copyrights in this original Daqri software (the
 *  "Daqri Software"), to use, reproduce, modify and redistribute the Daqri
 *  Software, with or without modifications, in source and/or binary forms;
 *  provided that if you redistribute the Daqri Software in its entirety and
 *  without modifications, you must retain this notice and the following
 *  text and disclaimers in all such redistributions of the Daqri Software.
 *  Neither the name, trademarks, service marks or logos of Daqri LLC may
 *  be used to endorse or promote products derived from the Daqri Software
 *  without specific prior written permission from Daqri.  Except as
 *  expressly stated in this notice, no other rights or licenses, express or
 *  implied, are granted by Daqri herein, including but not limited to any
 *  patent rights that may be infringed by your derivative works or by other
 *  works in which the Daqri Software may be incorporated.
 *
 *  The Daqri Software is provided by Daqri on an "AS IS" basis.  DAQRI
 *  MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 *  THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS
 *  FOR A PARTICULAR PURPOSE, REGARDING THE DAQRI SOFTWARE OR ITS USE AND
 *  OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 *  IN NO EVENT SHALL DAQRI BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL
 *  OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION,
 *  MODIFICATION AND/OR DISTRIBUTION OF THE DAQRI SOFTWARE, HOWEVER CAUSED
 *  AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE),
 *  STRICT LIABILITY OR OTHERWISE, EVEN IF DAQRI HAS BEEN ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *
 *  Copyright 2015 Daqri, LLC.
 *  Copyright 2011-2015 ARToolworks, Inc.
 *
 *  Author(s): Julian Looser, Philip Lamb
 *
 */

package org.artoolkit.ar.samples.ARSimple;

import android.util.Log;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.NativeInterface;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.base.rendering.Cube;
import org.artoolkit.ar.base.rendering.Line;

import javax.microedition.khronos.opengles.GL10;

/**
 * A very simple Renderer that adds a marker and draws a cube on it.
 */
public class SimpleRenderer extends ARRenderer {

    private int markerID = -1;
    private int markerID2 = -1;
    private Cube cube = new Cube(40.0f, 0.0f, 0.0f, 20.0f);
    private Cube cube2 = new Cube(1.0f, 0.0f, 0.0f, 0.5f);
    /**
     * Markers can be configured here.
     */
    @Override
    public boolean configureARScene() {

        markerID = ARToolKit.getInstance().addMarker("nft;Data/pinball");
        markerID2 = ARToolKit.getInstance().addMarker("nft;Data/003-022");
        ARToolKit.getInstance().setMarkerOptionFloat(markerID2, NativeInterface.ARW_MARKER_OPTION_FILTER_CUTOFF_FREQ,30); // ?? Is this working ??
        ARToolKit.getInstance().setMarkerOptionFloat(markerID, NativeInterface.ARW_MARKER_OPTION_FILTER_CUTOFF_FREQ,30); // ?? Is this working ??
        if (markerID < 0) return false;

        return true;
    }

    /**
     * Override the draw function from ARRenderer.
     */
    @Override
    public void draw(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Apply the ARToolKit projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);

        //Rotate it by 90 degrees
        gl.glLoadIdentity();
        gl.glRotatef(90.0f, 0.0f, 0.0f, -1.0f);
        gl.glMultMatrixf(ARToolKit.getInstance().getProjectionMatrix(), 0);

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glFrontFace(GL10.GL_CW);

        // If the marker is visible, apply its transformation, and draw a cube
        if (ARToolKit.getInstance().queryMarkerVisible(markerID)) {
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadMatrixf(ARToolKit.getInstance().queryMarkerTransformation(markerID), 0);

            int pattID = ARToolKit.getInstance().getMarkerPatternCount(markerID);


            if(pattID!=0) {
                float[] matrix = new float[16];
                float[] height = new float[1];
                float[] width = new float[1];
                int[] imsX = new int[1];
                int[] imsY = new int[1];
                ARToolKit.getInstance().getMarkerPatternConfig(markerID,pattID-1,matrix,width,height,imsX,imsY);
                Log.d("Pattern","width = " + width[0] + "\n" +
                        "height = " + height[0] + "\n" +
                        "imsX =" + imsX[0] + "\n" +
                        "imsY =" + imsY[0] + "\n");

                for (int i = 0; i < 16;i++) {
                    Log.d("Matrix","mat[" + i +"] = " + matrix[i]);
                }
                gl.glTranslatef(width[0]/2.0f,height[0]/2.0f,0.0f);
                gl.glScalef(width[0]*0.90f,height[0]*0.90f,1.0f);

            }
            cube2.draw(gl);
        }
        if (ARToolKit.getInstance().queryMarkerVisible(markerID2)) {

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadMatrixf(ARToolKit.getInstance().queryMarkerTransformation(markerID2), 0);
            int pattID = ARToolKit.getInstance().getMarkerPatternCount(markerID2);


            if(pattID!=0) {
                float[] matrix = new float[16];
                float[] height = new float[1];
                float[] width = new float[1];
                int[] imsX = new int[1];
                int[] imsY = new int[1];
                ARToolKit.getInstance().getMarkerPatternConfig(markerID2,pattID-1,matrix,width,height,imsX,imsY);
                Log.d("Pattern","width = " + width[0] + "\n" +
                                    "height = " + height[0] + "\n" +
                                    "imsX =" + imsX[0] + "\n" +
                                    "imsY =" + imsY[0] + "\n");

                for (int i = 0; i < 16;i++) {
                    Log.d("Matrix","mat[" + i +"] = " + matrix[i]);
                }
                gl.glTranslatef(width[0]/2.0f,height[0]/2.0f,1);
                gl.glScalef(width[0],height[0],1);

            }
            cube2.draw(gl);
        }

    }
}