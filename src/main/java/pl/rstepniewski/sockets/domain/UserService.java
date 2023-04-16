package pl.rstepniewski.sockets.domain;

import pl.rstepniewski.sockets.file.FileName;
import pl.rstepniewski.sockets.file.FilePath;
import pl.rstepniewski.sockets.file.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final FileService fileService;
    private List<User> userList = new ArrayList<>();
    private List<User> adminList = new ArrayList<>();

    public UserService(FileService fileService) {
        this.fileService = fileService;
    }

    public List<User> getUserList() throws IOException {
        userList = fileService.importAllUsersFromFiles(FilePath.USER_FOLDER.getFolderPath());
        return userList;
    }

    public List<User> getAdminList() throws IOException {
        adminList = fileService.importAllUsersFromFiles(FilePath.ADMIN_FOLDER.getFolderPath());
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
        UserRole userRole = user.getRole();

        switch (userRole) {
            case USER -> {
                List<User> userList = getUserList();
                if(!userList.contains(user)){
                    userList.add(user);
                   fileService.exportUserData(userList, FilePath.USER_FOLDER, FileName.USERFILENAME);
                }
            }
            case ADMIN -> {
                List<User> adminList = getAdminList();
                if(!adminList.contains(user)){
                    adminList.add(user);
                    fileService.exportUserData(adminList, FilePath.USER_FOLDER, FileName.USERFILENAME);
                }
            }
        }
    }

    public void removeUser(String userName) throws IOException {
        List<User> userList = getUserList();

        Optional<String> optionaUser = userList.stream()
                .map(User::getUsername)
                .findAny();

        if(!userList.contains(user)){
            userList.add(user);
            fileService.exportUserData(userList, FilePath.USER_FOLDER, FileName.USERFILENAME);
        }
        List<User> adminList = getAdminList();
        if(!adminList.contains(user)){
            adminList.add(user);
            fileService.exportUserData(adminList, FilePath.USER_FOLDER, FileName.USERFILENAME);
        }

    }
}
