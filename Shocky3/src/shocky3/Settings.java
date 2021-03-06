package shocky3;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.pircbotx.Channel;
import pl.shockah.json.JSONObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class Settings {
	public final Shocky botApp;
	protected Map<String, Setting<?>> settings = Collections.synchronizedMap(new HashMap<String, Setting<?>>());
	
	public Settings(Shocky botApp) {
		this.botApp = botApp;
	}
	
	public void read() {
		DBCollection dbc = botApp.collection("settings");
		for (DBObject dbo : JSONUtil.all(dbc.find())) {
			JSONObject j = JSONUtil.fromDBObject(dbo);
			
			Setting<Object> s = new Setting<>(j.get("defaultValue"));
			if (j.contains("custom")) {
				JSONObject jCustom = j.getObject("custom");
				for (String key : jCustom.keys()) {
					s.custom.put(key, jCustom.get(key));
				}
			}
			settings.put(j.getString("name"), s);
		}
	}
	
	public <T> void add(Plugin plugin, String setting, T defaultValue) {
		setting = plugin.pinfo.internalName() + "|" + setting;
		if (settings.containsKey(setting)) return;
		settings.put(setting, new Setting<T>(defaultValue, plugin));
	}
	public <T> void add(String setting, T defaultValue) {
		if (settings.containsKey(setting)) return;
		settings.put(setting, new Setting<T>(defaultValue));
	}
	
	public String getStringForChannel(Channel channel, Plugin plugin, String setting) {
		return getStringForChannel(channel, plugin.pinfo.internalName() + "|" + setting);
	}
	public String getStringForChannel(Channel channel, String setting) {
		return getStringForChannel(channel.getBot().getConfiguration().getServerHostname(), channel.getName(), setting);
	}
	public String getStringForChannel(String server, String channel, String setting) {
		return (String)getForChannel(server, channel, setting);
	}
	
	public Object getForChannel(Channel channel, Plugin plugin, String setting) {
		return getForChannel(channel, plugin.pinfo.internalName() + "|" + setting);
	}
	public Object getForChannel(Channel channel, String setting) {
		return getForChannel(channel.getBot().getConfiguration().getServerHostname(), channel.getName(), setting);
	}
	public Object getForChannel(String server, String channel, String setting) {
		Setting<?> s = settings.get(setting);
		if (s.custom.containsKey(server + "|" + channel)) return s.custom.get(server + "|" + channel);
		return s.defaultValue;
	}
	
	private class Setting<T> {
		public List<Plugin> plugins = Collections.synchronizedList(new LinkedList<Plugin>());
		public T defaultValue;
		public Map<String, T> custom = Collections.synchronizedMap(new HashMap<String, T>());
		
		public Setting(T defaultValue, Plugin... plugins) {
			this.defaultValue = defaultValue;
			for (Plugin plugin : plugins) this.plugins.add(plugin);
		}
	}
}