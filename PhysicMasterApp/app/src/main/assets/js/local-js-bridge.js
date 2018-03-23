(function (window) {
    "use strict";
    if (window.LocalJSBridge) {
        return window.LocalJSBridge;
    }
    var apiName = {
            // JS可以主动调用Native的方法
            showLoading: "showLoading", // 显示原生loading
            hideLoading: "hideLoading", // 隐藏原生loading
            quit: "quit",// 退出Web窗口
            updateViewHeight: "updateViewHeight",// 更新Web窗口高度
            submitPreviewQuBatch: "submitPreviewQuBatch", // 预习题闯关提交做题数据
            loadWrongPool: "loadWrongPool", // 错题池列表分页加载
            removeFromWrongPool: "removeFromWrongPool", // 删除错题池中记录
            addWrongDir: "addWrongDir", // 添加错题目录
            saveQuWrong: "saveQuWrong", // 把错题池中记录保存到错题本
            loadWrongByDir: "loadWrongByDir", // 错题本某个目录下错题列表分页加载
            removeWrong: "removeWrong", // 删除错题记录
            archiveWrong: "archiveWrong" // 错题归档
        },
        all_code = {
            success: 200,
            cancel: -1,
            error_default: 0
        },
        bridge = null,
        LocalJSBridge = {},
        debug = false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        isDocumentReady = false, // document 是否ready
        isJSBridgeReady = false, // WebViewJavascriptBridge 是否ready
        isSetupCalled = false, // config方法是否已经调用过了
        document = window.document,
        userAgent = window.navigator.userAgent,
        documentReadyCallbackList = [], // document ready后回调的方法列表
        jsBridgeReadyCallbackList = [],  // WebViewJavascriptBridge ready后回调的方法列表
        eventsHandlers = {};

    /**
     * 构建WebViewJavascriptBridge
     * @param callback 构建成功后回调的方法 参数WJWebViewJavascriptBridge
     * @param setupErrorCallback JSBridge构建失败回调的方法 参数:{code:0,msg:"出错啦"}
     */
    var isFn = function (fn) {
            return fn != null && typeof fn == "function";
        },
        isArray = function (array) {
            return array != null && typeof array == "object" && array instanceof Array;
        },
        isNotEmptyArray = function (array) {
            return isArray(array) && array.length > 0;
        },
        setupWebViewJavascriptBridge = function (callback, setupErrorCallback) {
            if (window.WJWebViewJavascriptBridge) {
                if (callback != null && typeof callback == "function") {
                    callback(window.WJWebViewJavascriptBridge);
                }
                return;
            }
            if (window.WJWVJBCallbacks) {
                if (callback != null && typeof callback == "function"
                    && window.WJWVJBCallbacks.indexOf(callback) == -1) {
                    window.WJWVJBCallbacks.push(callback);
                }
                return;
            }
            if (callback != null && typeof callback == "function") {
                window.WJWVJBCallbacks = [callback];
            } else {
                window.WJWVJBCallbacks = [];
            }
            if (setupErrorCallback != null && typeof setupErrorCallback == "function") {
                window.WJWVJBSetupErrorCallback = setupErrorCallback;
            }
            var isAndroid = /Android/.test(userAgent)
            if (isAndroid) {
                window.__wj_bridge_loaded__.setup();
            } else {
                var WVJBIframe = document.createElement('iframe');
                WVJBIframe.style.display = 'none';
                WVJBIframe.src = 'https://__wj_bridge_loaded__';
                document.documentElement.appendChild(WVJBIframe);
                setTimeout(function () {
                    document.documentElement.removeChild(WVJBIframe)
                }, 0);
            }
        },
    // The ready event handler
        documentReadyCompleted = function (event) {
            // readyState === "complete" is good enough for us to call the dom ready in oldIE
            if (document.addEventListener || event.type === "load" || document.readyState === "complete") {
                documentReadyDetach();
                invokeDocumentReadyCallbacks();
            }
        },
    // Clean-up method for dom ready events
        documentReadyDetach = function () {
            if (document.addEventListener) {
                document.removeEventListener("DOMContentLoaded", documentReadyCompleted, false);
                window.removeEventListener("load", documentReadyCompleted, false);
            } else {
                document.detachEvent("onreadystatechange", documentReadyCompleted);
                window.detachEvent("onload", documentReadyCompleted);
            }
        },
    // invoke document ready callback
        invokeDocumentReadyCallbacks = function () {
            if (isDocumentReady) {
                return;
            }
            // Make sure body exists, at least, in case IE gets a little overzealous (ticket #5443).
            if (!document.body) {
                return setTimeout(invokeDocumentReadyCallbacks, 0);
            }
            // Remember that the DOM is ready
            isDocumentReady = true;
            if (documentReadyCallbackList != null) {
                // fire document ready callbacks
                var callback = documentReadyCallbackList;
                documentReadyCallbackList = [];
                for (var index = 0; index < callback.length; index++) {
                    callback[index].apply(document, document);
                }
            }
        },
    // invoke WebViewJavascriptBridge ready callback
        invokeJSBridgeReadyCallbacks = function () {
            if (jsBridgeReadyCallbackList != null) {
                // fire WebViewJavascriptBridge ready callbacks
                var callback = jsBridgeReadyCallbackList;
                jsBridgeReadyCallbackList = [];
                for (var index = 0; index < callback.length; index++) {
                    callback[index].apply();
                }
            }
        },
    // bind on document ready
        bindOnDocumentReady = function () {
            // Catch cases where $(document).ready() is called after the browser event has already occurred.
            // we once tried to use readyState "interactive" here, but it caused issues like the one
            if (document.readyState === "complete") {
                // Handle it asynchronously to allow scripts the opportunity to delay ready
                setTimeout(invokeDocumentReadyCallbacks, 0);

                // Standards-based browsers support DOMContentLoaded
            } else if (document.addEventListener) {
                // Use the handy event callback
                document.addEventListener("DOMContentLoaded", documentReadyCompleted, false);

                // A fallback to window.onload, that will always work
                window.addEventListener("load", documentReadyCompleted, false);

                // If IE event model is used
            } else {
                // Ensure firing before onload, maybe late but safe also for iframes
                document.attachEvent("onreadystatechange", documentReadyCompleted);

                // A fallback to window.onload, that will always work
                window.attachEvent("onload", documentReadyCompleted);

                // If IE and not a frame
                // continually check to see if the document is ready
                var top = false;

                try {
                    top = window.frameElement == null && document.documentElement;
                } catch (e) {
                }

                if (top && top.doScroll) {
                    (function doScrollCheck() {
                        if (!isDocumentReady) {
                            try {
                                // Use the trick by Diego Perini
                                // http://javascript.nwbox.com/IEContentLoaded/
                                top.doScroll("left");
                            } catch (e) {
                                return setTimeout(doScrollCheck, 50);
                            }

                            // detach all dom ready events
                            documentReadyDetach();

                            // and execute any waiting functions
                            invokeDocumentReadyCallbacks();
                        }
                    })();
                }
            }
        },
        tryConsoleLog = function (msg) {
            if (window.console && window.console.log && typeof window.console.log == "function") {
                window.console.log(msg);
            }
        },

        callHandler = function (method, param, callback) {
            bridge.callHandler(method, param, callback);
        },
        registerHandler = function (method, handler) {
            if (!isFn(handler)) {
                throw new Error("handler function required when call registerHandler.");
            }
            bridge.registerHandler(method, handler);
        },
        /**
         * 根据Native Api回调的JSON数据，选择要执行哪个方法
         * @param data Native Api回调时，传入的参数
         * @param success 执行成功了的回调方法
         * @param error 执行出错了的回调方法
         * @param cancel 用户取消了的回调方法
         */
        invokeDataCallback = function (jsonStr, success, error, cancel) {
            var data = jsonStr;// iOS是直接传入JSON对象，无需在parse了
            if (jsonStr != null && typeof(jsonStr) == "string") {
                data = JSON.parse(jsonStr);
            }
            if (data == null) {
                data = {code: all_code.error_default};
            }
            var code = data.code * 1;
            switch (code) {
                case all_code.success:
                    if (success != null) {
                        if (data.data != null) {
                            success(data.data);
                        } else if (data.msg != null) {
                            success(data.msg);
                        } else {
                            success(data);
                        }
                    }
                    break;
                case all_code.cancel:
                    if (cancel != null) {
                        cancel(data);
                    }
                    break;
                default :
                    if (error != null) {
                        error(data);
                    }
                    break;
            }
        },
        /**
         * 执行某种事件的所有回调方法
         * @param eventName
         */
        invokeEventsHandlers = function (eventName, param) {
            var handlers = eventsHandlers[eventName];
            if (isArray(handlers)) {
                for (var i = 0; i < handlers.length; i++) {
                    handlers[i].apply(LocalJSBridge, [param]);
                }
            }
        },
        /**
         * 绑定用户事件的处理方法
         * @param eventName
         * @param handler
         */
        bindEvent = function (eventName, handler) {
            if (!isFn(handler)) {
                throw new Error("handler function required when bind event " + eventName);
            }
            var handlers = eventsHandlers[eventName];
            if (handlers) {
                if (handlers.indexOf(handler) == -1) {
                    handlers.push(handler);
                }
            } else {
                eventsHandlers[eventName] = handlers = [handler];
                registerHandler(eventName, function (_param) {
                    var param = _param;
                    if (_param != null && typeof(_param) == "string") {
                        param = JSON.parse(_param);
                    }
                    invokeEventsHandlers(eventName, param);
                });
            }
        };

    /**
     * 需要最先调用的方法，用于构建WebViewJavascriptBridge
     * @param option {
     *      debug:false  // 开启调试模式，默认false，调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
     *      error:function(res)  // 当setup配置出错时，回调方法；res:{code:0,msg:"出错啦"}
     * }
     */
    LocalJSBridge.setup = function (option) {
        if (isSetupCalled) {
            // 已经调用了setup方法，拒绝再次被调用
            if (option && typeof option.error == "function") {
                option.error({code: 0, msg: "setup has been called."});
                return;
            } else {
                throw new Error("setup has been called.");
            }
        }
        if (option) {
            debug = option.debug === true;
        }
        isSetupCalled = true;
        documentReadyCallbackList.push(function (document) {
            // document ready 后调用setupWebViewJavascriptBridge
            if (debug) {
                tryConsoleLog("document ready.");
                alert("document ready.");
            }
            setupWebViewJavascriptBridge(function () {
                if (debug) {
                    tryConsoleLog("Bridge ready.");
                    alert("Bridge ready.");
                }
                isJSBridgeReady = true;
                bridge = window.WJWebViewJavascriptBridge;
                // JSBridge ready 后调用invokeJSBridgeReadyCallbacks
                invokeJSBridgeReadyCallbacks();
            }, function (res) {
                if (option && typeof option.error == "function") {
                    option.error(res);
                } else {
                    alert(res.msg);
                }
            });
        });
        bindOnDocumentReady();
    }
    /**
     * 绑定当JSBridge ready要执行的方法
     * @param fn
     */
    LocalJSBridge.ready = function (fn) {
        if (fn != null && typeof fn == "function") {
            if (jsBridgeReadyCallbackList.indexOf(fn) == -1) {
                jsBridgeReadyCallbackList.push(fn);
            }
            if (isJSBridgeReady) {
                // JSBridge已经ready，则直接异步调用
                setTimeout(invokeJSBridgeReadyCallbacks, 0);
            }
        }
    }
    /**
     * 显示原生Loading效果
     * @param option {
     *  msg:"努力加载中",
     *  success:function(){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.showLoading = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        var msg = option != null ? option.msg : null;
        var param = null;
        if (msg != null && msg != "") {
            param = {msg: msg};
        }
        callHandler(apiName.showLoading, param, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    /**
     * 隐藏原生Loading效果
     * @param option {
     *  success:function(){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.hideLoading = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        callHandler(apiName.hideLoading, null, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    /**
     * 退出Web窗口
     * @param option {
     *  type:0,
     *  success:function(){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.quit = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        var type = option != null ? option.type : null;
        var param = null;
        if (type != null) {
            param = {type: type};
        }
        callHandler(apiName.quit, param, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    /**
     * 习题闯关数据提交
     * @param option {
     *  answerJson:"{...}",
     *  success:function(){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.submitPreviewQuBatch = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        var answerJson = option != null ? option.answerJson : null;
        if (answerJson == null || answerJson == "") {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "answerJson required."});
                return;
            } else {
                throw new Error("answerJson required.");
            }
        }
        var param = {answerJson: answerJson};
        callHandler(apiName.submitPreviewQuBatch, param, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    /**
     * 向Native注册事件方法（即此方法有Native主动调用）
     * @param handler
     */
    LocalJSBridge.registerHandler = function (key, handler) {
        bindEvent(key, handler);
    }
    /**
     * 更新Web窗口高度
     * @param option {
     *  height:0,
     *  success:function(){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.updateViewHeight = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        var height = option != null ? option.height : null;
        if (height == null || isNaN(height) || height <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "height required."});
                return;
            } else {
                throw new Error("height required.");
            }
            return;
        }
        var param = null;
        if (height != null) {
            param = {height: height};
        }
        callHandler(apiName.updateViewHeight, param, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    /**
     * 错题池列表分页加载
     * @param option {
     *  subjectId:1,
     *  next:1,
     *  success:function({code:200,data:{}}){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.loadWrongPool = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        var subjectId = option != null ? option.subjectId : null;
        var next = option != null ? option.next : null;
        if (subjectId == null || isNaN(subjectId) || subjectId <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "subjectId required."});
                return;
            } else {
                throw new Error("subjectId required.");
            }
            return;
        }
        var param = {subjectId: subjectId};
        if (next != null) {
            param.next = next;
        }
        callHandler(apiName.loadWrongPool, param, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    /**
     * 删除错题池中记录
     * @param option {
     *  id:#quWrongPoolId#,
     *  success:function({code:200,msg:""}){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.removeFromWrongPool = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        var id = option != null ? option.id : null;
        if (id == null || isNaN(id) || id <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "id required."});
                return;
            } else {
                throw new Error("id required.");
            }
            return;
        }
        var param = {id: id};
        callHandler(apiName.removeFromWrongPool, param, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    /**
     * addWrongDir 添加错题目录
     * @param option {
     *  subjectId:1,
     *  dirName:"目录名",
     *  success:function({code:200,data:{}}){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.addWrongDir = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        var subjectId = option != null ? option.subjectId : null;
        var dirName = option != null ? option.dirName : null;
        if (subjectId == null || isNaN(subjectId) || subjectId <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "subjectId required."});
                return;
            } else {
                throw new Error("subjectId required.");
            }
            return;
        }
        if (dirName == null || dirName == '') {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "dirName required."});
                return;
            } else {
                throw new Error("dirName required.");
            }
            return;
        }
        var param = {subjectId: subjectId, dirName: dirName};
        callHandler(apiName.addWrongDir, param, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    /**
     * 录入系统错题
     * @param option {
     *  id:#quWrongPoolId#,difficultyLevel:5,masterLevel:5,tagId:"[1,3,4]",newTagName:"新标签名称",dirId:111,newDirName:"新目录名称",
     *  success:function({code:200,data:{}}){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.saveQuWrong = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        var id = option != null ? option.id : null;
        if (id == null || isNaN((id = id * 1)) || id <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "id required."});
                return;
            } else {
                throw new Error("id required.");
            }
            return;
        }
        var difficultyLevel = option != null ? option.difficultyLevel : null;
        if (difficultyLevel == null || isNaN((difficultyLevel = difficultyLevel * 1)) || difficultyLevel <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "difficultyLevel required."});
                return;
            } else {
                throw new Error("difficultyLevel required.");
            }
            return;
        }
        var masterLevel = option != null ? option.masterLevel : null;
        if (masterLevel == null || isNaN((masterLevel = masterLevel * 1)) || masterLevel <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "masterLevel required."});
                return;
            } else {
                throw new Error("masterLevel required.");
            }
            return;
        }
        var tagId = option != null ? option.tagId : null;
        var newTagName = option != null ? option.newTagName : null;
        var dirId = option != null ? option.dirId : null;
        var newDirName = option != null ? option.newDirName : null;
        var param = {id: id, difficultyLevel: difficultyLevel, masterLevel: masterLevel};
        if (tagId != null && tagId != '' && tagId != '[]') {
            param.tagId = tagId;
        }
        if (newTagName != null && newTagName != '') {
            param.newTagName = newTagName;
        }
        if (dirId != null && !isNaN((dirId = dirId * 1)) && dirId > 0) {
            param.dirId = dirId;
        }
        if (newDirName != null && newDirName != '') {
            param.newDirName = newDirName;
        }
        callHandler(apiName.saveQuWrong, param, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    /**
     * 错题本某个目录下错题列表分页加载
     * @param option {
     *  dirId:1,
     *  sort:0,
     *  desc:1,
     *  next:1,
     *  success:function({code:200,data:{}}){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.loadWrongByDir = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        var dirId = option != null ? option.dirId : null;
        var sort = option != null ? option.sort : null;
        var desc = option != null ? option.desc : null;
        var next = option != null ? option.next : null;
        if (dirId == null || isNaN(dirId) || dirId <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "dirId required."});
                return;
            } else {
                throw new Error("dirId required.");
            }
            return;
        }
        if (sort == null || isNaN(sort) || sort < 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "sort required."});
                return;
            } else {
                throw new Error("sort required.");
            }
            return;
        }
        if (desc == null || isNaN(desc) || desc < 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "desc required."});
                return;
            } else {
                throw new Error("desc required.");
            }
            return;
        }
        var param = {
            dirId: dirId,
            sort: sort,
            desc: desc
        };
        if (next != null) {
            param.next = next;
        }
        callHandler(apiName.loadWrongByDir, param, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    /**
     * 删除错题记录
     * @param option {
     *  id:#wrongId#,
     *  subjectId:1,
     *  success:function({code:200,msg:""}){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.removeWrong = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        var id = option != null ? option.id : null;
        var subjectId = option != null ? option.subjectId : null;
        if (id == null || isNaN(id) || id <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "id required."});
                return;
            } else {
                throw new Error("id required.");
            }
            return;
        }
        if (subjectId == null || isNaN(subjectId) || subjectId <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "subjectId required."});
                return;
            } else {
                throw new Error("subjectId required.");
            }
            return;
        }
        var param = {id: id, subjectId: subjectId};
        callHandler(apiName.removeWrong, param, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    /**
     * 错题归档
     * @param option {
     *  id:#wrongId#,
     *  subjectId:1,
     *  success:function({code:200,msg:""}){},
     *  error:function({code:0,msg:"ERROR"}){}
     * }
     */
    LocalJSBridge.archiveWrong = function (option) {
        var success = option != null ? option.success : null;
        var error = option != null ? option.error : null;
        var id = option != null ? option.id : null;
        var subjectId = option != null ? option.subjectId : null;
        if (id == null || isNaN(id) || id <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "id required."});
                return;
            } else {
                throw new Error("id required.");
            }
            return;
        }
        if (subjectId == null || isNaN(subjectId) || subjectId <= 0) {
            if (isFn(error)) {
                error({code: all_code.error_default, msg: "subjectId required."});
                return;
            } else {
                throw new Error("subjectId required.");
            }
            return;
        }
        var param = {id: id, subjectId: subjectId};
        callHandler(apiName.archiveWrong, param, function (data) {
            invokeDataCallback(data, success, error, null);
        });
    }
    window.LocalJSBridge = LocalJSBridge;
    return LocalJSBridge;
})(this);