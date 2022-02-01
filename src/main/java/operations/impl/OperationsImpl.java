package operations.impl;

import dto.CFGrammar;
import lombok.NoArgsConstructor;
import operations.Operations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class OperationsImpl implements Operations {
	
    @Override
    public CFGrammar removeUselessVar(CFGrammar cfGrammar) {
        var lambdaList = cfGrammar.getRules().stream()
                .map(l -> {
                    List<String> returnList = new ArrayList<>();
                    l.stream().forEach(rule -> {
                        if(rule.contains("#")) {
                            returnList.add(l.get(0));
                        }
                    });
                    return returnList;
                })
                .flatMap(t -> t.stream())
                .collect(Collectors.toList());
        return cfGrammar;
    }

    @Override
    public CFGrammar removeUnitaryRules(CFGrammar cfGrammar) {
        //TODO
        return cfGrammar;
    }

    @Override
    public CFGrammar makeRulesVarOnly(CFGrammar cfGrammar) {
        //TODO
        return cfGrammar;
    }

    @Override
    public CFGrammar limitVarFromRules(CFGrammar cfGrammar) {
        //TODO
        return cfGrammar;
    }
}
