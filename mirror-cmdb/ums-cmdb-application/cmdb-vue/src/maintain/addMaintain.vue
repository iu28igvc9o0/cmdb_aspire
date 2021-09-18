<template>
<div class="container">
<el-row :gutter="20">
<el-col :span="12">
     <div style="padding:20px 0px 20px 0px;text-align:center;"><h4>新增维护圈</h4></div>  
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
            <el-button type="primary" @click="onSubmit" >{{ruleForm.submitCon}}</el-button>
            <el-button  @click="closeDialog">取消</el-button>
        </el-form-item>
    </el-form>
 </el-col>
    
 <el-col :span="1" :offset="5">

 </el-col>
</el-row>
</div>
</template>
<style>
  
</style>
<script>
import router from '../router'
export
default {
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
                                callback(new Error('名称已存在'));
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
                ruleForm: {
                    name: '',
                    dec: '',
                    code: '',
                    submitCon: '立即创建'
                },

                rules: {
                    name: [{
                        validator: validateName,
                        trigger: 'blur'
                    }, {
                        min: 1, max:16,
                        message: '长度大于1小于16',
                        trigger: 'blur'
                    }],
                    dec: [{
                        required: true,
                        message: '请输入编码',
                        trigger: 'blur'
                    }, {
                        min: 1, max:140,
                        message: '长度大于1小于140',
                        trigger: 'blur'
                    }]
                }
            };
        },

        mounted: function () {},

        methods: {
            //提交表单
            onSubmit() {
                    debugger;
                    this.$refs["ruleForm"].validate((valid) => {
                        if (valid) {
                            var paramData = {
                                "name": this.ruleForm.name,
                                "dec": this.ruleForm.dec,
                                "code": this.ruleForm.code,
                                "type": 0,
                            }
                            debugger;
                            var submitUrl = "/cmdb/circle/add";
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
                                            message: '添加成功',
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
                closeWin: function () {
                    router.push({
                        path: '/cmdb/user/getUsers1'
                    });
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

        }
}
</script>
