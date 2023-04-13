package pl.rstepniewski.sockets.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.rstepniewski.sockets.domain.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileReadingService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<User> getAllUsersFromFiles(String jsonUserFolder) throws IOException {
        File jsonFolder = new File(jsonUserFolder);

        List<File> userFiles = Files.walk(Paths.get(jsonFolder.toURI()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        List<User> allUsers = null;
        for (File userFile : userFiles) {
            User[] users = objectMapper.readValue(userFile, User[].class);
            allUsers = Arrays.stream(users).collect(Collectors.toList());
        }
        return allUsers;
    }

}
