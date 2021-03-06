package shocky3.ident;

import org.pircbotx.User;
import pl.shockah.Box;
import pl.shockah.Util;
import shocky3.BotManager;

public abstract class IdentHandler implements Comparable<IdentHandler> {
	public static final int
		OVERHEAD_LOW = 0,
		OVERHEAD_MEDIUM = 500,
		OVERHEAD_HIGH = 1000;
	
	public final BotManager manager;
	public final String id;
	public final int overhead;
	protected Box<Boolean> available = null;
	
	public IdentHandler(String id, int overhead) {
		this(null, id, overhead);
	}
	public IdentHandler(BotManager manager, String id, int overhead) {
		this.manager = manager;
		this.id = id;
		this.overhead = overhead;
	}
	
	public final boolean equals(Object o) {
		if (!(o instanceof IdentHandler)) return false;
		IdentHandler h = (IdentHandler)o;
		return getClass() == h.getClass() && Util.equals(manager, h.manager);
	}
	public int compareTo(IdentHandler h) {
		return Integer.compare(overhead, h.overhead);
	}
	
	public final boolean isAvailable() {
		if (available == null) {
			available = new Box<>(checkAvailability());
		}
		return available.value;
	}
	
	public abstract IdentHandler copy(BotManager manager);
	public abstract boolean checkAvailability();
	public abstract String account(User user);
	public abstract boolean isAccount(User user, String account);
}