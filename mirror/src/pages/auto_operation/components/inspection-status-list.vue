<template>
    <span>
        <span v-if="status == 0"
              class="status-marking bggreen">正常</span>
        <span v-else-if="status == 1"
              class="status-marking bgorange">异常</span>
        <span v-else-if="status == 2"
              class="status-marking bggrey">无结果</span>
        <span v-else-if="status == 3"
              class="status-marking bgblue">人工判断</span>
    </span>
</template>

<script>
    export default {
        components: {},
        props: ['status'],
        data() {
            return {
            }
        }
    }
</script>

<style scoped>
</style>