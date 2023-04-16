package pl.rstepniewski.sockets.file;

public enum FilePath {
    USER_FOLDER( "src/main/resources/json/users/user"),
    ADMIN_FOLDER( "src/main/resources/json/users/admin"),
    USER_MESSAGE_FOLDER( "src/main/resources/json/message/user"),
    ADMIN_MESSAGE_FOLDER( "src/main/resources/json/message/admin");
    private final String folderPath;

    FilePath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderPath() {
        return folderPath;
    }

}
