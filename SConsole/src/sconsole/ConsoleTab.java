package sconsole;

public class ConsoleTab {
	public final ConsoleView view;
	public String caption;
	
	public ConsoleTab(String caption, ConsoleView view) {
		this.caption = caption;
		this.view = view;
	}
	
	public void updateTabView(ConsoleViewSplitter.Side side, boolean active) {}
}