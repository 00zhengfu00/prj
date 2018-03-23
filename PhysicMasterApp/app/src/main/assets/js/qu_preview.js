$(function () {
    LocalJSBridge.setup();
    var pg = {fn: {}};
    pg.$page_carousel = $(".page_carousel");
    pg.$quit_btn = $("#quit_btn");
    pg.$qu_list = $(".qu_list");

    pg.fn.onShowAnalysis = function () {
        window.PAGE_TYPE = 1;
        var $allItem = pg.$page_carousel.find(".item");
        $allItem.removeClass("active");
        $($allItem[0]).addClass("active");
        $(".fixed_bottom").hide();
        // pg.$progress_num.hide();
        pg.$progress_num.html("1/" + pg.quListSize);
        pg.$top_bar.find(".progress_box").hide();
        pg.$time_box.hide();
        pg.fn.hideLoading();
    };
    LocalJSBridge.ready(function () {
        LocalJSBridge.registerHandler("onShowAnalysis", pg.fn.onShowAnalysis);
    });

    pg.$page_carousel.carousel({interval: false, keyboard: false});

    pg.$quit_btn.on("click", function ($event) {
        var type = 0;
        if (window.PAGE_TYPE == 0) {
            // 还在闯关，就要提示积分不返还的提示
            type = 1;
            //var flag = window.confirm("放弃本次闯关，将失去积分及其他奖励，已消耗的精力不做返还！");
            //if (flag) {
            //    // 执行退出
            //    alert("执行退出")
            //} else {
            //    // 取消
            //}
        }
        LocalJSBridge.quit({type: type});
        $event.preventDefault();
        $event.stopPropagation();
    });
    pg.userAnswerResult = {quBatchId: window["quBatchId"], timeConsuming: 0, answerList: []}; // 用户答案结果对象
    //  pg.$loading_toast = $("#loading_toast");
    //  pg.$weui_toast_content = pg.$loading_toast.find(".weui_toast_content");
    pg.$bottom_toast = $("#bottom_toast");
    pg.$top_bar = $("#top_bar");
    pg.$progress_num = pg.$top_bar.find(".progress_num");
    pg.$progress_bar = pg.$top_bar.find(".progress-bar");
    pg.$time_box = pg.$top_bar.find(".time_box");
    pg.quListSize = 0;// 总共的习题数
    pg.timeout_seconds = window["TIMEOUT_SECONDS"] || 600;// 闯关限时秒数
    pg.hasPoint = true;// 是否增加积分，由于一些规则限制，可能重复闯关以后不再奖励积分
    if (window["hasPoint"] != null) {
        pg.hasPoint = window["hasPoint"] === true;
    }
    pg.point_award_correct = 2;// 预习习题闯关时，1题答对奖励的积分数
    pg.point_award_continuous_correct = 3;// 预习习题闯关时，1次连对奖励的积分数
    if (window["point_award_correct"] != null) {
        var tmpNum = window["point_award_correct"] * 1;
        if (!isNaN(tmpNum) && tmpNum > 0) {
            pg.point_award_correct = tmpNum;
        }
    }
    if (window["point_award_continuous_correct"] != null) {
        var tmpNum = window["point_award_continuous_correct"] * 1;
        if (!isNaN(tmpNum) && tmpNum > 0) {
            pg.point_award_continuous_correct = tmpNum;
        }
    }
    pg.msg_correct = "正确";
    pg.msg_continuous_correct = "连击";
    if (pg.hasPoint) {
        pg.msg_correct = "正确，积分+" + pg.point_award_correct;
        pg.msg_continuous_correct = "连击，积分+" + pg.point_award_continuous_correct;
    } else {
        pg.$bottom_toast.css("margin-left", "-18px");
    }
    if (pg.timeout_seconds == null || isNaN(pg.timeout_seconds) || pg.timeout_seconds <= 0) {
        pg.timeout_seconds = 600;
    }
    pg.elapsedTimeSeconds = 0;// 消耗的时间
    pg.timerIntervalId = null;
    pg.isLoading = false;
    pg.fn.showLoading = function () {
        pg.isLoading = true;
        //if (msg == null || msg === "") {
        //    msg = "努力加载中";
        //}
        //pg.$weui_toast_content.html(msg);
        //pg.$loading_toast.show();
        try {
            LocalJSBridge.showLoading();
        } catch (e) {
        }
    }
    pg.fn.hideLoading = function () {
        pg.isLoading = false;
        //pg.$loading_toast.hide();
        try {
            LocalJSBridge.ready(function () {
                LocalJSBridge.hideLoading();
            });
        } catch (e) {
        }
    }
    pg.fn.resetAnimationToast = function () {
        pg.$bottom_toast.fadeOut("slow", function () {
            pg.$bottom_toast.show();
            pg.$bottom_toast.css("bottom", "-25px");
        });
    }
    pg.html_answer_mark_correct = '<img class="answer_mark temp_img" src="img/icon_correct_v2.png">';
    pg.html_answer_mark_wrong = '<img class="answer_mark temp_img" src="img/icon_wrong_v2.png">';
    pg.quAnswerMap = {};// 正确答案Map
    pg.isLatestQuCorrect = false;
    pg.$submit_btn = $("#submit_btn");
    pg.submitDataDoing = false;
    pg.fn.submitData = function () {
        if (pg.submitDataDoing) {
            return;
        }
        pg.submitDataDoing = true;
        clearInterval(pg.timerIntervalId);
        pg.timerIntervalId = null;
        pg.userAnswerResult.timeConsuming = pg.elapsedTimeSeconds;
        pg.$submit_btn.prop("disabled", true);
        // pg.fn.showLoading();
        try {
            LocalJSBridge.submitPreviewQuBatch({answerJson: JSON.stringify(pg.userAnswerResult)});
        } catch (e) {
        }
    }
    pg.fn.formatSeconds = function (seconds) {
        if (seconds == null || isNaN(seconds) || seconds <= 0) {
            return "00:00";
        }
        var buf = [];
        if (seconds < 60) {
            buf.push("00");
            if (seconds < 10) {
                buf.push("0" + seconds);
            } else {
                buf.push(seconds);
            }
        } else if (seconds < 3600) {
            // 一小时内
            var sec = seconds % 60;
            var min = (seconds - sec) / 60;
            if (min < 10) {
                buf.push("0" + min);
            } else {
                buf.push(min);
            }
            if (sec < 10) {
                buf.push("0" + sec);
            } else {
                buf.push(sec);
            }
        } else {
            // 一小时以上
            var sec = seconds % 60;
            var allMin = (seconds - sec) / 60;
            var min = allMin % 60;
            var hour = (allMin - min) / 60;
            if (hour < 10) {
                buf.push("0" + hour);
            } else {
                buf.push(hour);
            }
            if (min < 10) {
                buf.push("0" + min);
            } else {
                buf.push(min);
            }
            if (sec < 10) {
                buf.push("0" + sec);
            } else {
                buf.push(sec);
            }
        }
        return buf.join(":");
    }
    {
        pg.$submit_btn.on("tap click", function ($event) {
            if (pg.$submit_btn.prop("disabled")) {
                return;
            }
            var btn_action_type = pg.$submit_btn.attr("action-type");// check：校验答案；next：下一题；submit：提交闯关数据
            if (btn_action_type === "check") {
                var $activeQu = pg.$page_carousel.find(".item.active");
                var quIndex = $activeQu.attr("data-qu-index") * 1;
                var quId = $activeQu.attr("data-qu-id");
                var $qu_item_list = $activeQu.find(".qu_item_list");
                var $qu_item_user_choose = $activeQu.find(".qu_item.active");
                if ($qu_item_user_choose.length > 0) {
                    // 用户点击提交按钮时，选中了选项的
                    var correctAnswer = pg.quAnswerMap[quId];
                    var user_choose_item_code = $qu_item_user_choose.attr("data-code");// 用户选中的答案
                    if (user_choose_item_code != null) {
                        user_choose_item_code = user_choose_item_code.toUpperCase().trim();
                    }
                    {
                        // 记录用户选中的结果
                        var quResultObj = null;
                        var _answerList = pg.userAnswerResult.answerList;
                        var quIdInt = quId * 1;
                        for (var ii = 0; ii <= _answerList.length; ii++) {
                            var tmp = _answerList[ii];
                            if (tmp.quId === quIdInt) {
                                quResultObj = tmp;
                                break;
                            }
                        }
                        if (quResultObj == null) {
                            quResultObj = {quId: quIdInt, answer: user_choose_item_code};
                            _answerList.push(quResultObj);
                        } else {
                            quResultObj.answer = user_choose_item_code;
                        }
                    }
                    var isUserCorrect = user_choose_item_code != null && user_choose_item_code === correctAnswer;
                    var item_height_user_choose = $qu_item_user_choose.height();
                    var $item_code = $qu_item_user_choose.find(".item_code");
                    var item_code_width = $item_code.innerWidth();
                    if (isUserCorrect) {
                        // 用户回答正确
                        $qu_item_user_choose.append(pg.html_answer_mark_correct);
                        if (pg.isLatestQuCorrect) {
                            // 连击
                            pg.$bottom_toast.html(pg.msg_continuous_correct);
                        } else {
                            pg.$bottom_toast.html(pg.msg_correct);
                        }
                        pg.$bottom_toast.animate({bottom: '80px'}, "normal", function () {
                            pg.fn.resetAnimationToast();
                        });
                        pg.isLatestQuCorrect = true;
                    } else {
                        // 用户回答错误
                        $qu_item_user_choose.append(pg.html_answer_mark_wrong);
                        var $real_correct_qu_item = $activeQu.find('.qu_item[data-code="' + correctAnswer + '"]');// 真正的正确项
                        $real_correct_qu_item.append(pg.html_answer_mark_correct);
                        pg.isLatestQuCorrect = false;
                    }
                    {
                        $qu_item_list.addClass("immutable");// 一旦提交答案，则不能再修改了
                        $activeQu.find(".qu_explain").css("visibility", "visible");
                    }
                    if (pg.quListSize == quIndex + 1) {
                        // 此次next操作以后，已经到了最后一个，则此按钮变成submit，提交数据
                        pg.$submit_btn.attr("action-type", "submit").html("提交");
                    } else {
                        pg.$submit_btn.attr("action-type", "next").html("继续");
                    }
                    {
                        var quNum = quIndex + 1;
                        pg.$progress_num.html(quNum + "/" + pg.quListSize);
                        pg.$progress_bar.css("width", ((quNum * 100) / pg.quListSize) + "%");
                    }
                }
            } else if (btn_action_type === "next") {
                var carousel = null;
                var page_carousel_data = pg.$page_carousel.data();
                if (page_carousel_data != null) {
                    carousel = page_carousel_data["bs.carousel"];
                }
                if (carousel != null) {
                    if (carousel.sliding == null || carousel.sliding === false) {
                        pg.$page_carousel.carousel("next");
                        pg.$submit_btn.attr("action-type", "check").prop("disabled", true).html("确定");
                    }
                }
            } else if (btn_action_type === "submit") {
                pg.fn.submitData();
            }
            $event.preventDefault();
            $event.stopPropagation();
        })
    }

    pg.texCacheMap = {};// tex缓存Map
    pg.allImgReady = false;
    pg.fn.render = function () {
        var $texJqArray = $(".tex[data-expr]");
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
        {
            var $allImgJq = $("img[src]:not(.temp_img)");
            var allImgDomArray = [];
            var allImgSrcSet = {};
            for (var j = 0; j < $allImgJq.length; j++) {
                var imgDom = $allImgJq[j];
                var src = imgDom.src;
                // console.log(j + " - complete:" + imgDom.complete);// + " - " + imgDom.src);
                if ((!imgDom.complete) && allImgSrcSet[src] == null) {
                    // 此图片未完成加载
                    allImgDomArray.push(imgDom);
                    allImgSrcSet[src] = 1;
                }
            }
            var allImgSize = allImgDomArray.length;
            if (allImgSize > 0) {
                $allImgJq = $(allImgDomArray);
                var readyImgSize = 0;
                $allImgJq.one("load", function () {
                    readyImgSize++;
                    if (readyImgSize >= allImgSize && (!pg.allImgReady)) {
                        pg.allImgReady = true;
                        $("body").removeClass("body_loading");
                        pg.fn.hideLoading();
                        if (pg.timerIntervalId == null) {
                            pg.timerIntervalId = setInterval(function () {
                                var timeRemaining = pg.timeout_seconds - ++pg.elapsedTimeSeconds;
                                pg.$time_box.html(pg.fn.formatSeconds(timeRemaining));
                                if (timeRemaining <= 0) {
                                    pg.fn.submitData(); // 自动提交数据
                                }
                            }, 1000);
                        }
                    }
                });
            } else {
                // 一张图片都没有
                pg.allImgReady = true;
                $("body").removeClass("body_loading");
                pg.fn.hideLoading();
                if (pg.timerIntervalId == null) {
                    pg.timerIntervalId = setInterval(function () {
                        var timeRemaining = pg.timeout_seconds - ++pg.elapsedTimeSeconds;
                        pg.$time_box.html(pg.fn.formatSeconds(timeRemaining));
                        if (timeRemaining <= 0) {
                            pg.fn.submitData(); // 自动提交数据
                        }
                    }, 1000);
                }
            }
        }
        {
            var $all_qu_item_jq = $(".qu_item");
            $all_qu_item_jq.on("tap click", function ($event) {
                var $qu_item = $(this);
                var $parent_qu_item_list = $qu_item.parents(".qu_item_list:eq(0)");
                var $qu_item_latest_active = $parent_qu_item_list.find(".qu_item.active");
                if ($parent_qu_item_list.hasClass("immutable")) {
                    // 此题已提交，则不能再改变了
                    $event.preventDefault();
                    $event.stopPropagation();
                    return;
                }
                $qu_item_latest_active.removeClass("active");
                $qu_item.addClass("active");
                pg.$submit_btn.prop("disabled", false);
                $event.preventDefault();
                $event.stopPropagation();
            });
        }
        {
            //window.PAGE_TYPE // 0：习题闯关；1：习题闯关提交答案后查看解析；
            $(document).on("swipeleft", function ($event) {
                if (!pg.isLoading && window["PAGE_TYPE"] === 1) {
                    // 1：习题闯关提交答案后查看解析；
                    var carousel = null;
                    var page_carousel_data = pg.$page_carousel.data();
                    if (page_carousel_data != null) {
                        carousel = page_carousel_data["bs.carousel"];
                    }
                    if (carousel != null) {
                        if (carousel.sliding == null || carousel.sliding === false) {
                            var $lastItem = pg.$page_carousel.find(".item:last");
                            if (!$lastItem.hasClass("active")) {
                                var quIndex = (pg.$page_carousel.find(".item.active").attr("data-qu-index") * 1) + 1;
                                pg.$progress_num.html((quIndex + 1) + "/" + pg.quListSize);
                                pg.$page_carousel.carousel("next");
                            }
                        }
                    }
                }
            });
            $(document).on("swiperight", function ($event) {
                if (!pg.isLoading && window["PAGE_TYPE"] === 1) {
                    // 1：习题闯关提交答案后查看解析；
                    var carousel = null;
                    var page_carousel_data = pg.$page_carousel.data();
                    if (page_carousel_data != null) {
                        carousel = page_carousel_data["bs.carousel"];
                    }
                    if (carousel != null) {
                        if (carousel.sliding == null || carousel.sliding === false) {
                            var $firstItem = pg.$page_carousel.find(".item:first");
                            if (!$firstItem.hasClass("active")) {
                                var quIndex = (pg.$page_carousel.find(".item.active").attr("data-qu-index") * 1) - 1;
                                pg.$progress_num.html((quIndex + 1) + "/" + pg.quListSize);
                                pg.$page_carousel.carousel("prev");
                            }
                        }
                    }
                }
            });
        }
    }
    pg.qu_box_template_html = $("#qu_box_template").html();
    pg.quList = window["QU_LIST"];
    if (pg.quList != null && pg.quList.length > 0) {
        try {
            window["QU_LIST"] = null;
            delete window["QU_LIST"];
        } catch (e) {
        }
        pg.quListSize = pg.quList.length;
        {
            pg.$progress_num.html("0/" + pg.quListSize);
            pg.$time_box.html(pg.fn.formatSeconds(pg.timeout_seconds));
        }
        for (var i = 0; i < pg.quListSize; i++) {
            var qu = pg.quList[i];
            var title = qu.c;
            var quId = qu.id + "";
            var item1 = qu.i1;
            var item2 = qu.i2;
            var item3 = qu.i3;
            var item4 = qu.i4;
            var answer = qu.a.toUpperCase().trim();
            var explain = qu.e;
            pg.quAnswerMap[quId] = answer;
            pg.userAnswerResult.answerList.push({quId: quId * 1, answer: ""});// 如果由于超时自动提交，则用户未选的题，答案将设置为空白字符串
            var $tmp_qu_box = $(pg.qu_box_template_html);
            $tmp_qu_box.attr("data-qu-id", quId);
            $tmp_qu_box.find(".qu_title").html(title);
            var $qu_item1_inner = $tmp_qu_box.find(".qu_item1>.qu_item_inner");
            $qu_item1_inner.html(item1);
            var $qu_item2_inner = $tmp_qu_box.find(".qu_item2>.qu_item_inner");
            $qu_item2_inner.html(item2);
            var $qu_item3_inner = $tmp_qu_box.find(".qu_item3>.qu_item_inner");
            $qu_item3_inner.html(item3);
            var $qu_item4_inner = $tmp_qu_box.find(".qu_item4>.qu_item_inner");
            $qu_item4_inner.html(item4);
            $tmp_qu_box.find(".qu_explain").html(explain);
            var $firstP = $tmp_qu_box.find(".qu_explain p:eq(0)");
            var firstPHtml = null;
            if ($firstP.length > 0 && (firstPHtml = $firstP.html().trim()).indexOf("解析") == 0 && firstPHtml.length == 3) {
                $firstP.css("font-weight", "600").css("font-size", "16px").css("line-height", "24px").css("color", "#444");
            }
            if (i == 0) {
                $tmp_qu_box.addClass("active");
            }
            $tmp_qu_box.attr("data-qu-index", i);
            pg.$qu_list.append($tmp_qu_box);
        }
        pg.fn.render();
    }
});