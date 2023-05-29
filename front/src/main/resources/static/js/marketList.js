async function cardViewUpdate(transaction) {
    let card = await getCard(transaction.cardUUID);
    let template = document.querySelector("#cardViewTemplate");
    let clone = document.importNode(template.content, true);
    console.log(card)
    if (card.price == -1) {
        card.price = "Not for sale";
    }
    let newContent = clone.firstElementChild.innerHTML
        .replace(/{{uuid}}/g, card.uuid)
        .replace(/{{family_name}}/g, card.family)
        .replace(/{{img_src}}/g, card.imageUrl)
        .replace(/{{name}}/g, card.name)
        .replace(/{{description}}/g, card.description)
        .replace(/{{hp}}/g, card.hp)
        .replace(/{{energy}}/g, card.energy)
        .replace(/{{attack}}/g, card.attack)
        .replace(/{{defense}}/g, card.defense)
        .replace(/{{price}}/g, card.price)
        .replace(/{{sell_button}}/g, "Buy");

    clone.firstElementChild.innerHTML = newContent;
    let cardViewContainer = document.querySelector("#cardViewContainer");
    let sellButton = clone.firstElementChild.querySelector("#sellButton2");
    sellButton.addEventListener("click", (function (card) {
        return function (event) {
            let c = transaction;
            buy_request(c);
            cardViewUpdate(c);
        };
    })(card));
    cardViewContainer.children[cardViewContainer.children.length - 1].remove();
    cardViewContainer.appendChild(clone);
    console.log(cardViewContainer.children[cardViewContainer.children.length - 1]);
}

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = document.cookie;
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
async function getTransactions(){
    let user = getCookie('user');
    let response = await fetch("/market/getTransactions");
    if (response.status !== 200) {
        console.log("Error fetching transactions");
        return null;
    }
    return await response.json();
}

async function getCard(uuid){
    let user = getCookie('user');
    let response = await fetch("/card/getCard/"+uuid);
    if (response.status !== 200) {
        console.log("Error fetching card " + uuid);
        return null;
    }
    return await response.json();
}

async function displayUserCard(){
    let transactions = await getTransactions();
    console.log(transactions);
    let template = document.querySelector("#row");

    let newContent;
    let count = 0;
    for (const transaction of transactions) {
        let card = await getCard(transaction.cardUUID);
        let clone = document.importNode(template.content, true);
        console.log(card)
        newContent = clone.firstElementChild.innerHTML
            .replace(/{{user_uuid}}/g, transaction.fromUserUUID)
            .replace(/{{uuid}}/g, card.uuid)
            .replace(/{{family_name}}/g, card.family)
            .replace(/{{img_src}}/g, card.imageUrl)
            .replace(/{{name}}/g, card.name)
            .replace(/{{description}}/g, card.description)
            .replace(/{{hp}}/g, card.hp)
            .replace(/{{energy}}/g, card.energy)
            .replace(/{{attack}}/g, card.attack)
            .replace(/{{defense}}/g, card.defense)
            .replace(/{{price}}/g, card.price)
            .replace(/{{sell_button}}/g, "Buy");
        clone.firstElementChild.innerHTML = newContent;

        let cardContainer = document.querySelector("#tableContent");
        let sellButton = clone.firstElementChild.querySelector("#sellButton");
        cardContainer.appendChild(clone);
        card_tmp = cardContainer.children[cardContainer.children.length-1]
        sellButton.addEventListener("click", (function (card) {
            return function (event) {
                let c = transaction;
                buy_request(c);
                cardViewUpdate(c);
            };
        })(card));
        card_tmp.addEventListener("click", (function (card) {
            return function (event) {
                let c = card;
                cardViewUpdate(c);
            };
        })(card));
        console.log(card_tmp)
        count = count+1;
    }
    cardViewUpdate(transactions[0]);
}

async function buy_request(transaction) {
    // send request to server
    let userUUID = getCookie('user');
    let data = {
        transcationUUID: transaction.transcationUUID,
        cardUUID: transaction.cardUUID,
        fromUserUUID: transaction.fromUserUUID,
        toUserUUID: userUUID,
        price: transaction.price
    };
    let response = await fetch("/market/acceptTransaction", {method: "POST", headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify(data)});
    if (response.status !== 200) {
        console.log("Error accepting transaction");
        return null;
    }
    console.log("Transaction accepted");
    // reload page
    window.location.reload();
}
displayUserCard().then(r => console.log("Market displayed"));





