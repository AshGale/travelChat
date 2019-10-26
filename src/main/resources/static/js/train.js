window.addEventListener("load", function() {

    //debug
    document.getElementById("account-name-input").value = "theDean";

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

            document.getElementById("create-trip-btn").innerText = "Cancel New Trip";

            //add logged in user to attending tiles for a new trip automatically
            let userName = document.getElementById("account-name-input").value;

            let initialAttending = convertToArrayString([userName]);

            document.getElementById("trip-attending-input").value = initialAttending;
            document.getElementById("trip-mode-input").value = "Train";
            document.getElementById("trip-discoverable-input").checked = true;

            populateAttendingAccountBlock([userName]);
        }
    });

    //------------------------------------------------------Account------------------------------------------------------

}); //end load

//------------------------------------------------------Functions------------------------------------------------------
function keyPressed(event){

    if ( event.which == 13 ) {
        event.preventDefault();
    }
}

//todo add in search for location name based on input

async function populateAttendingAccountBlock(accountArray) {

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
    let nickname = event.currentTarget.parentNode.querySelector('.account-nickname').innerText;

    let attending = document.getElementById("trip-attending-input");
    attendingArray = JSON.parse(attending.value);
    attendingArray = attendingArray.filter(arrayItem => arrayItem !== nickname);
    attending.value = convertToArrayString(attendingArray);

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

    let attending = document.getElementById("trip-attending-input").value;
    attending = JSON.parse(attending);
    attending.push(nickname);
    populateAttendingAccountBlock(attending);

    let attendingArray = convertToArrayString(attending);
    document.getElementById("trip-attending-input").value = attendingArray;
}

