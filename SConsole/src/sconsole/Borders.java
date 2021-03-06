package sconsole;

import com.googlecode.lanterna.terminal.Terminal.Color;

public class Borders {
	public static final char[] chars = "   ═ ╝╚╩ ╗╔╦║╣╠╬".toCharArray();
	
	public static char sides(boolean l, boolean r, boolean u, boolean d) {
		return chars[(l ? 1 : 0) + (r ? 2 : 0) + (u ? 4 : 0) + (d ? 8 : 0)];
	}
	
	public final int w, h;
	public Color[][] bg, fg;
	
	public Borders(ScreenRect rect) {
		this(rect.w, rect.h);
	}
	public Borders(int w, int h) {
		this.w = w;
		this.h = h;
		bg = new Color[w][h];
		fg = new Color[w][h];
	}
	
	public void set(int x, int y) {
		set(x, y, Color.WHITE);
	}
	public void set(int x, int y, Color foreground) {
		set(x, y, foreground, Color.BLACK);
	}
	public void set(int x, int y, Color foreground, Color background) {
		if (x < 0 || y < 0 || x >= w || y >= h) return;
		fg[x][y] = foreground;
		bg[x][y] = background;
	}
	
	public boolean get(int x, int y) {
		if (x < 0 || y < 0 || x >= w || y >= h) return true;
		return fg[x][y] != null;
	}
	public Color getForeground(int x, int y) {
		if (x < 0 || y < 0 || x >= w || y >= h) return null;
		return fg[x][y];
	}
	public Color getBackground(int x, int y) {
		if (x < 0 || y < 0 || x >= w || y >= h) return null;
		return bg[x][y];
	}
	
	public void drawAndClear(ScreenRect rect) {
		draw(rect);
		clear();
	}
	public void draw(ScreenRect rect) {
		for (int x = 0; x < w; x++) for (int y = 0; y < h; y++) {
			if (get(x, y)) {
				rect.draw(x, y, sides(get(x - 1, y), get(x + 1, y), get(x, y - 1), get(x, y + 1)), getForeground(x, y), getBackground(x, y));
			}
		}
	}
	public void clear() {
		for (int x = 0; x < w; x++) for (int y = 0; y < h; y++) {
			fg[x][y] = null;
			bg[x][y] = null;
		}
	}
}