window.addEventListener("load", function() {

  //debug
  //document.getElementById("account-nickname-input").value = "theDean";
  document.getElementById("account-name-input").value = "Dean";
  document.getElementById("trip-id-input").value = "5d8784fee246f610b84fdfce";

    //initialize the date time pickers
    $('#trip-leaving-input').datetimepicker({
        showMeridian: true,
        startDate: '+1h',
        minuteStep: 1,
    });
    $('#trip-arriving-input').datetimepicker({
        showMeridian: true,
        startDate: '+1h',
        minuteStep: 1,
    });

    /*******************testing section************************/


    //have input, with hidden dropdown
    // - on key up check that length of input is greater than 3 characters
    // - in api, add in a service for Location name starting with, return max 8 names only
    // - use code to show the dropdown on results greater than 1,
    // - search when key released, and not 13 ie enter


    /*******************testing section************************/


  //------------------------------------------------------Account------------------------------------------------------
  $('#submit-account').click(function(event) {
    event.preventDefault();

    let account = getAccountForm();
    if (account == null) {
      displayResult("0", "Request not send due to error");
    } else {
      submitAccount(account);
    }
  });

  $('#get-account').click(function(event) {
    event.preventDefault();
    processRequestForAccount();
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
    let accountBlock = document.getElementById("account-block");
    accountBlock.innerHTML = ""; //clear
  });

  //TODO add in delete for account

  //------------------------------------------------------Location------------------------------------------------------
  $('#submit-location').click(function(event) {
    event.preventDefault();

    let location = getLocationForm();
    if (location == null) {
      console.log("Request not send due to error");
    } else {
      post_json('/location', location);
    }
  });

  $('#get-location').click(function(event) {
    event.preventDefault();
    processRequestForLocation().finally(); //process reactive style
  });

  $('#clear-location').click(function(event) {
    event.preventDefault();
    document.getElementById("location-id-input").value = "";
    document.getElementById("location-name-input").value = "";
    document.getElementById("location-longitude-input").value = "";
    document.getElementById("location-latitude-input").value = "";
  });
  //------------------------------------------------------Trip------------------------------------------------------
  $('#submit-trip').click(async function(event) {
    event.preventDefault();

    let trip = await getTripForm();
    if (trip == null) {
      displayResult("", "Request not send due to error");
    } else {
      trip = submitTrip(trip);
    }
  });

  $('#get-trip').click(function(event) {
    event.preventDefault();
    processRequestForTrip().finally(); //process reactive style
  });

  $('#clear-trip').click(function(event) {
    event.preventDefault();
    document.getElementById("trip-id-input").value = "";
    document.getElementById("trip-leaving-input").value = "";
    document.getElementById("trip-arriving-input").value = "";
    document.getElementById("trip-departing-input").value = "";
    document.getElementById("trip-destination-input").value = "";
    document.getElementById("trip-mode-input").value = "";
    document.getElementById("trip-discoverable-input").value = "";
    document.getElementById("trip-attending-input").value = "";

    let alert = document.getElementById("alertStatus");
    alert.className = '';
    alert.innerHTML = null;
    let tripBlock = document.getElementById("trip-block");
    tripBlock.innerHTML = ""; //clear
  });

    $('#trip-leaving-input').click(function(event) {
        $('#trip-leaving-input').datetimepicker();
    });

    $('#trip-arriving-input').click(function(event) {
        $('#trip-arriving-input').datetimepicker();
    });
}); //end load

//------------------------------------------------------Functions------------------------------------------------------

//------------------------------------------------------Account------------------------------------------------------

async function submitAccount(data = '') {
  let account = Object.create(Account);

  account = await sendRequest('/account', 'POST', data);
  populateAccountForm(account);
}

