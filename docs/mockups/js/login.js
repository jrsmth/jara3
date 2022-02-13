function toggleError(condition){
    if(condition){
        document.getElementById("login-error").style.display = "block";
        document.getElementsByClassName("form")[0].style.padding = "45px 45px 16px 45px";
    } else{
        document.getElementById("login-error").style.display = "none";
        document.getElementsByClassName("form")[0].style.padding = "45px 45px 60px 45px";
    }
}