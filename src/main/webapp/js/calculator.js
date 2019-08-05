/**
 * Function called when selecting calculator in userList. The function loads a 'p' element with information
 * and the input-form.
 */
function openCalculator(){
    userIdCurrentChat = -1;
    document.getElementById('usernameReceiver').innerHTML = "Kalkulator";

    if(messagesLoaded){
        clearChatlog();
    }
    let messages = [
        {"messageContent": "Hei! Dette er en kalkulator som utfører addisjon og subtraksjon. Eksempler på skrivemåte: "},
        {"messageContent": "200+100"},
        {"messageContent": "3 + 10"},
        {"messageContent": "200 000 + 100 000"}
        ];
    messages.map(message => createReceivedMessageElement(message));
    messagesLoaded = true;
    displayMessageInputElement("calculateForm");
}

/**
 * Function called when pressing the submit button for calculator. Sends a HTTP POST request to /calculator.
 * Calls createResponseElement() on reply.
 */
function submitExpression(event){
    event.preventDefault();

    let expression = document.getElementById("expressionInput").value;
    document.getElementById("expressionInput").value = '';

    createSentMessageElement({"messageContent": expression});
    
    fetch('../api/calculator', {
        method: "POST",
        headers: {
            "Content-Type": "text/plain"
        },
        body: expression
    })
        .then(response => response.text())
        .then(solution => createReceivedMessageElement({"messageContent": solution}))
        .catch(error => console.error(error));
}