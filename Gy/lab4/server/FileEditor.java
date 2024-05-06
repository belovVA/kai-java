
import java.io.FileWriter;
        import java.io.IOException;
        import java.util.Date;

public class FileEditor {
    private String journal_path;

    FileEditor(String path) {
        this.journal_path = path;
    }

    public void save(String log) {
        Date date = new Date();
        try(FileWriter fw = new FileWriter(journal_path, true);) {
            fw.write( '\n' + date.toString().substring(0, 19) + ": " + log);
            fw.flush();
        } catch (IOException ignored) {}
    }
}
