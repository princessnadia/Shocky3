package shocky3.ident;

import org.pircbotx.User;
import shocky3.BotManager;

public class NickIdentHandler extends IdentHandler {
	public NickIdentHandler() {
		this(null);
	}
	public NickIdentHandler(BotManager manager) {
		super(manager, "n", IdentHandler.OVERHEAD_LOW);
	}
	
	public IdentHandler copy(BotManager manager) {
		return new NickIdentHandler(manager);
	}
	
	public boolean checkAvailability() {
		return true;
	}
	
	public String account(User user) {
		return user.getNick();
	}
	
	public boolean isAccount(User user, String account) {
		String acc = account(user);
		return acc.equals(account);
	}
}