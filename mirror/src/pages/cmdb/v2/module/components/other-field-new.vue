<template>
    <div style="height: 100%;overflow: hidden">
        <el-container style="padding-top: 10px;height: 90%;">
            <!-- 左边 -->
            <el-aside class="aside"
                      width="16%"
                      v-if="parentForm.state !== 'detail'">
                <div style="padding: 5px">
                    <div class="button-box"
                         @click="addArea">
                        <el-button type="text"
                                   size="mini"><i class="el-icon-plus" />添加区域</el-button>
                    </div>
                    <el-row type="flex"
                            :offset="2"
                            justify="space-between">
                        <el-col :span="24">
                            <el-input v-model="keyword"
                                      placeholder="请输入关键字">
                            </el-input>
                        </el-col>
                    </el-row>

                    <el-collapse v-model="activeNames">
                        <el-collapse-item v-for="(item, index) in codeByGroup"
                                          :key="index"
                                          :title="item.groupName"
                                          :name="item.groupName">
                            <draggable style="cursor: move"
                                       :class="item.groupName"
                                       v-model="item.codeList"
                                       :options="dragOptions">
                                <div v-show="code.filedName.indexOf(keyword.trim())>-1"
                                     v-for="(code, index) in item.codeList"
                                     :key="index">
                                    <el-tooltip :content="getTipContent(code)">
                                        <el-row :class="{unDraggable: code.unDraggable}">
                                            {{code.filedName}}({{code.filedCode}})
                                        </el-row>
                                    </el-tooltip>
                                </div>
                            </draggable>
                        </el-collapse-item>
                    </el-collapse>
                </div>

            </el-aside>
            <!-- 左边 -->

            <div class="boder-line"></div>

            <!-- 右边 -->
            <el-main style="overflow: hidden">
                <el-container style="height: 100%">
                    <el-aside width="100%"
                              style="overflow-y: auto;height: 100%">
                        <el-form :model="form"
                                 ref="form">
                            <DragField :codeByDetails="codeByDetails"
                                       :limit="{emitEvents:false,close:false}"
                                       @emitEvents="emitEvents"
                                       :dragOptions="dragOptions3"></DragField>
                            <DragField :codeByDetails="codeByDetailsNew"
                                       :limit="{emitEvents:true,close:true}"
                                       @emitEvents="emitEvents"
                                       :dragOptions="dragOptions2"></DragField>
                        </el-form>
                    </el-aside>
                </el-container>
            </el-main>
            <!-- 右边 -->
        </el-container>
        <div style="text-align: center;">
            <el-button @click="stepTo(2,'prev')"
                       type="primary">上一步</el-button>
            <el-button @click="stepTo(2,'next')"
                       type="primary">下一步</el-button>
            <el-button @click="cancel">取消</el-button>
            <el-button @click="resetForm('form')">重置</el-button>
        </div>
    </div>
</template>

