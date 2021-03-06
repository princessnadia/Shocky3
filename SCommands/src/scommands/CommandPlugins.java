package scommands;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import shocky3.Shocky;

public class CommandPlugins extends Command {
	public CommandPlugins() {
		super("reload");
	}
	
	public void call(Shocky botApp, MessageEvent<PircBotX> e, String trigger, String args) {
		if (!botApp.identManager.userHasPermission(e, "Shocky.SCommands.Plugins.Reload")) return;
		e.respond("Reloading...");
		botApp.pluginManager.reload();
		e.respond("Finished.");
	}
}