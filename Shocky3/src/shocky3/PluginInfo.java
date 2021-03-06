package shocky3;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import pl.shockah.json.JSONObject;

public final class PluginInfo {
	public final Shocky botApp;
	public final PluginManager manager;
	public final File pluginFile;
	public JSONObject jInfo = null;
	protected Plugin plugin = null;
	
	public PluginInfo(PluginManager manager, File pluginFile) {
		this.botApp = manager.botApp;
		this.manager = manager;
		this.pluginFile = pluginFile;
	}
	
	public String baseClass() {return jInfo.getString("baseClass");}
	public String internalName() {return jInfo.getString("internalName");}
	public List<String> dependsOn() {return jInfo.contains("dependsOn") ? jInfo.getList("dependsOn").ofStrings() : new LinkedList<String>();}
	public String name() {return jInfo.contains("name") ? jInfo.getString("name") : null;}
	public String version() {return jInfo.contains("version") ? jInfo.getString("version") : null;}
	public String author() {return jInfo.contains("author") ? jInfo.getString("author") : null;}
	public boolean defaultState() {return jInfo.contains("defaultState") ? jInfo.getBoolean("defaultState") : true;}
	
	public void markLoad() {
		if (markedForLoading()) return;
		manager.markLoad(this);
	}
	public void markUnload() {
		if (!markedForLoading()) return;
		manager.markUnload(this);
	}
	public boolean markedForLoading() {
		return manager.markedForLoading(this);
	}
	public boolean loaded() {
		return plugin != null;
	}
}