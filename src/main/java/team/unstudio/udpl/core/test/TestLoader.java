package team.unstudio.udpl.core.test;

import team.unstudio.udpl.command.anno.AnnoCommandManager;
import team.unstudio.udpl.core.UDPLib;

public class TestLoader {
	
	public static final TestLoader INSTANCE = new TestLoader();

	public static TestConfiguration config;
	
	public void onLoad(){
		UDPLib.getInstance().getLogger().info("Loaded test.");
	}
	
	public void onEnable(){
		testConfig();
		new AnnoCommandManager("test", UDPLib.getInstance()).addCommand(new TestCommand()).unsafeRegisterCommand();
		UDPLib.getInstance().getLogger().info("Enable test.");
	}
	
	private void testConfig(){
		UDPLib.getInstance().getLogger().info("[Test]Config");
		UDPLib.getInstance().saveResource("test.yml", false);
		config = new TestConfiguration();
		config.reload();
		UDPLib.getInstance().getLogger().info("[Test]"+config.test);
		config.test = "Loaded plugin.";
		config.save();
		config.reload();
		UDPLib.getInstance().getLogger().info("[Test]"+config.test);
	}
}
