<template>
    <div class="components-container yw-dashboard">
        <el-tabs class="yw-tabs"
                 v-model="activeName"
                 @tab-click="handleClick">
            <template v-for="(item,index) in tabData">
                <el-tab-pane :key="index"
                             :label="item.label"
                             :name="item.name"
                             v-loading="loadingInfo.loading"
                             v-if="item.isShow">
                </el-tab-pane>
            </template>
        </el-tabs>

        <component :is="currentTabComponent"
                   :deviceInfo="deviceInfo"
                   @showOtherTab="showOtherTab"></component>
    </div>
</template>

<script>
    import rbConfigServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'
    export default {
        name: 'ResourceIframeDetail',
        components: {
            instanceDetail: () => import('src/pages/resource/iframe/add/instance-add.vue'),
            changeInfo: () => import('src/pages/resource/iframe/detail/changeInfo.vue'),
            instancePortRelation: () => import('src/pages/resource/iframe/detail/portRelation.vue'),
            resourceRelation: () => import('src/pages/resource/iframe/detail/resourceRelation.vue'),
            alertProgram: () => import('src/pages/resource/iframe/detail/alertProgram.vue'),
            networkCard: () => import('src/pages/resource/iframe/detail/networkCard.vue'),
            approvalInfo: () => import('src/pages/resource/iframe/detail/approval.vue'),
            deviceTopo: () => import('src/pages/resource/iframe/detail/deviceTopo/index.vue'),
            instanceFile: () => import('src/pages/resource/iframe/detail/fileManage/instanceFile.vue'),
        },
        data() {
            return {
                // 触发刷新
                triggerQuery: '',
                queryParams: JSON.parse(this.$route.query.queryParams),
                loadingInfo: {
                    loading: false
                },
                activeName: 'instanceDetail',
                nameList: [],
                name: JSON.parse(this.$route.query.queryParams).name,
                tabData: [
                    { label: '基本属性信息', name: 'instanceDetail', index: 0, isShow: JSON.parse(this.$route.query.queryParams).from !== 'os' },
                    { label: '变更流程管理', name: 'changeInfo', index: 1, isShow: JSON.parse(this.$route.query.queryParams).from !== 'os' },
                    { label: '资源端口管理', name: 'instancePortRelation', index: 2, isShow: JSON.parse(this.$route.query.queryParams).from !== 'os' },
                    { label: '资源关系管理', name: 'resourceRelation', index: 2, isShow: JSON.parse(this.$route.query.queryParams).from !== 'os' },
                    { label: '告警管理信息', name: 'alertProgram', index: 3, isShow: JSON.parse(this.$route.query.queryParams).from !== 'os' },
                    { label: '配置项变更记录', name: 'approvalInfo', index: 4, isShow: true },
                    { label: '设备拓扑图', name: 'deviceTopo', index: 4, isShow: JSON.parse(this.$route.query.queryParams).from !== 'os' },
                    { label: '附件管理', name: 'instanceFile', index: 5, isShow: JSON.parse(this.$route.query.queryParams).from !== 'os' }
                ],
                currentTabComponent: 'instanceDetail',
                otherTabs: ['网卡信息', '端口信息', '相关信息管理'],
                deviceInfo: {}
            }
        },
        mounted() {
            this.getDeviceTypeWithDisplay()
        },
        methods: {
            // 根据实况显示对应的tab页
            showOtherTab(val) {
                var flag = false
                var list = this.nameList
                var name = val
                for (var i = 0; i < list.length; i++) {
                    if (name === list[i].value) {
                        flag = true
                        break
                    }
                }
                for (let i = 0; i < this.tabData.length; i++) {
                    if (this.otherTabs.indexOf(this.tabData[i].label) > -1) {
                        flag = false
                    }
                }
                if (flag) {
                    if (name === 'X86服务器') {
                        this.tabData.push({ label: '网卡信息', name: 'networkCard', index: 5, isShow: this.queryParams.from !== 'os' })
                    } else if (name === '云主机') {
                        this.tabData.push({ label: '端口信息', name: 'networkCard', index: 5, isShow: this.queryParams.from !== 'os' })
                    } else {
                        this.tabData.push({ label: '相关信息管理', name: 'networkCard', index: 5, isShow: this.queryParams.from !== 'os' })
                    }
                }

            },
            // showCurrentTab(){
            //   var flag = false
            //   var list = this.nameList
            //   var name = this.name
            //   for(var i=0;i<list.length;i++) {
            //     if(name === list[i].value) {
            //       flag = true
            //       break
            //     }
            //   }
            //   if(flag) {
            //     if(name === 'X86服务器') {
            //       this.tabData.push({ label: '网卡信息', name: 'networkCard', index: 5,isShow: this.queryParams.from !== 'os' })
            //     } else if(name === '云主机') {
            //       this.tabData.push({ label: '端口信息', name: 'networkCard', index: 5,isShow: this.queryParams.from !== 'os' })
            //     } else {
            //       this.tabData.push({ label: '相关信息管理', name: 'networkCard', index: 5,isShow: this.queryParams.from !== 'os' })
            //     }
            //   }
            // },
            // 获取tab要显示的设备类型
            getDeviceTypeWithDisplay() {
                this.currentTabComponent = this.queryParams.from === 'os' ? 'approvalInfo' : 'instanceDetail'
                var params = {
                    type: 'device_type_display'
                }
                rbConfigServiceFactory.getDictsByType(params).then((res) => {
                    this.nameList = res
                    // this.showCurrentTab()
                })
            },
            handleClick(tab) {
                this.currentTabComponent = tab.name
                this.activeIndex = tab.index
            },
            // 上一步
            goPrev() {
                if (this.activeIndex > 0) {
                    --this.activeIndex
                    this.activeName = this.tabData[this.activeIndex].name
                    this.currentTabComponent = this.tabData[this.activeIndex].name
                }
            },

            // 下一步
            goNext() {
                if (this.activeIndex < this.tabData.length - 1) {
                    ++this.activeIndex
                    this.activeName = this.tabData[this.activeIndex].name
                    this.currentTabComponent = this.tabData[this.activeIndex].name
                }
            },
        }
    }
</script>

<style scoped>
</style>
