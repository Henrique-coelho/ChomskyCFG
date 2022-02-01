package operations;

import dto.CFGrammar;

public interface Operations {

    CFGrammar removeUselessVar(CFGrammar cfGrammar);
    CFGrammar removeUnitaryRules(CFGrammar cfGrammar);
    CFGrammar makeRulesVarOnly(CFGrammar cfGrammar);
    CFGrammar limitVarFromRules(CFGrammar cfGrammar);

}
