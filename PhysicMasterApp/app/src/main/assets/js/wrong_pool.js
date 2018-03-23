$(function () {
    LocalJSBridge.setup();
    var pg = {};
    pg.isLoadingList = false;// 是否正在加载列表
    pg.quCacheMap = {};// key:wrongPoolId(AppUserQuWrongPool.id)-value:qu
    pg.subjectCacheMap = {};// key:subjectId-value:subject
    pg.texCacheMap = {};// tex缓存Map
    pg.latestScrollTopInList = 0;// 最后滚动虽在高度
    pg.page_config = {dir_limit_count: 8, dir_name_max_length: 16};
    pg.focus_subject_id = window.FOCUS_SUBJECT_ID || 1;
    if (window.PAGE_CONFIG != null) {
        var wpc = window.PAGE_CONFIG;
        if (wpc.dir_limit_count != null && wpc.dir_limit_count > 0) {
            pg.page_config.dir_limit_count = wpc.dir_limit_count;
        }
        if (wpc.dir_name_max_length != null && wpc.dir_name_max_length > 0) {
            pg.page_config.dir_name_max_length = wpc.dir_name_max_length;
        }
    }
    var $page_carousel = $("#page_carousel");
    pg.onNativeGoBackFn = function () {
        // 当安卓点击返回键时，触发此方法
        $("#back_btn").trigger('click');
    }
    LocalJSBridge.ready(function () {
        try {
            LocalJSBridge.registerHandler("onGoBack", pg.onNativeGoBackFn);// 当安卓点击返回键时，触发此方法
        } catch (e) {
        }
        try {
            LocalJSBridge.hideLoading();// 隐藏Native的loading效果
        } catch (e) {
        }
        try {
            pg.loadListFn($('.filter_btn.active[data-id]').attr('data-id'), null);// JSBridge ready后加载列表第一页
        } catch (e) {
        }
    });
    pg.changePage = function (newId) {
        if (newId == null || newId == "" || newId == "#") {
            LocalJSBridge.quit({type: 0});
        } else {
            var $target = $("#" + newId);
            var $body = $("body");
            var $page_title = $("#page_title");
            var activeId = $body.attr("data-active-id");
            var newPageTitle = "";
            switch (activeId) {
                case 'qu_list_box':
                    // 当前在错题池列表页
                    if (newId == "qu_detail") {
                        newPageTitle = $target.attr("data-title");// 切换到 错题详情
                        $page_carousel.one('slid.bs.carousel', function () {
                            $(window).scrollTop(0);
                        })
                        $page_carousel.carousel("next");
                    }
                    break;
                case 'qu_detail':
                    // 当前在错题详情页
                    if (newId == "qu_list_box") {
                        newPageTitle = $target.attr("data-title");// 切换到 错题池 列表
                        var scrollTo = pg.latestScrollTopInList;
                        if ($('#qu_list').hasClass('first_loading')) {
                            // 已经重新加载列表页的第一页了，则滚动到顶部
                            scrollTo = 0;
                        }
                        $page_carousel.one('slid.bs.carousel', function () {
                            $(window).scrollTop(scrollTo);
                        })
                        $page_carousel.carousel("prev");
                    } else {
                        newPageTitle = $target.attr("data-title");// 切换到 录入错题 列表
                        $page_carousel.one('slid.bs.carousel', function () {
                            $(window).scrollTop(0);
                        })
                        $page_carousel.carousel("next");
                        $('.reason_item').removeClass('active');
                        $('.qu_dir_item').removeClass('active');
                        $('#qu_add .star').removeClass('active');
                    }
                    break;
                case 'qu_add':
                    // 当前在录入错题页
                    if (newId == "qu_detail") {
                        newPageTitle = $target.attr("data-title");// 切换到 错题详情
                        $page_carousel.one('slid.bs.carousel', function () {
                            $(window).scrollTop(0);
                        })
                        $page_carousel.carousel("prev");
                    } else if (newId == "qu_list_box") {
                        newPageTitle = $target.attr("data-title");// 切换到 列表页
                        $page_carousel.one('slid.bs.carousel', function () {
                            $(window).scrollTop(0);
                        })
                        $page_carousel.carousel(0);
                    }
                    break;
            }
            $page_title.html(newPageTitle);
            $body.attr("data-active-id", newId);
        }
    }
    pg.renderTex = function ($parentEle) {
        var $texJqArray = $($parentEle).find(".tex[data-expr]");
        var texElSize = $texJqArray.length;
        for (var i = 0; i < texElSize; i++) {
            var $tex = $($texJqArray[i]);
            var expr = $tex.attr("data-expr");
            if (expr != null && expr.length > 0) {
                try {
                    var html = pg.texCacheMap[expr];
                    if (html == null) {
                        html = katex.renderToString(expr);
                        pg.texCacheMap[expr] = html;
                    }
                    $tex.html(html);
                } catch (e) {
                    $tex.addClass("errorMessage").html("【显示出错：" + expr + "】");
                }
                try {
                    $tex.removeAttr("data-expr");
                } catch (e) {
                }
            }
        }
    }
    pg.renderListFn = function (data) {
        if (data != null) {
            var subjectId = data.subjectId;
            var next = data.next;
            var listJsonStr = data.listJsonStr;
            var list = JSON.parse(listJsonStr);
            if (list == null) {
                list = [];
            }
            if (next == null || next < 0 || list.length <= 0) {
                next = -1;
            }
            var _quCacheMap = pg.quCacheMap;
            if (next == 1) {
                // 第一页返回的next是1，如果总共就一页，则返回-1，就无法触发清理，但总共只有一页缓存的内容不多，不影响
                // 当前在请求第一页，则要把缓存的数据清理掉
                _quCacheMap = {};
                pg.quCacheMap = _quCacheMap;
            }
            if (next < 0) {
                $("#load_more").hide();
                $("#load_end").show();
            }
            $('.filter_btn[data-id="' + subjectId + '"]').attr('data-next', next);
            var $qu_list = $("#qu_list");
            var buf = [];
            for (var i = 0; i < list.length; i++) {
                var q = list[i];
                q.sid = subjectId;// 向问题缓存中增加科目ID
                _quCacheMap[(q.id + "")] = q;// 此q.id是AppUserQuWrongPool.id
                buf.push('<li class="qu_list_item main_padding_lr qu_list_item_new" data-id="' + q.id + '">');
                buf.push('<div class="qu_content">');
                buf.push(q.con);
                buf.push('</div>');
                buf.push('<div class="qu_info"><div class="qu_wrong_times_box">做错<span class="wrong_times">');
                buf.push(q.wt);
                buf.push('</span>次</div><div class="qu_time">');
                buf.push(q.ut);
                buf.push('</div></div></li>');
            }
            if ($qu_list.hasClass('first_loading')) {
                // 加载第一页
                if (list.length <= 0) {
                    // 第一页就没有返回数据，则显示“暂无数据”
                    $("#load_more").hide();
                    $("#load_end").hide();
                    $qu_list.html('<li class="qu_list_item main_padding_lr"><div class="no_data_div"><img class="wFull" src="./img/no_data.png"/></div></li>');
                } else {
                    $qu_list.html(buf.join(''));
                }
                $qu_list.removeClass('first_loading');
            } else {
                $qu_list.append(buf.join(''));
            }
            var $qu_list_item_array = $(".qu_list_item_new");
            $qu_list_item_array.on("tap click", function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                pg.openQuDetailFn(this);
            });
            pg.renderTex($qu_list_item_array);
            $qu_list_item_array.removeClass('qu_list_item_new');
        }
    }
    pg.loadListFn = function (subjectId, next) {
        if (pg.isLoadingList) {
            // 正在请求，则拒绝重复请求
            return;
        }
        var option = {
            subjectId: subjectId,
            success: function (data) {
                pg.isLoadingList = false;
                pg.renderListFn(data);
            },
            error: function (json) {
                pg.isLoadingList = false;
                if (json && json.msg) {
                    $.toptip(json.msg, null, 'error');
                }
            }
        };
        if (next != null && !isNaN((next = next * 1)) && next > 0) {
            option.next = next;
        } else {
            $("#load_more").show();
            $("#load_end").hide();
            $("#qu_list").addClass('first_loading');// 加载第一页
        }
        pg.isLoadingList = true;
        //{
        //    var params = {subjectId: option.subjectId};
        //    if (option.next != null) {
        //        params.next = option.next;
        //    }
        //    $.ajax({
        //        url: "/c/p/test/wb_getPoolQuList",
        //        data: params,
        //        type: "POST",
        //        dataType: "json",
        //        success: function (json) {
        //            option.success(json.data);
        //        },
        //        error: function (jqXHR, textStatus, errorThrown) {
        //            option.error({code: 0, msg: "网络故障"});
        //        }
        //    });
        //}
        LocalJSBridge.loadWrongPool(option);
    }
    pg.removeFromWrongPool_doing = false;// 是否正在请求删除，避免并发请求
    pg.removeQuFn = function (wrongPoolId, successCallbackFn) {
        // 删除题目
        if (pg.removeFromWrongPool_doing) {
            return;// 避免重复请求
        }
        var $loading_toast = $('#loading_toast');
        $loading_toast.show();
        var option = {
            id: wrongPoolId,
            success: function (data) {
                pg.removeFromWrongPool_doing = false;
                $loading_toast.hide();
                successCallbackFn(data);
            },
            error: function (json) {
                pg.removeFromWrongPool_doing = false;
                $loading_toast.hide();
                if (json && json.msg) {
                    $.toptip(json.msg, null, 'error');
                }
            }
        };
        pg.removeFromWrongPool_doing = true;// 是否正在请求删除，避免并发请求
        LocalJSBridge.removeFromWrongPool(option);
    }
    pg.saveQuWrong_doing = false;// 避免重复请求
    pg.quAddSubmitFn = function () {
        // 录入错题提交方法
        if (pg.saveQuWrong_doing) {
            return;// 避免重复请求
        }
        var $qu_add = $('#qu_add');
        var wrongPoolId = $qu_add.attr('data-id');
        if (wrongPoolId == null || wrongPoolId == '' || wrongPoolId == '0') {
            return;
        }
        var qu = pg.quCacheMap[wrongPoolId];
        if (qu == null) {
            return;
        }
        var subjectId = qu.sid;
        var $reason_item_active = $qu_add.find('.reason_item.active');// 选中的错误原因
        if ($reason_item_active.length <= 0) {
            $.toptip('请选择错误原因', null, 'error');
            return;
        }
        var $difficulty_level_star_active = $qu_add.find(".difficulty_level_box .star.active");
        if ($difficulty_level_star_active.length <= 0) {
            $.toptip('请选择难易程度', null, 'error');
            return;
        }
        var $master_level_star_active = $qu_add.find(".master_level_box .star.active");
        if ($master_level_star_active.length <= 0) {
            $.toptip('请选择掌握程度', null, 'error');
            return;
        }
        var $qu_dir_item_active = $qu_add.find('.qu_dir_item.active');// 选中的错误原因
        if ($qu_dir_item_active.length <= 0) {
            $.toptip('请选择标签目录', null, 'error');
            return;
        }
        var $loading_toast = $('#loading_toast');
        $loading_toast.show();
        var tagIdArrayJson = null;
        if ($reason_item_active.length > 0) {
            var tmpArray = [];
            for (var i = 0; i < $reason_item_active.length; i++) {
                var $t = $($reason_item_active[i]);
                var dirIdTmp = $t.attr('data-id');
                if (dirIdTmp != null && dirIdTmp != '' && !isNaN((dirIdTmp = dirIdTmp * 1))) {
                    tmpArray.push(dirIdTmp);
                }
            }
            if (tmpArray.length > 0) {
                tagIdArrayJson = JSON.stringify(tmpArray);
            }
        }
        var _callbackFn = function (data) {
            $.toptip('录入成功', 2000, 'success');

            var $filter_btn = $(".filter_btn.active[data-id]");
            var sb = pg.subjectCacheMap[subjectId];
            sb.c = sb.c - 1;
            if (sb.c < 0) {
                sb.c = 0;
            }
            var countStr = sb.c;
            if (countStr > 99) {
                countStr = "99+";
            }
            $filter_btn.find('.qu_count_box').attr('data-count', sb.c).html('(' + countStr + ')');

            $('#qu_detail').attr('data-id', '0');
            $('#qu_add').attr('data-id', '0');
            pg.quCacheMap[wrongPoolId] = null;
            pg.loadListFn($(".filter_btn.active[data-id]").attr('data-id'), null);
            pg.changePage('qu_list_box');
            if (data != null) {
                if (data.quWrongDirs != null) {
                    var sb = pg.subjectCacheMap[subjectId];
                    if (sb != null) {
                        sb.dirs = data.quWrongDirs;
                    }
                }
            }
        }
        var option = {
            success: function (data) {
                pg.saveQuWrong_doing = false;// 避免重复请求
                $loading_toast.hide();
                _callbackFn(data);
            },
            error: function (json) {
                pg.saveQuWrong_doing = false;// 避免重复请求
                $loading_toast.hide();
                $.toptip((json != null && json.msg != null) ? json.msg : '请求失败', null, 'error');
            }
        };
        option.id = wrongPoolId;
        option.subjectId = subjectId;
        if (tagIdArrayJson != null && tagIdArrayJson != '') {
            option.tagId = tagIdArrayJson;
        }
        option.difficultyLevel = $difficulty_level_star_active.length;
        option.masterLevel = $master_level_star_active.length;
        if ($qu_dir_item_active.length > 0) {
            option.dirId = $qu_dir_item_active.attr('data-id');
        }
        pg.saveQuWrong_doing = true;// 避免重复请求
        LocalJSBridge.saveQuWrong(option);
    }
    pg.renderReasonFn = function (reasonList) {
        if (reasonList == null) {
            return;
        }
        var rsBuf = [];
        for (var i = 0; i < reasonList.length; i++) {
            var rs = reasonList[i];
            rsBuf.push('<li><div class="reason_item" data-id="' + rs.id + '">' + rs.n + '</div></li>')
        }
        $('#qu_add .error_reason_box').html(rsBuf.join(''));
        $('.reason_item').on('tap click', function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            var $this = $(this);
            if ($this.hasClass('active')) {
                $this.removeClass('active');
            } else {
                $this.addClass('active');
            }
        });
    }
    {
        var sbs = window.SUBJECT_LIST;
        if (sbs != null) {
            var $filter_ul = $("#filter_bar .filter_ul");
            var sbBuf = [];
            for (var i = 0; i < sbs.length; i++) {
                var s = sbs[i];
                pg.subjectCacheMap[s.id] = s;
                var countStr = s.c;
                if (countStr > 99) {
                    countStr = "99+";
                }
                sbBuf.push('<li id="filter' + s.id + '"><div class="filter_btn' + (pg.focus_subject_id == s.id ? ' active' : '')
                    + '" data-id="' + s.id + '"><span class="subject_name">' + s.n
                    + '</span><span class="qu_count_box" data-count="' + s.c + '">(' + countStr + ')</span></div></li>');
            }
            $filter_ul.html(sbBuf.join(''));
            if (sbs.length > 0) {
                var ul_width = $filter_ul.width();
                var all_li_width = 0;
                for (var i = 0; i < sbs.length; i++) {
                    var $filter = $("#filter" + sbs[i].id);
                    var li_width_with_padding = $filter.innerWidth();
                    all_li_width += li_width_with_padding;
                }
                if (all_li_width <= ul_width) {
                    // 可以均分布局，TODO 唯一的问题，未考虑如果某科目字数较多，平分后的宽度不足以存放这么多字；经确认后续的科目名称均为4个字
                    var li_width = null;
                    if (sbs.length == 1) {
                        li_width = '100%';
                    } else if (sbs.length == 2) {
                        li_width = '50%';
                    } else if (sbs.length == 3) {
                        li_width = '33.333333%';
                    } else if (sbs.length == 4) {
                        li_width = '25%';
                    } else {
                        var n = parseInt((100 / sbs.length) * 1000000) / 1000000;//保留6位小数，后面的直接舍去
                        li_width = s + '%';
                    }
                    for (var i = 0; i < sbs.length; i++) {
                        var $filter = $("#filter" + sbs[i].id);
                        $filter.css("width", li_width);
                    }
                }
            }
            {
                // 判断是否要滚动科目条
                var scrollLeft = null;
                var $filter_btn_active = $(".filter_btn.active");
                var offsetLeft = ($filter_btn_active.offset().left);// 当前科目的左边位置
                if (offsetLeft < 15) {
                    // 当前科目左边部分隐藏，则要向右滚动
                    scrollLeft = offsetLeft - 15;
                } else {
                    // 判断当前科目右边是否因此
                    var btnWidth = $filter_btn_active.innerWidth();// 当前科目的宽度
                    var btnRight = (btnWidth + offsetLeft) + 15; // 当前科目右边位置+15
                    var filter_bar_outerWidth = $("#filter_bar").outerWidth();
                    if (btnRight > (filter_bar_outerWidth + 1)) {// +5是考虑到，btnRight可能由于计算不精确，会比filter_bar_outerWidth略大零点几
                        // 当前科目有一部分被隐藏，则要向左滚动
                        scrollLeft = btnRight - filter_bar_outerWidth;
                    }
                }
                if (scrollLeft != null) {
                    if (scrollLeft < 0) {
                        scrollLeft = 0;
                    }
                    $("#filter_bar").animate({scrollLeft: scrollLeft + "px"}, 300);
                }
            }
            $filter_ul.removeClass('hidden');
        }
        pg.renderReasonFn(window.WRONG_REASON_LIST);
        $(".star_box").each(function () {
            var $star_box = $(this);
            var $stars = $star_box.find(".star");
            $stars.on('tap click', function () {
                var thisDom = this;
                $stars.removeClass('active');
                for (var index = 0; index < $stars.length; index++) {
                    var $tmpStar = $($stars[index]);
                    $tmpStar.addClass('active');
                    if (thisDom == $stars[index]) {
                        // 被点击星星以及其左边的都点亮
                        break;
                    }
                }
            });
        })
    }
    pg.openQuDetailFn = function (qu_list_item_dom) {
        var $qu_list_item = $(qu_list_item_dom);
        var $qu_detail = $("#qu_detail");
        var quId = $qu_list_item.attr('data-id');
        if (quId == null || quId == "") {
            return;
        }
        var qu = pg.quCacheMap[quId];
        $qu_detail.find('.qu_title').html(qu.con);
        $qu_detail.find('.qu_item1 .qu_item_inner').html(qu.it1);
        $qu_detail.find('.qu_item2 .qu_item_inner').html(qu.it2);
        $qu_detail.find('.qu_item3 .qu_item_inner').html(qu.it3);
        $qu_detail.find('.qu_item4 .qu_item_inner').html(qu.it4);
        $qu_detail.find('.qu_explain').html(qu.ae);
        var $firstP = $qu_detail.find(".qu_explain p:eq(0)");
        var firstPHtml = null;
        if ($firstP.length > 0 && (firstPHtml = $firstP.html().trim()).indexOf("解析") == 0 && firstPHtml.length == 3) {
            $firstP.css("font-weight", "600").css("font-size", "16px").css("line-height", "24px").css("color", "#444");
        }
        $qu_detail.find('.qu_item').removeClass('correct').removeClass('wrong');
        var standard_answer = qu.an;
        if (standard_answer != null && standard_answer != '') {
            standard_answer = standard_answer.toUpperCase();
            $qu_detail.find('.qu_item[data-code="' + standard_answer + '"]').addClass('correct');
        }
        var user_answer = qu.uan;
        if (user_answer != null && user_answer != '') {
            user_answer = user_answer.toUpperCase();
            if (user_answer != standard_answer) {
                $qu_detail.find('.qu_item[data-code="' + user_answer + '"]').addClass('wrong');
            }
        }
        $qu_detail.attr('data-id', qu.id);
        pg.renderTex($qu_detail);
        pg.changePage('qu_detail');
    }
    pg.renderDirFn = function (dirs) {
        if (dirs == null || dirs.length <= 0) {
            dirs = [];
        }
        var $qu_add = $('#qu_add');
        var $qu_dir_box = $qu_add.find('.qu_dir_box');
        var dirBuf = [];
        for (var i = 0; i < dirs.length; i++) {
            var d = dirs[i];
            dirBuf.push('<li><div class="qu_dir_item" data-id="' + d.id + '">' + d.n + '</div></li>');
        }
        $qu_dir_box.html(dirBuf.join(''));
        if (dirs.length >= pg.page_config.dir_limit_count) {
            // 已创建的标签目录数量已达到限制数量，则禁止再创建了
            $('#qu_add .qu_dir_add_box').hide();
        } else {
            $('#qu_add .qu_dir_add_box').show();
        }
        var $all_qu_dir_item = $('.qu_dir_item');
        $all_qu_dir_item.on('tap click', function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $all_qu_dir_item.removeClass('active');
            $(this).addClass('active');
        });
    }
    pg.openQuAddFn = function (wrongPoolId) {
        var qu = pg.quCacheMap[wrongPoolId];
        var sb = pg.subjectCacheMap[qu.sid];
        pg.renderDirFn(sb.dirs);
        $('#qu_add').attr('data-id', wrongPoolId);
        pg.changePage('qu_add');
    }
    $('.qu_dir_add_input').attr('maxlength', pg.page_config.dir_name_max_length);
    $page_carousel.carousel({interval: false, keyboard: false});
    $("#back_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        var $prev_page = $page_carousel.find(".item.active").prev();
        if ($prev_page == null || $prev_page.length <= 0) {
            // 已经退回到第一页了
            pg.changePage(null);
        } else {
            var prevId = $prev_page.attr('id');
            pg.changePage(prevId);
        }
    })
    $(".filter_btn[data-id]").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        if (pg.isLoadingList) {
            // 正在加载中，则不触发切换
            return;
        }
        var $this = $(this);
        var $active_filter_btn = $(".filter_btn.active");
        if (this != $active_filter_btn[0]) {
            $active_filter_btn.removeClass("active");
            $this.addClass("active");
            {
                // 判断是否要滚动科目条
                var scrollLeft = null;
                var $filter_btn_active = $(".filter_btn.active");
                var offsetLeft = ($filter_btn_active.offset().left);// 当前科目的左边位置
                if (offsetLeft < 15) {
                    // 当前科目左边部分隐藏，则要向右滚动
                    scrollLeft = offsetLeft - 15;
                } else {
                    // 判断当前科目右边是否因此
                    var btnWidth = $filter_btn_active.innerWidth();// 当前科目的宽度
                    var btnRight = (btnWidth + offsetLeft) + 15; // 当前科目右边位置+15
                    var filter_bar_outerWidth = $("#filter_bar").outerWidth();
                    if (btnRight > (filter_bar_outerWidth + 1)) {// +5是考虑到，btnRight可能由于计算不精确，会比filter_bar_outerWidth略大零点几
                        // 当前科目有一部分被隐藏，则要向左滚动
                        scrollLeft = btnRight - filter_bar_outerWidth;
                    }
                }
                if (scrollLeft != null) {
                    if (scrollLeft < 0) {
                        scrollLeft = 0;
                    }
                    $("#filter_bar").animate({scrollLeft: scrollLeft + "px"}, 300);
                }
            }
            pg.loadListFn($this.attr('data-id'), null);
        }
        //else {
        //    // 当前点击就是当前科目，则什么都不做
        //}
    });
    $(window).scroll(function () {
        var window_scrollTop = $(window).scrollTop();
        var currentPageId = $('body').attr("data-active-id");
        if (currentPageId == 'qu_list_box') {
            pg.latestScrollTopInList = window_scrollTop;
            if (!pg.isLoadingList) {
                // 不在加载中
                var window_height = $(window).height()
                var load_more_offset = $("#load_more").offset();
                if ((load_more_offset.top - (window_scrollTop + window_height)) < 50) {
                    var $filter_btn = $(".filter_btn.active[data-id]");
                    var next = $filter_btn.attr('data-next');
                    if (next != '-1') {
                        // 没有到最后一页
                        pg.loadListFn($filter_btn.attr('data-id'), next);
                    }
                }
            }
        }
    });
    $("#remove_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $("#confirm_remove_dialog").fadeIn('fast');
    })
    $("#do_remove_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        var wrongPoolId = $('#qu_detail').attr('data-id');
        if (wrongPoolId != null && wrongPoolId != '' && wrongPoolId != '0') {
            $('#confirm_remove_dialog').fadeOut('fast');
            pg.removeQuFn(wrongPoolId, function () {
                var $filter_btn = $(".filter_btn.active[data-id]");
                var subjectId = $filter_btn.attr('data-id') * 1;
                var sb = pg.subjectCacheMap[subjectId];
                sb.c = sb.c - 1;
                if (sb.c < 0) {
                    sb.c = 0;
                }
                var countStr = sb.c;
                if (countStr > 99) {
                    countStr = "99+";
                }
                $filter_btn.find('.qu_count_box').attr('data-count', sb.c).html('(' + countStr + ')');
                $('#qu_detail').attr('data-id', '0');
                $('#qu_add').attr('data-id', '0');
                pg.quCacheMap[wrongPoolId] = null;
                pg.loadListFn($(".filter_btn.active[data-id]").attr('data-id'), null);
                pg.changePage('qu_list_box');
            });
        }
    })
    $("#add_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        pg.openQuAddFn($('#qu_detail').attr('data-id'));
    })
    $("#add_submit_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        pg.quAddSubmitFn();
    })
    $("#add_dir_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        var $add_dir_dialog = $("#add_dir_dialog");
        $add_dir_dialog.show();
        $add_dir_dialog.find(".qu_dir_add_input").focus().val('');
    })
    pg.addWrongDir_doing = false;// 避免重复请求
    $("#do_add_dir_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        if (pg.addWrongDir_doing) {
            return;
        }
        var $add_dir_dialog = $("#add_dir_dialog");
        var $qu_dir_add_input = $add_dir_dialog.find(".qu_dir_add_input");
        var dirName = $qu_dir_add_input.val();
        if (dirName == null || dirName == '' || $.trim(dirName) == '') {
            $.toptip('请输入目录名称', null, 'error');
            return;
        }
        var $qu_add = $('#qu_add');
        var wrongPoolId = $qu_add.attr('data-id');
        if (wrongPoolId == null || wrongPoolId == '' || wrongPoolId == '0') {
            return;
        }
        var qu = pg.quCacheMap[wrongPoolId];
        if (qu == null) {
            return;
        }
        var subjectId = qu.sid;
        pg.addWrongDir_doing = true;
        var option = {
            subjectId: subjectId,
            dirName: dirName,
            success: function (data) {
                pg.addWrongDir_doing = false;
                $add_dir_dialog.hide();
                $.toptip('新建成功', 2000, 'success');
                var quWrongDirs;
                if (data != null && (quWrongDirs = data.quWrongDirs) != null && quWrongDirs.length > 0) {
                    var dirs = [];
                    for (var i = 0; i < quWrongDirs.length; i++) {
                        var tmpBean = quWrongDirs[i];
                        if (subjectId == tmpBean.subjectId) {
                            var appUserQuWrongDirDtoList = tmpBean.appUserQuWrongDirDtoList;
                            if (appUserQuWrongDirDtoList != null && appUserQuWrongDirDtoList.length > 0) {
                                for (var j = 0; j < appUserQuWrongDirDtoList.length; j++) {
                                    var d = appUserQuWrongDirDtoList[j];
                                    if (d.dirId != null && d.dirName != null && d.dirName != '') {
                                        dirs.push({id: d.dirId, n: d.dirName});
                                    }
                                }
                            }
                            break;
                        }
                    }
                    var subject = pg.subjectCacheMap[subjectId];
                    subject.dirs = dirs;
                    pg.renderDirFn(dirs);
                    $('.qu_dir_item:last').trigger('click');
                }
            },
            error: function (json) {
                pg.addWrongDir_doing = false;
                var msg = "新建失败";
                if (json != null && json.msg != null && json.msg != '') {
                    msg = json.msg;
                }
                $.toptip(msg, null, 'error');
            }
        };
        LocalJSBridge.addWrongDir(option);
    })
    $("#page_title").on("dblclick", function () {
        // 双击顶部标题，则回到顶部
        $("body,html").animate({scrollTop: 0}, 500);
    })
    $("#filter_bar").on("swipeleft swiperight", function ($event) {
        // 在科目切换栏，横向滑动，禁止事件冒泡
        $event.preventDefault();
        $event.stopPropagation();
    });
    $("#page_carousel").on("swipeleft", function ($event) {
        // 切换到右边一个科目
        var $filter_ul = $("#filter_bar .filter_ul");
        var $active_li = $filter_ul.find(".filter_btn.active").parents("li:eq(0)");
        var $next_li = $active_li.next();
        if ($next_li != null && $next_li.length > 0) {
            $next_li.find(".filter_btn").trigger("click");
        }
        $event.preventDefault();
        $event.stopPropagation();
    });
    $("#page_carousel").on("swiperight", function ($event) {
        // 切换到左边一个科目
        var $filter_ul = $("#filter_bar .filter_ul");
        var $active_li = $filter_ul.find(".filter_btn.active").parents("li:eq(0)");
        var $prev_li = $active_li.prev();
        if ($prev_li != null && $prev_li.length > 0) {
            $prev_li.find(".filter_btn").trigger("click");
        }
        $event.preventDefault();
        $event.stopPropagation();
    });
});