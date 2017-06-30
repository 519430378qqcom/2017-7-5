package com.lvshandian.lemeng.moudles.mine.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.moudles.mine.bean.BankCardInfo;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.widget.view.RoundDialog;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.lvshandian.lemeng.R.style.dialog;

/**
 * 银行卡提现界面
 */
public class WithdrawalActivity extends BaseActivity {

    @Bind(R.id.tv_titlebar_left)
    TextView tvTitlebarLeft;
    @Bind(R.id.tv_change_balance)
    TextView tvChangeBalance;
    @Bind(R.id.rcy_bank_card)
    RecyclerView rcyBankCard;
    private List<BankCardInfo> bankCardList = new ArrayList<>();
    private BankCardAdapter bankCardAdapter;
    private RoundDialog deleteDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdrawal;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initialized() {
        initTitle("", getString(R.string.unionpay_withdraw), null);
        tvChangeBalance.setText(SharedPreferenceUtils.getGoldCoin(mContext));
        rcyBankCard.setLayoutManager(new LinearLayoutManager(mContext));
        bankCardAdapter = new BankCardAdapter();
        rcyBankCard.setAdapter(bankCardAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryBankCard();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                finish();
                break;
        }
    }

    class BankCardAdapter extends RecyclerView.Adapter<BankCardAdapter.BankCardHolder> {

        @Override
        public BankCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            BankCardHolder bankCardHolder = new BankCardHolder(LayoutInflater.from(mContext).inflate(R.layout.item_bank_card, parent, false));
            return bankCardHolder;
        }


        @Override
        public void onBindViewHolder(BankCardHolder holder, final int position) {
            if (position == bankCardList.size()) {
                holder.cardview.setVisibility(View.GONE);
                holder.rl_add_card.setVisibility(View.VISIBLE);
                holder.rl_add_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBankCard();
                    }
                });
            } else {
                holder.cardview.setVisibility(View.VISIBLE);
                holder.rl_add_card.setVisibility(View.GONE);
                BankCardInfo bankCardInfo = bankCardList.get(position);
                holder.tv_bankcard_name.setText(bankCardInfo.getUsername());
                holder.tv_bankcard_type.setText(bankCardInfo.getCardType());
                String bankNum = bankCardInfo.getCardNum();
                holder.tv_bank_num.setText(bankNum.substring((bankNum.length() - 4)));
                holder.cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectBankCard(position);
                    }
                });
                holder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        isDeleteBankCard(position);
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return bankCardList.size() + 1;
        }

        class BankCardHolder extends RecyclerView.ViewHolder {
            TextView tv_bankcard_name, tv_bankcard_type, tv_bank_num;
            CardView cardview;
            RelativeLayout rl_add_card;

            public BankCardHolder(View itemView) {
                super(itemView);
                tv_bankcard_name = (TextView) itemView.findViewById(R.id.tv_bankcard_name);
                tv_bankcard_type = (TextView) itemView.findViewById(R.id.tv_bankcard_type);
                tv_bank_num = (TextView) itemView.findViewById(R.id.tv_bank_num);
                cardview = (CardView) itemView.findViewById(R.id.cardview);
                rl_add_card = (RelativeLayout) itemView.findViewById(R.id.rl_add_card);
            }
        }
    }

    /**
     * 查询银行卡列表
     */
    private void queryBankCard() {
        String url = UrlBuilder.CHARGE_SERVER_URL_8080 + String.format(UrlBuilder.QUERY_BANK_CARD, appUser.getId());
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("1".equals(obj.getString("code"))) {
                        String str = obj.getString("obj");
                        List<BankCardInfo> list = JsonUtil.json2BeanList(str, BankCardInfo.class);
                        bankCardList.clear();
                        bankCardList.addAll(list);
                        bankCardAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 点击选择银行卡
     *
     * @param position
     */
    private void selectBankCard(int position) {
        View contentView = View.inflate(this,R.layout.dialog_withdrawal,null);
        ImageView iv_back = (ImageView) contentView.findViewById(R.id.iv_back);
        EditText et_withdrawal_amount = (EditText) contentView.findViewById(R.id.et_withdrawal_amount);
        TextView tv_change_balance_dialog = (TextView) contentView.findViewById(R.id.tv_change_balance_dialog);
        Button btn_confirm_withdrawal = (Button) contentView.findViewById(R.id.btn_confirm_withdrawal);
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(contentView)
                .show();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_confirm_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 长按解绑银行卡
     *
     * @param position
     */
    private void deleteBankCard(int position) {
        String url = UrlBuilder.CHARGE_SERVER_URL_8080 + UrlBuilder.DELETE_BANK_CARD;
        OkHttpUtils.post().url(url).addParams("cardId", String.valueOf(bankCardList.get(position).getId()))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("1".equals(obj.getString("code"))) {
                        queryBankCard();
                    }
                    showToast(obj.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 点击跳转添加银行卡
     */
    private void addBankCard() {
        startActivity(new Intent(mContext, AddBankCardActivity.class));
    }

    /**
     * 删除对话框
     */
    private void isDeleteBankCard(final int position) {
        View view = getLayoutInflater().inflate(R.layout.dialog_quit_login, null);
        if (deleteDialog == null) {
            deleteDialog = new RoundDialog(this, view, dialog, 0.66f, 0.2f);
        }
        deleteDialog.setCanceledOnTouchOutside(true);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvSure = (TextView) view.findViewById(R.id.tv_sure);
        tvTitle.setText(mContext.getString(R.string.if_delete_bank_card));
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBankCard(position);
                deleteDialog.dismiss();
            }
        });
        if (!deleteDialog.isShowing()) {
            deleteDialog.show();
        }

    }
}
