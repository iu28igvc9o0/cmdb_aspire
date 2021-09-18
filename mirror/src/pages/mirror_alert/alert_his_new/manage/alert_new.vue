<template>
    <div class="components-container">
        <alert-list :alertLevel_default="this.alert_level"></alert-list>

    </div>
</template>
<script>
    import alertList from 'src/pages/mirror_alert/alert_his_new/list/alert-list.vue'
    import rbMirrorAlertNum from 'src/pages/mirror_alert/common/rb-mirror-alert-num.vue'
    import rbAlertServicesFactory from 'src/services/alert/rb-alert-services.factory.js'
    export default {
        name: 'MirrorAlertAlertHisNewManage',
        mixins: [],
        components: {
            alertList,
            rbMirrorAlertNum
        },
        props: [
        ],
        data() {
            return {
                alertNum: {
                    low: 0,
                    medium: 0,
                    high: 0,
                    serious: 0,
                    sum: 0,
                    confirmCount: 0
                },
                showFlag: false,
                alarmNum: 0,
                activeNames: 'first',
                activity: true,
                confirm: false,
                alert_level: this.$route.query.alert_level
            }
        },
        mounted: function () {
            let status = this.$route.query.operation_status
            if (status && status === 1) {
                this.activeNames = 'second'
                this.activity = false
                this.confirm = true
            }
            this.getAlertSum(this.alarmNum)
        },
        methods: {

            getAlertSum(code) {
                rbAlertServicesFactory.getAlertCount(code).then(res => {
                    this.alertNum.sum = res.summary
                    this.alertNum.serious = res.serious
                    this.alertNum.high = res.high
                    this.alertNum.medium = res.medium
                    this.alertNum.low = res.low
                    this.alertNum.tip = res.tip
                    this.alertNum.confirmCount = res.confirmed
                })
            },
        },
        watch: {
        }
    }
</script>
<style scoped lang="scss">
    @import "../list/alert.scss";
    .confirmCount {
        position: relative;
    }
    .table-operate-wrap {
        position: absolute;
        top: 15px;
        right: 15px;
    }
</style>
