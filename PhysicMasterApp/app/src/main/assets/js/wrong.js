$(function () {
    LocalJSBridge.setup();
    var pg = {};
    pg.dirId = window.DIR_ID;// 当前目录ID
    pg.dirName = window.DIR_NAME || "目录";// 当前目录名称
    pg.subjectId = window.SUBJECT_ID;
    pg.inArchivalDir = window.IN_ARCHIVAL_DIR === true;// 当前是否在“掌握归档”目录下
    pg.isLoadingList = false;// 是否正在加载列表
    pg.quCacheMap = {};// key:wrongId(AppUserQuWrongPool.id)-value:qu
    pg.texCacheMap = {};// tex缓存Map
    pg.latestScrollTopInList = 0;// 最后滚动虽在高度
    // self_import_content_max_length_in_list：自录错题在列表中文本显示的最大字数
    pg.page_config = {self_import_content_max_length_in_list: 45};
    if (pg.inArchivalDir) {
        // 当前在“掌握归档”目录下
        $('body').addClass('in_archival_dir');
    }
    $("#page_title").html(pg.dirName);
    $("title").html(pg.dirName);
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
            var $filter_btn = $('.filter_btn.active[data-sort]');
            var sort = $filter_btn.attr('data-sort');
            var desc = $filter_btn.hasClass('sort_desc_active') ? 1 : 0;
            pg.loadListFn(sort, desc, null);// JSBridge ready后加载列表第一页
        } catch (e) {
        }
    });
    pg.changePage = function (newId) {
        if (newId == null || newId == "" || newId == "#") {
            LocalJSBridge.quit({type: 0});
        } else {
            var $target = $("#" + newId);
            var $body = $("body");
            //var $page_title = $("#page_title");
            var activeId = $body.attr("data-active-id");
            //var newPageTitle = "";
            switch (activeId) {
                case 'qu_list_box':
                    // 当前在错题池列表页
                    if (newId == "qu_detail") {
                        //newPageTitle = $target.attr("data-title");// 切换到 错题详情
                        $page_carousel.one('slid.bs.carousel', function () {
                            $(window).scrollTop(0);
                        })
                        $page_carousel.carousel("next");
                    }
                    break;
                case 'qu_detail':
                    // 当前在错题详情页
                    if (newId == "qu_list_box") {
                        //newPageTitle = $target.attr("data-title");// 切换到 错题池 列表
                        var scrollTo = pg.latestScrollTopInList;
                        if ($('#qu_list').hasClass('first_loading')) {
                            // 已经重新加载列表页的第一页了，则滚动到顶部
                            scrollTo = 0;
                        }
                        $page_carousel.one('slid.bs.carousel', function () {
                            $(window).scrollTop(scrollTo);
                        });
                        $page_carousel.carousel("prev");
                        $("body").removeClass("show_wrong_item_box");// 隐藏错题答案，解析部分
                    }
                    break;
            }
            //$page_title.html(newPageTitle);
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
            $('.filter_btn.active').attr('data-next', next);
            var $qu_list = $("#qu_list");
            var buf = [];
            for (var i = 0; i < list.length; i++) {
                var q = list[i];
                var type = q.type;
                var content = q.con;
                if (type == 1) {
                    // 自录错题
                    var maxLength = pg.page_config.self_import_content_max_length_in_list;
                    if (content != null && content.length > maxLength) {
                        content = content.substr(0, maxLength) + "...";
                    }
                    // 解析题干中的图片JSON
                    var cimg = q.cimg;
                    var cimgArray = null;
                    if (cimg != null && cimg != '' && cimg != '[]' && cimg != 'null') {
                        cimgArray = JSON.parse(cimg);
                        for (var j = 0; j < cimgArray.length; j++) {
                            cimgArray[j].src = cimgArray[j].u;
                        }
                    } else {
                        cimgArray = [];
                    }
                    q.cimg = cimgArray;
                    // 解析题目解析中的图片JSON
                    var aimg = q.aimg;
                    var aimgArray = null;
                    if (aimg != null && aimg != '' && aimg != '[]' && aimg != 'null') {
                        aimgArray = JSON.parse(aimg);
                        for (var j = 0; j < aimgArray.length; j++) {
                            aimgArray[j].src = aimgArray[j].u;
                        }
                    } else {
                        aimgArray = [];
                    }
                    q.aimg = aimgArray;
                }
                _quCacheMap[(q.id + "")] = q;// 此q.id是AppUserQuWrong.id
                buf.push('<li class="qu_list_item main_padding_lr qu_list_item_new" data-id="' + q.id + '">');
                buf.push('<div class="qu_info"><div class="qu_time">' + q.ct + '</div></div>');
                buf.push('<div class="qu_content">');
                buf.push(content);
                if (q.smUrl != null && q.smUrl != '') {
                    buf.push('<img class="qu_img" src="' + q.smUrl + '"/>');
                }
                buf.push('</div>');
                buf.push('<div class="qu_info">');

                buf.push('<div class="difficulty_level_box">');
                buf.push('<span class="level_label">难易程度：</span>');
                buf.push('<div class="star_box">');
                if (q.dl != null) {
                    for (var j = 1; j <= q.dl; j++) {
                        buf.push('<i class="star active glyphicon glyphicon-star"></i>');
                    }
                }
                buf.push('</div>');// /star_box
                buf.push('</div>');// /difficulty_level_box

                buf.push('<div class="master_level_box">');
                buf.push('<span class="level_label">掌握程度：</span>');
                buf.push('<div class="star_box">');
                if (q.ml != null) {
                    for (var j = 1; j <= q.ml; j++) {
                        buf.push('<i class="star active glyphicon glyphicon-star"></i>');
                    }
                }
                buf.push('</div>');// /star_box
                buf.push('</div>');// /master_level_box

                buf.push('</div>');// /qu_info

                buf.push('</li>');
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
    pg.loadListFn = function (sort, desc, next) {
        if (pg.isLoadingList) {
            // 正在请求，则拒绝重复请求
            return;
        }
        var option = {
            dirId: pg.dirId,
            sort: sort,
            desc: desc,
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
        LocalJSBridge.loadWrongByDir(option);
    }
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
                    var $filter_btn = $(".filter_btn.active[data-sort]");
                    var next = $filter_btn.attr('data-next');
                    if (next != '-1') {
                        // 没有到最后一页
                        var sort = $filter_btn.attr('data-sort');
                        var desc = $filter_btn.hasClass('sort_desc_active') ? 1 : 0;
                        pg.loadListFn(sort, desc, next);
                    }
                }
            }
        }
    });
    pg.openQuDetailFn = function (qu_list_item_dom) {
        var $qu_list_item = $(qu_list_item_dom);
        var $qu_detail = $("#qu_detail");
        var qu = pg.quCacheMap[$qu_list_item.attr('data-id')];
        var type = qu.type;
        var title = qu.con;
        if (qu.cimg != null && qu.cimg.length > 0) {
            var buf = [];
            for (var i = 0; i < qu.cimg.length; i++) {
                var img = qu.cimg[i];
                buf.push('<img class="detail_img cimg" src="' + img.u + '" data-width="' + img.w + '" data-height="' + img.h + '"/>');
            }
            title = title + buf.join('');
        }
        $qu_detail.find('.qu_title').html(title);
        var $qu_item_list = $qu_detail.find('.qu_item_list');
        if (qu.it1 != null && qu.it1 != '') {
            // 可能是系统错题，所以有A选项的内容
            $qu_detail.find('.qu_item1 .qu_item_inner').html(qu.it1);
            $qu_detail.find('.qu_item2 .qu_item_inner').html(qu.it2);
            $qu_detail.find('.qu_item3 .qu_item_inner').html(qu.it3);
            $qu_detail.find('.qu_item4 .qu_item_inner').html(qu.it4);
            $qu_item_list.show();
        } else {
            $qu_item_list.hide();// 否则不显示选项区域
        }
        var user_answer = qu.uan;
        if (user_answer == null) {
            user_answer = '';
        }
        $qu_detail.find('.user_answer').html(user_answer);
        var standard_answer = qu.an;
        if (standard_answer == null) {
            standard_answer = '';
        }
        $qu_detail.find('.standard_answer').html(standard_answer);
        var analysis = qu.ae;
        if (qu.aimg != null && qu.aimg.length > 0) {
            var buf = [];
            for (var i = 0; i < qu.aimg.length; i++) {
                var img = qu.aimg[i];
                buf.push('<img class="detail_img aimg" src="' + img.u + '" data-width="' + img.w + '" data-height="' + img.h + '"/>');
            }
            analysis = analysis + buf.join('');
        }
        $qu_detail.find('.analysis').html(analysis);
        var error_reason = '';
        if (qu.tag != null && qu.tag.length > 0) {
            var buf = [];
            var tag = qu.tag;
            for (var i = 0; i < tag.length; i++) {
                buf.push(tag[i]);
            }
            error_reason = buf.join('，');
        }
        $qu_detail.find('.error_reason').html(error_reason);
        var difficulty_level_star_box_html = '';
        if (qu.dl != null) {
            var buf = [];
            for (var j = 1; j <= qu.dl; j++) {
                buf.push('<i class="star active glyphicon glyphicon-star"></i>');
            }
            difficulty_level_star_box_html = buf.join('');
        }
        $qu_detail.find('.difficulty_level_star_box').html(difficulty_level_star_box_html);
        var master_level_star_box_html = '';
        if (qu.ml != null) {
            var buf = [];
            for (var j = 1; j <= qu.ml; j++) {
                buf.push('<i class="star active glyphicon glyphicon-star"></i>');
            }
            master_level_star_box_html = buf.join('');
        }
        $qu_detail.find('.master_level_star_box').html(master_level_star_box_html);
        $qu_detail.attr('data-id', qu.id);
        if (type == 1) {
            // 自录错题
            if ((qu.cimg != null && qu.cimg.length > 0) || (qu.aimg != null && qu.aimg.length > 0)) {
                $qu_detail.find('.detail_img').on("tap click", function ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    var $this = $(this);
                    var pswpElement = $('.pswp:eq(0)')[0];
                    var items = [];
                    if ($this.hasClass('cimg')) {
                        items = qu.cimg;
                    } else if ($this.hasClass('aimg')) {
                        items = qu.aimg;
                    }
                    var thisStr = $this.attr("src");
                    var thisIndex = 0;
                    for (var i = 0; i < items.length; i++) {
                        var img = items[i];
                        if (thisStr == img.src) {
                            thisIndex = i;
                            break;
                        }
                    }
                    var options = {
                        // optionName: 'option value'
                        mainClass: 'pswp--minimal--dark',
                        barsSize: {top: 0, bottom: 0},
                        captionEl: false,
                        fullscreenEl: false,
                        shareEl: false,
                        bgOpacity: 0.85,
                        tapToClose: true,
                        tapToToggleControls: false,
                        index: thisIndex // start at first slide
                    };
                    var gallery = new PhotoSwipe(pswpElement, PhotoSwipeUI_Default, items, options);
                    gallery.init();
                })
            }
        } else {
            // 系统错题
            var $contentImgs = $qu_detail.find('.qu_title img[data-width][data-height]');
            if ($contentImgs.length > 0) {
                $contentImgs.addClass('detail_img cimg');
            }
            var $itemAbcdImgs = $qu_detail.find('.qu_item_list img[data-width][data-height]');
            if ($itemAbcdImgs.length > 0) {
                $itemAbcdImgs.addClass('detail_img cimg');
            }
            var $analysisImgs = $qu_detail.find('.analysis img[data-width][data-height]');
            if ($analysisImgs.length > 0) {
                $analysisImgs.addClass('detail_img aimg');
            }
            $qu_detail.find('.detail_img').on("tap click", function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                var $this = $(this);
                var pswpElement = $('.pswp:eq(0)')[0];
                var items = [];
                if ($this.hasClass('cimg')) {
                    var $cimgs = $qu_detail.find(".cimg");
                    for (var i = 0; i < $cimgs.length; i++) {
                        var $i = $($cimgs[i]);
                        items.push({
                            w: $i.attr('data-width'),
                            h: $i.attr('data-height'),
                            src: $i.attr('src')
                        })
                    }
                } else if ($this.hasClass('aimg')) {
                    var $aimg = $qu_detail.find(".aimg");
                    for (var i = 0; i < $aimg.length; i++) {
                        var $i = $($aimg[i]);
                        items.push({
                            w: $i.attr('data-width'),
                            h: $i.attr('data-height'),
                            src: $i.attr('src')
                        })
                    }
                }
                if (items.length <= 0) {
                    return;
                }
                var thisStr = $this.attr("src");
                var thisIndex = 0;
                for (var i = 0; i < items.length; i++) {
                    var img = items[i];
                    if (thisStr == img.src) {
                        thisIndex = i;
                        break;
                    }
                }
                var options = {
                    // optionName: 'option value'
                    mainClass: 'pswp--minimal--dark',
                    barsSize: {top: 0, bottom: 0},
                    captionEl: false,
                    fullscreenEl: false,
                    shareEl: false,
                    bgOpacity: 0.85,
                    tapToClose: true,
                    tapToToggleControls: false,
                    index: thisIndex // start at first slide
                };
                var gallery = new PhotoSwipe(pswpElement, PhotoSwipeUI_Default, items, options);
                gallery.init();
            })
        }
        pg.renderTex($qu_detail);
        pg.changePage('qu_detail');
    }
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
    $("#remove_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $("#confirm_remove_dialog").fadeIn('fast');
    })
    $("#archive_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $("#confirm_archive_dialog").fadeIn('fast');
    })
    $("#show_wrong_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $("body").addClass("show_wrong_item_box");// 显示错题答案，解析部分
    })
    pg.removeWrong_doing = false;// 是否正在请求删除，避免并发请求
    pg.removeQuFn = function (wrongId, successCallbackFn) {
        // 删除题目
        if (pg.removeWrong_doing) {
            return;// 避免重复请求
        }
        var $loading_toast = $('#loading_toast');
        $loading_toast.show();
        var option = {
            id: wrongId,
            subjectId: pg.subjectId,
            success: function (json) {
                pg.removeWrong_doing = false;
                $loading_toast.hide();
                successCallbackFn(json);
            },
            error: function (json) {
                pg.removeWrong_doing = false;
                $loading_toast.hide();
                if (json && json.msg) {
                    $.toptip(json.msg, null, 'error');
                }
            }
        };
        pg.removeWrong_doing = true;// 是否正在请求删除，避免并发请求
        LocalJSBridge.removeWrong(option);
    }
    pg.archiveWrong_doing = false;// 是否正在请求归档，避免并发请求
    pg.archiveQuFn = function (wrongId, successCallbackFn) {
        // 删除题目
        if (pg.archiveWrong_doing) {
            return;// 避免重复请求
        }
        var $loading_toast = $('#loading_toast');
        $loading_toast.show();
        var option = {
            id: wrongId,
            subjectId: pg.subjectId,
            success: function (json) {
                pg.archiveWrong_doing = false;
                $loading_toast.hide();
                successCallbackFn(json);
            },
            error: function (json) {
                pg.archiveWrong_doing = false;
                $loading_toast.hide();
                if (json && json.msg) {
                    $.toptip(json.msg, null, 'error');
                }
            }
        };
        pg.archiveWrong_doing = true;// 是否正在请求归档，避免并发请求
        LocalJSBridge.archiveWrong(option);
    }
    $("#do_remove_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        var wrongId = $('#qu_detail').attr('data-id');
        if (wrongId != null && wrongId != '' && wrongId != '0') {
            $('#confirm_remove_dialog').fadeOut('fast');
            pg.removeQuFn(wrongId, function (json) {
                var $filter_btn = $(".filter_btn.active");
                $filter_btn.removeAttr('data-next');
                $('#qu_detail').attr('data-id', '0');
                pg.quCacheMap[wrongId] = null;
                var sort = $filter_btn.attr('data-sort');
                var desc = $filter_btn.hasClass('sort_desc_active') ? 1 : 0;
                pg.loadListFn(sort, desc, null);// JSBridge ready后加载列表第一页
                pg.changePage('qu_list_box');
            });
        }
    })
    $("#do_archive_btn").on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        var wrongId = $('#qu_detail').attr('data-id');
        if (wrongId != null && wrongId != '' && wrongId != '0') {
            $('#confirm_archive_dialog').fadeOut('fast');
            pg.archiveQuFn(wrongId, function (json) {
                var $filter_btn = $(".filter_btn.active");
                $filter_btn.removeAttr('data-next');
                $('#qu_detail').attr('data-id', '0');
                pg.quCacheMap[wrongId] = null;
                var sort = $filter_btn.attr('data-sort');
                var desc = $filter_btn.hasClass('sort_desc_active') ? 1 : 0;
                pg.loadListFn(sort, desc, null);// JSBridge ready后加载列表第一页
                pg.changePage('qu_list_box');
            });
        }
    })
    $('.filter_btn[data-sort]').on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        if (pg.isLoadingList) {
            // 正在加载中，则不触发切换
            return;
        }
        var $this = $(this);
        var sort = $this.attr('data-sort');
        var desc = 0;
        if ($this.hasClass('active')) {
            // 当前被点击的就是按照它排序，则desc和asc切换
            if ($this.hasClass('sort_desc_active')) {
                $this.removeClass('sort_desc_active').addClass('sort_asc_active');
            } else {
                $this.removeClass('sort_asc_active').addClass('sort_desc_active');
                desc = 1;// 降序
            }
        } else {
            $('.filter_btn.active[data-sort]').removeClass('active sort_desc_active sort_asc_active');
            var defaultSort = $this.attr('data-default-sort');
            if ('sort_desc_active' == defaultSort) {
                desc = 1;// 降序
            }
            $this.addClass('active ' + defaultSort);
        }
        pg.loadListFn(sort, desc, null);
    })
    $("#page_title").on("dblclick", function () {
        // 双击顶部标题，则回到顶部
        $("body,html").animate({scrollTop: 0}, 500);
    })
})