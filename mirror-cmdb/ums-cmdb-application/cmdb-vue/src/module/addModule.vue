<template>
    <div id="addmodule">
        <div style="padding:20px 0px 20px 0px;text-align:center;">创建模型</div>
        <el-form :model="formmodule" :rules="formRules" ref="formmodule" label-width="85px">
            <el-form-item label="选择分组:" prop="parentId" required>
                <el-select v-model="formmodule.parentId" placeholder="请选择模型所属分组:">
                    <el-option v-for="option in moduleoptions" :label="option.name" :value="option.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="类型名称:" prop="name" required>
                <el-input v-model="formmodule.name" placeholder="请输入类型名称（中、英文）"></el-input>
            </el-form-item>
            <el-form-item label="类型编号:" prop="code" required>
                <el-input v-model="formmodule.code" placeholder="请输入类型编号（数字、字母、下划线）"></el-input>
            </el-form-item>
            <el-form-item label="模型标签:">
                <el-tag :key="tag" v-for="tag in moduletags" :closable="true" :close-transition="false" @close="handleCloseTag(tag)">{{tag}}</el-tag>
                <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="mini" @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
                </el-input>
                <el-button v-else class="button-new-tag" size="small" @click="showInput">+ 点击新增</el-button>
            </el-form-item>
            <el-form-item label="模型图标:" prop="iconurl" required>
                <div class="btnIcon" @click="selectIcon" v-model="formmodule.iconurl"><span :class="{dis:icondisplay}">选择图标</span>
                    <div :class="{dis:!icondisplay}" style="background:#1d6aa7;border-radius:3px;">
                        <img width="30" height="30" :src="formmodule.iconurl" />
                        <div style="position: absolute;padding: 0 7px;height:20px;line-height:20px;top:43px;">修改图标</div>
                    </div>
                </div>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit('formmodule')">下一步</el-button>
                <el-button @click="resetForm('formmodule')">重置</el-button>
            </el-form-item>
        </el-form>
        <el-dialog :visible.sync="iconDialog" class="iconDialog" @close="iconDialogClose">
            <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="系统图标库" name="sysicon">
                    <div class="sysicon" v-model="sysicons">
                        <li v-for="(item,index) in sysicons" :key="index" :class="{liactive:activeIcon==item.id}" class="iconlist" v-on:click="iconClick(item)">
                        <img width="30" height="30" :src="item.iconUrl" />
                        <i :class="{active:activeIcon==item.id,nactive:activeIcon!=item.id}" class="fa fa-check-circle"></i></li>
                        <!--解决浮动之后无法撑开外层div的问题-->
                        <div style="clear:both;"></div>
                    </div>
                    <el-pagination style="clear:both;margin-top:20px;"
                      :page-size="cuspage.pageSize"
                      @current-change="sysCurrentChange"
                      :current-page.sync="syspage.currPage"
                      layout="prev, pager, next, jumper"
                      :total="syspage.total">
                    </el-pagination>
                    <div style="text-align:center;margin-top:20px;">
                        <el-button type="primary" @click="iconcommit" :disabled="!btn_active">确定</el-button>
                        <el-button @click="iconCancel">取消</el-button>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="自定义图标库" name="cusicon">
                    <div class="uploadicon">
                        <el-upload class="upload-demo" action="/cmdb/icon/uploadIcon" :on-error="uploadError" :on-success="uploadSuccess" accept="image/jpeg,image/png">
                            <el-button size="small" type="primary">上传图标</el-button>
                            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过200kb</div>
                        </el-upload>
                    </div>
                    <div class="sysicon" v-model="customicons">
                        <li v-for="item in customicons" :key="item.id" :class="{liactive:activeIcon==item.id}" class="iconlist" v-on:click="iconClick(item)">
                        <img width="30" height="30" :src="item.iconUrl"/>
                        <i :class="{active:activeIcon==item.id,nactive:activeIcon!=item.id}" class="fa fa-check-circle"></i></li>
                        <!--解决浮动之后无法撑开外层div的问题-->
                        <div style="clear:both;"></div>
                    </div>
                    
                    <el-pagination style="clear:both;margin-top:20px"
                      :page-size="cuspage.pageSize"
                      @current-change="cusCurrentChange"
                      :current-page.sync="cuspage.currPage"
                      layout="prev, pager, next, jumper"
                      :total="cuspage.total">
                    </el-pagination>
                    <div style="text-align:center;margin-top:20px;">
                        <el-button type="primary" @click="iconcommit" :disabled="!btn_active">确定</el-button>
                        <el-button @click="iconCancel">取消</el-button>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </el-dialog>
    </div>
