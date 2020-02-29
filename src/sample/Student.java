package sample;

import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;

public class Student {
        private String name;//name of the main folder of the student
        private ArrayList<Integer> tokens = new ArrayList<>(); //stores the tokenized source code
        private int unOperators = 0;
        private int sumOperators = 0;
        private int unOperands = 0;
        private int sumOperands=0;

        public String getName() {
            return name;
        }
        public void setName(String studentName){
            int length;
                length = studentName.length();
            if(studentName.endsWith(".cpp")){
                name = studentName.substring(0,length-4);
            }
            else if(studentName.endsWith(".java")){
                name = studentName.substring(0,length-5);
            }
            else name = studentName;
        }

    public void addToken(String filePath) throws IOException {
        /*Author: TJ Contreras
         *This code is intended for Source code to tokenize
         *
         */

        String reserveWords[] = {"abstract", "assert", "boolean", "break", "byte", "case",
                "catch", "char", "class", "const", "continue", "default",
                "double", "do", "else", "enum", "extends", "false",
                "final", "finally", "float", "for", "goto", "if",
                "implements", "import", "instanceof", "int", "interface", "long",
                "native", "new", "null", "package", "private", "protected",
                "public", "return", "short", "static", "strictfp", "super",
                "switch", "synchronized", "this", "throw", "throws", "transient",
                "true", "try", "void", "volatile", "while"};

        char[] seperator = {',', '(', ')', '[', ']', '{', ']', ';'};

        char[] operator = {'+', '-', '/', '=', '!', '*', '%', '<', '>'};

        FileReader rd = new FileReader(filePath);
        StreamTokenizer st = new StreamTokenizer(rd);
        st.parseNumbers();
        st.wordChars('_', '_');
        st.eolIsSignificant(true);
        st.ordinaryChars(0, ' ');
        st.ordinaryChar('\n');
        st.slashSlashComments(true);
        st.slashStarComments(true);
        int token;
        do {
            token = st.nextToken();


            //execute if the token is digit
            if(token == StreamTokenizer.TT_NUMBER){
                tokens.add(0);
            }

            //execute if the token is a string literal
            if (token == (int)'"') {
                if (st.sval != null)
                    tokens.add(1);
            }




            //execute if the token is a word
            if(token == StreamTokenizer.TT_WORD){
                String word = st.sval;
                boolean found = false;
                for (String reserveWord : reserveWords) {
                    //check first if the token is a reserve word
                    if (word.equalsIgnoreCase(reserveWord)){
                        tokens.add(2);
                        found =true;
                    }
                }

                //if it is not a reserve word then it is a identifier
                if(!found){
                    tokens.add(3);
                }


            }


            else{
                //checks if the token is operator
                for (char c : operator) {
                    if (token == c){
                        tokens.add(4);
                    }
                }

                //checks if the token is separator
                for(char c: seperator){
                    if(token ==c){
                        tokens.add(5);
                    }
                }
            }


        } while (token != StreamTokenizer.TT_EOF); // loops until the end of line reaches

        //close reader
        rd.close();
    }

    public ArrayList <Integer> getToken(){
        return tokens;
    }

