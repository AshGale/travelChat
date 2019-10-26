
//----------------------------------------------------------

const host = `http://localhost:8080/`;//can update this when deployed

const close_button_margin = '40px';

const Account = {
    id : null,
    name : '',
    nickname : '',
    trips : []
};

const Location = {
    id : null,
    name : '',
    longitude : 0.0,
    latitude : 0.0
};

const Trip = {
    id : null,
    leaving : '2019-01-01T00:00:00',
    arriving : '2019-01-01T00:00:00',
    departing :
    {
        id : null,
        name : '',
        longitude : 0.0,
        latitude : 0.0
    },
    destination :
    {
        id : null,
        name : '',
        longitude : 0.0,
        latitude : 0.0
    },
    mode : 'Walk',
    discoverable : 'true',
    attending : []
};

function post_json(url = ``, data = ``) {
    // Default options are marked with *
    return fetch(url, {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
        .then(response => {

            console.log(response.status + ` ` + response.url);
            return response.json();
        })
        .catch(error => {
             console.error('Error:', error)
            return error;
        });
}

function get_json(url = ``) {
    return fetch(url)
        .then(function (response) {
            return response.json();
        })
        .catch(error => {
            console.error('Error:', error);
            return error;
        });
}

function get_responce(url = ``) {
    return fetch(url)
        .catch(error => {
            console.error('Error:', error);
            return error;
        });
}

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
    return body;
  }).catch(error => {
    console.error('Error:', error);
    let alert = document.getElementById("alertStatus");
    alert.className = 'alert';
    alert.classList.add("alert-danger");
    alert.innerHTML = '<strong>Error during submission </strong> ' + error;
  });
}

function convertToArrayString(string) {
  return JSON.stringify(string, "", 0)
}