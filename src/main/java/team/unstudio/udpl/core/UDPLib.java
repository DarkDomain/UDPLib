package team.unstudio.udpl.core;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import team.unstudio.udpl.area.Area;
import team.unstudio.udpl.area.AreaDataContainer;
import team.unstudio.udpl.bungeecord.ServerLocation;
import team.unstudio.udpl.command.anno.AnnoCommandManager;
import team.unstudio.udpl.core.test.TestLoader;
import team.unstudio.udpl.mapping.MappingHelper;
import team.unstudio.udpl.nms.NmsHelper;
import team.unstudio.udpl.nms.nbt.NBTUtils;

public final class UDPLib extends JavaPlugin{

	public static final String NAME = "UDPLib";
	public static final String VERSION = "1.0.0";

	private static UDPLib INSTANCE;
	private static boolean DEBUG;
	private static UDPLConfiguration CONFIG;

	public UDPLib() {
		INSTANCE = this;
	}

	@Override
	public void onLoad() {
		ConfigurationSerialization.registerClass(AreaDataContainer.class);
		ConfigurationSerialization.registerClass(Area.class);
		ConfigurationSerialization.registerClass(ServerLocation.class);
		
		NBTUtils.registerAllNBTSerilizable();
		
		MappingHelper.loadMapping();
		NmsHelper.loadNmsHelper();
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		CONFIG = new UDPLConfiguration(new File(getDataFolder(), "config.yml"));
		CONFIG.reload();
		setDebug(CONFIG.debug);
		
		if(CONFIG.enableTest)
			TestLoader.INSTANCE.onLoad();
		
		loadPluginManager();
		
		if(CONFIG.enableTest)
			TestLoader.INSTANCE.onEnable();
	}

	@Override
	public void onDisable() {
		//防止玩家未关闭界面造成刷物品
		for(Player player:Bukkit.getOnlinePlayers())
			player.closeInventory();
	}
	
	private void loadPluginManager(){
		new AnnoCommandManager("pm").addCommand(new PluginManager()).registerCommand();
	}

	public static UDPLConfiguration getUDPLConfig(){
		return CONFIG;
	}

	public static UDPLib getInstance(){
		return INSTANCE;
	}
	
	public static boolean isDebug(){
		return DEBUG;
	}
	
	public static void setDebug(boolean debug){
		DEBUG = debug;
	}
}
