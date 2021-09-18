<template>
    <!-- 全设备展示页 -->
    <div class="components-container yw-dashboard">
        <!-- 查询条件 -->
        <el-form class="yw-form components-condition"
                 label-width="75px"
                 :inline="true"
                 :model="formData">
            <el-form-item :label="labelItem.name"
                          v-for="(labelItem,labelIndex) in conditionList"
                          :key="labelIndex">
                <YwCodeFrame :frameDatas="labelItem.frameDatas"
                             v-if="labelItem.frameDatas.show"
                             :frameOptions="labelItem.frameOptions"
                             @changeSelect="changeSelect"></YwCodeFrame>
            </el-form-item>
            <el-form-item label="统计时间">
                <el-date-picker v-model="formData.date"
                                type="daterange"
                                align="right"
                                range-separator="至"
                                start-placeholder="开始时间"
                                end-placeholder="结束时间"
                                :picker-options="pickerOptions"
                                value-format="yyyy-MM-dd">
                </el-date-picker>
            </el-form-item>
            <div class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </div>
        </el-form>
        <!-- 查询条件 -->
        <!-- tab -->
        <section class="tab-wrap">
            <el-tabs class="yw-tabs"
                     v-model="activeTab"
                     @tab-click="changeTab">
                <el-tab-pane :label="item.label"
                             tabindex="-1"
                             :name="item.name"
                             v-for="(item,index) in tabData"
                             :key="index">
                </el-tab-pane>
            </el-tabs>

        </section>
        <!-- tab -->

        <!-- chart -->
        <section class="chart-wrap">
            <div v-for="(item,index) in filter"
                 :key="index">
                <component :is="currentTabComponent"
                           style="width:100%;height:400px;"
                           v-if="resetComponent"
                           :filterData="item"
                           :moduleData="moduleData"></component>
            </div>

        </section>

        <!-- chart -->
    </div>

</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import { formatDate2 } from 'src/assets/js/utility/rb-filters.js'

    export default {
        name: 'DeviceList',
        mixins: [CommonOption, YwCodeFrameOption],
        components: {
            YwCodeFrame: () => import('src/components/common/yw-code-frame/yw-code-frame.vue'),
            Trend: () => import('./trend.vue'),
        },
        data() {
            return {
                // 日期
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date()
                            const start = new Date()
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
                            picker.$emit('pick', [start, end])
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date()
                            const start = new Date()
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
                            picker.$emit('pick', [start, end])
                        }
                    }, {
                        text: '最近13个月',
                        onClick(picker) {
                            const end = new Date()
                            const start = new Date()
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30 * 13)
                            picker.$emit('pick', [start, end])
                        }
                    }]
                },
                // 表单数据
                formData: {
                    date: ''
                },
                // 表单字段
                conditionList: [
                    {
                        key: 'idcType',
                        name: '资源池',
                        frameDatas: {
                            show: true,
                            // 当前选中值
                            select: '',
                            // 当前code对象
                            codeObj: { filedCode: 'idcType' },// 暂时写死，后面看cmdb后端字段优化
                            // 父级code对象
                            parentCode: '',
                            // 父级选中的值
                            parentSelect: '',
                            // 级联的子级key
                            cascadeList: [],
                        },
                        frameOptions: {
                            type: 'select',
                        }
                    },
                ],

                // tab
                activeTab: 'cpu',
                activeTabObj: { label: 'CPU', data: '(0)', name: 'cpu', componentPage: 'Trend' },
                tabData: [
                    { label: 'CPU', data: '(0)', name: 'cpu', componentPage: 'Trend' },
                    { label: '内存', data: '(0)', name: 'memory', componentPage: 'Trend' },
                ],
                // filter
                filter: [{ name: '裸金属', label: 'X86服务器' }, { name: '云主机', label: '云主机' }],
                activeFilter: 'X86服务器',

                // 内容
                currentTabComponent: 'Trend',
                moduleData: {
                    conditionParams: {},// 查询条件
                    tabParams: {},// tab
                },


            }
        },

        methods: {

            // tab切换
            changeTab(tab) {
                this.tabData.some((item) => {
                    if (item.name === tab.name) {
                        this.activeTabObj = item
                        return true
                    } else {
                        return false
                    }
                })

                this.query()
            },

            // 查询结果
            query() {
                if (!this.formData.date) {
                    this.$confirm('统计时间不能为空', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                    return false
                }
                // 更新参数
                this.moduleData.conditionParams = {
                    'idcType': this.getSelectValueByKey('idcType', 'value'),
                    'date': this.formData.date
                }

                this.moduleData.tabParams = this.activeTabObj


                // 更新组件
                this.currentTabComponent = this.activeTabObj.componentPage
                this.updateComponent()
            },


            // 重置
            reset() {
                this.resetCondition()
                this.formData.date = ''
            },
            // 初始化
            async init() {
                let now = new Date()
                let before = now.getTime() - 1000 * 60 * 60 * 24 * 30 * 13
                this.formData.date = [formatDate2(before), formatDate2(now)]
                this.query()
            }

        },
        created() {
            this.init()
        },
    }
</script>

<style lang="scss" scoped>
    .components-condition {
        padding-right: 225px;
    }
    .tab-wrap {
        position: relative;
        .status-wrap {
            position: absolute;
            bottom: 10px;
            right: 10px;
        }
    }
</style>
