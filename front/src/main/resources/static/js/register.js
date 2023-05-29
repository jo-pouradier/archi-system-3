async function register(){
    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password1 = document.getElementById("password1").value;
    const password2 = document.getElementById("password2").value;

    const data = {name: username, email: email, password: password1};
    console.log(JSON.stringify(data));

    const response = await fetch('/auth/register', {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body:  JSON.stringify(data)
    });
    console.log(response);
    if (response.ok) {
        console.log(response.body);
        try {
            if (data !== "-1") {
                document.cookie = "user=" + response.body.uuid + ";path=/";
                window.location.href = "../html/login.html";
            }
        } catch (e) {
            console.log(e);
            alert("Error: " + e + "\nReinscrivez vous");
        }
    }


    // if(password1 !== password2){
    //     alert("Les mots de passe ne correspondent pas");
    //     return;
    // }else {
    //     const data = {username: username, email: email, password: password1};
    //     console.log(data);
    //
    //     fetch('http://127.0.0.1:8000/auth/register', {
    //         method: 'POST',
    //         body: JSON.stringify(data)
    //     }).then(function (response) {
    //         console.log(response);
    //         if (response.ok) {
    //             console.log(response.body);
    //             try {
    //                 if (data !== "-1") {
    //                     document.cookie = "userId=" + response.body.uuid + ";path=/";
    //                     window.location.href = "../html/login.html";
    //                 }
    //             } catch (e) {
    //                 console.log(e);
    //                 alert("Error: " + e + "\nReinscrivez vous");
    //             }
    //         } else {
    //             console.log(response.status);
    //             alert(response.message);
    //             return null;
    //         }
    //     })
    // }
}
function  login() {
    window.location.href = "/login";
}