<template>
    <!-- 告警最新热点组件 -->
    <el-scrollbar class="yw-scrollbar scroll-y dark"
                  style="height:100%">
        <div class="yw-alert-hot-wrap">
            <vueSeamlessScroll :data="datas"
                               :class-option="defaultOption"
                               class="seamless-warp">
                <section class="yw-alert"
                         v-for="(item,index) in datas"
                         :key="index">
                    <div class="fast-order"
                         style="width:30%">
                        <p class="fast-status"
                           :class=" `bg-linear-${item.status}`">{{item.statusName}}</p>
                    </div>
                    <div class="fast-details"
                         style="width:70%">
                        <el-tooltip class="item"
                                    effect="dark"
                                    :content="item.name"
                                    placement="top-start">
                            <p class="fast-name text-ellipse">{{item.name}}</p>
                        </el-tooltip>
                        <el-tooltip class="item"
                                    effect="dark"
                                    :content="item.desc"
                                    placement="top-start">
                            <p class="fast-desc text-ellipse">{{item.desc}}</p>
                        </el-tooltip>
                        <el-tooltip class="item"
                                    effect="dark"
                                    :content="item.date"
                                    placement="top-start">
                            <p class="fast-date text-ellipse">{{item.date}}</p>
                        </el-tooltip>
                    </div>
                </section>
            </vueSeamlessScroll>
        </div>
    </el-scrollbar>

</template>

<script>
    import vueSeamlessScroll from 'vue-seamless-scroll'

    export default {
        props: ['datas'],
        components: {
            vueSeamlessScroll
        },
        data() {
            return {
                // 滚动自定义
                defaultOption: {
                    step: 1, // 数值越大速度滚动越快
                    limitMoveNum: 8, // 开始无缝滚动的数据量 this.dataList.length
                    hoverStop: true, // 是否开启鼠标悬停stop
                    direction: 1, // 0向下 1向上 2向左 3向右
                    openWatch: true, // 开启数据实时监控刷新dom
                    singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
                    singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
                    waitTime: 20 // 单步运动停止的时间(默认值1000ms)
                },
                // datas: [
                //  {          name: '待确认',
                //       icon: require('src/assets/theme/dark/img/alert-wait-confirm.png'),
                //       num: 0,
                //       serious: 0,
                //       high: 0,
                //       middle: 0,
                //       low: 0,
                //     }
                // ],
            }
        },
        methods: {

        },

    }
</script>
<style lang="scss" scoped>
    .yw-alert-hot-wrap {
        width: 100%;
        min-height: 100%;
        .yw-alert {
            display: flex;
            justify-content: center;
            height: 25%;
            padding: 5px 0;
            color: $color-base;
            background: #080831;
            &:nth-child(even) {
                background: $color-blue;
            }
        }
        .fast-order {
            text-align: center;
        }
        .fast-details {
            padding-right: 15px;
        }
        .fast-status {
            display: inline-block;
            width: 50px;
            height: 24px;
            line-height: 24px;
            border-radius: $border-radius;
            text-align: center;
            font-size: $font-14;
            text-shadow: 2px 3px 10px #000;
        }
        .fast-name {
            font-size: $font-14;
        }
        .fast-desc {
            font-size: $font-12;
            color: $color-blue-xs;
        }
        .fast-date {
            font-size: $font-12;
            color: $color-blue-xs;
        }
    }
</style>
