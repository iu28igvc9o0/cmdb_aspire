<template>
    <div class="components-container">
        <!-- tab -->
        <el-tabs class="yw-tabs"
                 v-model="activeNames"
                 @tab-click="handleClick">
            <el-tab-pane label="待确认告警"
                         name="first">
                <keep-alive>
                    <alert-list v-if="activeFlagList['first'].flag"
                                :alertType="'activity'"
                                :alertLevel_default="this.alert_level"></alert-list>
                </keep-alive>
            </el-tab-pane>
            <!-- <el-tab-pane label="待观察告警"
                         name="third">
                <keep-alive>
                    <alert-list v-if="activeFlagList['third'].flag"
                                :alertType="'observe'"
                                :alertLevel_default="this.alert_level"></alert-list>
                </keep-alive>
            </el-tab-pane> -->
            <el-tab-pane label="已确认告警"
                         name="second">
                <keep-alive>
                    <alert-list v-if="activeFlagList['second'].flag"
                                :alertType="'confirm'"
                                :alertLevel_default="this.alert_level"></alert-list>
                </keep-alive>
            </el-tab-pane>
        </el-tabs>

        <div class="table-operate-wrap clearfix">
            <section class="fr">
                <div class="alertNumContent">告警总数:&nbsp;&nbsp;{{alertNum.sum}}</div>
                <div class="alertNumContent">
                    <rb-mirror-alert-num :status="5"></rb-mirror-alert-num>
                    <span class="total">{{alertNum.serious}}</span>
                </div>
                <div class="alertNumContent">
                    <rb-mirror-alert-num :status="4"></rb-mirror-alert-num>
                    <span class="total">{{alertNum.high}}</span>
                </div>
                <div class="alertNumContent">
                    <rb-mirror-alert-num :status="3"></rb-mirror-alert-num>
                    <span class="total">{{alertNum.medium}}</span>
                </div>
                <div class="alertNumContent">
                    <rb-mirror-alert-num :status="2"></rb-mirror-alert-num>
                    <span class="total">{{alertNum.low}}</span>
                </div>
                <div v-show="showFlag"
                     class="alertNumContent">{{showName}}:&nbsp;&nbsp;{{showCount}}</div>
            </section>
        </div>
        <!-- tab -->
    </div>
</template>
<script>
    import alertList from 'src/pages/mirror_alert/alert_new/list/alert-list.vue'
    import rbMirrorAlertNum from 'src/pages/mirror_alert/common/rb-mirror-alert-num.vue'
    import rbAlertServicesFactory from 'src/services/alert/rb-alert-services.factory.js'
    export default {
        name: 'MirrorAlertAlertNewManage',
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
                showName: '',
                showCount: 0,
                alarmNum: 0,
                activeNames: this.$route.query.solveStatus ? this.$route.query.solveStatus : 'first',
                alert_level: this.$route.query.alert_level,
                activeFlagList: {
                    first: {
                        flag: true,
                    },
                    second: {
                        flag: false,
                    },
                    third: {
                        flag: false,
                    },
                },
            }
        },
        mounted: function () {
            let status = this.$route.query.operation_status
            if (status && status === 1) {
                this.activeNames = 'second'
            } else if (status && status === 4) {
                this.activeNames = 'third'
            }
            this.handleClick()
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
                    if (this.activeNames === 'second') {
                        this.showCount = res.confirmed ? res.confirmed : 0
                    } else if (this.activeNames === 'third') {
                        this.showCount = res.observed ? res.observed : 0
                    }
                })
            },
            handleClick() {
                this.activeFlagList[this.activeNames].flag = true
                Object.keys(this.activeFlagList).forEach((item) => {
                    if (item !== this.activeNames) {
                        this.activeFlagList[item].flag = false
                    }
                })
                if (this.activeNames === 'first') {
                    this.alarmNum = 0
                    this.showFlag = false
                } else if (this.activeNames === 'second') {
                    this.alarmNum = 1
                    this.showFlag = true
                    this.showName = '已确认'
                } else if (this.activeNames === 'third') {
                    this.alarmNum = 4
                    this.showFlag = true
                    this.showName = '待观察'
                }
                this.getAlertSum(this.alarmNum)
            },
            alertStart() {
                if (this.$route.query.type === 'toBeConfirmed') {
                    this.activeNames = 'first'
                } else if (this.$route.query.type === 'toBeResolved') {
                    this.activeNames = 'second'
                } else if (this.$route.query.type === 'observe') {
                    this.activeNames = 'third'
                }
                this.handleClick()
            }
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