<script>
    import { mapState } from 'vuex'
    import draggable from 'vuedraggable'
    import cmdbCodeService from 'src/services/cmdb/rb-cmdb-code-services.factory'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import CommonOption from 'src/utils/commonOption.js'

    export default {
        name: 'OtherFields',
        mixins: [CommonOption],
        props: ['parentForm'],
        components: {
            draggable,
            DragField: () => import('./drag-field.vue')
        },
        data() {
            return {
                activeNames: [], // 默认打开区域名
                codeByGroup: [], // 左边:分组码表数据
                codeByDetails: [],// 右边:引用字段不可编辑(来源于引用模型moduleId !==ownerModuleId)
                codeByDetailsNew: [],// 右边:自定义字段可编辑(来源于自定义模型moduleId===ownerModuleId)
                keyword: '',// 关键字搜做
                // 左边拖拽配置项
                dragOptions: {
                    animation: 0,
                    group: {
                        name: 'code-list',
                        pull: 'clone',
                        put: false
                    },
                    sort: false,
                    filter: '.unDraggable'
                },
                // 右边拖拽配置项
                dragOptions2: {
                    animation: 0,
                    group: {
                        name: 'code-list'
                    }
                },
                dragOptions3: {
                    animation: 0,
                    group: {
                        name: 'no'
                    }
                }
            }
        },
        computed: {
            ...mapState({
                refModulesIds: (state) => {
                    return state.model.moduleObj.refModules.map((item) => {
                        return item.id
                    })
                },
                commonObj: state => state.model.commonObj,
                moduleObj: state => state.model.moduleObj
            }),
        },

        methods: {
            // 查询分组和模型字段
            getRefFields() {
                let promiseList = [];
                ['getCode', 'getModuleDetails'].forEach((item, index) => {
                    let result = null
                    let result2 = null
                    if (index === 0) {
                        result = this.getCode()
                    } else if (index === 1) {
                        switch (this.commonObj.moduleStatus) {
                            case 'add':
                                result = this.getRefFieldsByAdd()
                                break
                            case 'copy':
                                result2 = this.getModuleDetails()
                                result = this.getRefFieldsByAdd()
                                break
                            default:
                                result2 = this.getModuleDetails()
                                result = this.getRefFieldsByAdd()
                                break
                        }

                    }
                    if (result) {
                        promiseList.push(result)
                    }
                    if (result2) {
                        promiseList.push(result2)
                    }

                })
                Promise.all(promiseList).then(() => {
                    this.initUnDraggable()
                })
            },
            // 查询模型字段
            getModuleDetails() {
                let temp = this.commonObj.rowDetails.groupList.filter((item) => {
                    return item.codeList.some((code) => { return code.codeSetting.moduleId === code.codeSetting.ownerModuleId })  // moduleId和ownerModuleId是否相等
                })
                this.codeByDetailsNew = JSON.parse(JSON.stringify(temp))
            },

            // 查询引用模型字段
            getRefFieldsByAdd() {
                let promiseList = []
                this.refModulesIds.forEach((item, index) => {
                    let params = {
                        moduleId: this.refModulesIds[index]
                    }
                    let result = rbCmdbModuleService.getModuleSelfDetail(params).then((data) => {
                        let obj = {
                            'ownerIsModuleEqual': false,  // moduleId和ownerModuleId是否相等
                            'groupName': data.name,
                            'codeList': [
                            ]
                        }
                        obj.codeList = data.codeList
                        obj.ownerIsModuleEqual = obj.codeList.some((code) => { return code.codeSetting.moduleId === code.codeSetting.ownerModuleId })
                        return obj
                    })
                    promiseList.push(result)
                })
                return Promise.all(promiseList).then((res) => {
                    this.codeByDetails = res
                    // this.codeByDetailsNew = res.filter((item) => {
                    //   return item.ownerIsModuleEqual;
                    // });
                    // this.codeByDetails = res.filter((item) => {
                    //   return !(item.ownerIsModuleEqual);
                    // });
                })
            },

            // 上一步、下一步
            stepTo(number, direction) {
                if (direction === 'prev') {// 返回上一步
                    this.$emit('stepTo', { number: number, direction: direction })
                    return
                }

                // 区域名称是否存在空值
                let codeByDetailsAll = this.codeByDetailsNew.concat(this.codeByDetails)
                let nameList = codeByDetailsAll.map((item) => {
                    return item.groupName.trim()
                })
                let some = codeByDetailsAll.some((item) => {
                    return !(item.groupName) ? true : false
                })

                if (some) {// 如果存在空值
                    this.$confirm('区域名称不能为空', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                } else if (new Set(nameList).size !== nameList.length) {// 如果名称重复
                    this.$confirm('区域名称不能重复', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                } else {

                    this.$store.commit('setModuleField', { origin: this.codeByDetails, new: this.codeByDetailsNew })

                    // let groupList = this.codeByDetailsNew.map((item, index) => {
                    //     return {
                    //         groupName: item.groupName,
                    //         codeList: item.codeList
                    //     }
                    // })
                    // this.$store.commit('setModuleObj', { groupList: groupList });
                    this.$emit('stepTo', { number: number, direction: direction })
                }

            },
            // 取消
            cancel() {
                this.$emit('setShow', false)
            },

            // 重置
            resetForm() {
                this.init()
            },

            // 添加区域
            addArea() {
                let obj = {
                    // "fieldDisplay": {//字段显示隐藏
                    //   ownerModuleId: this.moduleObj.id,
                    //   codeId: '',
                    //   display: 0
                    // },
                    'groupName': '',// 区域名称
                    'codeList': [// 字段列表
                    ]
                }

                this.codeByDetailsNew.push(obj)
            },

            // 获得码表数据
            getCode() {
                this.showFullScreenLoading({ text: '正在查询数据, 请稍等...' })
                let params = {
                    catalogId: this.commonObj.topCatalogId,
                }
                return cmdbCodeService.queryCodeListFormatByGroup(params).then((data) => {
                    this.codeByGroup = data
                    this.activeNames = data.map((item) => {
                        return item.groupName
                    })
                }).catch(() => {

                    this.$notify({
                        title: '提示',
                        message: '加载模型字段失败',
                        type: 'error',
                        duration: 3000
                    })
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 初始化分组与模型选中关系
            initUnDraggable() {
                let codeByAll = this.codeByDetails.concat(this.codeByDetailsNew)
                codeByAll.forEach((item) => {
                    item.codeList.forEach(code => {
                        this.changeUnDraggable(code, true)
                    })
                })
            },
            // 分组与模型选中关系
            changeUnDraggable(code = {}, unDraggable = true) {
                this.codeByGroup.forEach((item) => {
                    item.codeList.forEach((subItem) => {
                        if (subItem.codeId === code.codeId) {
                            this.$set(subItem, 'unDraggable', unDraggable)
                        }
                    })
                })
            },
            // 拖拽回调
            onAdd(val) {
                this.codeByGroup.forEach(item => {
                    if (item.groupName === val.from.className) {
                        this.$set(item.codeList[val.oldIndex], 'unDraggable', true)
                    }
                })
            },
            // 删除区域
            handleAreaDelete(index) {
                this.$confirm('确认删除吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.codeByDetailsNew[index].codeList.forEach(item => {
                        this.changeUnDraggable(item, false)
                    })
                    this.codeByDetailsNew.splice(index, 1)
                })
            },
            // 删除字段
            handleDelete(code, areaIndex, codeIndex) {
                this.codeByDetailsNew[areaIndex].codeList.splice(codeIndex, 1)
                this.changeUnDraggable(code, false)
            },
            // 模型事件
            emitEvents(eventName, params) {
                switch (eventName) {
                    case 'onAdd':
                        this.onAdd(params[0])
                        break
                    case 'handleAreaDelete':
                        this.handleAreaDelete(params[0])
                        break
                    case 'handleDelete':
                        this.handleDelete(params[0], params[1], params[2])
                        break
                }
            },
            // 获取需要提示的内容 
            getTipContent(code) {
                return code.filedName + '(' + code.filedCode + ')'
            },
            // 初始化
            async init() {
                this.activeNames = []
                this.codeByGroup = []
                this.codeByDetails = []
                this.codeByDetailsNew = []
                this.keyword = ''

                // 查询模型字段
                this.getRefFields()
            }
        },
        mounted() {
            this.init()
        }

    }
  </script>

  <style lang="scss" scoped>
    .aside {
        .button-box {
            text-align: center;
            border: 1px solid #e4e7ed;
            line-height: 30px;
            margin-bottom: 10px;
            cursor: pointer;
        }
        /deep/ .el-collapse {
            border: 1px solid #e4e7ed;
            .el-collapse-item__header {
                padding: 0 5px;
                background-color: #f5f7fa;
                line-height: 30px;
                height: 30px;
                font-size: 14px;
            }
            .el-collapse-item__content {
                padding-bottom: 0;
            }
            .el-row {
                line-height: 30px;
                padding: 0 5px 0 15px;
                margin-bottom: 0;
                border-bottom: 1px solid #e4e7ed;
                font-size: 12px;
                display: -webkit-box;
                overflow: hidden;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 1;
            }
            .unDraggable {
                color: #c0c4cc;
            }
        }
    }

    /deep/ .el-main {
        padding: 0 0 0 10px;
    }
    .boder-line {
        width: 1px;
        height: 100%;
        margin-left: 10px;
        background-color: #e4e7ed;
    }
</style>
