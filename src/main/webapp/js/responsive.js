let mediaQueryList = window.matchMedia("(max-width: 480px)");
let groupChatList = document.getElementById("groupChatList");
let userlist = document.getElementById("userList");
let wrapper = document.getElementById("wrapper");
let chat = document.getElementById("chat");

mediaQueryList.addListener(function() {
    if(!mediaQueryList.matches){
        userlist.style.display = "block";
        groupChatList.style.display = "block";
        document.getElementById("chat").style.display = "grid";
    }
});
function toggleUserList(){
    if(mediaQueryList.matches){
        if(userlist.style.display === "block"){
            userlist.style.display = "none";
            groupChatList.style.display = "none";
            wrapper.style.gridTemplateRows = "3fr 2fr 15fr";
            chat.style.display = "grid";
        }else{
            chat.style.display = "none";
            userlist.style.display = "block";
            groupChatList.style.display = "block";
            wrapper.style.gridTemplateRows = "3fr 17fr 0fr";
        }
    }
}