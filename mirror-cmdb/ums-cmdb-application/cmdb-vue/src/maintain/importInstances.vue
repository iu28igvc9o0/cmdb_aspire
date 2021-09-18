<template>
<div class="container">

    <el-form label-position="top"  class="demo-ruleForm" label-width="100px" style="padding-right:30px;">
             <el-form-item align="center" >
                  <el-upload
  class="upload-demo"
  drag
  :action="'/cmdb/repertory/uploadData/'+circleId" :on-change="uploadChange" 	  :on-success="handleSuccess" :on-remove="handleRemove"
  multiple>
  <i class="el-icon-upload"></i>
  <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
</el-upload>

         </el-form-item>        


         <el-alert v-if="isShowResult" style="padding:7px 0px 7px 0px;left:22%;bottom:10px;width: 400px;text-align:center;" align="center"
		    :title="resultMsg"
		    :type="infoType"
		    @close="closeAlert"
		    show-icon>
		  </el-alert>
	     <el-form-item align="center"  v-if="isShowError" >
		  <el-button @click="downLoadErrorfile">下载失败原因文件</el-button>
		 </el-form-item> 

	     <el-form-item align="center" >
		  <el-button type="info" @click="isShowItem=!isShowItem">还没有Excel模版?请下载模版</el-button>
		 </el-form-item> 
	  
	    <el-form-item align="center"  v-if="isShowItem" label="请选择需要导出的配置类型"   >
	              <el-select  placeholder="配置类型" v-model="moduleId">
				    <el-option-group
				      v-for="group in selectData"
				      :key="group.id"
				      :label="group.name">
				      <el-option
				        v-for="item in group.item"
				        :key="item.id"
				        :label="item.name"
				        :value="item.id">
				      </el-option>
				    </el-option-group>
				  </el-select>
        </el-form-item>
        

  
        <el-form-item align="center"  v-if="isShowItem"  style="margin-top:15px;">
            <el-button type="primary" @click="onSubmit" >导出</el-button>
        </el-form-item>
    </el-form>
    </div>
</template>
<style>
</style>
<script>
export
default {
    props: ['circleId'],
    data() {
        return {
            moduleId:'',
            selectData:[],
            isShowItem:false,
            fileList:[],
            resultMsg:'',
            isShowError:false,
            isShowResult:false,
            currentErrorFile:'',
            currentError:'',
            infoType:'success',
            
            
        };
    },

    mounted: function () {
            this.initData();
        },

        methods: {
                //提交表单
                onSubmit() {
                            debugger;
                            var submitUrl = "/cmdb/repertory/downloadExcel/"+this.moduleId;
                            window.open(submitUrl);
                            
                },

                initData() {
                   var sysCodeUrl = '/cmdb/circle/getModuleTree';
                    $.ajax({
                        url: sysCodeUrl,
                        type: "POST",
                        cache: false,
                        async: true,
                        dataType: "json",
                        data: "",
                    }).done(function (data) {
                        debugger;
                        this.selectData = data;
                    }.bind(this));

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
                resetForm(formName) {
                    this.$refs[formName].resetFields();
                },
  
      uploadChange(file) {
      debugger;
        console.log(file);
      },
    handleSuccess(response, file, fileList) {
    debugger;
    const self = this;
     for(var i = 0; i < fileList.length; i++){
       
       if(response.success== true ){
             if(file.uid == fileList[i].uid){
		       fileList[i].url = response.url;
		       }

		        this.resultMsg = file.name.substring(0,file.name.lastIndexOf('.'))+'成功导入数据';
		        this.isShowResult = true;
		        this.isShowError = false;
		        
		        this.infoType='success';

       }else{
               if(file.uid == fileList[i].uid) {
                  fileList[i].status = false;
                  file.status = false;
			    }
			    this.currentErrorFile=file.name.substring(0,file.name.lastIndexOf('.'))+'-error.txt';
			    this.currentError=response.message;
		        this.resultMsg = file.name.substring(0,file.name.lastIndexOf('.')) + '导入失败';
			    this.isShowResult = true;
			    this.isShowError = true;
			    
			    this.infoType='warning';
			    
       }
     }  

        },
    handleRemove(file, fileList){
           for(var i = 0; i < fileList.length; i++){
               if(file.uid == fileList[i].uid) {
			      fileList.splice(i, 1);
			    }
           }
    },
    
        downLoadErrorfile() {

		                
		    // 创建隐藏的可下载链接
		    var eleLink = document.createElement('a');
		    eleLink.download = this.currentErrorFile;
		    eleLink.style.display = 'none';
		    // 字符内容转变成blob地址
		    var blob = new Blob([this.currentError]);
		    eleLink.href = URL.createObjectURL(blob);
		    // 触发点击
		    document.body.appendChild(eleLink);
		    eleLink.click();
		    // 然后移除
		    document.body.removeChild(eleLink);
    },
    
         closeAlert : function() {
                    this.isShowResult = false;
           },  
        }
}
</script>
