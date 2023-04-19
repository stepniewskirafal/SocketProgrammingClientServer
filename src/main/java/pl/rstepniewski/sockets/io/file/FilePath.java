package pl.rstepniewski.sockets.io.file;

/**
 * Created by rafal on 19.04.2023
 *
 * @author : rafal
 * @date : 19.04.2023
 * @project : SocketProgrammingClientServer
 */

public enum FilePath {
    USER_FOLDER( "src/main/resources/json/users/user"),
    ADMIN_FOLDER( "src/main/resources/json/users/admin"),
    USER_MESSAGE_FOLDER( "src/main/resources/json/message/user"),
    ADMIN_MESSAGE_FOLDER( "src/main/resources/json/message/admin");
    private final String folderPath;

    FilePath(final String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderPath() {
        return folderPath;
    }

}
