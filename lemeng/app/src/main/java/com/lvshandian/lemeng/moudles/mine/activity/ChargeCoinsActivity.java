package com.lvshandian.lemeng.moudles.mine.activity;

import android.app.AlertDialog;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.billingutils.IabBroadcastReceiver;
import com.lvshandian.lemeng.utils.billingutils.IabBroadcastReceiver.IabBroadcastListener;
import com.lvshandian.lemeng.utils.billingutils.IabHelper;
import com.lvshandian.lemeng.utils.billingutils.IabHelper.IabAsyncInProgressException;
import com.lvshandian.lemeng.utils.billingutils.IabResult;
import com.lvshandian.lemeng.utils.billingutils.Inventory;
import com.lvshandian.lemeng.utils.billingutils.Purchase;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

import static com.lvshandian.lemeng.UrlBuilder.CHARGE_SERVER_URL;

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
     * 商品ID
     */
    private String productId="";
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
                    Log.d(TAG,"Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;
                mBroadcastReceiver = new IabBroadcastReceiver(ChargeCoinsActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabAsyncInProgressException e) {
                    Log.e(TAG,"Error querying inventory. Another async operation in progress.");
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
                productId=mContext.getString(R.string.google_billing_lepiao06);
                break;
            case R.id.ll_two:
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "30";
                productId=mContext.getString(R.string.google_billing_lepiao30);
                break;
            case R.id.ll_three:
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "98";
                productId=mContext.getString(R.string.google_billing_lepiao98);
                break;
            case R.id.ll_for:
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "298";
                productId=mContext.getString(R.string.google_billing_lepiao298);
                break;
            case R.id.ll_five:
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                money = "588";
                productId=mContext.getString(R.string.google_billing_lepiao588);
                break;
            case R.id.ll_sex:
                llFive.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llOne.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llThree.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llFor.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llTwo.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_bai));
                llSex.setBackground(getResources().getDrawable(R.drawable.selector_loginbtn_sloidmain));
                money = "1598";
                productId=mContext.getString(R.string.google_billing_lepiao1598);
                break;
            //充值
            case R.id.btn_cz:
