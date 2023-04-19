package pl.rstepniewski.sockets.domain.user;

/**
 * Created by rafal on 19.04.2023
 *
 * @author : rafal
 * @date : 19.04.2023
 * @project : SocketProgrammingClientServer
 */

import pl.rstepniewski.sockets.io.file.FileName;
import pl.rstepniewski.sockets.io.file.FilePath;
import pl.rstepniewski.sockets.io.file.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService implements UserManager{
    private final FileService fileService;
    private List<User> userList = new ArrayList<>();
    private List<User> adminList = new ArrayList<>();

    public UserService(final FileService fileService) {
        this.fileService = fileService;
    }
    @Override
    public Optional<User> getUserByName(final String userName) throws IOException {
        userList = getUserAndAdminList();
        return userList.stream()
                .filter(x -> x.getUsername().equals(userName))
                .findFirst();
    }

    @Override
    public List<User> getUserList() throws IOException {
        userList = fileService.importDataFromJsonFiles(FilePath.USER_FOLDER.getFolderPath(), User[].class);
        return userList;
    }
    @Override
    public List<User> getAdminList() throws IOException {
        adminList = fileService.importDataFromJsonFiles(FilePath.ADMIN_FOLDER.getFolderPath(), User[].class);
        return adminList;
    }
    @Override
    public List<User> getUserAndAdminList() throws IOException {
        userList = getUserList();
        adminList = getAdminList();
        final List<User> allUserList = new ArrayList<>();
        allUserList.addAll(userList);
        allUserList.addAll(adminList);

        return allUserList;
    }
    @Override
    public boolean addNewUser(final User user) throws IOException {
        boolean result = false;
        final UserRole role = user.getRole();
        final List<User> userList;

        if (role == UserRole.USER) {
            userList = getUserList();
            if(!userList.contains(user)){
                userList.add(user);
                fileService.exportDataToJsonFiles(userList, FilePath.USER_FOLDER, FileName.USER_FILENAME);
                result = true;
            }
        } else if (role == UserRole.ADMIN) {
            adminList = getAdminList();
            if(!adminList.contains(user)){
                adminList.add(user);
                fileService.exportDataToJsonFiles(adminList, FilePath.ADMIN_FOLDER, FileName.ADMIN_FILENAME);
                result = true;
            }
        }

        return result;
    }
    @Override
    public boolean removeUser(final String userName) throws IOException {
        List<User> allUserList = getUserAndAdminList();
        boolean result = false;

        Optional<User> optionaUser = allUserList.stream()
                .filter(x -> x.getUsername().equals(userName))
                .findFirst();

        if(!optionaUser.isEmpty()){
            User userToRemove = optionaUser.get();

            UserRole role = userToRemove.getRole();
            if(role == UserRole.USER){
                List<User> userList = getUserList();
                result = userList.removeIf(x -> x.getUsername().equals(userName));
                fileService.exportDataToJsonFiles(userList, FilePath.USER_FOLDER, FileName.USER_FILENAME);
            } else if (role == UserRole.ADMIN) {
                List<User> adminList = getAdminList();
                result = adminList.removeIf(x -> x.getUsername().equals(userName));
                fileService.exportDataToJsonFiles(adminList, FilePath.ADMIN_FOLDER, FileName.ADMIN_FILENAME);
            }
        }
        return result;
    }
}
