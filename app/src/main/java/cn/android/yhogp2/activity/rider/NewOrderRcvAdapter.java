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
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.android.yhogp2.R;
import cn.android.yhogp2.activity.shop.NewOrderActivity;
import cn.android.yhogp2.application.MainApplication;
import cn.android.yhogp2.intentservice.UpdateRiderLocationService;
import cn.android.yhogp2.javabean.MyAddress;
import cn.android.yhogp2.javabean.Order;
import cn.android.yhogp2.javabean.OrdersGroup;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.TextUtilTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class NewOrderRcvAdapter extends RecyclerView.Adapter<NewOrderRcvAdapter.ViewHolder> {
    public static final int TYPE_GROUP = 0;
    public static final int TYPE_EX = 1;
    public List<Order> orderList;
    public List<OrdersGroup> ordersGroupList;
    public int type = 0;

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
            initExHolder(holder, position);
        } else {
            initDefaultHodler(holder, position);
        }
    }

    private void initDefaultHodler(ViewHolder holder, int position) {
        holder.btnOrderItemReject.setVisibility(View.GONE);
        holder.tvOrderItemCharge.setVisibility(View.GONE);
        holder.tvOrderItemUser.setVisibility(View.GONE);
        holder.tvOrderItemState.setVisibility(View.GONE);
        holder.tvOrderItemTAq.setText(ordersGroupList.get(position).getOrdersContent());
        holder.tvOrderItemTime.setText(ordersGroupList.get(position).getTime());
        holder.btnOrderItemGet.setOnClickListener(view -> changeOrderState(position, ordersGroupList.get(position).getId(), Order.RIDER_GET_ORDERS, MainApplication.loginRider.getTel(), 0));
    }

    private void initExHolder(ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.btnOrderItemReject.setVisibility(View.GONE);
        holder.tvOrderItemUser.setText("用户名：" + order.getUserName() + "\n商家：" + order.getShopName());
        holder.tvOrderItemState.setText(order.getStateString());//double 需转为为string来显示
        holder.tvOrderItemTAq.setText("商品内容：" + order.getContent());
        holder.tvOrderItemTime.setText(order.getTime());
        holder.tvOrderItemCharge.setText("配送费：" + order.getDeliveryFee() * 2);
        MyAddress myAddress = new Gson().fromJson(order.getAddressJson(), MyAddress.class);
        holder.tvOrderItemUserAddr.setText("用户地址：" + myAddress.getUserAddr() + "\n商家地址：" + myAddress.getShopAddr());
        holder.btnOrderItemGet.setOnClickListener(view -> changeOrderState(position, orderList.get(position).getOrderId(), Order.RIDER_GET_ORDERS, MainApplication.loginRider.getTel(), 1));
    }

    @Override
    public int getItemCount() {
        if (type == TYPE_EX)
            return orderList == null ? 0 : orderList.size();
        return ordersGroupList == null ? 0 : ordersGroupList.size();
    }


    private void changeOrderState(int position, int orderId, double state, String riderTel, int type) {
        OkHttpUtil.riderChangeOrderState(orderId, state, MainApplication.loginRider.getRiderId(),
                riderTel, type, new Callback() {
            Message msg = RiderHomeActivity.handler.obtainMessage();
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = response.body().string();
                if (responseStr.equals("true")) {
                    msg.what = RiderHomeActivity.GET_ORDER_SUCCESS;
                    msg.arg1 = position;
                    if (type == 0) {
                        List<MyAddress> myAddressList = TextUtilTools.fromToJson(ordersGroupList.
                                        get(position).getAddressJsonList(),
                                new TypeToken<List<MyAddress>>() {
                        }.getType());
                        RoutePlanningActivity.myAddressList = myAddressList;
                    } else {
                        List<MyAddress> addresses = new ArrayList<>();
                        addresses.add(new Gson().fromJson(orderList.get(position).getAddressJson(),
                                MyAddress.class));
                        RoutePlanningActivity.myAddressList = addresses;
                    }

                }else if (responseStr.equals("orderStateHaveChange"))
                    msg.what = RiderHomeActivity.GET_ORDER_FAIL_BY_ORDER_STATE_HAVE_CHANGE;
                else if (responseStr.equals("unfinished"))
                    msg.what = RiderHomeActivity.GET_ORDER_FAIL_BY_UNFINISHED;
                else
                    msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                msg.obj = "changeState";
                RiderHomeActivity.handler.sendMessage(msg);
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                RiderHomeActivity.handler.sendMessage(msg);
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}