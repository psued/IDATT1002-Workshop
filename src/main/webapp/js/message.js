let userIdCurrentChat = -1;
let currentGroupChat = -1;
let messagesLoaded = false;
let notifications = [];
let groupChatNotifications = [];


/**
 * Sends a HTTP post request to server that includes the message
 */
function postMessage (event) {
    event.preventDefault();
    let message = document.getElementById("messageInput").value;
    document.getElementById("messageInput").value = '';

    let messageObj = {
        "userId1": sessionStorage.getItem("userId"),
        "userId2": userIdCurrentChat,
        "messageContent": message,
    };
    
    fetch("../api/message" , {
        method:"POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify(messageObj)
    })
        .then(response => response.json())
        .then(message => {
            sendText(JSON.stringify(message));
            createSentMessageElement(message);
        })
        .catch(error => console.error(error));
}

/**
 * Sends a HTTP GET request to server to get all messages between two users
 * @param id the ID of the user to get messages from
 */
function getAllMessages (id) {
    document.getElementById("chat").style.display = "grid";

    userIdCurrentChat = id;
    currentGroupChat = -1;

    if(messagesLoaded){
        clearChatlog();
    }

    fetch('../api/message/'+sessionStorage.getItem("userId")+"/"+id, {
        method: "GET"
    })
        .then(response => response.json())
        .then(messages => {
            messagesLoaded = true;
            displayMessageInputElement("messageForm");
            getChatlog(messages);
            removeNotificationElement(id);
        })
        .catch(error => console.error(error))
}


/**
 * function called by getMessages(), creates elements in html-page
 * @param messages array of messages to be displayed
 */
function getChatlog(messages) {
    messages.map(e => {
        let ptag = document.createElement("p");
        let spantag = document.createElement("span");
        if(sessionStorage.getItem("userId") == e.userId1){
            ptag.setAttribute("class", "sender");
            ptag.setAttribute("id", e.userId2);
        }else{
            ptag.setAttribute("class", "receiver");
            ptag.setAttribute("id", e.userId1);
        }

        if (e.groupChatId != 0) {
            let senderText = document.createElement("SPAN");
            senderText.setAttribute("class", "senderGroupChat");
            let username;
            if (sessionStorage.getItem("userId") == e.userId1) {
                username = sessionStorage.getItem("username")
            } else {
                username = document.getElementById(e.userId1).textContent
            }
            senderText.appendChild(document.createTextNode(username + ": "));
            ptag.appendChild(senderText);
        }

        ptag.appendChild(document.createTextNode(e.messageContent));
        spantag.setAttribute("class", "timestamp");
        spantag.appendChild(document.createTextNode(formatTimestamp(e.timestamp)));

        ptag.appendChild(spantag);
        document.getElementById("messages").appendChild(ptag);
        ptag.scrollIntoView(false);
    })
}

/**
 * Removes all message elements from document. Used when loading a new chat.
 */
function clearChatlog(){
    let messageList = document.getElementById("messages");
    while (messageList.firstChild) {
        messageList.removeChild(messageList.firstChild);
    }
    messagesLoaded = false;
}

/**
 * Formats timestamp
 * @param timestamp timestamp to be formatted
 * @returns {string} timestamp formatted
 */
function formatTimestamp (timestamp) {
    let datetime = timestamp.split("T");
    let tmp = datetime[0].split("-");
    return  datetime[1].substring(0,5) + " " + tmp[2] + "-" + tmp[1] + "-" +tmp[0];
}