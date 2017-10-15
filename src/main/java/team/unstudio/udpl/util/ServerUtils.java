package team.unstudio.udpl.util;

import java.util.function.Predicate;

import org.bukkit.Bukkit;

public final class ServerUtils {

	private ServerUtils(){}
	
	public static String[] getOnlinePlayerNames(){
		return Bukkit.getOnlinePlayers().stream().map(player->player.getName()).toArray(String[]::new);
	}
	
	public static String[] getOnlinePlayerNamesWithFilter(Predicate<String> filter){
		return Bukkit.getOnlinePlayers().stream().map(player->player.getName()).filter(filter).toArray(String[]::new);
	}
	
	public static String getMinecraftVersion(){
		return Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf("-"));
	}
}
