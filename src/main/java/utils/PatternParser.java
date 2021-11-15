package utils;

import exceptions.ParseException;
import fs.MyFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PatternParser {
    public static List<MyFile> parse(String pattern) throws ParseException {
        List<MyFile> out = new ArrayList<>();
        String[] elements = pattern.split(" ");
        if (elements.length < 2) {
            throw new ParseException("Sablon mora biti u obliku mkdir/mkfile ime. Sablon: " + pattern);
        }
        if (!elements[0].equals("mkdir") && !elements[0].equals("mkfile")) {
            throw new ParseException("Sablon mora poceti sa mkdir ili mkfile. Sablon: " + pattern);
        }
        boolean isFile = elements[0].equals("mkfile");
        if (!elements[1].contains("{")) {
            for (int i = 1; i < elements.length; i++) {
                String[] nameAndExtension = elements[i].split("\\.");
                if (nameAndExtension.length != 0) {
                    out.add(new MyFile(elements[i], isFile, nameAndExtension[nameAndExtension.length-1]));
                } else {
                    out.add(new MyFile(elements[i], isFile, ""));
                }
            }
        } else {
            if (elements[1].split("\\{")[0].startsWith("random")) {
                parseRandom(out, elements[1], isFile);
            } else {
                parseRange(out, elements[1], isFile);
            }
        }
        return out;
    }

    private static void parseRandom(List<MyFile> out, String element, boolean isFile) {
        String nameAndExtension = element.split("\\{")[0];
        int num = Integer.parseInt(element.substring(nameAndExtension.length() + 1, element.length() - 1));
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
            if (nameAndExtension.split("\\.").length > 1) {
                String extension = nameAndExtension.split("\\.")[1];
                out.add(new MyFile(generatedString.concat(".").concat(extension), isFile, extension));
            } else {
                out.add(new MyFile(generatedString, isFile, ""));
            }
        }
    }

    private static void parseRange(List<MyFile> out, String element, boolean isFile) throws ParseException {
        String nameAndExtension = element.split("\\{")[0];
        String range = element.substring(nameAndExtension.length() + 1, element.length() - 1);
        int left = Integer.parseInt(range.split("\\.+")[0]);
        int right = Integer.parseInt(range.split("\\.+")[1]);
        if (left > right) throw new ParseException("Levi broj u opsegu mora biti manji ili jednak desnom broju. " + left + ">" + right);
        for (int i = left; i <= right; i++) {
            String name = nameAndExtension.split("\\.")[0];
            if (nameAndExtension.split("\\.").length > 1) {
                String extension = nameAndExtension.split("\\.")[1];
                out.add(new MyFile(name.concat(String.valueOf(i)).concat(".").concat(extension), isFile, extension));
            } else {
                out.add(new MyFile(name.concat(String.valueOf(i)), isFile, ""));
            }
        }
    }
}
