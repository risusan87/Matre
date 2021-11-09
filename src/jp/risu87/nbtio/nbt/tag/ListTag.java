package jp.risu87.nbtio.nbt.tag;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

public class ListTag<T extends Tag> extends Tag implements Tag.TagCompound {

	private List<T> list;
	
	/**
	 * Creates an empty object of list tag
	 * @param par1name
	 * @param par2instance - give any instance of this type
	 */
	public ListTag(@Nullable String par1name) {
		super(par1name);
		this.list = new ArrayList<T>();
	}

	@Override
	public tagID setType() {
		return tagID.LIST;
	}

	@Override
	protected Function<Tag, byte[]> _toByteArrayFunction() {
		return tag -> {
			ByteBuffer nbt = ByteBuffer.allocate(tag.getCorrespondedAllocatedByteSize());
			if (!tag.isInList) {
				nbt.put(tag.getTagID());
				if (tag.Tag_name != null) {
					nbt.put(StringTag.toNBTByteTag(tag.Tag_name));
				} else
					nbt.put(new EndTag().toByteArray()).put(new EndTag().toByteArray());
				
			}
			
			List<? extends Tag> listoflist = ((ListTag<?>)tag).tagComponent();
			int size = listoflist.size();
			if (size != 0) {
				nbt.put(listoflist.get(0).getTagID());
				nbt.putInt(size);
				for (Tag t : listoflist) {
					nbt.put(t.toByteArray());
				}
			} else
				nbt.putInt(0);
			return nbt.array();
		};
	}

	@Override
	protected byte getTagID() {
		return (byte)0x09;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> tagComponent() {
		return this.list;
	}

	@Override
	public int getElementCount() {
		return this.tagComponent().size();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public ListTag<T> addTag(Tag... par1tag) {
		for (Tag t : par1tag)
			this.tagComponent().add((T)t);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public ListTag<T> add(T... par1elements) {
		for (T t : par1elements)
			this.tagComponent().add(t);
		return this;
	}


}
