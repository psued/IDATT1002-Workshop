let usersNewGroupChat = [];
let groupChatNameInput = document.getElementById("groupname");

groupChatNameInput.addEventListener("input",function () {
    if (groupChatNameInput.validity.patternMismatch) {
        groupChatNameInput.setCustomValidity("Ugyldig tegn, bruk kun bokstaver (a-z) og tall (0-9)");
    } else {
        groupChatNameInput.setCustomValidity("");
    }
});

function openNewGroupChatForm() {
    getUsersForGroupChat();
    document.getElementById("formpopup").style.display = "block";
}

function getUsersForGroupChat() {
    fetch('../api/user', {
        method: "GET"
    })
        .then(response => response.json())
        .then(users => createUserElementsPopupForm(users))
        .catch(error => console.log("Error: ", error))
}

function createGroupChat(event){
    event.preventDefault();
    let groupChatName = document.getElementById("groupname").value;
    document.getElementById("groupname").value = "";

    usersNewGroupChat.push({
        "userId": sessionStorage.getItem("userId"),
        "username": sessionStorage.getItem("username")
    });

    let newGroupChat = {
        "groupChatName": groupChatName,
        "userList": usersNewGroupChat
    };
    usersNewGroupChat = [];

    fetch('../api/groupchat', {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(newGroupChat)
    })
        .then(response => response.json())
        .then(groupChat => {
            let groupChatArray = [];
            groupChatArray.push(groupChat);
            createGroupChatElements(groupChatArray);
            closeForm(event);
            openGroupChat(groupChat);
            removeUserElementsPopupForm();

            let newGroupChatMessage = {
                "newGroupChat": true,
                "groupChatId": groupChat.groupChatId,
                "groupChatName": groupChat.groupChatName,
                "userId1": sessionStorage.getItem("userId")
            };
            sendText(JSON.stringify(newGroupChatMessage));
        })
        .catch(error => console.error(error));
}

function openGroupChat(groupChat){
    userIdCurrentChat = -1;
    currentGroupChat = groupChat.groupChatId;
    document.getElementById('usernameReceiver').innerHTML = groupChat.groupChatName;

    let index = groupChatNotifications.find(e => e == currentGroupChat);
    if(index !== undefined){
        delete groupChatNotifications[index];
        let li = document.getElementById("groupChat"+currentGroupChat);
        li.removeChild(li.childNodes[2]);
    }

    clearChatlog();
    displayMessageInputElement("groupChatForm");

    fetch('../api/groupchat/'+groupChat.groupChatId+'/message',{
        method: "GET"
    })
        .then(response => response.json())
        .then(messages => {
            messagesLoaded = true;
            getChatlog(messages);
        })
        .catch(error => console.error(error));
}

function postGroupMessage(event){
    event.preventDefault();
    let messageInput = document.getElementById("groupMessageInput").value;
    document.getElementById("groupMessageInput").value = '';

    let message = {
        "userId1": sessionStorage.getItem("userId"),
        "messageContent": messageInput,
        "groupChatId": currentGroupChat
    };

    fetch("../api/groupchat/"+ currentGroupChat+"/message", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(message)
    })
        .then(response => response.json())
        .then(json => {
            createSentMessageElementGroupChat(json);
            sendText(JSON.stringify(message));
        })
        .catch(error => console.error(error));
}


function closeForm(event) {
    event.preventDefault();
    document.getElementById("formpopup").style.display = "none";
    removeUserElementsPopupForm();
}