+function ($) {
    'use strict';
    // jQuery对象支持transitionEnd，CSS动画完成后的回调方法
    $.fn.transitionEnd = function (callback) {
        var events = ['webkitTransitionEnd', 'transitionend', 'oTransitionEnd', 'MSTransitionEnd', 'msTransitionEnd'],
            i, dom = this;

        function fireCallBack(e) {
            /*jshint validthis:true */
            if (e.target !== this) return;
            callback.call(this, e);
            for (i = 0; i < events.length; i++) {
                dom.off(events[i], fireCallBack);
            }
        }

        if (callback) {
            for (i = 0; i < events.length; i++) {
                dom.on(events[i], fireCallBack);
            }
        }
        return this;
    };
    $('.ds-dialog-close').on("tap click", function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $(this).parents(".ds-dialog:eq(0)").fadeOut('fast');
    })

    var timeout;

    $.toptip = function (text, duration, type) {
        if (!text) return;
        if (typeof duration === typeof "a") {
            type = duration;
            duration = undefined;
        }
        duration = duration || 3000;
        var className = type ? 'bg-' + type : 'bg-danger';
        var $t = $('.weui-toptips').remove();
        $t = $('<div class="weui-toptips"></div>').appendTo(document.body);
        $t.html(text);
        $t[0].className = 'weui-toptips ' + className

        clearTimeout(timeout);

        if (!$t.hasClass('weui-toptips_visible')) {
            $t.show().width();
            $t.addClass('weui-toptips_visible');
        }

        timeout = setTimeout(function () {
            $t.removeClass('weui-toptips_visible').transitionEnd(function () {
                $t.remove();
            });
        }, duration);
    }
}
(jQuery);