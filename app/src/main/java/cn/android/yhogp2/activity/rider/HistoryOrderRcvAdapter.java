package cn.android.yhogp2.activity.rider;


import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.android.yhogp2.R;
import cn.android.yhogp2.activity.shop.NewOrderActivity;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.javabean.MyAddress;
import cn.android.yhogp2.javabean.Order;
import cn.android.yhogp2.javabean.OrdersGroup;
import cn.android.yhogp2.uitils.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoryOrderRcvAdapter extends RecyclerView.Adapter<HistoryOrderRcvAdapter.ViewHolder> {
    public List<Order> orderList;

    public HistoryOrderRcvAdapter(List<Order> list_resource) {
        this.orderList = list_resource;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_rcv_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        initHolder(holder, position);

    }


    private void initHolder(ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.btnOrderItemReject.setVisibility(View.GONE);
        holder.btnOrderItemGet.setVisibility(View.GONE);
        if (orderList.get(position).getState() < Order.ORDER_FINISH)
            holder.btnOrderItemStateUp.setVisibility(View.VISIBLE);
        holder.tvOrderItemUser.setText("用户：" + order.getUserName() + "\ntel:" + order.getUserTel() +
                "\n商家：" + order.getShopName() + "\ntel:" + order.getRiderTel());
        holder.tvOrderItemState.setText(order.getStateString());//double 需转为为string来显示
        holder.tvOrderItemTAq.setText("商品内容" + order.getContent());
        holder.tvOrderItemTime.setText(order.getTime());
        holder.tvOrderItemCharge.setText("配送费¥：" + order.getDeliveryFee());
        MyAddress myAddress = new Gson().fromJson(order.getAddressJson(), MyAddress.class);
        holder.tvOrderItemUserAddr.setText("用户地址："+myAddress.getUserAddr()+"\n商家地址"+myAddress.getShopAddr());
        holder.btnOrderItemStateUp.setOnClickListener(view -> changeOrderState(position, orderList.get(position).getOrderId(), orderList.get(position).getState() + 1, orderList.get(position).getRiderTel(), 1));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    private void changeOrderState(int position, int orderId, double state, String riderTel, int type) {
        OkHttpUtil.riderChangeOrderState(orderId, state, MainApplication.loginRider.getRiderId(),
                riderTel, type, new Callback() {
            Message msg = RiderHistoryActivity.handler.obtainMessage();

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                NewOrderActivity.handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body().string().equals("true")) {
                    msg.what = OkHttpUtil.ORDER_RIDER_CHANGE_ORDER_STATE;
                    msg.arg1 = position;
                } else {
                    msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                }
                msg.obj = "changeState";
                RiderHistoryActivity.handler.sendMessage(msg);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_orderItem_user)
        TextView tvOrderItemUser;
        @BindView(R.id.tv_orderItem_state)
        TextView tvOrderItemState;
        @BindView(R.id.tv_orderItem_tAq)
        TextView tvOrderItemTAq;
        @BindView(R.id.tv_orderItem_charge)
        TextView tvOrderItemCharge;
        @BindView(R.id.tv_orderItem_userAddr)
        TextView tvOrderItemUserAddr;
        @BindView(R.id.tv_orderItem_time)
        TextView tvOrderItemTime;
        @BindView(R.id.btn_orderItem_get)
        Button btnOrderItemGet;
        @BindView(R.id.btn_orderItem_reject)
        Button btnOrderItemReject;
        @BindView(R.id.btn_orderItem_stateUp)
        Button btnOrderItemStateUp;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}