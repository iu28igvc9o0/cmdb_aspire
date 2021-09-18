<template>
    <!-- 模块:纯数据 -->
    <section class="chart-part">
        <div class="chart-title">{{moduleData.title}}</div>
        <div class="chart-content">
            <p class="data-wrap"
               :style="activeStyle">{{moduleData.data}}</p>
        </div>
    </section>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'

    export default {
        mixins: [QueryObject],
        props: ['moduleData', 'option'],
        components: {

        },
        data() {
            return {


            }
        },
        computed: {
            activeStyle() {
                let res = {}
                if (this.option && this.option.style) {
                    res.fontSize = this.option.style.fontSize || 'auto'
                }
                return res
            }
        },

        mounted() {

        },
        methods: {

        }
    }
</script>

<style lang="scss" scoped>
    @import "./device-monitor.scss";
</style>
