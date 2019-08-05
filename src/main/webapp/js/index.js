let inputfield = document.getElementById('username');
inputfield.addEventListener("input",function () {
    if (inputfield.validity.patternMismatch) {
        inputfield.setCustomValidity("Ugyldig tegn, bruk kun bokstaver (a-z) og tall (0-9)");
    } else {
        inputfield.setCustomValidity("");
    }
});


/**
 * Function called when pressing login-button. Makes a HTTP POST request to server.
 * On success it calls loadApplication().
 */
function login(event){
    event.preventDefault();
    let user = {
        "username": document.getElementById('username').value
    };

    fetch('../api/user', {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=utf-8;"
        },
        body: JSON.stringify(user)
    })
        .then(response => response.json())
        .then(user => {
            if(user.userId === 0) alert("Feil brukernavn og/eller passord");
            else loadApplication(user);
        })
        .catch(error => console.error(error));
}


/**
 * Adds user information to sessionStorage and redirects to app.html
 * @param user contains user information as a JSON object
 */
function loadApplication(user){
    sessionStorage.setItem("userId", user.userId);
    sessionStorage.setItem("username", user.username);
    window.location.href = "../app.html"

}