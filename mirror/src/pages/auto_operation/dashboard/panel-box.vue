<template>
    <div class="content-chart p010"
         style="padding-left: 0px;padding-right: 0px;border: 1px solid #1690ED;border-radius: 6px;box-sizing: border-box;">
        <el-row :gutter="10"
                type="flex"
                class="panel-box"
                align="middle"
                justify="start">
            <el-col :span="9"
                    align="center">
                <img :src="panelData.iconUrl" />
            </el-col>
            <el-col :span="15">
                <div>{{panelData.name}}</div>
                <div class="target-num">{{panelData.count || 0}}</div>
                <span class="status-marking mtop5 bggreen-linear"
                      v-if="panelData.successCount !== undefined">成功：{{panelData.successCount}}</span>
                <span class="status-marking mtop5 bggrey-linear"
                      v-if="panelData.running !== undefined">运行中：{{panelData.running}}</span>
                <br v-if="panelData.successCount !== undefined">
                <span class="status-marking mtop5 bgred-linear"
                      v-if="panelData.alertCount !== undefined">告警：{{panelData.alertCount}}</span>
                <span class="status-marking mtop5 bggrey-linear"
                      v-if="panelData.failCount !== undefined">失败：{{panelData.failCount}}</span>
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
    }
    .mtop15 {
        margin-top: 15px;
    }
</style>

