
window.addEventListener("load", function () {

//debug
//document.getElementById("account-nickname-input").value = "theDean";
document.getElementById("account-name-input").value = "Dean";

//------------------------------------------------------Account------------------------------------------------------
    $('#submit-account').click(function(event) {
        event.preventDefault();

        let account = getAccountForm();
        if(account == null) {
            console.log("Request not send due to error");
        } else {
            sendThenDisplayResult('/account', "POST", account).finally();
        }
    });

    $('#get-account').click(function(event) {
            event.preventDefault();
            processRequestForAccount().finally();//process reactive style
    });

    $('#clear-account').click(function(event) {
            event.preventDefault();
            document.getElementById("account-id-input").value = "";
            document.getElementById("account-name-input").value = "";
            document.getElementById("account-nickname-input").value = "";
            document.getElementById("account-trips-input").value = "";
            let alert = document.getElementById("alertStatus");
            alert.className = '';
            alert.innerHTML = null;
    });

    //TODO add in delete for account

//------------------------------------------------------Location------------------------------------------------------
    $('#submit-location').click(function(event) {
        event.preventDefault();

        let location = getLocationForm();
        if(location == null) {
            console.log("Request not send due to error");
        } else {
            post_json('/location', location);
        }
    });

    $('#get-location').click(function(event) {
            event.preventDefault();
            processRequestForLocation().finally();//process reactive style
    });

    $('#clear-location').click(function(event) {
            event.preventDefault();
            document.getElementById("location-id-input").value = "";
            document.getElementById("location-name-input").value = "";
            document.getElementById("location-longitude-input").value = "";
            document.getElementById("location-latitude-input").value = "";
    });
 });

//------------------------------------------------------Functions------------------------------------------------------

async function sendThenDisplayResult(url = '/', method = 'GET', data = '', json = 'true'){
    fetch(url,  {
                    method: method, // *GET, POST, PUT, DELETE, etc.
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data),
                }
    ).then(response => {
//        console.log(response.status + ` ` + response.url);
        status = response.status;
        body = json = 'true' ? response.json() : response;
        displayResult(status, body);
    })
    .catch(error => {
        console.error('Error:', error);
        let alert = document.getElementById("alertStatus");
        alert.className = 'alert';
        alert.classList.add("alert-danger");
        alert.innerHTML = '<strong>Submitted ok</strong> ' + error;
    });
}

function displayResult(status ='0', body =''){
    let alert = document.getElementById("alertStatus");
    alert.className = 'alert';
    alert.classList.add("alert-success");
    alert.innerHTML = '<strong>'+status+'</strong> Submitted ok';
}

//------------------------------------------------------Account------------------------------------------------------
async function processRequestForAccount(){
    let result;
    let form_account = getAccountForm();//get account fields from form
    
    if(form_account == null) {
        console.log("Request not send due to error");
    } else {
        //determine type of get to perform, and wait for server response
        if(form_account.id != null && form_account.id != "") {
            result = await get_json('/account/' + form_account.id);
            let account = getAccountObjectFromResult(result);
            populateAccountForm(account);
        } else if(form_account.nickname != "" && form_account.nickname != null) {
            result = await get_json('/account/nickname/' + form_account.nickname);
            let account = getAccountObjectFromResult(result);
            populateAccountForm(account);
        } else if(form_account.name != "" && form_account.name != null) {
             result = await get_json('/account/name/' + form_account.name);//encodeURIComponent(name)
             populateAccountBlock(result);
        } else {
            alert("No identifiable information provided\nPlease enter an Id, Nickname, or Name");
        }
     }
 }

//create empty nodes for blank entries
function getAccountObjectFromResult(result) {
    let account = Object.create(Account);

    account.id = (result || {}).id;
    account.name = (result || {}).name;
    account.nickname = (result || {}).nickname;
    account.trips = (result || {}).trips;
    return account;
}

function populateAccountForm(account) {
    document.getElementById("account-id-input").value = account.id;
    document.getElementById("account-name-input").value = account.name;
    document.getElementById("account-nickname-input").value = account.nickname;
    document.getElementById("account-trips-input").value = convertToArrayString(account.trips);
}

function getAccountForm() {
   let account = Object.create(Account);
   account.id = $('#account-id-input').val();
   if(account.id == "") { // ensure null is sent when nothing is filled
       account.id = null;
   }
   account.name = $('#account-name-input').val();
   account.nickname = $('#account-nickname-input').val();
   if($('#account-trips-input').val() == "" || $('#account-trips-input').val() == null) { // ensure null is sent when nothing is filled
       account.trips = null;
   } else {
       try {
           account.trips = JSON.parse($('#account-trips-input').val());//["5d5827bb9660491f480fad3d"]
       } catch {
           window.alert("Please ensure trips are in format [\"value\",\"value\"]");
           return null;
       }
   }
   return account;
}

