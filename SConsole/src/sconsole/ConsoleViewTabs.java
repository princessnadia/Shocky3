package sconsole;

import java.util.LinkedList;
import pl.shockah.SelectList;
import com.googlecode.lanterna.terminal.Terminal.Color;

public class ConsoleViewTabs extends ConsoleView {
	public SelectList<ConsoleTab> tabs = new SelectList<>(new LinkedList<ConsoleTab>());
	public ConsoleViewTab view = null;
	
	public ConsoleViewTabs(ConsoleThread thread) {
		super(thread);
	}
	
	public void update(ConsoleViewSplitter.Side side) {
		if (side == null) return;
		if (view == null) return;
		
		if (side.h) {
			int w = 2;
			for (ConsoleTab tab : tabs) {
				tab.updateTabView(side, tabs.getCurrent() == tab);
				int ww = tab.caption.length() + 2;
				if (ww > w) w = ww;
			}
			rect.w = w;
		} else {
			int w = 0;
			for (ConsoleTab tab : tabs) {
				tab.updateTabView(side, tabs.getCurrent() == tab);
				w += tab.caption.length() + 2;
			}
			rect.w = w;
			rect.h = 1;
		}
	}
	
	public void draw(ConsoleViewSplitter.Side side) {
		if (side == null) return;
		if (view == null) return;
		
		if (side.h) {
			for (int i = 0; i < tabs.size(); i++) {
				ConsoleTab tab = tabs.get(i);
				String text = tab.caption;
				text = /*tabs.getCurrentIndex() == i ? Borders.arrowRight+text+Borders.arrowLeft : */" "+text+" ";
				boolean sel = tabs.getCurrentIndex() == i;
				rect.draw(0, i, text, sel ? Color.BLACK : Color.WHITE, sel ? Color.WHITE : Color.BLACK);
			}
		} else {
			int xx = 0;
			for (int i = 0; i < tabs.size(); i++) {
				ConsoleTab tab = tabs.get(i);
				String text = tab.caption;
				text = /*tabs.getCurrentIndex() == i ? Borders.arrowRight+text+Borders.arrowLeft : */" "+text+" ";
				boolean sel = tabs.getCurrentIndex() == i;
				rect.draw(xx, 0, text, sel ? Color.BLACK : Color.WHITE, sel ? Color.WHITE : Color.BLACK);
				xx += text.length();
			}
		}
	}
}