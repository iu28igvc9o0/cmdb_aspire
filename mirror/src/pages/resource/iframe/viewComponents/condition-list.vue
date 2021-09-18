<template>
    <div>
        <div class="overview"
             v-if="this.showLength !== 0">
            <div class="box-content">
                <!-- 条件列表 -->
                <template v-for="(item, listIndex) in conditionInfo">
                    <div class="wp100 displaybox condition-row boxalignstart"
                         v-show="(listIndex + 1) <= showLength"
                         :class="{ 'half-line' : !isCascaderOrListSel(item.cmdbCode.controlType.controlCode, listIndex) 
                        || (isIdcTypeAndProjectNameAllFolded && isIdcTypeAndProjectName(item.cmdbCode.filedCode, listIndex)) }"
                         :key="item.codeId">
                        <div class="boxflex01">
                            <div class="displaybox boxalignstretch">
                                <div class="condition-type"
                                     :class="{ 'pt13' : item.cmdbCode.filedCode === 'device_mfrs' }">
                                    {{item.cmdbCode.filedName}}
                                </div>
                                <!-- 制造厂商 -->
                                <div v-if="item.cmdbCode.filedCode === 'device_mfrs'"
                                     class="boxflex01 p020 condition-logo"
                                     :class="{ 'logo-one-line' : item.isFolded }">
                                    <span class="condition-name pointer"
                                          v-for="(condition, index) in item.cmdbCode.conditionList"
                                          :class="{ 'logo-active' : item.cmdbCode.activeIndex === index || curActiveLogoIndex === index}"
                                          @click="searchByClickText(item.cmdbCode, condition, index, 'top')"
                                          @mouseover="item.cmdbCode.activeIndex = index"
                                          @mouseleave="item.cmdbCode.activeIndex = curActiveLogoIndex"
                                          :key="condition.id">
                                        <span v-if="item.cmdbCode.activeIndex === index || curActiveLogoIndex === index || !condition.produce_logo">
                                            {{ condition.produce_name || '全部'}}
                                        </span>
                                        <img v-else
                                             :src="localImgHost + condition.produce_logo">
                                    </span>
                                </div>
                                <!-- 文本类型 Cascader Or ListSel -->
                                <div v-else-if="isCascaderOrListSel(item.cmdbCode.controlType.controlCode)"
                                     class="boxflex01 p020"
                                     :class="{ 'one-line' : item.isFolded }">
                                    <!-- <div class="mright20 condition-name pointer"
                                         v-if="item.cmdbCode.filedName==='项目名称'">
                                        <el-input :placeholder="'请输入' + item.cmdbCode.filedName"
                                                  v-model="inputParams[item.cmdbCode.filedCode]">
                                            <i slot="suffix"
                                               class="el-input__icon el-icon-search"
                                               @click="searchByInput(item.cmdbCode.filedCode)"></i>
                                        </el-input>
                                    </div> -->
                                    <div>
                                        <div class="mright20 condition-name pointer"
                                             v-if="item.showInput==='1'">
                                            <el-input :placeholder="'请输入' + item.cmdbCode.filedName"
                                                      v-model="inputParams[item.cmdbCode.filedCode]">
                                                <i slot="suffix"
                                                   class="el-input__icon el-icon-search"
                                                   @click="searchServce(item)"></i>
                                            </el-input>
                                        </div>
                                        <template v-if="item.showOption==='1'">
                                            <span class="mright20 condition-name pointer"
                                                  v-for="(condition, index) in allNameList[item.cmdbCode.filedCode]"
                                                  :class="{ 'blue' : item.cmdbCode.activeIndex === index}"
                                                  @click="searchByClickText(item.cmdbCode, condition, index, 'top')"
                                                  :key="condition.id">
                                                <span>{{ condition.value}}</span>
                                            </span>
                                        </template>

                                        <!-- <span class="mright20 condition-name pointer"
                                              v-else
                                              v-for="(condition, index) in item.cmdbCode.conditionList"
                                              :class="{ 'blue' : item.cmdbCode.activeIndex === index}"
                                              @click="searchByClickText(item.cmdbCode, condition, index, 'top')"
                                              :key="condition.id">
                                            <span>{{ condition.value}}</span>
                                        </span> -->
                                    </div>
                                </div>
                                <!-- 日期控件 -->
                                <div v-else-if="item.cmdbCode.controlType.controlCode === 'dateTime'">
                                    <el-date-picker v-model="dateTimeData[item.cmdbCode.filedCode]"
                                                    class="mleft20"
                                                    type="daterange"
                                                    range-separator="至"
                                                    :start-placeholder="item.cmdbCode.filedName"
                                                    :end-placeholder="item.cmdbCode.filedName"
                                                    value-format="yyyy-MM-dd"
                                                    :clearable="true"
                                                    @clear="searchByDateTime(item.cmdbCode.filedCode)"
                                                    @change="searchByDateTime(item.cmdbCode.filedCode)">
                                    </el-date-picker>
                                </div>
                                <!-- 输入框 singleRowText/ip -->
                                <div v-else>
                                    <el-input v-if="item.cmdbCode.filedName==='IP地址'"
                                              v-model="inputParams[item.cmdbCode.filedCode]"
                                              class="mleft20"
                                              style="width:300%"
                                              clearable
                                              placeholder="请输入需要查询的IP,支持管理IP、业务网IP、IPMI IP等地址查询">
                                        <i slot="suffix"
                                           class="el-input__icon el-icon-search"
                                           @click="searchByInput(item.cmdbCode.filedCode)"></i>
                                    </el-input>
                                    <el-input v-else
                                              v-model="inputParams[item.cmdbCode.filedCode]"
                                              class="mleft20"
                                              clearable
                                              :placeholder="'请输入' + item.cmdbCode.filedName">
                                        <i slot="suffix"
                                           class="el-input__icon el-icon-search"
                                           @click="searchByInput(item.cmdbCode.filedCode)"></i>
                                    </el-input>
                                </div>
                            </div>
                        </div>
                        <div class="mleft10 mright10 row-btn"
                             v-if="isCascaderOrListSel(item.cmdbCode.controlType.controlCode)&&item.showOption==='1'">
                            <el-button @click="toggleRow(item)">
                                <span v-if='item.isFolded'>
                                    更多<i class="el-icon-arrow-down"></i>
                                </span>
                                <span v-else>
                                    收起<i class="el-icon-arrow-up"></i>
                                </span>
                            </el-button>
                        </div>
                    </div>
                </template>
            </div>
        </div>
        <div class="displaybox">
            <div class="boxflex01">
                <p v-if="this.showLength === 5 && this.conditionInfo.length > 5"
                   class="query-btn pointer"
                   @click='showMore'>
                    <i class="el-icon-arrow-down"></i>更多查询选项
                </p>
                <p v-if="this.showLength > 5 && this.conditionInfo.length > 5"
                   class="query-btn pointer"
                   @click='hideMore'>
                    <i class="el-icon-arrow-up"></i>点击收起更多查询选项
                </p>
                <p v-if="this.showLength === 0"
                   class="query-btn pointer"
                   @click='hideMore'>
                    <i class="el-icon-arrow-down"></i>点击显示查询选项
                </p>
            </div>
            <div class="query-btn-right">
                <p v-if="this.showLength === 0"
                   @click="showMore"
                   class="blue pointer">展开全部查询选项</p>
                <p v-if="this.showLength !== 0"
                   @click="hideAll"
                   class="blue pointer">隐藏全部查询选项</p>
            </div>
        </div>
        <div class="selected-list"
             v-show="selectedConditions.length">
            <div class="selected-total">共找到<span>{{total}}</span>条记录，已选</div>
            <div id="querySelector"
                 class="chosenFrame">
                <div class="selected-condition"
                     v-for="(item, index) in selectedConditions"
                     :key="index">
                    <span class="name">{{item.filedName}}：</span>
                    <span class="value">{{item.filedValue}}</span>
                    <span class="icon"><i @click="clearCondition(item.key)"
                           class="el-icon-close"></i></span>
                </div>
                <div class="selected-delete"
                     v-show="selectedConditions.length > 0">
                    <el-button @click="clearAllCondition()"
                               icon="el-icon-delete">清空选项</el-button>
                </div>
            </div>
        </div>
    </div>

