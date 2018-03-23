package com.physicmaster.net.response.excercise;


import com.physicmaster.net.response.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by songrui on 16/11/16.
 */

public class GetTopicmapAnalysisResponse extends Response {


    /**
     * data : {"questionWrongList":[{"answer":"C","answerExplain":"<div><p>解析：<\/p><p>因为入射光线与平面镜的夹角是30°，所以入射角为90°- 30°=60°．<\/p><p>根据光的反射定律，反射角等于入射角，反射角也为60°，所以反射光线与入射光线的夹角是120°．故选C．<\/p><\/div>","content":"<div>一束光与镜面成30°，则入射光线和反射光线的夹角为（　　）<\/div>","itemList":[{"itemCode":"A","questionItem":"<div>60°<\/div>"},{"itemCode":"B","questionItem":"<div>30°<\/div>"},{"itemCode":"C","questionItem":"<div>120°<\/div>"},{"itemCode":"D","questionItem":"<div>90°<\/div>"}],"quId":210903,"quType":0,"sort":0,"wrongAnswer":"D","wrongId":33}],"quBatchId":660}
     */

    public DataBean data;

    public static class DataBean implements Serializable{
        /**
         * questionWrongList : [{"answer":"C","answerExplain":"<div><p>解析：<\/p><p>因为入射光线与平面镜的夹角是30°，所以入射角为90°- 30°=60°．<\/p><p>根据光的反射定律，反射角等于入射角，反射角也为60°，所以反射光线与入射光线的夹角是120°．故选C．<\/p><\/div>","content":"<div>一束光与镜面成30°，则入射光线和反射光线的夹角为（　　）<\/div>","itemList":[{"itemCode":"A","questionItem":"<div>60°<\/div>"},{"itemCode":"B","questionItem":"<div>30°<\/div>"},{"itemCode":"C","questionItem":"<div>120°<\/div>"},{"itemCode":"D","questionItem":"<div>90°<\/div>"}],"quId":210903,"quType":0,"sort":0,"wrongAnswer":"D","wrongId":33}]
         * quBatchId : 660
         */

        public int chapterId;
        public List<QuestionWrongListBean> questionWrongList;

        public static class QuestionWrongListBean implements Serializable {
            /**
             * answer : C
             * answerExplain : <div><p>解析：</p><p>因为入射光线与平面镜的夹角是30°，所以入射角为90°- 30°=60°．</p><p>根据光的反射定律，反射角等于入射角，反射角也为60°，所以反射光线与入射光线的夹角是120°．故选C．</p></div>
             * content : <div>一束光与镜面成30°，则入射光线和反射光线的夹角为（　　）</div>
             * itemList : [{"itemCode":"A","questionItem":"<div>60°<\/div>"},{"itemCode":"B","questionItem":"<div>30°<\/div>"},{"itemCode":"C","questionItem":"<div>120°<\/div>"},{"itemCode":"D","questionItem":"<div>90°<\/div>"}]
             * quId : 210903
             * quType : 0
             * sort : 0
             * wrongAnswer : D
             * wrongId : 33
             */

            public String answer;
            public String             answerExplain;
            public String             content;
            public int                quId;
            public int                quType;
            public int                sort;
            public String             wrongAnswer;
            public int                wrongId;
            public List<ItemListBean> itemList;

            public static class ItemListBean implements  Serializable{
                /**
                 * itemCode : A
                 * questionItem : <div>60°</div>
                 */

                public String itemCode;
                public String questionItem;
            }
        }
    }
}
