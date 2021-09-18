<template> 
    <div>
        <el-dialog class="yw-dialog"
                    :append-to-body="true" 
                    title="基本信息"
                    :visible.sync="modifyObj.visiable">
            <section class="yw-dialog-main pbottom10">
                <el-form label-width="105px"
                         class="yw-form"
                         ref="moduleRelationForm"
                         :model="formData"
                         :rules="moduleRelationRules">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="关联类型" prop="relationType">
                                <el-select placeholder="请选择"
                                           v-model="formData.relationType"
                                           filterable
                                           @change="changeRelation"
                                           :disabled="relationTypeVisable">
                                    <el-option v-for="(item, index) in moduleRelationList"
                                               :key="index"
                                               :label="item.value"
                                               :value="item.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="关联关系" prop="relation">
                                <el-select placeholder="请选择"
                                           v-model="formData.relation"
                                           filterable>
                                    <el-option v-for="item in resourceRelationList"
                                               :key="item.value"
                                               :label="item.value"
                                               :value="item.id"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <!-- 模型类型 -->
                    <el-row v-if="activeModuleType == '模型'">
                        <el-col :span="12">
                            <el-form-item label="关联模型" prop="relationModuleId">
                                <Treeselect class="yw-treeselect"
                                            v-model="checkModule"
                                            :options="moduleTree"
                                            :limit="1"
                                            :clearable="false"
                                            @select="changeModule"
                                            noOptionsText="暂无数据"
                                            placeholder="请选择">
                                </Treeselect>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="关联资源名称" prop="resourceName">
                                <el-input class="ctextarea"
                                          v-model="formData.resourceName"
                                          type="text"
                                          autosize
                                          placeholder="请输入资源名称">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row v-if="activeModuleType == '模型'"> 
                        <el-row><div class="mtop5 pbottom10">字段关系映射</div></el-row>
                        <el-row v-for="(column, index) in bindColumnsTable"
                            :key="index" type="flex" align="middle">
                            <el-col :span="10">
                                <el-form-item label="字段" label-width="50px">
                                    <el-input class="ctextarea"
                                            type="text"
                                            style="width: 90px;"
                                            v-model="currentModuleName"
                                            disabled>
                                    </el-input>
                                    <el-select placeholder="请选择"
                                               filterable
                                               style="width: 100px;"
                                               v-model="bindColumnsTable[index].currCodeId">
                                        <el-option v-for="item in currentCodeList"
                                                :key="item.value"
                                                :label="item.value"
                                                :value="item.codeId"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="10">
                                <el-form-item label="关联" label-width="50px">
                                    <el-input class="ctextarea"
                                            type="text"
                                            style="width: 90px;"
                                            v-model="relationModuleName"
                                            disabled>
                                    </el-input>
                                    <el-select placeholder="请选择"
                                            filterable
                                            style="width: 100px;"
                                            v-model="bindColumnsTable[index].relationCodeId">
                                        <el-option v-for="item in relationCodeList"
                                                :key="item.value"
                                                :label="item.value"
                                                :value="item.codeId"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="2">
                                <i class="el-icon-circle-plus-outline ICON" @click="increBindCode()"
                                   style="color: #409eff; font-size:15px;cursor: pointer;"></i>
                                <i class="el-icon-remove-outline ICON"
                                   style="color: #409eff; font-size:15px;cursor: pointer;"
                                   v-show="showMouseTable(index)" @click="reduceBindCode(index)"></i>
                            </el-col>
                        </el-row>
                    </el-row>

                    <!-- 数据表类型 -->
                    <el-row v-if="activeModuleType == '数据表'">
                        <el-col>
                            <el-form-item label="关联资源名称" prop="resourceName">
                                <el-input class="ctextarea"
                                          v-model="formData.resourceName"
                                          type="text"
                                          autosize
                                          placeholder="请输入资源名称">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row v-if="activeModuleType == '数据表'"> 
                        <el-row>
                            <el-col>
                                <el-form-item label="字段" prop="multiCheckCode">
                                    <el-input class="ctextarea"
                                            type="text"
                                            v-model="currentModuleName"
                                            disabled>
                                    </el-input>
                                    <el-select placeholder="请选择字段"
                                            v-model="multiCheckCode"
                                            multiple
                                            size="small"
                                            style="width: 220px;"
                                            collapse-tags
                                            @change="checkFieldCode"
                                            filterable>
                                        <el-option v-for="item in currentCodeList"
                                                :key="item.value"
                                                :label="item.value"
                                                :value="item.codeId"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col>
                                <el-form-item label="关联表SQL" prop="relationSql">
                                    <el-input type="textarea" v-model="formData.relationSql"
                                            placeholder="请输入SQL">
                                    </el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-row>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary" @click="save">保存</el-button>
                <el-button @click="cancel">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>

    import rbConfigDictServiceFactory from '../../../../../services/cmdb/rb-configDict-service.factory'
    import rbCmdbModuleServiceFactory from '../../../../../services/cmdb/rb-cmdb-module-service.factory'
    import rbCmdbCodeServiceFactory from '../../../../../services/cmdb/rb-cmdb-code-services.factory'
    import { filterVueTree } from 'src/assets/js/utility/rb-filters.js'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'
    export default {
        name: 'ModuleReltaionModify',
        props: ['modifyObj'],
        components:{
            Treeselect: () => import('@riophae/vue-treeselect'),
        },
        data () {
            return {
                multiFieldCode: [],
                relationTypeVisable: false,   // 关联类型是否可变
                currentModuleName: '',
                relationModuleName: '',
                checkModule: null,
                bindColumnsTable:[],
                activeModuleType: '',
                multiCheckCode: [], // '数据表'模型，字段多选
                moduleRelationList: [], // 关联类型列表
                resourceRelationList:[], // 关联关系列表
                moduleTree: [], // 关联模型树
                currentCodeList: [], // 当前模型字段列表
                relationCodeList: [], // 关联模型字段列表
                formData: {
                    moduleId: null, // 当前模型ID
                    relation: null, // 关联关系
                    relationType: null,  // 关联类型
                    relationModuleId: null, // 关联模型
                    resourceName: null, // 关联资源名称
                    relationSql: null, // 关联表sql
                },
                // 检查规则
                moduleRelationRules:{
                    relation: [{ required: true, message: '请先选择关联关系!', trigger: ['blur', 'change']}],
                    relationType: [{ required: true, message: '请先选择关联类型!', trigger: ['blur', 'change']}],
                    relationModuleId: [{ required: true, message: '请先选择关联模型!', trigger: ['blur', 'change']}],
                    resourceName: [{ required: true, message: '请先输入关联资源名称!', trigger: ['blur', 'change']}],
                    relationSql: [{ validator: (rule, value, callback) => this.validRelationSql(rule, value, callback), trigger: ['blur', 'change']}],
                    multiCheckCode: [{ validator: (rule, value, callback) => this.validRequiredWithList(rule, value, callback), trigger: ['blur', 'change']}]
                }
            }
        },
        computed:{
            transData(){ return this.modifyObj.data},
            transType(){ return this.modifyObj.type}
        },
        methods: {
            // 筛选数据表多选后的fieldCode
            checkFieldCode(row){
                var fieldCodes = []
                row.forEach((codeId) => {
                    var codeObj = this.currentCodeList.filter(item => item.codeId == codeId)
                    fieldCodes.push(codeObj[0].name)
                })
                this.multiFieldCode = fieldCodes
            },
            // 验证关联sql的字段是否符合
            validRelationSql(rule, value, callback){
                if(this.activeModuleType == '模型'){
                    return callback()
                }
                var sql = this.formData.relationSql
                if(sql == null || sql == ''){
                    return callback('请先输入关联表sql!')
                }
                // 判断左右[]是否对等
                var l=0,r=0
                for(var i=0;i<sql.length;i++){
                    if(sql[i] == '[') { l++ }
                    if(sql[i] == ']') { r++ }
                }
                if(l != r){
                    return callback('sql有误请检查')
                }
                // 判断filedCode是否相同
                var list = []
                for(var k=0;k<sql.length;k++){
                    if(sql.charAt(k) == '['){
                        for(var j=k+1;j<sql.length;j++){
                            if(sql.charAt(j) == ']'){
                                list.push(sql.substring(k+1,j))
                                k = j + 1
                                break
                            }
                        }
                    }
                }
                // 判断选中的字段是否都在sql中
                list = list.sort()
                var multi = this.multiFieldCode
                multi = multi.sort()
                if(multi.toString() != list.toString()){
                    return callback('字段和sql可变参数不匹配')
                }
                return callback()
            },
            // 验证数据表类型，字段选择
            validRequiredWithList(rule, value, callback){
                if(this.multiCheckCode.length == 0){
                    return callback(new Error('至少选择一个字段')) 
                }
                return callback() 
            },
            // 判断一个[]是否有重复值
            judgeIfRepeat(list) {
                console.info('list')
                console.info(list)
                var hash = {}
                for(var i=0;i<list.length;i++) {
                    if(hash[list[i]]) {
                        return true
                    }
                    hash[list[i]] = true
                }
                return false
            },
            // 重置
            reset(){
                var formDataVo = {
                    moduleId: this.formData.moduleId,
                    relation: null,
                    relationType: null,
                    relationModuleId: null,
                    resourceName: null,
                    relationSql: null,
                }
                this.formData = formDataVo
                this.checkModule = null
                this.multiCheckCode = []
                this.bindColumnsTable = [{
                    currCodeId: null,
                    relationCodeId: null
                }]
            },
            increBindCode() {
                this.bindColumnsTable.push({
                    currCodeId: null,
                    relationCodeId: null
                })
            },
            reduceBindCode(idxTable) {
                this.bindColumnsTable.splice(idxTable, 1)
            },
            // 更改关联模型，联动码表字段
            changeModule(row){
                this.formData.resourceName = row.label
                this.formData.relationModuleId = row.id
                this.relationModuleName = row.label
                this.getCodeListById(row.id,'relationCodeList')
                this.$refs['moduleRelationForm'].validate()
                this.bindColumnsTable = [{
                    currCodeId: null,
                    relationCodeId: null
                }]
            },
            // 更改关联类型事件
            changeRelation(row){
                this.reset()
                this.moduleRelationList.forEach((item) => {
                    if(item.id === row) {
                        this.activeModuleType = item.value
                        this.formData.relationType = item.id
                    }
                })
            },
            // 保存和修改前参数格式整理
            dealPOJOFormatter(){
                this.formData.createPerson = 'luowenbo'
                if(this.activeModuleType === '模型') {
                    this.formData.codeRelationList = this.bindColumnsTable
                } else if(this.activeModuleType === '数据表') {
                    var codeList = new Array()
                    this.multiCheckCode.forEach((item) => {
                        codeList.push({
                            currCodeId: item,
                            relationCodeId: null
                        })
                    })
                    this.formData.codeRelationList = codeList
                }
            },
            // 验证模型映射字段不能为空
            validateNotNull(){
                var flag = true
                if(this.activeModuleType === '模型'){
                    var checkCodes = []
                    this.bindColumnsTable.forEach((item) => {
                        if(item.currCodeId === null || item.relationCodeId === null){
                            this.$message.warning('字段不能为空')
                            flag = false
                        }
                        checkCodes.push(item.currCodeId)
                    })
                    if(flag && this.judgeIfRepeat(checkCodes)){
                        this.$message.warning('字段不可重复')
                        flag = false
                    }
                }
                return flag
            },
            // 保存
            save(){
                this.$refs['moduleRelationForm'].validate((valid) => {
                    if (valid && this.validateNotNull()) {
                        this.dealPOJOFormatter()
                        if(this.transType === 'ADD'){
                            rbCmdbModuleServiceFactory.addModuleRelation(this.formData).then((res) => {
                                if (res.success) {
                                    this.$message.success('保存成功！')
                                    this.$emit('setCloseDialog')
                                } else {
                                    this.$message.error(res.message)
                                }
                            })
                        } else if(this.transType === 'UPDATE') {
                            rbCmdbModuleServiceFactory.updateModuleRelation(this.formData).then((res) => {
                                if (res.success) {
                                    this.$message.success('修改成功！')
                                    this.$emit('setCloseDialog')
                                } else {
                                    this.$message.error(res.message)
                                }
                            })
                        }
                    } else {
                        console.error('校验表单失败！')
                    }
                })
            },
            cancel(){
                this.$emit('setCloseDialog')
            },
            // 获取下拉框数据——关联关系
            getResourceRelationList(){
                var param = {
                    type: 'resource_relation_type'
                }
                rbConfigDictServiceFactory.getDictsByType(param).then((res) => {
                    this.resourceRelationList = res
                })
            },
            // 获取下拉框数据——关联类型
            getModuleRelationList(){
                var param = {
                    type: 'module_relation_type'
                }
                rbConfigDictServiceFactory.getDictsByType(param).then((res) => {
                    this.moduleRelationList = res
                })
            },
            // 获取当前模型的码表字段
            getCodeListById(moduleId,key){
                rbCmdbCodeServiceFactory.getCodeByModuleId(moduleId).then((res) => {
                    const codeList = new Array()
                    res.forEach((item) => {
                        codeList.push({
                            codeId: item.codeId,
                            name: item.filedCode,
                            value: item.filedName
                        })
                    })
                    this[key] = codeList
                })
            },
            // 获取下拉框数据——模型树
            getModuleTree(){
                rbCmdbModuleServiceFactory.getModuleTree({}).then((data) => {
                    var moduleTree = filterVueTree(data, {
                        id: 'id',
                        label: 'name',
                        children: 'childModules',
                        isDisabled: (item) => this.filterDisabled(item),
                        isDisplay: 'isDisplay',
                        autoAddRoot: true
                    })
                    this.moduleTree = moduleTree
                }).catch((e) => {
                    console.error(e)
                })
            },
            filterDisabled(item) {
                // if (item.isDisplay && item.isDisplay === 1) {
                //     return true
                // }
                if(item.id === this.formData.moduleId) {
                    return true
                }
                return false
            },
            showMouseTable(idxTable){
                return (this.bindColumnsTable.length > 1 && idxTable === 0) || idxTable > 0
            }
        },
        mounted: function () {
            this.getResourceRelationList()
            this.getModuleRelationList()
            this.getModuleTree()
            if(this.transType === 'ADD') {
                // 当前模型ID
                this.formData.moduleId =  this.transData.id
                // 当前模型名称
                this.currentModuleName = this.transData.name
                this.getCodeListById(this.transData.id,'currentCodeList')
                this.bindColumnsTable = [{
                    currCodeId: null,
                    relationCodeId: null
                }]
            } else if(this.transType === 'UPDATE') {
                // 当修改时，关联类型无法更改
                this.relationTypeVisable = true
                const _data = this.transData
                this.activeModuleType = _data.relationTypeDict.dictNote
                this.getCodeListById(_data.curModule.id,'currentCodeList')
                // 当前模型名称
                this.currentModuleName = _data.curModule.name
                var formDataVo = {
                    moduleId: _data.curModule.id,
                    relation: _data.relationDict.dictId,
                    relationType: _data.relationTypeDict.dictId,
                    relationModuleId: null,
                    resourceName: _data.resourceName,
                    relationSql: _data.relationSql
                }
                this.formData = formDataVo
                // 编辑的关联模型关系ID
                this.formData.id = this.transData.id
                // 处理 模型 以及 数据表
                var codeRelationList = _data.codeRelationList
                if(_data.relationTypeDict.dictNote === '模型') {
                    this.getCodeListById(_data.relationModule.id,'relationCodeList')
                    // 模型有关联模型
                    this.formData.relationModuleId = _data.relationModule.id
                    this.checkModule = _data.relationModule.id
                    this.relationModuleName = _data.relationModule.name
                    var bindColumnsTable = []
                    codeRelationList.forEach((item) => {
                        bindColumnsTable.push({
                            currCodeId: item.currCode.codeId,
                            relationCodeId: item.relationCode.codeId
                        })
                    })
                    this.bindColumnsTable = bindColumnsTable
                } else if(_data.relationTypeDict.dictNote === '数据表') {
                    var multiCheckCode = []
                    var feildCodes = []
                    codeRelationList.forEach((item) => {
                        multiCheckCode.push(item.currCode.codeId)
                        feildCodes.push(item.currCode.filedCode)
                    })
                    this.multiCheckCode = multiCheckCode
                    this.multiFieldCode = feildCodes
                }
            }
        }
    }
</script>

<style scoped>
</style>
