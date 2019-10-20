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
        //add logged in user to attending tiles for a new trip automatically
        let userName = document.getElementById("account-name-input").value;

        document.getElementById("trip-attending-input").value = '["' + userName + '"]';
        document.getElementById("trip-mode-input").value = "Train";
        document.getElementById("trip-discoverable-input").checked = true;
    });
    //------------------------------------------------------Account------------------------------------------------------

}); //end load

//------------------------------------------------------Functions------------------------------------------------------

//------------------------------------------------------Account------------------------------------------------------

