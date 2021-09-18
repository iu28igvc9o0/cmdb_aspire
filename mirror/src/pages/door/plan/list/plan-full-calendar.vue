
<template>
    <div>
        <!-- 日历 -->
        <full-calendar ref="calendar"
                       :defaultView="curView"
                       :timeGridEventMinHeight="40"
                       locale="zh-cn"
                       timeZone="local"
                       weekNumberCalculation="ISO"
                       :editable="false"
                       :droppable="false"
                       :allDaySlot="true"
                       allDayText="全天"
                       scrollTime="00:00"
                       :firstDay="0"
                       :views="views"
                       :contentHeight="contentHeight"
                       :titleFormat="titleFormat"
                       :slotLabelFormat="slotLabelFormat"
                       :eventTimeFormat="slotLabelFormat"
                       :header="header"
                       :buttonText="buttonText"
                       :plugins="calendarPlugins"
                       :events="calendarEvents"
                       @eventMouseEnter="handleMouseEnter"
                       @dateClick="handleDateClick"
                       @eventClick="handleTaskClick"
                       @eventDrop="calendarEventDrop"
                       @datesRender="handleDatesRender"></full-calendar>
    </div>
</template>
 
<script>
    import moment from 'moment'
    import FullCalendar from '@fullcalendar/vue'
    import dayGridPlugin from '@fullcalendar/daygrid'
    import timeGridPlugin from '@fullcalendar/timegrid'
    import interactionPlugin from '@fullcalendar/interaction'
    import '@fullcalendar/core/main.css'
    import '@fullcalendar/daygrid/main.css'
    import '@fullcalendar/timegrid/main.css'

    import tippy from 'tippy.js'
    import 'tippy.js/dist/tippy.css'

    export default {
        name: 'PlanFullCalendar',
        components: {
            FullCalendar
        },
        props: {
            dataList: {
                type: Array,
                default: []
            }
        },
        data() {
            return {
                curView: 'dayGridMonth',
                // 插件
                calendarPlugins: [
                    dayGridPlugin,
                    timeGridPlugin,
                    interactionPlugin
                ],
                // 头部功能
                header: {
                    left: 'title prev,today,next',
                    center: 'dayGridMonth,timeGridWeek,listView',
                    right: '',
                },
                // 查看全部任务按钮
                views: {
                    dayGridMonth: {
                        eventLimit: 4,
                        eventLimitText: '查看全部',
                        eventLimitClick: 'timeGridWeek',
                    },
                    listView: {
                        type: 'timeGridDay',
                        buttonText: '列表视图'
                    }
                },
                // 修改默认按钮文案
                buttonText: {
                    today: '今天',
                    month: '月视图',
                    week: '周视图',
                },
                // 周视图左侧时间格式
                titleFormat: {
                    year: 'numeric',
                    month: 'short'
                },
                // 周视图左侧时间格式
                slotLabelFormat: {
                    hour: '2-digit',
                    minute: '2-digit',
                    hour12: false,
                },
                // 事件列表数据
                calendarEvents: [],
                // 拖拽事件
                // calendarEventDrop: info => {
                //     // this.dropEvent(info)
                // },
                // 切换视图
                handleDatesRender: info => {
                    this.changeView(info)
                },
            }
        },
        watch: {
            dataList: {
                handler() {
                    this.getEventsList()
                },
                deep: true
            },
        },
        computed: {
            contentHeight() {
                let docHeight = document.body.clientHeight
                let cHeight = docHeight - 184
                return cHeight
            }
        },
        created() {
            this.getEventsList()
        },
        methods: {
            handleTimeFormat(timeStamp) {
                return moment(timeStamp).format('YYYY-MM-DD')
            },
            changeView(info) {
                let req = {
                    startDateTime: this.handleTimeFormat(info.view.activeStart),
                    endDateTime: this.handleTimeFormat(info.view.activeEnd),
                }

                this.curView = info.view.type
                // 显示自定义列表视图
                if (this.curView === 'listView') {
                    this.$emit('showListView')
                    return
                }
                // 显示月视图、周视图
                if (info.view.type === 'dayGridMonth') {
                    this.$emit('search', '1', req)
                } else if (info.view.type === 'timeGridWeek') {
                    this.$emit('search', '2', req)
                }
            },
            // 获取事件列表
            getEventsList() {
                this.calendarEvents = this.handleDataList(this.dataList)
            },
            // 处理事件列表数据
            handleDataList(list) {
                if (!list.length) {
                    return [{}]
                }
                list.forEach(item => {
                    let className = ''
                    item.id = item.uuid
                    item.title = item.taskName
                    item.start = item.taskStartTime
                    item.end = item.taskEndTime
                    item.description = item.taskDescription

                    // 跨天的任务，allDay设置为true，显示在顶部allDay区域；否则直接在当天一列中显示
                    let dateDiff = moment(item.end).diff(moment(item.start), 'day')
                    if (dateDiff > 0) {
                        item.allDay = true
                    }

                    item.textColor = '#475C6C'
                    item.borderColor = 'rgba(255,0,0,0)'
                    // 设置事件状态颜色
                    /** 
                     * taskStatus ：1-待执行 2-执行中 3-已执行
                     * taskResult ：1-执行成功 2-执行失败
                     */
                    if (item.taskStatus === '1') {
                        className = 'status-bg status-grey'
                    } else if (item.taskStatus === '2') {
                        className = 'status-bg status-blue'
                    } else if ((item.taskStatus === '3' || item.taskStatus === '4') && item.taskResult === '1') {
                        className = 'status-bg status-green'
                    } else if ((item.taskStatus === '3' || item.taskStatus === '4') && item.taskResult === '2') {
                        className = 'status-bg status-red'
                    }
                    // 添加反馈图标
                    if (item.messageCount) {
                        className = className + ' message-count'
                    }
                    item.classNames = className
                })
                return list
            },
            // 鼠标进入事件，显示标题tooltip
            handleMouseEnter(info) {
                tippy(info.el, {
                    content: info.event.title,
                    placement: 'top-start',
                    // arrow: false,
                    // 鼠标放在提示中提示不消失
                    // interactive: true,  
                })
            },
            // 点击日期空白处，新增任务
            handleDateClick(info) {
                this.$emit('handleDateClick', moment(info.dateStr).format('YYYY-MM-DD HH:mm:ss'))
            },
            // 点击任务，查看任务详情
            handleTaskClick(info) {
                this.$emit('handleTaskClick', info.event.extendedProps)
            },

        },
    }
</script>
 
<style  lang="scss" scoped>
</style>
