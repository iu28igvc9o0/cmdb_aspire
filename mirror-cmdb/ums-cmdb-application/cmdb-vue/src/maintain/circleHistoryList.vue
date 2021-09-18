<template>
  <div class="container" >
        <el-row style="padding:15px 25px 0;">
                <el-form :inline="true" >

                     <el-col :span="5" style="padding-right:10px;">
                        <div style="width:100%;max-width:550px;float:right;">

                                <div  style="float:right;" align="right">
                                    <el-form-item label="" class="keyword" prop="keyword">
                                        <el-input v-model="keyword" style="width:200px;" icon="search" :maxlength="50" placeholder="请输入内容">
                                        </el-input>
                                    </el-form-item>
                                </div>
                        </div>
                        
                    </el-col>
                </el-form>
            </el-row>

    <el-row >
      <el-table  v-if="isInit"  :data="tableData"   row-key="id" 
                          stripe class="margin-top-12"  
                          v-loading="loading"    element-loading-text="拼命加载中">

                <el-table-column   prop="name"  fixed  label="配置项名称"  align="center"    show-overflow-tooltip></el-table-column>
                <el-table-column   prop="relationName" label="关系" align="center" v-if="actionType == 'Relation'"    show-overflow-tooltip></el-table-column>

                <el-table-column prop="targetName" label="目标配置名称" align="center" v-if="actionType == 'Relation'"  show-overflow-tooltip></el-table-column>
                <el-table-column prop="action" label="状态" align="center"  v-if="actionType == 'Relation'" show-overflow-tooltip></el-table-column>
                
                <el-table-column prop="action" label="操作" align="center"  v-if="actionType == 'Instance'" show-overflow-tooltip></el-table-column>                
                <el-table-column prop="insertTime" label="时间" align="center"  show-overflow-tooltip></el-table-column>

          </el-table>

        <div class="block">
        <el-pagination class="text-right padding-right-10" ref="taskPage" align='right' 
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="paginationData.currentPage"
          :page-sizes="[10,20,40, 60, 80]"
          :page-size="paginationData.pageSize"
          :total="paginationData.total"
           layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </div>
    </el-row>
  </div>
</template>

<script>
import router from '../router';
export
default {
    props: ['actionType','circleId'],
    data() {

        return {
            // 是否隐藏筛选层
            keyword: '',
            tableData: [],
            columnList: [],
            loading: true,
            paginationData: {
                currentPage: 1,
                total: 0,
                pageSize: 10,
                selectPageSizes: [20, 30, 50, 70],
                sort: 'insertTime',
                order: 'desc',
            },
            selectData: [],
            isInit:false,
        }
    },
    watch: {
        actionType(curVal, oldVal) {
                this.queryData();　　　　　　　　
            },
            keyword(curVal, oldVal) {
                this.queryData();　　　　　　　　
            }, 　　

    },
    mounted: function () {
            this.queryData();
        },
        methods: {
                queryData: function () {
                    // 改变this.currentPage 即触发翻页
                    if (this.paginationData.currentPage != 1) {
                        this.paginationData.currentPage = 1;
                    } else {

                    }
                    this.getData();
                },

                getData: function () {
                    this.isInit =false;
                    debugger;
                    this.loading = true;
                    debugger;
                    var query_data = {
                        "pageNumber": this.paginationData.currentPage,
                        "pageSize": this.paginationData.pageSize,
                        "sort": this.paginationData.sort,
                        "order": this.paginationData.order,
                        "name": this.keyword,
                        "actionType": this.actionType,
                        "circleId": this.circleId,
                    };
                    var sysCodeUrl = '/cmdb/circle/getHistoryByActionType';
                    $.ajax({
                        url: sysCodeUrl,
                        type: "POST",
                        cache: false,
                        async: false,
                        dataType: "json",
                        data: query_data,
                    }).done(function (data) {
                        debugger;
                         this.isInit =true;
                        this.loading = false;
                        this.paginationData.total = data.total;
                        this.tableData = data.dataList;

                    }.bind(this));
                },

    

                //选择每页显示记录数
                handleSizeChange(size) {
                    this.paginationData.pageSize = size;
                    var maxPage = Math.ceil(this.paginationData.total / size);
                    if (this.paginationData.currentPage > maxPage) {
                        this.paginationData.currentPage = maxPage;
                    } else {
                        this.getData();
                    }
                },
                //分页
                handleCurrentChange(val) {
                    this.paginationData.currentPage = val;
                    this.getData();
                },
 
        }
}
</script>

<style scoped>

@keyframes myfirst
{
0%   {left:100%;top:0px;}
100% {left:55%; top:0px;}

}


.right-dialog{
    width:45%;
    height:100%;
    left: 55%;
    animation:myfirst 0.8s;
    top:0%;
    background: #fff;
    border-radius: 2px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, .3);
    box-sizing: border-box;
}


.dialog-fade-leave-active {
    animation: dialog-fade-out .3s
}
  .el-row {
    margin-bottom: 1px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .row-bg {
    padding: 1px 0;
    background-color: #f9fafc;
  }
</style>
