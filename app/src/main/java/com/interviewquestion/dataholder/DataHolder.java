package com.interviewquestion.dataholder;

import android.content.Context;
import android.content.SharedPreferences;

import com.interviewquestion.models.databasemodel.Questions;
import com.interviewquestion.util.Constant;
import com.interviewquestion.view.activity.HomeActivity;

import java.util.List;

public class DataHolder {
    private static DataHolder dataHolder;
    private List<Questions> questionList;
    private List<Questions> shuffledQuestionList;
    private HomeActivity instance;


    private DataHolder() {
        //singleton
    }

    public synchronized static DataHolder getInstance() {
        if (dataHolder == null) {
            dataHolder = new DataHolder();
        }
        return dataHolder;
    }

    public void setInstance(HomeActivity instance) {
        this.instance = instance;
    }

    public List<Questions> getShuffledQuestionList() {
        return shuffledQuestionList;
    }

    public void setShuffledQuestionList(List<Questions> shuffledQuestionList) {
        this.shuffledQuestionList = shuffledQuestionList;
    }

    public List<Questions> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Questions> questionList) {
        this.questionList = questionList;
    }

    public SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREF, 0);
    }

}
