package sidentityns;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.NoticeEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.WhoisEvent;
import shocky3.BotManager;
import shocky3.PluginInfo;
import shocky3.ident.IdentHandler;

public class Plugin extends shocky3.ListenerPlugin {
	protected IdentHandler identHandler = null;
	protected Map<BotManager, NickServIdentHandler> map = Collections.synchronizedMap(new HashMap<BotManager, NickServIdentHandler>());
	
	public Plugin(PluginInfo pinfo) {
		super(pinfo);
	}
	
	protected void onLoad() {
		botApp.identManager.add(
			identHandler = new NickServIdentHandler()
		);
	}
	
	protected void onUnload() {
		botApp.identManager.remove(
			identHandler
		);
	}
	
	protected void onWhois(WhoisEvent<PircBotX> e) {
		if (!e.getNick().equals("NickServ")) return;
		map.get(botApp.serverManager.byBot(e)).whois = e;
	}
	
	protected void onNotice(NoticeEvent<PircBotX> e) {
		if (!e.getUser().getNick().equals("NickServ")) return;
		if (!map.containsKey(botApp.serverManager.byBot(e))) {
			map.put(botApp.serverManager.byBot(e), new NickServIdentHandler());
		}
		NickServIdentHandler identHandler = map.get(botApp.serverManager.byBot(e));
		
		String[] spl = e.getMessage().split(" ");
		NickServIdentHandler.UserEntry ue = null;
		if (spl[1].equals("->") && !spl[2].equals("*") && spl[4].equals("3")) {
			ue = new NickServIdentHandler.UserEntry(spl[2]);
		} else {
			ue = new NickServIdentHandler.UserEntry(null);
		}
		if (ue != null) {
			identHandler.map.put(spl[0].toLowerCase(), ue);
		}
	}
	
	protected void onNickChange(NickChangeEvent<PircBotX> e) {
		if (!map.containsKey(botApp.serverManager.byBot(e))) {
			map.put(botApp.serverManager.byBot(e), new NickServIdentHandler());
		}
		NickServIdentHandler identHandler = map.get(botApp.serverManager.byBot(e));
		String sold = e.getOldNick().toLowerCase();
		String snew = e.getNewNick().toLowerCase();
		if (identHandler.map.containsKey(sold)) {
			identHandler.map.put(snew, identHandler.map.get(sold));
			identHandler.map.remove(sold);
		}
	}
	
	protected void onQuit(QuitEvent<PircBotX> e) {
		if (!map.containsKey(botApp.serverManager.byBot(e))) {
			map.put(botApp.serverManager.byBot(e), new NickServIdentHandler());
		}
		NickServIdentHandler identHandler = map.get(botApp.serverManager.byBot(e));
		String nick = e.getUser().getNick().toLowerCase();
		if (identHandler.map.containsKey(nick)) {
			identHandler.map.remove(nick);
		}
	}
}