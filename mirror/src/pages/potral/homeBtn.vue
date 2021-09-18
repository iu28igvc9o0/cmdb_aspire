<template>
    <div>
        <!-- 新增 -->
        <div class="add-box-btn"
             @click="signIn(1)">
            签到
        </div>
        <div class="add-box-btn"
             @click="signIn(2)">
            签退
        </div>
        <div class="add-box-btn"
             @click="gotoDetail">
            交班
        </div>
        <!-- <div style="position:relative;">
            <span>操作</span>
        </div> -->
    </div>
</template>

<script>
    import rbCustomServices from 'src/services/custom_pages/rb-services.js'
    export default {
        name: 'HomePageBtn',
        data() {
            return {

            }
        },
        created() {

        },
        mounted() {

        },
        methods: {
            signIn(val) {
                let req = {
                    checkType: val,
                    checkWay: 1
                }
                rbCustomServices
                    .signIn(req)
                    .then(res => {
                        console.log(res)
                        this.$message.success(res.msg)
                    })
                    .catch(error => {
                        this.$message.error(error)
                    })
            },
            gotoDetail() {
                // 待办 task/2009241132348055991  
                // 已办、我发起的 inst/2009241132348055991
                let routerHash = ''
                routerHash = 'start/' + window.bpm_id

                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: routerHash,
                        currentTitle: '交接班'
                    }
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    .add-box-btn {
        border-radius: 5px;
        width: 30px;
        margin-left: 6px;
        text-align: center;
        height: 20px;
        line-height: 18px;
        border: 1px solid #54adff;
        cursor: pointer;
        float: left;
    }
    .add-box-btn:nth-child(1) {
        margin-left: 0px;
    }
</style>
