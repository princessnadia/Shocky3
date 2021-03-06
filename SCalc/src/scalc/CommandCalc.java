package scalc;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import pl.shockah.Box;
import pl.shockah.Strings;
import scommands.Command;
import shocky3.Shocky;

public class CommandCalc extends Command {
	public static final Pattern
		REGEX_VARIABLE_ASSIGN = Pattern.compile("([a-zA-Z_][a-zA-Z_0-9]*)\\s?=\\s(.*)");
	
	public CommandCalc() {
		super("calc");
	}
	
	public void call(Shocky botApp, MessageEvent<PircBotX> e, String trigger, String args) {
		String[] spl = args.split(";");
		
		String expression = null;
		Map<String, Double> variables = new HashMap<>();
		
		for (String s : spl) {
			Matcher m = REGEX_VARIABLE_ASSIGN.matcher(s);
			if (m.find()) {
				Box<Double> boxd = new Box<>();
				if (Strings.tryParseDouble( m.group(2), boxd)) {
					variables.put(m.group(1), boxd.value);
				} else {
					e.respond("Nested expressions are not implemented just yet.");
					return;
				}
			} else {
				expression = s;
			}
		}
		
		if (expression == null) {
			e.respond("Invalid expression.");
			return;
		} else {
			ExpressionBuilder exprb = new ExpressionBuilder(expression);
			for (String key : variables.keySet()) {
				exprb.variable(key);
			}
			Expression expr = exprb.build();
			expr.setVariables(variables);
			
			ValidationResult vresult = expr.validate();
			if (vresult.isValid()) {
				e.respond("" + expr.evaluate());
				return;
			} else {
				for (String error : vresult.getErrors()) {
					e.respond(error);
				}
				return;
			}
		}
	}
}