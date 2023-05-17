async function sendForm(){
    const form = document.getElementById("addCardForm");
    const formData = new FormData(form);
    const card = Object.fromEntries(formData.entries());
    const cardSet = await getCards();

    for (let cardS of cardSet){
        if (cardS === card) {
            alert("La carte existe déjà");
            return;
        }
    }

    sendCard(card);
    cancelForm();
}

function cancelForm(){
    const form = document.getElementById("addCardForm");
    form.reset();
}

async function getCards(){
    const url = 'http://vps.cpe-sn.fr:8083/cards';
    const context = {
        method: 'GET'
    };
    response = await fetch(url,context);
    if(response.ok){
       return response.json();
    }
    else{
        console.log(response.status);
        return null;
    }
}

async function sendCard(card){
    const url = 'http://vps.cpe-sn.fr:8083/card';
    const context = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'accept': '*/*',
            'Access-Control-Allow-Origin': '*'
        },
        body: JSON.stringify(card)
    };

    response = await fetch(url,context, {mode: 'no-cors'});
    if(response.ok){
        alert("La carte a bien été ajoutée");
        cancelForm();
    }
    else{
        console.log(response.status);
    }
}

