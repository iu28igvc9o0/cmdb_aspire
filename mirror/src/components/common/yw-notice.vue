<template>
    <!-- 公告组件 -->
    <!-- <el-scrollbar class="yw-scrollbar scroll-y dark"
                style="height:100%"> -->
    <div class="yw-alert-hot-wrap">
        <!-- <vueSeamlessScroll :data="datas"
                         :class-option="defaultOption"
                         class="seamless-warp"> -->
        <section class="yw-alert"
                 v-for="(item,index) in datas"
                 :key="index">
            <div class="fast-order-wrap"
                 :style="activeStyle.orderStyle">
                <el-tooltip class="item"
                            effect="dark"
                            :content="item.name"
                            placement="top-start">
                    <p class="fast-name text-ellipse"
                       style="max-width:calc(100% - 40px);width:auto;">{{item.name}}</p>
                </el-tooltip>
                <p class="fast-status"
                   v-if="item.statusName"
                   :class=" `bg-linear-${item.status}`">{{item.statusName}}</p>
            </div>
            <div class="fast-person-wrap"
                 :style="activeStyle.personStyle">
                <el-tooltip class="item"
                            effect="dark"
                            :content="item.person"
                            placement="top-start">
                    <p class="fast-person text-ellipse">{{item.person}}</p>
                </el-tooltip>
                <!-- <p class="fast-person">{{item.person}}</p> -->
            </div>
            <div class="fast-date-wrap"
                 :style="activeStyle.dateStyle">
                <!-- <el-tooltip class="item"
                    effect="dark"
                    :content="item.date"
                    placement="top-start">
          <p class="fast-date text-ellipse">{{item.date}}</p>
        </el-tooltip> -->
                <p class="fast-date">{{item.date}}</p>
            </div>
        </section>
        <!-- </vueSeamlessScroll> -->
    </div>
    <!-- </el-scrollbar> -->

</template>

<script>
    import vueSeamlessScroll from 'vue-seamless-scroll'

    export default {
        props: ['datas', 'noticeOption'],
        components: {
            vueSeamlessScroll
        },
        data() {
            return {
                // 滚动自定义
                defaultOption: {
                    step: 0.001, // 数值越大速度滚动越快
                    limitMoveNum: 1, // 开始无缝滚动的数据量 this.dataList.length
                    hoverStop: true, // 是否开启鼠标悬停stop
                    direction: 1, // 0向下 1向上 2向左 3向右
                    openWatch: true, // 开启数据实时监控刷新dom
                    singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
                    singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
                    waitTime: 20 // 单步运动停止的时间(默认值1000ms)
                },

                // 列表默认样式
                defaultNoticeOption: {
                    orderStyle: {
                        width: '50%',
                        display: 'inline-block'
                    },
                    dateStyle: {
                        width: '50%',
                        display: 'inline-block'
                    },
                    personStyle: {
                        width: '50%',
                        display: 'none'
                    },
                }
            }
        },
        computed: {
            activeStyle() {
                let style = this.defaultNoticeOption
                if (this.noticeOption) {
                    style = {
                        orderStyle: this.noticeOption.orderStyle ? this.noticeOption.orderStyle : this.defaultNoticeOption.orderStyle,
                        dateStyle: this.noticeOption.dateStyle ? this.noticeOption.dateStyle : this.defaultNoticeOption.dateStyle,
                        personStyle: this.noticeOption.personStyle ? this.noticeOption.personStyle : this.defaultNoticeOption.personStyle,
                    }
                }

                return style
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
            padding: 5px 10px;
            background: #080831;
            font-size: $font-16;
            color: $color-base;
            &:nth-child(even) {
                background: $color-blue;
            }
        }
        .fast-order-wrap {
            .fast-status {
                display: inline-block;
                width: 32px;
                height: 16px;
                line-height: 14px;
                border-radius: 4px;
                text-align: center;
                font-size: $font-12;
                text-shadow: 2px 3px 10px #000;
            }
            .fast-name {
                padding-right: 5px;
            }
        }
        .fast-date-wrap {
            // padding-right: 15px;
            .fast-date {
                text-align: right;
            }
        }
    }
</style>
