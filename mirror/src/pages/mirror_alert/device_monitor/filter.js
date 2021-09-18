export default {
    data() {
        return {
            // 调色盘(蓝、绿、橙、黄、红\紫色)
            lineColorList: ['#6FDBF8', '#7CE3B8', '#D77728', '#EAB839', '#E24D42', '#E04BF5', '#C13ED3', '#6C087C'],
        }
    },
    methods: {
        // rgb颜色随机
        rgbRandom() {
            var r = Math.floor(Math.random() * 256)
            var g = Math.floor(Math.random() * 256)
            var b = Math.floor(Math.random() * 256)
            var rgb = '(' + r + ',' + g + ',' + b + ')'
            return rgb
        },
    },


}
