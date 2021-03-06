package sconsole;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal.Color;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import pl.shockah.Util;

public class ConsoleThread extends Thread {
	public final Plugin plugin;
	
	public Screen screen = null;
	public ScreenRect rect = null;
	public Borders borders = null;
	public ConsoleView view = null;
	public ConsoleViewTabs viewTabs = null;
	public ConsoleViewTab viewTab = null;
	
	public ConsoleThread(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void run() {
		screen = TerminalFacade.createScreen();
		screen.startScreen();
		if (screen.getTerminal() instanceof SwingTerminal) {
			((SwingTerminal)screen.getTerminal()).getJFrame().addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e) {
					plugin.botApp.stop();
				}
			});
		}
		rect = new ScreenRect(this);
		borders = new Borders(rect);
		
		ConsoleViewSplitter cvs1 = new ConsoleViewSplitter(this);
		//ConsoleViewSplitter cvs2 = new ConsoleViewSplitter(this);
		ConsoleViewTabs cvtt = new ConsoleViewTabs(this);
		ConsoleViewTab cvt = new ConsoleViewTab(this);
		ConsoleViewOutput cvo = new ConsoleViewOutput(this);
		
		cvo.apply();
		
		view = cvs1;
		cvs1.setMain(cvtt, ConsoleViewSplitter.Side.Top);
		cvs1.setOff(cvt);
		
		cvtt.tabs.add(new ConsoleTab("Output", cvo));
		
		viewTab = cvtt.view = cvt;
		viewTabs = cvt.view = cvtt;
		
		ScreenWriter sw = new ScreenWriter(screen);
		while (plugin.botApp.running && plugin.running) {
			if (screen.resizePending()) {
				screen.completeRefresh();
				rect.setSizeToScreen();
				borders = new Borders(rect);
			}
			
			sw.setBackgroundColor(Color.BLACK);
			sw.setForegroundColor(Color.WHITE);
			sw.fillScreen(' ');
			
			if (view != null) {
				view.rect.x = 0;
				view.rect.y = 0;
				view.rect.w = rect.w;
				view.rect.h = rect.h;
				view.update(null);
				view.draw(null);
			}
			
			borders.drawAndClear(rect);
			screen.refresh();
			screen.getTerminal().setCursorVisible(false);
			Util.sleep(50);
		}
		
		cvo.restore();
		
		screen.stopScreen();
		plugin.stopped = true;
	}
}