function populateAccountBlock(accountArray) {

    //populate the form with the first in the array
    let firstAccount = getAccountObjectFromResult(accountArray[0]);
    populateAccountForm(firstAccount);

    let accountBlock = document.getElementById("account-block");
    accountBlock.innerHTML = "";//clear

    accountArray.forEach(function(element) {
        let account = getAccountObjectFromResult(element);
        addNewAccountToBlock(account);
    });
}

function addNewAccountToBlock(account) {



    //html temples node

    let accountBlock = document.getElementById("account-block");
    let AccountBlockCnt = accountBlock.childElementCount;

    let accountCard = $(
        '<div class="m-2 p-2 border border-dark" id="account-card' + AccountBlockCnt + '">' +
            '<div id="account-id-input' + AccountBlockCnt + '">' + account.id + '</div>' +
            '<div id="account-name-input' + AccountBlockCnt + '">' + account.name + '</div>' +
            '<div id="account-nickname-input' + AccountBlockCnt + '">' + account.nickname + '</div>' +
            '<div id="account-trips-input' + AccountBlockCnt + '">' + account.trips.length + '</div>' +
            '<button class="btn-secondary" id="edit_account-button' + AccountBlockCnt + '" onclick="editAccount(event)">Edit Account &raquo;</button>' +
        '</div>');
    accountBlock.append(accountCard[0]);

//      alternative

//    let numberOfTrips =  account.trips.length;//at this point is in array format

//    let accountBlock = document.getElementById("account-block");

//    let card = document.createElement("div")
//    let id = document.createElement("div");
//    let name = document.createElement("div");
//    let nickname = document.createElement("div");
//    let trips = document.createElement("p");
//
//    card.className = "col-lg-4 card";
//
//    id.innerHTML = account.id;
//    name.innerHTML = account.name;
//    nickname.innerHTML = account.nickname;
//    trips.innerHTML = account.trips.length;
//
//    card.appendChild(id);
//    card.appendChild(name);
//    card.appendChild(nickname);
//    card.appendChild(trips);
//    accountBlock.appendChild(card);

//.addEventListener("click", editAccount, false);
}

async function editAccount (event) {
     let accountCard = event.currentTarget;
     let cardID = accountCard.id;
     cardID = cardID.substr(cardID.length - 1);
     let account = Object.create(Account);


     let selectedId = '#account-id-input' + cardID;
     let selectedAccountID =  $(selectedId).text();

     let result = await get_json('/account/' + selectedAccountID);
     account = getAccountObjectFromResult(result);
     populateAccountForm(account);

}

//------------------------------------------------------Location------------------------------------------------------
function getLocationForm() {
    let location = Object.create(Location);
    location.id = $('#location-id-input').val();
    if(location.id == "") { // ensure null is sent when nothing is filled
        location.id = null;
    }
    location.name = $('#location-name-input').val();
    location.longitude = $('#location-longitude-input').val();
    location.latitude = $('#location-latitude-input').val();

    return location;
}

async function processRequestForLocation(){
    let result;
    let form_location = getLocationForm();//get location fields from form

    if(form_location == null) {
        console.log("Request not send due to error");
    } else {
        //determine type of get to perform, and wait for server response
        if(form_location.id != null && form_location.id != "") {
            result = await get_json('/location/' + form_location.id);
//        } else if(form_location.name != "" && form_location.name != null) {//Not supporting list atm
//            result = await get_json('/location/name/' + form_location.name);//encodeURIComponent(name)
        }else if(form_location.name != "" && form_location.name != null) {
            result = await get_json('/location/name/' + form_location.name + "/first");//encodeURIComponent(name)
        } else if(form_location.nickname != "" && form_location.nickname != null) {
            result = await get_json('/location/longitude/' + form_location.longitude
            + '/latitude/' + form_location.latitude);
        } else {
            alert("No identifiable information provided\nPlease enter an Id, Name or Longitude and Latitude");
        }

        let location = getLocationObjectFromResult(result);
        populateLocationForm(location);
     }
 }

//create empty nodes for blank entries
function getLocationObjectFromResult(result) {
    let location = Object.create(Location);

    location.id = (result || {}).id;
    location.name = (result || {}).name;
    location.longitude = (result || {}).longitude;
    location.latitude = (result || {}).latitude;
    return location;
}

function populateLocationForm(location) {
    document.getElementById("location-id-input").value = location.id;
    document.getElementById("location-name-input").value = location.name;
    document.getElementById("location-longitude-input").value = location.longitude;
    document.getElementById("location-latitude-input").value = location.latitude;
}

//Util
function convertToArrayString(string) {
    //not working :/
//    let stringWithQuotes = string.replace(',', '","');
//    return "[\"" + stringWithQuotes + "\"]"
    //But
       return JSON.stringify(string, "", 0)
}