package jp.risu87.matre;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jp.risu87.nbtio.nbt.tag.ListTag;
import jp.risu87.nbtio.nbt.NBT;
import jp.risu87.nbtio.nbt.tag.IntTag;

/**
 * Matre is a software that converts image / photos into minecraft pixel art
 * 
 * @author kobayashi
 */
public class CoreMatre {

	public static void main(String[] args) throws IOException {
		BufferedImage img = PixelColor.convertImage("images/chino.jpg");
		ImageIO.write(img, "jpg", new File("images/export/chino.jpg"));
	}

}
