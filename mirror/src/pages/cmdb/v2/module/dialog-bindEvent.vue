<template>
    <!-- 绑定模型事件 -->
    <el-dialog class="yw-dialog"
               v-if="dialogMsg.dialogVisible"
               width="1000px"
               title="绑定模型事件"
               @close="cancel"
               :visible.sync="dialogMsg.dialogVisible">
        <section class="yw-dialog-main">
            <!-- 模型事件 -->
            <section style="margin-bottom:20px;">
                <el-tabs class="yw-tabs">
                    <el-tab-pane label="模型事件">
                    </el-tab-pane>
                </el-tabs>
                <el-form class="yw-form">
                    <div class="table-operate-wrap clearfix">
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   icon="el-icon-plus"
                                   @click="add('模型事件')">新增</el-button>
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   icon="el-icon-delete"
                                   @click="delAll('模型事件')">清空</el-button>
                    </div>
                    <div class="yw-el-table-wrap">
                        <el-table :data="tableDatas"
                                  class="yw-el-table"
                                  height="200"
                                  border>
                            <el-table-column label="序号"
                                             width="80">
                                <template slot-scope="scope">
                                    {{scope.$index+1}}
                                </template>
                            </el-table-column>
                            <el-table-column label="事件类型">
                                <template slot-scope="scope">
                                    <el-select v-model="scope.row['eventType']"
                                               :clearable="true"
                                               :filterable="true"
                                               @change="validDatas('eventType',scope,'module')"
                                               placeholder="请选择">
                                        <el-option v-for="item in eventTypeOptions"
                                                   :key="item.id"
                                                   :label="item.name"
                                                   :value="item.id">
                                        </el-option>
                                    </el-select>

                                </template>
                            </el-table-column>
                            <el-table-column label="事件类">
                                <template slot-scope="scope">
                                    <!-- 下拉框 -->
                                    <el-select v-model="scope.row['eventClass']"
                                               :clearable="true"
                                               :filterable="true"
                                               @change="validDatas('eventClass',scope,'module')"
                                               placeholder="请选择">
                                        <el-option v-for="item in eventClassOptions"
                                                   :key="item.id"
                                                   :label="item.handlerName"
                                                   :value="item.id">
                                        </el-option>
                                    </el-select>
                                    <!-- 下拉框 -->
                                </template>
                            </el-table-column>
                            <el-table-column label="排序">
                                <template slot-scope="scope">
                                    <!-- 数字框 -->
                                    <el-input-number v-model="scope.row['index']"
                                                     :min="1"
                                                     style="width:160px"
                                                     @change="(currentValue, oldValue)=>validDatas('index',scope,'module',{currentValue:currentValue,oldValue:oldValue})"
                                                     label="描述文字"></el-input-number>
                                    <!-- 数字框 -->
                                </template>
                            </el-table-column>

                            <el-table-column label="操作"
                                             align="center"
                                             width="100">
                                <template slot-scope="scope">
                                    <div class="yw-table-operator">
                                        <el-button type="text"
                                                   title="添加"
                                                   icon="el-icon-circle-plus-outline"
                                                   @click="add('模型事件')"></el-button>
                                        <el-button type="text"
                                                   title="删除"
                                                   icon="el-icon-delete"
                                                   @click="del('模型事件',scope.$index)"></el-button>
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-form>
            </section>
            <!-- 模型事件 -->

            <!-- 配置项事件 -->
            <section style="margin-bottom:20px;">
                <el-tabs class="yw-tabs">
                    <el-tab-pane label="配置项事件">
                    </el-tab-pane>
                </el-tabs>
                <el-form class="yw-form">
                    <div class="table-operate-wrap clearfix">
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   icon="el-icon-plus"
                                   @click="add('配置项事件')">新增</el-button>
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   icon="el-icon-delete"
                                   @click="delAll('配置项事件')">清空</el-button>
                    </div>
                    <div class="yw-el-table-wrap">
                        <el-table :data="tableConfigDatas"
                                  class="yw-el-table"
                                  height="200"
                                  border>
                            <el-table-column label="序号"
                                             width="80">
                                <template slot-scope="scope">
                                    {{scope.$index+1}}
                                </template>
                            </el-table-column>
                            <el-table-column label="配置项名称">
                                <template slot-scope="scope">
                                    <el-select v-model="scope.row['configName']"
                                               :loading="loading"
                                               :clearable="true"
                                               :filterable="true"
                                               @change="validDatas('configName',scope,'code')"
                                               placeholder="请选择">
                                        <el-option v-for="item in configNameOptions"
                                                   :key="item.codeId"
                                                   :label="item.filedName"
                                                   :value="item.codeId">
                                        </el-option>
                                    </el-select>

                                </template>
                            </el-table-column>
                            <el-table-column label="事件类型">
                                <template slot-scope="scope">
                                    <el-select v-model="scope.row['configType']"
                                               :clearable="true"
                                               :filterable="true"
                                               @change="validDatas('configType',scope,'code')"
                                               placeholder="请选择">
                                        <el-option v-for="item in configTypeOptions"
                                                   :key="item.id"
                                                   :label="item.name"
                                                   :value="item.id">
                                        </el-option>
                                    </el-select>

                                </template>
                            </el-table-column>
                            <el-table-column label="事件类">
                                <template slot-scope="scope">
                                    <!-- 下拉框 -->
                                    <el-select v-model="scope.row['configClass']"
                                               :clearable="true"
                                               :filterable="true"
                                               @change="validDatas('configClass',scope,'code')"
                                               placeholder="请选择">
                                        <el-option v-for="item in configClassOptions"
                                                   :key="item.id"
                                                   :label="item.handlerName"
                                                   :value="item.id">
                                        </el-option>
                                    </el-select>
                                    <!-- 下拉框 -->
                                </template>
                            </el-table-column>
                            <el-table-column label="排序">
                                <template slot-scope="scope">
                                    <!-- 数字框 -->
                                    <el-input-number v-model="scope.row['index']"
                                                     :min="0"
                                                     :max="999999999"
                                                     style="width:160px"
                                                     @change="(currentValue, oldValue)=>validDatas('index',scope,'code',{currentValue:currentValue,oldValue:oldValue})"
                                                     label="描述文字"></el-input-number>
                                    <!-- 数字框 -->
                                </template>
                            </el-table-column>

                            <el-table-column label="操作"
                                             align="center"
                                             width="100">
                                <template slot-scope="scope">
                                    <div class="yw-table-operator">
                                        <el-button type="text"
                                                   title="添加"
                                                   icon="el-icon-circle-plus-outline"
                                                   @click="add('配置项事件')"></el-button>
                                        <el-button type="text"
                                                   title="删除"
                                                   icon="el-icon-delete"
                                                   @click="del('配置项事件',scope.$index)"></el-button>
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-form>
            </section>
            <!-- 配置项事件 -->
        </section>

        <!-- 确定 -->
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="submit()">保存</el-button>
            <el-button @click="cancel()">取消</el-button>
        </section>
        <!-- 确定 -->
    </el-dialog>