</template>

<script>
    import conditionManagement from 'src/services/condition_management/query_configuration.js'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import { setObjKeys } from 'src/utils/globalUtils.js'
    export default {
        components: {},
        props: {
            total: {
                type: Number,
                default() {
                    return 0
                }
            },
            condicationCodeDefault: {
                type: String,
                default() {
                    return ''
                }
            },
            parentParams: {
                type: Object,
                default() {
                    return {}
                }
            }
        },
        data() {
            return {
                // 条件数据
                conditionInfo: [],
                // 查询参数
                queryParams: {},
                // 输入框参数，点击搜索按钮时，更新对应值到 queryParams
                inputParams: {},
                dateTimeData: {},
                // 是否折叠
                isFolded: true,
                // 默认显示条件数量
                showLength: 5,
                // 当前已选中的厂商logo
                curActiveLogoIndex: 0,
                // 已选中条件列表
                selectedConditions: [],
                // 搜索框触发事件
                timer: null,
                // 根据node当前level-1获取当前node type
                nodeTypes: ['department1', 'department2', 'bizSystem'],
                serviceNameList: [],
                allNameList: {},
                allNameLists: {}
            }
        },
        watch: {
            // 更新已选中的条件，重新搜索
            queryParams: {
                handler(newVal) {
                    let keys = Object.keys(newVal)
                    this.selectedConditions = []
                    keys.forEach(key => {
                        this.conditionInfo.filter(item => {
                            let obj = {
                                key: key
                            }
                            // 已选中条件，获取该条件对应数据
                            if (item.cmdbCode.filedCode === key && newVal[key]) {
                                obj.filedName = item.cmdbCode.filedName
                                // 多选判断
                                if (newVal[key] && newVal[key].indexOf(',') > -1) {
                                    newVal[key].split(',').forEach(val => {
                                        if (item.cmdbCode.conditionList) {
                                            let selectedCondition = item.cmdbCode.conditionList.find(condition => {
                                                return condition.id === val
                                            })
                                            console.log('selectedCondition==', selectedCondition)
                                            obj.filedValue = this.inputParams[item.cmdbCode.filedCode]
                                        } else {
                                            obj.filedValue = val
                                        }

                                    })
                                    this.selectedConditions.push(obj)

                                } else {
                                    if (item.cmdbCode.conditionList) {
                                        let selectedCondition = item.cmdbCode.conditionList.find(condition => {
                                            return condition.id === newVal[key]
                                        })
                                        obj.filedValue = selectedCondition.produce_name || selectedCondition.value
                                    } else {
                                        obj.filedValue = newVal[key]
                                    }
                                    this.selectedConditions.push(obj)
                                }
                                // 已清空该条件，值为null，重置该条件信息
                            } else if (item.cmdbCode.filedCode === key && newVal[key] === null && item.cmdbCode.conditionList) {
                                item.cmdbCode.activeIndex = 0

                                // 更新级联条件列表
                                this.searchByClickText(item.cmdbCode, {}, 0, 'top')

                                // 更新时间控件
                            } else if (item.cmdbCode.filedCode === key && !newVal[key] && item.cmdbCode.controlType.controlCode === 'dateTime') {
                                this.dateTimeData[key] = []
                            }
                        })
                    })

                    // 触发重新搜索
                    this.emitQueryByCondition()
                },
                deep: true,
                immediate: true
            },
            // 处理树节点点击事件，更新条件列表、已选中条件
            parentParams: {
                handler(newVal) {

                    this.getCondicationData()

                    // 清空选中的查询条件
                    for (let key in this.queryParams) {
                        setObjKeys(this, this.queryParams, key, '')
                    }

                    for (let key in newVal.queryToData) {
                        this.matchCurNodeCondition(key, newVal.queryToData[key])
                    }

                    // 树节点触发重新搜索(树节点属性不在queryParams属性里)
                    if (newVal.queryToData) {
                        let isExist = Object.keys(newVal.queryToData).some((item) => {
                            if (item in this.queryParams) {
                                return true
                            } else {
                                return false
                            }
                        })

                        if (newVal.queryToData['module_id']) {
                            // 如果树节点有Module_id触发重新搜索
                            this.emitQueryByCondition({ 'module_id': newVal.queryToData['module_id'] })
                        } else if (!isExist) {
                            this.emitQueryByCondition()
                        }
                    }



                },
                deep: true,
                immediate: true
            },
            conditionInfoLength: {
                handler() {
                    this.$emit('initTableHeight')
                },
                immediate: true
            },
            showLength: {
                handler(val) {
                    if (val > 5) {
                        this.$emit('initTableHeight', 160)
                    } else {
                        this.$emit('initTableHeight')
                    }
                },
                immediate: true
            },
            condicationCodeDefault: {
                handler() {
                    this.getCondicationData()
                },
                immediate: true
            },
            // 过滤业务系统
            inputParams: {
                handler(val) {
                    let keys = Object.keys(val)
                    this.allNameList = JSON.parse(JSON.stringify(this.allNameLists))
                    keys.forEach(item => {
                        if (this.allNameList[item]) {
                            this.allNameList[item] = this.allNameList[item].filter(e => {
                                return e.value.indexOf(this.inputParams[item]) > -1
                            })
                        }
                    })
                },
                immediate: true,
                deep: true
            }
            // selectedConditions: {
            //     handler() {
            //         this.$emit('initTableHeight')
            //     },
            //     immediate: true,
            //     deep: true
            // },
        },
        computed: {
            // 资源池名称和项目名称 都是折叠状态
            isIdcTypeAndProjectNameAllFolded() {
                let isFoldedNum = 2
                this.conditionInfo.forEach(item => {
                    if ((item.cmdbCode.filedCode === 'idcType' && !item.isFolded) || (item.cmdbCode.filedCode === 'project_name' && !item.isFolded)) {
                        isFoldedNum--
                    }
                })
                return isFoldedNum === 2 ? true : false
            },
            // 本地图片域名
            localImgHost() {
                return location.origin.includes('localhost') ? 'http://10.12.70.40:8080' : ''
            },
            conditionInfoLength() {
                return this.conditionInfo.length
            }
        },
        mixins: [],
        created() {
        },
        methods: {
            // 触发条件查询
            emitQueryByCondition(val = {}) {
                this.$emit('queryByCondition', Object.assign({}, this.queryParams, val))
            },
            // 处理树节点点击事件，更新条件列表、已选中条件
            handleNodeData(curNodeData) {
                if (!curNodeData.level) {
                    return
                }
                // 当前节点类型
                let curNodeType = this.nodeTypes[curNodeData.level - 1]
                // 非一级节点，递归获取上一级数据，更新到条件列表
                // 二级及以上部门
                if (curNodeData.level >= 2) {
                    // 父节点
                    let parentData = curNodeData.parent
                    // 当前节点列表
                    let curNodeList = parentData.data.subList

                    // 子节点列表
                    let childList = curNodeData.data.subList
                    // 子节点类型
                    let childType = this.nodeTypes[curNodeData.level]

                    this.conditionInfo.forEach(item => {
                        // 更新当前条件列表：父节点下的 subList
                        if (item.cmdbCode.filedCode === curNodeType) {
                            item.cmdbCode.conditionList = this.handleNodeList(curNodeList, curNodeType)
                            // 设置当前条件为高亮
                            this.highLightCurNode(item.cmdbCode, curNodeData.data.uuid)
                        }
                        // 更新当前条件子节点的条件列表：子节点 subList
                        if (item.cmdbCode.filedCode === childType) {
                            item.cmdbCode.conditionList = this.handleNodeList(childList, childType)
                        }
                    })
                    this.handleNodeData(parentData.data)

                    // 一级部门
                } else if (curNodeData.level === 1) {
                    this.matchCurNodeCondition(curNodeType, curNodeData.data.uuid)
                }
            },
            // 匹配当前类型条件
            matchCurNodeCondition(curNodeType, curNodeId) {
                this.conditionInfo.some(item => {
                    // 设置当前条件为高亮
                    if (item.cmdbCode.filedCode === curNodeType) {
                        this.highLightCurNode(item.cmdbCode, curNodeId)
                        return true
                    } else {
                        return false
                    }
                })
            },
            // 设置当前条件为高亮
            highLightCurNode(cmdbCode, curNodeId) {
                if (cmdbCode.conditionList) {
                    cmdbCode.conditionList.some((condition, index) => {
                        if (condition.id === curNodeId) {
                            this.searchByClickText(cmdbCode, condition, index, 'top')
                            return true
                        } else {
                            return false
                        }
                    })
                }

            },
            // 转换树节点列表为 顶部条件列表数据
            handleNodeList(curNodeList, curNodeType) {
                if (!curNodeList.length) {
                    return []
                }
                let arr = [
                    {
                        value: '全部',
                        type: curNodeType,
                    }
                ]
                curNodeList.forEach(sub => {
                    let obj = {}
                    obj.id = sub.uuid
                    obj.key = obj.value = sub.name
                    arr.push(obj)
                })
                return arr
            },
            // 清除条件，设置值为null，以便watch进行区分
            clearCondition(key) {
                this.queryParams[key] = null
            },
            clearAllCondition() {
                let keys = Object.keys(this.queryParams)
                keys.forEach(key => {
                    this.queryParams[key] = null
                })
            },
            // 输入框
            searchByInput(filedCode) {
                setObjKeys(this, this.queryParams, filedCode, this.inputParams[filedCode])
            },
            // 日期
            searchByDateTime(filedCode) {
                setObjKeys(this, this.queryParams, filedCode, (this.dateTimeData[filedCode] && this.dateTimeData[filedCode].toString()) || '')
            },
            // 点击条件文案
            searchByClickText(cmdbCode, condition, index, clickSource) {
                // 联级清空业务系统搜索框
                if (cmdbCode.filedName === '一级部门' || cmdbCode.filedName === '二级部门') {
                    for (let key in this.inputParams) {
                        this.inputParams[key] = ''
                    }
                }
                this.$set(cmdbCode, 'activeIndex', index)
                // cmdbCode.activeIndex = index
                if (cmdbCode.filedCode === 'device_mfrs') {
                    this.curActiveLogoIndex = index
                }

                setObjKeys(this, this.queryParams, cmdbCode.filedCode, condition.id || '')

                // 更新级联条件
                if (cmdbCode.controlType.controlCode === 'cascader') {
                    cmdbCode.cascadeList.forEach(cascadeItem => {
                        // 更新级联条件，默认查询全部
                        setObjKeys(this, this.queryParams, cascadeItem.subFiledCode, '')
                        if (clickSource !== 'top') {
                            return
                        }
                        // 点击具体选项，非全部
                        if (condition.id) {
                            // 获取级联条件内容
                            this.queryTableData({
                                sql: cascadeItem.sqlString.replace(/\?/, condition.id)
                            }, cascadeItem.subFiledCode)

                            // 点击全部，重新查询对应的条件
                        } else {
                            let curCondition = this.conditionInfo.find(item => {
                                return item.cmdbCode.filedCode === cascadeItem.subFiledCode
                            })
                            this.getConditionList(curCondition.cmdbCode)
                        }
                    })
                }
            },
            // 判断该条件是否下拉框或者级联类型
            isCascaderOrListSel(controlCode, itemIndex) {
                const bl = controlCode === 'cascader' || controlCode === 'listSel'

                if (itemIndex === undefined) {
                    return bl
                }

                const prevCondition = this.conditionInfo[itemIndex - 1]
                const prevControlCode = prevCondition && prevCondition.cmdbCode.controlType.controlCode
                // 前面的条件为：下拉或级联类型（非输入框、日期等类型）
                const isThePrevControlCode = prevControlCode === 'cascader' || prevControlCode === 'listSel'

                const nextCondition = this.conditionInfo[itemIndex + 1]
                const nextControlCode = nextCondition && nextCondition.cmdbCode.controlType.controlCode
                // 后面的条件为：下拉或级联类型（非输入框、日期等类型）
                const isTheNextControlCode = nextControlCode === 'cascader' || nextControlCode === 'listSel'

                const isOneLine = (isThePrevControlCode && isTheNextControlCode) || (isTheNextControlCode && !prevControlCode) || (isThePrevControlCode && !nextControlCode)
                let prevHalfLineNum = 0
                this.conditionInfo.forEach((item, index) => {
                    let code = item.cmdbCode.controlType.controlCode
                    if (!(code === 'cascader' || code === 'listSel') && index < itemIndex) {
                        prevHalfLineNum++
                    }
                })

                // 非下拉或级联类型，&& ((前面的条件 && 后面的条件都是下拉或级联类型)
                //  || (后面的条件是下拉或级联类型 && 没有前面条件（第一个）))，
                //  || (前面的条件是下拉或级联类型 && 没有后面条件（最后一个）))，
                // 则占据整行
                if (!bl && isOneLine) {
                    return true

                    // 非下拉或级联类型，&& (前面的条件不是下拉或级联类型 
                    // && (后面的条件是下拉或级联类型 || 没有后面条件（最后一个）)
                    // && 当前条件前面的半行条件数量总和为偶数)
                    // 则占据整行
                } else if (!bl && !isThePrevControlCode && (isTheNextControlCode || !nextControlCode) && prevHalfLineNum > 0 && (prevHalfLineNum % 2) === 0) {
                    return true

                } else {
                    return bl
                }
            },
            // 判断 'idcType' && 'project_name' 是否同时出现在相邻位置
            isIdcTypeAndProjectName(filedCode, itemIndex) {
                const bl = filedCode === 'idcType' || filedCode === 'project_name'

                const prevCondition = this.conditionInfo[itemIndex - 1]
                const prevConditionCode = prevCondition && prevCondition.cmdbCode.filedCode
                const isThePrevCode = prevConditionCode === 'idcType' || prevConditionCode === 'project_name'

                const nextCondition = this.conditionInfo[itemIndex + 1]
                const nextConditionCode = nextCondition && nextCondition.cmdbCode.filedCode
                const isTheNextCode = nextConditionCode === 'idcType' || nextConditionCode === 'project_name'

                // 同时出现在相邻位置，占据半行
                if (bl && (isTheNextCode || isThePrevCode)) {
                    return true

                    // 没有同时出现在相邻位置，占据整行 || 其他下拉框等类型条件，占据整行
                } else {
                    return false
                }
            },
            showMore() {
                this.showLength = this.conditionInfo.length
            },
            hideMore() {
                this.showLength = 5
            },
            hideAll() {
                this.showLength = 0
            },
            // 右侧更多按钮
            toggleRow(item) {
                item.isFolded = !item.isFolded
            },
            // 获取条件数据源
            getCondicationData() {
                let condication = {
                    condicationCode: this.condicationCodeDefault
                }
                conditionManagement.getCondicationGet(condication)
                    .then(res => {
                        this.conditionInfo = res.settingRelationList
                        this.conditionInfo.forEach(item => {
                            let cmdbCode = item.cmdbCode
                            // 默认折叠
                            setObjKeys(this, item, 'isFolded', true)
                            this.getConditionList(cmdbCode)
                        })

                    })
            },
            // 获取对应类型的条件列表
            getConditionList(cmdbCode) {
                let controlCode = cmdbCode.controlType.controlCode
                let isBindSource = cmdbCode.isBindSource
                let bindSourceType = cmdbCode.codeBindSource && cmdbCode.codeBindSource.bindSourceType

                // 级联下拉框 || 下拉框
                if ((controlCode === 'cascader' || controlCode === 'listSel') && isBindSource === '是') {

                    // 通过引用模型接口查询
                    if (bindSourceType === '引用模型') {
                        this.getRefModuleDict(cmdbCode)

                        // 通过数据字典接口查询
                    } else if (bindSourceType === '数据字典') {
                        this.getDictsByType(cmdbCode)

                        // 数据表 通过sql查询 制造厂商，单独接口查询 
                    } else if (bindSourceType === '数据表' && cmdbCode.filedCode === 'device_mfrs') {
                        this.getRefModuleData(cmdbCode)

                    } else if (bindSourceType === '数据表') {
                        let sqlString = cmdbCode.cascadeList.sqlString
                        // 获取对应条件列表
                        this.queryTableData({
                            sql: sqlString
                        }, cmdbCode)
                    }
                }
            },
            // 获取制造厂商列表
            getRefModuleData(cmdbCode) {
                let req = {
                    // payload: { params: { produce_type: '生产供应商' } },
                    params: {
                        params: { produce_type: '生产供应商' },
                        moduleType: 'default_produce_module_id'
                    }
                }
                rbCmdbModuleServiceFactory.getRefModuleData(req)
                    .then(res => {
                        this.handleConditionData(cmdbCode, res)
                    })
            },
            // 通过sql获取对应条件列表
            queryTableData(data, codeInfo) {
                conditionManagement.queryTableData(data)
                    .then(res => {
                        // 级联条件的subFiledCode，更新对应的条件列表
                        if (typeof codeInfo === 'string') {
                            this.conditionInfo.some(item => {
                                if (item.cmdbCode.filedCode === codeInfo) {
                                    this.handleConditionData(item.cmdbCode, res)
                                    return true
                                } else {
                                    return false
                                }
                            })
                        } else {
                            this.handleConditionData(codeInfo, res)
                        }
                    })
            },
            // 通过引用模型接口查询条件列表
            getRefModuleDict(cmdbCode) {
                // 处理引用模型类型
                let refModuleId = cmdbCode.codeBindSource.refModuleId
                let refFiledCode = cmdbCode.codeBindSource.showModuleCodeId
                let filedCode = cmdbCode.filedCode
                if (!(refModuleId && refFiledCode)) {
                    this.$message.error(filedCode + '数据源未绑定引用模型或引用模型字段')
                    return
                }
                let params = {
                    codeId: cmdbCode.codeId
                }
                rbCmdbModuleServiceFactory.getRefModuleDict({ params: params }).then((res => {
                    this.handleConditionData(cmdbCode, res)
                }))
            },
            // 通过数据字典类型接口查询条件列表
            getDictsByType(cmdbCode) {
                rbCmdbServiceFactory.getDictsByType({ 'type': cmdbCode.codeBindSource.dictSource }).then(res => {
                    this.handleConditionData(cmdbCode, res)
                })
            },
            // 处理当前类型下的初始化条件数据
            handleConditionData(cmdbCode, conditionList) {
                let option = {
                    value: '全部',
                    type: cmdbCode.filedCode,
                }
                conditionList.unshift(option)

                // 将业务对应选项添加对象里

                this.allNameLists[cmdbCode.filedCode] = conditionList
                this.allNameList = JSON.parse(JSON.stringify(this.allNameLists))

                // 设置当前类型下的条件列表
                setObjKeys(this, cmdbCode, 'conditionList', conditionList)
                // 设置当前类型下的激活条件
                // setObjKeys(this, cmdbCode, 'activeIndex', 0)
                let activeIndex = 0
                let select = ''
                this.selectedConditions.some((item) => {
                    if (item.key === cmdbCode.filedCode) {
                        select = item.filedValue
                        return true
                    } else {
                        select = ''
                        return false
                    }
                })
                // let select = this.selectedConditions.map((item) => {
                //     return item.filedValue
                // })
                conditionList.some((item, index) => {

                    if (select === item.value) {
                        activeIndex = index
                        return true
                    } else {
                        return false
                    }
                })
                setObjKeys(this, cmdbCode, 'activeIndex', activeIndex)
            },
            // 业务系统搜索
            searchServce(item) {
                let filedCode = item.cmdbCode.filedCode
                let filedName = item.cmdbCode.filedName
                if (this.allNameList[filedCode] && this.allNameList[filedCode].length > 0) {
                    let idList = ''
                    this.allNameList[filedCode].forEach(item => {
                        let obj = {
                            key: filedCode,
                            filedName: filedName
                        }
                        if (idList) {
                            idList += ',' + item.id
                        } else {
                            idList += item.id
                        }
                        obj.filedValue = item.value
                        this.selectedConditions.push(obj)
                    })
                    setObjKeys(this, this.queryParams, filedCode, idList)
                } else {
                    this.selectedConditions.push({
                        key: filedCode,
                        filedName: filedName,
                        filedValue: this.inputParams[filedCode]
                    })
                    setObjKeys(this, this.queryParams, filedCode, '1')

                    this.emitQueryByCondition()
                }
            }
        }
    }
</script>
<style lang="scss" scoped>
    @import "src/pages/resource/iframe/list/condition.scss";
</style>
