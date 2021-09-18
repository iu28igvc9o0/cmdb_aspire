<template>
    <div class="content-chart p010">
        <el-row :gutter="10"
                type="flex"
                class="panel-box"
                align="middle"
                justify="start">
            <el-col :span="8"
                    class="relative"
                    align="center">
                <img :src="panelData.iconUrl" />
                <div class="total-count">{{panelData.s_count + panelData.h_count + panelData.m_count + panelData.l_count}}</div>
            </el-col>
            <el-col :span="16">
                <div>{{panelData.device_class === '服务器' ? '主机' : panelData.device_class === '存储设备' ? '块存储' : panelData.device_class}}</div>
                <div class="target-num">
                    {{panelData.device_count || 0}}
                    <span class="f14"
                          v-if="panelData.device_class !== '存储设备'"> 台</span>
                    <span class="f14"
                          v-else> T</span>
                </div>
                <span class="status-marking mtop5 bg-darkred-linear">{{panelData.s_count}}</span>
                <span class="status-marking mtop5 bg-orange-linear">{{panelData.h_count}}</span>
                <span class="status-marking mtop5 bg-yellow-linear">{{panelData.m_count}}</span>
                <span class="status-marking mtop5 bggrey-linear">{{panelData.l_count}}</span>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        props: {
            panelData: {
                type: Object,
                default: {}
            }
        },
        data() {
            return {
            }
        },
        methods: {
        },
        created() {
        }

    }
</script>

<style lang="scss" scoped>
    .panel-box {
        height: rem(170);
        font-size: $font-20;
        color: $color-bg;
        img {
            width: rem(100);
            height: rem(100);
        }
        .target-num {
            font-size: $font-30;
            color: $color-tip-blue-light;
            font-weight: bold;
        }
        .total-count {
            margin-top: -5px;
        }
    }
    .mtop15 {
        margin-top: 15px;
    }
</style>

