<template>
    <!-- 三角图标(上升、下降) -->
    <div class="yw-icon-triangle-wrap">
        <i class="icon-triangle"
           :class="className"></i>
        <span class="num"
              :class="className">{{showDatas}}</span>
    </div>
</template>

<script>
    export default {
        // 数据(格式如：'-20%')
        props: ['datas'],
        components: {

        },
        data() {
            return {

            }
        },
        computed: {
            className() {
                let res = []
                if (this.datas && this.datas.indexOf('-') > -1) {
                    res = ['down', 'green']
                } else {
                    res = ['up', 'red']
                }
                return res
            },
            showDatas() {

                return this.datas.indexOf('-') > -1 ? this.datas : ('+' + this.datas)
            }
        },
        methods: {


        },

        mounted() {

        },
    }
</script>

<style lang="scss" scoped>
    .yw-icon-triangle-wrap {
        display: inline-block;
        .icon-triangle {
            display: inline-block;
            width: 0;
            height: 0;
            border-left: 5px solid transparent;
            border-right: 5px solid transparent;
            &.up {
                border-bottom: 6px solid #0ba837;
            }
            &.down {
                border-top: 6px solid #0ba837;
            }

            &.green {
                border-top-color: #0ba837;
            }
            &.red {
                border-bottom-color: #f0234d;
            }
        }
        .num {
            &.green {
                color: #0ba837;
            }
            &.red {
                color: #f0234d;
            }
        }
    }
</style>
