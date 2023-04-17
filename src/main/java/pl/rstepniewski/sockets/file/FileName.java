package pl.rstepniewski.sockets.file;

public enum FileName {
    USER_FILENAME("user.json"),
    ADMIN_FILENAME("admin.json"),
    MESSAGE_FILENAME("message.json");

    private final String fileName;

    FileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
