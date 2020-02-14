package Process;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String cleanString (String string){
        String newString = string.replaceAll("[(){}\\[\\]/\\\\~\"']", "\\\\$0").replaceAll(":", " ");

        return newString;
    }

    public static String switchHTMLToCode(String html) {
        return html.replace("<br>", "\n").replace("&nbsp;", "\t").replace("&lt;", "<")
                .replace("&gt;", ">");
    }

    public static String getCurrentFileFrom(Project project) {
        String fileName = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument().toString();
        return fileName;
    }

    public static String getLanguageFrom(String fileName) {
        Pattern p = Pattern.compile("\\.(\\w*)]");
        Matcher m = p.matcher(fileName);

        if (m.find()) {
            String language = m.group(1);
            if(language.equals("rb"))
                language= "ruby";
            else if(language.equals("py"))
                language= "python";
            else if(language.equals("cpp"))
                language="c++";
            return language;
        } else return "";
    }

    public static ArrayList<String> getTrendsFor(String language) throws IOException {
        // TreeMap in reverse order to facilitate getting the best tags
        Map<String, Integer> trendsOccurrences = new TreeMap<>(Collections.reverseOrder());

        BufferedReader in = new BufferedReader(new InputStreamReader(
                Utils.class.getResourceAsStream("/properties/tags_occurrences.csv")));

        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);

        for (CSVRecord record : records) {
            // Get the number of occurrences of pair appearances for a specific tag
            trendsOccurrences.put(record.get("tagName"), Integer.parseInt(record.get(language)));
        }

        //LinkedHashMap preserve the ordering of elements in which they are inserted
        LinkedHashMap<String, Integer> reverseSortedTrends = new LinkedHashMap<>();

        //Use Comparator.reverseOrder() for reverse ordering
        trendsOccurrences.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedTrends.put(x.getKey(), x.getValue()));

        ArrayList<String> trendsList = new ArrayList<>(reverseSortedTrends.keySet());

        return trendsList;
    }
}
