package sidentityns;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.WhoisEvent;
import pl.shockah.Util;
import shocky3.BotManager;
import shocky3.ident.IdentHandler;

public class NickServIdentHandler extends IdentHandler {
	public static final int
		MAX_WAIT_TIME = 1000 * 3, //3 seconds
		RECHECK_DELAY = 1000 * 60 * 5; //5 minutes
	
	protected WhoisEvent<PircBotX> whois = null;
	protected Map<String, UserEntry> map = Collections.synchronizedMap(new HashMap<String, UserEntry>());
	
	public NickServIdentHandler() {
		this(null);
	}
	public NickServIdentHandler(BotManager manager) {
		super(manager, "ns", IdentHandler.OVERHEAD_MEDIUM);
	}
	
	public IdentHandler copy(BotManager manager) {
		return new NickServIdentHandler(manager);
	}
	
	public boolean checkAvailability() {
		return true; //trusting the config
		
		/*Plugin plugin = manager.botApp.pluginManager.byInternalName("Shocky.SIdentity.NickServ");
		if (!plugin.map.containsKey(manager)) {
			plugin.map.put(manager, this);
		}
		
		if (manager.bots().isEmpty()) {
			manager.connectNewBot();
		}
		PircBotX bot = manager.bots().get(0);
		whois = null;
		long sentAt = System.currentTimeMillis();
		bot.sendRaw().rawLine("WHOIS NickServ");
		while (whois == null) {
			long now = System.currentTimeMillis();
			if (now - sentAt >= MAX_WAIT_TIME) return false;
			Util.sleep(50);
		}
		
		return whois != null;*/
	}
	
	public String account(User user) {
		Plugin plugin = manager.botApp.pluginManager.byInternalName("Shocky.SIdentity.NickServ");
		if (!plugin.map.containsKey(manager)) {
			plugin.map.put(manager, this);
		}
		
		String nick = user.getNick().toLowerCase();
		if (map.containsKey(nick)) {
			UserEntry ue = map.get(nick);
			if (ue.isStillValid()) {
				return ue.acc;
			} else {
				map.remove(nick);
			}
		}
		
		if (manager.bots().isEmpty()) {
			manager.connectNewBot();
		}
		PircBotX bot = manager.bots().get(0);
		long sentAt = System.currentTimeMillis();
		bot.sendIRC().message("NickServ", String.format("acc %s *", nick));
		while (!map.containsKey(nick)) {
			long now = System.currentTimeMillis();
			if (now - sentAt >= MAX_WAIT_TIME) return null;
			Util.sleep(50);
		}
		
		UserEntry ue = map.get(nick);
		if (ue.acc == null) {
			map.remove(nick);
		}
		return ue.acc;
	}
	
	public boolean isAccount(User user, String account) {
		String acc = account(user);
		return Util.equals(acc, account);
	}
	
	public static class UserEntry {
		public final String acc;
		public final long checkTime;
		
		public UserEntry(String acc) {
			this.acc = acc;
			checkTime = System.currentTimeMillis();
		}
		
		public boolean isStillValid() {
			if (acc == null) return false;
			long now = System.currentTimeMillis();
			return now - checkTime < RECHECK_DELAY;
		}
	}
}