</template>
<script>
export default {
    data() {
        var checkName = (rule, val, callback) => {
            $.ajax({
                url: '/cmdb/module/getModuleSelective',
                type: 'POST',
                dataType: 'json',
                data: { "name": $.trim(val) }
            }).done(function(data) {
                if (data.length > 0) {
                    return callback(new Error('抱歉，该类型名称已被占用!'));
                }
                callback();
            });
        };
        var checkCode = (rule, val, callback) => {
            var reg = /^[a-zA-Z]+.*$/;
            if (!reg.test($.trim(val))) {
                return callback(new Error('编号首字符必须为字母'));
            }
            $.ajax({
                url: '/cmdb/module/getModuleSelective',
                type: 'POST',
                dataType: 'json',
                data: { "code": $.trim(val) }
            }).done(function(data) {
                if (data.length > 0) {
                    return callback(new Error('抱歉，该类型编号已被占用!'));
                }
                callback();
            });
        };
        var checkIconUrl = (rule, val, callback) => {
            if (this.formmodule.iconurl == "") {
                return callback(new Error('请选择图标！'));
            }
            callback();
        };
        return {
            formmodule: {
                parentId: '',
                name: '',
                code: '',
                iconurl: '',
                tag:'',
            },
            defaultformmodule:{
                parentId: '',
                name: '',
                code: '',
                iconurl: '',
            },
            moduleoptions: [],
            activeName: 'sysicon',
            iconDialog: false,
            syspage: {
                currPage: 1,
                pageSize: 27,
                total: 0,
            },
            cuspage: {
                currPage: 1,
                pageSize: 27,
                total: 0,
            },
            moduletags: [],
            inputVisible: false,
            inputValue: '',//标签域输入值
            beforeValue:'',//记录标签域上次输入值，以便校验
            sysicons: [], //系统图标库
            customicons: [], //自定义图标库
            currSelectIcon: "", //当前选中的图标
            icondisplay: false,
            formRules: {
                parentId: [{
                    required: true,
                    message: '请选择模型所属分组',
                    trigger: 'change'
                }],
                name: [{
                    required: true,
                    message: '请输入类型名称',
                    trigger: 'blur'
                }, {
                    min: 1,
                    max: 16,
                    message: '长度在 1 到 16 个字符',
                    trigger: 'blur',
                }, {
                    validator: checkName,
                    trigger: 'blur'
                }],
                code: [{
                    required: true,
                    message: '请输入类型编号',
                    trigger: 'blur'
                }, {
                    min: 1,
                    max: 16,
                    message: '长度在 1 到 16 个字符',
                    trigger: 'blur',
                },{
                    validator: checkCode,
                    trigger: 'blur'
                }],
                iconurl: [{
                    validator: checkIconUrl,
                    trigger: 'change'
                }]
            },
            activeIcon:'',
            btn_active:false,
        }
    },
    mounted: function() {
        this.getParentModule();
    },
    methods: {
        onSubmit() {
            console.log('submit!');
        },
        getParentModule() {
            $.ajax({
                url: '/cmdb/module/getModule/',
                type: 'POST',
                dataType: 'json',
            }).done(function(data) {
                this.moduleoptions = data;
            }.bind(this));
        },
        selectIcon() {
            this.iconDialog = true;
            this.getSysIcons();
            this.getCustomIcons();
        },
        //获取系统图标
        getSysIcons() {

            $.ajax({
                url: '/cmdb/icon/getIcons',
                type: 'POST',
                data: {
                    "pageNumber": this.syspage.currPage,
                    "pageSize": this.syspage.pageSize,
                    "iconcategory": 0, //获取系统图标
                },
                dataType: 'json',
            }).done(function(data) {
                debugger
                this.sysicons = data.dataList;
                this.syspage.currPage = data.pageNo;
                this.syspage.total = data.total;
            }.bind(this));
        },
        sysCurrentChange(val) {
            this.syspage.currPage = val;
            this.getSysIcons();
        },
        cusCurrentChange(val) {
            this.cuspage.currPage = val;
            this.getCustomIcons();
        },
        //获取自定义图标
        getCustomIcons() {
            $.ajax({
                url: '/cmdb/icon/getIcons',
                type: 'POST',
                data: {
                    "pageNumber": this.cuspage.currPage,
                    "pageSize": this.cuspage.pageSize,
                    "iconcategory": 1, //获取自定义图标
                },
                dataType: 'json',
            }).done(function(data) {
                this.customicons = data.dataList;
                this.cuspage.currPage = data.pageNo;
                this.cuspage.total = data.total;
            }.bind(this));
        },
        handleCloseTag(tag) {
            this.moduletags.splice(this.moduletags.indexOf(tag), 1);
        },
        showInput() {
            this.inputVisible = true;
            this.$nextTick(_ => {
                this.$refs.saveTagInput.$refs.input.focus();
            });
        },
        handleInputConfirm() {
            
            let inputValue = this.inputValue;
            if(this.moduletags.length>0 && this.moduletags.indexOf(inputValue)!=-1){
                this.$notify({
                    title: '提示',
                    message: '标签名称已经存在',
                    type: 'warning',
                    duration: 3000
                });
                return;
            }
            if($.trim(inputValue).length>20){
                this.$notify({
                    title: '提示',
                    message: '标签名称必须小于20个字符',
                    type: 'warning',
                    duration: 3000
                });
                return;
            }
            if (inputValue) {
                this.moduletags.push(inputValue);
            }
            this.inputVisible = false;
            this.inputValue = '';
        },
        uploadError(err, file, fileList) {
            this.$notify({
                title: '提示',
                message: '图标上传失败',
                type: 'error',
                duration: 3000
            });
        },
        uploadSuccess(response, file, fileList){
            if(response.success){
                this.getCustomIcons();
                this.$notify({
                    title: '提示',
                    message: '图标上传成功',
                    type: 'success',
                    duration: 3000
                });
            }else{
                this.$notify({
                    title: '提示',
                    message: '图标上传失败',
                    type: 'error',
                    duration: 3000
                });
            }
        },
        handleClick() {

        },
        iconClick(item) {
            this.activeIcon = item.id;
            this.currSelectIcon = item.iconUrl;
            this.btn_active=true;
        },
        //确定选择的图标
        iconcommit() {
            this.formmodule.iconurl = this.currSelectIcon;
            this.iconDialog = false;
            this.icondisplay = true;
        },
        //图标选择Dialog 取消按钮
        iconCancel() {
            this.iconDialog = false;
        },
        //iconDialog关闭时的
        iconDialogClose(){
            this.currSelectIcon = "";
            this.syspage.currPage = 1;
            this.cuspage.currPage = 1;
            this.activeIcon = null;
            this.btn_active = false;
        },
        //提交表单数据
        onSubmit(formName) {
            var data = {
                "name": this.formmodule.name,
                "code": this.formmodule.code,
                "parentId": this.formmodule.parentId,
                "iconurl": this.formmodule.iconurl,
                "tags":this.moduletags,
            }
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    jQuery.ajax({
                        url: '/cmdb/module/addModule',
                        type: "POST",
                        cache: false,
                        async: true,
                        traditional: true,
                        data: JSON.stringify(data),
                        contentType: "application/json; charset=utf-8",
                        success: function(json, textStatus) {
                            if (json.success == true) {
                                this.$notify({
                                    title: '提示',
                                    message: '创建成功',
                                    type: 'success',
                                    duration: 3000
                                });
                                //重置表单
                                this.resetForm('formmodule');
                                //向父组件传递参数
                                this.$emit('addModuleData', [json.module]);
                            } else {
                                this.$notify({
                                    title: '提示',
                                    message: json.message,
                                    type: 'error',
                                    duration: 3000
                                });
                            }

                        }.bind(this),
                        error: function(XMLHttpRequest, textStatus, errorThrown) {
                            this.$notify({
                                title: '提示',
                                message: '添加失败',
                                type: 'error',
                                duration: 3000
                            });
                        }.bind(this),
                    });
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });

        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
            this.icondisplay = false;
            this.currSelectIcon = "";
            this.moduletags = [];
        },
        /*checkIconUrl(rule, val, callback){
            console.log(this.formmodule.iconurl);
            if (this.formmodule.iconurl == "") {
                return callback(new Error('请选择图标！'));
            }
            callback();
        }*/
    }
}

