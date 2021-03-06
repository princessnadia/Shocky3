package shocky3.ident;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.types.GenericUserEvent;
import pl.shockah.Pair;
import pl.shockah.json.JSONObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import shocky3.BotManager;
import shocky3.JSONUtil;
import shocky3.Shocky;

public class IdentManager {
	public final Shocky botApp;
	public final Map<BotManager, List<IdentHandler>> identHandlers = Collections.synchronizedMap(new HashMap<BotManager, List<IdentHandler>>());
	public final Map<BotManager, List<IdentGroup>> identGroups = Collections.synchronizedMap(new HashMap<BotManager, List<IdentGroup>>());
	
	public IdentManager(Shocky botApp) {
		this.botApp = botApp;
		
		identHandlers.put(null, Collections.synchronizedList(new LinkedList<IdentHandler>()));
		identGroups.put(null, Collections.synchronizedList(new LinkedList<IdentGroup>()));
		add(
			new NickIdentHandler(),
			new HostIdentHandler()
		);
	}
	
	public void add(IdentHandler... hs) {
		for (Map.Entry<BotManager, List<IdentHandler>> entry : identHandlers.entrySet()) {
			for (IdentHandler h : hs) {
				IdentHandler copy = h.copy(entry.getKey());
				if (!entry.getValue().contains(copy)) {
					entry.getValue().add(copy);
				}
			}
		}
	}
	public void remove(IdentHandler... hs) {
		for (Map.Entry<BotManager, List<IdentHandler>> entry : identHandlers.entrySet()) {
			for (IdentHandler h : hs) {
				IdentHandler copy = h.copy(entry.getKey());
				entry.getValue().remove(copy);
			}
		}
	}
	
	public IdentHandler getIdentHandlerFor(BotManager manager, String account) {
		String[] spl = account.split(":");
		if (spl.length == 1) return null;
		for (IdentHandler h : identHandlers.get(manager)) {
			if (h.id.equals(spl[0])) {
				return h;
			}
		}
		return null;
	}
	
	public List<IdentGroup> userIdentGroups(Event<PircBotX> e, User user) {
		return userIdentGroups(e.getBot(), user);
	}
	public List<IdentGroup> userIdentGroups(GenericUserEvent<PircBotX> e) {
		return userIdentGroups(e.getBot(), e.getUser());
	}
	public List<IdentGroup> userIdentGroups(PircBotX bot, User user) {
		return userIdentGroups(botApp.serverManager.byBot(bot), user);
	}
	public List<IdentGroup> userIdentGroups(BotManager manager, User user) {
		List<IdentGroup> list = new LinkedList<>();
		if (manager != null) {
			if (identGroups.containsKey(manager)) {
				for (IdentGroup igroup : identGroups.get(manager)) {
					if (igroup.userBelongs(user)) {
						list.add(igroup);
					}
				}
			}
			manager = null;
		}
		if (manager == null) {
			for (IdentGroup igroup : identGroups.get(null)) {
				if (igroup.userBelongs(user)) {
					list.add(igroup);
				}
			}
		}
		return list;
	}
	
	public List<IdentGroup> permissionIdentGroups(Event<PircBotX> e, String permission) {
		return permissionIdentGroups(e.getBot(), permission);
	}
	public List<IdentGroup> permissionIdentGroups(PircBotX bot, String permission) {
		return permissionIdentGroups(botApp.serverManager.byBot(bot), permission);
	}
	public List<IdentGroup> permissionIdentGroups(BotManager manager, String permission) {
		List<IdentGroup> list = new LinkedList<>();
		if (manager != null) {
			if (identGroups.containsKey(manager)) {
				for (IdentGroup igroup : identGroups.get(manager)) {
					if (igroup.hasPermission(permission)) {
						list.add(igroup);
					}
				}
			}
			manager = null;
		}
		if (manager == null) {
			for (IdentGroup igroup : identGroups.get(null)) {
				if (igroup.hasPermission(permission)) {
					list.add(igroup);
				}
			}
		}
		return list;
	}
	
	public boolean userHasPermission(Event<PircBotX> e, User user, String permission) {
		return userHasPermission(e.getBot(), user, permission);
	}
	public boolean userHasPermission(GenericUserEvent<PircBotX> e, String permission) {
		return userHasPermission(e.getBot(), e.getUser(), permission);
	}
	public boolean userHasPermission(PircBotX bot, User user, String permission) {
		return userHasPermission(botApp.serverManager.byBot(bot), user, permission);
	}
	public boolean userHasPermission(BotManager manager, User user, String permission) {
		List<Pair<IdentHandler, String>> list = new LinkedList<>();
		for (IdentGroup igroup : permissionIdentGroups(manager, permission)) {
			for (String ident : igroup.idents) {
				IdentHandler handler = getIdentHandlerFor(igroup.manager, ident);
				if (handler != null) {
					list.add(new Pair<>(handler, ident));
				}
			}
		}
		Collections.sort(list, new Comparator<Pair<IdentHandler, String>>(){
			public int compare(Pair<IdentHandler, String> p1, Pair<IdentHandler, String> p2) {
				return p1.get1().compareTo(p2.get1());
			}
		});
		
		for (Pair<IdentHandler, String> p : list) {
			if (p.get1().isAvailable() && p.get1().isAccount(user, p.get2().split(":")[1])) {
				return true;
			}
		}
		return false;
	}
	
	public void readConfig() {
		DBCollection dbc = botApp.collection("identGroups");
		for (DBObject dbo : JSONUtil.all(dbc.find())) {
			JSONObject j = JSONUtil.fromDBObject(dbo);
			
			BotManager manager = botApp.serverManager.byServerName(j.getString("server"));
			IdentGroup igroup = new IdentGroup(botApp, manager, j.getString("name"));
			for (String s : j.getList("idents").ofStrings()) {
				igroup.idents.add(s);
			}
			for (String s : j.getList("permissions").ofStrings()) {
				igroup.permissions.add(s);
			}
			
			if (!identGroups.containsKey(manager)) {
				identGroups.put(manager, Collections.synchronizedList(new LinkedList<IdentGroup>()));
			}
			identGroups.get(manager).add(igroup);
		}
	}
}