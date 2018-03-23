package com.iask.yiyuanlegou1.home.main.product.pay;

public class PayConfig {

    /**
     * appid		-- 应用appid
     * privateKey   -- 应用私钥
     * publicKey	-- 平台公钥(platKey)
     * notifyurl	-- 商户服务端接收支付结果通知的地址
     * URL_onOrder  -- 服务器下单地址
     * */


    /**测试环境*/
//	public static final String appid = "3001259821";
//	public static final String privateKey="MIICXgIBAAKBgQCOqWFTCsTglunf036QBof9EnKo4g6EaZ+IYa
// +u3jMf3p4YeHmOZjvE8jTxJ
// +tLvVfroRJNIRQX81uJKhTbtfkBxQjOiC8y9pyfQXz02WTl6DFIM9Kgjjx51bGnEKP8ploW2ieqKCDBzYCwkVEQfr7ayOPx0WWD0Cw3J6nb3EDUKQIDAQABAoGBAI440BzQdJuN99Q67Ua6LCIgnQw+aMia3/8/m7xSKleQQL4WhOBwjQ93g04TROC5/4eZiTw5SOXjp5Kj0C2FSZp5lmIggAg/KRgFhcdQ28jxmGzBD9+2cshTucYoa7YxtaeuljODT21nh7m5XF9HEngU7Ty8nnW8Byumpxa5bYCBAkEA1d3sSsjR7Y5wV6a9VgZsOKvExoCeUSjSaC6/KAZmJHExZB2Mb4yh+3LSV4Pi9xnz4umk/JFWRMJ7oQs7JsOVUQJBAKrEUf9Dbtm1qzvUy474e83N6+3iUsX8p5giRSahaOSsl7jCnnwqwF8nhh2I40XliH106nC4YV2c3f7cS8BEe1kCQQCY319OPapBgrWvEdL5MPIeuDmaIsoH/YQZUID3nUtZ9Ud25uBBxGbtFDBiujV8qCJ7KsPyffkKgXJZtWt81AVhAkEAnoiHvz0xKfiYMYGKQP66oQOtJjlYsumuBXS7UfPDV5hLeoFjdM6TrUMaJU0yAW/oWOAzzdW+vpOlHLgTszlgcQJAPSHsnZBSlKPJUHt3D7tGedgi6f8ayrbqxlsbvMQEFVzgUr0x1OV4ni+5oZOsP6jfoRLeWbiS8GMZlp2t326fgA==";
//	public static final String
// publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPJmrO2n5qNNfiXM1hAUJlcOL95J2MZ8FJI9HYmD
// +GuFn9K75KFNEjdc3ZaRRmjSW6GA1ZLJ2Ddgf8XWjXZ5g1sO5y6zQWrq9OZpk6plKjAKF8LEfJyS7zClOG
// /ziUNj164GShMWOTmJ0xdLX6wTG/QYQ4DLu43gi1Y+RZbwkSGQIDAQAB";
//	public static final String notifyurl = "http://192.168.0.140:8094/monizhuang/api?type=100";
//	public static final String URL_onOrder = "http://101.251.205.114:40001/payapi/order";
// /**服务器下单接口（测试环境）*/

    /**
     * 线上环境
     */
    public static final String appid = "3005423945";
    public static final String privateKey =
            "MIICWwIBAAKBgQCYPR5nTIZ6I5/Qyw+XMqiySnoH3MheJc3/jkbrUBPV3/95dEPPTubTiwb16gac0kbXSU" +
					"+0q8Ap/qIuTXuUJvEba0cJvXvSBFpfEvn55LJQkt5z6YU2uTmo0BDlph5dsMPyW2lpm" +
					"/L1LeOUasrDAcH0cL7jk6fc5sVCZ" +
					"/YBJakQYwIDAQABAoGAflEoKvn6EtoAPxRd6UylYNofCusMbeHcXRFGkvUsoCduKO5pXVfQevI" +
					"+Ykk+F1z6XqN7uSNRHQcktlqgETuGjbkPEld6TX6QJqKsqg2QDu2jRPwSVJshjuaS+J22cgrL" +
					"/4H5MdYrztzoWajJ+rfv+44nRl3FXTKf5R3xdAdQLIECQQDh2l45Q+jz" +
					"/uTYS7hQrzk7ACU4RymWD2be89vj21YrqIx6zNjxK1vxgc2L+Hve5u4L" +
					"+152xR4XTftog0z7odqhAkEArI9GaNNRu5TIzsWo7CQs2Y67w/RQlWmwrYKd" +
					"/Cc3iJTINrkdMCcnALWx9mPpLbVbDGKhm92LHAwLareM8dgwgwJAR3ayM8qCLpprzOkAtD8Ni9a68DjMNtg+w7ND07B2brARa1XgG0eN+zgIUw7YGyrVDeOG+2vZ3qPlNie9ty16oQJAMt6p6w0g8yORbyNmAPdJIXmqcMH9X46gvwXafRMyk9kIjNlVeXSRUuw9Yl1hyE/GX4hUDBbrg4EpfHRP4JvS8QJAYNCr5R0wHtMP0GMJiG9+6yBG64a8OKNmpAKooB/JLH14JS82EvjGaPqOSS8FGlqsUS3dcNRCObEV2+Pmn0RarQ==";
    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfQ6l" +
            "/VG68w21zCRTNcEbnXfjROdypn/bPNXkS4/0SygSvpefEmX3fUpX4" +
            "+MybFrWa45uHBM4WV8IrlqpYtkf8TxGykvPpMuKxGPb21s8Bjz2wX7n/3" +
            "/7EZG36Zf5SkjSw630hNxXgteHkf41ORRZDP9OxRArpCbwGEimR7yw53wIDAQAB\n";
    public static final String notifyurl = "http://m.mayilezhuan.com/index" +
			".php/pay/iapppay_notifyurl/testResult";
    public static final String URL_onOrder = "http://ipay.iapppay.com:9999/payapi/order";/**服务器下单接口（线上环境）*/

    public static final String WEIXIN_PAY = "WX_APP";
    public static final String IPAY = "iapppay";
    public static final String BALANCE_PAY = "appMoney";
    public static final String YUN_PAY = "7";
}
