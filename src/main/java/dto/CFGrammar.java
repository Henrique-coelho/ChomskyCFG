package dto;

import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class CFGrammar {

    private List<String> variables;
    private List<String> alphabetSymbols;
    private List<List<String>> rules;
    private String startVar;

    public CFGrammar(CFGrammarDTO dto) {
        this.variables = listObjToString(Collections.singletonList(dto.getGlc().get(0)));
        this.alphabetSymbols = listObjToString(Collections.singletonList(dto.getGlc().get(1)));
        this.rules = listObjToListString(Collections.singletonList((Collections.singletonList(dto.getGlc().get(2)))));
        this.startVar = dto.getGlc().get(3).toString();
    }

    public static List<String> listObjToString(List<?> list){
        return list.stream()
                .map(obj -> {
                    var stringList = Objects.toString(obj, null);
                    StringBuilder sb = new StringBuilder(stringList);
                    sb.deleteCharAt(stringList.length() - 1);
                    sb.deleteCharAt(0);
                    return Arrays.asList(sb.toString().replace(" ","")
                            .split(","));
                })
                .flatMap(s -> s.stream())
                .collect(Collectors.toList());
    }

    public static List<List<String>> listObjToListString(List<List<Object>> list) {
        var obj = list.get(0).get(0);

        List<?> lista = new ArrayList<>();
        if (obj.getClass().isArray()) {
            lista = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            lista = new ArrayList<>((Collection<?>)obj);
        }
        return (List<List<String>>) lista;
    }
}
