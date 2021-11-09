package jp.risu87.nbtio.nbt.tag;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

/**
 * One of nbt tags that holds ByteArray
 * 
 * @author risusan87
 */
public class ByteArrayTag extends Tag implements Tag.TagArray {

	private List<Byte> barray;
	public static final ByteArrayTag instance = new ByteArrayTag(null, null);
	/**
	 * Creates new tag of byte array.
	 * give null as its name to declare as use for list
	 * 
	 * @param par1name - tag name
	 * @param par2b - initial data to be contained
	 */
	public ByteArrayTag(@Nullable String par1name, @Nullable byte[] par2b) {
		super(par1name);
		this.barray = new ArrayList<Byte>();
		if (par2b != null)
			for (byte b : par2b)
				this.barray.add(b);
		else
			this.isNull = true;
	}

	@Override
	protected tagID setType() {
		return tagID.BYTE_ARRAY;
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
			if (!this.isNull) {
				int size = ((Tag.TagArray)tag).arraySize();
				nbt.putInt(size);
				if (size != 0)
					for (byte b : ((ByteArrayTag)tag).tagComponent())
						nbt.put(b);
			} else
				nbt.putInt(0);
			return nbt.array();
		};
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized List<Byte> tagComponent() {
		return this.barray;
	}

	@Override
	public int arraySize() {
		return this.barray.size();
	}

	@Override
	public int primitiveSize() {
		return 0x01;
	}

	@Override
	protected byte getTagID() {
		return (byte)0x07;
	}
	
}
