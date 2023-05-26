export async function getFormData(idForm){
    const form = document.getElementById(idForm);
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());
    cancelForm();
    return data;
}

export function cancelForm(){
    const form = document.getElementById("addCardForm");
    form.reset();
}

export function setCookie(cname, cvalue, exdays) {
    const d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    let expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}