package operations.impl;

import dto.CFGrammar;
import lombok.NoArgsConstructor;
import operations.Operations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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
	    			
	    			// Se este caractere possui regra Lâmbda
	    			if(l.get(1).contains(c)) {
	    				String rule = l.get(1);
	    				
	    				// Lista todos os índices do caracter mencionado
	    				List<Integer> charsWithLambda = new ArrayList<>();
	    				for(int i=0; i<rule.length(); i++) {
	    					if (lambdaList.contains(rule.substring(i, i+1))) {
	    						charsWithLambda.add(i);
	    					}
	    				}
	    				
	    				// Cria todas permutações disponível de variações de posições do caractere e escreve as regras
	    				List<String> pendingRules = new ArrayList<>();
	    				for(HashSet<Integer> combination : permute(charsWithLambda)) {
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
	    					
	    					// Caso a regra é vazia, retorna uma nova regra Lâmbda
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
	    	
	    	// Enquanto existir novas regras lâmbdas geradas, o processo se repete
        } while (!new HashSet<>(lambdaList).equals(new HashSet<>(lambdListOG)));
        
        // Remove as regras Lâmbdas
        newGrammar.setRules(newGrammar.getRules().stream()
        		.filter(l -> (!l.get(1).contains("#")))
        		.collect(Collectors.toList())
        	);
        return newGrammar;
    }

    private List<HashSet<Integer>> permute(List<Integer> mainList) {
    	var p = permute(new ArrayList<>(), mainList);
    	var q = p.stream()
    			.map(l -> {
    				return new HashSet<>(l);
    			})
    			.distinct()
    			.collect(Collectors.toList());
    	q.add(new HashSet<>());
    	return q;
    }
    
    private List<List<Integer>> permute(List<Integer> midList, List<Integer> mainList) {
    	int size = mainList.size();
    	List<List<Integer>> combinations = new ArrayList<>();
    	List<Integer> auxList;
    	
    	for(int i=0;i<size;i++) {
    		auxList = new ArrayList<>(midList);
    		auxList.add(mainList.get(i));
    		if(auxList.size() != size) {
    			for(List<Integer> comb : permute(auxList,mainList)) {
    				combinations.add(comb);
    			}
    		}
    		else {
    			combinations.add(auxList);
    		}
    		
    	}
    	return combinations;
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
