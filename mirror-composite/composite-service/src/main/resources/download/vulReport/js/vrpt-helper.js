var lastWidth = 0;
var execFlag = 0;
var treeId = 0;
var showTreeFlag = 1;
MainMenu.class_normal = '';
MainMenu.class_over = '';
MainMenu.class_sel = 'select1';

MainMenu.main = null;
MainMenu.sub = null;
MainMenu.mout = null;
MainMenu.mover = null;

function MainMenu() {}

MainMenu.mouse_on = function (c) {
	c.className = MainMenu.class_over;
}

MainMenu.mouse_out = function (c) {
	c.className = MainMenu.class_normal;
}

MainMenu.hide_sub = function (sub) {
	sub.style.display = 'none';
}

MainMenu.show_sub = function (sub) {
	sub.style.display = '';
}

MainMenu.click = function (c) {
	var tid = jQuery(c).attr("treeid");
	var newsub = document.getElementById('tree' + tid);
	if (c == MainMenu.main) {
		if (MainMenu.class_sel == c.className) {
			MainMenu.hide_sub(newsub);
			
			c.className = MainMenu.class_over;
			c.onmouseout = MainMenu.mout;
			c.onmouseover = MainMenu.mover;
			
			MainMenu.main = null;
		} else {
			MainMenu.show_sub(newsub);
			
			MainMenu.mout = c.onmouseout;
			MainMenu.mout = c.onmouseover;
			
			c.className = MainMenu.class_sel;
			c.onmouseout = null;
			c.onmouseover = null;
		}
	} else {
		if (null != MainMenu.main) {
			MainMenu.hide_sub(MainMenu.sub);

			MainMenu.main.className = MainMenu.class_normal;
			MainMenu.main.onmouseout = MainMenu.mout;
			MainMenu.main.onmouseover = MainMenu.mover;
		}

		MainMenu.main = c;
		MainMenu.sub = newsub;
		MainMenu.mout = c.onmouseout;
		MainMenu.mover = c.onmouseover;

		MainMenu.show_sub(newsub);
		c.className = MainMenu.class_sel;
		c.onmouseout = null;
		c.onmouseover = null;
	}
}


MenuItem.class_normal = '';
MenuItem.class_over = '';
MenuItem.class_sel = 'select2';

MenuItem.main = null;
MenuItem.mout = null;
MenuItem.mover = null;
function MenuItem() {}

MenuItem.mouse_on = function (c) {
	c.className = MenuItem.class_over;
}

MenuItem.mouse_out = function (c) {
	c.className = MenuItem.class_normal;
}

MenuItem.click = function (c) {
	if (c == MenuItem.main) {
		return;
	}
	
	if (null != MenuItem.main) {
		MenuItem.main.className = MenuItem.class_normal;
		MenuItem.main.onmouseout = MenuItem.mout;
		MenuItem.main.onmouseover = MenuItem.mover;
	}

	MenuItem.main = c;
	MenuItem.mout = c.onmouseout;
	MenuItem.mover = c.onmouseover;

	c.className = MenuItem.class_sel;
	c.onmouseout = null;
	c.onmouseover = null;
}


function enableExecFlag(){
	execFlag = 1;
}


function resizeWindow(){
	
	var totalWidth = document.body.clientWidth;
	var rightWidth = totalWidth;
	var leftWidth = $("#divTree").width();
	var divTop = document.getElementById("divTop"); 
	jQuery("#divHead").css("width", totalWidth);
	
	if (1 == showTreeFlag){
		rightWidth =  totalWidth- leftWidth;	
		divTop.className = "leftside_hasmenu";
	}else{
		divTop.className = "leftside_nomenu";
	}
	
	var totalHeight = document.body.clientHeight;
	var topHeight = $("#divHead").height();
	var bodyHeight = totalHeight-topHeight;
	
	jQuery("#rightPanel").css("width", rightWidth);
	if ($.browser.msie){
		jQuery("#divContent").css("width", "99%");
	}else if ($.browser.mozilla){
		jQuery("#divContent").css("width", "95%");
	}else if ($.browser.safari){
		jQuery("#divContent").css("width", "95%");
	}else {
		jQuery("#divContent").css("width", "95%");
	}
	jQuery("#divTree").css("height", bodyHeight);
	jQuery("#divTree").css("overflow-y", "auto");
	jQuery("#divContent").css("height", bodyHeight);
	jQuery("#divContent").css("overflow-y", "auto");
	jQuery("#divContent").css("float", "right");

	
}


	
function addPrintCtrl(){
	var grpShow;
	var grpHide;
	var btnPrint;
	var btnMenu;
	var divTop;
	
	divTop = document.createElement("DIV");
	divTop.id="divTop";
	divTop.className="topbanner";
	
	var divHead = document.getElementById("divHead");	
	divHead.appendChild(divTop);
		
	grpShow = $("<div id='groupShowDiv' class='top_button_group' style='display:block;'></div>");
	$("#divTop").append(grpShow);
	btnPrint = $("<a href='#' id='btnPrint' onclick='printContent()' class='print_show' title='´òÓ¡'></a>");
	btnMenu = $("<a href='#' onclick='hideTreeMenu()' class='menu_hide'></a>");
	$('#groupShowDiv').append(btnPrint);
	$('#groupShowDiv').append(btnMenu);
	
	grpShow = $("<div id='groupHideDiv' class='top_button_group' style='display:none;'></div>");
	$("#divTop").append(grpShow);
	btnPrint = $("<a href='#' id='btnPrint' onclick='printContent()' class='print_show' title='´òÓ¡'></a>");
	btnMenu = $("<a href='#' onclick='showTreeMenu()' class='menu_show'></a>");
	$('#groupHideDiv').append(btnPrint);
	$('#groupHideDiv').append(btnMenu);

}




