package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import exceptions.AlphabetExceededException;

public final class OperationsUtils {

    private OperationsUtils(){}

    public static List<HashSet<Integer>> permute(List<Integer> mainList) {
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

    private static List<List<Integer>> permute(List<Integer> midList, List<Integer> mainList) {
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
    
    public static String getNewVarLetter(List<String> varList) throws AlphabetExceededException{
		var alphabet = Arrays.asList(new String[] {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"});
		List<String> letterList = new ArrayList<>();

		varList.stream().forEach(r -> letterList.add(r));
		String newVarLetter = "";

		for(int i=0; i< alphabet.size(); i++){
			if(!letterList.contains(alphabet.get(i))){
				newVarLetter = alphabet.get(i);
				break;
			}
		}
		if (newVarLetter == null)
			throw new AlphabetExceededException("Nao existem mais formas de representacao para novas variaveis, visto que todo o alfabeto ja foi consumido");
		return newVarLetter;
	}
}
