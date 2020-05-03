package cn.android.yhogp2.activity.rider;


import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

import static cn.android.yhogp2.activity.shop.OrderPageRcvAdapter.TYPE_NEW;

public class NewOrderRcvAdapter extends RecyclerView.Adapter<NewOrderRcvAdapter.ViewHolder> {
    public static final int TYPE_GROUP = 0;
    public static final int TYPE_EX = 1;
    public List<Order> orderList;
    public List<OrdersGroup> ordersGroupList;
    public int type;

    public NewOrderRcvAdapter(List<Order> list_resource, int type) {
        this.orderList = list_resource;
        this.type = TYPE_EX;
    }

    public NewOrderRcvAdapter(List<OrdersGroup> list_resource) {
        this.ordersGroupList = list_resource;
        this.type = TYPE_GROUP;
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
        if (type == TYPE_EX) {
            initExHolder(holder,position);
        } else {
            initDefaultHodler(holder,position);
        }
    }

    private void initDefaultHodler(ViewHolder holder,int position) {
        holder.btnOrderItemReject.setVisibility(View.GONE);
        holder.tvOrderItemCharge.setVisibility(View.GONE);
        holder.tvOrderItemUser.setVisibility(View.GONE);
        holder.tvOrderItemState.setText(String.valueOf(ordersGroupList.get(position).getState()));
        holder.tvOrderItemTAq.setText(ordersGroupList.get(position).getOrdersContent());
        holder.tvOrderItemTime.setText(ordersGroupList.get(position).getTime());
        holder.btnOrderItemGet.setOnClickListener(view -> changeOrderState(position, orderList.get(position).getOrderId(),Order.RIDER_GET_ORDERS,0));
    }

    private void initExHolder(ViewHolder holder,int position) {
        holder.btnOrderItemReject.setVisibility(View.GONE);
        holder.tvOrderItemUser.setText(orderList.get(position).getUserName());
        holder.tvOrderItemState.setText(String.valueOf(orderList.get(position).getState()));//double 需转为为string来显示
        holder.tvOrderItemTAq.setText(orderList.get(position).getContent());
        holder.tvOrderItemTime.setText(orderList.get(position).getTime());
        holder.tvOrderItemCharge.setText(String.valueOf(orderList.get(position).getCharge()));
        holder.tvOrderItemUserAddr.setText(new Gson().fromJson(orderList.get(position).getAddressJson(), MyAddress.class).getUserAddr());
        holder.btnOrderItemGet.setOnClickListener(view -> changeOrderState(position, orderList.get(position).getOrderId(),Order.RIDER_GET_ORDERS,1));
    }

    @Override
    public int getItemCount() {
        if (type == TYPE_EX)
            return orderList.size();
        return ordersGroupList.size();
    }


    private void changeOrderState(int position, int orderId, double state, int type) {
        OkHttpUtil.riderChangeOrderState(orderId, state, MainApplication.loginRider.getRiderId(),type, new Callback() {
            Message msg = NewOrderActivity.handler.obtainMessage();

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                NewOrderActivity.handler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body().string().equals("true")) {
                    msg.what = OkHttpUtil.REQUEST_SUCCESS;
                    if (type == TYPE_EX)
                        orderList.remove(position);
                    else
                        ordersGroupList.remove(position);
                } else {
                    msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                }
                msg.obj = "changeState";
                NewOrderActivity.handler.sendMessage(msg);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_orderItem_shop)
        ImageView ivOrderItemShop;
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}