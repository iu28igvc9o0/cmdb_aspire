<template>
    <el-form class="yw-form is-required"
             ref="templateForm"
             label-position="left"
             :model="listcurrentTemplateInfo"
             :rules="templateFormRules"
             label-width="100px"
             :inline="true">
        <template v-for="(v, i) in form.formItems">
            <el-form-item v-if="v.type==='input'"
                          :label="v.label"
                          :prop="v.prop"
                          :key="i">
                <el-input v-model="listcurrentTemplateInfo[v.model]"
                          :placeholder="v.placeholder"
                          :clearable="v.clearable"></el-input>
            </el-form-item>
            <el-form-item v-if="v.type==='selectTree'"
                          :label="v.label"
                          :prop="v.prop"
                          :key="i"
                          style="width:300px;">
                <select-tree @value-change="v['getvalue']"
                             :values="listcurrentTemplateInfo[v.model]"></select-tree>
            </el-form-item>
            <el-form-item v-if="v.type==='select'"
                          :label="v.label"
                          :prop="v.prop"
                          :key="i">
                <el-select v-model="listcurrentTemplateInfo[v.model]"
                           :placeholder="v.placeholder"
                           :class="v.class"
                           :clearable="v.clearable">
                    <el-option v-for="item in v.options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item v-if="v.type==='clearfix'"
                          class="clearfix"
                          :prop="v.prop"
                          :style="v.style"
                          :key="i">
                <div style="display:flex;">
                    <section class="search-right"
                             style="width:320px">
                        <div class="search-right-top">
                            <div class="name">
                                <h4>{{v.left.name}}</h4>
                            </div>
                            <div class="selectConfig">
                                <YwSearch :searchParams="v.left.searchParams"
                                          style="width:160px;"
                                          @changeSearch="v.left.changeSearch"></YwSearch>
                            </div>
                        </div>
                        <div class="yw-el-table-wrap">
                            <el-table class="yw-el-table"
                                      height="290"
                                      stripe
                                      :data="v.left.list"
                                      @selection-change="v.left.handleSelectionChange"
                                      style="width: 100%">
                                <el-table-column type="selection"
                                                 width="45"></el-table-column>
                                <el-table-column label="序号"
                                                 width="50"
                                                 type="index"
                                                 align="left"></el-table-column>
                                <el-table-column v-for="(col, i) in v.left.columns"
                                                 :show-overflow-tooltip='true'
                                                 :label="col.label"
                                                 :prop="col.prop"
                                                 :key="i"
                                                 :width="col.width"
                                                 :align="col.align">
                                    <template slot-scope="scope">
                                        <span v-if="col.formmata">{{col.formmata(scope.row)}}</span>
                                        <span v-else>{{scope.row[col.prop]}}</span>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <YwPagination @handleSizeChange="v.left.handleSizeChange"
                                          @handleCurrentChange="v.left.handleCurrentChange"
                                          :current-page="Pagination[v.name].currentPage"
                                          :page-sizes="Pagination[v.name].pageSizes"
                                          :page-size="Pagination[v.name].pageSize"
                                          layout="total, prev, pager, next, jumper"
                                          :total="Pagination[v.name].total"></YwPagination>
                        </div>
                    </section>
                    <section class="search-arrow"
                             @click="v.add"
                             style="width: 30px">
                        <i class="el-icon-right"></i>
                    </section>
                    <section class="search-right search-result"
                             style="width: 68%">
                        <div class="search-right-top">
                            <div class="name">
                                <h4>已选</h4>
                                <a class="yw-table-link"
                                   @click="v.right.empty">清空已选</a>
                            </div>
                            <div class="selectConfig">
                                <YwSearch v-if="v.right.components"
                                          :searchParams="v.right.searchParams"
                                          style="width:160px;"
                                          @changeSearch="v.right.changeSearch"></YwSearch>
                                <el-form-item v-else
                                              :label="v.right.label"
                                              :prop="v.right.prop"
                                              :rules="templateFormRules[v.right.model]">
                                    <el-input v-model="listcurrentTemplateInfo[v.right.model]"
                                              :placeholder="v.right.placeholder"
                                              :clearable="v.right.clearable"></el-input>
                                </el-form-item>
                            </div>
                        </div>
                        <div class="yw-el-table-wrap">
                            <el-table class="yw-el-table"
                                      height="290"
                                      stripe
                                      :data="v.right.list"
                                      style="width: 100%">
                                <el-table-column label="序号"
                                                 type="index"
                                                 align="left"></el-table-column>
                                <el-table-column v-for="(col, i) in v.right.columns"
                                                 :show-overflow-tooltip='true'
                                                 :label="col.label"
                                                 :prop="col.prop"
                                                 :key="i"
                                                 :width="col.width"
                                                 :align="col.align">
                                    <template slot-scope="scope">
                                        <span v-if="col.formmata">{{col.formmata(scope.row)}}</span>
                                        <el-input :disabled="col.disabled && col.disabled(scope)"
                                                  v-else-if="col.type==='input'"
                                                  v-model="v.right.list[scope.$index][col.prop]"
                                                  auto-complete="off"
                                                  class="input2"></el-input>
                                        <el-select v-else-if="col.type==='select'"
                                                   :disabled="col.disabled && col.disabled(scope)"
                                                   v-model="v.right.list[scope.$index][col.prop]"
                                                   @change="col.change(col.options,scope.$index, $event)"
                                                   @visible-change="col.visibleChange(v.right.list[scope.$index], $event)"
                                                   placeholder
                                                   :clearable="col.clearable">
                                            <el-option v-for="item in col.options"
                                                       :key="item.value"
                                                       :label="item.label"
                                                       :value="item.value"
                                                       :disabled="item.disabled"></el-option>
                                        </el-select>
                                        <div v-else-if="col.type==='indefinite'">
                                            <el-input :disabled="col.disabled && col.disabled(scope)"
                                                      v-if="scope.row.finishJudgeType  ==='aspnode_msg'"
                                                      v-model="v.right.list[scope.$index][col.prop]"
                                                      auto-complete="off"
                                                      class="input2"></el-input>
                                            <el-select :disabled="col.disabled && col.disabled(scope)"
                                                       v-else-if="scope.row.finishJudgeType  !=='aspnode_msg'"
                                                       v-model="v.right.list[scope.$index][col.prop]"
                                                       placeholder>
                                                <el-option v-for="item in col.options[scope.row.finishJudgeType]"
                                                           :key="item.value"
                                                           :label="item.label"
                                                           :value="item.value"></el-option>
                                            </el-select>
                                        </div>
                                        <span v-else>{{scope.row[col.prop]}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作"
                                                 align="left"
                                                 width="50">
                                    <template slot-scope="scope">
                                        <a class="yw-table-link"
                                           @click="v['right']['delete'](scope.row,scope.$index)">
                                            <i class="el-icon-delete"></i>
                                        </a>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </section>
                </div>
            </el-form-item>
        </template>
    </el-form>
</template>

<script>
    import { expressionList } from '../config/options.js'
    import _ from 'lodash'
    import rbAutoHealingServicesFactory from 'src/services/auto_operation/rb-auto-healing-services.factory.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import selectTree from './selectTree.vue'
    export default {
        components: {
            YwSearch: () => import('src/components/common/yw-search.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            selectTree
        },
        props: ['currentTemplateInfo'],
        data() {
            let _this = this
            // const refer_pipeline_list_validate = (rule, value, callback) => {
            // console.log(value)
            // console.log(_this.refer_pipeline_list);
            // }
            // const refer_apitem_list_validate = (rule, value, callback) => {
            // console.log(value)
            // console.log(_this.refer_apitem_list);
            // let s = false
            // for (let i = 0; i < _this.refer_apitem_list.length; i++) {
            //   let v = _this.refer_apitem_list[i]
            //   console.log(v);
            //   if (v.apItemName && v.judgeSymbol && v.judgeValue) {
            //     continue
            //   } else {
            //     // callback(new Error('请输入数字!'))
            //     break
            //   }
            // }
            //   callback()
            // }
            const multi_items_apply_time_validate = (rule, value, callback) => {
                let reg = /^([0]|[1-9][0-9]*)$/
                if (_this.refer_apitem_list.length > 1 && !value) {
                    callback(new Error('自愈指标有多个时,规则匹配时间不能为空'))
                } else if (_this.refer_apitem_list.length > 1 && !reg.test(value)) {
                    callback(new Error('规则匹配时间请输入正整数'))
                } else {
                    callback()
                }
            }
            return {
                willReplaceId: '',  // 将要删除的指标id
                form: {
                    formItems: [
                        {
                            type: 'input',
                            prop: 'scheme_name',
                            label: '模板名称',
                            model: 'scheme_name',
                            clearable: true,
                            placeholder: '请输入名称'
                        }, {
                            type: 'selectTree',
                            // type: 'input',
                            prop: 'group_relation_list',
                            label: '规则分类',
                            model: 'group_relation_list',
                            options: [],
                            clearable: true,
                            getvalue: (v) => {
                                this.$set(_this.listcurrentTemplateInfo, 'group_relation_list', v)
                            },
                            placeholder: '请选择',
                            class: 'list-sel add-con'
                        }, {
                            type: 'input',
                            prop: 'description',
                            label: '规则描述',
                            model: 'description',
                            clearable: true,
                            placeholder: '请输入描述'
                        }, {
                            type: 'clearfix',
                            name: 'one',
                            prop: 'refer_apitem_list',
                            left: {
                                name: '故障告警指标项',
                                searchParams: {
                                    keyword: '',
                                    desc: {
                                        placeholder: '请选择脚本名称',
                                        bgcolor: ''
                                    }
                                },
                                changeSearch: (val) => {
                                    _this.itemTypeList(val)
                                },
                                list: [],
                                columns: [{
                                    label: '告警指标',
                                    prop: 'apItemTypeName',
                                    width: '180',
                                    align: 'left'
                                }, {
                                    label: '指标来源',
                                    prop: 'sourceMark',
                                    width: '80',
                                    align: 'left',
                                    formmata: (row) => {
                                        return _this.filterSourceMark(row.sourceMark)
                                    }
                                }],
                                handleSelectionChange: (val) => {
                                    _this.multipleSelection1 = val
                                },
                                handleSizeChange(val) {
                                    _this.Pagination['one'].pageSize = val
                                    _this.itemTypeList()
                                },
                                handleCurrentChange(val) {
                                    _this.Pagination['one'].currentPage = val
                                    _this.itemTypeList()
                                }
                            },
                            add: () => {
                                if (_this.multipleSelection1.length) {
                                    _this.multipleSelection1.forEach(v => {
                                        _this.refer_apitem_list.push({
                                            id: '',
                                            schemeId: _this.currentTemplateInfo.scheme_id,
                                            apItemTypeId: v.id,
                                            sourceMark: v.sourceMark,
                                            apItemType: v.apItemType,
                                            apItemTypeName: v.apItemTypeName,
                                            apItem: '',
                                            apItemName: '',
                                            judgeSymbol: '',
                                            judgeValue: '',
                                            order: 1
                                        })
                                    })
                                }
                                this.$set(this.form.formItems[3]['right'], 'list', this.refer_apitem_list)
                            },
                            right: {
                                empty: () => {
                                    this.$nextTick(() => {
                                        let ids = this.currentTemplateInfo.refer_apitem_list.map(v => {
                                            return v.id
                                        })
                                        _this.refer_apitem_list.forEach(v => {
                                            if (ids.indexOf(v.id) > -1 && _this.remove_apitem_list.indexOf(v.id) < 0) {
                                                _this.remove_apitem_list.push(v.id)
                                            }
                                        })
                                        _this.refer_apitem_list = []
                                        this.$set(this.form.formItems[3]['right'], 'list', [])
                                    })
                                },
                                delete: (row, index) => {
                                    _this.refer_apitem_list.splice(index, 1)
                                    let ids = this.currentTemplateInfo.refer_apitem_list.map(v => {
                                        return v.id
                                    })
                                    if (ids.indexOf(row.id) > -1 && _this.remove_apitem_list.indexOf(row.id) < 0) {
                                        _this.remove_apitem_list.push(row.id)
                                    }
                                    this.$nextTick(() => {
                                        this.$set(this.form.formItems[3]['right'], 'list', _this.refer_apitem_list)
                                    })
                                },

                                prop: 'multi_items_apply_time',
                                label: '规则匹配时间',
                                model: 'multi_items_apply_time',
                                clearable: true,
                                placeholder: '请输入描述',

                                list: [],
                                columns: [{
                                    label: '脚本名称',
                                    prop: 'apItemTypeName',
                                    width: '160px',
                                    align: 'left'
                                }, {
                                    label: '指标来源',
                                    prop: 'sourceMark',
                                    align: 'left',
                                    formmata: (row) => {
                                        return _this.filterSourceMark(row.sourceMark)
                                    }
                                }, {
                                    label: '巡检模板',
                                    prop: 'template_name',
                                    align: 'left'
                                }, {
                                    label: '自愈指标名称',
                                    prop: 'apItemName',
                                    align: 'left',
                                    width: '140px',
                                    type: 'select',
                                    options: [],
                                    visibleChange: (v, e) => {
                                        this.willReplaceId = v.id
                                        if (e) {
                                            let obj = {
                                                itemTypeId: v.apItemTypeId
                                            }
                                            rbAutoHealingServicesFactory.queryOpsAutoRepairItemListByItemType(obj).then((res) => {
                                                if (res && res.length) {
                                                    let ids = _this.refer_apitem_list.map(v => {
                                                        return v.id
                                                    })
                                                    res.forEach(v => {
                                                        v.label = v.apItemName
                                                        v.value = v.id
                                                        if (ids.indexOf(v.id) > -1 || v.referBySchemeFlag) {
                                                            v.disabled = true
                                                        }
                                                    })
                                                    this.$nextTick(() => {
                                                        this.$set(this.form.formItems[3]['right']['columns'][3], 'options', res)
                                                    })
                                                }
                                            })
                                        }
                                    },
                                    change: (v, i, e) => {
                                        if (e) {
                                            this.remove_apitem_list.push(this.willReplaceId)    // 删除现有指标
                                            let selectData
                                            v.forEach(val => {
                                                if (val.id === e) {
                                                    selectData = val
                                                }
                                            })
                                            this.$set(_this.refer_apitem_list[i], 'id', '') // 修改指标，等于新增一个指标，删除现有指标
                                            this.$set(_this.refer_apitem_list[i], 'apItem', selectData.apItem)
                                            this.$set(_this.refer_apitem_list[i], 'apItemName', selectData.apItemName)
                                            this.$set(_this.refer_apitem_list[i], 'template_name', selectData.template_name)
                                        } else {
                                            this.$set(_this.refer_apitem_list[i], 'id', '')
                                            this.$set(_this.refer_apitem_list[i], 'apItem', '')
                                            this.$set(_this.refer_apitem_list[i], 'apItemName', '')
                                            this.$set(_this.refer_apitem_list[i], 'template_name', '')
                                        }
                                    }
                                }, {
                                    label: '判断符号',
                                    prop: 'judgeSymbol',
                                    align: 'left',
                                    type: 'select',
                                    options: expressionList,
                                    visibleChange: () => {
                                    },
                                    change: () => {
                                    }
                                }, {
                                    label: '匹配值',
                                    prop: 'judgeValue',
                                    align: 'left',
                                    type: 'input'
                                }]
                            }
                        },
                        {
                            type: 'clearfix',
                            prop: 'refer_pipeline_list',
                            style: 'margin-top:30px;',
                            name: 'two',
                            left: {
                                name: '故障自愈作业',
                                searchParams: {
                                    keyword: '',
                                    desc: {
                                        placeholder: '请选择脚本名称',
                                        bgcolor: ''
                                    }
                                },
                                changeSearch: (val) => {
                                    _this.getworkList(val)
                                },
                                list: [],
                                columns: [{
                                    label: '作业名称',
                                    prop: 'pipeline_name',
                                    width: '100',
                                    align: 'left'
                                }, {
                                    label: '步骤',
                                    prop: 'step_count',
                                    width: '100',
                                    align: 'left'
                                }],
                                handleSelectionChange: (val) => {
                                    _this.multipleSelection2 = val
                                },
                                handleSizeChange(val) {
                                    _this.Pagination['two'].pageSize = val
                                    _this.getworkList()
                                },
                                handleCurrentChange(val) {
                                    _this.Pagination['two'].currentPage = val
                                    _this.getworkList()
                                }
                            },
                            add: () => {
                                if (_this.multipleSelection2.length) {
                                    _this.multipleSelection2.forEach(v => {
                                        _this.refer_pipeline_list.push({
                                            schemeId: '',
                                            pipelineId: v.pipeline_id,
                                            pipelineName: v.pipeline_name,
                                            finishJudgeType: '',
                                            finishJudgeValue: '',
                                            finishJudgeAction: '',
                                            order: 1
                                        })
                                    })
                                }
                                this.$set(this.form.formItems[4]['right'], 'list', this.refer_pipeline_list)
                            },
                            right: {
                                empty: () => {
                                    this.$nextTick(() => {
                                        let ids = this.currentTemplateInfo.refer_pipeline_list.map(v => {
                                            return v.id
                                        })
                                        _this.refer_pipeline_list.forEach(v => {
                                            if (ids.indexOf(v.id) > -1 && _this.remove_pipeline_list.indexOf(v.id) < 0) {
                                                _this.remove_pipeline_list.push(v.id)
                                            }
                                        })
                                        _this.refer_pipeline_list = []
                                        this.$set(this.form.formItems[4]['right'], 'list', [])
                                    })
                                },
                                delete: (row, index) => {
                                    _this.refer_pipeline_list.splice(index, 1)
                                    let ids = this.currentTemplateInfo.refer_pipeline_list.map(v => {
                                        return v.id
                                    })
                                    if (ids.indexOf(row.id) > -1 && _this.remove_pipeline_list.indexOf(row.id) < 0) {
                                        _this.remove_pipeline_list.push(row.id)
                                    }
                                    this.$nextTick(() => {
                                        this.$set(this.form.formItems[4]['right'], 'list', _this.refer_pipeline_list)
                                    })
                                },
                                components: true,
                                searchParams: {
                                    keyword: '',
                                    desc: {
                                        placeholder: '请选择脚本名称',
                                        bgcolor: ''
                                    }
                                },
                                changeSearch: (val) => {
                                    _this.workList = JSON.parse(JSON.stringify(_this.refer_pipeline_list))
                                    _this.workList = _.filter(_this.refer_pipeline_list, (item) => {
                                        return item.pipelineName.includes(val)
                                    })
                                    this.$nextTick(() => {
                                        this.$set(this.form.formItems[4]['right'], 'list', _this.workList)
                                    })
                                },
                                list: [],
                                columns: [{
                                    label: '作业名称',
                                    prop: 'pipelineName',
                                    align: 'left'
                                }, {
                                    label: '返回类型',
                                    prop: 'finishJudgeType',
                                    align: 'left',
                                    width: 100,
                                    type: 'select',
                                    options: [],
                                    visibleChange: () => {
                                    },
                                    change: (v, i, e) => {
                                        if (e) {
                                            let obj = {
                                                judgeType: e
                                            }
                                            if (e === 'aspnode_msg') {
                                                this.$set(this.form.formItems[4]['right']['list'][i], 'finishJudgeValue', '')
                                            } else {
                                                rbAutoHealingServicesFactory.loadApPipeFinishJudgeDropdownList(obj).then((res) => {
                                                    let l = []
                                                    res.forEach((v) => {
                                                        l.push({
                                                            label: Object.values(v)[0],
                                                            value: Object.keys(v)[0],
                                                            key: Object.keys(v)[0]
                                                        })
                                                    })
                                                    this.$set(this.form.formItems[4]['right']['list'][i], 'finishJudgeValue', '')
                                                    this.$set(this.form.formItems[4]['right']['columns'][2]['options'], e, l)
                                                })
                                            }
                                        }
                                    },
                                    disabled: (scope) => {
                                        if (_this.refer_pipeline_list.length === scope.$index + 1) {
                                            return true
                                        } else {
                                            return false
                                        }
                                    }
                                }, {
                                    label: '返回值',
                                    prop: 'finishJudgeValue',
                                    align: 'left',
                                    width: 100,
                                    type: 'indefinite',
                                    visibleChange: () => { },
                                    change: () => { },
                                    disabled: (scope) => {
                                        if (_this.refer_pipeline_list.length === scope.$index + 1) {
                                            return true
                                        } else {
                                            return false
                                        }
                                    },
                                    options: {
                                        aspnode_result: [{
                                            label: '执行成功',
                                            value: '0',
                                            key: '0'
                                        }, {
                                            label: '执行失败',
                                            value: '1',
                                            key: '1'
                                        }],
                                        exec_status: [{
                                            label: '执行成功',
                                            value: 'success',
                                            key: 'success'
                                        }, {
                                            label: '执行失败',
                                            value: 'fail',
                                            key: 'fail'
                                        }]
                                    }
                                }, {
                                    label: '完毕动作',
                                    prop: 'finishJudgeAction',
                                    align: 'left',
                                    type: 'select',
                                    options: [],
                                    visibleChange: () => {
                                    },
                                    change: () => {
                                    },
                                    disabled: (scope) => {
                                        if (_this.refer_pipeline_list.length === scope.$index + 1) {
                                            return true
                                        } else {
                                            return false
                                        }
                                    }
                                }]
                            }
                        }]
                },
                multipleSelection1: [], // 自愈规则选中数组
                multipleSelection2: [],// 作业选中数组
                tempList: [], // 自愈过滤临时数组
                workList: [], // 作业临时数据
                refer_apitem_list: [], // 自愈规则而数组
                refer_pipeline_list: [], // 作业数组
                listcurrentTemplateInfo: {}, // 本页面编辑中的数据

                remove_apitem_list: [],// 移除关联的指标配置id
                remove_pipeline_list: [], // 移除关联的作业配置id

                Pagination: {
                    one: {
                        total: 0,
                        pageSize: 50, // 分页每页多少行数据
                        pageSizes: [10, 20, 50, 100], // 每页多少行数组
                        currentPage: 1
                    }, two: {
                        total: 0,
                        pageSize: 50, // 分页每页多少行数据
                        pageSizes: [10, 20, 50, 100], // 每页多少行数组
                        currentPage: 1
                    }
                },

                templateFormRules: {
                    scheme_name: [
                        {
                            required: true,
                            message: '请输入模板名称!',
                            trigger: 'blur'
                        },
                        {
                            min: 4,
                            max: 100,
                            message: '长度在 4 到 100 个字符!',
                            trigger: 'blur'
                        }
                    ],
                    group_relation_list: [
                        {
                            required: true,
                            message: '请选择模板分类!',
                            trigger: 'change'
                        }
                    ],
                    description: [
                        {
                            max: 200,
                            message: '最大200个字符!',
                            trigger: 'blur'
                        }
                    ],
                    multi_items_apply_time: [{
                        validator: multi_items_apply_time_validate,
                        trigger: 'blur'
                    }]
                    // refer_pipeline_list: [{
                    //   validator: refer_pipeline_list_validate
                    // }],
                    // refer_apitem_list: [{
                    //   validator: refer_apitem_list_validate
                    // }]
                }
            }
        },
        mounted() {
            this.listcurrentTemplateInfo = JSON.parse(JSON.stringify(this.currentTemplateInfo))
            this.refer_apitem_list = JSON.parse(JSON.stringify(this.currentTemplateInfo.refer_apitem_list))
            this.refer_pipeline_list = JSON.parse(JSON.stringify(this.currentTemplateInfo.refer_pipeline_list))
            this.$nextTick(() => {
                this.$set(this.form.formItems[3]['right'], 'list', this.refer_apitem_list)
                this.$set(this.form.formItems[4]['right'], 'list', this.refer_pipeline_list)
            })
            this.getActionsList()
            this.getJudgeTypeList()
            this.itemTypeList()
            this.getworkList()
        },
        methods: {
            itemTypeList(val) {
                let obj = {
                    apItemTypeNameLike: val || '',
                    page_no: this.Pagination['one'].currentPage,
                    page_size: this.Pagination['one'].pageSize
                }
                rbAutoHealingServicesFactory.queryOpsAutoRepairItemTypeList(obj).then((res) => {
                    this.Pagination['one'].total = res.totalCount
                    this.$set(this.form.formItems[3]['left'], 'list', res.dataList)
                })
            },
            getworkList(val) {
                let obj = {
                    pipeline_name_like: val || '',
                    page_no: this.Pagination['two'].currentPage,
                    page_size: this.Pagination['two'].pageSize,
                    labelId: 'autoRepair'
                }
                rbAutoOperationWorkServicesFactory.queryOpsPipelineList(obj).then((res) => {
                    this.Pagination['two'].total = res.totalCount
                    this.$set(this.form.formItems[4]['left'], 'list', res.dataList)
                })
            },
            filterSourceMark(val) {
                let text = '--'
                // xj:巡检  gj:告警
                switch (val) {
                    case 'xj':
                        text = '巡检'
                        break
                    case 'gj':
                        text = '告警'
                        break
                    default:
                        text = '--'
                        break
                }
                return text
            },
            getActionsList() {
                rbAutoHealingServicesFactory.loadApSchemePipeFinishActionList().then((res) => {
                    if (res && res.length) {
                        let l = []
                        res.forEach((v) => {
                            l.push({
                                label: Object.values(v)[0],
                                value: Number(Object.keys(v)[0]),
                                key: Object.keys(v)[0]
                            })
                        })
                        this.$nextTick(() => {
                            this.$set(this.form.formItems[4]['right']['columns'][3], 'options', l)
                        })
                    }
                })
            },
            getJudgeTypeList() {
                rbAutoHealingServicesFactory.loadApPipeFinishJudgeTypeList().then((res) => {
                    if (res && res.length) {
                        let l = []
                        res.forEach((v) => {
                            l.push({
                                label: Object.values(v)[0],
                                value: Object.keys(v)[0],
                                key: Object.keys(v)[0]
                            })
                        })
                        this.$nextTick(() => {
                            this.$set(this.form.formItems[4]['right']['columns'][1], 'options', l)
                        })
                    }
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    .choose-wrap {
        display: inline-block;
    }

    .search-arrow {
        width: 30px;
        height: 30px;
        border: 1px solid rgb(83, 96, 128);
        border-radius: 50%;
        margin: 170px 15px 0 15px;
        text-align: center;
        line-height: 30px;
        cursor: pointer;
        .el-icon-right {
            font-size: 18px;
        }
        &:hover {
            border: 1px solid #46bafe;
            .el-icon-right {
                color: #46bafe;
            }
        }
    }
    .search-right {
        width: 360px;
        border: 1px solid #dcdfe6;
    }
    .search-right-top {
        .name {
            float: left;
            padding-right: 10px;
            padding-left: 10px;
            h4 {
                color: #606266cc;
                float: left;
                padding-right: 10px;
            }
        }
        .selectConfig {
            text-align: right;
            padding-right: 10px;
        }
    }
    .clearfix {
        padding-top: 10px;
        width: 100%;
        /deep/ & > .el-form-item__content {
            width: 100%;
        }
    }
    /deep/ .el-input__inner {
        border: 1px solid #dcdfe6 !important;
        background-color: #fff !important;
        border-radius: 6px !important;
    }
    /deep/ .el-icon-search {
        top: 6px !important;
    }

    /deep/ .search-box-wrap {
        /*border: 1px solid #DCDFE6 !important;*/
        line-height: 26px;
    }
    .yw-el-table-wrap {
        /*border: 1px solid rgba(220, 223, 230, 1);*/
        /*border-radius: 8px;*/
        margin-top: 15px;
        height: 300px;
        padding: 2px;
    }

    .yw-search-wrap {
        display: inline-block;
    }
    .iconLeft .el-input__suffix {
        margin-right: 10px;
    }
</style>
