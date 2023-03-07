document.getElementById("newUsername").value = sessionStorage.getItem("username");
document.getElementById("cancelButton").addEventListener("click", function(){
    window.location.href = "../app.html";
});

/**
 * Makes HTTP PUT request to server for updating username and password
 */
function editUser (event) {
    event.preventDefault();
    newUsername = document.getElementById("newUsername").value;
    newPassword = document.getElementById("newPassword").value;
    let newInformation = {newUsername, newPassword};

    fetch('../api/user/'+sessionStorage.getItem("userId"), {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(newInformation)
    })
        .then(response => response.json())
        .then(response => {
            if (response === true) {
                sessionStorage.setItem("username", newUsername);
                alert("Bruker oppdatert");
                window.location.href = "../app.html";
            } else {
                alert("Brukernavn eksisterer fra før, vennligst skriv inn et nytt brukernavn");
            }
        })
        .catch(error => console.error(error));
}

// id="newUsername"
// id="newPassword"

