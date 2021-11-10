package utils;

import exceptions.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatternParser {
    public static List<String> parse(String pattern) {
        List<String> out = new ArrayList<>();
        String[] elements = pattern.split(" ");
        if (elements.length < 2) {
            throw new ParseException();
        }
        if (!elements[0].equals("mkdir") && !elements[0].equals("mkfile")) {
            throw new ParseException();
        } else {
            out.add(elements[0]);
        }
        if (!elements[1].contains("{")) {
            for (int i = 1; i < elements.length; i++) {
                out.add(elements[i]);
            }
        } else {
            if (elements[1].split("\\{")[0].equalsIgnoreCase("random")) {
                parseRandom(out, elements[1]);
            } else {
                parseRange(out, elements[1]);
            }
        }
        return out;
    }

    private static void parseRandom(List<String> out, String element) {
        int num = Integer.parseInt(element.substring(7, element.length() - 1));
        for (int i = 0; i < num; i++) {
            int leftLimit = 48; // '0'
            int rightLimit = 122; // 'z'
            int targetStringLength = 10;
            Random random = new Random();
            String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .filter(j -> (j <= 57 || j >= 65) && (j <= 90 || j >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            out.add(generatedString);
        }
    }

    private static void parseRange(List<String> out, String element) {
        String name = element.split("\\{")[0];
        String range = element.substring(name.length() + 1, element.length() - 1);
        int left = Integer.parseInt(range.split("\\.+")[0]);
        int right = Integer.parseInt(range.split("\\.+")[1]);
        if (left > right) throw new ParseException();
        for (int i = left; i <= right; i++) {
            out.add(name.concat(String.valueOf(i)));
        }
    }
}
