import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main (String[] args){
        System.out.println(
            """
                CALCULATOR
                It can receive an integer numbers from 1 to 10
                The numbers can be written in Arabic or Roman numerals
                Enter a math expression (for example: 7*5 or X-VIII):"""
        );
        Scanner in = new Scanner(System.in);
        String s = calc(in.nextLine());
        in.close();
        if (!s.isEmpty()) System.out.println(s);
    }
    public static String calc(String input) {
        String o;
        String[] a;
        String in = input.replace(" ", "");
        if (indexOfRegEx(in, "[^\\+\\-\\*\\/\\dIVXivx]") >-1 ||
            indexOfRegEx(in, "[\\+\\-\\*\\/]{2,}") >-1
           ){
            printError(0);
            return "";
        }

        int i = indexOfRegEx(in, "[\\+\\-\\*\\/]");
        try{
            o = in.substring(i, i+1);
        } catch (StringIndexOutOfBoundsException e) {
            printError(1);
            return "";
        }
        try{
            a = in.split("\\"+o);
            if (a[0].isEmpty() || a[1].isEmpty()){
                printError(2);
                return "";
            }
        } catch (ArrayIndexOutOfBoundsException e){
            printError(2);
            return "";
        }
        byte n = checkType(a);
        if (n > 0) {
            int[] b = parseAndCheck(a, n == 2);
            if (b[0] < 1) {
                printError(3);
                return "";
            }
            String c = calculate(o, b, n == 2);
            if (c.isEmpty()){
                printError(5);
                return "";
            } else return c;
        } else {
            printError(4);
            return "";
        }
    }

    private static int indexOfRegEx(String string, String pattern){
        int i = -1;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(string);
        if (m.find()) {
            i = m.start();
        }
        return i;
    }
    private static void printError(int e){
        String[] a = {
                "Error: inadmissible symbol(s) or wrong expression.",
                "Error: math operation symbol is missing or invalid.",
                "Error: the expression is wrong.",
                "Error: the operands must be in range 1 to 10.",
                "Error: the operands isn't the same type or incorrect.",
                "Error: invalid operation for set of operands."
        };
        System.out.println(a[e]);
    }
    private static byte checkType(String[] a){
        byte r = 0;
        byte d = 0;
        for (String s : a) {
            if (indexOfRegEx(s, "\\D") > -1) r++;
            if (indexOfRegEx(s, "\\d") > -1) d++;
        }
        if (d == a.length && r == 0) {return 1;}
        else if (r == a.length && d == 0) {return 2;}
        else return 0;
    }
    private static int[] parseAndCheck(String[] a, boolean roman){
        int[] b = new int[a.length];
        for(int i = 0; i < a.length; i++){
            if (roman) {b[i] = rtoa(a[i]);}
            else b[i] = Integer.parseInt(a[i]);
            if (b[i] < 1 || b[i] > 10){
                return new int[a.length];
            }
        }
        return b;
    }
    private static String calculate(String o, int[] b, boolean roman){
        int r = switch (o) {
            case ("+") -> b[0] + b[1];
            case ("-") -> b[0] - b[1];
            case ("*") -> b[0] * b[1];
            case ("/") -> b[0] / b[1];
            default -> 0;
        };
        if (roman) {
            if (r > 0){return ator(r);}
            else return "";
        }
        else return String.valueOf(r);
    }
    private static String ator(int n){
        int[] A = {1, 4, 5, 9, 10, 40, 50, 90, 100};
        String[] R = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C"};
        int i = R.length-1;
        String r = "";
        while (n > 0) {
            while (A[i] > n) i--;
            r += R[i];
            n -= A[i];
        }
        return r;
    }
    private static int rtoa(String roman){
        int[] A = {4, 9, 40, 90, 1, 5, 10, 50, 100};
        String[] R = {"IV", "IX", "XL", "XC", "I", "V", "X", "L", "C"};
        String s = roman.toUpperCase();
        int r = 0;
        int i = 0;
        while (i < R.length && s.length() > 0){
            while (s.contains(R[i])){
                s = s.replaceFirst(R[i], "");
                r += A[i];
            }
            i++;
        }
        return r;
    }
}
