<template>
    <!-- 自动化采集异常设备 -->
    <div class="components-container yw-dashboard" v-loading="loading">
     <iframe ref='iframes' :src="iframeSrc" width="100%" height="100%" frameborder="0"></iframe>
    </div>
</template>

<script>
import {myMixin} from 'src/pages/cmdb/smartData/resourcePerformance/mixin.js'
    export default {
       mixins:[myMixin],
        data() {
            return {
              iframeSrc:''
            }
        },
        created() {
          let excelOpt=encodeURIComponent(JSON.stringify({'shuiyin':'','isPwd':'','fileName':'自动化采集异常设备','title':'','title2':'','numRows':['class','school'],'sums':['class','school'], 'pageVarName':'','password':''}))
          this.iframeSrc=this.hostName+'/previewPages/index.html#/preview/1609918924977/report?excelOpt='+excelOpt
        },
        methods: {
          
        }
    }
</script>

<style lang="scss" scoped></style>