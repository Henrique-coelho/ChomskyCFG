package operations.impl;

import dto.CFGrammar;
import lombok.NoArgsConstructor;
import operations.Operations;
import utils.OperationsUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@NoArgsConstructor
public class OperationsImpl implements Operations {

    @Override
    public CFGrammar removeUselessVar(CFGrammar cfGrammar) {
    	List<String> lambdListOG;
    	List<String> lambdaList = new ArrayList<>();
    	CFGrammar newGrammar = cfGrammar;
        do {
        	lambdListOG = lambdaList;
        	// Identifica novas regras nulas
	    	lambdaList = newGrammar.getRules().stream()
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
	    	
	    	// Atualiza Gramatica
	    	List<List<String>> newRules = new ArrayList<>(); 
	    	for(String c : lambdaList){
	    		for(List<String> l : cfGrammar.getRules()) {
	    			if(!newRules.contains(l))
	    				newRules.add(l);
	    			
	    			// Se este caractere possui regra Lambda
	    			if(l.get(1).contains(c)) {
	    				String rule = l.get(1);
	    				
	    				// Lista todos os indices do caracter mencionado
	    				List<Integer> charsWithLambda = new ArrayList<>();
	    				for(int i=0; i<rule.length(); i++) {
	    					if (lambdaList.contains(rule.substring(i, i+1))) {
	    						charsWithLambda.add(i);
	    					}
	    				}
	    				
	    				// Cria todas permutacoes disponivel de variacoes de posicoes do caractere e escreve as regras
	    				List<String> pendingRules = new ArrayList<>();
	    				for(HashSet<Integer> combination : OperationsUtils.permute(charsWithLambda)) {
	    					String newRuleAux = "";
	    					for(int i=0; i<rule.length(); i++) {
	    						if(charsWithLambda.contains(i)) {
	    							if(combination.contains(i)) {
	    								newRuleAux = newRuleAux.concat(rule.substring(i,i+1));
	    							}
	    						}
	    						else {
	    							newRuleAux = newRuleAux.concat(rule.substring(i,i+1));
	    						}
	    					}
	    					
	    					// Caso a regra � vazia, retorna uma nova regra Lambda
	    					if(newRuleAux.isEmpty()) 
	    						pendingRules.add("#");
	    					else
	    						pendingRules.add(newRuleAux);
	    				}
	    				
	    				// Formata as novas regras
	    				for(String pendingRule : pendingRules) {
	    					List<String> newRule = new ArrayList<>();
	    					newRule.add(l.get(0));
	    					newRule.add(pendingRule);
	    					if(!newRules.contains(newRule) && !(l.get(0).equals(pendingRule)))
	    	    				newRules.add(newRule);
	    				}
	    			}
	    		}
	    	};

	    	// Atualiza as novas regras
	    	newGrammar.setRules(newRules);
	    	
	    	// Enquanto existir novas regras l�mbdas geradas, o processo se repete
        } while (!new HashSet<>(lambdaList).equals(new HashSet<>(lambdListOG)));
        
        // Remove as regras Lambdas
        newGrammar.setRules(newGrammar.getRules().stream()
        		.filter(l -> (!l.get(1).contains("#")))
        		.collect(Collectors.toList())
        	);
        return newGrammar;
    }

    @Override
    public CFGrammar removeUnitaryRules(CFGrammar cfGrammar) {
		var grammarRules = cfGrammar.getRules();
		var variables = cfGrammar.getVariables();
		List<List<String>> rulesChain = new ArrayList<>();

		var unitaryRules = grammarRules.stream()
				.filter(l -> Character.isUpperCase(l.get(1).charAt(0)) && l.get(1).length()==1)
				.collect(Collectors.toList());

		variables.forEach(r -> {
			List<String> chain = new ArrayList<>();
			chain.add(r);
			for(int i = 0; i < grammarRules.size(); i++){
				for(int j = 0; j< unitaryRules.size(); j++){
					var regra = grammarRules.get(i);
					var regraUnitaria = unitaryRules.get(j);

					if(regra.equals(regraUnitaria) && regra.get(0).equals(r)){
						chain.add(grammarRules.get(i).get(1));

						int finalI = i;
						unitaryRules.forEach(a -> {
							if(a.get(0).equals(grammarRules.get(finalI).get(1))){
								var newRule = unitaryRules.stream()
										.filter(p-> p.get(0).equals(grammarRules.get(finalI).get(1)))
										.collect(Collectors.toList())
										.get(0).get(1);
								chain.add(newRule);
							}
						});
					}
				}
			}
			rulesChain.add(chain);
		});

		List<List<String>> newRules = new ArrayList<>();

			for(int i=0; i< rulesChain.size(); i++) {
				for(int j=0; j<rulesChain.get(i).size(); j++){
					var newVariable = rulesChain.get(i).get(0);
					var analizeVariable = rulesChain.get(i).get(j);
					grammarRules.forEach(rule -> {
						String newRule = "";
						if(rule.get(0).equals(analizeVariable) && !unitaryRules.contains(rule)) {
							newRule = rule.get(1);
							List<String> addRule = new ArrayList<>();
							addRule.add(0, newVariable);
							addRule.add(1, newRule);
							newRules.add(addRule);
						}
					});
				}
			}

		cfGrammar.setRules(newRules);

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
