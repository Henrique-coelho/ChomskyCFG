package job;

import dto.CFGrammar;
import operations.Operations;
import operations.impl.OperationsImpl;

public class Parser {
    private static final String JSON_STRING = "{ \"glc\": [\n  [{variables}],\n  [{alphabetSymbols}],\n  [{rules}],\n  \"{startVar}\"\n]}";
    private static final String VARIABLES = "{variables}";
    private static final String ALPHABET_SYMBOLS = "{alphabetSymbols}";
    private static final String RULES = "{rules}";
    private static final String START_VAR = "{startVar}";

    OperationsImpl op;

    public CFGrammar parseGrammarToFNC(CFGrammar cfGrammar){
    	op = new OperationsImpl();
        cfGrammar = op.removeUselessVar(cfGrammar);
        cfGrammar = op.removeUnitaryRules(cfGrammar);
        cfGrammar = op.makeRulesVarOnly(cfGrammar);
        return op.limitVarFromRules(cfGrammar);
    }

    public void printFNC(CFGrammar cfGrammar){
        JSON_STRING.replace(VARIABLES, cfGrammar.getRules().toString())
                .replace(ALPHABET_SYMBOLS, cfGrammar.getAlphabetSymbols().toString())
                .replace(RULES, cfGrammar.getRules().toString())
                .replace(START_VAR, cfGrammar.getStartVar());
        System.out.println(JSON_STRING);
    }


}
