import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

let routes = [{
    path: '/',
    redirect: '/index'
}];

let childrens = [];



/*维护圈*/
childrens.push({
	path: "/cmdb/user/getUsers1",
	component: resolve => require([`../maintain/maintain.vue`], resolve),
});

/*仓库*/
childrens.push({
	path: "/cmdb/user/getUsers2",
	component: resolve => require([`../repertory/repertory.vue`], resolve),
});

/*模型*/
childrens.push({
	path: "/cmdb/user/getUsers",
	component: resolve => require([`../module/module.vue`], resolve),
});

/*维护圈子配置*/
childrens.push({
	path: "/cmdb/maintain/configure",
	component: resolve => require([`../maintain/enterMaintain.vue`], resolve),
});

/*新增维护圈子*/
childrens.push({
	path: "/cmdb/maintain/addMaintain",
	component: resolve => require([`../maintain/addMaintain.vue`], resolve),
});

/*修改维护圈子*/
childrens.push({
	path: "/cmdb/maintain/editMaintain",
	component: resolve => require([`../maintain/editMaintain.vue`], resolve),
});

routes.push({
	path: '/index',
	component: resolve => require(['../portal/Home.vue'], resolve),
	children: childrens
});

export default new Router({
    routes: routes
})
