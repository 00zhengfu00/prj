$(function () {
    LocalJSBridge.setup();

    var prependText = function (content, prefixText) {
        //if (content != null)
        {
            content = content.trim();
            if (content.indexOf("<div><p>") == 0) {
                content = "<div><p>" + prefixText + content.substr("<div><p>".length);
            } else if (content.indexOf("<div><p ") == 0) {
                // 可能p标签中添加了style/class属性
                var contentPrefix = "<div><p ";
                var tmp = content.substring(contentPrefix.length);
                var prefixEndIndex = tmp.indexOf(">") + ">".length;
                contentPrefix += tmp.substring(0, prefixEndIndex);
                var contentSuffix = tmp.substring(prefixEndIndex);
                content = contentPrefix + prefixText + contentSuffix;
            } else if (content.indexOf("<div>") == 0) {
                content = "<div>" + prefixText + content.substr("<div>".length);
            } else if (content.indexOf("<p>") == 0) {
                content = "<p>" + prefixText + content.substr("<p>".length);
            } else {
                content = prefixText + content;
            }
        }
        return content;
    }
    var fixContenItemHtml = function (html) {
        //if (html != null)
        //{
        //    html = html.replace(/_{2,}/g, '<span class="qu_underline">________</span>');
        //}
        return html;
    }
    var replaceItemTextCenter = function (html) {
        if (html != null) {
            html = html.replace(/text-align:(\s+?)center;/g, '');
        }
        return html;
    }


    var $review_qu = $("#review_qu");
    var qu = window.QU;
    var content = fixContenItemHtml(qu.c);
    $review_qu.find(".qu_content").html(content);
    var hasItem = qu.t == 0 && qu.i1 != null && qu.i2 != null && qu.i3 != null && qu.i4 != null;// 此题是否有选项
    if (hasItem) {
        $review_qu.find(".qu_item_1").html(fixContenItemHtml(prependText(replaceItemTextCenter(qu.i1), '<span class="_item_code">A. </span>')));
        $review_qu.find(".qu_item_2").html(fixContenItemHtml(prependText(replaceItemTextCenter(qu.i2), '<span class="_item_code">B. </span>')));
        $review_qu.find(".qu_item_3").html(fixContenItemHtml(prependText(replaceItemTextCenter(qu.i3), '<span class="_item_code">C. </span>')));
        $review_qu.find(".qu_item_4").html(fixContenItemHtml(prependText(replaceItemTextCenter(qu.i4), '<span class="_item_code">D. </span>')));
    } else {
        // 不是选择题
        $review_qu.find(".qu_item_list").remove();
    }
    {
        var $texJqArray = $review_qu.find(".tex[data-expr]");
        var texElSize = $texJqArray.length;
        var texCacheMap = {};
        for (var jj = 0; jj < texElSize; jj++) {
            var $tex = $($texJqArray[jj]);
            var expr = $tex.attr("data-expr");
            if (expr != null && expr.length > 0) {
                try {
                    var html = texCacheMap[expr];
                    if (html == null) {
                        html = katex.renderToString(expr);
                        texCacheMap[expr] = html;
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
    var afterAllImgLoad = function () {
        if (hasItem) {
            // 判断选项的排列方式
            var $qu_item_list = $review_qu.find(".qu_item_list");
            var useItemBlock = false;
            var $qu_item_1 = $review_qu.find(".qu_item_1");
            var $qu_item_2 = $review_qu.find(".qu_item_2");
            var $qu_item_3 = $review_qu.find(".qu_item_3");
            var $qu_item_4 = $review_qu.find(".qu_item_4");
            //if ($qu_item_list.find("img").length > 0) {
            //    // 选项中有图，则一定一行一个排列
            //    $qu_item_list.removeClass("one_row").addClass("qu_item_block");
            //    useItemBlock = true;
            //} else
            //{
            var allItemWidthOneRow = 6 * 4 + 4 * 10 + $qu_item_1.width() + $qu_item_2.width() + $qu_item_3.width() + $qu_item_4.width();
            var useTwoRow = false;
            if (allItemWidthOneRow >= ($qu_item_list.width() - 6)) {
                // 四个选项放一行 宽度超过了父DIV的宽度
                $qu_item_list.removeClass("one_row").addClass("two_row");
                useTwoRow = true;
                useItemBlock = true;
                var allItemWidthTwoRow1 = $qu_item_1.width() + $qu_item_2.width();
                var allItemWidthTwoRow2 = $qu_item_3.width() + $qu_item_4.width();
                var heightDivide1 = Math.abs($qu_item_1.height() - $qu_item_2.height());
                var heightDivide2 = Math.abs($qu_item_3.height() - $qu_item_4.height());
                if (allItemWidthTwoRow1 >= $qu_item_list.width()
                    || allItemWidthTwoRow2 >= $qu_item_list.width()
                    || heightDivide1 > 20
                    || heightDivide2 > 20
                ) {
                    // 每两个选项放一行 宽度超过了父DIV的宽度
                    $qu_item_list.removeClass("two_row").addClass("qu_item_block");
                    useItemBlock = true;
                    useTwoRow = false;
                }
            }
            //}
            if (useItemBlock) {
                // 一行一个选项
                $qu_item_1.find("._item_code").remove();
                $qu_item_2.find("._item_code").remove();
                $qu_item_3.find("._item_code").remove();
                $qu_item_4.find("._item_code").remove();
                $($qu_item_1.children()[0]).addClass("_item_content");
                $($qu_item_2.children()[0]).addClass("_item_content");
                $($qu_item_3.children()[0]).addClass("_item_content");
                $($qu_item_4.children()[0]).addClass("_item_content");
                $qu_item_1.prepend('<div class="_item_code_block">A. </div>');
                $qu_item_2.prepend('<div class="_item_code_block">B. </div>');
                $qu_item_3.prepend('<div class="_item_code_block">C. </div>');
                $qu_item_4.prepend('<div class="_item_code_block">D. </div>');
                if (useTwoRow) {
                    var maxLineHeightRow1 = 0;
                    var maxLineHeightRow2 = 0;
                    if ($qu_item_1.height() <= 33) {
                        if ($qu_item_1.height() > maxLineHeightRow1) {
                            maxLineHeightRow1 = $qu_item_1.height();
                        }
                    }
                    if ($qu_item_2.height() <= 33) {
                        if ($qu_item_2.height() > maxLineHeightRow1) {
                            maxLineHeightRow1 = $qu_item_2.height();
                        }
                    }
                    if ($qu_item_3.height() <= 33) {
                        if ($qu_item_3.height() > maxLineHeightRow2) {
                            maxLineHeightRow2 = $qu_item_3.height();
                        }
                    }
                    if ($qu_item_4.height() <= 33) {
                        if ($qu_item_4.height() > maxLineHeightRow2) {
                            maxLineHeightRow2 = $qu_item_4.height();
                        }
                    }
                    if (maxLineHeightRow1 > 0) {
                        $qu_item_1.css("line-height", maxLineHeightRow1 + "px").css("float", "left");
                        $qu_item_2.css("line-height", maxLineHeightRow1 + "px");
                    }
                    if (maxLineHeightRow2 > 0) {
                        $qu_item_3.css("line-height", maxLineHeightRow2 + "px").css("float", "left");
                        $qu_item_4.css("line-height", maxLineHeightRow2 + "px");
                    }
                    $qu_item_list.append('<div class="row_1"></div>');
                    $qu_item_list.append('<div class="row_2"></div>');
                    var $row_1 = $qu_item_list.find(".row_1");
                    $row_1.append($qu_item_1[0]);
                    $row_1.append($qu_item_2[0]);
                    var $row_2 = $qu_item_list.find(".row_2");
                    $row_2.append($qu_item_3[0]);
                    $row_2.append($qu_item_4[0]);
                }
                //$review_qu.find(".qu_item_list").addClass("qu_item_block");
            }
        }
        LocalJSBridge.ready(function () {
            $review_qu.css("visibility", "visible");
            LocalJSBridge.updateViewHeight({height: $("body").height()});
        });
    }
    var allImgReady = false;
    {
        var $allImgJq = $review_qu.find("img[src]");
        var allImgDomArray = [];
        var allImgSrcSet = {};
        for (var j = 0; j < $allImgJq.length; j++) {
            var imgDom = $allImgJq[j];
            var src = imgDom.src;
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
                if (readyImgSize >= allImgSize && (!allImgReady)) {
                    // 而且allImgReady必须为false，避免多次回调
                    allImgReady = true;
                    afterAllImgLoad();
                }
            });
        } else {
            // 一张图片都没有
            allImgReady = true;
            afterAllImgLoad();
        }
    }
});