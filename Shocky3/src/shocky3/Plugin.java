package shocky3;

public abstract class Plugin {
	public final Shocky botApp;
	public final PluginInfo pinfo;
	
	public Plugin(PluginInfo pinfo) {
		this.botApp = pinfo.botApp;
		this.pinfo = pinfo;
	}
	
	void preOnLoad() {}
	void preOnUnload() {}
	protected void onLoad() {}
	protected void onUnload() {}
	protected void onSettingUpdated(String setting) {}
}