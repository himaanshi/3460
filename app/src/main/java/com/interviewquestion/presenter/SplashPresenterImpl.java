package com.interviewquestion.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.interviewquestion.R;
import com.interviewquestion.activity.HomeActivity;
import com.interviewquestion.activity.SplashActivity;
import com.interviewquestion.databasemanager.DatabaseManager;
import com.interviewquestion.dataholder.DataHolder;
import com.interviewquestion.fragment.CategoryFragment;
import com.interviewquestion.interactor.SplashInteractor;
import com.interviewquestion.interactor.SplashInteractorImpl;
import com.interviewquestion.network.RetrofitApiService;
import com.interviewquestion.network.RetrofitClient;
import com.interviewquestion.repository.Question;
import com.interviewquestion.repository.databasemodel.Android;
import com.interviewquestion.repository.databasemodel.Ios;
import com.interviewquestion.repository.databasemodel.Java;
import com.interviewquestion.util.Constant;
import com.interviewquestion.view.SplashView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by root on 28/9/16.
 */

public class SplashPresenterImpl implements SplashPresenter, SplashInteractor.OnIosQuestionResponseListener,
        SplashInteractor.OnAndroidQuestionResponseListener, SplashInteractor.OnJavaQuestionResponseListener {

    private WeakReference<SplashView> splashView;
    private SplashInteractor splashInteractor;
    private int serviceCount;

    public SplashPresenterImpl(WeakReference<SplashView> splashView) {
        this.splashView = splashView;
        splashInteractor = new SplashInteractorImpl();
    }

    @Override
    public void onDestroy() {
        splashView.clear();
    }

    @Override
    public void prepareToFetchQuestion() {
        if (splashView.get() != null) {
            SplashActivity context = (SplashActivity) splashView.get();
            if (context.isInternetAvailable()) {

                splashView.get().showProgress();
                RetrofitApiService apiService = RetrofitClient.getRetrofitClient();
                Call<Question> androidQuestionCall;
                if (context.isServiceCallExist(Constant.ANDROID_URL)) {
                    androidQuestionCall = context.getServiceCallIfExist(Constant.ANDROID_URL);
                } else {
                    androidQuestionCall = apiService.getAndroidQuestion();
                    context.putServiceCallInServiceMap(androidQuestionCall, Constant.ANDROID_URL);
                }


                Call<Question> iosQuestionCall;
                if (context.isServiceCallExist(Constant.IOS_URL)) {
                    iosQuestionCall = context.getServiceCallIfExist(Constant.IOS_URL);
                } else {
                    iosQuestionCall = apiService.getIosQuestion();
                    context.putServiceCallInServiceMap(iosQuestionCall, Constant.IOS_URL);
                }

                Call<Question> javaQuestionCall;
                if (context.isServiceCallExist(Constant.JAVA_URL)) {
                    javaQuestionCall = context.getServiceCallIfExist(Constant.JAVA_URL);
                } else {
                    javaQuestionCall = apiService.getJavaQuestion();
                    context.putServiceCallInServiceMap(javaQuestionCall, Constant.JAVA_URL);
                }

                splashInteractor.getAndroidQuestions(this, androidQuestionCall);
                splashInteractor.getIosQuestion(this, iosQuestionCall);
                splashInteractor.getJavaQuestions(this, javaQuestionCall);
            } else {
                context.onError(context.getString(R.string.error_internet_first_launch));
            }

        }
    }

    @Override
    public void onSuccess(List<Question.Response> questionList, int serviceType) {
        serviceCount++;
        saveDataToDB(questionList, serviceType);
        if (serviceCount == 3)
            goToHomeActivity();
    }

    @Override
    public void onError(String error) {
        serviceCount--;
//        displayDataReloadAlert();
    }

    @Override
    public void displayDataReloadAlert() {
        try {
            final CategoryFragment context = (CategoryFragment) splashView.get();
            if (context.isAdded() && context.getActivity() != null) {
                new AlertDialog.Builder(context.getActivity())
                        .setTitle("Error")
                        .setMessage("Error receiving data from server, Reload Again...?")
                        .setPositiveButton("Reload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (context.getArguments().getInt("serviceType")) {
                                    case Constant.ANDROID:
//                                        prepareToFetchQuestion(Constant.ANDROID);
                                        break;

                                    case Constant.IOS:
//                                        prepareToFetchQuestion(Constant.IOS);
                                        break;

                                    case Constant.JAVA:
//                                        prepareToFetchQuestion(Constant.JAVA);
                                        break;
                                }

                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                context.getActivity().onBackPressed();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    synchronized public void saveDataToDB(List<Question.Response> questionList, int serviceType) {
        DatabaseManager databaseManager = DatabaseManager.getDataBaseManager((SplashActivity) splashView.get());
        switch (serviceType) {
            case Constant.ANDROID:
                saveAndroidQuestion(databaseManager, questionList);
                break;

            case Constant.IOS:
                saveIosQuestion(databaseManager, questionList);
                break;

            case Constant.JAVA:
                saveJavaQuestion(databaseManager, questionList);
                break;
        }
    }

    private void saveAndroidQuestion(DatabaseManager databaseManager, List<Question.Response> questionList) {
        List<Android> androidList = new ArrayList<>();
        for (Question.Response question : questionList) {
            Android android = new Android();
            android.setId(Integer.parseInt(question.getId()));
            android.setUserLevel(Integer.parseInt(question.getQuestionType()));
            android.setCategory(question.getCategory());
            android.setQuestion(question.getQuestion());
            android.setA(question.getA());
            android.setB(question.getB());
            android.setC(question.getC());
            android.setD(question.getD());
            android.setAnswer(question.getAnswer());
            androidList.add(android);
        }
        databaseManager.clearAndroidTableData();
        databaseManager.saveQuestionToAndroidTable(androidList);

    }

    private void saveIosQuestion(DatabaseManager databaseManager, List<Question.Response> questionList) {
        List<Ios> iosList = new ArrayList<>();

        for (Question.Response question : questionList) {
            Ios ios = new Ios();
            ios.setId(Integer.parseInt(question.getId()));
            ios.setUserLevel(Integer.parseInt(question.getQuestionType()));
            ios.setCategory(question.getCategory());
            ios.setQuestion(question.getQuestion());
            ios.setA(question.getA());
            ios.setB(question.getB());
            ios.setC(question.getC());
            ios.setD(question.getD());
            ios.setAnswer(question.getAnswer());
            iosList.add(ios);
        }
        databaseManager.clearIosTableData();
        databaseManager.saveQuestionToIosTable(iosList);

    }

    private void saveJavaQuestion(DatabaseManager databaseManager, List<Question.Response> questionList) {
        List<Java> javaList = new ArrayList<>();

        for (Question.Response question : questionList) {
            Java java = new Java();
            java.setId(Integer.parseInt(question.getId()));
            java.setUserLevel(Integer.parseInt(question.getQuestionType()));
            java.setCategory(question.getCategory());
            java.setQuestion(question.getQuestion());
            java.setA(question.getA());
            java.setB(question.getB());
            java.setC(question.getC());
            java.setD(question.getD());
            java.setAnswer(question.getAnswer());
            javaList.add(java);
        }
        databaseManager.clearJavaTableData();
        databaseManager.saveQuestionToJavaTable(javaList);

    }

    @Override
    public void goToHomeActivity() {
        Context context = (SplashActivity) splashView.get();
        DataHolder.getInstance().getPreferences(context).edit().putBoolean(Constant.IS_APP_FIRST_LAUNCH, false).apply();
        splashView.get().hideProgress();
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        ((SplashActivity) context).finish();
    }
}