</script>
<style>
#addmodule {
    width: 400px;
    margin: 0 auto;
}
#addmodule .el-tag{margin-left: 10px;}
.input-new-tag{
    width:78px;margin-left:10px;vertical-align:bottom;
    
}
.button-new-tag{
    margin-left:10px;height:24px;line-height:22px;padding-top:0;padding-bottom:0;
}
.dis {
    display: none;
}

.btnIcon {
    width: 60px;
    height: 60px;
    border: 1px dashed #d1dbe5;
    font-size: 12px;
    color: #48576a;
    line-height: 60px;
    text-align: center;
    border-radius: 2px;
    cursor: pointer;
}
.sysicon .active{
    display: block;
    color: #6adc71;
    float: right;
}
.sysicon .liactive{
    background: #1681c4;
    border-radius: 10px;
}
.sysicon .nactive{
    display: none;
}
.iconDialog .el-dialog__body {}

.iconDialog .el-tabs {}

.sysicon {
    background-color: #1d6aa7;
    border-radius: 5px;
    cursor: pointer;
    padding:0px 0px 10px 0px;
}


.sysicon li:hover {
    border-radius: 10px;
    background: #1681c4;;
}

.sysicon .iconlist {
    padding: 10px;
    float: left;
    width: 40px;
    height:40px;
    list-style-type: none;
    margin: 10px 0px 0px 10px;
}
.el-upload-list{
    
}

</style>
