package jp.risu87.nbtio.nbt.tag;

import java.nio.ByteBuffer;
import java.util.function.Function;

import javax.annotation.Nullable;

/**
 * One of nbt tags. Holds int
 *
 * @author risusan87
 */
public class IntTag extends Tag {
	
	public Integer i = null;
	public static final IntTag instance = new IntTag(null, null);
	
	/**
	 * Creates new tag of int.
	 * give null as its name to declare as use for list
	 * 
	 * @param par1name - tag name
	 * @param par2setint - int to be set
	 */
	public IntTag(@Nullable String par1name, Integer par2setint) {
		super(par1name);
		if (par2setint != null)
			this.i = par2setint;
		else
			this.isNull = true;
	}

	@Override
	public tagID setType() {
		return tagID.INT;
	}

	@Override
	protected Function<Tag, byte[]> _toByteArrayFunction() {
		return tag -> {
			ByteBuffer nbt = ByteBuffer.allocate(tag.getCorrespondedAllocatedByteSize());
			if (!tag.isInList)
			if (tag.Tag_name != null) {
				nbt.put(tag.getTagID());
				nbt.put(StringTag.toNBTByteTag(tag.Tag_name));
			} else
				nbt.put(tag.getTagID()).put(new EndTag().toByteArray()).put(new EndTag().toByteArray());
			if (!tag.isNull)
				nbt.putInt((int)tag.tagComponent());
			else
				nbt.put(new EndTag().toByteArray());
			return nbt.array();
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer tagComponent() {
		return this.i;
	}

	@Override
	protected byte getTagID() {
		return (byte)0x03;
	}
	
}
