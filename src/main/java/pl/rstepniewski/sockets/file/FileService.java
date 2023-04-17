package pl.rstepniewski.sockets.file;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import pl.rstepniewski.sockets.domain.message.Message;
import pl.rstepniewski.sockets.domain.user.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileService implements FileManager{

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<User> importUsersFromJsonFiles(String filePath) throws IOException {
        File jsonFolder = new File(filePath);

        List<File> userFiles = Files.walk(Paths.get(jsonFolder.toURI()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        List<User> allUsers = new ArrayList<>();
        List<User> newUsers = null;
        for (File userFile : userFiles) {
            newUsers = Arrays.stream(objectMapper.readValue(userFile, User[].class)).toList();
            allUsers.addAll(newUsers);
        }

        return allUsers;
    }

    @Override
    public void exportUsersToJsonFiles(List<User> userList, FilePath filePath, FileName fileName) throws IOException {    /*jedna funckja do exporrtu i czytania import Json i export Json*/
        File jsonFile = new File(filePath.getFolderPath()+ "/" + fileName.getFileName());
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());

        writer.writeValue(jsonFile, userList);
    }

    @Override
    public List<Message> importMessagesFromJsonFiles(String filePath) throws IOException {
        File jsonFolder = new File(filePath);

        List<File> userFiles = Files.walk(Paths.get(jsonFolder.toURI()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        List<Message> allMessageList = new ArrayList<>();
        List<Message> newMessageList = null;
        for (File userFile : userFiles) {
            newMessageList = Arrays.stream(objectMapper.readValue(userFile, Message[].class)).toList();
            allMessageList.addAll(newMessageList);
        }

        return allMessageList;
    }

    @Override
    public void exportMessagesFromJsonFiles(List<Message> messageList, FilePath filePath, String recipient, FileName fileName) throws IOException {
        File jsonFile = new File(filePath.getFolderPath()+ "/" + recipient + "/"+ fileName.getFileName());
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(jsonFile, messageList);
    }

    @Override
    public void deleteJsonMessagesFiles(FilePath filePath, String recipient) throws IOException {
        File jsonFolder = new File(filePath.getFolderPath() + "/" + recipient);

        List<File> userFiles = Files.walk(Paths.get(jsonFolder.toURI()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        for (File userFile : userFiles) {
            userFile.delete();
        }
    }

}