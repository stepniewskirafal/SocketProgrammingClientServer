package pl.rstepniewski.sockets.file;

public enum FileName {
    USERFILENAME("user.json"),
    ADMINFILENAME("admin.json");

    private final String fileName;

    FileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
