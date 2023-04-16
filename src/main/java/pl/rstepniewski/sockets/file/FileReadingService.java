package pl.rstepniewski.sockets.file;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import pl.rstepniewski.sockets.domain.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileReadingService implements FileManager{

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<User> importAllUsersFromFiles(String filePath) throws IOException {
        File jsonFolder = new File(filePath);

        List<File> userFiles = Files.walk(Paths.get(jsonFolder.toURI()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        List<User> allUsers = new ArrayList<>();
        List<User> newUsers = null;
        for (File userFile : userFiles)
        return allUsers;
    }

    @Override
    public void exportUserData(List<User> userList, String filePath, String jsonFilename) throws IOException {
        File jsonFile = new File(filePath+ "/" + jsonFilename);

        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());

        writer.writeValue(jsonFile, userList);
    }

}