</template>

<script>
    import updateComponent from 'src/utils/updateComponent.js'
    import CommonOption from 'src/utils/commonOption.js'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import rbCmdbCodeService from 'src/services/cmdb/rb-cmdb-code-services.factory'
    import rbConfigDictService from 'src/services/cmdb/rb-configDict-service.factory'

    export default {
        mixins: [updateComponent, CommonOption],
        props: ['dialogMsg'],
        components: {
        },
        data() {
            return {
                // 模型事件数据
                tableDatas: [],
                // 配置项事件数据
                tableConfigDatas: [],
                // 事件类型
                eventTypeOptions: [],
                // 事件类
                eventClassOptions: [],
                // 配置项名称(配置项)
                configNameOptions: [],
                // 事件类型（配置项）
                configTypeOptions: [],
                // 事件类（配置项）
                configClassOptions: [],
            }
        },
        methods: {
            // 数据校验(当前字段,当前对象,类型,回调函数)
            validDatas(curType, scope, compareType, callback) {

                let cur = scope.row[curType]  // 当前数据
                let curObj = scope.row
                let curOrder = scope.$index + 1
                let order = [curOrder] // 相同数据序号列表
                let compareList = [] // 比较数组
                let compareFields = [] // 比较列
                let compare = false // 比较结果

                switch (compareType) {
                    // 模型事件
                    case 'module':
                        compareList = this.tableDatas
                        compareFields = ['eventType', 'eventClass']
                        break
                    // 配置项事件
                    case 'code':
                        compareList = this.tableConfigDatas
                        compareFields = ['configName', 'configType', 'configClass']
                        break
                    default:

                        break

                }

                if (!cur || !compareList || compareList.length < 1) {
                    return false
                }

                // 比较方法
                let compareFun = (compareMessage = {}) => {
                    compare = compareList.some((item, index) => {
                        // 空和自身不判断
                        let isEmpty = true
                        if (isEmpty && (index + 1) !== curOrder) {
                            // 所有字段比较
                            let isSame = compareFields.every((compareItem) => {
                                return item[compareItem] === curObj[compareItem]
                            })
                            if (isSame) {
                                order.push(index + 1)
                                return true
                            }
                        }


                    })

                    if (compare) {
                        return this.$confirm(`序号${order.join(',')}${compareMessage.tip}`, '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {

                            if (callback && typeof callback === 'function') {
                                callback()
                            }

                        }).finally(() => {
                            scope.row[curType] = compareMessage.oldValue ? compareMessage.oldValue : ''
                        })
                    }
                }

                if (curType === 'index') {
                    // 如果是排序
                    compareFields = ['index']
                    compareFun({ tip: '排序重复，请先修改排序', oldValue: callback.oldValue })
                } else {
                    // 其他
                    compareFun({ tip: '数据重复，请先修改数据' })
                }


            },

            add(val = '') {
                switch (val) {
                    case '模型事件':
                        {
                            let orderList = this.tableDatas.map((item) => {
                                return item.index
                            })
                            let obj = {
                                eventType: '',
                                eventClass: '',
                                index: orderList && orderList.length > 0 ? Number(Math.max(...orderList)) + 1 : 0,
                            }
                            this.tableDatas.push(obj)

                            // this.validDatas('', { row: obj, $index: this.tableDatas.length - 1 }, 'module', () => {
                            //     this.tableDatas.pop()
                            // })

                        }

                        break
                    default:
                        {

                            let orderList = this.tableConfigDatas.map((item) => {
                                return item.index
                            })
                            let obj = {
                                configName: '',
                                configType: '',
                                configClass: '',
                                index: orderList && orderList.length > 0 ? Number(Math.max(...orderList)) + 1 : 0,
                            }
                            this.tableConfigDatas.push(obj)

                            // this.validDatas('', { row: obj, $index: this.tableConfigDatas.length - 1 }, 'code', () => {
                            //     this.tableConfigDatas.pop()
                            // })

                        }
                        break
                }

            },
            delAll(val = '') {
                this.$confirm('确定清空吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    switch (val) {
                        case '模型事件':
                            this.tableDatas = []
                            break
                        default:
                            this.tableConfigDatas = []
                            break
                    }
                })

            },
            del(val, index) {
                switch (val) {
                    case '模型事件':
                        this.tableDatas.splice(index, 1)
                        break
                    default:
                        this.tableConfigDatas.splice(index, 1)
                        break
                }
            },
            submit() {

                this.$confirm('确定保存吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.save()
                })

            },
            save() {

                this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                let submitEvent = this.tableDatas.map((item) => {
                    return {
                        eventClass: 'module',
                        codeId: '',
                        eventType: item.eventType,
                        handlerClassId: item.eventClass,
                        eventOrder: item.index,
                        eventPageScript: null
                    }
                })
                let submitConfig = this.tableConfigDatas.map((item) => {
                    return {
                        eventClass: 'code',
                        codeId: item.configName,
                        eventType: item.configType,
                        handlerClassId: item.configClass,
                        eventOrder: item.index,
                        eventPageScript: null
                    }
                })
                let params = {
                    moduleId: this.dialogMsg.data.id,
                }
                let data = submitEvent.concat(submitConfig)

                return rbCmdbModuleService.saveModuleEvent(params, data).then((res) => {
                    if (res.success) {
                        this.$message.success(res.message)
                        this.$emit('closeDialogBind', 'update')
                    } else {
                        this.$message.error(res.message)
                    }
                    return res

                }).finally(() => {
                    this.closeFullScreenLoading()
                })

            },
            cancel() {
                this.$emit('closeDialogBind', '')
            },
            // 获得事件类型(模型事件)
            getEventType() {
                let params = {
                    type: 'event_type_module'
                }
                return rbConfigDictService.getDictsByType(params).then((res) => {
                    this.eventTypeOptions = res
                    return res
                }).finally(() => {

                })
            },
            // 获得事件类(模型事件)
            getEventClass() {
                let params = {
                    eventClass: 'module'
                }
                return rbCmdbModuleService.getEventClass(params).then((res) => {
                    this.eventClassOptions = res
                    return res

                }).finally(() => {

                })
            },
            // 配置项名称(配置项)
            getConfigName() {
                this.showLoading()
                let params = this.dialogMsg.data.id
                return rbCmdbCodeService.getModuleCodes(params).then((res) => {
                    this.configNameOptions = res
                    return res
                }).finally(() => {
                    this.closeLoading()
                })
            },
            // 事件类型(配置项)
            getConfigType() {
                let params = {
                    type: 'event_type_code'
                }
                return rbConfigDictService.getDictsByType(params).then((res) => {
                    this.configTypeOptions = res
                    return res
                }).finally(() => {

                })
            },
            // 事件类(配置项)
            getConfigClass() {
                let params = {
                    eventClass: 'code'
                }
                return rbCmdbModuleService.getEventClass(params).then((res) => {
                    this.configClassOptions = res
                    return res

                }).finally(() => {

                })
            },
            query() {
                this.queryEvent()
                this.queryConfig()
            },
            queryEvent() {
                let params = {
                    moduleId: this.dialogMsg.data.id,
                    eventClass: 'module'
                }
                return rbCmdbModuleService.getModuleEvent(params).then((res) => {
                    this.tableDatas = res.map((item) => {
                        return {
                            eventType: item.eventType,
                            eventClass: item.handlerClassId,
                            index: item.eventOrder
                        }
                    })
                    return res

                }).finally(() => {

                })
            },
            queryConfig() {
                let params = {
                    moduleId: this.dialogMsg.data.id,
                    eventClass: 'code'
                }
                return rbCmdbModuleService.getModuleEvent(params).then((res) => {
                    this.tableConfigDatas = res.map((item) => {
                        return {
                            configName: item.codeId,
                            configType: item.eventType,
                            configClass: item.handlerClassId,
                            index: item.eventOrder
                        }
                    })
                    return res

                }).finally(() => {

                })
            },
            init() {
                this.getEventType()
                this.getEventClass()
                this.getConfigName()
                this.getConfigType()
                this.getConfigClass()
                this.query()
            },
        },
        mounted() {
            this.init()
        }
    }

</script>
<style lang="scss" scoped>
</style>
