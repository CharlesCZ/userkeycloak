$(document)
    .ready(
        function () {
            $.ajax({
                url: "/newOrderList",
                success: function (result) {
                    $("#orderCount").text("New orders: " + result.length)
                }
            });
        }
    );




$(document).ready(function () {

    $.ajax({
        url: "/newOrderList",
        async: false,
        dataType: "json",
        type: "GET",
        success: function (result) {
            var regEx = /^\d{4}-\d{2}-\d{2}/;
            var dates = result.map(order =>new Date(regEx.exec(order.lastModifiedDate).input));
            var maxDate = dates.reduce(function(x,y){
                return x < y ? y: x;

            });
            console.log(maxDate);
            $("#dateFooter").text("Updated at: "+maxDate);
            $("#dateFooter1").text("Updated at: "+maxDate);
        }
    });
});
