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
        do {
        	lambdListOG = lambdaList;
        	// Identifica novas regras nulas
	    	lambdaList = cfGrammar.getRules().stream()
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
	    			if(l.get(1).contains(c)) {
	    				
	    			}
	    		}
	    	};
	    	
	    	List<Integer> t = new ArrayList<>();
	    	t.add(1);
	    	t.add(2);
	    	t.add(3);
	    	
	    	List<HashSet<Integer>> p = permute(t);
	    	
	    	List<String> llfinal = lambdaList;
	    	CFGrammar cfAux = cfGrammar;
	    	cfGrammar.getRules().stream()
	    		.map(l -> {
	    			l.get(1);
	    			Boolean aux = false;
	    			
	    			return null;
	    		});
	    	
        } while (!new HashSet<>(lambdaList).equals(new HashSet<>(lambdListOG)));
        return cfGrammar;
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
