package pl.rstepniewski.sockets.file;

public enum FilePath {
    USER_FOLDER( "src/main/resources/json/users/user"),
    ADMIN_FOLDER( "src/main/resources/json/users/admin");

    private String folderPath;

    FilePath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderPath() {
        return folderPath;
    }

}
