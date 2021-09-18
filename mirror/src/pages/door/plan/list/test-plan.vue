<template>
    <FullCalendar defaultView="dayGridMonth"
                  locale="zh-cn"
                  timeZone="UTC"
                  firstDay="1"
                  weekNumberCalculation="ISO"
                  editable="true"
                  droppable="true"
                  displayEventEnd="true"
                  :eventTimeFormat="evnetTime"
                  :header="header"
                  :plugins="calendarPlugins"
                  :events="calendarEvents"
                  @dateClick="handleDateClick"
                  @eventClick="handleEventClick"
                  @eventDrop="calendarEventDrop"
                  @datesRender="handleDatesRender" />
</template>

<script>
    import FullCalendar from '@fullcalendar/vue'
    import dayGridPlugin from '@fullcalendar/daygrid'
    import timeGridPlugin from '@fullcalendar/timegrid'
    import interactionPlugin from '@fullcalendar/interaction'
    import '@fullcalendar/core/main.css'
    import '@fullcalendar/daygrid/main.css'
    import '@fullcalendar/timegrid/main.css'

    export default {
        components: {
            FullCalendar
        },
        data() {
            return {
                calendarPlugins: [
                    dayGridPlugin,
                    timeGridPlugin,
                    interactionPlugin
                ],
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'dayGridMonth,timeGridWeek,timeGridDay'
                },
                evnetTime: {
                    hour: 'numeric',
                    minute: '2-digit',
                    hour12: false
                },

                calendarEvents: [
                    { 'uuid': '136b4f8b-58b1-4ec5-b25d-6354694491a1', 'taskName': '测试任务3', 'taskType': '2', 'taskDescription': '啊啊啊啊所所', 'idcType': '南方基地', 'taskStartTime': '2020-04-20 00:00:00', 'taskEndTime': '2020-04-20 20:00:00', 'orderId': 's d', 'taskStatus': '2', 'taskResult': null, 'content': null, 'creator': 'alauda(13800138003)', 'createTime': '2020-04-20 14:01:20', 'updater': null, 'updateTime': null, 'messageCount': 0, 'id': '136b4f8b-58b1-4ec5-b25d-6354694491a1', 'title': '测试任务3', 'start': '2020-04-20 00:00:00', 'end': '2020-04-20 20:00:00', 'description': '啊啊啊啊所所', 'textColor': '#475C6C', 'borderColor': 'rgba(255,0,0,0)', 'className': 'status-bg status-blue' }, { 'uuid': 'ebf6ca4c-04cb-4ec9-81b1-e4b38a4021f5', 'taskName': '测试任务4', 'taskType': '1', 'taskDescription': '啊啊所多撒多', 'idcType': '信息港池外', 'taskStartTime': '2020-04-20 14:46:00', 'taskEndTime': '2020-04-20 15:00:00', 'orderId': 'we', 'taskStatus': '3', 'taskResult': '1', 'content': null, 'creator': 'alauda(13800138003)', 'createTime': '2020-04-20 14:46:41', 'updater': null, 'updateTime': null, 'messageCount': 1, 'id': 'ebf6ca4c-04cb-4ec9-81b1-e4b38a4021f5', 'title': '测试任务4', 'start': '2020-04-20 14:46:00', 'end': '2020-04-20 15:00:00', 'description': '啊啊所多撒多', 'textColor': '#475C6C', 'borderColor': 'rgba(255,0,0,0)', 'className': 'status-bg status-green message-count' }, { 'uuid': '64b4dfe5-8704-45dd-9df8-d245a38483e7', 'taskName': '测试任务5', 'taskType': '2', 'taskDescription': 'asdadsada', 'idcType': '信息港池外', 'taskStartTime': '2020-04-21 00:00:00', 'taskEndTime': '2020-04-24 00:00:00', 'orderId': 'c d c d', 'taskStatus': '3', 'taskResult': '2', 'content': null, 'creator': 'alauda(13800138003)', 'createTime': '2020-04-20 14:55:11', 'updater': null, 'updateTime': null, 'messageCount': 0, 'id': '64b4dfe5-8704-45dd-9df8-d245a38483e7', 'title': '测试任务5', 'start': '2020-04-21 00:00:00', 'end': '2020-04-24 00:00:00', 'description': 'asdadsada', 'textColor': '#475C6C', 'borderColor': 'rgba(255,0,0,0)', 'className': 'status-bg status-red' }, { 'uuid': 'ba3d39a3-dc25-43d3-92b8-280caad1d9ad', 'taskName': '周三测试2', 'taskType': '2', 'taskDescription': '周三测试2', 'idcType': '呼和浩特资源池', 'taskStartTime': '2020-04-22 00:00:00', 'taskEndTime': '2020-04-22 00:00:00', 'orderId': 'v f v f v f', 'taskStatus': '3', 'taskResult': '2', 'content': null, 'creator': 'alauda(13800138003)', 'createTime': '2020-04-22 13:22:38', 'updater': null, 'updateTime': null, 'messageCount': 1, 'id': 'ba3d39a3-dc25-43d3-92b8-280caad1d9ad', 'title': '周三测试2', 'start': '2020-04-22 00:00:00', 'end': '2020-04-22 00:00:00', 'description': '周三测试2', 'textColor': '#475C6C', 'borderColor': 'rgba(255,0,0,0)', 'className': 'status-bg status-red message-count' }, { 'uuid': '906725e8-1df1-4f1d-9bd8-b4f9377d1999', 'taskName': '周三测试！', 'taskType': '1', 'taskDescription': '周三测试！', 'idcType': '信息港资源池', 'taskStartTime': '2020-04-22 13:17:18', 'taskEndTime': '2020-04-22 13:17:20', 'orderId': 'qwe', 'taskStatus': '3', 'taskResult': '1', 'content': null, 'creator': 'alauda(13800138003)', 'createTime': '2020-04-22 13:21:56', 'updater': null, 'updateTime': null, 'messageCount': 0, 'id': '906725e8-1df1-4f1d-9bd8-b4f9377d1999', 'title': '周三测试！', 'start': '2020-04-22 13:17:18', 'end': '2020-04-22 13:17:20', 'description': '周三测试！', 'textColor': '#475C6C', 'borderColor': 'rgba(255,0,0,0)', 'className': 'status-bg status-green' }, { 'uuid': 'd479cd9f-a30c-40cc-80fe-7a7b30ab8dba', 'taskName': '测试任务0422', 'taskType': '1', 'taskDescription': '22313啊啊', 'idcType': '呼和浩特资源池', 'taskStartTime': '2020-04-23 05:10:02', 'taskEndTime': '2020-04-30 16:06:00', 'orderId': 'qwe', 'taskStatus': '3', 'taskResult': '2', 'content': null, 'creator': 'alauda(13800138003)', 'createTime': '2020-04-22 09:46:20', 'updater': null, 'updateTime': null, 'messageCount': 1, 'id': 'd479cd9f-a30c-40cc-80fe-7a7b30ab8dba', 'title': '测试任务0422', 'start': '2020-04-23 05:10:02', 'end': '2020-04-30 16:06:00', 'description': '22313啊啊', 'textColor': '#475C6C', 'borderColor': 'rgba(255,0,0,0)', 'className': 'status-bg status-red message-count' }],
                calendarEventDrop: info => {
                    this.dropEvent(info)
                },
                handleDatesRender: arg => {
                    this.getEventsList(arg.view)
                },

                dialogFormVisible: false,
                form: {
                    title: null,
                    start: null,
                    end: null
                },
                optTitle: '添加事件',
            }
        },
        created() {
            //
        },
        methods: {
            // 获取事件列表
            // getEventsList(info) {

            // },
            handleDateClick(arg) {
                this.dialogFormVisible = true
                this.optTitle = '新增事件'
                this.form.title = ''
                this.form.id = ''
                this.form.start = arg.date
                this.form.end = arg.date
            },
            handleEventClick(info) {
                info.el.style.borderColor = 'red'
                this.dialogFormVisible = true
                this.optTitle = '修改事件'
                this.form = {
                    id: info.event.id,
                    title: info.event.title,
                    start: info.event.start,
                    end: info.event.end,
                }
            },
            // 保存事件
            saveEvent() {

            },
            // 删除事件
            delEvent() {

            },
            // 拖动事件
            // dropEvent(info) {

            // }
        }
    }
</script>