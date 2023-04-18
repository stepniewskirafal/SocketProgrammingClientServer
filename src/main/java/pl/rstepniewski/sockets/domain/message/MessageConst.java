package pl.rstepniewski.sockets.domain.message;

public enum MessageConst {
    MAX_LENGTH_OF_MESSAGE(255);

    private final int messageLenght;

    MessageConst(int messageLenght) {
        this.messageLenght = messageLenght;
    }

    public int getMessageLenght() {
        return messageLenght;
    }
}
