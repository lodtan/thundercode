package Process;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;


public class FileModif {
    private String path;
    private String code;
    private int lineNumber;
    private Project project;

    public FileModif(String path, String code, int lineNumber, Project project) {
        this.path = path;
        this.project = project;
        this.code = Utils.switchHTMLToCode(code);
        this.code = this.code.substring(3, this.code.length()-4);
        this.lineNumber = lineNumber;
    }

    public void changeCode() {
        try {


            VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(new File(path));
            Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
            BufferedReader br = new BufferedReader(new FileReader(virtualFile.getPath()));
            String currentLine;
            int i = 1;
            String line, newText = "";
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                int nbTab=0;
                if (i == lineNumber -2){
                    nbTab = StringUtils.countMatches(line, "\t");
                }
                if (i != lineNumber)
                    stringBuilder.append(line+"\n");

                else {
                    //code.replaceAll("\n", "\n" + "\t".repeat(nbTab));
                    stringBuilder.append(code);
                }
                i++;
            }
            String s = stringBuilder.toString();
            document.setText(s);
        }
        catch (Exception e){

        }
    }
}
