/***
 * Neuroph  http://neuroph.sourceforge.net
 * Copyright by Neuroph Project (C) 2008
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Neuroph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */

package org.neuroph.contrib.imgrec;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * This class uses a given Robot object to sample images from the screen at an 
 * arbitrary sampling resolution.  The coordinates of the scanning rectangle 
 * is (x, y) expressed as a fraction of the screen resolution, with the width also being
 * a fraction.
 * 
 * For example, when the screen is 800x600 resolution, to scan a rectangle with
 * xy coordinates (400,300) to xy (600, 400), use the following rectangle as an argument:
 * 
 * new Rectangle2D.Double(0.5, 0.5, 0.25, 0.16667)
 * 
 * Scanning using sampling is faster than scanning using a screenshot, but is
 * prone to introduce tearing and shearing into the scanned image.  Scanning
 * from a screenshot has no tearing or shearing at the cost of speed.
 *
 * Also provides method for downsampling (scaling) image.
 * This class is based on the code from tileclassification by Jon Tait.
 * 
 * @author Jon Tait
 *
 */
public class ImageSampler
{
	/**
	 * Scans screen location using screenshot
	 * @param robot
	 * @param rectangleAsDecimalPercent
	 * @param samplingResolution
	 * @return image sample from the specified location
	 */
	public static BufferedImage scanLocationUsingScreenshot(Robot robot,
			Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution)
	{
		return scanLocationUsingScreenshot(robot, rectangleAsDecimalPercent, samplingResolution, BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * Scans screen location using screenshot
	 * @param robot
	 * @param rectangleAsDecimalPercent
	 * @param samplingResolution
	 * @param imageType
	 * @return image sample from the specified location
	 */
	public static BufferedImage scanLocationUsingScreenshot(Robot robot,
			Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution,
			int imageType)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int inspectX = (int) Math.round((screenSize.width * rectangleAsDecimalPercent.x));
		int inspectY = (int) Math.round((screenSize.height * rectangleAsDecimalPercent.y));
		int inspectWidth = (int) Math.round((screenSize.width * rectangleAsDecimalPercent.width));
		int inspectHeight = (int) Math.round((screenSize.height * rectangleAsDecimalPercent.height));
		
		Rectangle screenRect = new Rectangle(inspectX, inspectY, inspectWidth, inspectHeight);
		BufferedImage screenCapture = robot.createScreenCapture(screenRect);
		
		
		return downSampleImage(samplingResolution, screenCapture, imageType);
	}

        /**
         * Scales image to the specified dimension
         * @param samplingResolution sampling resolution/image size
         * @param bigImg image to scale
         * @return image scaled/downsampled to the specified dimension/resolution
         */
	public static BufferedImage downSampleImage(
			Dimension samplingResolution, BufferedImage bigImg) {
		return downSampleImage(samplingResolution, bigImg, BufferedImage.TYPE_INT_RGB);
	}


        /**
         * Scales image to the specified dimension
         * @param samplingResolution sampling resolution/image size
         * @param bigImg image to scale
         * @param returnImageType type of the return image
         * @return image scaled/downsampled to the specified dimension/resolution
         */
	public static BufferedImage downSampleImage(Dimension samplingResolution,
			BufferedImage bigImg, int returnImageType) {
		int inspectX;
		int inspectY;
		int numberOfSamplesAcross = samplingResolution.width;
		int numberOfSamplesDown = samplingResolution.height;
		
		if(bigImg.getWidth() <= numberOfSamplesAcross || bigImg.getHeight() <= numberOfSamplesDown) {
			return bigImg;
		}
		
		BufferedImage img = new BufferedImage(
				numberOfSamplesAcross, numberOfSamplesDown, 
				returnImageType);
		
		double samplingIncrementX = bigImg.getWidth() / (samplingResolution.getWidth() - 1);
		double samplingIncrementY = bigImg.getHeight() / (samplingResolution.getHeight() - 1);
		
		double sampleX = 0;
		double sampleY = 0;
		for(int y=0; y < numberOfSamplesDown; y++) {
			for(int x=0; x < numberOfSamplesAcross; x++) {
				inspectX = (int) Math.round(sampleX);
				inspectY = (int) Math.round(sampleY);
				
				if(inspectX >= bigImg.getWidth()) {
					inspectX = bigImg.getWidth() - 1;
				}
				if(inspectY >= bigImg.getHeight()) {
					inspectY = bigImg.getHeight() - 1;
				}
				int color = bigImg.getRGB(inspectX, inspectY);
				img.setRGB(x, y, color);
				sampleX+=samplingIncrementX;
			}
			sampleX=0;
			sampleY+=samplingIncrementY;
		}
		
		return img;
	}
	
	/**
	 * Scans screen location using sampling
	 * @param robot an instance of java.awt.Robot for the scaned screen
	 * @param rectangleAsDecimalPercent
	 * @param samplingResolution
	 * @return image sample from the specified location
	 */
	public static BufferedImage scanLocationUsingSampling(Robot robot,
			Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution)
	{
		return scanLocationUsingSampling(robot, rectangleAsDecimalPercent, samplingResolution, BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * Scans screen location using sampling
	 * @param robot an instance of java.awt.Robot for the scaned screen
	 * @param rectangleAsDecimalPercent
	 * @param samplingResolution
	 * @param imageType
	 * @return image sample from the specified location
	 */
	public static BufferedImage scanLocationUsingSampling(Robot robot,
			Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution,
			int imageType)
	{
		double slotULXAsDecimalPercent = rectangleAsDecimalPercent.x; 
		double slotULYAsDecimalPercent = rectangleAsDecimalPercent.y;
		double sampleIncrementAcrossAsDecimalPercent = rectangleAsDecimalPercent.width / (samplingResolution.getWidth()-1);
		double sampleIncrementDownAsDecimalPercent = rectangleAsDecimalPercent.height / (samplingResolution.getHeight()-1);
		
		int numberOfSamplesAcross = samplingResolution.width;
		int numberOfSamplesDown = samplingResolution.height;
		BufferedImage img = new BufferedImage(
				numberOfSamplesAcross, numberOfSamplesDown, 
				imageType);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		waitForVerticalScreenRefresh();
		
		double inspectXAsDecimalPercent = slotULXAsDecimalPercent;
		double inspectYAsDecimalPercent = slotULYAsDecimalPercent;
		for(int y=0; y < numberOfSamplesDown; y++) {
			for(int x=0; x < numberOfSamplesAcross; x++) {
				int inspectX = (int) Math.round((screenSize.width * inspectXAsDecimalPercent));
				int inspectY = (int) Math.round((screenSize.height * inspectYAsDecimalPercent));
				Color color = robot.getPixelColor(inspectX, inspectY);
				img.setRGB(x, y, color.getRGB());
				inspectXAsDecimalPercent+=sampleIncrementAcrossAsDecimalPercent;
			}
			inspectXAsDecimalPercent = slotULXAsDecimalPercent;
			inspectYAsDecimalPercent+=sampleIncrementDownAsDecimalPercent;
		}
		return img;
	}

	/**
	 * Another name for this is "Vertical Sync".  Minimizes frame shearing.
	 */
	private static void waitForVerticalScreenRefresh()
	{
		Toolkit.getDefaultToolkit().sync();
	}
}
