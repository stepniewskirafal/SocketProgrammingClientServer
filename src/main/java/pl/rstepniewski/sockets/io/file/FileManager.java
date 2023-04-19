package pl.rstepniewski.sockets.io.file;

/**
 * Created by rafal on 19.04.2023
 *
 * @author : rafal
 * @date : 19.04.2023
 * @project : SocketProgrammingClientServer
 */

import java.io.IOException;
import java.util.List;

public interface FileManager {
    <T> List<T> importDataFromJsonFiles(String filePath, Class<T[]> type) throws IOException;

    <T> void exportDataToJsonFiles(List<T> messageList, FilePath filePath, FileName fileName) throws IOException;
    void deleteJsonMessagesFiles(FilePath filePath, String recipient) throws IOException;

}
