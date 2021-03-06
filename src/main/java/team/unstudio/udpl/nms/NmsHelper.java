package team.unstudio.udpl.nms;

import org.bukkit.inventory.ItemStack;

import team.unstudio.udpl.core.UDPLib;
import team.unstudio.udpl.core.nms.asm.AsmNmsManager;
import team.unstudio.udpl.nms.inventory.NmsItemStack;
import team.unstudio.udpl.nms.nbt.NmsNBT;
import team.unstudio.udpl.util.BukkitVersion;

public final class NmsHelper {
	
	private NmsHelper(){}
	
	private static AsmNmsManager ASM_NMS_MANAGER;

	public static void loadNmsHelper(){
		ASM_NMS_MANAGER = new AsmNmsManager();
		UDPLib.getInstance().getLogger().info("Loaded nms helper. Version: " + BukkitVersion.CURRENT_BUKKIT_VERSION);
	}
	
	public static NmsNBT getNBT(){
		return ASM_NMS_MANAGER.getNmsNBT();
	}
	
	public static NmsItemStack createItemStack(ItemStack itemStack){
		return ASM_NMS_MANAGER.createNmsItemStack(itemStack);
	}
}
