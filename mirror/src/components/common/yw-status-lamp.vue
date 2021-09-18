<template>
    <!-- 告警状态灯 -->
    <div class="status-lamp-wrap">
        <a class="status-item"
           @click="goto(0, item.status)"
           v-for="(item,index) in datas"
           :key="index">
            <span class="status-lamp"
                  :class=" `bg-${item.status}`"></span>
            <span class="status-text text-ellipse"
                  :class=" `color-${item.status}`">{{item.num}}</span>
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
        computed: {

        },
        methods: {
            goto(status, level) {
                let levelVal = 0
                if (level) {
                    if (level === 'red') {
                        levelVal = 5
                    } else if (level === 'orange') {
                        levelVal = 4
                    } else if (level === 'yellow') {
                        levelVal = 3
                    } else {
                        levelVal = 2
                    }
                }
                let path = '/mirror_alert/alert/manage'
                if (status === 3 || status === '3') {
                    path = '/mirror_alert/alert_his/his_alert'
                    status = 3
                }
                this.$router.push({
                    path: path,
                    query: {
                        alertType: 'main',
                        operation_status: status,
                        alert_level: level === '' ? '' : levelVal
                    }
                })
            },
        },
        mounted() {

        }

    }
</script>
<style lang="scss" scoped>
    .status-lamp-wrap {
        display: flex;
        width: 100%;
        justify-content: space-around;
        align-items: center;
        .status-item {
            display: inline-block;
            width: 24%;
            .status-lamp {
                display: inline-block;
                vertical-align: middle;
                width: 8px;
                height: 8px;
                border-radius: 100%;
            }
            .status-text {
                width: calc(100% - 15px);
                font-size: $font-14;
            }
        }
    }
</style>
