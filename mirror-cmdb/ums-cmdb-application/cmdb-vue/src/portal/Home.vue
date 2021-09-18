<template>
    <div class="home_container">
        <el-row class="header">
            <el-col :span="4">
                <div class="logo">CMDB</div>
            </el-col>
            <el-col :span="13" class="headernav">
              &nbsp;
            </el-col>
            <el-col :span="7" align="right" style="padding-right:30px;">
                <div class="massage">
                    <span v-html="time"></span>
                </div>
            </el-col>
        </el-row>
        <el-row class="home_body">
            <el-col :span="4" class="leftnav">
                <el-menu theme="dark" :unique-opened="true" menu-trigger="click">
                        <template>
                            <el-menu-item :index="'\''+0+'\''" @click="changeBre('0')">维护圈</el-menu-item>
                        </template>
						<template>
                            <el-menu-item :index="'\''+1+'\''" @click="changeBre('1')">仓库</el-menu-item>
                        </template>
						<template>
                            <el-menu-item :index="'\''+2+'\''" @click="changeBre('2')">模型</el-menu-item>
                        </template>
                </el-menu>
            </el-col>
            <el-col :span="20" class="contendContainer">
                <div>
                    <router-view></router-view>
                </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
import '../assets/js/common.js'
export default {
    data() {
            return {
                time: '',
                currentUrl: ''
            }
        },
        mounted: function() {
          var now = new Date();
          var dateString = now.format("yyyy-MM-dd");
          var week = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];
          var nowDay = now.getDay();
          var nowDayString = week[nowDay];
          var nowTime = now.format("HH:mm");

          this.time = dateString + "&nbsp;&nbsp;" + nowDayString + nowTime + "&nbsp;&nbsp;";

        },
        methods: {
            changeBre(index) {
        				this.currentUrl = "/cmdb/user/getUsers1";
        				if(index == "1"){
        					this.currentUrl = "/cmdb/user/getUsers2";
        				}
        				if(index == "2"){
        					this.currentUrl = "/cmdb/user/getUsers";
        				}
                this.$router.push(this.currentUrl);
            },
        },
        computed: {
            onRoutes() {
                return this.$route.path.replace('/', '');
            }
        }
}
</script>
<style scoped>
.el-breadcrumb {
    background-color: #fff;
    padding: 19px 0 19px 30px;
    border-bottom: 4px solid #f0f0f0;
}

.header .massage {
    color: #FDFEFD;
    font-size: 14px;
    line-height: 24px;
    margin-top: 18px;
}

.home_container {
    min-width: 1024px;
    height: 100%;
    overflow-x: hidden;
}

.contendContainer {
    height: 100%;
    background: #ffffff;
}

.header {
    background-color: #292C31;
    height: 60px;
    overflow: hidden;
}

.home_body {
    height: calc(100% - 60px);
}

.logo {
    font-size: 20px;
    min-width: 160px;
    line-height: 60px;
    color: #fff;
    text-align: center;
}

.headernav {
    box-sizing: border-box;
    padding-left: 40px;
}

.leftnav {
    min-width: 160px;
}
</style>
