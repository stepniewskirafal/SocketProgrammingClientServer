package pl.rstepniewski.sockets.file;

import java.io.IOException;
import java.util.List;

public interface FileManager {
    <T> List<T> importDataFromJsonFiles(String filePath, Class<T[]> type) throws IOException;

    <T> void exportDataToJsonFiles(List<T> messageList, FilePath filePath, FileName fileName) throws IOException;
    void deleteJsonMessagesFiles(FilePath filePath, String recipient) throws IOException;

}
