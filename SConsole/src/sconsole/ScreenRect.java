package sconsole;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal.Color;
import com.googlecode.lanterna.terminal.TerminalSize;

public class ScreenRect {
	protected final ConsoleThread thread;
	public int x, y, w, h;
	
	public ScreenRect(ConsoleThread thread) {
		this.thread = thread;
		x = y = 0;
		setSizeToScreen();
	}
	public ScreenRect() {
		this(0, 0, 0, 0);
	}
	public ScreenRect(int w, int h) {
		this(null, 0, 0, w, h);
	}
	public ScreenRect(int x, int y, int w, int h) {
		this(null, x, y, w, h);
	}
	public ScreenRect(ConsoleThread thread, int x, int y, int w, int h) {
		this.thread = thread;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public String toString() {
		return String.format("[ScreenRect: %d,%d %dx%d]", x, y, w, h);
	}
	
	public ConsoleThread thread() {
		return thread;
	}
	public Screen screen() {
		return thread.screen;
	}
	
	public void setSizeToScreen() {
		TerminalSize size = screen().getTerminalSize();
		w = size.getColumns();
		h = size.getRows();
	}
	
	public ScreenRect rectBase() {
		return new ScreenRect(thread);
	}
	public ScreenRect rectBase(int x, int y, int w, int h) {
		return new ScreenRect(thread, x, y, w, h);
	}
	public ScreenRect rect(int x, int y, int w, int h) {
		if (w < 0 || h < 0 || w > this.w || h > this.h) throw new IllegalArgumentException();
		if (x < 0 || y < 0 || this.x + x + w > this.x + this.w || this.y + y + h > this.y + this.h) throw new IllegalArgumentException();
		return new ScreenRect(thread, this.x + x, this.y + y, w, h);
	}
	
	public boolean inRect(int x, int y) {
		return x >= 0 && y >= 0 && x < w && y < h;
	}
	
	public void draw(int x, int y, String text) {draw(x,y,text,Color.WHITE,Color.BLACK);}
	public void draw(int x, int y, String text, Color cForeground, Color cBackground) {
		String[] spl = text.split("\\r?\\n");
		int wleft = w - x;
		for (int i = 0; i < spl.length; i++) {
			if (spl[i].length() > wleft) spl[i] = spl[i].substring(0, wleft);
			if (inRect(x, y + i)) screen().putString(this.x + x, this.y + y + i, spl[i], cForeground, cBackground);
		}
	}
	public void draw(int x, int y, char c) {draw(x,y,c,Color.WHITE,Color.BLACK);}
	public void draw(int x, int y, char c, Color cForeground, Color cBackground) {
		if (inRect(x,y)) screen().putString(this.x+x,this.y+y,""+c,cForeground,cBackground);
	}
	
	public void setBorder(int x, int y) {
		if (inRect(x, y)) thread.borders.set(this.x + x, this.y + y);
	}
	
	public void drawBorder() {
		drawBorder(Color.WHITE, Color.BLACK);
	}
	public void drawBorder(Color cForeground, Color cBackground) {
		for (int x = -1; x < w + 1; x++) {
			thread.borders.set(this.x + x, this.y - 1, cForeground, cBackground);
			thread.borders.set(this.x + x, this.y + this.h, cForeground, cBackground);
		}
		for (int y = 0; y < h; y++) {
			thread.borders.set(this.x - 1, this.y + y, cForeground, cBackground);
			thread.borders.set(this.x + w, this.y + y, cForeground, cBackground);
		}
	}
}