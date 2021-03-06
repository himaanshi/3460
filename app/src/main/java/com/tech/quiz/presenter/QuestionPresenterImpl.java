package com.tech.quiz.presenter;

import com.tech.quiz.adapter.QuestionPagerAdapter;
import com.tech.quiz.dataholder.DataHolder;
import com.tech.quiz.models.databasemodel.Questions;
import com.tech.quiz.repositories.presenter.QuestionPresenter;
import com.tech.quiz.view.activity.QuestionActivity;
import com.tech.quiz.view.views.QuestionView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import library.mvp.MvpBasePresenter;

/**
 * Created by root on 28/9/16.
 */

public class QuestionPresenterImpl extends MvpBasePresenter<QuestionView> implements QuestionPresenter {

    private QuestionPagerAdapter questionPagerAdapter;

    public QuestionPresenterImpl(QuestionView view) {
        attachView(view);
    }

    @Override
    public void onCreate() {
        shuffledQuestionList.addAll(DataHolder.getInstance().getQuestionList());
        shuffleQuestion();
        Collections.shuffle(shuffledQuestionList);
        DataHolder.getInstance().setShuffledQuestionList(shuffledQuestionList);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        questionPagerAdapter = null;
        detachView();
        shuffledQuestionList.clear();
    }

    @Override
    public void prepareListToShowAllQuestion() {
        if (isViewAttached()) {
            getView().showProgress();
            shuffledQuestionList.clear();
            shuffledQuestionList.addAll(DataHolder.getInstance().getQuestionList());
            questionPagerAdapter.notifyDataSetChanged();
            getView().hideProgress();
        }
    }

    @Override
    public void prepareListToShowUnansweredQuestion() {
        if (isViewAttached()) {
            getView().showProgress();
            shuffledQuestionList.clear();
            shuffledQuestionList.addAll(DataHolder.getInstance().getQuestionList());
            Iterator<Questions> iterator = shuffledQuestionList.iterator();
            while (iterator.hasNext()) {
                Questions response = iterator.next();
                if (response.isAttempted())
                    iterator.remove();
            }
            questionPagerAdapter.notifyDataSetChanged();
            getView().hideProgress();
        }
    }

    @Override
    public void prepareListToShowAnsweredQuestion() {
        if (isViewAttached()) {
            getView().showProgress();
            shuffledQuestionList.clear();
            shuffledQuestionList.addAll(DataHolder.getInstance().getQuestionList());
            Iterator<Questions> iterator = shuffledQuestionList.iterator();
            while (iterator.hasNext()) {
                Questions response = iterator.next();
                if (!response.isAttempted())
                    iterator.remove();
            }
            questionPagerAdapter.notifyDataSetChanged();
            getView().hideProgress();
        }
    }

    @Override
    public void prepareListToResetAll() {
        if (isViewAttached()) {
            getView().showProgress();
            shuffledQuestionList.clear();
            shuffledQuestionList.addAll(DataHolder.getInstance().getQuestionList());
            shuffleQuestionAndResetAllLocally();
            questionPagerAdapter.notifyDataSetChanged();
            getView().hideProgress();

        }
    }

    @Override
    public void shuffleQuestion() {
        List<String> shuffledOptionList = new ArrayList<>();
        for (Questions question : shuffledQuestionList) {
            shuffledOptionList.clear();
            if (!question.isAttempted()) {
                String answer = "";
                switch (question.getAnswer()) {
                    case "1":
                        answer = question.getA();
                        break;

                    case "2":
                        answer = question.getB();
                        break;

                    case "3":
                        answer = question.getC();
                        break;

                    case "4":
                        answer = question.getD();
                        break;
                }

                shuffledOptionList.add(question.getA());
                shuffledOptionList.add(question.getB());
                shuffledOptionList.add(question.getC());
                shuffledOptionList.add(question.getD());

                Collections.shuffle(shuffledOptionList);

                int answerIndex = shuffledOptionList.indexOf(answer);

                question.setAnswer(String.valueOf(answerIndex + 1));

                question.setA(shuffledOptionList.get(0));
                question.setB(shuffledOptionList.get(1));
                question.setC(shuffledOptionList.get(2));
                question.setD(shuffledOptionList.get(3));
            }
        }
    }

    @Override
    public void shuffleQuestionAndResetAllLocally() {
        List<String> shuffledOptionList = new ArrayList<>();
        for (Questions response : shuffledQuestionList) {
            shuffledOptionList.clear();
            response.setAttempted(false);
            response.setCorrectAnswerProvided(false);
            response.setUserAnswer(0);
            String answer = "";
            switch (response.getAnswer()) {
                case "1":
                    answer = response.getA();
                    break;

                case "2":
                    answer = response.getB();
                    break;

                case "3":
                    answer = response.getC();
                    break;

                case "4":
                    answer = response.getD();
                    break;
            }

            shuffledOptionList.add(response.getA());
            shuffledOptionList.add(response.getB());
            shuffledOptionList.add(response.getC());
            shuffledOptionList.add(response.getD());

            Collections.shuffle(shuffledOptionList);

            int answerIndex = shuffledOptionList.indexOf(answer);

            response.setAnswer(String.valueOf(answerIndex + 1));

            response.setA(shuffledOptionList.get(0));
            response.setB(shuffledOptionList.get(1));
            response.setC(shuffledOptionList.get(2));
            response.setD(shuffledOptionList.get(3));
        }
    }

    @Override
    public QuestionPagerAdapter initAdapter(int technology) {
        return questionPagerAdapter = new QuestionPagerAdapter(((QuestionActivity) getView()).getSupportFragmentManager(),
                shuffledQuestionList, technology);
    }
}
