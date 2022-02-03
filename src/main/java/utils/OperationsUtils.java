package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
}
