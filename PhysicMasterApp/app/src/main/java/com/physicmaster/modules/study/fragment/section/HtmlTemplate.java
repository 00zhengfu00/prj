package com.physicmaster.modules.study.fragment.section;

/**
 * Created by huashigen on 2016/12/8.
 */

public final class HtmlTemplate {
    public static final String TITLE_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta http-equiv=\"Content-Language\" content=\"zh-CN\">\n" +
            "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, " +
            "user-scalable=0\">\n" +
            "    <meta name=\"format-detection\" content=\"telephone=no\">\n" +
            "    <link rel=\"stylesheet\" href=\"katex/katex.min.css\">\n" +
            "    <style>\n" +
            "        *{\n" +
            "            -webkit-box-sizing: border-box;\n" +
            "            -moz-box-sizing: border-box;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        body,html{\n" +
            "            margin: 0;background-color: transparent;-moz-user-select: -moz-none;" +
            "-moz-user-select: none;-o-user-select: none;-khtml-user-select: none;" +
            "-webkit-user-select: none;-ms-user-select: none;user-select: none;\n" +
            "        }\n" +
            "        .wFull {\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        img {\n" +
            "            max-width: 100%;\n" +
            "        }\n" +
            "        .katex .reset-textstyle.scriptstyle .cjk-char,\n" +
            "        .katex .reset-textstyle.scriptstyle .cjk_fallback{\n" +
            "            font-size: 0.7em;\n" +
            "        }\n" +
            "        .errorMessage{\n" +
            "            color: red;\n" +
            "        }\n" +
            "        .auto_br, p{\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        .auto_br, .katex, p{\n" +
            "            display: inline-block;white-space: -moz-pre-wrap;white-space: -pre-wrap;" +
            "white-space: -o-pre-wrap;white-space: pre-wrap;word-wrap: break-word;\n" +
            "        }\n" +
            "        p {\n" +
            "            display: block;margin: 0 0 10px;\n" +
            "        }\n" +
            "        p:last-child {\n" +
            "            margin-bottom: 0;\n" +
            "        }\n" +
            "        body, .katex{\n" +
            "            font-size: 16px;line-height: 24px;\n" +
            "        }\n" +
            "        @media screen and (min-device-width: 765px) {\n" +
            "            body, .katex{\n" +
            "                font-size: 20px;line-height: 30px;\n" +
            "            }\n" +
            "        }\n" +
            "        @media screen and (max-device-width: 365px) {\n" +
            "            body, .katex{\n" +
            "                font-size: 14px;line-height: 21px;\n" +
            "            }\n" +
            "        }\n" +
            "        #hide_img_div{\n" +
            "            width:0;height:0;overflow:hidden;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<script type=\"text/javascript\">window[\"redefine_katex_cjkRegex\"] = " +
            "/[\\u3040-\\u309F]|[\\u30A0-\\u30FF]|[\\u4E00-\\u9FAF]|[\\uAC00-\\uD7AF]|[\\u2460" +
            "-\\u2469]/;</script>\n" +
            "###REPLACEMENT###\n" +
            "<div id=\"hide_img_div\"></div>\n" +
            "<script src=\"katex/katex.min.js\"></script>\n" +
            "<script>\n" +
            "    (function() {\n" +
            "        var texCacheMap = {};\n" +
            "        var tex = document.getElementsByClassName(\"tex\");\n" +
            "        Array.prototype.forEach.call(tex, function(el) {\n" +
            "            var expr = el.getAttribute(\"data-expr\");\n" +
            "            if(expr!=null&&expr.length>0){\n" +
            "                try{\n" +
            "                    var html = texCacheMap[expr];\n" +
            "                    if(html == null){\n" +
            "                        html = katex.renderToString(expr);\n" +
            "                        texCacheMap[expr] = html;\n" +
            "                    }\n" +
            "                    el.innerHTML = html;\n" +
            "                }catch(e){\n" +
            "                    el.className=el.className+\" errorMessage\";\n" +
            "                    el.innerHTML=\"【显示出错：\"+expr+\"】\";\n" +
            "                }\n" +
            "                try{\n" +
            "                    el.removeAttribute(\"data-expr\");\n" +
            "                }catch(e){\n" +
            "                }\n" +
            "            }\n" +
            "        });\n" +
            "        {var m=document.getElementsByTagName(\"img\");if(m!=null&&m.length>0){var " +
            "c=[];for(var v=0;v<m.length;v++){var n=m[v];if(n.parentNode!=null&&n.parentNode" +
            ".id==\"hide_img_div\"){continue}c.push(n)}m=c;var h=document.getElementsByTagName" +
            "(\"body\")[0];var e=document.getElementById(\"hide_img_div\");var z=[];for(var v=0;" +
            "v<m.length;v++){var r=m[v];var p=r.parentNode;var f=r.src;var g=r.attributes;var " +
            "o=0;var u=0;if(g!=null){for(var s=0;s<g.length;s++){var q=g[s];if(q" +
            ".name==\"data-width\"){o=q.value*1}else{if(q.name==\"data-height\"){u=q" +
            ".value*1}}}}var y=p.clientWidth;if(o==null||isNaN(o)||o<0){o=0}if(u==null||isNaN(u)" +
            "||u<0){u=0}var b=\"real_img\"+v;r.src=\"blank.png\";var w=r.style;if(w!=null){w" +
            ".display=\"\";if(o>0&&u>0){var A=y;if(A<=0){A=o}var d=o;var a=u;if(o>A){d=A;a=(u*d)" +
            "/o;a=Math.round(a*100)/100}w.width=d+\"px\";w.height=a+\"px\"}}r.style=w;r.id=b;z" +
            ".push('<img id=\"hide_img'+v+'\" src=\"'+f+'\" name=\"'+b+'\"/>')}e.innerHTML=z.join" +
            "(\"\");for(var t=0;t<m.length;t++){var l=\"hide_img\"+t;var x=document" +
            ".getElementById(l);x.onload=function(j){var B=this;var i=B.name;var k=document" +
            ".getElementById(i);k.src=B.src;B.remove();j.stopPropagation()}}}}\n" +
            "    })();\n" +
            "</script>\n" +
            "</body>\n" +
            "</html>";
    public static final String ANALYSIS_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta http-equiv=\"Content-Language\" content=\"zh-CN\">\n" +
            "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, " +
            "user-scalable=0\">\n" +
            "    <meta name=\"format-detection\" content=\"telephone=no\">\n" +
            "    <link rel=\"stylesheet\" href=\"katex/katex.min.css\">\n" +
            "    <style>\n" +
            "        *{\n" +
            "            -webkit-box-sizing: border-box;\n" +
            "            -moz-box-sizing: border-box;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        body,html{\n" +
            "            margin: 0;background-color: transparent;-moz-user-select: -moz-none;" +
            "-moz-user-select: none;-o-user-select: none;-khtml-user-select: none;" +
            "-webkit-user-select: none;-ms-user-select: none;user-select: none;\n" +
            "        }\n" +
            "        .wFull {\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        img {\n" +
            "            max-width: 100%;\n" +
            "        }\n" +
            "        .katex .reset-textstyle.scriptstyle .cjk-char,\n" +
            "        .katex .reset-textstyle.scriptstyle .cjk_fallback{\n" +
            "            font-size: 0.7em;\n" +
            "        }\n" +
            "        .errorMessage{\n" +
            "            color: red;\n" +
            "        }\n" +
            "        .auto_br, p{\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        .auto_br, .katex, p{\n" +
            "            display: inline-block;white-space: -moz-pre-wrap;white-space: -pre-wrap;" +
            "white-space: -o-pre-wrap;white-space: pre-wrap;word-wrap: break-word;\n" +
            "        }\n" +
            "        p {\n" +
            "            display: block;margin: 0 0 10px;\n" +
            "        }\n" +
            "        p:last-child {\n" +
            "            margin-bottom: 0;\n" +
            "        }\n" +
            "        body, .katex{\n" +
            "            font-size: 16px;line-height: 24px;\n" +
            "        }\n" +
            "        @media screen and (min-device-width: 765px) {\n" +
            "            body, .katex{\n" +
            "                font-size: 20px;line-height: 30px;\n" +
            "            }\n" +
            "        }\n" +
            "        @media screen and (max-device-width: 365px) {\n" +
            "            body, .katex{\n" +
            "                font-size: 14px;line-height: 21px;\n" +
            "            }\n" +
            "        }\n" +
            "        #hide_img_div{\n" +
            "            width:0;height:0;overflow:hidden;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<script type=\"text/javascript\">window[\"redefine_katex_cjkRegex\"] = " +
            "/[\\u3040-\\u309F]|[\\u30A0-\\u30FF]|[\\u4E00-\\u9FAF]|[\\uAC00-\\uD7AF]|[\\u2460" +
            "-\\u2469]/;</script>\n" +
            "###REPLACEMENT###\n" +
            "<div id=\"hide_img_div\"></div>\n" +
            "<script src=\"katex/katex.min.js\"></script>\n" +
            "<script>\n" +
            "    (function() {\n" +
            "        var texCacheMap = {};\n" +
            "        var tex = document.getElementsByClassName(\"tex\");\n" +
            "        Array.prototype.forEach.call(tex, function(el) {\n" +
            "            var expr = el.getAttribute(\"data-expr\");\n" +
            "            if(expr!=null&&expr.length>0){\n" +
            "                try{\n" +
            "                    var html = texCacheMap[expr];\n" +
            "                    if(html == null){\n" +
            "                        html = katex.renderToString(expr);\n" +
            "                        texCacheMap[expr] = html;\n" +
            "                    }\n" +
            "                    el.innerHTML = html;\n" +
            "                }catch(e){\n" +
            "                    el.className=el.className+\" errorMessage\";\n" +
            "                    el.innerHTML=\"【显示出错：\"+expr+\"】\";\n" +
            "                }\n" +
            "                try{\n" +
            "                    el.removeAttribute(\"data-expr\");\n" +
            "                }catch(e){\n" +
            "                }\n" +
            "            }\n" +
            "        });\n" +
            "        {var m=document.getElementsByTagName(\"img\");if(m!=null&&m.length>0){var " +
            "c=[];for(var v=0;v<m.length;v++){var n=m[v];if(n.parentNode!=null&&n.parentNode" +
            ".id==\"hide_img_div\"){continue}c.push(n)}m=c;var h=document.getElementsByTagName" +
            "(\"body\")[0];var e=document.getElementById(\"hide_img_div\");var z=[];for(var v=0;" +
            "v<m.length;v++){var r=m[v];var p=r.parentNode;var f=r.src;var g=r.attributes;var " +
            "o=0;var u=0;if(g!=null){for(var s=0;s<g.length;s++){var q=g[s];if(q" +
            ".name==\"data-width\"){o=q.value*1}else{if(q.name==\"data-height\"){u=q" +
            ".value*1}}}}var y=p.clientWidth;if(o==null||isNaN(o)||o<0){o=0}if(u==null||isNaN(u)" +
            "||u<0){u=0}var b=\"real_img\"+v;r.src=\"blank.png\";var w=r.style;if(w!=null){w" +
            ".display=\"\";if(o>0&&u>0){var A=y;if(A<=0){A=o}var d=o;var a=u;if(o>A){d=A;a=(u*d)" +
            "/o;a=Math.round(a*100)/100}w.width=d+\"px\";w.height=a+\"px\"}}r.style=w;r.id=b;z" +
            ".push('<img id=\"hide_img'+v+'\" src=\"'+f+'\" name=\"'+b+'\"/>')}e.innerHTML=z.join" +
            "(\"\");for(var t=0;t<m.length;t++){var l=\"hide_img\"+t;var x=document" +
            ".getElementById(l);x.onload=function(j){var B=this;var i=B.name;var k=document" +
            ".getElementById(i);k.src=B.src;B.remove();j.stopPropagation()}}}}\n" +
            "    })();\n" +
            "</script>\n" +
            "</body>\n" +
            "</html>";
    public static final String ITEM_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta http-equiv=\"Content-Language\" content=\"zh-CN\">\n" +
            "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, " +
            "user-scalable=0\">\n" +
            "    <meta name=\"format-detection\" content=\"telephone=no\">\n" +
            "    <link rel=\"stylesheet\" href=\"katex/katex.min.css\">\n" +
            "    <style>\n" +
            "        *{\n" +
            "            -webkit-box-sizing: border-box;\n" +
            "            -moz-box-sizing: border-box;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        body,html{\n" +
            "            margin: 0;background-color: transparent;-moz-user-select: -moz-none;" +
            "-moz-user-select: none;-o-user-select: none;-khtml-user-select: none;" +
            "-webkit-user-select: none;-ms-user-select: none;user-select: none;\n" +
            "        }\n" +
            "        .wFull {\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        img {\n" +
            "            max-width: 100%;\n" +
            "        }\n" +
            "        .katex .reset-textstyle.scriptstyle .cjk-char,\n" +
            "        .katex .reset-textstyle.scriptstyle .cjk_fallback{\n" +
            "            font-size: 0.7em;\n" +
            "        }\n" +
            "        .errorMessage{\n" +
            "            color: red;\n" +
            "        }\n" +
            "        .auto_br, p{\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        .auto_br, .katex, p{\n" +
            "            display: inline-block;white-space: -moz-pre-wrap;white-space: -pre-wrap;" +
            "white-space: -o-pre-wrap;white-space: pre-wrap;word-wrap: break-word;\n" +
            "        }\n" +
            "        p {\n" +
            "            display: block;margin: 0 0 10px;\n" +
            "        }\n" +
            "        p:last-child {\n" +
            "            margin-bottom: 0;\n" +
            "        }\n" +
            "        body, .katex{\n" +
            "            font-size: 16px;line-height: 24px;\n" +
            "        }\n" +
            "        @media screen and (min-device-width: 765px) {\n" +
            "            body, .katex{\n" +
            "                font-size: 20px;line-height: 30px;\n" +
            "            }\n" +
            "        }\n" +
            "        @media screen and (max-device-width: 365px) {\n" +
            "            body, .katex{\n" +
            "                font-size: 14px;line-height: 21px;\n" +
            "            }\n" +
            "        }\n" +
            "        #hide_img_div{\n" +
            "            width:0;height:0;overflow:hidden;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<script type=\"text/javascript\">window[\"redefine_katex_cjkRegex\"] = " +
            "/[\\u3040-\\u309F]|[\\u30A0-\\u30FF]|[\\u4E00-\\u9FAF]|[\\uAC00-\\uD7AF]|[\\u2460" +
            "-\\u2469]/;</script>\n" +
            "###REPLACEMENT###\n" +
            "<div id=\"hide_img_div\"></div>\n" +
            "<script src=\"katex/katex.min.js\"></script>\n" +
            "<script>\n" +
            "    (function() {\n" +
            "        var texCacheMap = {};\n" +
            "        var tex = document.getElementsByClassName(\"tex\");\n" +
            "        Array.prototype.forEach.call(tex, function(el) {\n" +
            "            var expr = el.getAttribute(\"data-expr\");\n" +
            "            if(expr!=null&&expr.length>0){\n" +
            "                try{\n" +
            "                    var html = texCacheMap[expr];\n" +
            "                    if(html == null){\n" +
            "                        html = katex.renderToString(expr);\n" +
            "                        texCacheMap[expr] = html;\n" +
            "                    }\n" +
            "                    el.innerHTML = html;\n" +
            "                }catch(e){\n" +
            "                    el.className=el.className+\" errorMessage\";\n" +
            "                    el.innerHTML=\"【显示出错：\"+expr+\"】\";\n" +
            "                }\n" +
            "                try{\n" +
            "                    el.removeAttribute(\"data-expr\");\n" +
            "                }catch(e){\n" +
            "                }\n" +
            "            }\n" +
            "        });\n" +
            "        {var m=document.getElementsByTagName(\"img\");if(m!=null&&m.length>0){var " +
            "c=[];for(var v=0;v<m.length;v++){var n=m[v];if(n.parentNode!=null&&n.parentNode" +
            ".id==\"hide_img_div\"){continue}c.push(n)}m=c;var h=document.getElementsByTagName" +
            "(\"body\")[0];var e=document.getElementById(\"hide_img_div\");var z=[];for(var v=0;" +
            "v<m.length;v++){var r=m[v];var p=r.parentNode;var f=r.src;var g=r.attributes;var " +
            "o=0;var u=0;if(g!=null){for(var s=0;s<g.length;s++){var q=g[s];if(q" +
            ".name==\"data-width\"){o=q.value*1}else{if(q.name==\"data-height\"){u=q" +
            ".value*1}}}}var y=p.clientWidth;if(o==null||isNaN(o)||o<0){o=0}if(u==null||isNaN(u)" +
            "||u<0){u=0}var b=\"real_img\"+v;r.src=\"blank.png\";var w=r.style;if(w!=null){w" +
            ".display=\"\";if(o>0&&u>0){var A=y;if(A<=0){A=o}var d=o;var a=u;if(o>A){d=A;a=(u*d)" +
            "/o;a=Math.round(a*100)/100}w.width=d+\"px\";w.height=a+\"px\"}}r.style=w;r.id=b;z" +
            ".push('<img id=\"hide_img'+v+'\" src=\"'+f+'\" name=\"'+b+'\"/>')}e.innerHTML=z.join" +
            "(\"\");for(var t=0;t<m.length;t++){var l=\"hide_img\"+t;var x=document" +
            ".getElementById(l);x.onload=function(j){var B=this;var i=B.name;var k=document" +
            ".getElementById(i);k.src=B.src;B.remove();j.stopPropagation()}}}}\n" +
            "    })();\n" +
            "</script>\n" +
            "</body>\n" +
            "</html>";
    public static final String COURSE_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta http-equiv=\"Content-Language\" content=\"zh-CN\">\n" +
            "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, " +
            "user-scalable=0\">\n" +
            "    <meta name=\"format-detection\" content=\"telephone=no\">\n" +
            "    <link rel=\"stylesheet\" href=\"katex/katex.min.css\">\n" +
            "    <style>\n" +
            "        *{\n" +
            "            -webkit-box-sizing: border-box;\n" +
            "            -moz-box-sizing: border-box;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        body,html{\n" +
            "            margin: 0;background-color: transparent;-moz-user-select: -moz-none;" +
            "-moz-user-select: none;-o-user-select: none;-khtml-user-select: none;" +
            "-webkit-user-select: none;-ms-user-select: none;user-select: none;\n" +
            "        }\n" +
            "        .wFull {\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        img {\n" +
            "            max-width: 100%;\n" +
            "        }\n" +
            "        .katex .reset-textstyle.scriptstyle .cjk-char,\n" +
            "        .katex .reset-textstyle.scriptstyle .cjk_fallback{\n" +
            "            font-size: 0.7em;\n" +
            "        }\n" +
            "        .errorMessage{\n" +
            "            color: red;\n" +
            "        }\n" +
            "        .auto_br, p{\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        .auto_br, .katex, p{\n" +
            "            display: inline-block;white-space: -moz-pre-wrap;white-space: -pre-wrap;" +
            "white-space: -o-pre-wrap;white-space: pre-wrap;word-wrap: break-word;\n" +
            "        }\n" +
            "        p {\n" +
            "            display: block;margin: 0 0 10px;\n" +
            "        }\n" +
            "        p:last-child {\n" +
            "            margin-bottom: 0;\n" +
            "        }\n" +
            "        body, .katex{\n" +
            "            font-size: 16px;line-height: 24px;\n" +
            "        }\n" +
            "        @media screen and (min-device-width: 765px) {\n" +
            "            body, .katex{\n" +
            "                font-size: 20px;line-height: 30px;\n" +
            "            }\n" +
            "        }\n" +
            "        @media screen and (max-device-width: 365px) {\n" +
            "            body, .katex{\n" +
            "                font-size: 14px;line-height: 21px;\n" +
            "            }\n" +
            "        }\n" +
            "        .web_hide{\n" +
            "            display: inline-block;\n" +
            "            width: 100%;\n" +
            "            margin-bottom: 50px;\n" +
            "        }\n" +
            "        ul.app_award{\n" +
            "            display: inline-block;\n" +
            "            margin: 0 3%;\n" +
            "            width: 94%;\n" +
            "            -webkit-padding-start: 0;\n" +
            "            -webkit-margin-before: 0;\n" +
            "            -webkit-margin-after: 0;\n" +
            "        }\n" +
            "        ul.app_award li{\n" +
            "            background-color: #f8f6f3;\n" +
            "            width: 48%;\n" +
            "            padding:4%;\n" +
            "            list-style: none;\n" +
            "            margin-top: 4%;\n" +
            "            position: relative;\n" +
            "        }\n" +
            "        ul.app_award .li-0,\n" +
            "        ul.app_award .li-1{\n" +
            "            margin-top:0;\n" +
            "        }\n" +
            "        ul.app_award li.li-l{\n" +
            "            float: left;\n" +
            "        }\n" +
            "        ul.app_award li.li-r{\n" +
            "            float: right;\n" +
            "        }\n" +
            "        ul.app_award .item_title{\n" +
            "            color: #666;\n" +
            "            font-size: 14px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        ul.app_award .item_num{\n" +
            "            color: #bf2428;\n" +
            "            font-size: 24px;\n" +
            "            position: absolute;\n" +
            "            left: 54%;\n" +
            "            top: 36%;\n" +
            "        }\n" +
            "        ul.app_award .item_ico{\n" +
            "            width: 50%;\n" +
            "            margin: 0 auto;\n" +
            "        }\n" +
            "        ul.app_award .item_ico > img{\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        ul.app_award li .left{\n" +
            "            float: left;\n" +
            "            width: 56%;\n" +
            "        }\n" +
            "        ul.app_award li .right{\n" +
            "            float: right;\n" +
            "        }\n" +
            "        @media screen and (max-device-width: 365px) {\n" +
            "            ul.app_award .item_title{\n" +
            "                font-size: 12px;\n" +
            "            }\n" +
            "            ul.app_award .item_num{\n" +
            "                font-size: 22px;\n" +
            "                left: 54%;\n" +
            "                top: 34%;\n" +
            "            }\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<script type=\"text/javascript\">window[\"redefine_katex_cjkRegex\"] = " +
            "/[\\u3040-\\u309F]|[\\u30A0-\\u30FF]|[\\u4E00-\\u9FAF]|[\\uAC00-\\uD7AF]|[\\u2460" +
            "-\\u2469]/;</script>\n" +
            "###REPLACEMENT###\n" +
            "<script src=\"katex/katex.min.js\"></script>\n" +
            "<script>\n" +
            "    (function() {\n" +
            "        var texCacheMap = {};\n" +
            "        var tex = document.getElementsByClassName(\"tex\");\n" +
            "        Array.prototype.forEach.call(tex, function(el) {\n" +
            "            var expr = el.getAttribute(\"data-expr\");\n" +
            "            if(expr!=null&&expr.length>0){\n" +
            "                try{\n" +
            "                    var html = texCacheMap[expr];\n" +
            "                    if(html == null){\n" +
            "                        html = katex.renderToString(expr);\n" +
            "                        texCacheMap[expr] = html;\n" +
            "                    }\n" +
            "                    el.innerHTML = html;\n" +
            "                }catch(e){\n" +
            "                    el.className=el.className+\" errorMessage\";\n" +
            "                    el.innerHTML=\"【显示出错：\"+expr+\"】\";\n" +
            "                }\n" +
            "                try{\n" +
            "                    el.removeAttribute(\"data-expr\");\n" +
            "                }catch(e){\n" +
            "                }\n" +
            "            }\n" +
            "        });\n" +
            "    })();\n" +
            "</script>\n" +
            "</body>\n" +
            "</html>";

