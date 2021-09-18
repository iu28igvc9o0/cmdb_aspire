<template>
    <div class="component-container"
         style="padding: 0px 0px 0px;">
        <div class="main-page-box">
            <img @click="hidePage"
                 :src="'/static/img/pages/' + currentPageList[currentIndex]">
        </div>

    </div>
</template>
<script>

    export default {
        components: {
        },
        data() {
            return {
                pageData: {
                    '事件接入管理': [
                        '事件接入管理.jpg',
                        '新建事件.png',
                    ],
                    '网络配置文件比对': [
                        '对比-主设备配置对比.jpg',
                        '对比3.png',
                        '对比-主设备配置对比.jpg',
                        '对比2.png',
                        '对比-主设备配置对比.jpg',
                        '对比1.png',
                        '对比-主设备配置对比.jpg',
                        '对比日志.png',
                    ],
                    '操作系统安装': [
                        '操作系统安装1.jpg',
                        '操作系统安装2.jpg',
                        '操作系统安装1.jpg',
                        '操作系统安装3.jpg',
                    ],
                    '应用软件安装': [
                        '软件安装.jpg',
                        '软件安装2.jpg',
                    ],
                    'LLDP自动采集': [
                        'LLDP自动采集管理.jpg',
                        'LLDP信息采集设置.png',
                        'LLDP自动采集管理.jpg',
                        'LLDP采集记录.png',
                    ],
                },
                currentIndex: 0
            }
        },
        computed: {
            currentPageList() {
                return this.pageData[this.$route.query.currentTitle]
            }
        },
        created() {

        },
        methods: {
            hidePage() {

                if (this.currentIndex + 1 < this.currentPageList.length) {
                    this.currentIndex++
                } else {
                    this.currentIndex = 0
                }
            }
        },



    }
</script>
<style lang="scss">
    .main-page-box {
        width: 100%;
        height: 100%;
        position: relative;
        img {
            width: 100%;
            height: auto;
            position: absolute;
            top: 0;
        }
    }
</style>
