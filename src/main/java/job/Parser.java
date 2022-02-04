package job;

import dto.CFGrammar;
import exceptions.AlphabetExceededException;
import operations.impl.OperationsImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Parser {
    private static final String JSON_STRING = "{ \"glc\": [\n  {variables},\n  {alphabetSymbols},\n  {rules},\n  \"{startVar}\"\n]}";
    private static final String VARIABLES = "{variables}";
    private static final String ALPHABET_SYMBOLS = "{alphabetSymbols}";
    private static final String RULES = "{rules}";
    private static final String START_VAR = "{startVar}";

    OperationsImpl op;

    public CFGrammar parseGrammarToFNC(CFGrammar cfGrammar) throws AlphabetExceededException{
    	op = new OperationsImpl();
        cfGrammar = op.removeLambdaRules(cfGrammar);
        cfGrammar = op.removeUnitaryRules(cfGrammar);
        cfGrammar = op.removeUselessVar(cfGrammar);
        cfGrammar = op.makeRulesVarOnly(cfGrammar);
        return op.limitVarFromRules(cfGrammar);
    }

    public void printFNC(CFGrammar cfGrammar){
        List<String> rules = new ArrayList<>();
        cfGrammar.getRules().forEach(l -> rules.add(listToString(l)));

        var variables = listToString(cfGrammar.getVariables());
        var alphabetSymbols = listToString(cfGrammar.getAlphabetSymbols());

        var output = JSON_STRING.replace(VARIABLES, variables)
                .replace(ALPHABET_SYMBOLS, alphabetSymbols)
                .replace(RULES, rules.toString())
                .replace(START_VAR, cfGrammar.getStartVar());

        System.out.println(output);
    }

    private String listToString(List<String> lista){
        var returnString = lista.stream()
                 .map(p -> p = "\""+p+"\"")
                 .collect(Collectors.toList())
                 .toString();
        return returnString;
    }


}