    public static final String COURSE_DETAIL_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta http-equiv=\"Content-Language\" content=\"zh-CN\">\n" +
            "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, " +
            "user-scalable=0\">\n" +
            "    <meta name=\"format-detection\" content=\"telephone=no\">\n" +
            "    <link rel=\"stylesheet\" href=\"katex/katex.min.css\">\n" +
            "    <style>\n" +
            "        *{\n" +
            "            -webkit-box-sizing: border-box;\n" +
            "            -moz-box-sizing: border-box;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "        body,html{\n" +
            "            margin: 0;background-color: transparent;-moz-user-select: -moz-none;" +
            "-moz-user-select: none;-o-user-select: none;-khtml-user-select: none;" +
            "-webkit-user-select: none;-ms-user-select: none;user-select: none;\n" +
            "        }\n" +
            "        .wFull {\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        img {\n" +
            "            max-width: 100%;\n" +
            "        }\n" +
            "        .katex .reset-textstyle.scriptstyle .cjk-char,\n" +
            "        .katex .reset-textstyle.scriptstyle .cjk_fallback{\n" +
            "            font-size: 0.7em;\n" +
            "        }\n" +
            "        .errorMessage{\n" +
            "            color: red;\n" +
            "        }\n" +
            "        .auto_br, p{\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        .auto_br, .katex, p{\n" +
            "            display: inline-block;white-space: -moz-pre-wrap;white-space: -pre-wrap;" +
            "white-space: -o-pre-wrap;white-space: pre-wrap;word-wrap: break-word;\n" +
            "        }\n" +
            "        p {\n" +
            "            display: block;margin: 0 0 10px;\n" +
            "        }\n" +
            "        p:last-child {\n" +
            "            margin-bottom: 0;\n" +
            "        }\n" +
            "        body, .katex{\n" +
            "            font-size: 16px;line-height: 24px;\n" +
            "        }\n" +
            "        @media screen and (min-device-width: 765px) {\n" +
            "            body, .katex{\n" +
            "                font-size: 20px;line-height: 30px;\n" +
            "            }\n" +
            "        }\n" +
            "        @media screen and (max-device-width: 365px) {\n" +
            "            body, .katex{\n" +
            "                font-size: 14px;line-height: 21px;\n" +
            "            }\n" +
            "        }\n" +
            "        .web_hide{\n" +
            "            display: inline-block;\n" +
            "            width: 100%;\n" +
            "            margin-bottom: 50px;\n" +
            "        }\n" +
            "        ul.app_award{\n" +
            "            display: inline-block;\n" +
            "            margin: 0 3%;\n" +
            "            width: 94%;\n" +
            "            -webkit-padding-start: 0;\n" +
            "            -webkit-margin-before: 0;\n" +
            "            -webkit-margin-after: 0;\n" +
            "        }\n" +
            "        ul.app_award li{\n" +
            "            background-color: #f8f6f3;\n" +
            "            width: 48%;\n" +
            "            padding:4%;\n" +
            "            list-style: none;\n" +
            "            margin-top: 4%;\n" +
            "            position: relative;\n" +
            "        }\n" +
            "        ul.app_award .li-0,\n" +
            "        ul.app_award .li-1{\n" +
            "            margin-top:0;\n" +
            "        }\n" +
            "        ul.app_award li.li-l{\n" +
            "            float: left;\n" +
            "        }\n" +
            "        ul.app_award li.li-r{\n" +
            "            float: right;\n" +
            "        }\n" +
            "        ul.app_award .item_title{\n" +
            "            color: #666;\n" +
            "            font-size: 14px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        ul.app_award .item_num{\n" +
            "            color: #bf2428;\n" +
            "            font-size: 24px;\n" +
            "            position: absolute;\n" +
            "            left: 54%;\n" +
            "            top: 36%;\n" +
            "        }\n" +
            "        ul.app_award .item_ico{\n" +
            "            width: 50%;\n" +
            "            margin: 0 auto;\n" +
            "        }\n" +
            "        ul.app_award .item_ico > img{\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        ul.app_award li .left{\n" +
            "            float: left;\n" +
            "            width: 56%;\n" +
            "        }\n" +
            "        ul.app_award li .right{\n" +
            "            float: right;\n" +
            "        }\n" +
            "        @media screen and (max-device-width: 365px) {\n" +
            "            ul.app_award .item_title{\n" +
            "                font-size: 12px;\n" +
            "            }\n" +
            "            ul.app_award .item_num{\n" +
            "                font-size: 22px;\n" +
            "                left: 54%;\n" +
            "                top: 34%;\n" +
            "            }\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<script type=\"text/javascript\">window[\"redefine_katex_cjkRegex\"] = " +
            "/[\\u3040-\\u309F]|[\\u30A0-\\u30FF]|[\\u4E00-\\u9FAF]|[\\uAC00-\\uD7AF]|[\\u2460" +
            "-\\u2469]/;</script>\n" +
            "###REPLACEMENT###\n" +
            "<script src=\"katex/katex.min.js\"></script>\n" +
            "<script>\n" +
            "    (function() {\n" +
            "        var texCacheMap = {};\n" +
            "        var tex = document.getElementsByClassName(\"tex\");\n" +
            "        Array.prototype.forEach.call(tex, function(el) {\n" +
            "            var expr = el.getAttribute(\"data-expr\");\n" +
            "            if(expr!=null&&expr.length>0){\n" +
            "                try{\n" +
            "                    var html = texCacheMap[expr];\n" +
            "                    if(html == null){\n" +
            "                        html = katex.renderToString(expr);\n" +
            "                        texCacheMap[expr] = html;\n" +
            "                    }\n" +
            "                    el.innerHTML = html;\n" +
            "                }catch(e){\n" +
            "                    el.className=el.className+\" errorMessage\";\n" +
            "                    el.innerHTML=\"【显示出错：\"+expr+\"】\";\n" +
            "                }\n" +
            "                try{\n" +
            "                    el.removeAttribute(\"data-expr\");\n" +
            "                }catch(e){\n" +
            "                }\n" +
            "            }\n" +
            "        });\n" +
            "    })();\n" +
            "</script>\n" +
            "</body>\n" +
            "</html>";

    public static final String QUESTION_PREVIEW_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta http-equiv=\"Content-Language\" content=\"zh-CN\">\n" +
            "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, " +
            "user-scalable=0\">\n" +
            "    <meta name=\"format-detection\" content=\"telephone=no\">\n" +
            "    <title>习题闯关</title>\n" +
            "    <link rel=\"stylesheet\" href=\"katex/katex.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/base.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/components.css\">\n" +
            "</head>\n" +
            "<body class=\"pass_qu_bd body_loading\">\n" +
            "<div id=\"qu_box_template\" style=\"display:none !important;\">\n" +
            "    <div class=\"item qu_box\" data-qu-index=\"0\">\n" +
            "        <div class=\"qu_title\"></div>\n" +
            "        <div class=\"qu_item_list\">\n" +
            "            <div class=\"input-group qu_item\" data-code=\"A\">\n" +
            "                <span class=\"input-group-addon item_code\">A</span>\n" +
            "                <div class=\"form-control qu_item_val qu_item1\"></div>\n" +
            "            </div>\n" +
            "            <div class=\"input-group qu_item\" data-code=\"B\">\n" +
            "                <span class=\"input-group-addon item_code\">B</span>\n" +
            "                <div class=\"form-control qu_item_val qu_item2\"></div>\n" +
            "            </div>\n" +
            "            <div class=\"input-group qu_item\" data-code=\"C\">\n" +
            "                <span class=\"input-group-addon item_code\">C</span>\n" +
            "                <div class=\"form-control qu_item_val qu_item3\"></div>\n" +
            "            </div>\n" +
            "            <div class=\"input-group qu_item\" data-code=\"D\">\n" +
            "                <span class=\"input-group-addon item_code\">D</span>\n" +
            "                <div class=\"form-control qu_item_val qu_item4\"></div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"qu_explain\" style=\"visibility:hidden;\"></div>\n" +
            "    </div>\n" +
            "</div>\n" +
            "<div id=\"top_bar\" class=\"top_bar\">\n" +
            "    <div class=\"quit_box\"><a id=\"quit_btn\" href=\"javascript:;\">× " +
            "退出</a></div>\n" +
            "    <div class=\"progress_num\">0/5</div>\n" +
            "    <div class=\"progress_box\"><div class=\"progress\"><div class=\"progress-bar\" " +
            "style=\"width:0;\"></div></div></div>\n" +
            "    <div class=\"time_box\">10:00</div>\n" +
            "</div>\n" +
            "<div class=\"carousel slide page_carousel\">\n" +
            "    <div class=\"carousel-inner qu_list\"></div>\n" +
            "    <div class=\"fixed_bottom\">\n" +
            "        <button type=\"button\" id=\"submit_btn\" action-type=\"check\" disabled " +
            "class=\"btn btn-blue btn-lg btn_block\">确定</button>\n" +
            "    </div>\n" +
            "</div>\n" +
            "<div id=\"bottom_toast\">正确，积分+2</div>\n" +
            "<div id=\"loading_toast\" class=\"weui_loading_toast\" style=\"display:none;\">\n" +
            "    <div class=\"weui_mask_transparent\"></div>\n" +
            "    <div class=\"weui_toast\">\n" +
            "        <div class=\"weui_loading\">\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_0\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_1\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_2\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_3\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_4\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_5\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_6\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_7\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_8\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_9\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_10\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_11\"></div>\n" +
            "        </div>\n" +
            "        <p class=\"weui_toast_content\">努力加载中</p>\n" +
            "    </div>\n" +
            "</div>\n" +
            "<script type=\"text/javascript\">window[\"redefine_katex_cjkRegex\"] = " +
            "/[\\u3040-\\u309F]|[\\u30A0-\\u30FF]|[\\u4E00-\\u9FAF]|[\\uAC00-\\uD7AF]|[\\u2460" +
            "-\\u2469]/;</script>\n" +
            "###REPLACEMENT1###\n" +
            "<script src=\"katex/katex.min.js\"></script>\n" +
            "<script src=\"js/jquery.all.min.js\"></script>\n" +
            "<script src=\"js/local-js-bridge.js\"></script>\n" +
            "<script src=\"js/carousel.js\"></script>\n" +
            "<script src=\"js/qu_preview.js\"></script>\n" +
            "###REPLACEMENT2###\n" +
            "</body>\n" +
            "</html>";

    public static final String QUESTION_WRONG_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <meta http-equiv=\"Content-Language\" content=\"zh-CN\">\n" +
            "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=0\">\n" +
            "    <meta name=\"format-detection\" content=\"telephone=no\">\n" +
            "    <title>错题本</title>\n" +
            "    <link rel=\"stylesheet\" href=\"katex/katex.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/base.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/components.css\">\n" +
            "</head>\n" +
            "<body class=\"pass_qu_bd body_loading\">\n" +
            "<div id=\"qu_box_template\" style=\"display:none !important;\">\n" +
            "    <div class=\"item qu_box\" data-qu-index=\"0\">\n" +
            "        <div class=\"qu_title\"></div>\n" +
            "        <div class=\"qu_item_list\">\n" +
            "            <div class=\"input-group qu_item\" data-code=\"A\">\n" +
            "                <span class=\"input-group-addon item_code\">A</span>\n" +
            "                <div class=\"form-control qu_item_val qu_item1\"></div>\n" +
            "            </div>\n" +
            "            <div class=\"input-group qu_item\" data-code=\"B\">\n" +
            "                <span class=\"input-group-addon item_code\">B</span>\n" +
            "                <div class=\"form-control qu_item_val qu_item2\"></div>\n" +
            "            </div>\n" +
            "            <div class=\"input-group qu_item\" data-code=\"C\">\n" +
            "                <span class=\"input-group-addon item_code\">C</span>\n" +
            "                <div class=\"form-control qu_item_val qu_item3\"></div>\n" +
            "            </div>\n" +
            "            <div class=\"input-group qu_item\" data-code=\"D\">\n" +
            "                <span class=\"input-group-addon item_code\">D</span>\n" +
            "                <div class=\"form-control qu_item_val qu_item4\"></div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "        <div class=\"qu_explain\"></div>\n" +
            "    </div>\n" +
            "</div>\n" +
            "<div id=\"top_bar\" class=\"top_bar top_bar_wrong\">\n" +
            "    <div class=\"quit_box\"><a id=\"quit_btn\" href=\"javascript:;\"><img class=\"back_btn temp_img\" src=\"img/back.png\"></a></div>\n" +
            "    <div class=\"chapter_name auto_dian\">&nbsp;</div>\n" +
            "    <div class=\"progress_num_wrong\">99/99</div>\n" +
            "</div>\n" +
            "<div class=\"carousel slide page_carousel\">\n" +
            "    <div class=\"carousel-inner qu_list\"></div>\n" +
            "    <div class=\"fixed_bottom\" style=\"display:none;\">\n" +
            "        <button type=\"button\" id=\"submit_btn\" action-type=\"check\" disabled class=\"btn btn-blue btn-lg btn_block\">确定</button>\n" +
            "    </div>\n" +
            "</div>\n" +
            "<div id=\"bottom_toast\">正确，积分+2</div>\n" +
            "<div id=\"loading_toast\" class=\"weui_loading_toast\" style=\"display:none;\">\n" +
            "    <div class=\"weui_mask_transparent\"></div>\n" +
            "    <div class=\"weui_toast\">\n" +
            "        <div class=\"weui_loading\">\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_0\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_1\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_2\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_3\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_4\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_5\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_6\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_7\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_8\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_9\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_10\"></div>\n" +
            "            <div class=\"weui_loading_leaf weui_loading_leaf_11\"></div>\n" +
            "        </div>\n" +
            "        <p class=\"weui_toast_content\">努力加载中</p>\n" +
            "    </div>\n" +
            "</div>\n" +
            "<script type=\"text/javascript\">window[\"redefine_katex_cjkRegex\"] = /[\\u3040-\\u309F]|[\\u30A0-\\u30FF]|[\\u4E00-\\u9FAF]|[\\uAC00-\\uD7AF]|[\\u2460-\\u2469]/;</script>\n" +
            "###REPLACEMENT1###\n" +
            "<script src=\"katex/katex.min.js\"></script>\n" +
            "<script src=\"js/jquery.all.min.js\"></script>\n" +
            "<script src=\"js/local-js-bridge.js\"></script>\n" +
            "<script src=\"js/carousel.js\"></script>\n" +
            "<script src=\"js/qu_wrong.js\"></script>\n" +
            "###REPLACEMENT2###\n" +
            "</body>\n" +
            "</html>";
}
