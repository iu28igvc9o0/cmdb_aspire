<!-- 单价管理 -->
<template>
    <div>
        <el-container class="econtainer">
            <el-main>
                <el-form>
                    <el-form-item>
                        <span class="tit">单价管理</span>
                        <span style="float:right;padding-left:5px">
                            <el-button type="primary"
                                        @click="addPrice()">新增</el-button>
                        </span>
                        <!-- <span style="float:right">
                            <el-select v-model="searchIdcKey" placeholder="选择资源池"  clearable>
                                <el-option
                                v-for="item in idcList"
                                :key="item.idcId"
                                :label="item.idcName"
                                :value="item.idcId">
                                </el-option>
                            </el-select>
                            <el-button type="primary"
                                        @click="searchChge(searchIdcKey)">查询</el-button>
                        </span> -->
                    </el-form-item>
                    <el-form-item>
                        <el-table border
                                      stripe
                                      :data="tableData.slice((pageNum - 1) * pageSize, pageNum * pageSize)"
                                      height="calc(100vh - 290px)"
                                      v-loading="loading"
                                      class="yw-el-table"
                                      :cell-style="{padding:'2px'}"      
                                      :header-cell-style="{background:'#ffffff',color:'#32455D',padding:'1px!important'}"
                                      style="width: 100%">
                                <!-- <el-table-column prop="id"
                                                 align="center"
                                                 label="id">
                                </el-table-column> -->
                                <el-table-column prop="deviceTypeName"
                                                 align="center"
                                                 label="产品名称">
                                </el-table-column>
                                <el-table-column prop="idcName"
                                                 align="center"
                                                 label="资源池名称">
                                </el-table-column>
                                <el-table-column prop="price"
                                                 align="center"
                                                 label="单价">
                                </el-table-column>
                                <el-table-column prop="unit"
                                                 align="center"
                                                 label="单位">
                                </el-table-column>
                                <el-table-column 
                                                 align="center"
                                                 label="操作">
                                    <template slot-scope="scope">
                                        <el-button type="text"
                                                title="编辑"
                                                icon="el-icon-edit"
                                                @click="editPrice(scope.row)"></el-button>
                                        <el-button
                                            type="text"
                                            title="删除"
                                            slot="reference"
                                            icon="el-icon-delete" @click="deletePrice(scope.row)"></el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <el-pagination
                                align="right"
                                background
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                @prev-click="prevClick"
                                @next-click="nextClick"
                                :current-page="pageNum"
                                :page-sizes="[10, 20, 30, 40]"
                                :page-size="pageSize"
                                :total="this.tableData.length"
                                layout="total, sizes, prev, pager, next">
                                >
                            </el-pagination>

                    </el-form-item>
                </el-form>
            </el-main>
        </el-container>
        <!-- 新增单价 -->
        <el-dialog class="yw-dialog price-dialog"
                   title="单价管理"
                   :visible.sync="addDialogShow"
                   :show-close="false"
                   width="500"
                   center>
            <el-form :model="addForm" ref="addForm">
                <el-form-item label="产品名称"
                              required
                              :label-width="formLabelWidth">
                <el-select v-model="addForm.deviceTypeId"
                    placeholder="请选择"
                    clearable>
                    <el-option v-for="item in deviceTypeList"
                            :key="item.deviceTypeId"
                            :label="item.deviceTypeName"
                            :value="item.deviceTypeId"></el-option>
                </el-select>
                </el-form-item>
                <el-form-item label="资源池名称"
                              required
                              :label-width="formLabelWidth">
                <el-select v-model="addForm.idcId"
                placeholder="请选择"
                clearable>
                <el-option v-for="item in idcList"
                        :key="item.idcId"
                        :label="item.idcName"
                        :value="item.idcId"></el-option>
                </el-select>
                </el-form-item>
                <el-form-item label="单价"
                              required
                              :label-width="formLabelWidth">
                    <el-input v-model="addForm.price"
                              style="width:180px"
                              placeholder="只限输入10位以内数字"
                              maxlength="10"
                              oninput="value=value.replace(/[^\d]/g, '')"
                              autocomplete="off"></el-input>
                </el-form-item>
                
                <el-form-item label="单位"
                              required
                              :label-width="formLabelWidth">
                <el-select v-model="addForm.unit"
                placeholder="请选择"
                clearable>
                <el-option v-for="item in unitList"
                        :key="item.id"
                        :label="item.value"
                        :value="item.value"></el-option>
                </el-select>
                </el-form-item>
            </el-form> <span slot="footer"
                  class="dialog-footer">
                <el-button @click="addDialogShow = false">取 消</el-button>
                <el-button type="primary"
                           @click="addSubmit(addForm)">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 编辑单价 -->
        <el-dialog class="yw-dialog price-dialog"
                   title="单价管理"
                   :visible.sync="editDialogShow"
                   :show-close="false"
                   width="500"
                   center>
            <el-form :model="editForm" ref="editForm">
                <el-form-item label="产品名称"
                              required
                              :label-width="formLabelWidth">
                <el-select v-model="editForm.deviceTypeId"
                    placeholder="请选择"
                    clearable>
                    <el-option v-for="item in deviceTypeList"
                            :key="item.deviceTypeId"
                            :label="item.deviceTypeName"
                            :value="item.deviceTypeId"></el-option>
                </el-select>
                </el-form-item>
                <el-form-item label="资源池名称"
                              required
                              :label-width="formLabelWidth">
                <el-select v-model="editForm.idcId"
                placeholder="请选择"
                clearable>
                <el-option v-for="item in idcList"
                        :key="item.idcId"
                        :label="item.idcName"
                        :value="item.idcId"></el-option>
                </el-select>
                </el-form-item>
                <el-form-item label="单价"
                              required
                              :label-width="formLabelWidth">
                    <el-input v-model="editForm.price"
                              style="width:180px"
                              placeholder="只限输入10位以内数字"
                              maxlength="10"
                              @input="editForm.price=editForm.price.replace(/[^\d]/g, '')"
                              autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="单位"
                              required
                              :label-width="formLabelWidth">
                <el-select v-model="editForm.unit"
                placeholder="请选择"
                clearable>
                <el-option v-for="item in unitList"
                        :key="item.id"
                        :label="item.value"
                        :value="item.value"></el-option>
                </el-select>
                </el-form-item>
            </el-form> <span slot="footer"
                  class="dialog-footer">
                <el-button @click="editDialogShow = false">取 消</el-button>
                <el-button type="primary"
                           @click="editSubmit(editForm)">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                pageSize:10,
                pageNum:1,
                tableData: [],
                loading:false,
                unitList:[],
                editForm: {
                    id:'',
                    deviceTypeId:'',
                    idcId:'',
                    price:null,
                    unit:''
                },
                addForm: {
                    deviceTypeId:'',
                    idcId:'',
                    price:null,
                    unit:''
                },
                // rules:{ deviceTypeId:[{required: true, message: '请选择产品类型', trigger: 'change'}],
                //         idcId:[{required: true, message: '请选择资源池类型', trigger: 'change'}],
                //         price:[{ required: true, message: '请输入单价', trigger: 'blur' }]},//  验证是否为空
                editDialogShow: false,
                addDialogShow:false,//  新增单价页面
                isNormal:true,//  验证表单
                formLabelWidth: '200px',
                idcList:[],//  总的资源池列表
                deviceTypeList:[],//  总的设备类型列表
                searchIdcKey:'',//  查询key
            }
        },

        methods: {
            // // 表格界面函数
            handleSizeChange(val) {
                this.pageSize=val
            },
            handleCurrentChange(val) {
                this.pageNum=val
            },
            prevClick(val){
                console.log(val)
            },
            nextClick(val){
                console.log(val)
            },
            // 搜索选中值
            searchChge(val){
                this.getPriceByKey(val)
                console.log(val)
            },
             // 获得单价列表
            getPriceList() {
                this.loading = true
                return this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/bill/price/list',
                    params: {
                    },
                }).then((res) => {
                    this.tableData=res
                    this.loading = false
                    return res
                })
            },
            // 获得单价单位列表
            getUnitList(){
                return this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/bill/price/unit',
                    params: {
                    },
                }).then((res) => {
                    this.unitList = res
                    return res
                })
            },
            // 删除
            deletePrice(row){
                this.$confirm('确认删除？').then(() => {
                        this.rbHttp.sendRequest({
                        method: 'DELETE',
                        url: '/v1/cmdb/bill/price',
                        params:{id:row.id}
                    }).then((res) => {
                        console.log(res)
                        if (res) {
                            this.$message.success('删除成功！')
                            this.getPriceList()
                        } else {
                            this.$message.error(res.message)
                        }
                    })
                })
                
            },
            addPrice(){
                this.addDialogShow = true
            },
            addSubmit(addForm){
                // 验证是否为空
                this.isNormal = true
                for(let i in addForm){
                    if(addForm[i]==null || addForm[i]==''){
                        this.isNormal = false
                        this.$notify.error('表单项不能为空')
                        return
                    } 
                }
                if(this.isNormal){
                    this.rbHttp.sendRequest({
                            method: 'POST',
                            url: '/v1/cmdb/bill/price',
                            params: addForm
                        }).then((res) => {
                            if(res.success){
                                this.addForm = {deviceTypeId:'',idcId:'',price:null},
                                this.getPriceList()
                                this.addDialogShow = false
                                this.$message({
                                    message: '添加成功!',
                                    type: 'success'
                                })
                            } else { this.$message.error(res.error) }
                        }).catch(()=>{this.$message.error('添加失败,请检查表单项')})
                }
            },
            // 编辑
            editPrice(row) {
                this.editForm.id = row.id
                this.editForm.deviceTypeId = row.deviceTypeId
                this.editForm.idcId = row.idcId
                this.editForm.price = row.price
                this.editForm.unit = row.unit
                this.editDialogShow = true
            },
            // 提交
            editSubmit(editForm){
                // 验证是否为空
                this.isNormal = true
                for(let i in editForm){
                    if(editForm[i]===null || editForm[i]===''){
                        this.isNormal = false
                        this.$notify.error('单项不能为空')
                        return
                    } 
                }
                if(this.isNormal){
                    this.rbHttp.sendRequest({
                        method: 'PUT',
                        url: '/v1/cmdb/bill/price',
                        params: editForm
                    }).then((res) => {
                        if(res===1){
                            this.editForm = {id:null,deviceTypeId:'',idcId:'',price:null,unit:''},
                            this.getPriceList()
                            this.editDialogShow = false
                            this.$message({
                                message: '修改成功!',
                                type: 'success'
                            })
                        } else { this.$message.error('添加失败') }
                    }).catch((err)=>{
                        // this.$message.error(err.data.errors[0].message)})
                        this.$message.error(JSON.parse(err.data.errors[0].message).message)})
                }
            },
            // 获取资源池列表
            getIdcList(){
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/bill/price/loadIdcList'
                }).then((res) => {
                    this.idcList = res
                    console.log(res)
                })
            },
            // 获取设备类型列表
            getDeviceTypeList(){
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/bill/price/loadDeviceTypeList'
                }).then((res) => {
                    this.deviceTypeList = res
                    console.log(res)
                })
            },
            // 按条件获取单价，只入资源池
            getPriceByKey(idcId){
                if(idcId==''){this.getPriceList()}
                else{
                    this.loading = true
                    this.rbHttp.sendRequest({
                        method: 'GET',
                        url: '/v1/cmdb/bill/price',
                        params:{idcId:idcId}
                    }).then((res) => {
                        if(!res){this.tableData = []}
                            else{
                                let temp = []
                                temp.push(res)
                                this.tableData = temp//  转为数组
                            }
                        console.log(res)
                        this.loading = false
                    })
                }
            }
        },
        mounted(){
            this.getPriceList()
            this.getIdcList()
            this.getDeviceTypeList()
            this.getUnitList()
        }
    }

</script>
<style lang='scss' scoped>
    .econtainer{
        // height:700px;

        .tit {
            font-size: 13px;
            font-weight: 700;
            color: #000;
        }

        .el-button--mini{
            // color: #fff;
            // padding:5px 16px!important;
            // border-radius: 5px;
        }

    }
    .price-dialog {
        .buttom-wrap {
            height: 100px;
        }

        .el-input {
            width: 200px;

            .el-input__inner {
                height: 38px;
                line-height: 38px;
            }
        }
    }

</style>