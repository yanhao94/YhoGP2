package cn.android.yhogp2.activity.shop;


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
import cn.android.yhogp2.javabean.MyAddress;
import cn.android.yhogp2.javabean.Order;
import cn.android.yhogp2.uitils.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderPageRcvAdapter extends RecyclerView.Adapter<OrderPageRcvAdapter.ViewHolder> {
    public static final int TYPE_NEW = 0;
    public static final int TYPE_HISTORY = 1;
    public List<Order> list_resource;

    private int type;

    public OrderPageRcvAdapter(List<Order> list_resource, int type) {
        this.list_resource = list_resource;
        this.type = type;
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
        holder.tvOrderItemUser.setText(list_resource.get(position).getUserName());
        holder.tvOrderItemState.setText(String.valueOf(list_resource.get(position).getStateString()));//double 需转为为string来显示
        holder.tvOrderItemTAq.setText(list_resource.get(position).getContent());
        holder.tvOrderItemTime.setText(list_resource.get(position).getTime());
        holder.tvOrderItemCharge.setText(String.valueOf(list_resource.get(position).getCharge()));
        holder.tvOrderItemUserAddr.setText(new Gson().fromJson(list_resource.get(position).getAddressJson(), MyAddress.class).getUserAddr());
        if (type == TYPE_NEW&&list_resource.size()>0) {
            holder.btnOrderItemGet.setTag(position);
            holder.btnOrderItemReject.setTag(position);
            holder.btnOrderItemGet.setOnClickListener(view -> changeOrderState(position, list_resource.get(position).getOrderId(), Order.SHOP_GET_ORDERS));
            holder.btnOrderItemReject.setOnClickListener(view -> changeOrderState(position, list_resource.get(position).getOrderId(), Order.SHOP_REJECT_ORDERS));
        } else {
            holder.btnOrderItemGet.setVisibility(View.GONE);
            holder.btnOrderItemReject.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list_resource.size();
    }

    @OnClick({R.id.btn_orderItem_get, R.id.btn_orderItem_reject})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_orderItem_get:
                int position = (Integer) view.getTag();
                break;
            case R.id.btn_orderItem_reject:
                int position1 = (Integer) view.getTag();
                Log.i("dsss", "btn_orderItem_reject: position=" + position1);
                break;
        }
    }

    private void changeOrderState(int position, int orderId, double state) {
        OkHttpUtil.changeOrderState(orderId, state, new Callback() {
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
                    list_resource.remove(position);
                } else {
                    msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                }
                msg.obj="changeState";
                NewOrderActivity.handler.sendMessage(msg);
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