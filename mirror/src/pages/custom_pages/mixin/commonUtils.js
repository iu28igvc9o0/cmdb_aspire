export default {
    // 定制化类：共用方法
    data() {
        return {

        }
    },

    methods: {
        // 跳转链接(链接,参数)
        linkUrl(url = '/resource/flow', query = {}) {
            this.$router.push({
                path: url,
                query: query
            })
        },

        // conditions查询参数设置
        setConditions() {

            // tab
            if (this.moduleData && this.moduleData.conditions && this.moduleData.conditions.activeTab) {
                this.tabList.some((item, index) => {
                    if (item.name === this.moduleData.conditions.activeTab.name) {
                        this.activeTab = item
                        this.activeIndex = index
                        return true
                    } else {
                        return false
                    }
                })
            } else {
                this.activeTab = this.tabList[0]
                this.activeIndex = 0
            }

            // select
            if (this.moduleData && this.moduleData.conditions && this.moduleData.conditions.activeSelect) {
                this.selectList.some((item, index) => {
                    if (item.value === this.moduleData.conditions.activeSelect) {
                        this.activeSelect = item.value
                        return true
                    } else {
                        return false
                    }
                })
            } else {
                this.activeSelect = this.selectList && this.selectList.length > 0 ? this.selectList[0].value : ''
            }
        }
    },
    mounted() {
    }
}
