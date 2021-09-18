<template>
    <el-dialog class="yw-dialog"
               :title="title"
               :visible.sync="show"
               @close="resetDialog"
               width="700px">
        <section class="yw-dialog-main">
            <el-form ref="rulesFrom"
                     :model="queryList"
                     class="yw-form is-required"
                     :inline="true"
                     label-position="right"
                     label-width="100px"
                     :rules="rules">
                <el-row class="diaTitle">字段信息</el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item prop="moduleCatalogId"
                                      label="模型分组">
                            <el-select v-model="queryList.moduleCatalogId"
                                       placeholder="请选择字段分组"
                                       filterable
                                       :clearable="codeId === ''">
                                <el-option v-for="item in ControlTypesList"
                                           :key="item.value"
                                           :label="item.value"
                                           :value="item.label"
                                           @click.native="modelFieldClick(item.label)"
                                           @change="modelFieldClick(item.label)"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="codeLength"
                                      label="字段长度">
                            <el-input-number style="width:193px"
                                             v-model="queryList.codeLength"
                                             :min="1"
                                             :max="4000"></el-input-number>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item prop="filedCode"
                                      label="码表编码">
                            <el-input :clearable="codeId === ''"
                                      v-model="queryList.filedCode"
                                      :disabled="codeId !== ''"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <!-- :disabled="codeId !== ''" -->
                        <el-form-item prop="filedName"
                                      label="码表名称">
                            <el-input :clearable="codeId === ''"
                                      v-model="queryList.filedName"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item prop="controlTypeId"
                                      label="控件类型">
                            <el-select v-model="queryList.controlTypeId"
                                       placeholder="请选择控件类型"
                                       filterable
                                       :clearable="codeId === ''"
                                       @change="changeControlType">
                                <el-option v-for="item in controlTypeIdList"
                                           :key="item.value"
                                           :label="item.value"
                                           :value="item.label"
                                           @click.native="getTypeId(item.value)"
                                           @change="getTypeIdS()"></el-option>

                                <!-- @change="getTypeId(item.value)" -->
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item v-if="dataSource"
                                      prop="bindSourceType"
                                      label="数据源">
                            <el-select v-model="queryList.codeBindSource.bindSourceType"
                                       placeholder="请选择控数据源"
                                       filterable
                                       clearable
                                       @clear="getDataSourceclear">
                                <el-option v-for="item in dataSourceList"
                                           :key="item.value"
                                           :label="item.value"
                                           :value="item.value"
                                           @click.native="getDataSource(item.value)"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <!-- 控件类型表格牵扯的  框框 -->
                <el-row v-show="TABLE"
                        v-for="(column, idxTable) in bindColumnsTable"
                        :key="idxTable">
                    <el-col :span="12">
                        <el-form-item prop="colTitle"
                                      label="表格列名">
                            <el-input :clearable="codeId === ''"
                                      @change="tablecodeNameClick(bindColumnsTable[idxTable].colTitle,idx)"
                                      v-model="bindColumnsTable[idxTable]['colTitle']">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="colKey"
                                      label="映射Key值">
                            <el-input :clearable="codeId === ''"
                                      @change="tablesqLClick(bindColumnsTable[idxTable].colKey,idx)"
                                      v-model="bindColumnsTable[idxTable]['colKey']"
                                      style="width: 160px !important">
                            </el-input>
                            <i class="el-icon-circle-plus-outline"
                               v-show="showPlusTable(idxTable)"
                               style="color: #409eff; font-size:15px;cursor: pointer;"
                               @click="plusTable(idxTable)"></i>
                            <i class="el-icon-remove-outline"
                               v-show="showMouseTable(idxTable)"
                               style="color: #409eff; font-size:15px;cursor: pointer;"
                               @click="mouseTable(idxTable)"></i>
                        </el-form-item>
                    </el-col>
                </el-row>
                <!-- 控件类型下拉框及联——数据字典 -->
                <el-row v-if="dataDictionary">
                    <el-col :span="12">
                        <el-form-item prop="codeBindSource.dictSource"
                                      label="字典类型"
                                      :rules="{required: true, message: '不能为空', trigger: 'change'}">
                            <el-select v-model="queryList.codeBindSource.dictSource"
                                       placeholder="请选字典类型"
                                       filterable
                                       :clearable="codeId === ''">
                                <!-- :label="item.col_name" :value="item.description"  -->
                                <el-option class="dictSourceClass"
                                           v-for="(item, index) in dictList"
                                           :key="index"
                                           :label="item.col_name"
                                           @click.native="dictSourceClick(item.col_name)">
                                    <span style="float: left; width: 200px;">{{ item.description }}</span>
                                    <span style="float: right; color: #8492a6; font-size: 13px">{{ item.col_name }}</span>
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <!-- 控件类型下拉框及联——数据表  SQL配置 -->
                <el-row v-if="dataSheet">
                    <el-col :span="12"
                            style="width: 100%">
                        <el-form-item label="SQL配置"
                                      prop="codeBindSource.tableSql"
                                      :rules="{required: true, message: '不能为空', trigger: 'change'}">
                            <el-input class="ctextarea"
                                      type="textarea"
                                      :clearable="codeId === ''"
                                      autosize
                                      placeholder="请输入内容"
                                      v-model="queryList.codeBindSource.tableSql">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <!-- 控件类型下拉框及联——模型字段 -->
                <el-row v-if="modelField">
                    <el-col :span="12">
                        <el-form-item prop="codeBindSource.refModuleId"
                                      label="引用模型:"
                                      :rules="{required: true, message: '不能为空', trigger: 'change'}">
                            <el-select v-model="queryList.codeBindSource.refModuleId"
                                       :clearable="codeId === ''"
                                       placeholder="请选择"
                                       collapse-tags
                                       @change="selectChange"
                                       @clear="refModuleIdClear">
                                <el-option :value="mineStatusValue.id"
                                           :label="mineStatusValue.name"
                                           style="height: auto">
                                    <el-tree :data="reportData"
                                             node-key="id"
                                             ref="tree"
                                             highlight-current
                                             :props="defaultProps"
                                             @check-change="handleCheckChange"
                                             @node-click="handleNodeClick"></el-tree>
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="codeBindSource.showModuleCodeId"
                                      label="模型字段"
                                      :rules="{required: true, message: '不能为空', trigger: 'change'}">
                            <el-select v-model="queryList.codeBindSource.showModuleCodeId"
                                       placeholder="请选模型字段"
                                       filterable
                                       :clearable="codeId === ''">
                                <el-option v-for="item in reportDataField"
                                           :key="item.id"
                                           :label="item.label"
                                           :value="item.id"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <!-- 控件类型及联   下拉框及联——子码表名称  SQL配置 -->
                <el-row v-if="dataSheetS">
                    <el-row v-for="(column, idx) in bindColumns"
                            :key="idx">
                        <el-col :span="12">
                            <el-form-item prop="subCodeId"
                                          label="子码表名称">
                                <el-select v-model="bindColumns[idx]['subCodeId']"
                                           placeholder="请选子码表名称"
                                           filterable
                                           :clearable="codeId === ''">
                                    <el-option v-for="item in codeTypeNameList"
                                               :label="item.label"
                                               :key="item.label"
                                               :value="item.value"
                                               @click.native="codeNameClick(bindColumns[idx].subCodeId,idx)">
                                        <span style="float: left; width: 200px;">{{ item.label }}</span>
                                        <span style="float: right; color: #8492a6; font-size: 13px">{{ item.catalogName }}</span>
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item prop="sqlString"
                                          label="SQL配置">
                                <el-input clearable
                                          @change="sqLClick(bindColumns[idx].sqlString,idx)"
                                          v-model="bindColumns[idx]['sqlString']"
                                          style="width: 160px !important"
                                          type="textarea"></el-input>
                                <i class="el-icon-circle-plus-outline"
                                   v-show="showPlus(idx)"
                                   style="color: #409eff; font-size:15px;cursor: pointer;"
                                   @click="plus(idx)"></i>
                                <i class="el-icon-remove-outline"
                                   v-show="showMouse(idx)"
                                   style="color: #409eff; font-size:15px;cursor: pointer;"
                                   @click="mouse(idx)"></i>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-row>

                <!-- 控件类型及联下拉框及联——子码表名称 -->
                <el-row v-if="['debb3cfd-7bbc-11e9-b0c3-0242ac110002','43688b37899944da8fe3fd58725a17f0'].indexOf(queryList.controlTypeId)>-1">
                    <el-col :span="4"
                            class="diaTitle">过滤条件</el-col>
                    <el-col :span="20">
                        <el-form-item prop="refModuleQuery">
                            <el-input type="textarea"
                                      clearable
                                      :autosize="{ minRows: 4, maxRows: 6}"
                                      placeholder='[{"filed":"xxx","operator":"=","value":"123"}]'
                                      v-model="queryList.refModuleQuery">
                            </el-input>
                            <p style="color:orange;">注意:operator操作符支持(=、!=、like、in、not in、between)操作,value多个值用英文逗号(,)隔开</p>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item prop="defaultValue"
                                      label="默认值">
                            <el-input clearable
                                      v-model="queryList.defaultValue"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="displayStyle"
                                      label="码表宽度">
                            <div class="code_table_width">
                                <p @click="codeWidth(item.displayStyle)"
                                   :class="(item.displayStyle == queryList.displayStyle)?'codeTableClass': ''"
                                   v-for="(item,index) in codeTableList"
                                   :key=index>{{item.value}}</p>
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item prop="codeTip"
                                      label="字段说明">
                            <el-input clearable
                                      v-model="queryList.codeTip"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="4"
                            class="diaTitle">字段只读信息</el-col>
                    <el-col :span="20">
                        <el-form-item prop="isValidate">
                            <el-checkbox v-model="queryList.addReadOnly">新增只读</el-checkbox>
                        </el-form-item>
                        <el-form-item prop="isValidate">
                            <el-checkbox v-model="queryList.updateReadOnly">修改只读</el-checkbox>
                        </el-form-item>

                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="4"
                            class="diaTitle">字段验证信息</el-col>
                    <el-col :span="20">
                        <el-form-item prop="isValidate"
                                      label="是否验证">
                            <el-switch v-model="isValidate"
                                       active-text="是"
                                       inactive-text="否"
                                       active-value="是"
                                       inactive-value="否"></el-switch>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row v-if="fieldValInf">
                    <el-col :span="12">
                        <el-form-item prop="validType"
                                      label="校验规则"
                                      class="el-check">
                            <el-select v-model="queryList.validates[0].validType"
                                       multiple
                                       collapse-tags
                                       placeholder="请选择">
                                <el-option v-for="item in validTypeList"
                                           :key="item.id"
                                           :label="item.name"
                                           :value="item.name"
                                           @click.native="validTypeCheck(item)">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"
                            v-if="codeCheck !== '正则表达式'?false:true">
                        <el-form-item prop="validates[0].validTypeExpression"
                                      label="正则表达式"
                                      :rules="[{required: true, message: '正则表达式不能为空', trigger: 'change'},
                                               {pattern: /^\/\^?.*(\$?[\/|\/g|\/gi|\/i|\/ig])$/, message: '请输入正确的正则,', trigger: ['blur', 'change']}]">
                            <el-input clearable
                                      @blur='validTypeChange(queryList.validates[0].validTypeExpression)'
                                      v-model="queryList.validates[0].validTypeExpression"
                                      :disabled="codeCheck !== '正则表达式'?true:false"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"
                            v-if="codeCheck !== '正则表达式'?false:true"
                            class="check_regular">
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   @click="showCreateField(codeCheck)"></el-button>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="4"
                            class="diaTitle">审核配置信息</el-col>
                    <el-col :span="20">
                        <el-form-item prop="isApprove"
                                      label="是否审核">
                            <el-switch v-model="isApprove"
                                       active-text="是"
                                       inactive-text="否"
                                       active-value="是"
                                       inactive-value="否"></el-switch>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row v-if="auditConInf">
                    <el-col :span="12">
                        <el-form-item prop="approve.approveType"
                                      label="审核规则"
                                      :rules="{required: true, message: '审核规则不能为空', trigger: 'change'}">
                            <el-select v-model="queryList.approve.approveType"
                                       placeholder="请选择审核规则"
                                       filterable
                                       :clearable="codeId === ''">
                                <el-option v-for="item in approveTypeList"
                                           :key="item.value"
                                           :label="item.value"
                                           :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="approveTypeExpression"
                                      label="审核备注">
                            <el-input clearable
                                      v-model="queryList.approve.approveTypeExpression"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="4"
                            class="diaTitle">采集配置信息</el-col>
                    <el-col :span="20">
                        <el-form-item prop="isCollect"
                                      label="是否采集">
                            <el-switch v-model="isCollect"
                                       active-text="是"
                                       inactive-text="否"
                                       active-value="是"
                                       inactive-value="否"></el-switch>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row v-if="collectionConfig">
                    <el-col :span="12">
                        <el-form-item prop="codeCollect.collectType"
                                      label="采集方式">
                            <el-select :clearable="codeId === ''"
                                       v-model="queryList.codeCollect.collectType"
                                       placeholder="请选择采集方式"
                                       filterable>
                                <el-option v-for="item in collectTypeList"
                                           :key="item.value"
                                           :label="item.value"
                                           :value="item.value"
                                           @click.native="collectTypelick(item.value)"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"
                            v-if="queryList.codeCollect.collectType !== '自定义脚本'?false:true">
                        <el-form-item prop="codeCollect.collectScriptId"
                                      label="采集脚本"
                                      :rules="{required: true, message: '采集脚本不能为空', trigger: 'change'}">
                            <el-input placeholder="请输入采集脚本"
                                      :clearable="codeId === ''"
                                      v-model="queryList.codeCollect.collectScriptId"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row v-if="collectionConfig">
                    <el-col :span="12">
                        <el-form-item prop="codeCollect.collectFrequency"
                                      label="采集频率"
                                      :rules="{required: true, message: '采集频率不能为空', trigger: 'change'}">
                            <el-select :clearable="codeId === ''"
                                       v-model="queryList.codeCollect.collectFrequency"
                                       placeholder="请选择采集频率"
                                       filterable>
                                <el-option v-for="item in collectFrequencyList"
                                           :key="item.value"
                                           :label="item.value"
                                           :value="item.label"
                                           @click.native="collectFrequencyCheck(item.value)"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="codeCollect.collectMapperKey"
                                      label="映射Key值"
                                      :rules="{required: true, message: '映射Key值不能为空', trigger: 'change'}">
                            <el-input :clearable="codeId === ''"
                                      v-model="queryList.codeCollect.collectMapperKey"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row v-if="collectionConfig">
                    <el-col :span="12"
                            v-if="frequency != '自定义'?false:true">
                        <el-form-item prop="codeCollect.collectFrequencyExpression"
                                      label="正则表达式"
                                      :rules="[{required: true, message: '正则表达式不能为空', trigger: 'change'},{pattern: /^\/\^?.*(\$?[\/|\/g|\/gi|\/i|\/ig])$/, message: '请输入正确的正则,', trigger: ['blur', 'change']}]">
                            <el-input clearable
                                      @blur='frequencyChange(queryList.codeCollect.collectFrequencyExpression)'
                                      v-model="queryList.codeCollect.collectFrequencyExpression"
                                      :disabled="frequency != '自定义'?true:false"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12"
                            v-if="frequency != '自定义'?false:true"
                            class="check_regular">
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   @click="showCreate"></el-button>
                    </el-col>
                </el-row>
            </el-form>
        </section>
        <section class="btn-wrap"
                 style="left: calc(50% - 70px);">
            <el-button type="primary"
                       @click="submit('rulesFrom',title)">保存</el-button>
            <el-button @click="resetDialog('rulesFrom')">取消</el-button>
        </section>
    </el-dialog>
