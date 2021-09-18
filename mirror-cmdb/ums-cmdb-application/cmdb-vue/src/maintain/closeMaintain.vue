<template>
<div class="container">

    <el-form label-position="top"  class="demo-ruleForm" label-width="100px" style="padding-right:30px;">
         <div style="padding:20px 0px 20px 0px;text-align:center;"><h4>关闭理由</h4></div>
	
	    <el-form-item align="center" label=""  style="width:100%; margin-left:auto;margin-right:auto;">
				  <el-input
					  type="textarea"
					  :rows="3"
					  placeholder="请输入关闭理由"
					  v-model="desc">
				  </el-input>
        </el-form-item>
        
         <el-alert style="padding:5px 0px 5px 0px;left:30%;width: 250px;text-align:center;" align="center"
		    title="圈子关闭后,配置将放回仓库"
		    type="info"
		    :closable="false"
		    show-icon>
		  </el-alert>
  
        <el-form-item align="center" style="margin-top:15px;">
            <el-button type="primary" @click="onSubmit" >提交</el-button>
        </el-form-item>
    </el-form>
    </div>
</template>
<style>
</style>
<script>
import router from '../router'
export
default {
    props: ['circleId'],
    data() {
        return {
            desc:'',
        };
    },

    mounted: function () {
        },

        methods: {
                //提交表单
                onSubmit() {
                       var paramData = {
                            "id": this.circleId,
                            "isDelete": 1,
                            "desc":this.desc,
                        }
                        var DeleteUrl = "/cmdb/circle/delete";
                        $.ajax({
                            url: DeleteUrl,
                            type: "POST",
                            data: paramData,
                            dataType: "json",
                            success: function (json, textStatus) {
                                if (json.success) {
                                    router.push({
                                        path: '/cmdb/user/getUsers1'
                                    });
                                    this.$notify({
                                        title: '提示',
                                        message: '删除成功!',
                                        type: 'success',
                                        duration: 3000
                                    });
                                } else {
                                    this.$notify({
                                        title: '提示',
                                        message: '删除失败!',
                                        type: 'error',
                                        duration: 3000
                                    });
                                }
                            }.bind(this),
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                this.$notify({
                                    title: '提示',
                                    message: '操作失败!',
                                    type: 'error',
                                    duration: 3000
                                });
                            }.bind(this)
                        });
                },

                closeWin: function () {
                    this.$emit("childBindAdd");
                },
                closeDialog() {
                    this.$confirm('是否确认退出？', '警告', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.closeWin();
                    }).catch(() => {
                        this.$notify({
                            title: '提示',
                            message: '已取消退出',
                            type: 'info',
                            duration: 3000
                        });
                    });
                },

        }
}
</script>
