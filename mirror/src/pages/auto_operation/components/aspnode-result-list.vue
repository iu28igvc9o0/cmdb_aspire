<template>
    <span>
<span v-if="status === 0" class="status-marking bggreen">正常</span>
        <span v-else-if="status === 1" class="status-marking bgorange">异常</span>
        <span v-else-if="status === 2" class="status-marking">无状态</span>
    </span>
</template>

<script>
    export default {
        name: 'AspnodeResultList',
        components: {},
        props: ['status'],
        data() {
            return {
            }
        },
    }
</script>
