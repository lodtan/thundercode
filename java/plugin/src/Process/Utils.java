package Process;

public class Utils {

    public static String cleanString (String string){
        String newString = string.replaceAll("[(){}\\[\\]/\\\\~\"']", "\\\\$0").replaceAll(":", " ");
        System.out.println(newString);

        return newString;
    }
}
