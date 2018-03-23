package com.physicmaster.net.response.excercise;


import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by songrui on 16/11/16.
 */

public class GetExerciseResponse extends Response {


    /**
     * data : {"quBatchId":"1480418635147kfwxq3k5","questionList":[{"answer":"C","answerExplain":"由焦耳定律得：Q=I2Rt\r\n\r\n若电阻为2R，电流强度为I/2时，Q'=I22×2R×t=Q2，故选C","content":"通过电阻R的电流强度为I时，在t时间内产生的热量为Q，若电阻为2R，电流强度为I/2，则在时间t内产生的热量为（  ） ","itemList":[{"itemCode":"A","questionItem":"4Q"},{"itemCode":"B","questionItem":"2Q"},{"itemCode":"C","questionItem":"Q/2"},{"itemCode":"D","questionItem":"Q/4"}],"quId":210903,"quType":0,"sort":0},{"answer":"B","answerExplain":"1min通过线圈的电流产生的热量：\r\n\r\nQ=I2Rt=（5A）2×2Ω×60s=3000J．","content":" 把一台电动机接入电压为220V的电路中，通过电动机的电流为5A，电动机线圈的电阻为2Ω，1min通过线圈的电流产生的热量为（　　）","itemList":[{"itemCode":"A","questionItem":"1452000J"},{"itemCode":"B","questionItem":"3000J"},{"itemCode":"C","questionItem":"66000J"},{"itemCode":"D","questionItem":"50J"}],"quId":210904,"quType":0,"sort":1},{"answer":"A","answerExplain":"\r\n\r\nA、电热毯是利用电流的热效应工作的，在额定电压下工作相同时间电能全部产生热量．\r\n\r\nB、电风扇是主要利用电流的磁效应工作的，在额定电压下工作相同时间电能主要转化成机械能，产生热量很少．\r\n\r\nC、电视机在额定电压下工作相同时间主要是把电能转化为声能和光能，产生热量很少．\r\n\r\nD、日光灯在额定电压下工作相同时间主要是把电能转化为光能，产生热量更少．\r\n\r\n所以三个用电器产生热量不一样多，电热毯产生热量最多．","content":"标有\u201c220V  40W\u201d的字样的电视机、电风扇、电热毯和日光灯四种电器，都在额定电压下工作相同的时间，则产生的热量最多的是（　　）","itemList":[{"itemCode":"A","questionItem":"电热毯"},{"itemCode":"B","questionItem":"电风扇"},{"itemCode":"C","questionItem":"电视机"},{"itemCode":"D","questionItem":"日光灯"}],"quId":210905,"quType":0,"sort":2},{"answer":"D","answerExplain":"用电器标有\u201c6 V  3 W\u201d的字样，用电流表测得此时通过它的电流是300 mA，说明该用电器没有在额定电压下工作，因为它的电阻是不变的\r\n\r\nR=U额2P额=6V23W=12Ω，所以根据公式W=I2Rt计算出它的电能\r\n\r\nW=300mA2×12Ω×60s=64.8J","content":"一用电器标有\u201c6 V  3 W\u201d的字样，用电流表测得此时通过它的电流是300 mA，则1 min内该用电器实际消耗的电能是（     ）","itemList":[{"itemCode":"A","questionItem":"180 J  "},{"itemCode":"B","questionItem":"108 J       "},{"itemCode":"C","questionItem":"1.8 J"},{"itemCode":"D","questionItem":"64.8 J"}],"quId":210906,"quType":0,"sort":3},{"answer":"A","answerExplain":"A、电热在有些情况下对我们有利，有时对我们有害，故A说法错误，符合题意；B、电视机、收音机上有小孔是为了让电视机与收音机内部气体与外部大气相通，有利于散热，故B说法正确，不符合题意；\r\n\r\nC、电饭锅工作时，导体发热，电能主要转化为内能，电饭锅是利用电流的热效应工作的，C说法正确，不符合题意；\r\n\r\nD、梅雨季节可用电热给电视机驱潮，D说法正确，不符合题意；故选A．","content":"关于电热的利用，下列说法中错误的是（   ）","itemList":[{"itemCode":"A","questionItem":"电热对我们来说总是有利的"},{"itemCode":"B","questionItem":" 电视机、收音机上开小孔是为了散热"},{"itemCode":"C","questionItem":"电饭锅是利用电流热效应来工作的"},{"itemCode":"D","questionItem":"梅雨季节可用电热给电视机驱潮"}],"quId":210909,"quType":0,"sort":6}],"limitTime":300}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * quBatchId : 1480418635147kfwxq3k5
         * questionList : [{"answer":"C","answerExplain":"由焦耳定律得：Q=I2Rt\r\n\r\n若电阻为2R，电流强度为I/2时，Q'=I22×2R×t=Q2，故选C","content":"通过电阻R的电流强度为I时，在t时间内产生的热量为Q，若电阻为2R，电流强度为I/2，则在时间t内产生的热量为（  ） ","itemList":[{"itemCode":"A","questionItem":"4Q"},{"itemCode":"B","questionItem":"2Q"},{"itemCode":"C","questionItem":"Q/2"},{"itemCode":"D","questionItem":"Q/4"}],"quId":210903,"quType":0,"sort":0},{"answer":"B","answerExplain":"1min通过线圈的电流产生的热量：\r\n\r\nQ=I2Rt=（5A）2×2Ω×60s=3000J．","content":" 把一台电动机接入电压为220V的电路中，通过电动机的电流为5A，电动机线圈的电阻为2Ω，1min通过线圈的电流产生的热量为（　　）","itemList":[{"itemCode":"A","questionItem":"1452000J"},{"itemCode":"B","questionItem":"3000J"},{"itemCode":"C","questionItem":"66000J"},{"itemCode":"D","questionItem":"50J"}],"quId":210904,"quType":0,"sort":1},{"answer":"A","answerExplain":"\r\n\r\nA、电热毯是利用电流的热效应工作的，在额定电压下工作相同时间电能全部产生热量．\r\n\r\nB、电风扇是主要利用电流的磁效应工作的，在额定电压下工作相同时间电能主要转化成机械能，产生热量很少．\r\n\r\nC、电视机在额定电压下工作相同时间主要是把电能转化为声能和光能，产生热量很少．\r\n\r\nD、日光灯在额定电压下工作相同时间主要是把电能转化为光能，产生热量更少．\r\n\r\n所以三个用电器产生热量不一样多，电热毯产生热量最多．","content":"标有\u201c220V  40W\u201d的字样的电视机、电风扇、电热毯和日光灯四种电器，都在额定电压下工作相同的时间，则产生的热量最多的是（　　）","itemList":[{"itemCode":"A","questionItem":"电热毯"},{"itemCode":"B","questionItem":"电风扇"},{"itemCode":"C","questionItem":"电视机"},{"itemCode":"D","questionItem":"日光灯"}],"quId":210905,"quType":0,"sort":2},{"answer":"D","answerExplain":"用电器标有\u201c6 V  3 W\u201d的字样，用电流表测得此时通过它的电流是300 mA，说明该用电器没有在额定电压下工作，因为它的电阻是不变的\r\n\r\nR=U额2P额=6V23W=12Ω，所以根据公式W=I2Rt计算出它的电能\r\n\r\nW=300mA2×12Ω×60s=64.8J","content":"一用电器标有\u201c6 V  3 W\u201d的字样，用电流表测得此时通过它的电流是300 mA，则1 min内该用电器实际消耗的电能是（     ）","itemList":[{"itemCode":"A","questionItem":"180 J  "},{"itemCode":"B","questionItem":"108 J       "},{"itemCode":"C","questionItem":"1.8 J"},{"itemCode":"D","questionItem":"64.8 J"}],"quId":210906,"quType":0,"sort":3},{"answer":"A","answerExplain":"A、电热在有些情况下对我们有利，有时对我们有害，故A说法错误，符合题意；B、电视机、收音机上有小孔是为了让电视机与收音机内部气体与外部大气相通，有利于散热，故B说法正确，不符合题意；\r\n\r\nC、电饭锅工作时，导体发热，电能主要转化为内能，电饭锅是利用电流的热效应工作的，C说法正确，不符合题意；\r\n\r\nD、梅雨季节可用电热给电视机驱潮，D说法正确，不符合题意；故选A．","content":"关于电热的利用，下列说法中错误的是（   ）","itemList":[{"itemCode":"A","questionItem":"电热对我们来说总是有利的"},{"itemCode":"B","questionItem":" 电视机、收音机上开小孔是为了散热"},{"itemCode":"C","questionItem":"电饭锅是利用电流热效应来工作的"},{"itemCode":"D","questionItem":"梅雨季节可用电热给电视机驱潮"}],"quId":210909,"quType":0,"sort":6}]
         * limitTime : 300
         */

