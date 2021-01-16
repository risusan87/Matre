package jp.risu87.matre;

import java.awt.image.BufferedImage;

/**
 * 
 * @author kobayashi
 */
public class MultiMapData {
	
	private final BufferedImage rawmap;
	private final MapData[] maps;
	
	private MultiMapData() {
		this.maps = null;
		this.rawmap = null;
	}
	
	private MultiMapData(BufferedImage par1rawmap) {
		int height = par1rawmap.getHeight();
		int width = par1rawmap.getWidth();
		int map_h = (int)Math.floor(height / 128.f);
		int map_w = (int)Math.ceil(width / 128.f);
		int null_space_h = height - map_h * 128;
		int null_space_w = width - map_w * 128;
	
		this.maps = new MapData[map_h * map_w];
		this.rawmap = par1rawmap;
		
		
	}
	
	/**
	 * 
	 * @param par1rawmap - 変換された画像データのみ
	 * @return
	 */
	public static MultiMapData genMapData(BufferedImage par1rawmap) {
		if (par1rawmap == null)
			return null;
		return new MultiMapData(par1rawmap);
	}
}
