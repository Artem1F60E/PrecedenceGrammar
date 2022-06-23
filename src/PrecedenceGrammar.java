import java.util.*;

public class PrecedenceGrammar {
    String string;

    private static final Map<Character, List<String>> grammar;
    private static final List<List<Integer>> matrix;
    private static final Map<Character, Integer> indices;

    static {
        grammar = new HashMap<>(){{
            put('S', Arrays.asList("EC", "DC"));
            put('A', Arrays.asList("aA", "a"));
            put('B', Arrays.asList("bB", "b"));
            put('C', Arrays.asList("cC", "c"));
            put('D', List.of("A"));
            put('E', List.of("DB"));
        }};
        matrix  = new ArrayList<>(){{
            add(Arrays.asList(null, null, null, null, null, null, null, null, null,   1));
            add(Arrays.asList(null, null,  0  , null, null, null, null, null,  -1 ,   1));
            add(Arrays.asList(null, null, null, null, null, null, null, null, null,   1));
            add(Arrays.asList(null, null,  0  , null, null,  0  , null,  -1 ,  -1 ,   1));
            add(Arrays.asList(null, null,  1  , null, null,  1  , null,   1 ,   1 ,   1));
            add(Arrays.asList(null, null,  1  , null, null, null, null, null,   1 ,   1));
            add(Arrays.asList(null, null,  1  , null,  0  ,  1  ,  -1 ,   1 ,   1 ,   1 ));
            add(Arrays.asList(null, null,  1  , null, null,  0  , null,  -1 ,   1 ,   1));
            add(Arrays.asList(null, null,  0  , null, null, null, null, null,  -1 ,   1));
            add(Arrays.asList( -1 ,  -1 ,  -1 ,  -1 ,  -1 ,  -1 ,  -1 ,  -1 ,  -1 , null));}};
        indices = new HashMap<>() {{
            put('S', 0);
            put('E', 1);
            put('C', 2);
            put('D', 3);
            put('A', 4);
            put('B', 5);
            put('a', 6);
            put('b', 7);
            put('c', 8);
            put('#', 9);
        }};
    }
    public PrecedenceGrammar(String string){
        this.string = string;
    }

    private Integer getValue(char left, char right) {
        return matrix.get(indices.get(left)).get(indices.get(right));
    }

    public boolean isValid() {
        char[] array = this.string.toCharArray();
        int position = 0;

        Stack<Character> stack = new Stack<>();
        stack.add('#');
        boolean result = false;

        if (array.length == 1) {return false;}
        while (position <= array.length) {
            char c = array[position];
            if (!indices.containsKey(c)) {
                result = true;
                break;
            }
            if (c == '#' && stack.size() == 2 && stack.peek() == 'S')
                break;
            Integer relation = getValue(stack.peek(), array[position]);
            if (relation == null) {
                result = true;
                break;
            }
            if (relation <= 0) {
                stack.push(c);
                position++;
            } else {
                StringBuilder builder = new StringBuilder();
                Iterator<Map.Entry<Character, List<String>>> iterator = grammar.entrySet().iterator();
                boolean found = false;
                char leftPart = 0;
                char lastChar;
                do {
                    lastChar = stack.pop();
                    builder.insert(0, lastChar);
                } while (getValue(stack.peek(), lastChar) >= 0);

                while (iterator.hasNext() && !found) {
                    Map.Entry<Character, List<String>> entry = iterator.next();
                    if (entry.getValue().contains(builder.toString())) {
                        found = true;
                        leftPart = entry.getKey();
                    }
                }
                if (!found) {
                    result = true;
                    break;
                }
                stack.add(leftPart);
            }
        }
        try {
            StringBuilder builder = new StringBuilder();
            builder.insert(0, stack.pop());
            builder.insert(0, stack.pop());
            return !result && builder.toString().equals("#S");
        } catch (EmptyStackException e) {
            return false;
        }
    }
}