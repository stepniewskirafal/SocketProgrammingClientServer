package pl.rstepniewski.sockets.domain.user;

import pl.rstepniewski.sockets.file.FileName;
import pl.rstepniewski.sockets.file.FilePath;
import pl.rstepniewski.sockets.file.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService implements UserManager{
    private final FileService fileService;
    private List<User> userList = new ArrayList<>();
    private List<User> adminList = new ArrayList<>();

    public UserService(FileService fileService) {
        this.fileService = fileService;
    }

    public Optional<User> getUserByName(String userName) throws IOException {
        userList = getUserAndAdminList();
        Optional<User> userOptional = userList.stream()
                .filter(x -> x.getUsername().equals(userName))
                .findFirst();
        return userOptional;
    }

    public List<User> getUserList() throws IOException {
        userList = fileService.importDataFromJsonFiles(FilePath.USER_FOLDER.getFolderPath(), User[].class);
        return userList;
    }

    public List<User> getAdminList() throws IOException {
        adminList = fileService.importDataFromJsonFiles(FilePath.ADMIN_FOLDER.getFolderPath(), User[].class);
        return adminList;
    }

    public List<User> getUserAndAdminList() throws IOException {
        userList = getUserList();
        adminList = getAdminList();
        List<User> allUserList = new ArrayList<>();
        allUserList.addAll(userList);
        allUserList.addAll(adminList);

        return allUserList;
    }

    public boolean addNewUser(User user) throws IOException {
        UserRole userRole = user.getRole();
        boolean result = false;

        switch (userRole) {
            case USER -> {
                List<User> userList = getUserList();
                if(!userList.contains(user)){
                    userList.add(user);
                    fileService.exportDataToJsonFiles(userList, FilePath.USER_FOLDER, FileName.USER_FILENAME);
                    result = true;
                }
            }
            case ADMIN -> {
                List<User> adminList = getAdminList();
                if(!adminList.contains(user)){
                    adminList.add(user);
                    fileService.exportDataToJsonFiles(adminList, FilePath.ADMIN_FOLDER, FileName.ADMIN_FILENAME);
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean removeUser(String userName) throws IOException {
        List<User> allUserList = getUserAndAdminList();
        boolean result = false;

        Optional<User> optionaUser = allUserList.stream()
                .filter(x -> x.getUsername().equals(userName))
                .findFirst();

        if(!optionaUser.isEmpty()){
            User userToRemove = optionaUser.get();
            switch (userToRemove.getRole()) {
                case USER -> {
                    List<User> userList = getUserList();
                    result = userList.removeIf(x -> x.getUsername().equals(userName));
                    fileService.exportDataToJsonFiles(userList, FilePath.USER_FOLDER, FileName.USER_FILENAME);
                }
                case ADMIN -> {
                    List<User> adminList = getAdminList();
                    result = adminList.removeIf(x -> x.getUsername().equals(userName));
                    fileService.exportDataToJsonFiles(adminList, FilePath.ADMIN_FOLDER, FileName.ADMIN_FILENAME);
                }
            }
        }
        return result;
    }
}
