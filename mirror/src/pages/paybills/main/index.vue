<!-- 折扣管理 -->
<template>
    <div class="ebody">
        <el-row :gutter="12"
                class="mt10">
            <el-col :span="24">
                <el-card shadow="always"
                         class='ml20 mr20'>

                    <el-form>
                        <el-form-item>
                            <div class="top-wrap">
                                <span class="tit">折扣管理</span>
                                <el-button type="success"
                                           icon="el-icon-plus"
                                           size="mini"
                                           @click="goAdd()">新增</el-button>
                            </div>
                        </el-form-item>
                        <el-form-item>
                            <el-table border
                                      stripe
                                      :data="tableData"
                                      :cell-style="{padding:'2px'}"              
                                      :header-cell-style="{background:'#ffffff',color:'#32455D',padding:'2px!important'}"
                                      style="width: 100%">
                                <el-table-column prop="type"
                                                 label="折扣类型">
                                </el-table-column>
                                <el-table-column prop="coefficient"
                                                 label="折扣系数">
                                </el-table-column>
                                <el-table-column prop="username"
                                                 label="折扣租户名称">
                                </el-table-column>
                                <el-table-column prop="business"
                                                 label="业务系统范围">
                                </el-table-column>
                                <el-table-column prop="pool"
                                                 label="资源池范围">
                                </el-table-column>
                                <el-table-column prop="address"
                                                 align="center"
                                                 label="操作">
                                    <template slot-scope="scope">
                                        <el-button type="primary"
                                                   icon="el-icon-edit"
                                                   size="mini"
                                                   round
                                                   @click="editSale(scope.row)"></el-button>
                                        <el-button type="danger"
                                                   icon="el-icon-delete"
                                                   size="mini"
                                                   round
                                                   @click="editSale(scope.row)"></el-button>
                                    </template>
                                </el-table-column>
                            </el-table>

                        </el-form-item>
                    </el-form>
                </el-card>
            </el-col>
        </el-row>

        <el-dialog class="price-dialog yw-dialog"
                   :visible.sync="editDialogShow"
                   :show-close="false"
                   width="500"
                   center>
                    <div class="sale-edit">
                        <el-form>
                            <el-form-item>
                                <div class="edit-tit">
                                    <span>折扣A</span>
                                    <div class="edit-tit-box">
                                        <span>折扣系数设置</span>
                                        <el-input placeholder="输入0~1范围"
                                                :min="0"
                                                :max="1"></el-input>
                                    </div>
                                </div>
                            </el-form-item>
                            <el-form-item>
                                <el-row :gutter="12"
                                        class="ecard">
                                    <el-col :span="24">
                                        <el-card>
                                            <el-form>
                                                <el-form-item>
                                                    <p>折扣租户名称</p>
                                                    <div class="box">
                                                        <el-tag :key="tag"
                                                                v-for="tag in dynamicTags"
                                                                closable
                                                                size="mini"
                                                                :disable-transitions="false"
                                                                @close="handleClose(tag)">
                                                            {{tag}}
                                                        </el-tag>
                                                        <el-button class="button-new-tag"
                                                                size="mini"
                                                                @click="dialogBtn">+ 添加</el-button>
                                                    </div>
                                                </el-form-item>
                                                <el-form-item >
                                                    <p>业务系统范围</p>
                                                    <div class="box">
                                                        <el-tag :key="tag"
                                                                v-for="tag in dynamicTags"
                                                                closable
                                                                size="mini"
                                                                :disable-transitions="false"
                                                                @close="handleClose(tag)">
                                                            {{tag}}
                                                        </el-tag>
                                                        <el-button class="button-new-tag"
                                                                size="mini"
                                                                @click="dialogBtn">+ 添加</el-button>
                                                    </div>
                                                </el-form-item>
                                                <el-form-item >
                                                    <p>资源池范围</p>
                                                    <el-checkbox-group v-model="checkboxGroup2"
                                                                    size="mini">
                                                        <el-checkbox label="备选项1"
                                                                    border></el-checkbox>
                                                        <el-checkbox label="备选项2"
                                                                    border></el-checkbox>
                                                    </el-checkbox-group>
                                                </el-form-item>
                                            </el-form>
                                        </el-card>
                                    </el-col>
                                </el-row>
                            </el-form-item>
                        </el-form>
                        <el-dialog :visible.sync="dialogShow"
                                class="yw-dialog sale-dialog"
                                width="500"
                                :append-to-body="true"
                                center>
                            <span slot="title"
                                class="dialog-header">
                                <div class="dialog-tit">
                                    <i class="el-icon-office-building">选择折扣租户</i>
                                </div>
                                <div>
                                    <el-button @click="dialogShow = false">全选</el-button>
                                    <el-button type="primary"
                                            @click="dialogShow = false,editDialogShow=false">提交</el-button>
                                </div>
                            </span>
                            <el-form>
                                <el-form-item>
                                    <p>住宿</p>
                                    <div>
                                        <el-checkbox-group v-model="dynamicTags">
                                            <el-checkbox label="标签阿萨一"></el-checkbox>
                                            <el-checkbox label="标签二"></el-checkbox>
                                            <el-checkbox label="标签三"></el-checkbox>
                                        </el-checkbox-group>
                                    </div>
                                </el-form-item>
                            </el-form>
                        </el-dialog>

                    </div>
                   <span slot="footer"
                  class="dialog-footer">
                <el-button @click="editDialogShow = false">取 消</el-button>
                <el-button type="primary"
                           @click="editDialogShow = false">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        data() {
            const item = {
                type:'折扣A',
                coefficient: '0.9',
                username: '苏州研发中心',
                business: '统一待办系统',
                pool:'呼和浩特资源池'
            }
            return {
                editDialogShow:false,
                tableData: Array(10).fill(item),
                // 编辑页面参数
                dynamicTags: ['标签阿萨一', '标签二', '标签三'],
                inputVisible: false,
                inputValue: '',
                checkboxGroup2: [],
                dialogShow: false
            }
        },

        methods: {
            editBtn() { },
            goAdd(){
                
            },
            editSale() {
                this.editDialogShow=true
            },
            
            // 编辑页面函数
            handleClose(tag) {
                this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1)
            },
            handleInputConfirm() {
                let inputValue = this.inputValue
                if (inputValue) {
                    this.dynamicTags.push(inputValue)
                }
                this.inputVisible = false
                this.inputValue = ''
            },
            dialogBtn() {
                this.dialogShow = true
            }
            // 编辑页面函数
        }
    }

