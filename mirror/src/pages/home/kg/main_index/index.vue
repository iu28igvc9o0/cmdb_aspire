<template>
    <div class="component-container"
         @click="clickFun"
         style="padding: 0px 0px 0px;">
        <commonview></commonview>
        <!-- <div style="overflow:hidden;">
            <iframe src="http://10.12.9.238:8088/previewPages/index.html#/preview/1591156960648/report"
                    width="100%"
                    height="900"
                    frameborder="0"
                    scrolling="auto">

            </iframe>
        </div> -->

    </div>
</template>
<script>
    import commonview from 'src/pages/iframe/components/common-view.vue'

    export default {
        // name: '/iframe/report/big-screen',
        components: {
            commonview,

        },
        data() {
            return {
                keepAliveIframe: '',
                pzrams: [
                    {
                        'device_class': '服务器',
                        'device_type': '云主机'
                    },
                    {
                        'device_class': '服务器',
                        'device_type': '物理机'
                    },
                    {
                        'device_class': '服务器',
                        'device_type': '宿主机'
                    },
                    {
                        'device_class': '存储',
                        'device_class_3': '磁盘阵列'
                    },
                    {
                        'device_class': '网络设备'
                    },
                    {
                        'device_class': '安全设备'
                    },
                ]
            }
        },
        created() {

        },
        methods: {
            getDay(day) {
                var today = new Date()
                var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day
                today.setTime(targetday_milliseconds)
                var tYear = today.getFullYear()
                var tMonth = today.getMonth()
                var tDate = today.getDate()
                let tMonth1 = this.doHandleMonth(tMonth + 1)
                let tDate1 = this.doHandleMonth(tDate)
                return tYear + '-' + tMonth1 + '-' + tDate1
            },
            doHandleMonth(month) {
                var m = month
                if (month.toString().length == 1) {
                    m = '0' + month
                }
                return m
            },
            formatDate(datet) {
                var date = new Date(datet)
                var YY = date.getFullYear() + '-'
                var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
                var DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
                return YY + MM + DD
            },
            clickFun(val) {
                console.log(val)
                if (val.title.url == 'cmdb') {
                    let params = val.title
                    let paramsNew = Object.assign({}, params)
                    delete paramsNew.url
                    this.$router.push({
                        path: '/resource/iframe/list',
                        query: {
                            parentParams: paramsNew,
                            condicationCode: 'instance_list'
                        }
                    })
                } else if (val.title.url == '当前告警' || val.title.two) {
                    let params = val.title
                    let num = params.two
                    let arr = []
                    var date = new Date()
                    var y = date.getFullYear()
                    var m = date.getMonth()
                    var firstDay = new Date(y, m, 1)
                    var lastDay = new Date(y, m + 1, 0)
                    for (let i in params) {
                        if (i != 'url' && i != 'two') {
                            if (params[i] == '重大') {
                                arr.push({ code: i, name: '5', type: 'in_and' })
                                arr.push({ code: 'cur_moni_time', name: this.formatDate(firstDay) + ' 00:00:00,' + this.formatDate(lastDay) + ' 23:59:59', type: 'datetime' })
                            } else if (params[i] == '高') {
                                arr.push({ code: i, name: '4', type: 'in_and' })
                                arr.push({ code: 'cur_moni_time', name: this.formatDate(firstDay) + ' 00:00:00,' + this.formatDate(lastDay) + ' 23:59:59', type: 'datetime' })
                            } else if (params[i] == '中') {
                                arr.push({ code: i, name: '3', type: 'in_and' })
                                arr.push({ code: 'cur_moni_time', name: this.formatDate(firstDay) + ' 00:00:00,' + this.formatDate(lastDay) + ' 23:59:59', type: 'datetime' })
                            } else if (params[i] == '低') {
                                arr.push({ code: i, name: '2', type: 'in_and' })
                                arr.push({ code: 'cur_moni_time', name: this.formatDate(firstDay) + ' 00:00:00,' + this.formatDate(lastDay) + ' 23:59:59', type: 'datetime' })
                            } else {
                                arr.push({ code: 'cur_moni_time', name: this.formatDate(new Date()) + ' 00:00:00,' + this.formatDate(new Date()) + ' 23:59:59', type: 'datetime' })
                                arr.push({ code: i, name: params[i], type: 'in_and' })

                            }

                            // arr.push({ code: i, name: params[i], type: 'in_and' })
                        }

                    }
                    if (Object.keys(params).length == 1) {
                        arr.push({ code: 'cur_moni_time', name: this.formatDate(new Date()) + ' 00:00:00,' + this.formatDate(new Date()) + ' 23:59:59', type: 'datetime' })
                    }

                    if (num != '2') {
                        this.$router.push({
                            path: '/mirror_alert/alert_new/manage',
                            query: {
                                solveStatus: num == '2' ? 'second' : 'first',// 处理状态'second'
                                filter: JSON.stringify(arr)
                                // filter: JSON.stringify([{ code: 'device_class', name: params.name, type: 'in_and' }])
                            }
                        })
                    } else {
                        this.$router.push({
                            path: '/mirror_alert/alert_his_new/manage',
                            query: {
                                solveStatus: num == '2' ? 'second' : 'first',// 处理状态'second'
                                filter: JSON.stringify(arr)
                                // filter: JSON.stringify([{ code: 'device_class', name: params.name, type: 'in_and' }])
                            }
                        })
                    }

                } else if (val.title.url == '当前告警1' || val.title.three) {
                    let params = val.title
                    let num = params.three
                    let arr = []
                    for (let i in params) {
                        if (i != 'url' && i != 'three') {
                            if (params[i] == '重大') {
                                arr.push({ code: 'cur_moni_time', name: this.getDay(-7) + ' 00:00:00,' + this.getDay(-1) + ' 23:59:59', type: 'datetime' })
                            } else if (params[i] == '高') {
                                arr.push({ code: 'cur_moni_time', name: this.getDay(-7) + ' 00:00:00,' + this.getDay(-1) + ' 23:59:59', type: 'datetime' })
                            } else if (params[i] == '中') {
                                arr.push({ code: 'cur_moni_time', name: this.getDay(-7) + ' 00:00:00,' + this.getDay(-1) + ' 23:59:59', type: 'datetime' })
                            } else if (params[i] == '低') {
                                arr.push({ code: 'cur_moni_time', name: this.getDay(-7) + ' 00:00:00,' + this.getDay(-1) + ' 23:59:59', type: 'datetime' })
                            } else {
                                arr.push({ code: i, name: params[i], type: 'in_and' })

                            }

                            // arr.push({ code: i, name: params[i], type: 'in_and' })
                        }

                    }

                    if (num != '2') {
                        this.$router.push({
                            path: '/mirror_alert/alert_new/manage',
                            query: {
                                solveStatus: num == '2' ? 'second' : 'first',// 处理状态'second'
                                filter: JSON.stringify(arr)
                                // filter: JSON.stringify([{ code: 'device_class', name: params.name, type: 'in_and' }])
                            }
                        })
                    } else {
                        this.$router.push({
                            path: '/mirror_alert/alert_his_new/manage',
                            query: {
                                solveStatus: num == '2' ? 'second' : 'first',// 处理状态'second'
                                filter: JSON.stringify(arr)
                                // filter: JSON.stringify([{ code: 'device_class', name: params.name, type: 'in_and' }])
                            }
                        })
                    }

                }
                else {
                    this.$router.push({
                        path: '/mirror_alert/alert_new/detail',
                        query: {
                            alertId: val.title[8],
                            detailType: 'alert',
                        }
                    })
                }
                // this.$router.push({
                //     path: '/resource/iframe/list',
                //     query: {
                //         parentParams: {
                //             'device_class': '服务器',
                //             'device_type': '云主机'
                //         },
                //         condicationCode: 'instance_list'
                //     }
                // })
                // this.$router.push({
                //     path: '/mirror_alert/alert_new/manage',
                //     query: {
                //         parentParams: {
                //             'device_class': '服务器',
                //             'device_type': '云主机'
                //         },
                //         condicationCode: 'instance_list',
                //         kgalert: 'kgalert'
                //         // filter: JSON.stringify([{ code: 'device_class', name: '服务器', type: 'in_and' }])
                //     }
                // })
            }
        },
        mounted() {
            let _this = this
            window.addEventListener('message', (e) => {
                if (e.data.title) {
                    console.log(e.data)
                    _this.clickFun(e.data)
                }
            }, false)
        }


    }
</script>
<style lang="scss" scoped>
    .iframe {
        width: 100%;
        border-width: 0;
        height: calc(100vh);
    }
</style>
