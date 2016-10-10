package com.interviewquestion.interactor;

import com.interviewquestion.repository.Question;
import com.interviewquestion.util.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 28/9/16.
 */

public class SplashInteractorImpl implements SplashInteractor {

    @Override
    public void getJavaQuestions(final OnJavaQuestionResponseListener questionResponseListener, Call<Question> questionCall) {
        questionCall.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful()) {
                    questionResponseListener.onSuccess(response.body().getResponse().get(0), Constant.JAVA);
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                t.printStackTrace();
                questionResponseListener.onError(t.getMessage());
            }
        });
    }

    @Override
    public void getAndroidQuestions(final OnAndroidQuestionResponseListener questionResponseListener, Call<Question> questionCall) {

        questionCall.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful()) {
                    questionResponseListener.onSuccess(response.body().getResponse().get(0),Constant.ANDROID);
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                t.printStackTrace();
                questionResponseListener.onError(t.getMessage());
            }
        });
    }

    @Override
    public void getIosQuestion(final OnIosQuestionResponseListener questionResponseListener, Call<Question> questionCall) {

        questionCall.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful()) {
                    questionResponseListener.onSuccess(response.body().getResponse().get(0), Constant.IOS);
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                t.printStackTrace();
                questionResponseListener.onError(t.getMessage());
            }
        });
    }
}
