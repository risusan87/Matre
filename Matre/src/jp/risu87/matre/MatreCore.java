package jp.risu87.matre;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MatreCore {
	
	public static final void main(String... args) throws IOException {
		io("kemo");
		System.out.println("Success!");
	}
	
	private static void io(String img) throws IOException {
		MultiMapData mapdata = MultiMapData.genMapData(ImageIO.read(new File("images/" + img + ".jpg")));
		ImageIO.write(mapdata.rawmap, "jpg", new File("images/export/" + img + ".jpg"));
		for (int i = 0; i < mapdata.maps.length; i++)
			mapdata.maps[i].exportMapData(Integer.toString(i+1));
	}
}
