package cn.android.yhogp2.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.android.yhogp2.R;
import cn.android.yhogp2.javabean.Goods;
import cn.android.yhogp2.uitils.OkHttpUtil;
import cn.android.yhogp2.uitils.TextUtilTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GoodsRcvAdapter extends RecyclerView.Adapter<GoodsRcvAdapter.ViewHolder> {

    private List<Goods> goodsList;
    private static Context context;
    private static int[] imageIds = {R.drawable.gaodian, R.drawable.mianshi, R.drawable.mifan, R.drawable.shaokao, R.drawable.haixian, R.drawable.huoguo, R.drawable.zhaji, R.drawable.naicha, R.drawable.kafei, R.drawable.hanbao, R.drawable.kuaican, R.drawable.qita};

    public GoodsRcvAdapter(List<Goods> goodsList, Context context) {
        this.goodsList = goodsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_rcv_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, GoodsSettingActivity.class);
            intent.putExtra("goods", goodsList.get(viewHolder.getAdapterPosition()));
            context.startActivity(intent);
        });
        viewHolder.itemView.setOnLongClickListener(view12 -> {
            showDeleteDialog(goodsList.get(viewHolder.getAdapterPosition()).getId());
            return false;
        });
        return viewHolder;
    }

    private void showDeleteDialog(int goodsId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("是否删除此商品");
        builder.setPositiveButton("是", (dialogInterface, i) -> OkHttpUtil.deleteGoods(goodsId
                , new Callback() {
            Message msg = ShopHomeActivity.orderHandler.obtainMessage();

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                msg.what = OkHttpUtil.REQUEST_FAIL_NET;
                ShopHomeActivity.orderHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body().string().equals("true"))
                    msg.what = ShopHomeActivity.DELETE_GOODS_SUCCESS;
                else
                    msg.what = OkHttpUtil.REQUEST_FAIL_SERVER;
                ShopHomeActivity.orderHandler.sendMessage(msg);
            }
        }));
        builder.setNegativeButton("否", (dialogInterface, i) -> {
        });
        builder.show();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //set resource
        int nrTimes = goodsList.get(position).getNrTimes();
        int prTimes = goodsList.get(position).getPrTimes();
        double HighOption;
        if (prTimes == 0)
            HighOption = 0;
        else
            HighOption = 100 * (double) prTimes / (double) (nrTimes + prTimes);
        holder.ivGoodsImage.setBackgroundResource(imageIds[goodsList.get(position).getTypeId()]);
        holder.tvGoodsName.setText(goodsList.get(position).getName());
        holder.tvGoodsNrTimes.setText("差评次数：" + nrTimes);
        holder.tvGoodsPrTimes.setText("好评次数：" + prTimes);
        holder.tvGoodsPrice.setText("¥" + goodsList.get(position).getPrice());
        holder.tvGoodsSellMonthly.setText("总售：" + goodsList.get(position).getSalesAll());
        holder.tvGoodsType.setText("类型：" + goodsList.get(position).getTypeString());
        holder.tvGoodsHighOption.setText("好评率：" + TextUtilTools.getAPoint(HighOption) + "%");

    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_goods_image)
        ImageView ivGoodsImage;
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.tv_goods_type)
        TextView tvGoodsType;
        @BindView(R.id.tv_goods_sellMonthly)
        TextView tvGoodsSellMonthly;
        @BindView(R.id.tv_goods_highOption)
        TextView tvGoodsHighOption;
        @BindView(R.id.tv_goods_prTimes)
        TextView tvGoodsPrTimes;
        @BindView(R.id.tv_goods_nrTimes)
        TextView tvGoodsNrTimes;
        @BindView(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @BindView(R.id.cb_goods)
        CheckBox cbGoods;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
