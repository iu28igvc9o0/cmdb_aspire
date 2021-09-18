<template>
    <!-- 防火墙会话数 -->
    <div class="components-container yw-dashboard" v-loading="loading">
     <iframe ref='iframes' :src="iframeSrc" width="100%" height="100%" frameborder="0"></iframe>
    </div>
</template>

<script>
import {myMixin} from '../mixin.js'
    export default {
      mixins:[myMixin],
        data() {
            return {
                iframeSrc:''
            }
        },
        created() {
           let excelOpt=encodeURIComponent(JSON.stringify({'shuiyin':'','isPwd':'','fileName':'防火墙会话数','title':'主标题','title2':'副标题','numRows':['class','school'],'sums':['class','school'], 'pageVarName':'','password':''}))
           this.iframeSrc=this.hostName+'/previewPages/index.html#/preview/1607326664946/all?excelOpt='+excelOpt
        },
        methods: {
          
        }
    }
</script>

<style lang="scss" scoped></style>