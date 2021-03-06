package shocky3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.pircbotx.PircBotX;
import pl.shockah.Util;
import shocky3.ident.IdentHandler;

public class BotManager {
	public final Shocky botApp;
	public final ServerManager manager;
	public final String host;
	public String botName = null;
	public int channelsPerConn = 0;
	public final List<PircBotX> bots = Collections.synchronizedList(new LinkedList<PircBotX>());
	
	public BotManager(ServerManager manager, String host) {
		this.botApp = manager.botApp;
		this.manager = manager;
		this.host = host;
	}
	
	public PircBotX joinChannel(String cname) {
		for (PircBotX bot : bots) {
			if (bot.getUserBot().getChannels().size() < channelsPerConn) {
				bot.sendIRC().joinChannel(cname);
				return bot;
			}
		}
		
		connectNewBot();
		return joinChannel(cname);
	}
	
	public PircBotX connectNewBot() {
		BotStarterThread botStarter = new BotStarterThread(this);
		botStarter.start();
		
		while (true) {
			if (botStarter.drop) break;
			Util.sleep(50);
		}
		
		try {
			if (botStarter.bot != null) {
				if (!botApp.identManager.identHandlers.containsKey(this)) {
					botApp.identManager.identHandlers.put(this, Collections.synchronizedList(new LinkedList<IdentHandler>()));
				}
				List<IdentHandler> handlers = botApp.identManager.identHandlers.get(this);
				for (IdentHandler h : botApp.identManager.identHandlers.get(null)) {
					IdentHandler copy = h.copy(this);
					if (!handlers.contains(copy)) {
						handlers.add(copy);
					}
				}
				bots.add(botStarter.bot);
			}
		} catch (Exception e) {e.printStackTrace();}
		
		return botStarter.bot;
	}
	
	public List<PircBotX> bots() {
		return Collections.unmodifiableList(bots);
	}
}