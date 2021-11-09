package jp.risu87.nbtio.nbt.tag;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

/**
 * The super class of all known NBT tag types.
 * 
 * @author kobayashi
 */
public abstract class Tag {
	
	public String Tag_name;
	protected boolean isNull = false;
	protected boolean isInList = false;
	protected static Tag instance;
	
	protected enum tagID {
		END, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BYTE_ARRAY,
		STRING, LIST, COMPOUND, INT_ARRAY, LONG_ARRAY
	}
	public final tagID Type;
	
	/**
	 * Creates a new tag with specified name
	 * @param par1name - name of the tag
	 */
	protected Tag(@Nullable String par1name) {
		if (par1name != null)
			this.Tag_name = par1name.equals("") ? "(unnamed)" : par1name;
		else
			this.Tag_name = null;
		this.Type = this.setType();
		instance = this;
	}
	
	protected abstract tagID setType();
	protected abstract Function<Tag, byte[]> _toByteArrayFunction();
	protected abstract byte getTagID();
	
	/**
	 * Returns corresponding variable of this tag,
	 * allowing to edit its data.<br>
	 * @return 
	 */
	public abstract <T>T tagComponent();
	
	/**
	 * Call this whenever data has changed.
	 */
	public final void checkNull() {
		this.isNull = this.tagComponent() == null;
	}
	
	/**
	 * Interface to be implemented on compound attributed tags
	 * @author kobayashi
	 */
	protected static interface TagCompound {
		public int getElementCount();
		
		public <T extends Tag>T addTag(Tag... par1tag);
	}
	
	/**
	 * Interface to be implemented on array attributed tags
	 * @author kobayashi
	 */
	protected static interface TagArray {
		public int arraySize();
		public int primitiveSize();
	}
	
	/**
	 * Converts this tag into unzipped NBT byte form.
	 * @return byte array of NBT tag
	 */
	public final byte[] toByteArray() {
		return this._toByteArrayFunction().apply(this);
	}
	
	/**
	 * Returns allocated byte size of this tag.
	 * 
	 * @return byte size
	 */
	public final int getCorrespondedAllocatedByteSize() {
		return this.getSizelessAllocatedByteSize();
	}
	
	/**
	 * Returns byte size of this whole tag allocated.
	 * 
	 * DEPRECATED: use getSizelessAllocatedByteSize() 
	 * or getCorrespondedAllocatedByteSize instead.
	 * @return byte size
	 */
	@Deprecated
	public final int getAllocatedByteSize() {
		int length = this.Tag_name.equals("") ? 1 : this.isInList ? 0 : this.Tag_name.length();
		int size = 3 + length;
		switch (this.Type) {
			case END: case BYTE: return size + 1;
			case SHORT: return size + 2;
			case INT: case FLOAT: return size + 4;
			case LONG: case DOUBLE: return size + 8;
			case STRING: return size + 2 + ((StringTag)this).tagComponent().length();
			case BYTE_ARRAY: case INT_ARRAY: case LONG_ARRAY:
				int balements = ((TagArray)this).arraySize();
				int allobyte = ((TagArray)this).primitiveSize();
				return balements != 0 ? size + (balements * allobyte) + 4 : size + 4;
			case LIST:
				List<Tag> list_list = this.tagComponent();
				int list_contentByte = 5;
				for (Tag t : list_list)
					list_contentByte += t.getCorrespondedAllocatedByteSize();
				return size + list_contentByte;
			case COMPOUND:
				List<Tag> comp_list = this.tagComponent();
				int comp_contentByte = 1;
				for (Tag t : comp_list)
					comp_contentByte += t.getCorrespondedAllocatedByteSize();
				return size + comp_contentByte;
					
			default: return -1;
		}
	}
	
	/**
	 * allocated byte size to be returned in 32 bit signed integer for null name or value.
	 * 
	 * @return byte size
	 */
	public final int getSizelessAllocatedByteSize() {
		//leading tag id + name payload size + string in utf-8 format = 1 + 2 byte = 3 byte + name payload size
		int name_mem = this.isInList ? 0 :
						this.Tag_name == null ? 3 : 
					   3 + this.Tag_name.length();
		switch (this.Type) {
			case END: return Byte.BYTES;
			case BYTE: return name_mem + Byte.BYTES;
			case SHORT: return this.isNull ?  Byte.BYTES + name_mem : Short.BYTES + name_mem;
			case INT: case FLOAT: return this.isNull ? Byte.BYTES + name_mem : Integer.BYTES + name_mem;
			case LONG: case DOUBLE: return this.isNull ? Byte.BYTES + name_mem : Long.BYTES + name_mem;
			case STRING: return this.isNull ? Short.BYTES + Byte.BYTES + name_mem :
									Short.BYTES + ((StringTag)this).tagComponent().length() + name_mem;
			case BYTE_ARRAY: case INT_ARRAY: case LONG_ARRAY:
				int balements = ((TagArray)this).arraySize();
				int allobyte = ((TagArray)this).primitiveSize();
				return this.isNull ? Integer.BYTES + name_mem : (balements * allobyte) + Integer.BYTES + name_mem;
			case LIST:
				@SuppressWarnings("unchecked")
				List<? extends Tag> list_list = (List<? extends Tag>) this.tagComponent();
				int list_contentByte = name_mem + Byte.BYTES + Integer.BYTES;
				if (!this.isNull)
					for (Tag t : list_list)
						list_contentByte += t.getCorrespondedAllocatedByteSize();
				return list_contentByte;
			case COMPOUND:
				List<Tag> comp_list = this.tagComponent();
				int comp_contentByte = name_mem;
				for (Tag t : comp_list)
					comp_contentByte += t.getCorrespondedAllocatedByteSize();
				return comp_contentByte + Byte.BYTES;
					
			default: return -1;
		}
	}
	
	/**
	 * flag to set if this tag is in a ListTag.
	 * do not forget to mark if it's in a list, otherwise causes overflow error.
	 */
	public Tag asInListTag() {
		this.isInList = true;
		return this;
	}
	
	/**
	 * not really reliable.
	 * doesn't meant to be used.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Tag))
			return false;
		Tag t = (Tag)obj;
		return (this.Type == t.Type) && (this.Tag_name.equals(t.Tag_name));
	}
	
}

