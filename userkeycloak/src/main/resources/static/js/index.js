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