        public String quBatchId;
        public int                    limitTime;
        public ArrayList<QuestionListBean> questionList;

        public static class QuestionListBean implements Serializable, Parcelable {
            /**
             * answer : C
             * answerExplain : 由焦耳定律得：Q=I2Rt

             若电阻为2R，电流强度为I/2时，Q'=I22×2R×t=Q2，故选C
             * content : 通过电阻R的电流强度为I时，在t时间内产生的热量为Q，若电阻为2R，电流强度为I/2，则在时间t内产生的热量为（  ）
             * itemList : [{"itemCode":"A","questionItem":"4Q"},{"itemCode":"B","questionItem":"2Q"},{"itemCode":"C","questionItem":"Q/2"},{"itemCode":"D","questionItem":"Q/4"}]
             * quId : 210903
             * quType : 0
             * sort : 0
             */

            public String answer;
            public String             answerExplain;
            public String             content;
            public int                quId;
            public int                quType;
            public int                sort;
            public List<ItemListBean> itemList;

            public static class ItemListBean implements Parcelable {
                /**
                 * itemCode : A
                 * questionItem : 4Q
                 */

                public String itemCode;
                public String questionItem;

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.itemCode);
                    dest.writeString(this.questionItem);
                }

                public ItemListBean() {
                }

                protected ItemListBean(Parcel in) {
                    this.itemCode = in.readString();
                    this.questionItem = in.readString();
                }

