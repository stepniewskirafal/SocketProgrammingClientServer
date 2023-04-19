package pl.rstepniewski.sockets.io.file;

/**
 * Created by rafal on 19.04.2023
 *
 * @author : rafal
 * @date : 19.04.2023
 * @project : SocketProgrammingClientServer
 */
public enum FileName {
    USER_FILENAME("user.json"),
    ADMIN_FILENAME("admin.json"),
    MESSAGE_FILENAME("message.json");

    private final String fileName;

    FileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
