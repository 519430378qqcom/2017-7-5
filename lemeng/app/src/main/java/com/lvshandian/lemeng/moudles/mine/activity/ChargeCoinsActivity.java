package com.lvshandian.lemeng.moudles.mine.activity;

import android.app.AlertDialog;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.utils.billingutils.IabBroadcastReceiver;
import com.lvshandian.lemeng.utils.billingutils.IabBroadcastReceiver.IabBroadcastListener;
import com.lvshandian.lemeng.utils.billingutils.IabHelper;
import com.lvshandian.lemeng.utils.billingutils.IabHelper.IabAsyncInProgressException;
import com.lvshandian.lemeng.utils.billingutils.IabResult;
import com.lvshandian.lemeng.utils.billingutils.Inventory;
import com.lvshandian.lemeng.utils.billingutils.Purchase;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.Bind;

/**
 * 颜票充值界面
 */
public class ChargeCoinsActivity extends BaseActivity implements IabBroadcastListener {

    @Bind(R.id.ll_one)
    AutoLinearLayout llOne;
    @Bind(R.id.ll_two)
    AutoLinearLayout llTwo;
    @Bind(R.id.ll_three)
    AutoLinearLayout llThree;
    @Bind(R.id.ll_for)
    AutoLinearLayout llFor;
    @Bind(R.id.ll_five)
    AutoLinearLayout llFive;
    @Bind(R.id.ll_sex)
    AutoLinearLayout llSex;
    @Bind(R.id.btn_cz)
    TextView btnCz;
    @Bind(R.id.lepiao)
    TextView tvLp;
    /**
     * 初始充值金额
     */
    private String money = "6";
    /**
     * 颜票数量
     */
    private String lepiao;

    private  final  static  String TAG="ChargeCoinsActivity";


    // SKU for our subscription (infinite gas)
    static final String SKU_INFINITE_GAS_MONTHLY = "infinite_gas_monthly";
    static final String SKU_INFINITE_GAS_YEARLY = "infinite_gas_yearly";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;
    static final String SKU_PREMIUM = "lemeng.test01";
    static final String SKU_GAS = "lemeng.test01";

    // The helper object
    IabHelper mHelper;


    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_coins;
    }

    @Override
    protected void initListener() {

        llOne.setOnClickListener(this);
        llTwo.setOnClickListener(this);
        llThree.setOnClickListener(this);
        llFor.setOnClickListener(this);
        llFive.setOnClickListener(this);
        llSex.setOnClickListener(this);
        btnCz.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.top_up_recharge), null);
        lepiao = appUser.getGoldCoin();
        tvLp.setText(lepiao);

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjU9OdrUnKqI1+g/LsOLLnHQnI8m0OsLxfeD8I6aeGxCgalcFHEaBXxG+UsaIC1qqtwqZOHGhRAs+BaykuTafqmgxDaOMVAh8dDRfNesvx9NhkR/Gd8dgduonceHHK93H4ybzVUTUyyzpc32rVh3ogMn6ee00G4fWohZIHel3KzhlCCN/1oGD4mS26lfK/aAyerK5cL/wwkfYjzVjPO/8dug3NMJI5EanLmZ8dZAps4yZpAPDUyv6WOPqzzTRLkMJf7KoZ5cefqK5bVFPQkbFnC5TAt5imgljVuHe+vBlMRMPV02Vwp9Rs6qMEHNYuxbRXkuuWiIul++REFPIbaxMjwIDAQAB";

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
//                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(ChargeCoinsActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabAsyncInProgressException e) {
//                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_one:
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "6";
            {
                String payload = "";

                try {
                    mHelper.launchPurchaseFlow(this, SKU_GAS, RC_REQUEST,
                            mPurchaseFinishedListener, payload);
                } catch (IabAsyncInProgressException e) {
//                    complain("Error launching purchase flow. Another async operation in progress.");
//                    setWaitScreen(false);
                }

            }
                break;
            case R.id.ll_two:
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "30";
                break;
            case R.id.ll_three:
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "98";
                break;
            case R.id.ll_for:
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "298";
                break;
            case R.id.ll_five:
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "588";
                break;
            case R.id.ll_sex:
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                money = "1598";
                break;
            //充值
            case R.id.btn_cz:
                showToast(getString(R.string.stay_open));
                break;
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
        }
    }





    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabAsyncInProgressException e) {
//            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
//                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(ChargeCoinsActivity.class.getName(), "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            inventory.getSkuDetails("lemeng.test01");

        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
//                complain("Error purchasing: " + result);
//                setWaitScreen(false);
                return;
            }


            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_GAS)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabAsyncInProgressException e) {

                    return;
                }
            } else if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert("Thank you for upgrading to premium!");

            } else if (purchase.getSku().equals(SKU_INFINITE_GAS_MONTHLY)
                    || purchase.getSku().equals(SKU_INFINITE_GAS_YEARLY)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Infinite gas subscription purchased.");
                alert("Thank you for subscribing to infinite gas!");

            }
        }
    };

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");

//                alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            } else {
//                complain("Error while consuming: " + result);
            }
//            updateUi();
//            setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };
}