</script>
<style lang='scss' scoped>
.ebody{
    .top-wrap {
        display: flex;
        justify-content: space-between;
        line-height: 20px;

        .tit{
            font-weight: 600;
            color: #32455d;
        }
    }
    .price-dialog{
        .sale-edit {
            .edit-tit {
                width: 300px;
                margin: 10px 0px 0px 30px;
                display: flex;
                justify-content: space-between;
                font-weight: bold;
                color: #40546c;

                span {
                    width: 180px;
                }

                .edit-tit-box {
                    display: flex;
                    justify-content: space-between;
                }
            }
            .ecard{
                padding: 0px 10px 0px 10px;
            }

            .box {
                height: 90px;
                border: 1px solid #eee;
                padding: 10px;

                .box-item {
                    padding: 0 10px;
                    background-color: #eee;
                    font-size: 12px;
                }

                .el-tag + .el-tag {
                    margin-left: 10px;
                }
                .button-new-tag {
                    margin-left: 10px;
                    padding-top: 10;
                    padding-bottom: 0;
                }
                .input-new-tag {
                    width: 90px;
                    margin-left: 10px;
                    vertical-align: bottom;
                }
            }

            .btn-wrap {
                display: flex;
                justify-content: center;
            }

            .sale-dialog {
                .dialog-header {
                    display: flex;
                    justify-content: space-between;
                    padding-right: 30px;
                }

                .el-checkbox-group {
                    padding-left: 38px;

                    .el-checkbox {
                        width: 140px;
                    }
                }
            }
        }

    }
}
    

.el-button--mini{
    color: #fff;
    padding:5px 5px!important;
}
</style>