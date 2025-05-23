package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SanitizeJSON {
    public static List<String> sanitizeJSON(String filePath) throws IOException {
        String json = readJsonFile(filePath);
        String[] jsonArray = json.split("[\\s:]+");
        List<String> jsonList = new ArrayList<>();
        for (String c : jsonArray) {
            if (c.length() == 1) {
                if (c.equals(",")) 
                    jsonList.add("#");
                else 
                    jsonList.add(c);
                continue;
            }

            if (c.charAt(0) == '{' || c.charAt(0) == '['  || c.charAt(0) == '}'  || c.charAt(0) == ']') {
                jsonList.add(String.valueOf(c.charAt(0)));
                c = c.substring(1, c.length());
            }

            if (c.length() == 1) {
                if (c.equals(",")) 
                    jsonList.add("#");
                else 
                    jsonList.add(c);
                continue;
            }

            if (c.charAt(c.length()-1) == ',') {
                jsonList.add(c.substring(0, c.length()-1));
                jsonList.add("#");
            } else if (c.charAt(c.length()-1) == '{' || c.charAt(c.length()-1) == '['  || c.charAt(c.length()-1) == '}'  || c.charAt(c.length()-1) == ']') {
                jsonList.add(c.substring(0, c.length()-1));
                jsonList.add(String.valueOf(c.charAt(c.length()-1)));
            } else {
                jsonList.add(c);
            }
        }
        return jsonList;
    }

    
    public static String readJsonFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }


    // public static void main(String[] args) {
    //     try {
    //         List<String> jsonContent = sanitizeJSON("D:\\TFE2-code-gaetan\\ValidatingJSONDocumentsWithLearnedVPA\\schemas\\benchmarks\\basicTypes\\Documents\\basicTypes.json\\Random\\basicTypes.json-valid-847.json");
    //         System.out.println(jsonContent.toString());
    //     } catch (IOException e) {
    //         System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
    //     }
    // }
}
