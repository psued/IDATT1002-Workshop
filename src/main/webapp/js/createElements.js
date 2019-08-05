/**
 * The function creates HTML elements for users and appends then to the document.
 * @param users array of users as JSON
 */
function createUsernameElements (users) {
    //Finds logged in user from list and removes object from list
    let index = users.findIndex(function(item){
        return item.username === sessionStorage.getItem("username");
    });
    if(index !== -1){
        delete users[index];
    }

    users.map(e => {
        usersFromDb.push(e.userId);
        let itag = document.createElement("i");
        itag.setAttribute("class", "fas fa-user userIcon");
        let litag = document.createElement("li");
        litag.setAttribute("id", e.userId);
        litag.onclick = function () {
            document.getElementById('usernameReceiver').innerHTML = document.getElementById(e.userId).textContent;
            getAllMessages(e.userId);
            toggleUserList();
        };

        litag.appendChild(itag);
        litag.appendChild(document.createTextNode(e.username));

        document.getElementById("userList").appendChild(litag);
    });
}

/**
 * The function creates HTML elements for group chats and appends them to the document
 * @param groupChats array of group chats as JSON
 */
function createGroupChatElements(groupChats){
    groupChats.map(e => {
        let itag = document.createElement("i");
        itag.setAttribute("class", "fas fa-users userIcon");
        let litag = document.createElement("li");
        litag.setAttribute("id", "groupChat"+e.groupChatId);
        litag.onclick = function () {
            openGroupChat(e);
            toggleUserList();
        };

        litag.appendChild(itag);
        litag.appendChild(document.createTextNode(e.groupChatName));

        document.getElementById("groupChatList").appendChild(litag);
    });
}

/**
 * The function creates HTML elements to list users for popup form, used when creating a new group chat.
 * @param users array of users as JSON
 */
function createUserElementsPopupForm (users) {
    //Finds logged in user from list and removes object from list
    let index = users.findIndex(function(item){
        return item.username === sessionStorage.getItem("username");
    });
    if(index !== -1){
        delete users[index];
    }

    users.map(e => {
        let litag = document.createElement("li");
        litag.setAttribute("id", e.userId);

        let inputtag = document.createElement("input");
        inputtag.setAttribute("id","checkBox"+e.userId);
        inputtag.setAttribute("type","checkbox");
        litag.onclick = function () {
            let user = usersNewGroupChat.find(x => {
                return x.userId === e.userId;
            });
            if(user === undefined){
                usersNewGroupChat.push(e);
                document.getElementById("checkBox"+e.userId).checked = true;
            }else{
                usersNewGroupChat = usersNewGroupChat.filter(x => {
                    return x.userId !== e.userId;
                });
                document.getElementById("checkBox"+e.userId).checked = false;
            }
        };
        litag.appendChild(inputtag);
        litag.appendChild(document.createTextNode(e.username));
        document.getElementById("userListGroupChat").appendChild(litag);
    });
}

/**
 *
 */
function removeUserElementsPopupForm(){
    let userListGroupChat = document.getElementById("userListGroupChat");
    while(userListGroupChat.firstChild){
        userListGroupChat.removeChild(userListGroupChat.firstChild);
    }
}


/**
 * Creates a HTML element with class "receiver" and appends it to the document
 * @param message message object as JSON
 */
function createReceivedMessageElement(message){
    let ptag = document.createElement("P");

    ptag.setAttribute("class","receiver");
    ptag.appendChild(document.createTextNode(message.messageContent));

    createTimestampElement(ptag, message);

    document.getElementById("messages").appendChild(ptag);
    ptag.scrollIntoView(false);

}

/**
 * Creates a HTML element with class "sender" and appends it to the document
 * @param message the message object as JSON
 */
function createSentMessageElement(message) {
    let ptag = document.createElement("P");

    ptag.setAttribute("class","sender");
    ptag.appendChild(document.createTextNode(message.messageContent));

    createTimestampElement(ptag, message);

    document.getElementById("messages").appendChild(ptag);
    ptag.scrollIntoView(false);
}

