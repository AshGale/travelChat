
window.addEventListener("load", function () {

    $('#submit-account').click(function(event) {
        event.preventDefault();

        let account = getAccountForm();
        if(account == null) {
            console.log("Request not send due to error");
        } else {
            post_json('/account', account);
        }
    });

    $('#get-account').click(function(event) {
            event.preventDefault();
            processRequesFortAccount().finally();//process reactive style
    });

    $('#clear-account').click(function(event) {
            event.preventDefault();
            document.getElementById("account_id").value = "";
            document.getElementById("name-input").value = "";
            document.getElementById("nickname-input").value = "";
            document.getElementById("trips-input").value = "";
    });
 });

async function processRequesFortAccount(){
    let result;
    let form_account = getAccountForm();//get account fields from form
    if(form_account == null) {
        console.log("Request not send due to error");
    } else {
        //determine type of get to perform, and wait for server response
        if(form_account.id != null && form_account.id != "") {
            result = await get_json('/account/' + form_account.id);
        } else if(form_account.nickname != "" && form_account.nickname != null) {
            result = await get_json('/account/nickname/' + form_account.nickname);
        } else if(form_account.name != "" && form_account.name != null) {
             result = await get_json('/account/name/' + form_account.name);//encodeURIComponent(name)
        } else {
            alert("No identifiable information provided\nPlease enter an Id, Nickname, or Name");
        }

        //process the result.
        //if error ignore
        //if object of single account, fill form
        //if array, show number of accounts, and store result, and add number selection

        if(result.constructor === Account){
            console.log("of type account");
        }
        else if(result.constructor === Array) {
            console.log("of type Array " + result.length);
        }
        else if(result.constructor === Object) {
            console.log("of type Object");
        } else {console.log("of type " + result.constructor );}

        let account = getAccountObjectFromResult(result);

        populateAccountForm(account);
     }
 }

function getAccountObjectFromResult(result) {
    let account = Object.create(Account);

    account.id = (result || {}).id;
    account.name = (result || {}).name;
    account.nickname = (result || {}).nickname;
    account.trips = (result || {}).trips;
    return account;
}

function populateAccountForm(account) {
    document.getElementById("account_id").value = account.id;
    document.getElementById("name-input").value = account.name;
    document.getElementById("nickname-input").value = account.nickname;
    document.getElementById("trips-input").value = account.trips;
}

 function getAccountForm() {
    let account = Object.create(Account);
    account.id = $('#account_id').val();
    if(account.id == "") { // ensure null is sent when nothing is filled
        account.id = null;
    }
    account.name = $('#name-input').val();
    account.nickname = $('#nickname-input').val();
    if($('#trips-input').val() == "" || $('#trips-input').val() == null) { // ensure null is sent when nothing is filled
        account.trips = null;
    } else {
        try {
            account.trips = JSON.parse($('#trips-input').val());//["5d5827bb9660491f480fad3d"]
        } catch {
            window.alert("Please ensure trips are in format [\"value\",\"value\"]");
            return null;
        }
    }


    return account;
 }