                public static final Creator<ItemListBean> CREATOR = new Creator<ItemListBean>() {
                    @Override
                    public ItemListBean createFromParcel(Parcel source) {
                        return new ItemListBean(source);
                    }

                    @Override
                    public ItemListBean[] newArray(int size) {
                        return new ItemListBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.answer);
                dest.writeString(this.answerExplain);
                dest.writeString(this.content);
                dest.writeInt(this.quId);
                dest.writeInt(this.quType);
                dest.writeInt(this.sort);
                dest.writeList(this.itemList);
            }

            public QuestionListBean() {
            }

            protected QuestionListBean(Parcel in) {
                this.answer = in.readString();
                this.answerExplain = in.readString();
                this.content = in.readString();
                this.quId = in.readInt();
                this.quType = in.readInt();
                this.sort = in.readInt();
                this.itemList = new ArrayList<ItemListBean>();
                in.readList(this.itemList, ItemListBean.class.getClassLoader());
            }

            public static final Parcelable.Creator<QuestionListBean> CREATOR = new Parcelable.Creator<QuestionListBean>() {
                @Override
                public QuestionListBean createFromParcel(Parcel source) {
                    return new QuestionListBean(source);
                }

                @Override
                public QuestionListBean[] newArray(int size) {
                    return new QuestionListBean[size];
                }
            };
        }
    }
}
