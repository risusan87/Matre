package jp.risu87.nbtio.nbt.tag;

import java.nio.ByteBuffer;
import java.util.function.Function;

import javax.annotation.Nullable;

/**
 * One of nbt tags. Holds double
 * 
 * @author risusan87
 *
 */
public class DoubleTag extends Tag {
	
	public Double d = null;
	public static final DoubleTag instance = new DoubleTag(null, null);
	
	/**
	 * Creates new tag of double.
	 * give null as its name to declare as use for list
	 * 
	 * @param par1name - tag name
	 * @param par2setdouble - double to be set
	 */
	public DoubleTag(@Nullable String par1name, @Nullable Double par2setdouble) {
		super(par1name);
		if (par2setdouble != null)
			this.d = par2setdouble;
		else
			this.isNull = true;
	}

	@Override
	public tagID setType() {
		return tagID.DOUBLE;
	}

	@Override
	protected Function<Tag, byte[]> _toByteArrayFunction() {
		return tag -> {
			ByteBuffer nbt = ByteBuffer.allocate(tag.getCorrespondedAllocatedByteSize());
			if (!tag.isInList)
			if (tag.Tag_name != null) {
				nbt.put(tag.getTagID());
				if (tag.Tag_name.equals(""))
					nbt.putInt(0x00)
					.put(new EndTag().toByteArray());
				else
					nbt.put(StringTag.toNBTByteTag(tag.Tag_name));
			}
			else
				nbt.put(tag.getTagID()).put(new EndTag().toByteArray()).put(new EndTag().toByteArray());
			if (!this.isNull)
				nbt.putDouble((double)tag.tagComponent());
			nbt.put(new EndTag().toByteArray());
			return nbt.array();
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public Double tagComponent() {
		return this.d;
	}

	@Override
	protected byte getTagID() {
		return (byte)0x06;
	}

}
