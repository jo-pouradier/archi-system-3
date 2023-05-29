function cardViewUpdate(card){
    let template = document.querySelector("#cardViewTemplate");
    let clone = document.importNode(template.content, true);
    console.log(card)
    if (card.price == -1)  {
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
        .replace(/{{sell_button}}/g, (card.price == "Not for sale") ? "Sell" : "Remove from market");

    clone.firstElementChild.innerHTML = newContent;
    let cardViewContainer = document.querySelector("#cardViewContainer");
    let sellButton = clone.firstElementChild.querySelector("#sellButton2");
    sellButton.addEventListener("click", (function (card) {
        return function (event) {
            let c = card;
            if (c.price == "Not for sale") {
                sell_request(c);
            }else{
                remove_market_request(c);
            }
            cardViewUpdate(c);
        };
    })(card));
    cardViewContainer.children[cardViewContainer.children.length-1].remove();
    cardViewContainer.appendChild(clone);
    console.log(cardViewContainer.children[cardViewContainer.children.length-1]);
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
async function getUserCards(){
    let user = getCookie('user');
    let response = await fetch("/market/getCardsTransaction/"+user);
    if (response.status !== 200) {
        console.log("Error fetching cards");
        return null;
    }
    return await response.json();
}

async function displayUserCard(){
    let userCards = await getUserCards();
    console.log(userCards);
    let template = document.querySelector("#row");

    let newContent;
    let count = 0;
    for (const card of userCards) {
        let clone = document.importNode(template.content, true);
        console.log(card)
        console.log(card.imageUrl)
        if (card.price == -1)  {
            card.price = "Not for sale";
        }
        newContent = clone.firstElementChild.innerHTML
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
            .replace(/{{sell_button}}/g, (card.price == "Not for sale") ? "Sell" : "Remove from market");
        clone.firstElementChild.innerHTML = newContent;

        let cardContainer = document.querySelector("#tableContent");
        let sellButton = clone.firstElementChild.querySelector("#sellButton");
        cardContainer.appendChild(clone);
        card_tmp = cardContainer.children[cardContainer.children.length-1]
        sellButton.addEventListener("click", (function (card) {
            return function (event) {
                let c = card;
                if (c.price == "Not for sale") {
                    sell_request(c);
                }else{
                    remove_market_request(c);
                }
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
    cardViewUpdate(userCards[0]);
}

async function remove_market_request(card) {
    // send request to server
    let user = getCookie('user');
    let data = {
        cardUUID: card.uuid,
        fromUserUUID: user,
        price: -1
    };
    let response = await fetch("/market/cancelTransaction/", {method: "POST", headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify(data)});
    if (response.status !== 200) {
        console.log("Error updating cards");
        return null;
    }
    console.log("cancel: " + card.uuid);
    // reload page
    window.location.reload();
}
async function sell_request(card) {
    // ask price to user
    let price = prompt("Please enter your price", "100");
    if (price == null || price === "") {
        alert("No price given");
        return;
    }
    // send request to server
    let user = getCookie('user');
    let data = {
        cardUUID: card.uuid,
        fromUserUUID: user,
        price: price
    };
    let response = await fetch("/market/createTransaction/", {method: "POST", headers: {
            'Content-Type': 'application/json'
        }, body: JSON.stringify(data)});
    if (response.status !== 200) {
        console.log("Error updating cards");
        return null;
    }
    console.log("sell: " + card.uuid + " for " + price + "$");
    // reload page
    window.location.reload();
}


displayUserCard().then(r => console.log("Cards displayed"));





