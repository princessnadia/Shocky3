package scommands;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import pl.shockah.json.JSONObject;
import shocky3.PluginInfo;

public class Plugin extends shocky3.ListenerPlugin {
	protected JSONObject j = null;
	protected List<Command> commands = Collections.synchronizedList(new LinkedList<Command>());
	
	public Plugin(PluginInfo pinfo) {
		super(pinfo);
	}
	
	public void add(Command... cmds) {
		for (Command cmd : cmds) {
			if (!commands.contains(cmd)) {
				commands.add(cmd);
			}
		}
	}
	public void remove(Command... cmds) {
		for (Command cmd : cmds) {
			commands.remove(cmd);
		}
	}
	
	protected void onLoad() {
		botApp.settings.add(this, "characters", ".");
		commands.clear();
		
		commands.add(new CommandDie());
		commands.add(new CommandPlugins());
	}
	
	protected void onUnload() {
		commands.clear();
	}
	
	protected void onMessage(MessageEvent<PircBotX> e) {
		String msg = e.getMessage();
		String[] spl = botApp.settings.getStringForChannel(e.getChannel(), this, "characters").split(" ");
		for (String s : spl) {
			if (msg.startsWith(s)) {
				msg = msg.substring(s.length());
				String trigger = msg.split("\\s")[0].toLowerCase();
				String args = msg.equals(trigger) ? "" : msg.substring(trigger.length() + 1).trim();
				
				for (Command cmd : commands) {
					if (cmd.main.equals(trigger)) {
						cmd.call(botApp, e, trigger, args);
						return;
					}
				}
				for (Command cmd : commands) {
					for (String alt : cmd.alt) {
						if (alt.equals(trigger)) {
							cmd.call(botApp, e, trigger, args);
							return;
						}
					}
				}
				
				Command closest = null;
				int diff = -1;
				for (Command cmd : commands) {
					if (cmd.main.startsWith(trigger)) {
						int d = cmd.main.length() - trigger.length();
						if (closest == null || d < diff) {
							closest = cmd;
							diff = d;
						}
					}
					for (String alt : cmd.alt) {
						if (alt.startsWith(trigger)) {
							int d = alt.length() - trigger.length();
							if (closest == null || d < diff) {
								closest = cmd;
								diff = d;
							}
						}
					}
				}
				
				if (closest != null) {
					closest.call(botApp, e, trigger, args);
				}
				return;
			}
		}
	}
}