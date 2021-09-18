<template>
    <!-- 服务台： 今日服务事件工单统计组件 -->
    <div class="content-chart"
         style="height: 170px">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>今日服务事件工单统计</div>
                </div>
            </div>
        </section>
        <div class="chart-section p10">
            <div class="order-static displaybox hp100">
                <div class="boxflex01 hp100"
                     v-for="(item, index) in staticList"
                     :class="item.class"
                     :key="item.count">
                    <div class="displaybox hp100">
                        <div class="wp100 t-center"
                             :class="{'border-right' : index !== 0 && index !== staticList.length - 1}">
                            <div>
                                <i class="icon iconfont f16"
                                   v-html="item.icon"></i>
                                <span>{{item.name}}</span>
                            </div>
                            <div class="count-num bold"
                                 :class="item.colorClass">{{staticObj[item.countKey]}}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'

    export default {
        props: {
            orderType: {
                type: String,
                default: 'all'
            }
        },
        components: {
        },
        data() {
            return {
                staticObj: {},
                staticList: [
                    {
                        name: '总数',
                        countKey: 'zs',
                        class: 'total-count white',
                        icon: '&#xe6b3;'
                    },
                    {
                        name: '待处理',
                        countKey: 'dcl',
                        colorClass: 'red',
                        icon: '&#xe6a8;'
                    },
                    {
                        name: '已关闭',
                        countKey: 'ygb',
                        icon: '&#xe6b1;'
                    },
                    {
                        name: '服务咨询',
                        countKey: 'fwzx',
                        colorClass: 'blue',
                        icon: '&#xe6b2;'
                    },
                    {
                        name: '技术支持',
                        countKey: 'jszc',
                        colorClass: 'blue',
                        icon: '&#xe6ae;'
                    },
                    {
                        name: '问题上报',
                        countKey: 'wtsb',
                        colorClass: 'blue',
                        icon: '&#xe6af;'
                    }
                ]
            }
        },
        mounted() {
            this.getHomePageStatist()
        },
        methods: {
            // 获取数据 
            getHomePageStatist() {
                this.loading = true
                rbBpmHomeServices.getHomePageStatist()
                    .then(res => {
                        this.loading = false
                        this.staticObj = res.data
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
        }

    }
</script>

<style lang="scss" scoped>
    .order-static {
        font-size: rem(16);
        .border-right {
            border-right: 1px solid $color-border;
            padding: 10px 0;
        }
        .icon {
            line-height: normal;
        }
        .total-count {
            border-radius: 4px;
            background: url("../../../../assets/img/custom_modules/bg-total-count.png")
                100% 100% no-repeat;
            background-size: cover;
        }
        .count-num {
            font-size: rem(28);
            height: rem(32);
        }
    }
</style>

