window.addEventListener("load", () => {
    getUsers();
    getGroupChats();
});
document.getElementById("currentUser").appendChild(document.createTextNode(sessionStorage.getItem("username")));
let usersFromDb = [];
let groupChats = [];

/**
 * Makes a HTTP GET request to server for all users
 */
function getUsers() {
    fetch('../api/user/', {
        method: "GET",
    })
        .then(response => response.json())
        .then(users => {
            createUsernameElements(users);
        })
        .catch(error => console.error(error));
}

/**
 * Makes a HTTP GET request to server for all group chats
 */
function getGroupChats(){
    fetch('../api/groupchat/user/'+sessionStorage.getItem("userId"), {
        method: "GET"
    })
        .then(response => response.json())
        .then(groupChatsResponse => {
            createGroupChatElements(groupChatsResponse);
            groupChatsResponse.map(groupChat => groupChats.push(groupChat));
        })
        .catch(error => console.error(error));
}