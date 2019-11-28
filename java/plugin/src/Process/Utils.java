package Process;

public class Utils {

    public static String cleanString (String string){
        String newString = string.replaceAll("[(){}\\[\\]/\\\\~\"']", "\\\\$0").replaceAll(":", " ");
        System.out.println(newString);

        return newString;
    }

    public static String switchHTMLToCode(String html){
        return html.replace("<br>", "\n").replace("&nbsp;", "\t").replace("&lt;", "<")
                .replace("&gt;", ">");
    }
}
