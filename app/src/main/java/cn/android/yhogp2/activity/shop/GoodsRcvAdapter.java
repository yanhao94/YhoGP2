package cn.android.yhogp2.activity.shop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.android.yhogp2.R;
import cn.android.yhogp2.javabean.Goods;
import cn.android.yhogp2.uitils.TextUtilTools;

public class GoodsRcvAdapter extends RecyclerView.Adapter<GoodsRcvAdapter.ViewHolder> {

    private List<Goods> goodsList;
    private static Context context;

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
            Intent intent = new Intent(context,GoodsSettingActivity.class);
            intent.putExtra("goods",goodsList.get(viewHolder.getAdapterPosition()));
            context.startActivity(intent);
        });
        return viewHolder;
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
            HighOption = prTimes / (nrTimes + prTimes);
        holder.tvGoodsName.setText(goodsList.get(position).getName());
        holder.tvGoodsNrTimes.setText("差评次数：" + nrTimes);
        holder.tvGoodsPrTimes.setText("好评次数：" + prTimes);
        holder.tvGoodsPrice.setText("¥" + goodsList.get(position).getPrice());
        holder.tvGoodsSellMonthly.setText("月售：" + goodsList.get(position).getSalesMonth());
        holder.tvGoodsType.setText("类型："+goodsList.get(position).getType());
        holder.tvGoodsHighOption.setText("好评率："+TextUtilTools.getAPoint(HighOption)+"%");
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

   public void showCheckBox() {

    }
}
