<template>
    <div class="progress-box">
        <!-- 单行进度条（标签在外面） -->
        <section class="yw-progress"
                 v-for="(subItem,subIndex) in datas"
                 :key="subItem.device_type + subIndex">
            <div class="f12">
                <span class="light-blue">
                    {{subItem.device_type}} <span class="mleft10 blue">已使用{{subItem.use_count}}</span>
                </span>
                <span class="dark-blue">
                    / 总{{subItem.total_count}}
                </span>
            </div>
            <el-progress :percentage="handleRate(subItem.use_count, subItem.total_count)"
                         :show-text="false"
                         :stroke-width="4"
                         :style="option.style"></el-progress>
        </section>
    </div>
</template>
<script>
    export default {
        props: {
            // 数据
            datas: {
                type: [Array],
                default() {
                    return []
                }
            },
            // 配置
            option: {
                type: Object,
                default: function () {
                    return {
                        // 类型
                        type: 'multiple',// ['single','multiple']
                        // 标题
                        title: {
                            position: 'up',// ['up','down','left','right']
                        },
                        // 样式
                        style: {
                            width: '100%'
                        },
                        // 标签
                        label: {
                            // 位置
                            position: 'outer',// ['outer','inner']
                            // 百分比
                            percentage: {
                                show: true
                            },
                            // 本身数据
                            num: {
                                show: false
                            }
                        }
                    }
                }
            }
        },
        data() {
            return {
                // datas: [{ name: '服务器', list: [], status: 'blue' }]
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
                        //   obj = 'color-blue';
                        //   break;
                        default:
                            obj = 'color-base'
                            break
                    }
                    return obj
                }
            },
            // 进度条背景色颜色设置
            rankBgColor() {
                return function (index) {
                    let obj = ''

                    switch (index) {
                        case 1:
                            obj = 'red'
                            break
                        case 2:
                            obj = 'orange'
                            break
                        case 3:
                            obj = 'yellow'
                            break
                        default:
                            obj = 'blue'
                            break
                    }
                    return obj
                }
            },
        },

        methods: {
            handleRate(num01 = 0, num02 = 0) {
                if (!num01 && !num02) {
                    return 0
                }
                return (num01 / num02).toFixed(4) * 100
            }
        },
        mounted() {
        }

    }
</script>
<style lang="scss" scoped>
    .progress-box {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
    }
    .yw-progress {
        flex: 1;
        line-height: 90%;
    }
</style>