</template>

<script>
    import cmdbCodeService from 'src/services/cmdb/rb-cmdb-code-services.factory' // 控件类型依靠
    import cmdbDictService from 'src/services/cmdb/rb-configDict-service.factory'
    import { isJsonArray } from 'src/utils/validate.js'

    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'

    import tableManagement from 'src/services/condition_management/table_management.js'
    export default {
        name: 'CodeEdit',
        props: ['display', 'codeId', 'datasList'],
        components: {
            Treeselect
        },
        data() {
            var checkFilterCondition = (rule, value, callback) => {

                if (!value) {
                    callback()
                    return false
                }
                let valid = false
                let valueParse = JSON.parse(value)

                if (Array.isArray(valueParse) && valueParse.length > 0) {
                    valid = valueParse.every((item) => {
                        if (Object.keys(item).includes('filed') && Object.keys(item).includes('operator') && Object.keys(item).includes('value')) {
                            return true
                        } else {
                            return false
                        }
                    })
                }
                if (!valid) {
                    callback(new Error('属性必须包含filed、operator、value'))
                } else {
                    callback()
                }
            }
            return {
                // 控件类型
                showCascaderControl: ['cascader'],
                approveTypeProp: '',
                codeTypeNameList: [], // 子码表名称 下拉框
                bindColumnsTable: [{
                    'colTitle': '',
                    'colKey': '',
                    'sortIndex': ''
                }],
                bindColumns: [{
                    'subCodeId': '', // 子码表ID
                    'sqlString': '', // SQL配置
                    'sortIndex': ''
                }],
                dataSourceData: '',
                fieldValInf: false, // 字段验证信息
                auditConInf: false, // 审核配置信息
                collectionConfig: false, // 采集配置信息
                TABLE: false, // 表格级联的框
                frequency: '', // 采集配置信息（正则表达式）
                codeCheck: '', // 字段验证信息（正则表达式）
                Drop_down: '', // 控件类型数据
                moduleID: '', // 模型分组ID
                defaultProps: {
                    children: 'childModules',
                    // name: 'name',
                    label: 'name'
                },
                mineStatus: '', // 树形数据
                mineStatusValue: {}, // 树形数据

                dataSource: false, // 控件类型-- 数据源
                dataDictionary: false, // 控件类型-- 数据字典
                dataSheet: false, // 控件类型-- 数据表
                dataSheetS: false, // 下拉框及联——子码表  SQL配置
                modelField: false, // 控件类型-- 模型字段

                reportData: [], // 引用模型树形数据
                reportDataField: [], // 引用模型树形字段
                dictList: [], // 字典下拉框数据
                dataSourceList: [
                    {
                        value: '数据字典',
                        label: '0'
                    }, {
                        value: '数据表',
                        label: '1'
                    }, {
                        value: '引用模型',
                        label: '2'
                    }
                ],
                codeIndex: 4,
                ControlTypesList: [], // 模型分组
                codeTableList: [
                    {
                        value: '整行',
                        displayStyle: 1
                    },
                    {
                        value: '1/2行',
                        displayStyle: 2
                    },
                    {
                        value: '1/3行',
                        displayStyle: 3
                    },
                    {
                        value: '1/4行',
                        displayStyle: 4
                    }
                ],
                title: this.codeId === '' ? '新增码表' : '修改码表',
                show: this.display,
                isValidate: '否', // 当"是"时,需要填写validates信息
                isApprove: '否', // 当"是"时,需要填写approve信息
                isCollect: '否', // 当"是"时,需要填写codeCollect信息
                defaultValue: '',
                addReadOnly: false,
                updateReadOnly: false,
                approveTypeList: [], // 审核机制下拉框
                validTypeList: [], // 字段验证规则下拉框
                collectFrequencyList: [], // 采集频率下拉框
                collectTypeList: [], // 采集方式下拉框
                controlTypeIdList: [], // 控件类型下拉框

                dictSourceSLIst: '', // 字典类型转换值
                queryList: {
                    codeId: '', // 修改专用
                    filedCode: '', // 码表编码
                    filedName: '', // 码表名称
                    moduleCatalogId: '', // 模型分组ID
                    controlTypeId: '', // 控件ID
                    codeLength: 40, // 码表长度
                    codeTip: '', // 码表提示信息
                    displayStyle: 4, // 码表宽度
                    isBindSource: '否', // 当"是"时,需要填写codeBindSource信息
                    codeBindSource: {
                        bindSourceType: '', // 数据源
                        dictSource: '', // 当bindSourceType="数据字典"时需要填写 字典类型
                        tableSql: '', // 当bindSourceType="数据表"时需要填写
                        refModuleId: '', // 当bindSourceType="引用模型"时需要填写
                        showModuleCodeId: '', // 当bindSourceType="引用模型"时需要填写

                    },
                    refModuleQuery: '',// 过滤条件

                    cascadeList: [ // 级联配置集合
                        {
                            'id': '', // 级联配置ID
                            'codeId': '', // 码表ID
                            'subCodeId': '', // 子码表ID
                            'sqlString': '', // SQL配置
                            'sortIndex': '1' // 排序
                        }
                    ],
                    tableColList: [ // 表格配置
                        {
                            id: '', // 表格配置ID
                            codeId: '', // 码表ID
                            colTitle: '', // 表格列名
                            colKey: '', // 映射KEY
                            sortIndex: 1 // 排序
                        }],
                    isValidate: '否', // 当"是"时,需要填写validates信息
                    validates:
                        [{
                            validType: [], // 字段验证规则
                            validTypeExpression: '', // 当validType="正则表达式"时,需要填写
                            handlerClass: '' // 验证处理类
                        }],
                    isApprove: '否', // 当"是"时,需要填写approve信息
                    approve: {
                        approveType: '', // 审核规则
                        approveTypeExpression: '' // 审核备注
                    },
                    isCollect: '否', // 当"是"时,需要填写codeCollect信息

                    defaultValue: '',
                    addReadOnly: this.datasList.addReadOnly === 1,
                    updateReadOnly: this.datasList.updateReadOnly === 1,
                    codeCollect: {
                        collectType: '',  // 采集方式
                        collectMapperKey: '', // 映射Key值
                        collectScriptId: '', // 当collectType="自定义脚本"时,需要填写
                        collectFrequency: '', // 采集频率
                        collectFrequencyExpression: '' // collectFrequency="正则表达式"时,需要填写
                    }
                },
                rules: {
                    refModuleQuery: [
                        {
                            validator: isJsonArray,
                            trigger: 'blur'
                        },
                        {
                            validator: checkFilterCondition,
                            trigger: 'blur'
                        }
                    ],
                    moduleCatalogId: [
                        { required: true, message: '请选择模型分组', trigger: ['blur', 'change'] }
                    ],
                    filedCode: [
                        { required: true, message: '请输入码表编码', trigger: ['blur', 'change'] },
                        { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '请输入以英文字母开头,数字,下划线组合,', trigger: ['blur', 'change'] },
                        { min: 1, max: 60, message: '最多不能超过60个字段', trigger: ['blur', 'change'] }
                    ],
                    filedName: [{ required: true, message: '请输入码表名称', trigger: ['blur', 'change'] }],
                    controlTypeId: [{ required: true, message: '请输入控件类型', trigger: ['blur', 'change'] }],
                    codeLength: [{ required: true, message: '请输入字段长度', trigger: ['blur', 'change'] }]
                }
            }
        },
        mounted: function () {
            this.queryList.addReadOnly = this.datasList.addReadOnly === 1
            this.queryList.updateReadOnly = this.datasList.updateReadOnly === 1
            this.ControlTypes()
            this.Dictionaries()
        },
        watch: {
            datasList: {
                handler(newVal) {
                    if (this.title === '修改码表') {
                        this.queryList.codeId = newVal.codeId // 码表编码
                        this.queryList.filedCode = newVal.filedCode // 码表编码
                        this.queryList.filedName = newVal.filedName // 码表名称
                        this.queryList.moduleCatalogId = newVal.moduleCatalogId // 模型分组
                        this.queryList.controlTypeId = newVal.controlTypeId // 控件类型
                        this.codeWidth(newVal.displayStyle)
                        this.queryList.codeLength = newVal.codeLength // 码表长度
                        this.queryList.codeTip = newVal.codeTip // 提示信息

                        this.isValidate = newVal.isValidate // 字段检验信息开关
                        this.queryList.isValidate = newVal.isValidate
                        if (newVal.isValidate === '是') {
                            this.fieldValInf = true
                        }
                        // 修改数据，带进来的数据
                        if (newVal.validates.length !== 0) {
                            let vs = newVal.validates
                            let vts = []
                            let hcs = []
                            let vte = ''
                            for (var i = 0; i < vs.length; i++) {
                                vts.push(vs[i].validType)
                                hcs.push(vs[i].handlerClass)
                                if (vs[i].validType == '正则表达式') {
                                    vte = vs[i].validTypeExpression
                                }
                            }
                            this.queryList.validates[0].validType = vts
                            this.queryList.validates[0].handlerClass = hcs.join(',')
                            this.queryList.validates[0].validTypeExpression = vte
                            this.codeCheck = vts
                        } else {
                            this.queryList.validates[0].validType = ''
                            this.queryList.validates[0].handlerClass = ''
                        }
                        // 字段检验信息  正则表达式
                        this.isApprove = newVal.isApprove
                        this.queryList.isApprove = newVal.isApprove
                        if (newVal.isApprove === '是') {
                            this.auditConInf = true
                        }  // 审核配置信息开关
                        this.queryList.approve.approveType = newVal.approve != null ? newVal.approve.approveType : '' // 审核规则
                        this.queryList.approve.approveTypeExpression = newVal.approve != null ? newVal.approve.approveTypeExpression : ''  // 审核备注

                        this.isCollect = newVal.isCollect // 采集配置信息
                        this.queryList.isCollect = newVal.isCollect
                        if (newVal.isCollect === '是') {
                            this.collectionConfig = true
                        }
                        this.queryList.codeCollect.collectType = newVal.codeCollect != null ? newVal.codeCollect.collectType : '' // 采集方式
                        this.queryList.codeCollect.collectScriptId = newVal.codeCollect != null ? newVal.codeCollect.collectScriptId : ''  // 采集脚本
                        this.queryList.codeCollect.collectFrequency = newVal.codeCollect != null ? newVal.codeCollect.collectFrequency : '' // 采集频率
                        this.frequency = newVal.codeCollect != null ? newVal.codeCollect.collectFrequency : '' // 采集频率
                        this.queryList.codeCollect.collectMapperKey = newVal.codeCollect != null ? newVal.codeCollect.collectMapperKey : '' // 映射Key值
                        this.queryList.codeCollect.collectFrequencyExpression = newVal.codeCollect != null ? newVal.codeCollect.collectFrequencyExpression : '' // 映射Key值

                        this.queryList.codeBindSource.bindSourceType = newVal.codeBindSource != null ? newVal.codeBindSource.bindSourceType : '' // 数据源类型
                        if (newVal.codeBindSource != null && newVal.codeBindSource.bindSourceType) {
                            this.queryList.isBindSource = '是'
                        } else {
                            this.queryList.isBindSource = '否'
                        }

                        this.queryList.defaultValue = newVal.defaultValue

                        this.queryList.addReadOnly = newVal.addReadOnly
                        this.queryList.updateReadOnly = newVal.updateReadOnly
                        this.queryList.codeBindSource.dictSource = newVal.codeBindSource != null ? newVal.codeBindSource.dictSource : '' // 数据源类型
                        this.queryList.codeBindSource.refModuleId = newVal.codeBindSource != null ? newVal.codeBindSource.refModuleId : '' // 数据源类型
                        this.queryList.codeBindSource.tableSql = newVal.codeBindSource != null ? newVal.codeBindSource.tableSql : '' // 数据源类型
                        this.queryList.codeBindSource.showModuleCodeId = newVal.codeBindSource != null ? newVal.codeBindSource.showModuleCodeId : '' // 数据源类型
                        this.queryList.refModuleQuery = newVal.codeBindSource && newVal.codeBindSource.refModuleQuery || ''

                        this.bindColumnsTable = newVal.tableColList != null ? newVal.tableColList : '' // 表格 表格名称 sql配置
                        this.queryList.cascadeList = this.bindColumns = newVal.cascadeList != null ? newVal.cascadeList : ''

                        // 子码表名称
                        this.rbHttp.sendRequest({
                            method: 'GET',
                            url: '/v1/cmdb/code/getDistinctCodeList'
                        }).then((res) => {
                            this.codeTypeNameList = []
                            for (let item in res) {
                                this.codeTypeNameList.push({
                                    label: res[item].filedName,
                                    value: res[item].codeId,
                                    catalogName: res[item].codeTypeNameList
                                })
                            }
                        })
                        // 修改触发的级联框
                        this.getDataSource(newVal.codeBindSource != null ? newVal.codeBindSource.bindSourceType : '')
                        this.modelFieldClick(newVal.moduleCatalogId)
                    }
                    if (this.queryList.controlTypeId === 'debb3cfd-7bbc-11e9-b0c3-0242ac110002') {
                        this.dataSource = true
                        this.getTypeId('下拉框')
                    } else if (this.queryList.controlTypeId === '43688b37899944da8fe3fd58725a17f0') {
                        this.dataSource = true
                        this.dataSheetS = true
                        this.getTypeIdS('及联下拉框')
                    } else if (this.queryList.controlTypeId === '2fb702713b584a9f85f2cc38bfccd1e3') {
                        this.TABLE = true
                        this.getTypeIdS('表格')
                    } else if (this.queryList.codeBindSource.tableSql) {
                        this.dataSheet = true
                    } else {
                        this.dataSource = false
                        this.dataSheetS = false
                        this.TABLE = false
                        this.dataSource = false
                        this.dataSheet = false
                        this.modelField = false
                        this.dataDictionary = false
                    }
                },
                // deep: true,
                immediate: true // 初始化传递
            },
            isValidate(val) {
                this.queryList.isValidate = val
                if (val === '是') {
                    this.fieldValInf = true
                } else if (val === '否') {
                    this.fieldValInf = false
                    this.queryList.validates.validTypeExpression = ''
                    this.codeCheck = ''
                    this.queryList.validates[0].validType = ''
                    this.queryList.validates[0].handlerClass = ''
                }
            },
            isApprove(val) {
                this.queryList.isApprove = val
                if (val === '是') {
                    this.auditConInf = true
                } else if (val === '否') {
                    this.auditConInf = false
                    this.queryList.approve.approveType = ''
                    this.queryList.approve.approveTypeExpression = ''
                }
            },
            isCollect(val) {
                this.queryList.isCollect = val
                if (val === '是') {
                    this.collectionConfig = true
                } else if (val === '否') {
                    this.collectionConfig = false
                    this.queryList.codeCollect.collectType = ''
                    this.queryList.codeCollect.collectMapperKey = ''
                    this.queryList.codeCollect.collectScriptId = ''
                    this.queryList.codeCollect.collectFrequency = ''
                    this.queryList.codeCollect.collectFrequencyExpression = ''
                    this.frequency = ''
                }
            }
        },
        methods: {
            // 验证码表编码和模型分组的唯一性(页面没有使用到)
            validateCodeUnique(rule, value, callback) {
                let params = {
                    filedCode: this.queryList.filedCode,
                    catalogName: this.queryList.moduleCatalogId
                }
                cmdbDictService.validateIfUnique(params).then((res) => {
                    if (res.flag) {
                        return callback()
                    } else {
                        return callback('编码和模型分组已存在,请重新编辑')
                    }
                })
            },
            dictSourceClick(data) {
                this.queryList.codeBindSource.dictSource = data
            },
            //  sql  检验
            checkSqlFiled(filed, key) {
                filed = filed.replace('distinct ', '')
                if (filed.split(' ').length !== 2 && filed.split(' ').length !== 3) {
                    this.$message.error('SQL配置格式不正确, 正确格式如:\n\rselect xx `id`, xx `key`, xxx `value` from table_name ')
                    return false
                }
                if (filed.split(' ').length === 2 && filed.split(' ')[1] !== key) {
                    this.$message.error('SQL配置格式不正确, 正确格式如:\n\rselect xx `id`, xx `key`, xxx `value` from table_name ')
                    return false
                }
                if (filed.split(' ').length === 3 && (filed.split(' ')[1] !== 'as' || filed.split(' ')[2] !== key)) {
                    this.$message.error('SQL配置格式不正确, 正确格式如:\n\rselect xx `id`, xx `key`, xxx `value` from table_name ')
                    return false
                }
                let pattern = /^[a-zA-Z.][a-zA-Z0-9_.]*$/
                if (!pattern.test(filed.split(' ')[0])) {
                    this.$message.error('SQL配置格式不正确, [id、key、value]列名只能为字母、数字或下划线')
                    return false
                }
                return true
            },
            checkSql(dataSQL) {
                let sql = dataSQL.toLowerCase()
                let filter = ['delete', 'drop', 'truncate', 'update', 'insert', 'alter']
                let flag = false
                sql.split(' ').forEach(item => {
                    if (filter.indexOf(item) > -1) {
                        flag = true
                        return false
                    }
                })
                if (flag) {
                    this.$message.error(
                        'SQL中不能包含敏感词[delete, drop, truncate, update, insert, alter等]'
                    )
                    return false
                }
                if (sql.substring(0, 6).trim() !== 'select') {
                    this.$message.error(
                        'SQL 以 select 开头'
                    )
                    return false
                }
                sql = sql.substring(6, sql.indexOf(' from')).trim()
                let fileds = sql.split(',')
                if (fileds.length !== 3) {
                    this.$message.error(
                        'SQL配置格式不正确, 正确格式如:\n\rselect xx `id`, xx `key`, xxx `value` from table_name '
                    )
                    return false
                }
                if (!this.checkSqlFiled(fileds[0].trim(), '`id`')) {
                    return false
                }
                if (!this.checkSqlFiled(fileds[1].trim(), '`key`')) {
                    return false
                }
                if (!this.checkSqlFiled(fileds[2].trim(), '`value`')) {
                    return false
                }
                return true
            },
            collectTypelick(dataValue) {
                this.queryList.codeCollect.collectType = dataValue
                if (dataValue !== '自定义脚本') {
                    this.queryList.codeCollect.collectScriptId = ''
                }
            },
            codeWidth(displayStyle) {  // 码表宽度
                this.queryList.displayStyle = displayStyle
            },

            tablecodeNameClick(data, index) {
                for (var i = 0; i < this.bindColumnsTable.length; i++) {
                    var item = this.bindColumnsTable[i]
                    if (item.colTitle === '' && index === i) {
                        item.colTitle = data
                    }
                }
                this.queryList.tableColList = this.bindColumnsTable
            },

            tablesqLClick(data, index) {
                for (var i = 0; i < this.bindColumnsTable.length; i++) {
                    var item = this.bindColumnsTable[i]
                    if (item.colKey === '' && index === i) {
                        item.colKey = data
                    }
                }
                this.queryList.tableColList = this.bindColumnsTable
            },
            plusTable(idxTable) {
                this.bindColumnsTable.splice(idxTable + 1, 0, { 'colTitle': '', 'colKey': '' })
                // let arr1 = this.bindColumnsTable.map(item => {
                //     return Object.assign(item)
                // })

                this.queryList.tableColList = this.bindColumnsTable
            },
            showPlusTable() {
                return true
            },
            mouseTable(idxTable) {
                this.bindColumnsTable.splice(idxTable, 1)
                this.queryList.tableColList = this.bindColumnsTable
            },
            showMouseTable(idxTable) {
                return (this.bindColumnsTable.length > 1 && idxTable === 0) || idxTable > 0
            },

            plus(idx) {
                this.bindColumns.splice(idx + 1, 0, { 'subCodeId': '', 'sqlString': '' })
                // let arr1 = this.bindColumns.map(item => {
                //     return Object.assign(item)
                // })
                this.queryList.cascadeList = this.bindColumns
            },
            showPlus() {
                return true
            },
            mouse(idx) {
                this.bindColumns.splice(idx, 1)
            },
            showMouse(idx) {
                return (this.bindColumns.length > 1 && idx === 0) || idx > 0
            },
            codeNameClick(data, index) {
                for (var i = 0; i < this.bindColumns.length; i++) {
                    var item = this.bindColumns[i]
                    if (item.subCodeId === '' && index === i) {
                        item.subCodeId = data
                    }
                }
                this.queryList.cascadeList = this.bindColumns
            },

            sqLClick(data, index) {
                for (var i = 0; i < this.bindColumns.length; i++) {
                    var item = this.bindColumns[i]
                    if (item.sqlString === '' && index === i) {
                        item.sqlString = data
                    }
                }
                this.queryList.cascadeList = this.bindColumns
            },
            validTypeCheck(data) {
                this.codeCheck = data.name
                if (data.name !== '正则表达式') {
                    this.queryList.validates[0].validTypeExpression = ''
                }
                this.queryList.validates[0].handlerClass = this.dealValueOfHandlerClass(data)
                if (data.name != '正则表达式' && this.queryList.validates[0].validType.includes('正则表达式')) {
                    this.queryList.validates[0].validType = [data.name]
                    this.queryList.validates[0].handlerClass = data.value
                } else if (data.name == '正则表达式' && this.queryList.validates[0].validType.includes('正则表达式')) {
                    this.queryList.validates[0].validType = ['正则表达式']
                    this.queryList.validates[0].handlerClass = 'com.aspire.ums.cmdb.v3.validator.RegexValidHandler'
                } else {
                    this.queryList.validates[0].validTypeExpression = ''
                }
            },
            // 处理handlerClass的值
            dealValueOfHandlerClass(data) {
                let handler = this.queryList.validates[0].handlerClass
                var handlerArray = handler == '' ? [] : handler.split(',')
                let typeList = this.queryList.validates[0].validType
                // 判断是 (true 新增)还是 (false 删除)
                let flag = false
                for (var i = 0; i < typeList.length; i++) {
                    if (typeList[i] == data.name) {
                        flag = true
                        break
                    }
                }
                if (flag) {
                    handlerArray.push(data.value)
                } else {
                    let index = -1
                    for (let i = 0; i < handlerArray.length; i++) {
                        if (handlerArray[i] == data.value) {
                            index = i
                            break
                        }
                    }
                    handlerArray.splice(index, 1)
                }
                return handlerArray.join(',')
            },
            collectFrequencyCheck(data) {
                this.queryList.codeCollect.collectFrequency = data
                this.frequency = data
                if (data !== '自定义') {
                    this.queryList.codeCollect.collectFrequencyExpression = ''
                }
            },
            handleNodeClick(e) {
                this.reportDataField = []
                this.queryList.codeBindSource.refModuleId = e.id
                this.queryList.codeBindSource.showModuleCodeId = ''
                this.mineStatusValue = { id: e.id, name: e.name }
                this.getModuleDetailList(e.id)
            },
            // 获取引用模型列表
            getTreeList() {
                tableManagement.getTreeList('').then(res => {
                    // JSON.parse(JSON.stringify(res).replace(/childModules/g, 'children')) // 改变树结构字段
                    this.reportData = res
                    this.$nextTick(() => {
                        this.getCurrentNode(this.queryList.codeBindSource.refModuleId)
                    })
                })
            },
            // 获取模型字段列表
            getModuleDetailList(id) {
                tableManagement.getModuleDetailList(id).then(res => {
                    for (let item in res) {
                        this.reportDataField.push({
                            id: res[item].codeId,
                            label: res[item].filedName
                        })
                    }
                })
            },
            refModuleIdClear() {
                this.reportDataField = []
                this.queryList.codeBindSource.showModuleCodeId = ''
            },
            // 模型分段值
            modelFieldClick(data) {
                this.moduleID = data
                this.reportData = []
                this.reportDataField = []
                this.getTreeList()
                this.getModuleDetailList(this.queryList.codeBindSource.refModuleId)
            },
            getCurrentNode(nodeId) {
                let node = this.$refs.tree.getNode(nodeId)
                this.mineStatusValue = {
                    id: node.data.id,
                    name: node.data.name
                }
            },
            getTypeIdS(data) {
                if (data === '及联下拉框') {
                    this.dataSheetS = true
                    this.dataSource = true
                    this.Drop_down = '级联下拉框'
                } else if (data === '表格') {
                    this.TABLE = true
                }
            },
            getTypeId(data) {
                this.Drop_down = data
                if (data === '级联下拉框') {
                    this.dataSheetS = true
                    this.dataSource = true
                    this.dataSheet = false
                    this.TABLE = false
                    this.dataDictionary = false
                    this.modelField = false
                    this.queryList.codeBindSource.bindSourceType = ''

                    this.bindColumns = [{
                        'subCodeId': '', // 子码表ID
                        'sqlString': '' // SQL配置
                    }]
                    // 子码表名称
                    this.rbHttp.sendRequest({
                        method: 'GET',
                        url: '/v1/cmdb/code/getDistinctCodeList'
                    }).then((res) => {
                        this.codeTypeNameList = []
                        for (let item in res) {
                            this.codeTypeNameList.push({
                                label: res[item].filedName,
                                value: res[item].codeId,
                                catalogName: res[item].catalogName
                            })
                        }
                    })
                } else if (data === '下拉框') {
                    this.dataSource = true
                    this.dataSheetS = false
                    this.TABLE = false
                } else if (data === '表格') {
                    this.TABLE = true
                    this.dataSource = false
                    this.dataSheetS = false
                    this.dataSheet = false
                    this.modelField = false
                    this.dataDictionary = false
                    this.queryList.isBindSource = '否'
                    this.queryList.codeBindSource.bindSourceType = ''
                    this.queryList.codeBindSource.dictSource = ''
                    this.queryList.codeBindSource.refModuleId = ''
                    this.queryList.codeBindSource.showModuleCodeId = ''
                    this.queryList.codeBindSource.tableSql = ''
                    this.bindColumnsTable = [{
                        'colTitle': '',
                        'colKey': ''
                    }]
                } else {
                    this.TABLE = false
                    this.dataSource = false
                    this.dataSheetS = false
                    this.dataSheet = false
                    this.dataDictionary = false
                    this.modelField = false

                    this.queryList.isBindSource = '否'
                    this.queryList.codeBindSource.bindSourceType = ''
                    this.queryList.codeBindSource.dictSource = ''
                    this.queryList.codeBindSource.refModuleId = ''
                    this.queryList.codeBindSource.showModuleCodeId = ''
                    this.queryList.codeBindSource.tableSql = ''
                }
            },
            getDataSourceclear() {
                this.queryList.isBindSource = '否'

                this.dataDictionary = false
                this.queryList.codeBindSource.dictSource = ''
                this.dataSheet = false
                this.queryList.codeBindSource.tableSql = ''
                this.modelField = false
                this.queryList.codeBindSource.refModuleId = ''
                this.queryList.codeBindSource.showModuleCodeId = ''
            },
            getDataSource(data) {
                this.dataSourceData = data
                if (data) {
                    this.queryList.isBindSource = '是'
                } else {
                    this.queryList.isBindSource = '否'
                }
                if (data === '数据字典' && this.Drop_down === '级联下拉框') {
                    this.dataDictionary = true
                    this.dataSheet = false
                    this.dataSheetS = true
                    this.modelField = false

                    this.queryList.codeBindSource.tableSql = ''
                    this.queryList.codeBindSource.refModuleId = ''
                    this.queryList.codeBindSource.showModuleCodeId = ''
                    cmdbDictService.getDistinctDictType().then((res) => {
                        this.dictList = res
                    }).catch(() => {
                        this.$message.error('获取字典列表失败')
                    })
                } else if (data === '数据表' && this.Drop_down === '级联下拉框') {
                    this.dataSheet = true
                    this.dataSheetS = true
                    this.dataSheet = true
                    this.dataDictionary = false
                    this.modelField = false
                    this.queryList.codeBindSource.dictSource = ''
                    this.queryList.codeBindSource.refModuleId = ''
                    this.queryList.codeBindSource.showModuleCodeId = ''
                } else if (data === '引用模型' && this.Drop_down === '级联下拉框') {
                    this.dataDictionary = false
                    this.dataSheet = false
                    this.modelField = true
                    this.dataSheetS = true
                    this.queryList.codeBindSource.tableSql = ''
                    this.queryList.codeBindSource.dictSource = ''
                    this.modelFieldClick(this.moduleID)
                } else if (data === '数据字典') {
                    this.dataDictionary = true
                    this.dataSheet = false
                    this.modelField = false
                    this.queryList.codeBindSource.tableSql = ''
                    this.queryList.codeBindSource.refModuleId = ''
                    this.queryList.codeBindSource.showModuleCodeId = ''
                    cmdbDictService.getDistinctDictType().then((res) => {
                        this.dictList = res
                    }).catch(() => {
                        this.$message.error('获取字典列表失败')
                    })
                } else if (data === '数据表') {
                    this.dataSheet = true
                    this.dataSheetS = false
                    this.dataDictionary = false
                    this.modelField = false
                    this.queryList.codeBindSource.dictSource = ''
                    this.queryList.codeBindSource.refModuleId = ''
                    this.queryList.codeBindSource.showModuleCodeId = ''
                } else if (data === '引用模型') {
                    this.dataDictionary = false
                    this.dataSheet = false
                    this.modelField = true
                    this.queryList.codeBindSource.tableSql = ''
                    this.queryList.codeBindSource.dictSource = ''
                    this.modelFieldClick(this.moduleID)
                }
            },
            Dictionaries() {
                // 字段验证规则
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'code_validate_type' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    for (let item in res) {
                        this.validTypeList.push({
                            label: res[item].id,
                            value: res[item].value,
                            name: res[item].name
                        })
                    }
                })
                // 审核规则
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'code_approve_type' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    for (let item in res) {
                        this.approveTypeList.push({
                            label: res[item].id,
                            value: res[item].name
                        })
                    }
                })
                // 采集方式
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'code_collect_type' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    for (let item in res) {
                        this.collectTypeList.push({
                            label: res[item].id,
                            value: res[item].name
                        })
                    }
                })
                // 采集频率
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'code_collect_frequency' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    for (let item in res) {
                        this.collectFrequencyList.push({
                            label: res[item].id,
                            value: res[item].name
                        })
                    }
                })
                // 控件类型
                cmdbCodeService.queryCodeControl().then(data => {
                    for (let item in data) {
                        this.controlTypeIdList.push({
                            label: data[item].controlId,
                            value: data[item].controlName
                        })
                    }
                })
                    .catch(() => {
                        this.$message.error('查询控件类型失败')
                    })
            },
            // 模型分组
            ControlTypes() {
                tableManagement.getcontrolList().then(data => {
                    for (let item in data) {
                        this.ControlTypesList.push({
                            label: data[item].id,
                            value: data[item].catalogName
                        })
                    }
                })
                    .catch(() => {
                        this.$message.error('查询模型分组失败')
                    })
            },
            // 保存
            submit(formData, title) {
                // 新增校验
                this.$refs['rulesFrom'].validate((valid) => {
                    if (valid && title === '新增码表') {
                        if (this.Drop_down === '表格') {
                            for (let i = 0; i < this.bindColumnsTable.length; i++) {
                                let item = this.bindColumnsTable[i]
                                if (item.colTitle === '' || item.colKey === '') {
                                    this.$message.error('表格列名，映射key值不能为空')
                                    return
                                } else if (i === this.bindColumnsTable.length - 1) {
                                    this.show = false // 关闭弹窗
                                    this.QueryList(title)
                                }
                            }
                        } else if (this.Drop_down === '级联下拉框' && this.queryList.codeBindSource.bindSourceType === '数据表') {
                            for (let i = 0; i < this.bindColumns.length; i++) {
                                let item = this.bindColumns[i]
                                let newArr = []
                                if (this.bindColumns) {
                                    for (let items in this.bindColumns) {
                                        newArr.push(this.bindColumns[items].subCodeId)
                                    }
                                }
                                let nary = newArr.sort()
                                nary.pop()
                                if (new Set(nary).size !== nary.length) {
                                    this.$message.error('子码表名称是唯一的不能重复,请重新选择')
                                } else if (item.sqlString !== '' && item.subCodeId !== '' && this.queryList.codeBindSource.tableSql !== '') {
                                    if (!this.checkSql(item.sqlString)) {
                                        return false
                                    }
                                    if (!this.checkSql(this.queryList.codeBindSource.tableSql)) {
                                        return false
                                    }
                                    if (i === this.bindColumns.length - 1) {
                                        this.show = false // 关闭弹窗
                                        this.QueryList(title)
                                    }
                                } if (item.sqlString === '' || item.subCodeId === '' || this.queryList.codeBindSource.tableSql === '') {
                                    this.$message.error('子码表名称，sql必填')
                                }
                            }
                        } else if (this.Drop_down === '级联下拉框') {
                            for (let S = 0; S < this.bindColumns.length; S++) {
                                let itemS = this.bindColumns[S]
                                let newArr = []
                                if (this.bindColumns) {
                                    for (let items in this.bindColumns) {
                                        newArr.push(this.bindColumns[items].subCodeId)
                                    }
                                }
                                let nary = newArr.sort()
                                nary.pop()
                                if (new Set(nary).size !== nary.length) {
                                    this.$message.error('子码表名称是唯一的不能重复,请重新选择')
                                } else if (itemS.sqlString !== '' && itemS.subCodeId !== '') {
                                    if (!this.checkSql(itemS.sqlString)) {
                                        return false
                                    }
                                    if (S === this.bindColumns.length - 1) {
                                        this.show = false // 关闭弹窗
                                        this.QueryList(title)
                                    }
                                } else if (itemS.sqlString === '' || itemS.subCodeId === '') {
                                    this.$message.error('子码表名称，sql必填')
                                }
                            }
                        } else if (this.queryList.codeBindSource.bindSourceType === '数据表') {
                            if (this.queryList.codeBindSource.tableSql) {
                                if (!this.checkSql(this.queryList.codeBindSource.tableSql)) {
                                    return false
                                }
                                this.show = false // 关闭弹窗
                                this.QueryList(title)
                            } else {
                                this.$message.error('sql必填')
                            }
                        } else {
                            this.show = false // 关闭弹窗
                            this.QueryList(title)
                        }
                    } else if (valid && title === '修改码表') { // 修改码表接口查询
                        if (this.Drop_down === '表格') {
                            for (let i = 0; i < this.bindColumnsTable.length; i++) {
                                let item = this.bindColumnsTable[i]
                                if (item.colTitle === '' || item.colKey === '') {
                                    this.$message.error('表格列名，映射key值不能为空')
                                    return
                                } else if (i === this.bindColumnsTable.length - 1) {
                                    this.show = false // 关闭弹窗
                                    this.QueryList(title)
                                }
                            }
                        } else if (this.Drop_down === '级联下拉框' && this.queryList.codeBindSource.bindSourceType === '数据表') {
                            for (let i = 0; i < this.bindColumns.length; i++) {
                                let item = this.bindColumns[i]
                                let newArr = []
                                if (this.bindColumns) {
                                    for (let items in this.bindColumns) {
                                        newArr.push(this.bindColumns[items].subCodeId)
                                    }
                                }
                                let nary = newArr.sort()
                                nary.pop()
                                if (new Set(nary).size !== nary.length) {
                                    this.$message.error('子码表名称是唯一的不能重复,请重新选择')
                                } else if (item.sqlString !== '' && item.subCodeId !== '' && this.queryList.codeBindSource.tableSql !== '') {
                                    if (!this.checkSql(item.sqlString)) {
                                        return false
                                    }
                                    if (!this.checkSql(this.queryList.codeBindSource.tableSql)) {
                                        return false
                                    }
                                    if (i === this.bindColumns.length - 1) {
                                        this.show = false // 关闭弹窗
                                        this.QueryList(title)
                                    }
                                } if (item.sqlString === '' || item.subCodeId === '' || this.queryList.codeBindSource.tableSql === '') {
                                    this.$message.error('子码表名称，sql必填')
                                }
                            }
                        } else if (this.Drop_down === '级联下拉框') {
                            for (let S = 0; S < this.bindColumns.length; S++) {
                                let itemS = this.bindColumns[S]
                                let newArr = []
                                if (this.bindColumns) {
                                    for (let items in this.bindColumns) {
                                        newArr.push(this.bindColumns[items].subCodeId)
                                    }
                                }
                                let nary = newArr.sort()
                                nary.pop()
                                if (new Set(nary).size !== nary.length) {
                                    this.$message.error('子码表名称是唯一的不能重复,请重新选择')
                                } else if (itemS.sqlString !== '' && itemS.subCodeId !== '') {
                                    if (!this.checkSql(itemS.sqlString)) {
                                        return false
                                    }
                                    if (S === this.bindColumns.length - 1) {
                                        this.show = false // 关闭弹窗
                                        this.QueryList(title)
                                    }
                                } else if (itemS.sqlString === '' || itemS.subCodeId === '') {
                                    this.$message.error('子码表名称，sql必填')
                                }
                            }
                        } else if (this.queryList.codeBindSource.bindSourceType === '数据表') {
                            if (this.queryList.codeBindSource.tableSql) {
                                if (!this.checkSql(this.queryList.codeBindSource.tableSql)) {
                                    return false
                                }
                                this.show = false // 关闭弹窗
                                this.QueryList(title)
                            } else {
                                this.$message.error('sql必填')
                            }
                        } else {
                            this.show = false // 关闭弹窗
                            this.QueryList(title)
                        }
                    }
                })
            },
            QueryList() {
                // 将validates的数据做处理，格式为字符串，转为以逗号分隔
                let validateTypes = this.queryList.validates[0].validType
                let hcs = this.queryList.validates[0].handlerClass.split(',')
                let tmpValidates = []
                for (var i = 0; i < validateTypes.length; i++) {
                    if (validateTypes[i] == '正则表达式') {
                        tmpValidates.push({
                            validType: validateTypes[i],
                            validTypeExpression: this.queryList.validates[0].validTypeExpression, // 当validType="正则表达式"时,需要填写
                            handlerClass: hcs[i]
                        })
                    } else {
                        tmpValidates.push({
                            validType: validateTypes[i],
                            validTypeExpression: '',
                            handlerClass: hcs[i]
                        })
                    }
                }
                let queryListS = {
                    codeId: this.queryList.codeId, // 修改专用
                    filedCode: this.queryList.filedCode, // 码表编码
                    filedName: this.queryList.filedName, // 码表名称
                    moduleCatalogId: this.queryList.moduleCatalogId, // 模型分组ID
                    controlTypeId: this.queryList.controlTypeId, // 控件ID
                    codeLength: this.queryList.codeLength, // 码表长度
                    codeTip: this.queryList.codeTip, // 码表提示信息
                    displayStyle: this.queryList.displayStyle, // 码表宽度
                    isBindSource: this.queryList.isBindSource, // 当"是"时,需要填写codeBindSource信息
                    codeBindSource: {
                        bindSourceType: this.queryList.codeBindSource.bindSourceType, // 数据字典
                        dictSource: this.queryList.codeBindSource.dictSource, // 当bindSourceType="数据字典"时需要填写
                        tableSql: this.queryList.codeBindSource.tableSql, // 当bindSourceType="数据表"时需要填写
                        refModuleId: this.queryList.codeBindSource.refModuleId, // 当bindSourceType="引用模型"时需要填写
                        showModuleCodeId: this.queryList.codeBindSource.showModuleCodeId, // 当bindSourceType="引用模型"时需要填写
                        refModuleQuery: this.queryList.refModuleQuery
                    },

                    cascadeList: this.queryList.cascadeList,
                    tableColList: this.queryList.tableColList,
                    isValidate: this.queryList.isValidate, // 当"是"时,需要填写validates信息
                    validates: tmpValidates,
                    isApprove: this.queryList.isApprove, // 当"是"时,需要填写approve信息
                    approve: {
                        approveType: this.queryList.approve.approveType, // 审核规则
                        approveTypeExpression: this.queryList.approve.approveTypeExpression // 审核备注
                    },
                    isCollect: this.queryList.isCollect, // 当"是"时,需要填写codeCollect信息
                    defaultValue: this.queryList.defaultValue,
                    addReadOnly: this.queryList.addReadOnly ? 1 : 0,
                    updateReadOnly: this.queryList.updateReadOnly ? 1 : 0,
                    codeCollect: {
                        collectType: this.queryList.codeCollect.collectType,  // 采集方式
                        collectMapperKey: this.queryList.codeCollect.collectMapperKey, // 映射Key值
                        collectFrequency: this.queryList.codeCollect.collectFrequency, // 采集频率
                        collectScriptId: this.queryList.codeCollect.collectScriptId,
                        collectFrequencyExpression: this.queryList.codeCollect.collectFrequencyExpression // collectFrequency="正则表达式"时,需要填写
                    }
                }
                if (this.title === '新增码表') {
                    tableManagement.getSaveCodeList(queryListS).then(data => {
                        if (data.flag === 'error') {
                            this.$message.error(data.msg)
                        } else {
                            this.$message.success('新增成功')
                        }
                    })
                } else if (this.title === '修改码表') {
                    tableManagement.getUpdateCodeList(queryListS).then(data => {
                        if (data.flag === 'error') {
                            this.$message.error(data.msg)
                        } else {
                            this.$message.success('修改成功')
                        }
                    })
                }
            },
            // 取消
            resetDialog() {
                this.$refs['rulesFrom'].resetFields()
                this.$emit('setEditDisplay', false)
            }
        }
    }
