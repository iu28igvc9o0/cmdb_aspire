<template>
    <div class="head-info">
        <div class="info-sigal">
            <span class="font-bold">已规划：</span>
            <span>{{tabipInfo.totalCount}}</span>
        </div>
        <div class="info-sigal">
            <span class="font-bold">已分配：</span>
            <span>{{tabipInfo.assignCount}}</span>
        </div>
        <div class="info-sigal">
            <span class="font-bold">分配率：</span>
            <span>{{tabipInfo.assignPercent}}</span>
        </div>
        <div class="info-sigal">
            <span class="font-bold">已使用：</span>
            <span>{{tabipInfo.useCount}}</span>
        </div>
        <div class="info-sigal">
            <span class="font-bold">使用率：</span>
            <span>{{tabipInfo.usePercent}}</span>
        </div>
    </div>
</template>
<script>
    export default {
        props: ['segmentAddr', 'ipType'],
        data() {
            return {
                tabipInfo: {
                    totalCount: '--',
                    assignCount: '--',
                    assignPercent: '--',
                    useCount: '--',
                    usePercent: '--'
                }
            }
        },
        methods: {
            // ip地址接口
            ipGetRes(resdata) {
                console.log(resdata, 'resdataresdata')
                let params = {
                    ip_type: this.ipType,// 'inner_ip',
                    segment_addr: this.segmentAddr,
                    idc_type: '',
                    conditions: {}
                }
                if (resdata) {
                    for (let prop in resdata) {
                        params['conditions'][prop] = resdata[prop]
                    }
                }
                this.rbHttp.sendRequest({
                    method: 'POST',
                    params: params,
                    url: '/v1/cmdb/cmic/ip/statisticsIpUseInfo'
                }).then((res) => {
                    if (res.flag == 'sucess') {
                        this.tabipInfo.totalCount = res.data.totalCount
                        this.tabipInfo.assignCount = res.data.assignCount
                        this.tabipInfo.assignPercent = res.data.assignPercent
                        this.tabipInfo.useCount = res.data.useCount
                        this.tabipInfo.usePercent = res.data.usePercent
                    }
                })
            }
        },
        created() {
            this.ipGetRes()
        }
    }
</script>
<style lang="scss" scoped>
    .head-info {
        background: #fff;
        box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
        border-radius: 6px;
        padding: 10px;
        margin-top: 10px;
        margin-right: 10px;
        display: flex;
        justify-content: space-around;
        .font-bold {
            font-weight: bold;
        }
    }
</style>