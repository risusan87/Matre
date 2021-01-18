package jp.risu87.matre;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class MapImage extends BufferedImage {

	public static final byte[] BASE_COLORS = {
		(byte)0, (byte)0, (byte)0, (byte)127, (byte)(178), (byte)56, (byte)(247), (byte)(233), 
		(byte)(163), (byte)(199), (byte)(199), (byte)(199), (byte)(255), (byte)0, (byte)0, (byte)(160),
		(byte)(160), (byte)(255),(byte)(167), (byte)(167), (byte)(167), (byte)0, (byte)124, (byte)0,
		(byte)(255), (byte)(255), (byte)(255), (byte)(164), (byte)(168), (byte)(184), (byte)(151),
		(byte)109, (byte)77, (byte)112, (byte)112, (byte)112, (byte)64, (byte)64, (byte)(255), 
		(byte)(143), (byte)119, (byte)72, (byte)(255), (byte)(252), (byte)(245), (byte)(216),
		(byte)127, (byte)51, (byte)(178), (byte)76, (byte)(216), (byte)102, (byte)(153), (byte)(216),
		(byte)(229), (byte)(229), (byte)51, (byte)127, (byte)(204), (byte)25, (byte)(242), (byte)127,
		(byte)(165), (byte)76, (byte)76, (byte)76, (byte)(153), (byte)(153), (byte)(153), (byte)76, 
		(byte)127, (byte)(153), (byte)127, (byte)63, (byte)(178), (byte)51, (byte)76, (byte)(178), 
		(byte)102, (byte)76, (byte)51, (byte)102, (byte)127, (byte)51, (byte)(153), (byte)51, (byte)51,
		(byte)25, (byte)25, (byte)25, (byte)(250), (byte)(238), (byte)77, (byte)92, (byte)(219), 
		(byte)(213), (byte)74, (byte)(128), (byte)(255), (byte)0, (byte)(217), (byte)58, (byte)(129),
		(byte)86, (byte)49, (byte)112, (byte)2, (byte)0, (byte)(209), (byte)(177), (byte)(161), 
		(byte)(159), (byte)82, (byte)36, (byte)(149), (byte)87, (byte)108, (byte)112, (byte)108,
		(byte)(138), (byte)(186), (byte)(133), (byte)36, (byte)103, (byte)117, (byte)53, (byte)(160),
		(byte)77, (byte)78, (byte)57, (byte)41, (byte)35, (byte)(135), (byte)107, (byte)98, (byte)87,
		(byte)92, (byte)92, (byte)122, (byte)73, (byte)88, (byte)76, (byte)62, (byte)92, (byte)76,
		(byte)50, (byte)35, (byte)76, (byte)82, (byte)42, (byte)(142), (byte)60, (byte)46, (byte)37,
		(byte)22, (byte)16
	};
	
	public static byte[] getBaseColor(int par1colorId) {
		int index = par1colorId * 3;
		return new byte[] {BASE_COLORS[index], BASE_COLORS[index + 1], BASE_COLORS[index + 2]};
	}
	
	public final int[] index;
	/**
	 * 
	 * @param par1colorId - base color id
	 * @param par2b - 0:darker, 1:lighter, 2:lightest(base color), 3:darkest
	 * @return
	 */
	public static byte[] getMapColor(int par1index) {
		int base_color = Math.floorDiv(par1index, 4);
		int b = par1index % 4;
		byte[] bc = getBaseColor(base_color);
		switch (b) {
		case 0: return new byte[] {
					(byte)Math.floor(180 / 255f * (bc[0]&0xff)),
					(byte)Math.floor(180 / 255f * (bc[1]&0xff)),
					(byte)Math.floor(180 / 255f * (bc[2]&0xff))
				};
		case 1: return new byte[] {
				(byte)Math.floor(220 / 255f * (bc[0]&0xff)),
				(byte)Math.floor(220 / 255f * (bc[1]&0xff)),
				(byte)Math.floor(220 / 255f * (bc[2]&0xff))
			};
		case 2: return new byte[] {
				(byte)Math.floor(bc[0]),
				(byte)Math.floor(bc[1]),
				(byte)Math.floor(bc[2])
			};
		case 3: return new byte[] {
				(byte)Math.floor(135 / 255f * (bc[0]&0xff)),
				(byte)Math.floor(135 / 255f * (bc[1]&0xff)),
				(byte)Math.floor(135 / 255f * (bc[2]&0xff))
			};
		default:
			return null;
		}
	}
	
	public MapImage(BufferedImage par1rawImg) {
		super(par1rawImg.getWidth(), par1rawImg.getHeight(), BufferedImage.TYPE_INT_RGB);
		this.setData(par1rawImg.getData());
		this.index = new int[this.getWidth() * this.getHeight()];
		
		int progress_tmp = -1;
		for (int h = 0; h < this.getHeight(); h++) for (int w = 0; w < this.getWidth(); w++) {
			this.setRGB(w, h, this.getClosestRGBMatch(this.getRGB(w, h), w, h));
			int progress = (int)(((float)h / this.getHeight()) * 100.f);
			if (progress_tmp + 1 == progress) {
				System.out.println(progress + "% converted");
				progress_tmp = progress;
			}
		}
		System.out.println("Conversion done!");
	}
	
	public MapImage(Raster par1part, int[] par2index) {
		super(par1part.getWidth(), par1part.getHeight(), BufferedImage.TYPE_INT_RGB);
		this.setData(par1part);
		this.index = par2index;
	}
	
	private int getClosestRGBMatch(int par1originalRBG, int par2width, int par3height) {
		int index = 0;
		int closest = -1;
		for (int i = 0; i < 204; i++) {
			byte[] map_color = getMapColor(i);
			int mR = map_color[0]&0xff;
			int mG = map_color[1]&0xff;
			int mB = map_color[2]&0xff;
			int R = (par1originalRBG >> 16)&0xff;
			int G = (par1originalRBG >> 8)&0xff;
			int B = (par1originalRBG >> 0)&0xff;
			int distance = (int)Math.sqrt(
					(int)Math.pow(Math.abs(mR - R), 2) +
					(int)Math.pow(Math.abs(mG - G), 2) +
					(int)Math.pow(Math.abs(mB - B), 2));
			if (closest > distance || i == 0) {
				closest = distance;
				index = i;
			}
		}
		
		this.index[par3height * this.getWidth() + par2width] = index;
		byte[] mc = getMapColor(index);
		return new Color(mc[0]&0xff, mc[1]&0xff, mc[2]&0xff).getRGB();
	}
	
	public MapImage getChildImage(int par1Px, int par2Py, int par3width, int par4height) {
		Raster part = this.getData().createChild(par1Px, par2Py, par3width, par4height, 0, 0, null);
		int[] newIndex = new int[par3width * par4height];
		int i = 0;
		for (int h = par2Py; h < par2Py + par4height; h++)
			for (int w = par1Px; w < par1Px + par3width; w++) {
				newIndex[i] = this.index[h * this.getWidth() + w];
				i++;
			}
		return new MapImage(part, newIndex);
	}
}