</script>
<style lang="scss" scoped>
    .ctextareaS {
        width: 110px !important;
    }
    .ctextarea {
        width: 510px !important;
    }
    .el-row {
        margin-bottom: 5px !important;
    }
    .el-form-item {
        // margin-bottom: 0px !important;
    }
    .el-dialog__body {
        padding-top: 5px !important;
        padding-left: 50px !important;
        padding-right: 50px !important;
        padding-bottom: 20px !important;
    }
    .el-input {
        width: 193px !important;
    }
    /deep/ .sql-box {
        .el-form-item__content {
            width: 79%;
        }
    }
    /deep/ .yw-dialog {
        .yw-dialog-main {
            min-height: 100px;
            max-height: 410px;
            overflow-y: auto;
            overflow-x: hidden;
        }
    }
    .diaTitle {
        // background: #DEE9FC;
        height: 30px;
        line-height: 30px;
        padding-left: 10px;
        color: #000;
    }
    .check_regular {
        width: unset;
        margin-top: 10px;
    }
    .codeTableClass {
        background: #3092dc !important;
    }
</style>
<style lang="stylus" scoped >
    div.el-select {
        width: 193px !important;
    }

    .el-check .el-form-item__label {
        width: 80px !important;
    }
</style>
<style lang="stylus" >
    .el-check .el-form-item__label {
        width: 80px !important;
    }

    .code_table_width {
        cursor: pointer;
        display: flex;
        width: 193px !important;
        height: 26px !important;
        border: 1px solid #DCDFE6;
        border-radius: 6px;
        margin-top: 4px;
    }

    .code_table_width p {
        width: 26.5%;
        line-height: 24px;
        text-align: center;
    }

    .code_table_width p:nth-child(2) {
        width: calc(25% - 2px);
        border-left: 1px solid #DCDFE6;
        border-right: 1px solid #DCDFE6;
    }

    .code_table_width p:nth-child(3) {
        width: calc(25% - 1px);
        border-right: 1px solid #DCDFE6;
    }
</style>
