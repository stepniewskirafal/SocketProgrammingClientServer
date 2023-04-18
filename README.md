# SocketProgramming
### Description
This program is a simple client/server application based on data (JSON format) transfer with sockets.

### User management
The application allows you to create a user and log in. The user can also send or check received messages and clear his inbox. In addition, the admin can delete users or grant them privileges.

### Message management
The application allows you to send (and read) private messages (up to 255 characters) between users. The unread messages box for users can store up to 5 messages (the admin inbox has unlimited capacity). When the limit is reached, the sender receives information about its overflow.

### Commands
#### For admin:
uptime, Return server running time.  
info,   Return server version and creation date.  
help,   Return list of available commands.  
stop,   Stop server and client.  

listAllUsers, Show a list of all users and their roles.  
addNewUser,   Add a new user to the app.  
deleteUser,   Delete user from the app.  
sendMessage,  Send a message to another User.  
readMessage,  Read the chosen message.  

#### For regular user:
sendMessage, Send a message to another User.  
readMessage, Read the chosen message.
