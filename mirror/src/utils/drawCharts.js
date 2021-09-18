/** 绘制图表常用方法**/
export default {
    props: {
        // 图表全局查询条件（来自父元素）
        conditionParams: {
            type: Object,
            default() {
                return {}
            }
        },
        moduleName: {
            type: String,
            default() {
                return ''
            }
        },
        isCustomPage: {
            type: Boolean,
            default() {
                return false
            }
        },
    },

    data() {
        return {
            // 自适应对象
            resizeFun: '',
            myChartObj: {}
        }
    },
    computed: {
        // 排行榜字体颜色设置
        rankColor() {
            return function (index) {
                let obj = 'color-base'
                switch (index) {
                    case 1:
                        obj = 'color-red'
                        break
                    case 2:
                        obj = 'color-orange'
                        break
                    case 3:
                        obj = 'color-yellow'
                        break
                    // case 4:
                    //     obj = 'color-blue';
                    //     break;
                    default:
                        obj = 'color-base'
                        break
                }
                return obj
            }
        },
    },

    methods: {
        // 设置需要自适应的图表对象
        setResizeFun(chartObj) {
            this.resizeFun = function () {
                chartObj.resize()
            }
            this.resizeChart()
            chartObj.hideLoading()
        },
        // 图表自适应
        resizeChart() {
            window.addEventListener('resize', this.resizeFun)
        },
        // 取消图表自适应
        removeResizeChart() {
            if (this.resizeFun) {
                window.removeEventListener('resize', this.resizeFun)
            }
        },
        // 初始化myChart，单个chart元素
        initMyChart(id) {
            let arr = Object.keys(this.myChartObj)
            if (arr.length === 0) {
                // 非空判断，避免报错
                let myChartDom = document.getElementById(id)
                if (!myChartDom) {
                    return
                }
                let myChart = echarts.init(myChartDom)
                this.chartShowLoading(myChart)
                return myChart
            }
            return this.myChartObj[id]
        },
        // 初始化myChartObj，包含了该模块下的全部chart元素，页面mounted时增加图表loading
        initMyChartObj(otherChart) {
            let myChartList = ['chartData', 'chartData1', 'chartList2', 'barChartData']
            if (otherChart) {
                myChartList.push(otherChart)
            }
            myChartList.forEach(chartDataKey => {
                let isKeyExist = this[chartDataKey] && this[chartDataKey].chartList && this[chartDataKey].chartList.length
                if (!isKeyExist) {
                    return
                }
                this[chartDataKey].chartList.forEach(item => {
                    // 非空判断，避免报错
                    let myChartDom = document.getElementById(item.id)
                    let myChart
                    if (!myChartDom) {
                        return
                    } else {
                        myChart = echarts.init(myChartDom)
                        this.myChartObj[item.id] = myChart
                        this.chartShowLoading(myChart)
                    }
                })
            })
        },
        chartShowLoading(myChart) {
            myChart.showLoading({
                text: '请稍候',
                color: '#40ACFF',
                textColor: '#40ACFF',
                maskColor: 'rgba(8, 33, 81, 0.5)',
                zlevel: 999,
                // 字体大小。从 `v4.8.0` 开始支持。
                fontSize: 12,
                // 是否显示旋转动画（spinner）。从 `v4.8.0` 开始支持。
                showSpinner: true,
                // 旋转动画（spinner）的半径。从 `v4.8.0` 开始支持。
                spinnerRadius: 10,
                // 旋转动画（spinner）的线宽。从 `v4.8.0` 开始支持。
                lineWidth: 5
            })
        }
    },
    mounted() {
        this.initMyChartObj()
    },
    destroyed() {
        this.removeResizeChart()
    }

}
