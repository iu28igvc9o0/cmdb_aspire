<template>
    <!-- 状态等级灯 -->
    <div>
        <a class="status-item"
           v-if="alertLevel">
            <i class="status-lamp"
               :style="{color:defaultIcons[statusObj[alertLevel].status] && defaultIcons[statusObj[alertLevel].status].color}"
               :class="defaultIcons[statusObj[alertLevel].status] && defaultIcons[statusObj[alertLevel].status].url"></i>
            <span class="status-text color-black">{{statusObj[alertLevel].name}}</span>
        </a>
    </div>

</template>
<script>
    export default {
        props: ['alertLevel'],
        data() {
            return {
                // 默认图标
                defaultIcons: {
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
                },
                statusObj: {
                    '5': { status: 'serious', name: '重大' },
                    '4': { status: 'high', name: '高' },
                    '3': { status: 'middle', name: '中' },
                    '2': { status: 'low', name: '低' },
                }
            }
        },
        computed: {

        },

        methods: {

        },
        mounted() {

        }

    }
</script>
<style lang="scss" scoped>
    .status-item {
        display: inline-block;
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
</style>
