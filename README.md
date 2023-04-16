# SocketProgramming
### Description
This program is a simple client/server application based on data (JSON format) transfer with sockets.

### User management
The application allows you to create a user and log in. The user can also send or check received messages and clear his inbox. In addition, the admin can delete users or grant them privileges.

### Message management
The application allows you to send (and read) private messages (up to 255 characters) between users. The unread messages box for users can store up to 5 messages (the admin inbox has unlimited capacity). When the limit is reached, the sender receives information about its overflow.

### Commands
For admin:

change - change user's privileges.
delete - delete user.
For regular user:

send - send message to the other user.
inbox - check your messages.
clear - clear your message box.
uptime - returns server lifetime.
info - returns server's version and creation date.
help - returns list of possible commands with short description.
logout - log user out from his account.
stop - stops server and client running.
