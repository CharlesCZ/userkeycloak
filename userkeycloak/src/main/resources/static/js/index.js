$(document)
    .ready(
        function () {
            $.ajax({
                url: "/newOrderList",
                success: function (result) {
                    $("#orderCount").text(result.length)
                }
            });
        }
    );