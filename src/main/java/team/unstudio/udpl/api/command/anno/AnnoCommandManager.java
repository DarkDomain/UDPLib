package team.unstudio.udpl.api.command.anno;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AnnoCommandManager implements CommandExecutor,TabCompleter{
	private final JavaPlugin plugin;
	private final String name;
	private final List<CommandWrapper> wrappers = new ArrayList<>();
	private CommandWrapper defaultCommand;
	
	private String noPermissionMessage = "没有足够的权限";
	private String noEnoughParameterMessage = "参数不足";
	private String wrongSenderMessage = "错误的指令执行者";
	private String errorParameterMessage = "参数错误";
	private String unknownCommandMessage = "未知的指令";
	private String runCommandFailureMessage = "指令执行失败";
	
	/**
	 * 创建指令管理者
	 * @param name 指令
	 * @param plugin 插件
	 */
	public AnnoCommandManager(String name,JavaPlugin plugin){
		this.name = name;
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length==0&&defaultCommand!=null){
			handleCommand(defaultCommand, sender, command, label, args);
			return true;
		}
		
		int i=0;
		List<CommandWrapper> subWrappers = wrappers;
		while(!subWrappers.isEmpty()&&i<args.length){
			for(CommandWrapper wrapper:subWrappers){
				if(!wrapper.getNode().equalsIgnoreCase(args[i]))
					continue;
				
				i++;
				subWrappers = wrapper.getChildren();
				
				if (subWrappers.isEmpty()) {
					handleCommand(wrapper, sender, command, label ,Arrays.copyOfRange(args, i, args.length));
					return true;
				}
				break;
			}
		}
		onUnknownCommand(sender, command, label, args);
		return true;
	}
	
	private void handleCommand(CommandWrapper wrapper,CommandSender sender,Command command,String label,String args[]){
		switch (wrapper.onCommand(sender, args)) {
		case ErrorParameter:
			onErrorParameter(sender, command, label, args, wrapper);
			break;
		case NoEnoughParameter:
			onNoEnoughParameter(sender, command, label, args, wrapper);
			break;
		case NoPermission:
			onNoPermission(sender, command, label, args, wrapper);
			break;
		case WrongSender:
			onWrongSender(sender, command, label, args, wrapper);
			break;
		case Failure:
			onRunCommandFailure(sender, command, label, args, wrapper);
			break;
		case Success:
			break;
		default:
			break;
		}
	}

	/**
	 * 添加指令
	 * @param sub
	 * @return
	 */
	public AnnoCommandManager addCommand(Object object){
		for(Method method:object.getClass().getDeclaredMethods()){
			team.unstudio.udpl.api.command.anno.Command anno = method.getAnnotation(team.unstudio.udpl.api.command.anno.Command.class);
			
			if(anno==null) 
				continue;
			
			CommandWrapper wrapper = getCommandWrapper(anno.value());
			wrapper.setMethod(object, method);
		}
		return this;
	}
	
	public CommandWrapper getCommandWrapper(String args[]){
		if(args.length==0){
			if(defaultCommand!=null) 
				return defaultCommand;
			CommandWrapper wrapper = new CommandWrapper(null);
			defaultCommand = wrapper;
			return wrapper;
		}else{
			CommandWrapper w = null;
			for(CommandWrapper w1:wrappers) if(w1.getNode().equalsIgnoreCase(args[0])){
				w = w1;
			}
			if(w == null){
				w = new CommandWrapper(args[0]);
				wrappers.add(w);
			}
			w: for (int i = 1; i < args.length; i++) {
				for (CommandWrapper w1 : w.getChildren()) {
					if (w.getNode().equalsIgnoreCase(args[i])) {
						w = w1;
						continue w;
					}
				}
				CommandWrapper w2 = new CommandWrapper(args[i]);
				w.getChildren().add(w2);
				w = w2;
			}
			return w;
		}
	}

	public String getNoPermissionMessage() {
		return noPermissionMessage;
	}

	public AnnoCommandManager setNoPermissionMessage(String def) {
		this.noPermissionMessage = def;
		return this;
	}

	public String getNoEnoughParameterMessage() {
		return noEnoughParameterMessage;
	}

	public AnnoCommandManager setNoEnoughParameterMessage(String def) {
		this.noEnoughParameterMessage = def;
		return this;
	}

	public String getWrongSenderMessage() {
		return wrongSenderMessage;
	}

	public AnnoCommandManager setWrongSenderMessage(String def) {
		this.wrongSenderMessage = def;
		return this;
	}
	
	public String getErrorParameterMessage() {
		return errorParameterMessage;
	}

	public AnnoCommandManager setErrorParameterMessage(String def) {
		this.errorParameterMessage = def;
		return this;
	}

	protected void onNoPermission(CommandSender sender, Command command, String label, String[] args, CommandWrapper handler){
		sender.sendMessage(noPermissionMessage);
	}
	
	protected void onNoEnoughParameter(CommandSender sender, Command command, String label, String[] args, CommandWrapper handler){
		sender.sendMessage(noEnoughParameterMessage);
	}
	
	protected void onWrongSender(CommandSender sender, Command command, String label, String[] args, CommandWrapper handler){
		sender.sendMessage(wrongSenderMessage);
	}
	
	protected void onErrorParameter(CommandSender sender, Command command, String label, String[] args, CommandWrapper handler){
		sender.sendMessage(errorParameterMessage);
	}
	
	protected void onUnknownCommand(CommandSender sender, Command command, String label, String[] args){
		sender.sendMessage(unknownCommandMessage);
	}
	
	protected void onRunCommandFailure(CommandSender sender, Command command, String label, String[] args, CommandWrapper handler){
		sender.sendMessage(runCommandFailureMessage);
	}
	
	public String getUnknownCommandMessage() {
		return unknownCommandMessage;
	}

	public AnnoCommandManager setUnknownCommandMessage(String def) {
		this.unknownCommandMessage = def;
		return this;
	}

	public String getRunCommandFailureMessage() {
		return runCommandFailureMessage;
	}

	public AnnoCommandManager setRunCommandFailureMessage(String def) {
		this.runCommandFailureMessage = def;
		return this;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}
	
	/**
	 * 注册指令
	 */
	public AnnoCommandManager registerCommand(){
		plugin.getCommand(name).setExecutor(this);
		plugin.getCommand(name).setTabCompleter(this);
		return this;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> list = new ArrayList<>();
		
		int i=0;
		List<CommandWrapper> tsubs = wrappers;
		while(!tsubs.isEmpty()&&i<args.length-1){
			for(CommandWrapper s:tsubs){
				if(s.getNode().equalsIgnoreCase(args[i])){
					i++;
					tsubs = s.getChildren();
					if(tsubs.isEmpty()){
						String targs[] = new String[args.length-i];
						for(int j=0;j<targs.length;j++)targs[j]=args[j+i];
						list.addAll(s.onTabComplete(args));
					}
					break;
				}
			}
		}
		if(!tsubs.isEmpty())
			for(CommandWrapper s:tsubs)
				if(args[i].isEmpty()||s.getNode().startsWith(args[args.length-1]))
					list.add(s.getNode());
		
		if(list.isEmpty())
			for(Player player:Bukkit.getOnlinePlayers())
				if(player.getName().startsWith(args[0]))
					list.add(player.getName());
		return list;
	}
}
