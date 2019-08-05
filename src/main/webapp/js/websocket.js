let websocketUri = "ws://" + window.location.host + "/websocket/"+sessionStorage.getItem("userId")+"/"+sessionStorage.getItem("username");
let websocket = new WebSocket(websocketUri);

websocket.onmessage = function(event) { onMessage(event) };
websocket.onerror = function(event) { onError(event) };


/**
 * Function handling message when received over websocket.
 * @param event contains information about the message received. event.data contains the message.
 */
function onMessage(event) {
    let message = JSON.parse(event.data);

    if(message.newConnection !== undefined){
        handleNewConnection(message);
    }else if(message.newGroupChat !== undefined) {
        createGroupChatElements([message]);
    }else if(message.groupChatId === 0){
       handlePrivateMessage(message);
    }else if(message.groupChatId !== undefined){
        handleGroupChatMessage(message);
    }
}

/**
 * Function handling new connecting user
 * @param user user information about the new connection
 */
function handleNewConnection(user){
    let userFromDb = usersFromDb.find(e => {
        return user.userId == e;
    });
    if(userFromDb === undefined){
        createUsernameElements([user]);
    }
}

/**
 * Function handling private message
 * @param message contains information about the message received
 */
function handlePrivateMessage(message){
    if (message.userId1 === userIdCurrentChat) {
        createReceivedMessageElement(message);
    }else{
        createMessageNotificationElement(message);
    }
}

/**
 * Function handling group chat message
 * @param message contains information about the message received
 */
function handleGroupChatMessage(message){
    if(message.groupChatId === currentGroupChat){
        createReceivedMessageElementGroupChat(message);
    }else{
        createMessageNotificationElement(message);
    }
}

/**
 * The function is called when an error occurs. Logs the error.
 * @param event contains information about the error
 */
function onError(event) {
    console.error(event.toString());
}

/**
 * Function used to send text with websocket
 * @param message the message object to be sent
 */
function sendText(message) {
    websocket.send(message);
}