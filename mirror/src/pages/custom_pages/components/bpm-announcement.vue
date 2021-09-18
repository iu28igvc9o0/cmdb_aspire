<template>
    <!-- 服务台： 公告 -->
    <div class="content-chart displaybox overflow-hidden bg-border-radius">
        <div class="announcement-bg bold white"
             :class="{'announcement-bg-blue' : platForm === 'it_devops_receptionp_platform'}">
            <i class="icon iconfont f16 align-middle">&#xe6ab;</i>公告
        </div>
        <el-carousel class="boxflex01 announcement-lsit mleft10"
                     height="36px"
                     direction="vertical"
                     :autoplay="true">
            <el-carousel-item v-for="item in dataList"
                              :key="item.id">
                <span class="blue pointer"
                      @click="gotoMore(item.subject)">{{item.subject}}</span>
                <span class="mleft10">{{item.createTime | formatDate}}</span>
            </el-carousel-item>
            <span v-if="!dataList.length">暂无新公告信息</span>
        </el-carousel>
        <div class="mleft10 mright10 blue pointer"
             @click="gotoMore()">更多</div>
    </div>
</template>

<script>
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'

    export default {
        components: {
        },
        data() {
            return {
                dataList: [],
                // 项目标识字段，默认为ums项目：it_devops_manage_platform
                platForm: 'it_devops_manage_platform'
            }
        },
        created() {
            this.platForm = sessionStorage.getItem('platForm') || this.platForm
            this.getHomePageNotice()
        },
        methods: {
            gotoMore(subject) {
                this.$router.push({
                    path: '/server/announcement/smart_pages/list',
                    query: {
                        subject
                    }
                })
            },
            getHomePageNotice() {
                rbBpmHomeServices.getHomePageNotice()
                    .then(res => {
                        this.dataList = res.result
                    })
            },
        }

    }
</script>

<style lang="scss" scoped>
    .announcement-bg {
        padding: 0 20px;
        height: 36px;
        line-height: 36px;
        overflow: hidden;
        background: url("../../../assets/img/custom_modules/bg-announcement.png")
            100% 100% no-repeat;
    }
    .announcement-lsit {
        line-height: 36px;
    }
</style>

