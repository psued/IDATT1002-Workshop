document.getElementById("newUsername").value = sessionStorage.getItem("username");
document.getElementById("cancelButton").addEventListener("click", function(){
    window.location.href = "../app.html";
});

/**
 * Makes HTTP PUT request to server for updating username and password
 */
function editUser (event) {
    event.preventDefault();
    let newInformation = {};

    fetch('../api/user/'+sessionStorage.getItem("userId"), {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(newInformation)
    })
        .then(response => response.json())
        .then(response => {
            if (response === true) {
                alert("Bruker oppdatert");
                window.location.href = "../app.html";
            } else {
                alert("Brukernavn eksisterer fra før, vennligst skriv inn et nytt brukernavn");
            }
        })
        .catch(error => console.error(error));
}
