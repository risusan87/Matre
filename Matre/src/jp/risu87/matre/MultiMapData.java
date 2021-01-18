package jp.risu87.matre;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * @author kobayashi
 */
public class MultiMapData {
	
	public final MapImage rawmap;
	public final MapData[] maps;
	
	private MultiMapData() {
		this.maps = null;
		this.rawmap = null;
	}
	
	private MultiMapData(BufferedImage par1rawmap) {
		int height = par1rawmap.getHeight();
		int width = par1rawmap.getWidth();
		int map_h = (int)Math.ceil(height / 128.f);
		int map_w = (int)Math.ceil(width / 128.f);
		int null_h = map_h * 128 - height;
		int null_w = map_w * 128 - width;
		int null_h_top = (int)Math.floor(null_h / 2.f);
		int null_w_left = (int)Math.floor(null_w / 2.f);
		int final_height = map_h * 128;
		int final_width = map_w * 128;
		
		System.out.println(final_width + " x " + final_height);
	
		this.maps = new MapData[map_h * map_w];
		
		BufferedImage img = new BufferedImage(final_width, final_height, BufferedImage.TYPE_INT_RGB);
		Raster mainImg = par1rawmap.getData().createChild(0, 0, width, height, null_w_left, null_h_top, null);
		img.setData(mainImg);
		
		MapImage mapimg = new MapImage(img);
		
		System.out.println("Generating  mini map...");
		for (int h = 0; h < map_h; h++) for (int w = 0; w < map_w; w++) {
			System.out.println(w + " x " + h);
			MapData part = new MapData(mapimg.getChildImage(w * 128, h * 128, 128, 128));
			this.maps[h * map_w + w] = part;
		}
		System.out.println("Generation done!");
		this.rawmap = mapimg;
	}
	
	/**
	 * 
	 * @param par1rawmap - a non-converted image
	 * @return
	 */
	public static MultiMapData genMapData(BufferedImage par1rawmap) {
		if (par1rawmap == null)
			return null;
		return new MultiMapData(par1rawmap);
	}
}
