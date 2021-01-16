package jp.risu87.nbtio.nbt.tag;

import java.nio.ByteBuffer;
import java.util.function.Function;

import javax.annotation.Nullable;

/**
 * One of nbt tags. Holds Byte
 * 
 * @author risusan87
 */
public class ByteTag extends Tag {

	public Byte b;

	/**
	 * Creates new tag of byte.
	 * give null as its name to declare as use for list
	 * 
	 * @param par1name - tag name
	 * @param par2setbyte - byte to be set
	 */
	public ByteTag(@Nullable String par1name, @Nullable Byte par2setbyte) {
		super(par1name);
		if (par2setbyte != null)
			this.b = par2setbyte;
		else {
			this.b = new EndTag().toByteArray()[0];
			this.isNull = true;
		}
	}

	@Override
	public tagID setType() {
		return tagID.BYTE;
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
			nbt.put(b);
			return nbt.array();
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public Byte tagComponent() {
		return b;
	}

	@Override
	protected byte getTagID() {
		return (byte)0x01;
	}
	
}