let cardList = [
    {
        family_name:"DC Comic",
        imageUrl:"http://www.superherobroadband.com/app/themes/superhero/assets/img/superhero.gif",
        name:"SUPERMAN",
        description: "The origin story of Superman relates that he was born Kal-El on the planet Krypton, before being rocketed to Earth as an infant by his scientist father Jor-El, moments before Krypton's destruction. Discovered and adopted by a farm couple from Kansas, the child is raised as Clark Kent and imbued with a strong moral compass. Early in his childhood, he displays various superhuman abilities, which, upon reaching maturity, he resolves to use for the benefit of humanity through a 'Superman' identity.",
        hp: 500,
        energy:100,
        attack:50,
        defense: 50,
        price:200
    },
    {
        family_name:"DC Comic",
        imageUrl:"https://static.fnac-static.com/multimedia/Images/8F/8F/7D/66/6716815-1505-1540-1/tsp20171122191008/Lego-lgtob12b-lego-batman-movie-lampe-torche-batman.jpg",
        name:"BATMAN",
        description: "Bruce Wayne, alias Batman, est un héros de fiction appartenant à l'univers de DC Comics. Créé par le dessinateur Bob Kane et le scénariste Bill Finger, il apparaît pour la première fois dans le comic book Detective Comics no 27 (date de couverture : mai 1939 mais la date réelle de parution est le 30 mars 1939) sous le nom de The Bat-Man. Bien que ce soit le succès de Superman qui ait amené sa création, il se détache de ce modèle puisqu'il n'a aucun pouvoir surhumain. Batman n'est qu'un simple humain qui a décidé de lutter contre le crime après avoir vu ses parents se faire abattre par un voleur dans une ruelle de Gotham City, la ville où se déroulent la plupart de ses aventures. Malgré sa réputation de héros solitaire, il sait s'entourer d'alliés, comme Robin, son majordome Alfred Pennyworth ou encore le commissaire de police James Gordon. ",
        hp: 50,
        energy:80,
        attack:170,
        defense: 80,
        price:100
    },
    {
        family_name:"Marvel",
        imageUrl:"https://static.hitek.fr/img/actualite/2017/06/27/i_deadpool-2.jpg",
        name:"DEAD POOL",
        description: "Le convoi d'Ajax est attaqué par Deadpool. Il commence par massacrer les gardes à l'intérieur d'une voiture, avant de s'en prendre au reste du convoi. Après une longue escarmouche, où il est contraint de n'utiliser que les douze balles qu'il lui reste, Deadpool capture Ajax (dont le véritable nom est Francis, ce que Deadpool ne cesse de lui rappeler). Après l'intervention de Colossus et Negasonic venus empêcher Deadpool de causer plus de dégâts et le rallier à la cause des X-Men, Ajax parvient à s'échapper en retirant le sabre de son épaule. Il apprend par la même occasion la véritable identité de Deadpool : Wade Wilson.",
        hp: 999999,
        energy:100,
        attack:15,
        defense: 15,
        price:250
    },

]

function cardViewUpdate(card){
    let template = document.querySelector("#cardViewTemplate");
    let clone = document.importNode(template.content, true);
    console.log(card)
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
        .replace(/{{fight_button}}/g,  "Fight ?");

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
    let response = await fetch("/card/getCardsByOwner/"+user);
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
    let response = await fetch("/fight/cancelFight/", {method: "POST", headers: {
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
    let price = prompt("Please enter your fight price", "100");
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
    let response = await fetch("/fight/createFight/", {method: "POST", headers: {
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


displayUserCard().then(r => console.log("Fights displayed"));





