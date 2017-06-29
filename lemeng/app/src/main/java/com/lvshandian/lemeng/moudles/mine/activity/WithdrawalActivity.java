package com.lvshandian.lemeng.moudles.mine.activity;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.moudles.mine.bean.BankCardInfo;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 提现界面
 */
public class WithdrawalActivity extends BaseActivity {

    @Bind(R.id.tv_titlebar_left)
    TextView tvTitlebarLeft;
    @Bind(R.id.tv_change_balance)
    TextView tvChangeBalance;
    @Bind(R.id.rcy_bank_card)
    RecyclerView rcyBankCard;
    private ArrayList<BankCardInfo> list;

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
        list = new ArrayList();
        list.add(new BankCardInfo("农业银行", "储蓄卡", "3838"));
        list.add(new BankCardInfo("建设银行", "储蓄卡", "2250"));
        list.add(new BankCardInfo("北京银行", "信用卡", "4356"));
        list.add(new BankCardInfo("瑞士银行", "储蓄卡", "4567"));
        rcyBankCard.setLayoutManager(new LinearLayoutManager(mContext));
        rcyBankCard.setAdapter(new BankCardAdapter());
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
        int BANK_CARD = 0;
        int BANK_CARD_ADD = 1;

        @Override
        public BankCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            BankCardHolder bankCardHolder = new BankCardHolder(LayoutInflater.from(mContext).inflate(R.layout.item_bank_card, parent, false));
            return bankCardHolder;
        }

        @Override
        public int getItemViewType(int position) {
            return list.size() == position ? BANK_CARD_ADD : BANK_CARD;
        }

        @Override
        public void onBindViewHolder(BankCardHolder holder, final int position) {
            if (position == list.size()) {
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
                BankCardInfo bankCardInfo = list.get(position);
                holder.tv_bankcard_name.setText(bankCardInfo.getCardName());
                holder.tv_bankcard_type.setText(bankCardInfo.getCardType());
                holder.tv_bank_num.setText(bankCardInfo.getCardNum());
                holder.cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectBankCard(position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size() + 1;
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
     * 点击选择银行卡
     *
     * @param position
     */
    private void selectBankCard(int position) {

    }

    /**
     * 点击跳转添加银行卡
     */
    private void addBankCard() {
        startActivity(new Intent(mContext, AddBankCardActivity.class));
    }
}
