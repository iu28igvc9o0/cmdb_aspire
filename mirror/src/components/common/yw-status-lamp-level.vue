<template>
    <!-- 状态等级灯列表 -->
    <div class="status-lamp-wrap">
        <span class="status-total">{{datas.total}}</span>
        <a class="status-item"
           v-for="(item,index) in datas.list"
           @click="changeLamp"
           :key="index">
            <i class="status-lamp"
               :style="{color:defaultIcons[item.status] && defaultIcons[item.status].color}"
               :class="defaultIcons[item.status] && defaultIcons[item.status].url"></i>
            <span class="status-text color-black">{{item.name}}</span>
        </a>
    </div>

</template>
<script>
    export default {
        props: {
            // 数据
            datas: {
                type: [Object, Array],
                default: function () {
                    return [
                        {
                            status: 'blue',
                            num: 0
                        }
                    ]
                }
            },
        },
        data() {
            return {
                // 默认图标
                defaultIcons: {

                }
            }
        },

        methods: {
            getIcons() {
                this.defaultIcons = {
                    'serious': {
                        color: '#DC3938',
                        url: 'fa fa-exclamation-circle',
                    },
                    'high': {
                        color: '#DC3938',
                        url: 'fa fa-circle',
                    },
                    'middle': {
                        color: '#ffeb6f',
                        url: 'fa fa-circle',
                    },
                    'low': {
                        color: '#F9C810',
                        url: 'fa fa-warning',
                    },
                }
            },
            changeLamp(item) {
                this.$emit('changeLamp', item)
            }
        },
        mounted() {
            this.getIcons()
        }

    }
</script>
<style lang="scss" scoped>
    .status-lamp-wrap {
        display: inline-block;

        .status-total {
            display: inline-block;
            vertical-align: middle;
            margin-right: 20px;
        }
        .status-item {
            display: inline-block;
            margin-right: 15px;
            &:hover {
                font-weight: bold;
            }
            &:last-of-type {
                margin-right: 0;
            }
            .status-lamp {
                display: inline-block;
                vertical-align: middle;
                font-size: 16px;
            }
            .status-text {
                display: inline-block;
                vertical-align: middle;
                font-size: 12px;
            }
        }
    }
</style>
