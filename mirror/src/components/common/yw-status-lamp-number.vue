<template>
    <!-- 告警状态灯+数据 -->
    <div class="status-lamp-wrap">
        <span class="status-total">{{datas.total}}</span>
        <a class="status-item"
           v-for="(item,index) in datas.list"
           :key="index">
            <i class="status-lamp"
               :class=" `bg-${item.status}`"></i>
            <span class="status-text color-black">{{item.num}}</span>
        </a>
    </div>

</template>
<script>
    export default {
        props: {
            // 数据
            datas: {
                type: [Object, Array],
                default: function () {
                    return [
                        {
                            status: 'blue',
                            num: 0
                        }
                    ]
                }
            },
        },
        data() {
            return {

            }
        },
        methods: {

        },
        mounted() {

        }

    }
</script>
<style lang="scss" scoped>
    .status-lamp-wrap {
        display: inline-block;
        .status-total {
            display: inline-block;
            vertical-align: middle;
            margin-right: 20px;
        }
        .status-item {
            display: inline-block;
            margin-right: 15px;
            &:last-of-type {
                margin-right: 0;
            }
            .status-lamp {
                display: inline-block;
                vertical-align: middle;
                width: 12px;
                height: 12px;
                border-radius: 100%;
            }
            .status-text {
                display: inline-block;
                vertical-align: middle;
                font-size: 12px;
            }
        }
    }
</style>
