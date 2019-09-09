$(document).ready(function(){
    // add event listener on the login button
    $("#login").click(function(){
        facebookLogin();
    });
});

function facebookLogin()
{
    FB.getLoginStatus(function(response) {
        console.log(response);
        statusChangeCallback(response);
    });
}

// This function is called when someone finishes with the Login
    // Button.  See the onlogin handler attached to it in the sample
    // code below.
    function checkLoginState() {
        FB.getLoginStatus(function(response) {
            statusChangeCallback(response);
        });
    }

function statusChangeCallback(response)
{
    console.log(response);
    if(response.status === "connected")
    {
        $("#login").hide();
        $("#logout").show();
        fetchUserProfile();
    }
    else{
        // Logging the user to Facebook by a Dialog Window
        facebookLoginByDialog();
    }
}

function facebookLoginByDialog()
{
    FB.login(function(response) {
        statusChangeCallback(response);
    }, {scope: 'public_profile,user_photos,user_gender,email'});
}

function fetchUserProfile()
{
    console.log('Welcome!  Fetching your information.... ');
    FB.api('/me?fields=id,name,gender,picture', function(response) {
        console.log(response);
        console.log('Successful login for: ' + response.name);
        var clientToken = FB.getAuthResponse()['accessToken'];
        var debugProfile = `<h1>Welcome ${response.name}<h1>
                          <h2>Your email is ${response.email}</h2>
                          <h3>Your Birthday is ${response.birthday}</h3>
                          <h3>Your Access Token is ${clientToken}</h3>
                          <p>Redirecting...</p>`;
        $("#status").append("Authentication successful! After photos are stored you will be redirected to your dashboard. Working...");
        if(typeof clientToken !== "undefined") {
            window.location.href = "/photos?fbToken=" + clientToken;
        } else {
            $("#status").append("<h3>Error: Access token not received.</h3>");
        }
    });
}

// add event listener on the logout button
$("#logout").click(function(){
    $("#logout").hide();
    $("#login").show();
    $("#status").empty();
    facebookLogout();
});

function facebookLogout()
{
    FB.logout(function(response) {
        statusChangeCallback(response);
    });
}