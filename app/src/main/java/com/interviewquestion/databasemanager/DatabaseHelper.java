package com.interviewquestion.databasemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.interviewquestion.repository.datamodel.Questions;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // Name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "3460_DB";

    // Any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1; // update shared preference for authorized user flag

    private Dao<Questions, Integer> questionModel;
//    private Context context;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Questions.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't Create Database", e);
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {

/*
        System.out.println("onUpgrade :" + oldVersion + " new:" + newVersion);
        try {
            if(oldVersion == 6 && newVersion == 7) {
                sqLiteDatabase.execSQL("ALTER TABLE `TEMPSMS` ADD COLUMN isSmsParsed BOOLEAN;");
            } else {
            TableUtils.dropTable(connectionSource, GraphModel.class, true);
            TableUtils.dropTable(connectionSource, Transaction.class, true);
            TableUtils.dropTable(connectionSource, CashInFlow.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, TempSms.class, true);
            TableUtils.dropTable(connectionSource, SubCategory.class, true);
            TableUtils.dropTable(connectionSource, Template.class, true);
            TableUtils.dropTable(connectionSource, Accounts.class, true);
            TableUtils.dropTable(connectionSource, MerchantMapping.class, true);
            TableUtils.dropTable(connectionSource, Transfers.class, true);
            TableUtils.dropTable(connectionSource, Alarms.class, true);
            TableUtils.dropTable(connectionSource, Bills.class, true);

                onCreate(sqLiteDatabase, connectionSource);

            DataHolder.getInstance().getPreferences(context).edit().
                    putBoolean(Constants.IS_APP_FIRST_LAUNCH, true).apply();
            DataHolder.getInstance().getPreferences(context).edit().
                    putBoolean(Constants.IS_SETUP_RUNNING, false).apply();
            Utils.restartApp(context);
            }
*/
/*

            sqLiteDatabase.execSQL("ALTER TABLE `GRAPHMODEL` RENAME TO GRAPHMODEL_TB;");
            sqLiteDatabase.execSQL("ALTER TABLE `GRAPHMODEL_TB` ADD COLUMN percentage INTEGER;");
            sqLiteDatabase.execSQL("ALTER TABLE `TEMPSMS` ADD COLUMN status TEXT;");
            sqLiteDatabase.execSQL("ALTER TABLE `TEMPSMS` ADD COLUMN errorCode TEXT;");
            sqLiteDatabase.execSQL("ALTER TABLE `CASHINFLOW` RENAME TO CASHINFLOW_TB;");
            sqLiteDatabase.execSQL("ALTER TABLE `CASHINFLOW_TB` ADD COLUMN sms_time_stamp LONG;");
            sqLiteDatabase.execSQL("ALTER TABLE `TRANSACTION` RENAME TO TRANSACTION_TB;");
*//*

        } catch (Exception e) {
            e.printStackTrace();
        }
*/
    }

    /*public Dao<GraphModel, Integer> getGraphModelDao() {
        if (graphModelDao == null) {
            try {
                graphModelDao = getDao(GraphModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return graphModelDao;
    }*/


    public void clearAllTableData() {
        try {
            TableUtils.clearTable(connectionSource, Questions.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearGraphTable() {
        try {
            TableUtils.clearTable(connectionSource, Questions.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
        questionModel = null;
    }
}
