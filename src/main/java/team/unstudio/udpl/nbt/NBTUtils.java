package team.unstudio.udpl.nbt;

public interface NBTUtils {
	
	public static NBTTagCompound loadNBTTagCompoundFromJson(String json){
		return null; //TODO:
	}
	
	public static NBTTagList loadNBTTagListFromJson(String json){
		return null; //TODO:
	}
	
	/**
	 * @throws NumberFormatException
	 */
	public static NBTNumber loadNBTNumber(String value){
		return new NBTTagDouble(Double.valueOf(value));
	}
}
