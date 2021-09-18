<template>
    <div class="container" style="height: 600px; overflow: auto;" ref="container" align="center">
 <el-row  type="flex" justify="end"  style="padding:30px 0px 0px 0px;" >
  <el-col :span="8"> </el-col>
 <el-col :span="8">
                    <div style="padding:10px 30px 10px 0px;width:400px;white-space:nowrap;" >
                        <el-input icon="search" placeholder="请输入圈子名称" v-model="searchValue" >
                        </el-input>
                    </div>
 </el-col> 
 <el-col :span="5"></el-col> 
  <el-col :span="4"> 
	  <div style="padding:10px 30px 10px 50px;" >
	       <el-button  @click="creatOne()" ><i class="fa fa-plus" aria-hidden="true"></i></el-button>
	  </div>
  </el-col>                   
                    
 </el-row>
          
        <div >            
        <el-row >
	          <div class="el-breadcrumb ">
	              <h4 style="font-size:18px;" align="left"><i class="fa fa-circle-o" aria-hidden="true"></i> 全部圈子</h4>
	          </div>
        </el-row>
            
            <template  v-for="item in tableData">
            		<div class="buju" v-on:mouseover='selectIt(item)' v-on:mouseleave='out(item)'>
		   <div class="quanzi">
				<div class="ding" >
				<div style='width:120px;text-overflow:ellipsis;overflow:hidden;float:left;white-space: nowrap;padding:0px 0px 0px 60px;' :title='item.name'>
				{{item.name}}
				</div>
				<div>
				<img v-if="item.isTop==1" src="../../static/img/ding_0.png" width="18" height="24" @click="goHead(item.id, item.isTop)"><img v-else-if="item.isTop==0 && item.id==currentId" src="../../static/img/ding_1.png" width="18" height="24" @click="goHead(item.id, item.isTop)"><img v-else="item.isTop==0 && item.id!=currentId"></div>
				</div>
				<div class="yuanhuan" @click="clickFn(item.id,item.name)">
					<span class="peizhi">配置数：{{ item.instanceNum}}</span>
					<span class="peizhi">变更数(30d)：{{ item.activeNum}}</span>
				</div>		
		   </div>
	      </div>
           </template>
               <MugenScroll :handler="fetchData" scroll-container="container" :should-handle="!loading">
               </MugenScroll>
            
        </div>

        <router-view></router-view>
    </div>
</template>
<style>
.tableDataList .el-button--text{color:#5e7382;}
.hide{display: none}
.text-right{ text-align: right; }
.buju{width:33.33%;border:0px solid #000;float:left;font-family: 'arial'}
.quanzi{width:240px;height:280px;margin:0 auto;}
.ding{width:100%;text-align: center;font-size:20px; line-height:40px;display:block;}
.ding img{margin-left:-40px;vertical-align: middle;}
.yuanhuan{border:6px solid #20A0FF;text-align: center;width:200px;height:200px;box-sizing: border-box;border-radius:100%;}
.jindu{width:65px;height:10px;background:#fff;display:inline-block;border-radius:5px;}
.peizhi{width:70%;float:left;margin:20px 15%;text-align:left;padding-left:8%;box-sizing:border-box; height:35px;color:#fff;background:#2c91e9;border-radius:30px;font-size:15px;line-height: 35px;display:inline-block;}


.el-breadcrumb {
    background-color: #fff;
    padding: 19px 0 19px 30px;
    border-bottom: 2px solid #f0f0f0;
    font-size: 14px;
}

</style>

<script>

import router from '../router';
import MugenScroll from 'vue-mugen-scroll';
export
default {
    components: {
        MugenScroll
    },
    data() {
        return {
            //传递到子组件的值
            row: '',
            options: {
                sysCode: [],
                code: [],
            },
            paginationData: {
                currentPage: 1,
                total: 1,
                pageSize: 27,
                selectPageSizes: [20, 30, 50, 70],
                sort: '',
                order: '',
            },
            tableData: [],
            multipleSelection: [],
            searchValue: "",
            isSearch: false,
            loading: false,
            currentId:"",
        }
    },
    watch: {
        searchValue: "do_search",

    },
    mounted: function () {



        },
        methods: {
            goHead(id, isTop) {
            debugger;
                    var goHeadUrl = "/cmdb/circle/goHead";
                    debugger;
                    $.ajax({
                        url: goHeadUrl,
                        type: "POST",
                        data: {
                            'id': id,
                            'isTop': isTop
                        },
                        dataType: "json",
                        success: function (json, textStatus) {
                            debugger;
                            if (json.success == true) {
                                this.do_search();
                            } else {}
                        }.bind(this),
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            debugger;
                            this.$notify({
                                title: '提示',
                                message: '请求失败',
                                type: 'error',
                                duration: 3000
                            });
                        }.bind(this)
                    });
                },
                fetchData() {
                    debugger;
                    if (this.paginationData.total > this.tableData.length) {
                        this.loading = true;


                        debugger;
                        var query_data = {
                            "pageNumber": this.paginationData.currentPage,
                            "pageSize": this.paginationData.pageSize,
                            "sort": this.paginationData.sort,
                            "orderBy": this.paginationData.order,
                            "name": this.searchValue,
                        };
                        var sysCodeUrl = '/cmdb/circle/getCircles';
                        $.ajax({
                            url: sysCodeUrl,
                            type: "POST",
                            cache: false,
                            async: false,
                            dataType: "json",
                            data: query_data,
                        }).done(function (data) {
                            debugger;
                            this.paginationData.total = data.total;
                            this.tableData.push.apply(this.tableData, data.dataList);

                            var pageNum = this.paginationData.currentPage + 1;
                            this.paginationData.currentPage = pageNum;
                        }.bind(this));
                        this.loading = false;
                    }
                },
                do_search() {
                    this.paginationData.currentPage = 1;
                    this.paginationData.total = 1;
                    this.tableData = [];
                    debugger;
                    this.fetchData();

                },
                clickFn(id, name) {
                    router.push({
                        path: '/cmdb/maintain/configure',
                        query: {
                            circleId: id,
                            circleName:name,
                        }
                    });
                },
                creatOne() {
                    debugger;
                    router.push({
                        path: '/cmdb/maintain/addMaintain'
                    });
                },
                //选择每页显示记录数
                handleSizeChange(size) {
                    this.paginationData.pageSize = size;
                    var maxPage = Math.ceil(this.paginationData.total / size);
                    if (this.paginationData.currentPage > maxPage) {
                        this.paginationData.currentPage = maxPage;
                    } else {
                        this.do_search();
                    }
                },
                //分页
                handleCurrentChange(val) {
                    this.paginationData.currentPage = val;
                    this.do_search();
                },
                //鼠标移入
                selectIt(item) {
                    this.currentId = item.id;
                },
               //鼠标移出
                out(item) {
                    this.currentId = "";
                },
        }
}
</script>
