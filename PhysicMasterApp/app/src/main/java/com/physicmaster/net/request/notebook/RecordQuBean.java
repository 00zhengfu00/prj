package com.physicmaster.net.request.notebook;

import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.common.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by huashigen on 2017-12-29.
 */

public class RecordQuBean implements Parcelable {
    /**
     * questionDes：这是一道数学错题              //问题描述  String 长度256  type=1时 questionDes 和questionImg不能同时为空
     * questionImg：[{"h":440,"u":"2324/1241","w":520}]  //图片  json数组格式，限制9张图片  可选
     * userAnswer：A         //用户答案  String 长度256  可选
     * standardAnswer：B  //正确答案  String 长度256  可选
     * analysisDes：解析  // 解析 String 长度256  可选
     * analysisImg：[{"h":440,"u":"2324/1241","w":520}]    //图片  json数组格式，限制9张图片  可选
     * quId：//题目Id Integer 类型  type=0时  非空
     * difficultyLevel：4  //难易程度   Integer 类型 1-5  非空
     * masterLevel：4    //掌握程度  Integer 类型 1-5  非空
     * tagId：7                //错误原因Id   Long 类型  可选默认0
     * dirId：10002       // 目录标签       Long 类型  dirId和newDirName只能有一个有值
     * type：1            // 错题类型，Integer类型 0为系统闯关错题，1为自录错题
     */
    private String questionImg = "";
    private String analysisImg = "";
    private String questionDes = "";
    private String userAnswer = "";
    private String standardAnswer = "";
    private String analysisDes = "";
    private String newTagName = "";
    private String newDirName = "";
    private String tagId;
    private int difficultyLevel;
    private int masterLevel;
    private int subjectId;
    private int dirId = -1;


    public void setNewTagName(String newTagName) {
        String value = null;
        try {
            value = URLEncoder.encode(newTagName, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.newTagName = value;
    }

    public void setNewDirName(String newDirName) {
        String value = null;
        try {
            value = URLEncoder.encode(newDirName, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.newDirName = value;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getNewTagName() {
        return newTagName;
    }

    public String getNewDirName() {
        return newDirName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getQuestionImg() {
        return questionImg;
    }

    public void setQuestionImg(String questionImg) {
        this.questionImg = questionImg;
    }

    public String getAnalysisImg() {
        return analysisImg;
    }

    public void setAnalysisImg(String analysisImg) {
        this.analysisImg = analysisImg;
    }

    public String getQuestionDes() {
        return questionDes;
    }

    public void setQuestionDes(String questionDes) {
        String value = null;
        try {
            value = URLEncoder.encode(questionDes, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.questionDes = value;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        String value = null;
        try {
            value = URLEncoder.encode(userAnswer, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.userAnswer = value;
    }

    public String getStandardAnswer() {
        return standardAnswer;
    }

    public void setStandardAnswer(String standardAnswer) {
        String value = null;
        try {
            value = URLEncoder.encode(standardAnswer, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.standardAnswer = value;
    }

    public String getAnalysisDes() {
        return analysisDes;
    }

    public void setAnalysisDes(String analysisDes) {
        String value = null;
        try {
            value = URLEncoder.encode(analysisDes, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.analysisDes = value;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getMasterLevel() {
        return masterLevel;
    }

    public void setMasterLevel(int masterLevel) {
        this.masterLevel = masterLevel;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public int getDirId() {
        return dirId;
    }

    public void setDirId(int dirId) {
        this.dirId = dirId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questionImg);
        dest.writeString(this.analysisImg);
        dest.writeString(this.questionDes);
        dest.writeString(this.userAnswer);
        dest.writeString(this.standardAnswer);
        dest.writeString(this.analysisDes);
        dest.writeString(this.newDirName);
        dest.writeString(this.newTagName);
        dest.writeInt(this.difficultyLevel);
        dest.writeInt(this.masterLevel);
        dest.writeString(this.tagId);
        dest.writeInt(this.subjectId);
        dest.writeInt(this.dirId);
    }

    public RecordQuBean() {
    }

    protected RecordQuBean(Parcel in) {
        this.questionImg = in.readString();
        this.analysisImg = in.readString();
        this.questionDes = in.readString();
        this.userAnswer = in.readString();
        this.standardAnswer = in.readString();
        this.analysisDes = in.readString();
        this.newDirName = in.readString();
        this.newTagName = in.readString();
        this.difficultyLevel = in.readInt();
        this.masterLevel = in.readInt();
        this.tagId = in.readString();
        this.subjectId = in.readInt();
        this.dirId = in.readInt();
    }

    public static final Creator<RecordQuBean> CREATOR = new Creator<RecordQuBean>() {
        @Override
        public RecordQuBean createFromParcel(Parcel source) {
            return new RecordQuBean(source);
        }

        @Override
        public RecordQuBean[] newArray(int size) {
            return new RecordQuBean[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("questionDes=" + getQuestionDes() + "&");
        stringBuilder.append("questionImg=" + getQuestionImg() + "&");
        stringBuilder.append("userAnswer=" + getUserAnswer() + "&");
        stringBuilder.append("standardAnswer=" + getStandardAnswer() + "&");
        stringBuilder.append("analysisDes=" + getAnalysisDes() + "&");
        stringBuilder.append("analysisImg=" + getAnalysisImg() + "&");
        stringBuilder.append("difficultyLevel=" + getDifficultyLevel() + "&");
        stringBuilder.append("masterLevel=" + getMasterLevel() + "&");
        stringBuilder.append("tagId=" + getTagId() + "&");
        if (dirId != -1) {
            stringBuilder.append("dirId=" + getDirId() + "&");
        }
        stringBuilder.append("type=1&");
        stringBuilder.append("newTagName=" + getNewTagName() + "&");
        stringBuilder.append("newDirName=" + getNewDirName() + "&");
        stringBuilder.append("subjectId=" + getSubjectId());
        return stringBuilder.toString();
    }
}
