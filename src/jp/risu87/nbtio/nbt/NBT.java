package jp.risu87.nbtio.nbt;

import javax.annotation.Nullable;

import jp.risu87.nbtio.nbt.tag.CompoundTag;

/**
 * NBTはマインクラフト内で使われているデータ保存形式の一つです。
 * 一つのファイルにあらゆる情報を階層制度で保存しています。
 * 
 * このNBTクラスでは、タグを生成する際のルートとなる部分です。
 * createNBT()メソッドでのみインスタンス化できます。
 * @author kobayashi
 */
public abstract class NBT extends CompoundTag {
	
	protected final String format;
	
	private NBT(@Nullable String par1name) {
		super(par1name);
		this.format = this.getFormatProtocol();
	}
	
	/**
	 * NBTを新規に作成する際に使用するメソッドです。
	 * 
	 * @param par1tagname　ルートとなるタグの名前を設定します。
	 * @param par2filetype　ファイルの拡張子を設定します。
	 * @return 生成したNBTクラスのオブジェクトを返却します。
	 */
	public static NBT createNBTTag(@Nullable String par1tagname, String par2filetype) {
		return new NBT(par1tagname) {
			@Override
			public String formatProtocol() {
				return par2filetype;
			}
		};
	}
	
	/**
	 * @return このファイルの拡張子を返却します。
	 */
	public abstract String formatProtocol();
	
	@Deprecated
	public String getFormatProtocol() {
		return this.format;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
}