//                showToast(R.string.stay_open);
                googleBuyTicket();

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
            Log.d(TAG,"Error querying inventory. Another async operation in progress.");
        }
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;
            // Is it a failure?
            if (result.isFailure()) {
                Log.d(TAG,"Failed to query inventory: " + result);
                return;
            }

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */
            Log.d(TAG, "Query inventory was successful.");
            List<Purchase> ownedProducts= inventory.getAllPurchases();
            if(ownedProducts!=null&&ownedProducts.size()>0) {
                for (Purchase pur:ownedProducts){
                    Log.d(TAG, "user has purchased ："+pur.toString());
                }
                try {
                    mHelper.consumeAsync(ownedProducts,mConsumeMultiFinishedListener);
                }catch (IabAsyncInProgressException e){
                    e.printStackTrace();
                }
            }
        }
    };

    IabHelper.OnConsumeMultiFinishedListener mConsumeMultiFinishedListener= new IabHelper.OnConsumeMultiFinishedListener() {
        @Override
        public void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results) {

            Log.d(TAG,"-----onConsumeMultiFinished called");
            if(purchases!=null&&purchases.size()>0){
                for (Purchase pc:purchases){
                    googlePay(pc);
                }
            }

            if(results!=null&&results.size()>0) {
                for (IabResult re:results){
                    Log.d(TAG,"-----onConsumeMultiFinished purchase:"+re.toString());
                }
            }
        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;
            if (result.isFailure()) {

                Log.d(TAG, "Purchase isFailure: ");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabAsyncInProgressException e) {
                    Log.d(TAG,"Error querying inventory. Another async operation in progress.");
                }
                return;
            }

            Log.d(TAG, "Purchase successful.");
            if(!mHelper.ismSetupDone()) {
                Log.d(TAG, "mHelper is not setup done");
                showToast(R.string.google_billing_notsetup);
                return;
            }

            if (purchase!=null) {
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabAsyncInProgressException e) {

                    return;
                }
            }else{
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabAsyncInProgressException e) {
                    Log.d(TAG,"Error querying inventory. Another async operation in progress.");
                }
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
            if (mHelper == null) return;
            Log.d(TAG,"-----onConsumeFinished purchase:"+purchase.toString());
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                googlePay(purchase);
                Log.d(TAG, "Consumption successful. Provisioning.");
            } else {
                Log.d(TAG,"Error while consuming: " + result);
            }

            Log.d(TAG, "End consumption flow.");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver=null;
        }
        // very important:
        Log.d(TAG, "Destroying google billing helper.");
        if (mHelper != null) {
            if(mHelper.ismSetupDone()) {
                mHelper.disposeWhenFinished();
            }
            mHelper = null;
        }

    }


    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "googlePay onErrorResponse :"+error);
        }
    };

    private void googleBuyTicket(){
        if(!mHelper.ismSetupDone()) {
            Log.d(TAG, "mHelper is not setup done");
            return;
        }
        try {
            mHelper.launchPurchaseFlow(this, productId, RC_REQUEST,
                    mPurchaseFinishedListener, "");
        } catch (IabAsyncInProgressException e) {
            Log.d(TAG,"Error launching purchase flow. Another async operation in progress.");
        }
    }
    private void googlePay( Purchase purchase) {
        if(purchase==null){
//            String sigdata="{\"orderId\":\"GPA.3388-8327-8706-42888\",\"packageName\":\"com.lvshandian.lemeng\",\"productId\":\"lemeng.test01\",\"purchaseTime\":1498717889599,\"purchaseState\":0,\"purchaseToken\":\"bcdhmhahngngjahpcjchcfpi.AO-J1OyLdd6Hl4t5kpGsNQ46AB0CK582KztWaRIeEj0vVjd8r1xduYVrEGo7RaexaGx-sbHtTV1LqzSUNdtEJfU0SsNGKct8EW84OsVx6Skhe-qQ5zrwAnQaQLSw3ewYE-Wxh4wmV69N\"}";
//            String sigure="CDPpGkl/nWLtXltE7H/MZWMZo86Dke6Ec9h3uvTIlBNdtYQJf2azdDqUPJRbmHWQIfFNx1gjlrOUAl8KCXQnUkuN2+TZPJtRrKRu76iz+LFxoOXJeV9UnZkcVXZ8ebSbVz25kmwrpG4zc4W47myasW/s/3rkI39qADyPPykGMpP7eg6OYACmzY3J2OrqkEhnYU3XmPcMoj8n5IMGCYKo02wGh5wnlQhsdD8hO3fIq0/pyNOJce+kbfeX3Pl5yQ2gtwN1d30ncLtbbW7wl/lY+R6+NiSgNEg/4W7SBMwZcw1JXxYjNMj6CKO9J5Rr6/fcDZ8qVb/TuX2pPiA5hO2LNw==";
//            try {
//                purchase = new Purchase(IabHelper.ITEM_TYPE_INAPP, sigdata, sigure);
//            }catch (JSONException e){
//                Log.d(TAG,"purchase==null"+e.toString());
//            }
            return;
        }
//        final String chargeUrl="http://10.11.1.192:8080/app/google-pay/recharge.html";
        final String chargeUrl=CHARGE_SERVER_URL+"app/google-pay/recharge.html";
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("userId", appUser.getId());
        map.put("productId", purchase.getSku());
        map.put("transactionId", purchase.getOrderId());
        map.put("amount", money);
        map.put("inappPurchaseData", purchase.getOriginalJson());
        map.put("inappDataSignature", purchase.getSignature());

        JSONObject params = new JSONObject(map);
//        Log.d(TAG,"JSONObject:" + params.toString());
        OkHttpUtils.post().url(chargeUrl).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(com.squareup.okhttp.Request request, Exception e) {
                Log.d(TAG, "onError" + e.toString());
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject!=null) {

                        if(jsonObject.getInt("code")==1) {
                            updateTicket();
                        }
                        showToast(jsonObject.getString("msg"));
                    }
                    Log.d(TAG, "onResponse" + jsonObject.toString());
                }catch (JSONException e){
                    Log.d(TAG, "JSONException" + e.toString());
                }
            }
        });
    }

    /**
     * 请求用户信息
     */
    private void updateTicket() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", appUser.getId());
        httpDatas.getNewDataCharServerCode1("查询用户信息", false, Request.Method.POST, UrlBuilder.SELECT_USER_INFO, map, mHandler, RequestCode.SELECT_USER, TAG);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                case RequestCode.SELECT_USER:
                    AppUser mAppUser = JsonUtil.json2Bean(json, AppUser.class);
                    SharedPreferenceUtils.saveUserInfo(mContext, mAppUser);
                    tvLp.setText(mAppUser.getGoldCoin());

                    break;
            }
        }
    };
}