/**
 * Creates a HTML element with class "sender" and appends it to the document
 * Also adds sender of the message infront of message
 * @param message the groupb chat message object as JSON
 */
function createSentMessageElementGroupChat(message) {
    let ptag = document.createElement("P");

    ptag.setAttribute("class","sender");

    let senderText = document.createElement("SPAN");
    senderText.setAttribute("class", "senderGroupChat");
    senderText.appendChild(document.createTextNode(sessionStorage.getItem("username") + ": "));
    ptag.appendChild(senderText);

    ptag.appendChild(document.createTextNode(message.messageContent));
    createTimestampElement(ptag, message);
    document.getElementById("messages").appendChild(ptag);
    ptag.scrollIntoView(false);
}

/**
 * Creates a HTML element with class "receiver" and adds it to the document
 * Also adds sender of the message infront of message
 * @param message message object as JSON
 */
function createReceivedMessageElementGroupChat(message){
    let ptag = document.createElement("P");

    ptag.setAttribute("class","receiver");

    let senderText = document.createElement("SPAN");
    senderText.setAttribute("class", "senderGroupChat");
    senderText.appendChild(document.createTextNode(document.getElementById(message.userId1).textContent + ": "));
    ptag.appendChild(senderText);

    ptag.appendChild(document.createTextNode(message.messageContent));
    createTimestampElement(ptag, message);
    document.getElementById("messages").appendChild(ptag);
    ptag.scrollIntoView(false);

}

/**
 * Used to display the input form for sending messages. Hides the rest
 * @param inputElementId the ID of which form to display
 */
function displayMessageInputElement(inputElementId){
    let elementIds = ["calculateForm", "groupChatForm", "messageForm"];
    let filtered = elementIds.filter(e => {
        return e !== inputElementId
    });

    document.getElementById(inputElementId).style.display = "flex";
    filtered.map(e => document.getElementById(e).style.display = "none");
}

/**
 * Creates a HTML element used to notify users that they got a new message
 * @param message message object as JSON
 */
function createMessageNotificationElement(message){
    if(message.groupChatId === 0){
        let index = notifications.indexOf(message.userId1);
        if(index === -1){
            notifications.push(message.userId1);
            let litag = document.getElementById(message.userId1);
            let itag = document.createElement("i");
            itag.setAttribute("class", "fas fa-comment notificationIcon");

            litag.appendChild(itag);
        }
    }else{
        let index = groupChatNotifications.indexOf(message.groupChatId);
        if(index === -1){
            groupChatNotifications.push(message.groupChatId);
            let litag = document.getElementById("groupChat"+message.groupChatId);
            let itag = document.createElement("i");
            itag.setAttribute("class", "fas fa-comment notificationIcon");

            litag.appendChild(itag);
        }
    }
}

/**
 * Removes notification element from document
 * @param userId user ID of the user to remove notification from
 */
function removeNotificationElement(userId) {
    notifications.find(e => {
        if(e === userId){
            let li = document.getElementById(userId);
            li.removeChild(li.childNodes[2]);

            notifications = notifications.filter(e => e !== userId);
        }
    })

}

/**
 * Creates a HTML element with the timestamp of the message or current date if message is NULL. Appends the element
 * tp the ptag.
 * @param ptag the element to append the new element to
 * @param message message object containing timestamp
 */
function createTimestampElement(ptag, message) {
    let spantag = document.createElement("SPAN");
    spantag.setAttribute("class","timestamp");
    if(message.timestamp !== undefined){
        spantag.appendChild(document.createTextNode(formatTimestamp(message.timestamp)));
    } else {
        var currentdate = new Date();
        var datetime = currentdate.getHours().toString().padStart(2, "0") + ":"
            + currentdate.getMinutes().toString().padStart(2, "0") + " "
            + currentdate.getDate().toString().padStart(2, "0") + "-"
            + (currentdate.getMonth()+1).toString().padStart(2, "0")  + "-"
            + currentdate.getFullYear();
        spantag.appendChild(document.createTextNode(datetime));
    }
    ptag.appendChild(spantag);
}
