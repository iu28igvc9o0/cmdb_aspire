<template>
<div class="container">
<el-row  type="flex" class="row-bg" justify="end">
<el-button icon="close"  size="mini" @click="closeDialog"></el-button>
</el-row>
<el-row :gutter="20">
<el-col :span="12">
         <div style="padding:20px 0px 20px 0px;text-align:center;"><h4>编辑圈子</h4></div>
    <el-form :model="ruleForm" ref="ruleForm" :rules="rules" label-width="120px"  class="demo-ruleForm" style="white-space:nowrap;">
        <el-form-item>
        </el-form-item>
        
        <el-form-item label="名称"    prop="name" required>
            <el-input placeholder="请输入内容" v-model="ruleForm.name" ></el-input> 
        </el-form-item>
	        
	    <el-form-item>
        </el-form-item>
	        
        <el-form-item label="描述"  prop="dec">
            <el-input   v-model="ruleForm.dec"></el-input>
        </el-form-item>
        
        <el-form-item>
        </el-form-item>
        
        <el-form-item align="center" style="margin-top:15px;">
            <el-button type="primary" @click="onSubmit" >保存</el-button>
            <el-button  @click="closeDialog">取消</el-button>
            <el-button type="primary" @click="closeCircle" >关闭圈子</el-button>
        </el-form-item>
    </el-form>
 </el-col>
    
 <el-col :span="1" :offset="5">
 </el-col>
</el-row>
<el-dialog title="" v-if="isClose" v-model="isClose"  size="small" @close="addClose">
<closeMaintain ref="closeMaintain" :circleId="circleId" v-on:childBindAdd="closeDialog"></closeMaintain>
</el-dialog>
</div>

</template>
<style>
</style>
<script>
import router from '../router'
import closeMaintain from './closeMaintain.vue';
export
default {
    components: {
    	closeMaintain
    },
    data() {
            var validateName = (rule, value, callback) => {
                debugger;
                if (value === '') {
                    callback(new Error('请输入名称'));
                } else {
                    var goHeadUrl = "/cmdb/circle/checkName";
                    $.ajax({
                        url: goHeadUrl,
                        type: "POST",
                        data: {
                            'name': value
                        },
                        dataType: "json",
                        async: false,
                        success: function (json, textStatus) {
                            debugger;
                            if (json.success == true) {} else {
                                if (this.ruleForm.name != this.nativename) {
                                    callback(new Error('名称已存在'));
                                }
                            }
                        }.bind(this),
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            debugger;
                            callback(new Error('请求失败'));

                        }.bind(this)
                    });
                    callback();
                }
            };

            return {
                ruleForm: {},

                rules: {
                    name: [{
                        validator: validateName,
                        trigger: 'blur'
                    }, {
                        min: 2,max:40,
                        message: '长度大于2小于40',
                        trigger: 'blur'
                    }],
                    code: [{
                        required: true,
                        message: '请输入编码',
                        trigger: 'blur'
                    }],
                    circleId: '',
                    nativename: '',
                },
                isClose:false,
            };
        },
        created: function () {
            this.circleId = this.$route.query.circleId;

        },
        mounted: function () {
            this.initData();
        },

        methods: {
            //提交表单
            onSubmit() {
                    debugger;
                    this.$refs["ruleForm"].validate((valid) => {
                        if (valid) {
                            var paramData = {
                                "id": this.ruleForm.id,
                                "name": this.ruleForm.name,
                                "dec": this.ruleForm.dec,
                                "code": this.ruleForm.code,
                                "type": 0,
                            }
                            debugger;
                            var submitUrl = "/cmdb/circle/update";
                            jQuery.ajax({
                                url: submitUrl,
                                type: "POST",
                                cache: false,
                                async: false,
                                traditional: true,
                                data: paramData,
                                success: function (json, textStatus) {
                                    if (json.success == true) {
                                        this.$notify({
                                            title: '提示',
                                            message: '修改成功',
                                            type: 'success',
                                            duration: 3000
                                        });
                                        this.closeWin();

                                    } else {
                                        this.$notify({
                                            title: '提示',
                                            message: json.msg,
                                            type: 'error',
                                            duration: 3000
                                        });
                                    }
                                }.bind(this),
                                error: function (XMLHttpRequest, textStatus, errorThrown) {
                                    this.$notify({
                                        title: '提示',
                                        message: '修改失败',
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
                closeWin: function () {
                    router.back();
                },
                closeDialog() {
                    router.back();
                },
                resetForm(formName) {
                    this.$refs[formName].resetFields();
                    this.ruleForm.name = "";
                    this.ruleForm.code = ""
                    this.ruleForm.dec = "";
                },

                initData() {
                    var paramData = {
                        "id": this.circleId,
                    }
                    var sysCodeUrl = '/cmdb/circle/getCircle';
                    $.ajax({
                        url: sysCodeUrl,
                        type: "POST",
                        cache: false,
                        async: true,
                        dataType: "json",
                        data: paramData,
                    }).done(function (data) {
                        debugger;
                        this.ruleForm = data;
                        this.nativename = data.name;
                    }.bind(this));
                },
                closeCircle() {
                	this.isClose = true;
                    
                },
        }
}
</script>