function initWindow(){
	if (execFlag == 1){
		$("body").css("overflow", "hidden");
		addPrintCtrl();
		showTreeMenu();
		resizeWindow();
		$(window).resize(function(){
  			resizeWindow();
		});
		
		setTimeout(scrollTop, 1);
	}
	
}

function scrollTop(){
	var cdiv = document.getElementById("divContent");
	jQuery(cdiv).scrollTop(0);
	lastTop =  $("#divContent").offset().top;
}

function changePositon(obj){

	var cdiv = document.getElementById("divContent");
    var cs = jQuery(obj).attr("linkid");
    var cobj = document.getElementById(cs);
    var dtop = jQuery(cobj).offset().top;
    var itop = jQuery(cdiv).scrollTop();
    var topHeight = $("#divTop").height();
	jQuery(cdiv).scrollTop(dtop + itop -topHeight);

}

function addChapterItem(parentItem, childItem, itemName, itemId){
	if (parentItem){
		var lstItem = document.createElement("LI");
		lstItem.className="DirChapter";
		var objItem = document.createElement("A");
		objItem.linkid = itemId;
		objItem.treeid = treeId;
		objItem.innerHTML=itemName;
		
		objItem.onclick=function(event){
			MainMenu.click(objItem);
			changePositon(objItem);
			
		}
		objItem.onMouseOut=function(){
			MainMenu.mouse_out(objItem);
		}
		objItem.onMouseOver=function(){
			MainMenu.mouse_on(objItem);
		}
		
		childItem.className="VDirChild";
		var idVal = "tree" + treeId;
		childItem.id = idVal;
		idVal = "#"+ idVal;
		treeId++;
		lstItem.appendChild(objItem);
		lstItem.appendChild(childItem);
		parentItem.appendChild(lstItem);
		jQuery(idVal).css("display", "none");
	}
}

function addSectionItem(parentItem, itemName, itemId){
	if (parentItem){
		var lstItem = document.createElement("LI");
		lstItem.className="DirSection";
		var objItem = document.createElement("A");
		objItem.linkid = itemId;
		objItem.innerHTML=itemName;
		objItem.onclick=function(event){
			MenuItem.click(objItem);
			changePositon(objItem);
		}
		objItem.onMouseOut=function(){
			MenuItem.mouse_on(objItem);
		}
		objItem.onMouseOver=function(){
			MenuItem.mouse_out(objItem);
		}
		lstItem.appendChild(objItem);
		parentItem.appendChild(lstItem);
	}
}

function printContent()
{
	var headstart = "<html><head>"
	var cssstr = "<link rel=\"stylesheet\" type=\"text/css\" href=\"./css/rpthtml_treeview.css\"/>";
	var headend = "<title></title></head><body>";
	var headstr = headstart + cssstr + headend;
	var footstr = "</body></html>";	
	var newstr = document.all.item("divContent").innerHTML;
	var docstr = headstr + newstr + footstr;	
	var newwin=window.open('','_blank',
							"top=100,left=100,width=700,height=500,toolbar=no,menubar=no,scrollbars=yes");
							
   	newwin.opener = null;
   	newwin.document.write(docstr);
   	newwin.focus();
   	newwin.document.close();
	newwin.print();
	newwin.close();
	
}

function showTreeMenu(){
	showTreeFlag = 1;
	jQuery("#groupShowDiv").css("display", "block");
	jQuery("#groupHideDiv").css("display", "none");
	var divTop = document.getElementById("divTop"); 
	divTop.className = "leftside_hasmenu";
	jQuery("#divTree").css("display", "block");
	var totalWidth = $(document).width();
	var rightWidth = totalWidth;
	var leftPanel = document.getElementById("divTree");
	var leftWidth = $(leftPanel).width();
	rightWidth =  totalWidth- leftWidth;
	jQuery("#rightPanel").css("width", rightWidth);
}

function hideTreeMenu(){
	showTreeFlag = 0;
	jQuery("#groupShowDiv").css("display", "none");
	jQuery("#groupHideDiv").css("display", "block");
	var divTop = document.getElementById("divTop"); 
	divTop.className = "leftside_nomenu";
	jQuery("#divTree").css("display", "none");
	var docWidth = $(document).width();
	jQuery("#rightPanel").css("width", docWidth);
	jQuery("#divContent").css("overflow-y", "auto");
	jQuery("#divContent").css("float", "right");

	
}
	