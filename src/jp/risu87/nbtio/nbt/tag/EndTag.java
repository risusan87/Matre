package jp.risu87.nbtio.nbt.tag;

import java.util.function.Function;

/**
 * One of nbt tags. Holds 0 as end sign of compound tags
 * 
 * @author risusan87
 */
public class EndTag extends Tag {
	
	public static final EndTag instance = new EndTag();
	
	/**
	 * Creates new tag of end.
	 * always hold 0
	 */
	public EndTag() {
		super(null);
	}

	@Override
	public tagID setType() {
		return tagID.END;
	}

	@Override
	protected Function<Tag, byte[]> _toByteArrayFunction() {
		return tag -> {
			return new byte[] {tag.tagComponent()};
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public Byte tagComponent() {
		return (byte)0x00;
	}

	@Override
	protected byte getTagID() {
		return (byte)0x00;
	}
	
}