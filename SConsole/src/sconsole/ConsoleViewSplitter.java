package sconsole;

public class ConsoleViewSplitter extends ConsoleView {
	public static enum Side {
		Left(true), Right(true), Top(false), Bottom(false);
		
		public final boolean h, v;
		
		private Side(boolean h) {
			this.h = h;
			this.v = !h;
		}
		
		public Side opposite() {
			switch (this) {
				case Left: return Right;
				case Right: return Left;
				case Top: return Bottom;
				case Bottom: return Top;
			}
			return null;
		}
	}
	
	protected ConsoleView main = null, off = null;
	protected Side side = null;
	protected int length = 0;
	
	public ConsoleViewSplitter(ConsoleThread thread) {
		super(thread);
	}
	
	public void setMain(ConsoleView main, Side side) {
		setMain(main, side, 0);
	}
	public void setMain(ConsoleView main, Side side, int length) {
		this.main = main;
		this.side = side;
	}
	public void setOff(ConsoleView off) {
		this.off = off;
	}
	
	public void update(ConsoleViewSplitter.Side side) {
		if (this.side == null) return;
		if (main == null && off == null) return;
		
		if ((main == null) ^ (off == null)) {
			ConsoleView view = main == null ? off : main;
			view.update(null);
			view.rect.x = rect.x;
			view.rect.y = rect.y;
			view.rect.w = rect.w;
			view.rect.h = rect.h;
		} else {
			main.update(this.side);
			off.update(this.side.opposite());
			int len = length == 0 ? (this.side.h ? main.rect.w : main.rect.h) : length;
			
			switch (this.side) {
				case Left:
					main.rect.x = rect.x;
					off.rect.x = rect.x + len + 1;
					break;
				case Right:
					main.rect.x = rect.x + rect.w - len;
					off.rect.x = rect.x;
					break;
				case Top:
					main.rect.y = rect.y;
					off.rect.y = rect.y + len + 1;
					break;
				case Bottom:
					main.rect.y = rect.y + rect.h - len;
					off.rect.y = rect.y;
					break;
			}
			
			if (this.side.h) {
				main.rect.y = rect.y;
				main.rect.h = rect.h;
				off.rect.y = rect.y;
				off.rect.w = rect.w - len - 1;
				off.rect.h = rect.h;
			} else {
				main.rect.x = rect.x;
				main.rect.w = rect.w;
				off.rect.x = rect.x;
				off.rect.w = rect.w;
				off.rect.h = rect.h - len - 1;
			}
		}
	}
	
	public void draw(ConsoleViewSplitter.Side side) {
		if (this.side == null) return;
		if (main != null) {
			main.draw(this.side);
			main.rect.drawBorder();
		}
		if (off != null) {
			off.draw(this.side.opposite());
			off.rect.drawBorder();
		}
	}
}