package com.physicmaster.modules.study.activity.exercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.physicmaster.R;

public class TestWebActivity extends AppCompatActivity {
    private WebView webView1, webView2, webView3, webView4, webView;
    private RelativeLayout rlOptionA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_excercise);
        webView = (WebView) findViewById(R.id.tv_content);
        webView1 = (WebView) findViewById(R.id.tv_options_A);
        webView2 = (WebView) findViewById(R.id.tv_options_B);
        webView3 = (WebView) findViewById(R.id.tv_options_C);
        webView4 = (WebView) findViewById(R.id.tv_options_D);
        webView1.setFocusable(false);

        rlOptionA = (RelativeLayout) findViewById(R.id.rl_options_A);
        rlOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlOptionA.setSelected(true);
            }
        });

//        webView1.setBackgroundColor(0); // 设置背景色
//        webView1.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        webView.loadUrl("file:///android_asset/www/assert/title.html");
        webView1.loadUrl("file:///android_asset/www/assert/item_a.html");
        webView2.loadUrl("file:///android_asset/www/assert/item_b.html");
//        webView3.loadUrl("file:///android_asset/www/assert/item_c.html");
        String title = "<div><p>用两个相同的电热器给质量相同的物质甲和乙加热，它们的温度随加热时间的变化关系如图所示．已知甲的比热容是<span " +
                "class=\"katex\"><span " +
                "class=\"katex-mathml\"><math><semantics><mrow><mn>1</mn><mi " +
                "mathvariant=\"normal\">" +
                ".</mi><mn>8</mn><mo>×</mo><mn>1</mn><msup><mn>0</mn><mn>3</mn></msup><mi>J</mi" +
                "><mi mathvariant=\"normal\">/</mi><mo>" +
                "(</mo><mi>k</mi><mi>g</mi></mrow><annotation encoding=\"application/x-tex\">1.8 " +
                "\\times 10^3 J/(kg</annotation></semantics></math></span><span " +
                "class=\"katex-html\" aria-hidden=\"true\"><span class=\"strut\" style=\"height:0" +
                ".8141079999999999em;\"></span><span class=\"strut bottom\" style=\"height:1" +
                ".064108em;vertical-align:-0.25em;\"></span><span class=\"base textstyle " +
                "uncramped\"><span class=\"mord mathrm\">1</span><span class=\"mord mathrm\">" +
                ".</span><span class=\"mord mathrm\">8</span><span class=\"mbin\">×</span><span " +
                "class=\"mord mathrm\">1</span><span class=\"mord\"><span class=\"mord " +
                "mathrm\">0</span><span class=\"vlist\"><span style=\"top:-0.363em;margin-right:0" +
                ".05em;\"><span class=\"fontsize-ensurer reset-size5 size5\"><span " +
                "style=\"font-size:0em;\">\u200B</span></span><span class=\"reset-textstyle " +
                "scriptstyle uncramped\"><span class=\"mord mathrm\">3</span></span></span><span " +
                "class=\"baseline-fix\"><span class=\"fontsize-ensurer reset-size5 size5\"><span " +
                "style=\"font-size:0em;\">\u200B</span></span>\u200B</span></span></span><span " +
                "class=\"mord mathit\" style=\"margin-right:0.09618em;\">J</span><span " +
                "class=\"mord mathrm\">/</span><span class=\"mopen\">(</span><span class=\"mord " +
                "mathit\" style=\"margin-right:0.03148em;\">k</span><span class=\"mord mathit\" " +
                "style=\"margin-right:0.03588em;\">g</span></span></span></span>⋅<span " +
                "class=\"katex\"><span " +
                "class=\"katex-mathml\"><math><semantics><mrow><msup><mrow></mrow><mrow><mo>∘</mo" +
                "></mrow></msup><mi>C</mi><mo>)" +
                "</mo></mrow><annotation encoding=\"application/x-tex\">^{\\circ}C)" +
                "</annotation></semantics></math></span><span class=\"katex-html\" " +
                "aria-hidden=\"true\"><span class=\"strut\" style=\"height:0.75em;\"></span><span" +
                " class=\"strut bottom\" style=\"height:1em;vertical-align:-0.25em;" +
                "\"></span><span class=\"base textstyle uncramped\"><span " +
                "class=\"mord\"><span></span><span class=\"vlist\"><span style=\"top:-0.363em;" +
                "margin-right:0.05em;\"><span class=\"fontsize-ensurer reset-size5 size5\"><span " +
                "style=\"font-size:0em;\">\u200B</span></span><span class=\"reset-textstyle " +
                "scriptstyle uncramped\"><span class=\"mord scriptstyle uncramped\"><span " +
                "class=\"mord\">∘</span></span></span></span><span class=\"baseline-fix\"><span " +
                "class=\"fontsize-ensurer reset-size5 size5\"><span style=\"font-size:0em;" +
                "\">\u200B</span></span>\u200B</span></span></span><span class=\"mord mathit\" " +
                "style=\"margin-right:0.07153em;\">C</span><span class=\"mclose\">)" +
                "</span></span></span></span>，据此判断物质乙的比热容为（　　）</p><p><img src=\"http://imgtest" +
                ".thelper.cn/qu/t2.png\"></p></div>";
        webView3.loadDataWithBaseURL("file:///android_asset/www/assert/", "<!DOCTYPE html>\n" +
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
                "        body,html{\n" +
                "            background-color: transparent;-moz-user-select: -moz-none;" +
                "-moz-user-select: none;-o-user-select: none;-khtml-user-select: none;" +
                "-webkit-user-select: none;-ms-user-select: none;user-select: none;\n" +
                "        }\n" +
                "        .katex .reset-textstyle.scriptstyle .cjk-char {\n" +
                "            font-size: 0.7em;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                title +
                "</body>\n" +
                "</html>", "text/html", "UTF-8", "");
        webView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView1.setBackgroundColor(getResources().getColor(R.color.colorBackgound));
            }
        });
        webView4.loadUrl("file:///android_asset/www/assert/item_d.html");
    }
}
