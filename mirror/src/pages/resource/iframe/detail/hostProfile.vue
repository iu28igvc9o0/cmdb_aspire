<template>
    <el-card class="box-card"
             v-loading="loading"
             ref="cards"
             :body-style="{ position: 'relative',height:'100%'}">
        <div slot="header"
             class="clearfix">
            <span class="file-name">主机配置文件目录</span>
            <div class="acquisition-time">
                <span style="margin-right:10px;">数据采集时间</span>
                <el-date-picker v-model="time"
                                @change="chnageDate"
                                type="datetime"
                                format="yyyy-MM-dd HH:mm:ss"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                placeholder="选择日期时间">
                </el-date-picker>
            </div>
        </div>
        <div v-if="files.length<1">暂无数据</div>
        <div v-else
             class="acquisition-detail">
            <div v-for="(item,index) in files"
                 :key="index"
                 class="text item">
                <a v-if="item.fileStatus!='False'"
                   @click.prevent="downLoadFiles(item.fileId,item.fileName)">{{item.fileName}}</a>
            </div>
        </div>
    </el-card>
</template>
<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        name: 'HostProfile',
        data() {
            return {
                time: '',
                files: [],
                loading: false
            }
        },
        created() {
            let currentTime = this.formatDate(new Date().getTime())
            let currentStartTime = currentTime + ' ' + '00:00:00'
            this.time = currentStartTime
        },
        mounted() {
            this.getHostInformation()
        },
        methods: {
            getHostInformation() {
                this.loading = true
                let queryData = {
                    time: this.time,
                    hostIp: JSON.parse(this.$route.query.queryParams).ip
                }
                rbCmdbServiceFactory.getHostProfile(queryData).then((res) => {
                    this.files = res
                    this.loading = false
                })
            },
            chnageDate() {
                this.getHostInformation()
            },
            downLoadFiles(fileId, fileName) {
                rbCmdbServiceFactory.downLooadHostProfile(fileId).then((res) => {
                    let blob = new Blob([res])
                    // 创建下载链接
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = fileName
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                }).catch((e) => {
                    this.$message.error('导出失败!' + e)
                })
            },
            // 日期获取
            formatDate(data) {
                let date = new Date(data)
                let YY = date.getFullYear() + '-'
                let MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
                let DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
                return YY + MM + DD
            }
        }
    }
</script>
<style lang="stylus" scoped>
    .item {
        margin-bottom: 20px;
    }

    .clearfix:before, .clearfix:after {
        display: table;
        content: '';
    }

    .clearfix:after {
        clear: both;
    }

    .file-name {
        font-weight: 600;
    }

    .box-card.el-card >>>.el-card__header {
        height: 60px;
        line-height: 60px;
    }

    .acquisition-time {
        float: right;
    }

    .acquisition-detail {
        height: 85%;
        overflow-y: auto;
    }
</style>