async function processRequestForAccount() {
  let result;
  let form_account = getAccountForm(); //get account fields from form

  if (form_account == null) {
    console.log("Request not send due to error");
  } else {
    //determine type of get to perform, and wait for server response
    if (form_account.id != null && form_account.id != "") {
      result = await sendRequest('/account/' + form_account.id);
      let account = getAccountObjectFromResult(result);
      populateAccountForm(account);
    } else if (form_account.nickname != "" && form_account.nickname != null) {
      result = await sendRequest('/account/nickname/' + form_account.nickname);
      let account = getAccountObjectFromResult(result);
      populateAccountForm(account);
    } else if (form_account.name != "" && form_account.name != null) {
      result = await sendRequest('/account/name/' + form_account.name); //encodeURIComponent(name)
      populateAccountBlock(result);
    } else {
      alertUser("No identifiable information provided" +
        "\nPlease enter an Id, Nickname, or Name", 'alert-dismissible');
      //            alert("No identifiable information provided\nPlease enter an Id, Nickname, or Name");
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
  document.getElementById("account-id-input").value = (account || {}).id;
  document.getElementById("account-name-input").value = (account || {}).name;
  document.getElementById("account-nickname-input").value = (account || {}).nickname;
  document.getElementById("account-trips-input").value = convertToArrayString((account || {}).trips);
}

function getAccountForm() {
  let account = Object.create(Account);
  account.id = $('#account-id-input').val();
  if (account.id == "") { // ensure null is sent when nothing is filled
    account.id = null;
  }
  account.name = $('#account-name-input').val();
  account.nickname = $('#account-nickname-input').val();
  if ($('#account-trips-input').val() == "" || $('#account-trips-input').val() == null) { // ensure null is sent when nothing is filled
    account.trips = null;
  } else {
    try {
      account.trips = JSON.parse($('#account-trips-input').val()); //["5d5827bb9660491f480fad3d"]
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
  accountBlock.innerHTML = ""; //clear

  accountArray.forEach(function(element) {
    let account = getAccountObjectFromResult(element);
    addNewAccountToBlock(account);
  });
}

function addNewAccountToBlock(account) {
  let accountBlock = document.getElementById("account-block");
  let accountBlockCount = accountBlock.childElementCount;

  let accountTemplate = document.getElementById('account-template').content.cloneNode(true);
  accountTemplate.querySelector('.account-id').innerText = account.id;
  accountTemplate.querySelector('.account-name').innerText = account.name;
  accountTemplate.querySelector('.account-nickname').innerText = account.nickname;
  accountTemplate.querySelector('.account-trips').innerText = account.trips.length;
  accountTemplate.id = 'account-template' + accountBlockCount;
  document.getElementById("account-block").appendChild(accountTemplate);
}

async function editAccount(event) {
  let idToEdit = event.currentTarget.parentNode.querySelector('.account-id').innerText;
  let account = Object.create(Account);

  let result = await sendRequest('/account/' + idToEdit);
  account = getAccountObjectFromResult(result);
  populateAccountForm(account);
}

//------------------------------------------------------Location------------------------------------------------------
function getLocationForm() {
  let location = Object.create(Location);
  location.id = $('#location-id-input').val();
  if (location.id == "") { // ensure null is sent when nothing is filled
    location.id = null;
  }
  location.name = $('#location-name-input').val();
  location.longitude = $('#location-longitude-input').val();
  location.latitude = $('#location-latitude-input').val();

  return location;
}

async function processRequestForLocation() {
  let result;
  let form_location = getLocationForm(); //get location fields from form

  if (form_location == null) {
    console.log("Request not send due to error");
  } else {
    //determine type of get to perform, and wait for server response
    if (form_location.id != null && form_location.id != "") {
      result = await get_json('/location/' + form_location.id);
      //        } else if(form_location.name != "" && form_location.name != null) {//Not supporting list atm
      //            result = await get_json('/location/name/' + form_location.name);//encodeURIComponent(name)
    } else if (form_location.name != "" && form_location.name != null) {
      result = await get_json('/location/name/' + form_location.name + "/first"); //encodeURIComponent(name)
    } else if (form_location.nickname != "" && form_location.nickname != null) {
      result = await get_json('/location/longitude/' + form_location.longitude +
        '/latitude/' + form_location.latitude);
    } else {
      alert("No identifiable information provided" +
        "\nPlease enter an Id, Name or Longitude and Latitude");
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

//------------------------------------------------------Trip------------------------------------------------------

async function submitTrip(data = '') {
  let trip = Object.create(Trip);

  trip = await sendRequest('/trip', 'POST', data);
  populateTripForm(trip);
}

function populateTripBlock(tripArray) {
    let firstTrip = getTripObjectFromResult(tripArray[0]);
    populateTripForm(firstTrip);

    let tripBlock = document.getElementById("trip-block");
    tripBlock.innerHTML = "";

    tripArray.forEach(function(element) {
        let trip = getTripObjectFromResult(element);
        addNewTripToBlock(trip);
    });
}

function addNewTripToBlock(trip) {
    let tripBlock = document.getElementById("trip-block");
    let tripBlockCount = tripBlock.childElementCount;

    let tripTemplate = document.getElementById('trip-template').content.cloneNode(true);
    tripTemplate.querySelector('.trip-id').innerText = trip.id;
    tripTemplate.querySelector('.trip-leaving').innerText = trip.leaving;
    tripTemplate.querySelector('.trip-arriving').innerText = trip.arriving;
    tripTemplate.querySelector('.trip-departing').innerText = trip.departing.name;
    tripTemplate.querySelector('.trip-destination').innerText = trip.destination.name;
    tripTemplate.querySelector('.trip-mode').innerText = trip.mode;
    tripTemplate.querySelector('.trip-discoverable').innerText = trip.discoverable;
    tripTemplate.querySelector('.trip-attending').innerText = trip.attending.length;
    tripTemplate.id = 'trip-template' + tripBlockCount
    document.getElementById("trip-block").appendChild(tripTemplate);
}

async function editTrip(event) {
  let idToEdit = event.currentTarget.parentNode.querySelector('.trip-id').innerText;
  let trip = Object.create(Trip);

  let result = await sendRequest('/trip/' + idToEdit);
  trip = getTripObjectFromResult(result);
  populateTripForm(trip);
}

async function getTripForm() {
  let trip = Object.create(Trip);
  trip.id = $('#trip-id-input').val();

  if (trip.id == "") { // ensure null is sent when nothing is filled
    trip.id = null;
  }
  trip.leaving = $('#trip-leaving-input').val();
  trip.arriving = $('#trip-arriving-input').val();

  //get based on name and fill in
  let departing = $('#trip-departing-input').val();
  if (departing != "" && departing != null) {
    let departingLocation = await sendRequest('/location/name/' + departing + "/first");
    trip.departing = departingLocation;
  } else {
    trip.departing.name = null;
  }

  //get based on name value
  let destination = $('#trip-destination-input').val();
  if (destination != "" && destination != null) {
    let destinationLocation = await sendRequest('/location/name/' + destination + "/first");
    trip.destination = destinationLocation;
  } else {
    trip.destination.name = null;
  }

  trip.mode = $('#trip-mode-input').val(); //todo make dropdown
  trip.discoverable = $('#trip-discoverable-input').val(); //todo make checkbox

  if ($('#trip-attending-input').val() == "" || $('#trip-attending-input').val() == null) { // ensure null is sent when nothing is filled
    trip.attending = null;
  } else {
    try {
      trip.attending = JSON.parse($('#trip-attending-input').val()); //["5d5827bb9660491f480fad3d"]
    } catch {
      let alert = document.getElementById("alertStatus");
      alert.className = 'alert';
      alert.classList.add("alert-danger");
      alert.innerHTML = '<strong>Array format</strong> Please ensure attending is in format [\"value\",\"value\"]';
      document.body.scrollTop = 0; // For Safari
      document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
      //           window.alert("Please ensure attending are in format [\"value\",\"value\"]");
      return null;
    }
  }
  return await trip;
}

async function processRequestForTrip() { //convert to trip for get
  let result;
  let formTrip = Object.create(Trip);
  formTrip = await getTripForm(); //get trip fields from form

  if (formTrip == null) {
    alertUser("Request not send due to error")
  } else {
    //determine type of get to perform, and wait for server response
    if (formTrip.id != null && formTrip.id != "") {
      result = await sendRequest('/trip/' + formTrip.id);
      let trip = getTripObjectFromResult(result);
      populateTripForm(trip);
    } else if (formTrip.departing.name != "" && formTrip.departing.name != null &&
      formTrip.leaving != "" && formTrip.leaving != null &&
      formTrip.destination.name != "" && formTrip.destination.name != null &&
      formTrip.arriving != "" && formTrip.arriving != null) {
      // Time for queries need to be in format 2019-01-01 00:00 to work NOT 2019-01-01T00:00:00
      result = await sendRequest('/trip' +
        '/departing/' + formTrip.departing.name + '/leaving/' + formTrip.leaving +
        '/destination/' + formTrip.destination.name + '/arriving/' + formTrip.arriving);
      populateTripBlock(result);
    } else if (formTrip.departing.name != "" && formTrip.departing.name != null &&
      formTrip.destination.name != "" && formTrip.destination.name != null) {
      // Time for queries need to be in format 2019-01-01 00:00 to work NOT 2019-01-01T00:00:00
      result = await sendRequest('/trip' + '/departing/' + formTrip.departing.name + '/destination/' + formTrip.destination.name);
      populateTripBlock(result);
    } else if (formTrip.departing.name != "" && formTrip.departing.name != null &&
      formTrip.leaving != "" && formTrip.leaving != null) {
      // Time for queries need to be in format 2019-01-01 00:00 to work NOT 2019-01-01T00:00:00
      result = await sendRequest('/trip' + '/departing/' + formTrip.departing.name + '/leaving/' + formTrip.leaving);
      populateTripBlock(result);
    } else if (formTrip.destination.name != "" && formTrip.destination.name != null &&
      formTrip.arriving != "" && formTrip.arriving != null) {
      // Time for queries need to be in format 2019-01-01 00:00 to work NOT 2019-01-01T00:00:00
      result = await sendRequest('/trip' + '/destination/' + formTrip.destination.name + '/arriving/' + formTrip.arriving);
      populateTripBlock(result);
    } else if (formTrip.departing.name != "" && formTrip.departing.name != null) {
      // Time for queries need to be in format 2019-01-01 00:00 to work NOT 2019-01-01T00:00:00
      result = await sendRequest('/trip' + '/departing/' + formTrip.departing.name);
      populateTripBlock(result);
    } else if (formTrip.destination.name != "" && formTrip.destination.name != null) {
      // Time for queries need to be in format 2019-01-01 00:00 to work NOT 2019-01-01T00:00:00
      result = await sendRequest('/trip' + '/destination/' + formTrip.destination.name);
      populateTripBlock(result);
    } else {
      alertUser("No identifiable information provided" +
        "\nPlease enter an Id, leaving, arriving, departing, destination", 'alert-dismissible');
    }
  }
}

function getTripObjectFromResult(result) {
  let trip = Object.create(Trip);

  trip.id = (result || {}).id;
  trip.leaving = (result || {}).leaving;
  trip.arriving = (result || {}).arriving;
  trip.departing = (result || {}).departing;
  trip.departing.name = ((result || {}).departing || {}).name;
  trip.destination.name = ((result || {}).destination || {}).name;
  trip.mode = (result || {}).mode;
  trip.discoverable = (result || {}).discoverable;
  trip.attending = (result || {}).attending;
  return trip;
}

function populateTripForm(trip) {

  document.getElementById("trip-id-input").value = trip.id;
  document.getElementById("trip-leaving-input").value = trip.leaving;
  document.getElementById("trip-arriving-input").value = trip.arriving;
  document.getElementById("trip-departing-input").value = trip.departing.name;
  document.getElementById("trip-destination-input").value = trip.destination.name;
  document.getElementById("trip-mode-input").value = trip.mode;
  document.getElementById("trip-discoverable-input").value = trip.discoverable;
  document.getElementById("trip-attending-input").value = convertToArrayString(trip.attending);
}
//------------------------------------------------------Util------------------------------------------------------

async function sendRequest(url = '/', method = 'GET', data = '', json = 'true') {
  // Reuse Function for request to get back the json by default
  return await fetch(url, {
    method: method,
    headers: {
      "Content-Type": "application/json; charset=utf-8",
    },
    body: method == 'GET' ? null : JSON.stringify(data),
  }).then(async (payload) => {
    status = payload.status;
    //        body = json == 'true' ? response.json() : response;
    body = await payload.json(); //request comes before the data, hence second await
    displayResult(status, body);
    return body;
  }).catch(error => {
    console.error('Error:', error);
    let alert = document.getElementById("alertStatus");
    alert.className = 'alert';
    alert.classList.add("alert-danger");
    alert.innerHTML = '<strong>Error during submission </strong> ' + error;
  });
}

function displayResult(status = '0', body = '') {
  let alert = document.getElementById("alertStatus");
  alert.className = 'alert';
  alert.classList.add("alert-success");
  alert.innerHTML = '<strong>' + status + '</strong>: ' + JSON.stringify(body, "", 0);
}

function alertUser(message = '<strong>Something wend wrong</strong> check with input data', type = 'alert-danger') {
  let alert = document.getElementById("alertStatus");
  alert.className = 'alert';
  alert.classList.add(type);
  alert.innerHTML = message;
  document.body.scrollTop = 0; // For Safari
  document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}

function convertToArrayString(string) {
  return JSON.stringify(string, "", 0)
}
