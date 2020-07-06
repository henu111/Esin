<html>
<#include "../common/header.ftl">

    <body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 600px;height:400px;"></div>
<script>
    /*动态添加数据*/
    var xAxisData = ["天数"];
    var data1 = [21,33,1];
    var data2 = [6,12,9];
    for (var i = 2; i < 32; i++) {
        //xAxisData.push('' + i);
        xAxisData.push('' + i);
        data1.push((Math.sin(i / 5) * (i / 5 -10) + i / 6) * 5);
        data2.push((Math.cos(i / 5) * (i / 5 -10) + i / 6) * 5);

    }

    option = {
        title: {
            text: '销售数据'
        },
        legend: {
            data: ['支出', '收入']
        },
        toolbox: {
            // y: 'bottom',
            feature: {
                magicType: {
                    type: ['stack', 'tiled']
                },
                dataView: {},
                saveAsImage: {
                    pixelRatio: 2
                }
            }
        },
        tooltip: {},
        xAxis: {
            data: xAxisData,
            splitLine: {
                show: false
            }
        },
        yAxis: {
        },
        series: [{
            name: '支出',
            type: 'bar',
            data: data1,
            animationDelay: function (idx) {
                return idx * 10;
            }
        }, {
            name: '收入',
            type: 'bar',
            data: data2,
            animationDelay: function (idx) {
                return idx * 10 + 100;
            }
        }],
        animationEasing: 'elasticOut',
        animationDelayUpdate: function (idx) {
            return idx * 5;
        }
    };
</script>
    </body>
</html>