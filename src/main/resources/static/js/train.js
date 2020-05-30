window.addEventListener("load", function() {

    //debug
    document.getElementById("account-name-input").value = "thedean";

    /*******************testing section************************/

    /*******************testing section************************/

    //------------------------------------------------------initialize------------------------------------------------------
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
    //------------------------------------------------------on-click------------------------------------------------------

    $('#trip-leaving-input').click(function(event) {
        $('#trip-leaving-input').datetimepicker();
    });
    $('#trip-arriving-input').click(function(event) {
        $('#trip-arriving-input').datetimepicker();
    });

    $('#create-trip-btn').click(function(event) {
        let createTripSection = document.getElementById("createTrainTrip");
        if(createTripSection.classList.contains("show")) {
            //hide create trip section
            document.getElementById("create-trip-btn").innerText = "Create a new Train Trip";
        } else {
            let alert = document.getElementById("alertStatus");
            alert.className = '';
            alert.innerHTML = null;
            document.getElementById("create-trip-btn").innerText = "Cancel New Trip";

            //add logged in user to attending tiles for a new trip automatically
            let userName = document.getElementById("account-name-input").value.toLowerCase();

            let initialAttending = convertToArrayString([userName]);

            document.getElementById("trip-attending-input").value = initialAttending;
            document.getElementById("trip-mode-input").value = "Train";
            document.getElementById("trip-discoverable-input").checked = true;

            populateAttendingAccountBlock([userName]);
        }
    });

    $('#submit-trip').click(async function(event) {
        event.preventDefault();

        let trip = await getTripForm();
        if (trip == null) {
          alertUser("Request could not send due to error");
        } else {
            trip = submitTrip(trip);
            if (trip == null) {
                alertUser("An error occurred");
            } else {
                displayPositiveResult("Created", "New Trip was saved successfully");
            }
        }
      });
}); //end load

//------------------------------------------------------Functions------------------------------------------------------
function keyPressed(event){

    if ( event.which == 13 ) {
        event.preventDefault();
    }
}

//todo add in search for location name based on input

function populateAttendingAccountBlock(accountArray) {

    let accountBlock = document.getElementById("attending-account-block");
    accountBlock.innerHTML = ""; //clear

    const accounts = accountArray.map(nickname => sendRequest('/account/nickname/' + nickname));

    Promise.all(accounts).then(result => {
        result.forEach( function(element) {
            let account = getAccountObjectFromResult(element);
            addNewAccountToBlock(account);
            console.log("adding to block: " + account.nickname);
        });
        console.log("now adding the add block");
        addAccountBlock();
    });
}

function addAccountBlock() {
    let accountBlock = document.getElementById("attending-account-block");

    let accountTemplate = document.getElementById('account-template').content.cloneNode(true);
    accountTemplate.getElementById('find-account-nickname').classList.remove("d-none");//replace hidden ?
    accountTemplate.querySelector('.btn-secondary').classList.remove("d-none");

    accountTemplate.querySelector('.account-nickname').remove();
    accountTemplate.querySelector('.btn-dark').remove();
    document.getElementById("attending-account-block").appendChild(accountTemplate);
}

function addNewAccountToBlock(account) {
  let accountBlock = document.getElementById("attending-account-block");
  let accountBlockCount = accountBlock.childElementCount;

  let accountTemplate = document.getElementById('account-template').content.cloneNode(true);
  accountTemplate.querySelector('.account-id').innerText = account.id;
  accountTemplate.querySelector('.account-nickname').innerText = account.nickname;
  accountTemplate.id = 'account-template' + accountBlockCount;
  document.getElementById("attending-account-block").appendChild(accountTemplate);
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

function removeAccount(event) {
    event.preventDefault();
    let nickname = event.currentTarget.parentNode.querySelector('.account-nickname').innerText.toLowerCase();

    let attending = document.getElementById("trip-attending-input").value;
    attendingArray = JSON.parse(attending);
    attendingArray = attendingArray.filter(arrayItem => arrayItem.toLowerCase() !== nickname);
    document.getElementById("trip-attending-input").value = convertToArrayString(attendingArray);

    event.currentTarget.parentNode.remove();//todo fix
}

async function addAttendee(event) {
    //there is a an odd but that when you press enter on the input for the nickname attendee to add,
    //the event of the first element in the parent is triggered
    event.preventDefault();

    let nickname = event.currentTarget.parentNode.querySelector("#find-account-nickname").value;
    let account = Object.create(Account);

    let result = await sendRequest('/account/nickname/' + nickname);
    account = getAccountObjectFromResult(result);

    let attending = document.getElementById("trip-attending-input").value.toLowerCase();
    attending = JSON.parse(attending);
    attending.push(nickname);
    populateAttendingAccountBlock(attending);

    let attendingArray = convertToArrayString(attending);
    document.getElementById("trip-attending-input").value = attendingArray;
}

async function getTripForm() {
    let trip = Object.create(Trip);

    trip.leaving = convertToAmericanDate($('#trip-leaving-input').val());
    trip.arriving = convertToAmericanDate($('#trip-arriving-input').val());

    trip.leaving  = new Date(trip.leaving);
    trip.arriving = new Date(trip.arriving);

  //get based on name and fill in
  //todo make search on froned, and just send
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

  trip.mode = $('#trip-mode-input').val();
  trip.discoverable = $('#trip-discoverable-input').checked;

    let attending = document.getElementById("trip-attending-input").value;
    attending = JSON.parse(attending);

  if ($('#trip-attending-input').val() == "" || $('#trip-attending-input').val() == null) { // ensure null is sent when nothing is filled
    trip.attending = null;
  } else {
    try {
      trip.attending = JSON.parse($('#trip-attending-input').val()); //["5d5827bb9660491f480fad3d"]
    } catch {
      let alert = document.getElementById("alertStatus");
      alert.className = 'alert';
      alert.classList.add("alert-danger");
      alert.innerHTML = '<strong>Something went wrong: </strong>Please ensure all the fields are filled in correctly or'
      + '\nPlease refresh and try again';
      document.body.scrollTop = 0; // For Safari
      document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
      //           window.alert("Please ensure attending are in format [\"value\",\"value\"]");
      return null;
    }
  }
  return await trip;
}

async function submitTrip(data = '') {
  let trip = Object.create(Trip);

  return await sendRequest('/trip', 'POST', data);
}
