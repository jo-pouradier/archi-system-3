// import * as moduleForm from './moduleForm.js'

function login(){
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const data = {email: email, password: password};
    console.log(email, password);
    fetch('http://127.0.0.1:8000/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then( function(response) {
        console.log(response);
        let data = response.json();
        console.log(data);
        console.log( response.body);
        if(response.ok) {
            console.log("ok");
            return data;
        } else {
            console.log(response.status);
            alert(response.message);
            return null;
        }
    }).then(function (data) {
        console.log(data);
        if(data != null) {
            document.cookie = "user=" + JSON.stringify(data.uuid) + ";path=/" ;
            window.location.href = "/index.html";
        } else {
            console.log("error");
        }
    });
}
function  register() {
    window.location.href = "../html/register.html";
}