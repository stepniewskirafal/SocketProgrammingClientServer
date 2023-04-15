package pl.rstepniewski.sockets.domain;

import pl.rstepniewski.sockets.file.FilePath;
import pl.rstepniewski.sockets.file.FileReadingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final FileReadingService fileReadingService;
    private List<User> userList = new ArrayList<>();
    private List<User> adminList = new ArrayList<>();

    public UserService(FileReadingService fileReadingService) {
        this.fileReadingService = fileReadingService;
    }

    public List<User> getUserList() throws IOException {
        userList = fileReadingService.getAllUsersFromFiles(FilePath.USER_FOLDER.getFolderPath());
        return userList;
    }

    public List<User> getAdminList() throws IOException {
        adminList = fileReadingService.getAllUsersFromFiles(FilePath.ADMIN_FOLDER.getFolderPath());
        return adminList;
    }

    public List<User> getAllUserList() throws IOException {
        userList = getUserList();
        adminList = getAdminList();
        List<User> allUserList = new ArrayList<>();
        allUserList.addAll(userList);
        allUserList.addAll(adminList);

        return allUserList;
    }

    public void addUser(User user) throws IOException {
        boolean contains = getAllUserList().contains(user);
        if(!contains){
            userList.add(user);           //*  nie do listy tylko do pliku   *//
        }
    }

    public void addAdnin(User admin){
        boolean contains = adminList.contains(admin);
        if(!contains){
            adminList.add(admin);
        }
    }

    public void removeUser(User user){
        userList.remove(user);

    }
    public void removeAdnin(User admin){
        adminList.remove(admin);
    }
}
