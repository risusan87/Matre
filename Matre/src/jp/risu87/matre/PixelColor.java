package jp.risu87.matre;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class PixelColor extends Color{
	
	public static final String COLOR_ID[] = {
		"GRASS", "SAND", "WOOL", "FIRE", "ICE", "METAL", "PLANT", "SNOW", "CLAY", "DIRT",
		"STONE", "WATER", "WOOD", "QUARTZ", "COLOR_ORANGE", "COLOR_MAGENTA", "COLOR_LIGHT_BLUE",
		"COLOR_YELLOW", "COLOR_LIGHT_GREEN", "COLOR_PINK", "COLOR_GRAY", "COLOR_LIGHT_GRAY",
		"COLOR_CYAN", "COLOR_PURPLE", "COLOR_BLUE", "COLOR_BROWN", "COLOR_GREEN", "COLOR_RED",
		"COLOR_BLACK", "GOLD", "DIAMOND", "LAPIS", "EMERALD", "PODZOL", "NETHER", "TERRACOTTA_WHITE",
		"TERRACOTTA_ORAGE", "TERRACOTTA_MAGENTA", "TERRACOTTA_LIGHT_BLUE", "TERRACOTTA_YELLOW",
		"TERRACOTTA_LIGHT_GREEN", "TERRACOTTA_PINK", "TERRACOTTA_GRAY", "TERRACOTTA_LIGHT_GRAY",
		"TERRACOTTA_CYAN", "TERRACOTTA_PURPLE", "TERRACOTTA_BLUE", "TERRACOTTA_BROWN", 
		"TERRACOTTA_GREEN", "TERRACOTTA_RED", "TERRACOTTA_BLACK"
	};
	
	public static final PixelColor colors[] = {
			new PixelColor(127, 178, 56), new PixelColor(247, 233, 163),
			new PixelColor(199, 199, 199), new PixelColor(255, 0, 0), new PixelColor(160, 160, 255),
			new PixelColor(167, 167, 167), new PixelColor(0, 124, 0), new PixelColor(255, 255, 255),
			new PixelColor(164, 168, 184), new PixelColor(151, 109, 77), new PixelColor(112, 112, 112),
			new PixelColor(64, 64, 255), new PixelColor(143, 119, 72), new PixelColor(255, 252, 245),
			new PixelColor(216, 127, 51), new PixelColor(178, 76, 216), new PixelColor(102, 153, 216),
			new PixelColor(229, 229, 51), new PixelColor(127, 204, 25), new PixelColor(242, 127, 165),
			new PixelColor(76, 76, 76), new PixelColor(153, 153, 153), new PixelColor(76, 127, 153),
			new PixelColor(127, 63, 178), new PixelColor(51, 76, 178), new PixelColor(102, 76, 51),
			new PixelColor(102, 127, 51), new PixelColor(153, 51, 51), new PixelColor(25, 25, 25),
			new PixelColor(250, 238, 77), new PixelColor(92, 219, 213), new PixelColor(74, 128, 255),
			new PixelColor(0, 217, 58), new PixelColor(129, 86, 49), new PixelColor(112, 2, 0),
			new PixelColor(209, 177, 161), new PixelColor(159, 82, 36), new PixelColor(149, 87, 108),
			new PixelColor(112, 108, 138), new PixelColor(186, 133, 36), new PixelColor(103, 117, 53),
			new PixelColor(160, 77, 78), new PixelColor(57, 41, 35), new PixelColor(135, 107, 98),
			new PixelColor(87, 92, 92), new PixelColor(122, 73, 88), new PixelColor(76, 62, 92),
			new PixelColor(76, 50, 35), new PixelColor(76, 82, 42), new PixelColor(142, 60, 46), 
			new PixelColor(37, 22, 16)
	};
	
	public static final String VALUE[] = { "LOW", "MID", "HIGH" };
	
	/**
	 * index 0 -> color_id
	 * index 1 -> value
	 * @param par1color
	 * @return
	 */
	public static String[] getPixelIdentification(Color par1color) {
		int index = -1;
		for (int i = 0; i < 153; i++) {
			if (totalColors[i].equals(par1color)) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			return null;
		}
		return new String[] {COLOR_ID[Math.floorDiv(index, 3)], VALUE[index % 3]};
	}
	
	/**
	 * any color image -> 151 color image
	 * @param par1src - src image
	 * @return converted image
	 */
	public static BufferedImage convertImage(String par1src) {
		BufferedImage b1;
		int progress_tmp = -1;
		try {
			b1 = ImageIO.read(new File(par1src));
			System.out.println("Starting conversion of image " + b1.getWidth() + " x " + b1.getHeight() + "...");
			for (int w = 0; w < b1.getWidth(); w++) {
				for (int h = 0; h < b1.getHeight(); h++) {
					int progress = (int)(((float)w / b1.getWidth()) * 100.f);
					if (progress_tmp + 1 == progress) {
						System.out.println(progress + "% converted");
						progress_tmp = progress;
					}
					int rgb = b1.getRGB(w, h);
					b1.setRGB
					(w, h, new PixelColor((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, (rgb) & 0xff).convert().getRGB());
				}
			}
			System.out.println("Conversion successfully finished!");
			return b1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Color totalColors[] = new Color[153];
	static {
		for (int i = 0; i < 51; i++) {
			totalColors[i * 3] = colors[i].getLowValue();
			totalColors[i * 3 + 1] = colors[i].getMidValue();
			totalColors[i * 3 + 2] = colors[i].getHighValue();
		}
	}
	
	public PixelColor(int par1int, int par2int, int par3int) {
		super(par1int, par2int, par3int);
	}
	
	public Color convert() {
		double dis[] = new double[153];
		
		for (int i = 0; i < 51; i++) {
			dis[i * 3] = this.get3Ddistance(i, 1);
			dis[i * 3 + 1] = this.get3Ddistance(i, 2);
			dis[i* 3 + 2] = this.get3Ddistance(i, 3);
		}
		
		int small = -1;
		for (int i = 0; i < 153; i++) {
			for (int j = 0; j < 153; j++) {
				if (dis[i] > dis[j])
					break;
				if (j == 152)
					small = i;
			}
		}
		
		int main = Math.floorDiv(small, 3);
		int value = small % 3;
		
		PixelColor c = colors[main];
		switch (value) {
		case 0:
			return c.getLowValue();
		case 1:
			return c.getMidValue();
		case 2:
			return c.getHighValue();
		}
		return null;
	}
	
	private double get3Ddistance(int par1arg, int par1mode) {
		int disR = -1;
		int disG = -1;
		int disB = -1;
		switch (par1mode) {
		case 1:
			disR = colors[par1arg].getLowValue().getRed() - this.getRed();
			disG = colors[par1arg].getLowValue().getGreen() - this.getGreen();
			disB = colors[par1arg].getLowValue().getBlue() - this.getBlue();
			break;
		case 2:
			disR = colors[par1arg].getMidValue().getRed() - this.getRed();
			disG = colors[par1arg].getMidValue().getGreen() - this.getGreen();
			disB = colors[par1arg].getMidValue().getBlue() - this.getBlue();
			break;
		case 3:
			disR = colors[par1arg].getHighValue().getRed() - this.getRed();
			disG = colors[par1arg].getHighValue().getGreen() - this.getGreen();
			disB = colors[par1arg].getHighValue().getBlue() - this.getBlue();
			break;
		}
		BigDecimal pretotal =
				((BigDecimal.valueOf(disR).pow(2))
						.add(BigDecimal.valueOf(disG).pow(2))
						.add(BigDecimal.valueOf(disB).pow(2)));
		return Math.sqrt(pretotal.doubleValue());
	}
	
	public strictfp PixelColor getMidValue() {
		int R = (int)Math.floor(this.getRed() * 220 / 255);
		int G = (int)Math.floor(this.getGreen() * 220 / 255);
		int B = (int)Math.floor(this.getBlue() * 220 / 255);
		
		return new PixelColor(R, G, B);
	}
	
	public PixelColor getHighValue() {
		return this;
	}
	
	public strictfp PixelColor getLowValue() {
		int R = (int)Math.floor(this.getRed() * 180 / 255);
		int G = (int)Math.floor(this.getGreen() * 180 / 255);
		int B = (int)Math.floor(this.getBlue() * 180 / 255);
		
		return new PixelColor(R, G, B);
	}
	
	
}
