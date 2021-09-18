<!--  -->
<template>
    <div class="components-container yw-dashboard">
        <el-form class="components-condition yw-form"
                 :model="formSearch"
                 label-position="left"
                 :inline="true"
                 ref="formSearch"
                 label-width="75px">

            <!-- <el-form-item label="资源池名称"
                          prop="idcType"
                          label-width="75px">
                <el-select v-model="formSearch.idcType"
                           placeholder="请选择"
                           clearable>
                    <el-option v-for="item in options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="POD名称"
                          prop="pod_name"
                          label-width="75px">
                <el-select v-model="formSearch.pod_name"
                           placeholder="请选择"
                           clearable>
                    <el-option v-for="item in options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="机房位置"
                          prop="roomId"
                          label-width="75px">
                <el-select v-model="formSearch.roomId"
                           placeholder="请选择"
                           clearable>
                    <el-option v-for="item in options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="一级部门"
                          prop="department1"
                          label-width="75px">
                <el-select v-model="formSearch.department1"
                           placeholder="请选择"
                           clearable>
                    <el-option v-for="item in options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="二级部门"
                          prop="department2"
                          label-width="75px">
                <el-select v-model="formSearch.department2"
                           placeholder="请选择"
                           clearable>
                    <el-option v-for="item in options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="业务系统"
                          prop="bizSystem"
                          label-width="75px">
                <el-select v-model="formSearch.bizSystem"
                           placeholder="请选择"
                           clearable>
                    <el-option v-for="item in options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="设备类型"
                          prop="device_type"
                          label-width="75px">
                <el-select v-model="formSearch.device_type"
                           placeholder="请选择"
                           clearable>
                    <el-option v-for="item in deviceOptions"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item> 
                        <el-form-item label="列头柜名称"
                          prop="name"
                          label-width="75px">
                <el-input v-model="formSearch.name"
                          placeholder="请输入列头柜名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="管理IP地址"
                          prop="ip"
                          label-width="75px">
                <el-input v-model="formSearch.ip"
                          placeholder="请输入IP"
                          clearable></el-input>
            </el-form-item>
-->
            <el-form-item :label="labelItem.name"
                          v-for="(labelItem,labelIndex) in conditionList"
                          :key="labelIndex">
                <YwCodeFrame :frameDatas="labelItem.frameDatas"
                             v-if="labelItem.frameDatas.show"
                             :frameOptions="labelItem.frameOptions"
                             @changeSelect="changeSelect"></YwCodeFrame>
            </el-form-item>
            <el-form-item label="列头柜名称"
                          prop="name"
                          label-width="75px">
                <el-input v-model="formSearch.name"
                          clearable></el-input>
            </el-form-item>

            <section class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>

        </el-form>
        <section class="tab-wrap">
            <el-tabs class="yw-tabs"
                     v-model="activeName"
                     @tab-click="handleClick">
                <el-tab-pane :label="item.label"
                             tabindex="-1"
                             :name="item.name"
                             v-for="(item,index) in tabData"
                             :key="index">
                </el-tab-pane>
            </el-tabs>

            <!-- <el-tabs v-model="activeName"
                     @tab-click="handleClick">
                <el-tab-pane label="物理机"
                             name="first">
                    <tableCom :moduleData="moduleData"
                              v-if="resetComponent"></tableCom>
                </el-tab-pane>
                <el-tab-pane label="云主机"
                             name="second">
                    <tableCom :moduleData="moduleData"
                              v-if="resetComponent"></tableCom>
                </el-tab-pane>
                <el-tab-pane label="网络及其他"
                             name="third">
                    <tableCom :moduleData="moduleData"
                              v-if="resetComponent"></tableCom>
                </el-tab-pane>
            </el-tabs> -->
        </section>
        <component :is="currentTabComponent"
                   v-if="resetComponent"
                   :moduleData="moduleData"></component>

    </div>
</template>

<script>
    import tableCom from './tableCom.vue'
    import YwCodeFrame from 'src/components/common/yw-code-frame/yw-code-frame.vue'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import CommonOption from 'src/utils/commonOption.js'


    export default {
        data() {
            return {
                activeName: 'first',
                formSearch: {
                    idcType: '',
                    pod_name: '',
                    name: '',
                    department1: '',
                    department2: '',
                    bizSystem: '',
                    ip: '',
                    device_type: ''
                },
                tabData: [{ label: '物理机', data: 'alert_phy', name: 'first', componentPage: 'tableCom' },
                { label: '云主机', data: 'alert_vm', name: 'second', componentPage: 'tableCom' },
                { label: '网络及其他', data: 'alert_other', name: 'third', componentPage: 'tableCom' },
                ],
                currentTabComponent: 'tableCom',
                deviceOptions: [],
                moduleData: {
                },
                condicationCodeVal: 'alert_phy'
            }
        },
        components: {
            tableCom,
            YwCodeFrame
            // YwCodeFrame: () => import('src/components/common/yw-code-frame/yw-code-frame.vue'),
        },
        mixins: [YwCodeFrameOption, CommonOption],
        methods: {
            // 初始化
            init() {
                // 查询级联下拉框字段
                this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
            },
            query() {
                // 更新参数
                this.moduleData = {
                    'idcType': this.getSelectValueByKey('idcType'),
                    'pod_name': this.getSelectValueByKey('pod_name'),
                    'roomId': this.getSelectValueByKey('roomId'),
                    'device_type': this.getSelectValueByKey('device_type'),
                    'department1': this.getSelectValueByKey('department1'),
                    'department2': this.getSelectValueByKey('department2'),
                    'bizSystem': this.getSelectValueByKey('bizSystem'),
                    'ip': this.getSelectValueByKey('ip'),
                    'condicationCode': this.condicationCodeVal,
                    'idc_cabinet': this.formSearch.name
                }
                this.updateComponent()
            },
            handleClick(tab, event) {
                switch (tab.name) {
                    case 'first': {
                        this.condicationCodeVal = 'alert_phy'
                        break
                    }
                    case 'second': {
                        this.condicationCodeVal = 'alert_vm'
                        break
                    }
                    case 'third': {
                        this.condicationCodeVal = 'alert_other'
                        break
                    }
                }
                this.query()
            },
            reset() {
                this.formSearch.name = ''
                this.conditionList.forEach((item) => {
                    item.frameDatas.select = ''
                    item.frameDatas.parentCode = ''
                    item.frameDatas.parentSelect = ''
                    this.freshCodeFrame(item)
                })
                this.query()
            },

        },
        created() {
            this.init()
            this.query()
        },
        watch: {
        }
    }
</script>
<style lang='scss' scoped>
</style>