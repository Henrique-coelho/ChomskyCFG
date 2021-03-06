package operations.impl;

import dto.CFGrammar;
import lombok.NoArgsConstructor;
import operations.Operations;
import exceptions.AlphabetExceededException;
import utils.OperationsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class OperationsImpl implements Operations {
	
	@Override
    public CFGrammar removeLambdaRules(CFGrammar cfGrammar) {
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
	    					
	    					// Caso a regra ??? vazia, retorna uma nova regra Lambda
	    					if(newRuleAux.isEmpty()) 
	    						pendingRules.add("#");
	    					else
	    						pendingRules.add(newRuleAux);
	    				}
	    				
	    				// Formata e armazenas as novas regras, elinando regras autoreferentes no processo
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
	    	
	    	// Enquanto existir novas regras l???mbdas geradas, o processo se repete
        } while (!new HashSet<>(lambdaList).equals(new HashSet<>(lambdListOG)));
        
        // Remove as regras Lambdas, exceto para vari???vel inicial
        newGrammar.setRules(newGrammar.getRules().stream()
        		.filter(l -> (!(l.get(1).contains("#") && !(newGrammar.getStartVar().equals(l.get(0))))))
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

		//Encadeamento de cada variavel
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
							if(!newRules.contains(addRule))
								newRules.add(addRule);
						}
					});
				}
			}

		cfGrammar.setRules(newRules);
		
        return cfGrammar;
    }

    @Override
    public CFGrammar removeUselessVar(CFGrammar cfGrammar) {
        List<String> uselessVars = new ArrayList<>(cfGrammar.getVariables());
        
        // Parte A:
        // Identifica todas variaveis sem regras
        for(List<String> rule : cfGrammar.getRules()) {
        	if(uselessVars.contains(rule.get(0))) {
        		uselessVars.remove(rule.get(0));
        	}
        }
        
        // Se tiver variaveis sem regras, as regras e variaveis serao atualizadas
        if(!uselessVars.isEmpty()) {
        	List<List<String>> newRules = new ArrayList<>();
        	for(List<String> rule : cfGrammar.getRules()) {
        		List<String> newRule = new ArrayList<>();
        		String command = rule.get(1);
        		
        		// ??? removido cada variavel inutil das regras envolvidas
        		for(String uselessVar : uselessVars) {
        			if(rule.get(1).contains(uselessVar)) {
        				String oldCommand = command;
        				String newCommand = "";
        				for(int i=0;i<oldCommand.length();i++) {
        					if(!uselessVar.equals(oldCommand.substring(i,i+1))) {
        						newCommand = newCommand.concat(oldCommand.substring(i,i+1));
        					}
        				}
        				command = newCommand;
        			}
        		}
        		
        		// Adiciona a regra modificada, se ainda for existente
        		if(!command.isEmpty()) {
	        		newRule.add(0, rule.get(0));
	        		newRule.add(1, command);
	        		if(!newRules.contains(newRule) && !(newRule.get(0).equals(newRule.get(1))))
	        			newRules.add(newRule);
        		}
        	}
        	cfGrammar.setRules(newRules);
        	// Remove as variaveis unitarias novamente
        	cfGrammar = removeUnitaryRules(cfGrammar);
        	
        	// Remove as variaveis inuteis da lista de variaveis
        	List<String> newVariables = new ArrayList<>(cfGrammar.getVariables());
        	newVariables.removeAll(uselessVars);
        	cfGrammar.setVariables(newVariables);
        }
        
    	// Parte B
        // Atualiza a variaveis uteis para reconhecer somente as variaveis geradas pela variavel inicial
        List<String> oldUsefullVars;
        List<String> newUsefullVars = new ArrayList<>();
        newUsefullVars.add(cfGrammar.getStartVar());
        
        do {
        	oldUsefullVars = new ArrayList<>(newUsefullVars);
        	for(List<String> rule : cfGrammar.getRules()) {
        		if(oldUsefullVars.contains(rule.get(0))) {
        			String command = rule.get(1);
        			List<String> variables = cfGrammar.getVariables();
        			for(int i=0;i<command.length();i++) {
        				String c = command.substring(i, i+1);
        				if(variables.contains(c)) {
        					if(!newUsefullVars.contains(c)) {
        						newUsefullVars.add(c);
        					}
        				}
        			}
        		}
        	}
        } while(!new HashSet<>(oldUsefullVars).equals(new HashSet<>(newUsefullVars)));
        
        final List<String> usefullVars = new ArrayList<>(newUsefullVars);
        if(!new HashSet<>(usefullVars).equals(new HashSet<>(cfGrammar.getVariables()))) {
	        // Remove as regras que contem variaveis nao geradas
	        cfGrammar.setRules(cfGrammar.getRules().stream()
	        		.filter(l -> (usefullVars.contains(l.get(0))))
	        		.collect(Collectors.toList())
	        	);
	        
	        // Atualiza as novas variaveis
	        cfGrammar.setVariables(usefullVars);
        }
        
        return cfGrammar;
    }
    
    @Override
    public CFGrammar makeRulesVarOnly(CFGrammar cfGrammar) throws AlphabetExceededException {
		List<List<String>> newRules = new ArrayList<>();
		List<List<String>> needsNewRules = new ArrayList<>();
		List<String> symbols = new ArrayList<>(cfGrammar.getAlphabetSymbols());
		
		// Define as regras que precisam de melhorias, isto eh, |w| >= 2, onde w contem nao variaveis
		for(List<String> rule : cfGrammar.getRules()) {
			String command = rule.get(1);
			
			if((command.length() >= 2)) {
				boolean hasNonVar = false;
				for(int i=0;i<command.length();i++) {
					if(symbols.contains(command.substring(i,i+1))) {
						hasNonVar = true;
						break;
					}
				}
				if(hasNonVar) 
					needsNewRules.add(rule);
			}
		}
		
		var alphabet = Arrays.asList(new String[] {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"});

		// Identifica quantas regras uma variavel tem
		HashMap<String, Integer> heldRules = new HashMap<>();
		for(List<String> rule : cfGrammar.getRules()) {
			String variable = rule.get(0);
			if(!heldRules.containsKey(rule.get(0)))
				heldRules.put(variable, 1);
			else
				heldRules.replace(variable, heldRules.get(variable)+1);
		}
		
		// A nova lista de variaveis eh acompanhada e atualizada se necessario
		List<String> variables = cfGrammar.getVariables();
		
		// As regras |w| >= 2, onde w contem nao variaveis, sao atualizadas
		for(List<String> rule : cfGrammar.getRules()) {
			if(!needsNewRules.contains(rule))
				newRules.add(rule);
			else {
				String variable = rule.get(0);
				String command = rule.get(1);
	
				String newCommand = "";
				for(int i=0;i<command.length();i++) {
					String c = command.substring(i,i+1);
					// Se o simbolo no comando nao for uma variavel
					if(symbols.contains(c)) {
						// Procura-se uma regra onde uma variavel ja gera somente este simbolo
						String equivalentVar = null;
						for(List<String> currentRule : newRules) {
							if(currentRule.get(1).equals(c) && heldRules.get(currentRule.get(0)) == 1) {
								equivalentVar = currentRule.get(0);
								break;
							}
						}
						// Se nao houver variavel com tal regra, ela eh criada
						if(equivalentVar == null) {
							String selectedVar = null;
							for(String letter : alphabet) {
								if(!variables.contains(letter)) {
									selectedVar = letter;
									variables.add(selectedVar);
									break;
								}
							}
							// Se todas as letra do alfabeto ja representam uma variavel, o codigo lanca um erro por nao ter representacao disponivel
							if (selectedVar == null) {
								throw new AlphabetExceededException("Nao existem mais formas de representacao para novas variaveis, visto que todo o alfabeto ja foi consumido");
							}
							else {
								List<String> substituteRule = new ArrayList<>();
								// eh criada e adicionada a nova regra
								substituteRule.add(selectedVar);
								substituteRule.add(c);
								newRules.add(substituteRule);
								heldRules.put(selectedVar, 1);
								// Ela eh entao concatenada a expressao
								newCommand = newCommand.concat(selectedVar);
							}
						}
						// Se houver variavel com tal regra, ela eh concatenada a expressao
						else 
							newCommand = newCommand.concat(equivalentVar);
					}
					else
						newCommand = newCommand.concat(c);
				}
				List<String> newRule = new ArrayList<>();
				newRule.add(variable);
				newRule.add(newCommand);
				if(!newRules.contains(newRule))
					newRules.add(newRule);
			}
		}
		
		cfGrammar.setVariables(variables);
		cfGrammar.setRules(newRules);
        return cfGrammar;
    }

    @Override
    public CFGrammar limitVarFromRules(CFGrammar cfGrammar) throws AlphabetExceededException{
        var rulesList = cfGrammar.getRules();

		List<List<String>> newRuleList = new ArrayList<>();
		List<String> newVariables = new ArrayList<>(cfGrammar.getVariables());

		// Registra o n?mero de ocorr?ncias de uma mesma regra
		HashMap<String, Integer> heldRules = new HashMap<>();
		for(List<String> rule : cfGrammar.getRules()) {
			String variable = rule.get(0);
			if(!heldRules.containsKey(rule.get(0)))
				heldRules.put(variable, 1);
			else
				heldRules.replace(variable, heldRules.get(variable)+1);
		}
		
		// Persiste as regras no formato correto
		for(List<String> rule : rulesList) {
			if(rule.get(1).length()<=2){
				newRuleList.add(rule);
			}
		}
		
		// Atualiza regras do formato: v -> w, onde |w| > 2
		for(List<String> rule : rulesList) {
			if(rule.get(1).length()>2 && !newRuleList.contains(rule)){
				// Regra atual, eventualmente atualizada
				List<String> currentRule = new ArrayList<>(rule);
				do {
					// Regra modificada que vai substituir a original
					List<String> modifiedRule = new ArrayList<>();
					// Regra criada para criar a vers?o modificada anterior
					List<String> newRule = Arrays.asList(new String []{"##","##"});
					
					// ? obtido uma regra candidato para auxiliar a modificar a regra original
					var letterCandidate = OperationsUtils.getNewVarLetter(newVariables);
					var commandCandidate = currentRule.get(1).substring(1);			
					
					// Identifica se existe regra equivalente ao candidato
					String letterDefinitive = null;
					for(List<String> addedRules : newRuleList) {
						if(heldRules.get(addedRules.get(0)) == 1 && commandCandidate.equals(addedRules.get(1))) {
							letterDefinitive = addedRules.get(0);
							break;
						}
					}
					
					// Se n?o existe uma regra equivalente, ela ? criada com o candidato apresentado
					if(letterDefinitive==null){
						letterDefinitive = letterCandidate;
						heldRules.put(letterCandidate, 1);
						newVariables.add(letterCandidate);
						newRule = Arrays.asList(new String[]{letterCandidate,commandCandidate});
						// Adiciona express?o a lista para futura compara??es de equival?ncias, ? removida posteriormente
						newRuleList.add(newRule);
					}
	
					modifiedRule = Arrays.asList(new String []{currentRule.get(0),currentRule.get(1).substring(0,1).concat(letterDefinitive)});
					currentRule = new ArrayList<>(newRule);
					
					// Adiciona a regra modificada, e sua auxiliar gerada, se final
					if(!newRuleList.contains(modifiedRule))
						newRuleList.add(modifiedRule);
					currentRule = new ArrayList<>(newRule);
					if(!newRule.contains("##"))
						if(newRule.get(1).length() <= 2)
							if(!newRuleList.contains(newRule))
								newRuleList.add(newRule);
				} while (currentRule.get(1).length() > 2);
			}
		};

		cfGrammar.setRules(newRuleList.stream()
				.filter(l -> (l.get(1).length() <= 2)).distinct().collect(Collectors.toList()));
		cfGrammar.setVariables(newVariables.stream().distinct().collect(Collectors.toList()));

		for(int i = 0; i< newRuleList.size(); i++){
			if(newRuleList.get(i).get(1).length()>2){
				cfGrammar = limitVarFromRules(cfGrammar);
			}
		}

        return cfGrammar;
    }
}
