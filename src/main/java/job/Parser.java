package job;

import dto.CFGrammar;
import operations.Operations;

public class Parser {
    private static final String JSON_STRING = "{ \"glc\": [\n  [{variables}],\n  [{alphabetSymbols}],\n  [{rules}],\n  \"{startVar}\"\n]}";

    Operations op;

    public CFGrammar parseGrammarToFNC(CFGrammar cfGrammar){
        cfGrammar = op.removeUselessVar(cfGrammar);
        cfGrammar = op.removeUnitaryRules(cfGrammar);
        cfGrammar = op.makeRulesVarOnly(cfGrammar);
        return op.limitVarFromRules(cfGrammar);
    }

    public void printFNC(CFGrammar cfGrammar){
        System.out.println(JSON_STRING);
    }


}
