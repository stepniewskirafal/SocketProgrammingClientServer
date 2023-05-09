package pl.rstepniewski.sockets.io.file;

/**
 * Created by rafal on 19.04.2023
 *
 * @author : rafal
 * @date : 19.04.2023
 * @project : SocketProgrammingClientServer
 */

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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
    public <T> List<T> importDataFromJsonFiles(final String filePath, final Class<T[]> type) throws IOException {
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

    public <T> void exportDataToJsonFiles(final List<T> messageList, final FilePath filePath, final String recipient, final FileName fileName) throws IOException {
        String dataFilePath = filePath.getFolderPath()+ "/" + recipient + "/"+ fileName.getFileName();
        File jsonFile = new File(dataFilePath);
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(jsonFile, messageList);
    }

    @Override
    public <T> void exportDataToJsonFiles(final List<T> messageList, final FilePath filePath, final FileName fileName) throws IOException {
        String dataFilePath = filePath.getFolderPath()+ "/" + fileName.getFileName();
        File jsonFile = new File(dataFilePath);
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(jsonFile, messageList);
    }

    @Override
    public void deleteJsonMessagesFiles(final FilePath filePath, final String recipient) throws IOException {
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