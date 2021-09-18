<template>
    <div class="solveQuestionsProcess">
        <Solve :selectInfo="selectInfo" />
    </div>
</template>
<script>
    import Solve from '../view'
    export default {
        name: 'SolveQuestionsProcess',
        data() {
            return {
                selectInfo: 'process'
            }
        },
        components: {
            Solve
        }
    }
</script>
<style lang="scss" scoped>
    .solveQuestionsProcess {
        height: 100%;
        width: 100%;
    }
</style>