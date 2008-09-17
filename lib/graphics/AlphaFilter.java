/**
 * Alpha Filter Class
 * AlphaFilter.java
 * From: http://docs.rinet.ru/JaTricks/ch12.htm
 * Modified only to meet code style guidelines.
 */

package graphics;

import java.awt.image.RGBImageFilter;

public class AlphaFilter extends RGBImageFilter {
	
	private int alphaLevel;

	public AlphaFilter (int alpha) {
		alphaLevel = alpha;
		canFilterIndexColorModel = true;
	}

	public int filterRGB (int x, int y, int rgb) {
		// Adjust the alpha value
		int alpha = (rgb >> 24) & 0xff;
		alpha = (alpha * alphaLevel) / 255;
		
		// Return the result
		return ((rgb & 0x00ffffff) | (alpha << 24));
	}
}
