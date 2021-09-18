<template>
    <span>
        <span v-if="status === 9"
              class="status-marking bggreen">执行成功</span>
        <span v-else-if="status === 101"
              class="status-marking bgorange">执行失败</span>
        <span v-else-if="status === 102"
              class="status-marking bggrey">执行超时</span>
        <span v-else-if="status === 100"
              class="status-marking">执行中</span>
        <span v-else-if="status === 5"
              class="status-marking bgblue">等待执行</span>
        <span v-else-if="status === 6"
              class="status-marking bgorange">暂停待确认</span>
        <span v-else-if="status === 7"
              class="status-marking bgblue">执行阻断</span>
        <span v-else-if="status === 8"
              class="status-marking bgorange">执行待审核</span>
        <span v-else-if="status === 10"
              class="status-marking bgorange">审核拒绝</span>
        <span v-else-if="status === 11"
              class="status-marking bgbule">审核通过待执行</span>
    </span>
</template>

<script>
    export default {
        name: 'AutoOperationCodeStatusList',
        components: {},
        props: ['status'],
        data() {
            return {
            }
        },
    }
</script>
