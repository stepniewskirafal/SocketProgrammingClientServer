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

    public <T> List<T> importDataFromJsonFiles(String filePath, Class<T[]> type) throws IOException {
        File jsonFolder = new File(filePath);
        List<File> userFiles = Files.walk(Paths.get(jsonFolder.toURI()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        List<T> allDataList = new ArrayList<>();
        for (File userFile : userFiles) {
            List<T> list = Arrays.stream(objectMapper.readValue(userFile, type)).toList();

            allDataList.addAll(list);
        }

        return allDataList;
    }


    public <T> void exportDataToJsonFiles(List<T> messageList, FilePath filePath, String recipient, FileName fileName) throws IOException {
        String dataFilePath = filePath.getFolderPath()+ "/" + recipient + "/"+ fileName.getFileName();
        File jsonFile = new File(dataFilePath);
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(jsonFile, messageList);
    }
    @Override
    public <T> void exportDataToJsonFiles(List<T> messageList, FilePath filePath, FileName fileName) throws IOException {
        String dataFilePath = filePath.getFolderPath()+ "/" + fileName.getFileName();
        File jsonFile = new File(dataFilePath);
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