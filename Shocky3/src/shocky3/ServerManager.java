package shocky3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import pl.shockah.json.JSONObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class ServerManager {
	public final Shocky botApp;
	protected List<BotManager> botManagers = Collections.synchronizedList(new LinkedList<BotManager>());
	
	public ServerManager(Shocky botApp) {
		this.botApp = botApp;
	}
	
	public void readConfig() {
		DBCollection dbc = botApp.collection("servers");
		for (DBObject dbo : JSONUtil.all(dbc.find())) {
			JSONObject j = JSONUtil.fromDBObject(dbo);
			
			BotManager bm = new BotManager(this, j.getString("host"));
			bm.botName = j.getString("name");
			bm.channelsPerConn = j.getInt("channelsPerConn");
			botManagers.add(bm);
			
			for (String jChannel : j.getList("channels").ofStrings()) {
				bm.joinChannel(jChannel);
			}
		}
	}
	
	public BotManager byServerName(String name) {
		for (BotManager manager : botManagers) {
			if (manager.host.equals(name)) {
				return manager;
			}
		}
		return null;
	}
	
	public BotManager byBot(Event<PircBotX> e) {
		return byBot(e.getBot());
	}
	public BotManager byBot(PircBotX bot) {
		for (BotManager manager : botManagers) {
			if (manager.bots.contains(bot)) {
				return manager;
			}
		}
		return null;
	}
}