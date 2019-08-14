
window.addEventListener("load", function () {

    $('#submit-account').click(function(event) {
        event.preventDefault();

        let account = getAccountForm();
        post_json('/account', account);
    });
 });

 function getAccountForm() {
    let account = Object.create(Account);
    account.id = $('#account_id').val();
    if(account.id == "") { // ensure null is sent when nothing is filled
        account.id = null;
    }
    account.name = $('#name-input').val();
    account.nickname = $('#nickname-input').val();
    account.trips = [];//assume new account has no trips
    return account;
 }