const TEMPLATE =  document.getElementById("card");
const CONTAINER = document.getElementById("cards-container");

async function sendForm(){
    const form = document.getElementById("formSelectNameCard");
    const formData = new FormData(form);
    const name = Object.fromEntries(formData.entries()).name;
    const cardSet = await getCards();

    // probleme pour supprimer les ancienne cartes
    for (let card of CONTAINER.children){
        card.remove();
    }
    CONTAINER.append(TEMPLATE);

    let i = 0;
    for (let cardS of cardSet){
        console.log(`name: ${cardS.name}, number: ${i}`)
        if (cardS.name === name) {
            renderCard(cardS, i);
            i++;
        }
    }

    // cancelForm();
}

async function getCards(){
    const url = '/card';
    const context = {
        method: 'GET'
    };
    let response = await fetch(url, context);
    if(response.ok){
        return response.json();
    }
    else{
        console.log(response.status);
        return null;
    }
}

function cancelForm(){
    const form = document.getElementById("formSelectNameCard");
    form.reset();
}

function renderCard(card, i){
    let clone = document.importNode(TEMPLATE, true);
    clone.innerHTML = clone.innerHTML
        .replace(/{{family}}/g, card.family)
        .replace(/{{name}}/g, card.name)
        .replace(/{{imgUrl}}/g, card.imgUrl)
        .replace(/{{description}}/g, card.description)
        .replace(/{{affinity}}/g, card.affinity)
        .replace(/{{attack}}/g, card.attack)
        .replace(/{{defense}}/g, card.defense)
        .replace(/{{energy}}/g, card.energy)
        .replace(/{{hp}}/g, card.hp);
    clone.setAttribute("id", "card"+i);
    CONTAINER.append(clone);
}


function testRenderCard(){
    const cardTest = {
            "family": "test",
            "name": "test",
            "imgUrl": "test",
            "description": "test",
            "affinity": "test",
            "attack": 1,
            "defense": 1,
            "energy": 1,
            "hp": 1
        };
    renderCard(cardTest, -1);
 }

window.onload = function() {
    testRenderCard();
}

