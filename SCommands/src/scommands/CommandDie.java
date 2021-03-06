package scommands;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import shocky3.Shocky;

public class CommandDie extends Command {
	public CommandDie() {
		super("die");
	}
	
	public void call(Shocky botApp, MessageEvent<PircBotX> e, String trigger, String args) {
		if (!botApp.identManager.userHasPermission(e, "Shocky.SCommands.Die")) return;
		botApp.stop();
	}
}