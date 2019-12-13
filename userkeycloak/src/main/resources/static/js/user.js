// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';


$(document).ready(function () {

    $.ajax({
        url: "/newOrderListForUser",
        data: "user="+$("#principal").text(),
        dataType: "json",
        type: "GET",
        success: function (result) {
            var ordersList;
            var regEx = /^\d{4}-\d{2}-\d{2}/;
            ordersList = result;
            var patt3 = ordersList.map(order => regEx.exec(order.createdDate)
        )
            ;
            var preetyDateString = patt3.map(str => str.input.split(":")[0]
        )
            ;
            var myMap = new Map();
            for (i = 0; i < preetyDateString.length; ++i) {
                var str1 = preetyDateString[i];
                var quantity = 0;
                ++quantity;
                for (j = i + 1; j < preetyDateString.length; ++j) {
                    var str2 = preetyDateString[j];
                    if (str1.localeCompare(str2) == 0) {
                        ++quantity;
                    }
                }

                var flag = false;
                for (var key of myMap.keys()) {
                    if (str1.localeCompare(key) == 0)
                        flag = true;
                }
                if (flag == false) {
                    myMap.set(str1, quantity);
                }

            }
            var valueArray = [];
            var dataArray = [];
            for (var value of myMap.values()) {
                console.log("values" + value)
                valueArray.push(value);
            }

            for (var key of myMap.keys()) {
                console.log("keys" + key)
                dataArray.push(key);
            }

// Area Chart Example
            var ctx = document.getElementById("myAreaChart");
            var myLineChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: dataArray,
                    datasets: [{
                        label: "Orders",
                        lineTension: 0.3,
                        backgroundColor: "rgba(2,117,216,0.2)",
                        borderColor: "rgba(2,117,216,1)",
                        pointRadius: 5,
                        pointBackgroundColor: "rgba(2,117,216,1)",
                        pointBorderColor: "rgba(255,255,255,0.8)",
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: "rgba(2,117,216,1)",
                        pointHitRadius: 50,
                        pointBorderWidth: 2,
                        data: valueArray,
                    }],
                },
                options: {
                    scales: {
                        xAxes: [{
                            time: {
                                unit: 'date'
                            },
                            gridLines: {
                                display: false
                            },
                            ticks: {
                                maxTicksLimit: 7
                            }
                        }],
                        yAxes: [{
                            ticks: {
                                min: 0,
                                max: 100,
                                maxTicksLimit: 5
                            },
                            gridLines: {
                                color: "rgba(0, 0, 0, .125)",
                            }
                        }],
                    },
                    legend: {
                        display: false
                    }
                }
            });

        }
    });
});










$(document)
    .ready(
        function () {
            $.ajax({
                url: "/newOrderListForUser",
                data: "user="+$("#principal").text(),
                success: function (result) {
                    $("#orderCount").text("New orders: " + result.length)
                }
            });
        }
    );





$(document).ready(function () {

    $.ajax({
        url: "/newOrderListForUser",
        data: "user="+$("#principal").text(),
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
