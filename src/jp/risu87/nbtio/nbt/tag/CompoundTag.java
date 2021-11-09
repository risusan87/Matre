package jp.risu87.nbtio.nbt.tag;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

/**
 * NBTタグ形式の一つです。
 * CompoundTagはあらゆる種類のタグを同時に,名前とともに保管します。
 * 
 * @author kobayashi
 */
public class CompoundTag extends Tag implements Tag.TagCompound {
	
	private final List<Tag> tags;
	
	/**
	 * 新規のCompoundTagを生成します。
	 * 
	 * @param par1name - タグの名前を設定します。
	 */
	public CompoundTag(@Nullable String par1name) {
		super(par1name);
		this.tags = new ArrayList<Tag>();
	}
	
	@Override
	public tagID setType() {
		return tagID.COMPOUND;
	}
	
	@Override
	protected Function<Tag, byte[]> _toByteArrayFunction() {
		return tag -> {
			ByteBuffer nbt = ByteBuffer.allocate(tag.getCorrespondedAllocatedByteSize());
			if (!tag.isInList) {
				nbt.put(tag.getTagID());
				if (tag.Tag_name != null)
					nbt.put(StringTag.toNBTByteTag(tag.Tag_name));
				else
					nbt.put(new EndTag().toByteArray()).put(new EndTag().toByteArray());
			}
			
			for (Tag t : ((CompoundTag)tag).tags) {
				nbt.put(t.toByteArray());
			}
			nbt.put(new EndTag().toByteArray());
			return nbt.array();
		};
	}

	@Override
	public int getElementCount() {
		return this.tags.size();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> tagComponent() {
		return this.tags;
	}

	@Override
	protected byte getTagID() {
		return (byte)0x0A;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundTag addTag(Tag... par1tag) {
		for (Tag t : par1tag)
			this.tagComponent().add(t);
		return this;
	}
	
}
