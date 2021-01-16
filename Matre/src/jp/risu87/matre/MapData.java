package jp.risu87.matre;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.zip.GZIPOutputStream;

import jp.risu87.nbtio.nbt.NBT;
import jp.risu87.nbtio.nbt.tag.ByteArrayTag;
import jp.risu87.nbtio.nbt.tag.ByteTag;
import jp.risu87.nbtio.nbt.tag.CompoundTag;
import jp.risu87.nbtio.nbt.tag.IntTag;
import jp.risu87.nbtio.nbt.tag.ListTag;

/**
 * マインクラフトの地図はNBTタグという形式で各ワールドフォルダ直下のdataフォルダ内に保管されています。
 * 地図上に描写される地形はマインクラフトの基本色ID及び専用の倍率、
 * つまり限られた色の中に従って実際の地形の色に最も近い色が算出され、
 * このデータは128x128項目が割り当てられたcolorsという名前のNBTバイト配列内に保存されます。
 * 
 * このMapDataクラスではそのデータを意図的に自動生成することで、
 * 実際のワールド内には存在しない地形(画像)を作り出すことを可能にします。
 * 
 * @author kobayashi
 */
public class MapData {
	
	private NBT map;
	
	public MapData() {
		this.map = NBT.createNBTTag(null, "dat");
		CompoundTag data = new CompoundTag("data");
		ByteTag scale = new ByteTag("scale", (byte)0);
		ByteTag dimension = new ByteTag("dimension", (byte)-2);
		ByteTag tracking_position = new ByteTag("trackingPosition", (byte)0);
		ByteTag unlimited_tracking = new ByteTag("unlimitedTracking", (byte)0);
		ByteTag locked = new ByteTag("locked", (byte)0);
		IntTag x_center = new IntTag("xCenter", 0);
		IntTag z_center = new IntTag("zCenter", 0);
		ListTag<CompoundTag> banners = new ListTag<CompoundTag>("banners");
		ListTag<CompoundTag> frames = new ListTag<CompoundTag>("frames");
		ByteArrayTag colors = new ByteArrayTag("colors", null);
		data.addTag(scale, dimension, tracking_position, unlimited_tracking, locked, x_center, z_center);
		data.addTag(banners, frames, colors);
		IntTag data_version = new IntTag("dataVersion", 1234);
		this.map.addTag(data, data_version);
	}
	
	public void exportMapData() throws IOException {
		FileOutputStream fos = new FileOutputStream(new File("map." + this.map.formatProtocol()));
		GZIPOutputStream gzos = new GZIPOutputStream(fos);
		for (byte b : this.map.toByteArray()) {
			System.out.println(b);
			gzos.write(b);
		}
		gzos.close();
		fos.close();
	}
}