    public void setMetrics(String path) throws IOException {
        /*Author: TJ Contreras
         *This code is intended for Source code to tokenize
         *
         */
        String[] operators = {"assert","boolean", "case", "char", "const", "double", "do", "else", "enum", "extends",
                "final", "finally", "float", "for", "goto", "if", "int", "interface", "long", "return", "short", "super",
                "switch", "true", "try", "while",
                "<", ">", "<=", "=<", "=>", ">=",
                "--", "++", "+=", "-=", "=+", "=-", "==",
                "+", "-", "/", "=", "!", "*", "%", "^",
                "&&", "||", "!=",
                "(", "[", ",", "{", ";"};

        ArrayList<String> uniqueOperators = new ArrayList<>(Arrays.asList("assert", "boolean", "case", "char", "const", "double", "do", "else", "enum", "extends",
                "final", "finally", "float", "for", "goto", "if", "int", "interface", "long", "return", "short", "super",
                "switch", "true", "try", "while",
                "<", ">", "<=", "=<", "=>", ">=",
                "--", "++", "+=", "-=", "=+", "=-", "==",
                "+", "-", "/", "=", "!", "*", "%", "^",
                "&&", "||", "!=",
                "(", "[", ",", "{", ";"));
        ArrayList<String> uniqueOperands = new ArrayList<>();

        //total operators and operands stores the count of occurrences of the variable in a given software
        int totalOperators = 0;
        int totalOperands = 0;

        FileReader rd = new FileReader(path);
        StreamTokenizer st = new StreamTokenizer(rd);

        st.parseNumbers();
        st.wordChars('_', '_');
        st.eolIsSignificant(true);
        st.ordinaryChars(0, ' ');
        st.ordinaryChar('\n');
        st.slashSlashComments(true);
        st.slashStarComments(true);
        int token = 0;
        while (token != StreamTokenizer.TT_EOF) {
            token = st.nextToken();
            switch (token) {

                case StreamTokenizer.TT_NUMBER:
                    totalOperands++;
                    double num = st.nval;
                    boolean found = false;
                    for (int i = 0; i < uniqueOperands.size(); i++) {
                        if (uniqueOperands.get(i).equals(Double.toString(num))) {
                            found = true;
                        }
                    }
                    if (!found) uniqueOperands.add(Double.toString(num));
                    break;

                case StreamTokenizer.TT_WORD: {
                    String word = st.sval;
                    boolean found2 = false;
                    for (String operator : operators) {
                        //check first if the token is a reserve word
                        if (word.equalsIgnoreCase(operator)) {
                            totalOperators++;
                            remover(uniqueOperators, word);
                            found2 = true;
                        }
                    }
                    if (!found2) {
                        totalOperands++;
                        boolean found3 = false;
                        for (int i = 0; i < uniqueOperands.size(); i++) {
                            if (uniqueOperands.get(i).equals(word)) {
                                found3 = true;
                            }
                        }
                        if (!found3) uniqueOperands.add(word);
                    }
                    break;
                }

                case '"':
                {
                    if (st.sval != null) {
                        totalOperands++;
                    }
                    break;
                }

                //switch case for +,++,+=
                case '+':
                    totalOperators++;
                    int s = st.nextToken();
                    switch (s) {
                        case '+':
                            remover(uniqueOperators, "++");
                            break;
                        case '=':
                            remover(uniqueOperators, "+=");
                            break;
                        default:
                            remover(uniqueOperators, "+");
                            st.pushBack();
                            break;
                    }
                    break;

                //switch case for -,--,-=
                case '-':
                    totalOperators++;
                    int x = st.nextToken();
                    switch (x) {
                        case '-':
                            remover(uniqueOperators, "--");
                            break;
                        case '=':
                            remover(uniqueOperators, "-=");
                            break;
                        default:
                            remover(uniqueOperators, "-");
                            st.pushBack();
                            break;
                    }
                    break;

                case '/':
                    totalOperators++;
                    remover(uniqueOperators, "/");
                    break;

                case '*':
                    totalOperators++;
                    remover(uniqueOperators, "*");
                    break;

                case '!':
                    totalOperators++;
                    int t = st.nextToken();
                    switch (t) {
                        case '=':
                            remover(uniqueOperators, "!=");
                            break;
                        default:
                            remover(uniqueOperators,"!");
                            break;
                    }
                    break;

                case '&':
                {
                    totalOperators++;
                    int o = st.nextToken();
                    switch (o) {
                        case '&':
                            remover(uniqueOperators, "&&");
                            break;
                        default:
                            break;
                    }
                    break;
                }

                case '|': {
                    totalOperators++;
                    int u = st.nextToken();
                    switch (u) {
                        case '|':
                            remover(uniqueOperators, "||");
                            break;
                        default:
                            break;
                    }
                    break;
                }

                case '<': {
                    totalOperators++;
                    int v = st.nextToken();
                    switch (v) {
                        case '=':
                            remover(uniqueOperators, "<=");
                            break;
                        case '<':
                            remover(uniqueOperators, "<<");
                            break;
                        default:
                            st.pushBack();
                            remover(uniqueOperators, "<");
                            break;
                    }
                }


                case '>': {
                    totalOperators++;
                    int b = st.nextToken();
                    switch (b) {
                        case '=':
                            remover(uniqueOperators, ">=");
                            break;
                        case '<':
                            remover(uniqueOperators, ">>");
                            break;
                        default:
                            st.pushBack();
                            remover(uniqueOperators, ">");
                            break;
                    }
                    break;
                }

                case '=': {
                    totalOperators++;
                    int n = st.nextToken();
                    switch (n) {
                        case '=':
                            remover(uniqueOperators, "==");
                            break;
                        case '<':
                            remover(uniqueOperators, "=<");
                            break;
                        case '>':
                            remover(uniqueOperators, "=>");
                            break;
                        case '+':
                            remover(uniqueOperators, "=+");
                            break;
                        case '-':
                            remover(uniqueOperators, "=-");
                            break;
                        default:
                            st.pushBack();
                            remover(uniqueOperators, "=");
                            break;
                    }
                    break;
                }

                case '[': {
                    totalOperators++;
                    remover(uniqueOperators, "[");
                    break;
                }

                case '(': {
                    totalOperators++;
                    remover(uniqueOperators, "(");
                    break;
                }

                case '{': {
                    totalOperators++;
                    remover(uniqueOperators, "{");
                    break;
                }

                case ';': {
                    totalOperators++;
                    remover(uniqueOperators, ";");
                    break;
                }





            }
        }
        rd.close();

        //update the parameters for the software metric
        this.unOperators =unOperators+ (operators.length - uniqueOperators.size());
        this.unOperands = unOperands+ uniqueOperands.size();
        this.sumOperators = sumOperators + totalOperators;
        this.sumOperands = sumOperands + totalOperands;
    }

     private void remover(ArrayList<String> arr, String del) {
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).equals(del)) {
                arr.remove(i);
            }

        }
    }

    public int getSumOperators() {
        return sumOperators;
    }

    public int getSumOperands() {
        return sumOperands;
    }

    public int getUnOperators() {
        return unOperators;
    }

    public int getUnOperands(){
            return unOperands;
    }
}

