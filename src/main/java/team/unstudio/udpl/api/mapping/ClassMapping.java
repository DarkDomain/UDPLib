package team.unstudio.udpl.api.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;

public final class ClassMapping {
	
	private final Map<String, String> obfToDeobf = Maps.newHashMap();
	private final Map<String, String> deobfToObf = Maps.newHashMap();
	
	public ClassMapping(InputStream inputStream) throws IOException {
		load(inputStream);
	}
	
	private void load(InputStream inputStream) throws IOException{
		for(String line:IOUtils.readLines(inputStream)){
			if(line.isEmpty()||line.startsWith("#"))
				continue;
				
			String[] args = line.split(" ",2);
			obfToDeobf.put(args[0], args[1]);
			deobfToObf.put(args[1], args[0]);
		}
	}
	
	public boolean containObf(String obf){
		return obfToDeobf.containsKey(obf);
	}
	
	public boolean containDeobf(String deobf){
		return deobfToObf.containsKey(deobf);
	}
	
	public String getDeobf(String obf){
		return obfToDeobf.get(obf);
	}
	
	public String getDeobf(String obf,String def){
		return obfToDeobf.containsKey(obf)?obfToDeobf.get(obf):def;
	}
	
	public String getObf(String deobf){
		return deobfToObf.get(deobf);
	}
	
	public String getObf(String deobf,String def){
		return deobfToObf.containsKey(deobf)?deobfToObf.get(deobf):def;
	}
}
