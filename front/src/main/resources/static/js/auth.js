function getUser() {

    return user;
}

function updateUserHUD(uuid){
    fetch('/user/getUser/' + uuid, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then( function(response) {
        let data = response.json();
        if(response.ok) {
            return data;
        } else {
            alert(response.message);
            return null;
        }
    }).then(function (data) {
        if(data != null) {
            document.getElementById("userNameId").innerHTML = data.name;
            document.getElementById("balanceID").innerHTML = data.balance;
        } else {
            console.log("error");
        }
    });
}

function isUserLoggedIn() {
    // use cookie to check if user is logged in
    // if not, redirect to login page
    try {
        let userUuid = getCookie('user');
        if (userUuid !== undefined || userUuid == "" || userUuid == null) {
            updateUserHUD(userUuid);
            return true;
        } else {
            window.location.href = "/html/login.html";
            return false;
        }
    }
    catch (e) {
        window.location.href = "/html/login.html";
    }


}

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = document.cookie;
    let ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
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