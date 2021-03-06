package operations;

import dto.CFGrammar;
import exceptions.AlphabetExceededException;

public interface Operations {

    CFGrammar removeLambdaRules(CFGrammar cfGrammar);
    CFGrammar removeUnitaryRules(CFGrammar cfGrammar);
    CFGrammar removeUselessVar(CFGrammar cfGrammar);
    CFGrammar makeRulesVarOnly(CFGrammar cfGrammar) throws AlphabetExceededException;
    CFGrammar limitVarFromRules(CFGrammar cfGrammar) throws AlphabetExceededException;

}
