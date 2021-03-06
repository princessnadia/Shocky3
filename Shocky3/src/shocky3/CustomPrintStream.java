package shocky3;

import java.io.PrintStream;
import java.util.Locale;
import pl.shockah.ZeroOutputStream;

public class CustomPrintStream extends PrintStream {
	public PrintStream other = null;
	
	public CustomPrintStream() {
		this(System.out);
	}
	public CustomPrintStream(PrintStream ps) {
		super(new ZeroOutputStream());
		this.other = ps;
	}
	
	public PrintStream append(char c) { if (other != null) other.append(c); return this; }
	public PrintStream append(CharSequence csq) { if (other != null) other.append(csq); return this; }
	public PrintStream append(CharSequence csq, int start, int end) { if (other != null) other.append(csq, start, end); return this; }
	
	public boolean checkError() { if (other != null) return other.checkError(); return false; }
	public void close() { if (other != null) other.close(); }
	public void flush() { if (other != null) other.flush(); }
	
	public PrintStream format(Locale l, String format, Object... args) {  if (other != null) other.format(l, format, args); return this; }
	public PrintStream format(String format, Object... args) { if (other != null) other.format(format, args); return this; }
	
	public void print(boolean b) { if (other != null) other.print(b); }
	public void print(char c) { if (other != null) other.print(c); }
	public void print(char[] s) { if (other != null) other.print(s); }
	public void print(double d) { if (other != null) other.print(d); }
	public void print(float f) { if (other != null) other.print(f); }
	public void print(int i) { if (other != null) other.print(i); }
	public void print(long l) { if (other != null) other.print(l); }
	public void print(Object obj) { if (other != null) other.print(obj); }
	public void print(String s) { if (other != null) other.print(s); }
	
	public PrintStream printf(Locale l, String format, Object... args) { if (other != null) other.printf(l, format, args); return this; }
	public PrintStream printf(String format, Object... args) { if (other != null) other.printf(format, args); return this; }
	
	public void println() { if (other != null) other.println(); }
	public void println(boolean x) { if (other != null) other.println(x); }
	public void println(char x) { if (other != null) other.println(x); }
	public void println(char[] x) { if (other != null) other.println(x); }
	public void println(double x) { if (other != null) other.println(x); }
	public void println(float x) { if (other != null) other.println(x); }
	public void println(int x) { if (other != null) other.println(x); }
	public void println(long x) { if (other != null) other.println(x); }
	public void println(Object x) { if (other != null) other.println(x); }
	public void println(String x) { if (other != null) other.println(x); }
	
	public void write(byte[] buf, int off, int len) { if (other != null) other.write(buf, off, len); }
	public void write(int b) { if (other != null) other.write(b); }
}