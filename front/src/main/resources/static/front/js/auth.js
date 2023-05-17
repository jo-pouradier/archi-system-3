function isUserLoggedIn() {
    // use cookie to check if user is logged in
    // if not, redirect to login page
    console.log("isUserLoggedIn");

    try {
        let user = getCookie('user');
        user = JSON.parse(user);
        if (user["uuid"] !== -1 || user !== undefined) {
            console.log("coucou" + user["name"])
            document.getElementById("userNameId").innerHTML = user["name"];
            document.getElementById("Balance").innerHTML = user["balance"];
            return true;
        } else {
            window.location.href = "/front/html/login.html";
            return false;
        }
    }
    catch (e) {
        window.location.href = "/front/html/login.html";
        console.log(e);
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