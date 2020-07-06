package cn.android.yhogp2.activity.rider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.android.yhogp2.R;
import cn.android.yhogp2.intentservice.UpdateRiderLocationService;
import cn.android.yhogp2.javabean.MyAddress;
import cn.android.yhogp2.uitils.TextUtilTools;
import cn.android.yhogp2.uitils.overlayutil.BikingRouteOverlay;

public class RoutePlanningActivity extends AppCompatActivity {

    @BindView(R.id.bmapView)
    MapView bmapView;
    @BindView(R.id.btn_up)
    Button btnUp;
    @BindView(R.id.btn_next)
    Button btnNext;
    private BaiduMap baiduMap;
    private RoutePlanSearch mSearch;
    private List<PlanNode> planNodeList;
    private List<PlanNode> planNodeList2;
    String city = UpdateRiderLocationService.city;
    public static List<MyAddress> myAddressList;

    private Handler handler;
    private final static int CANT_GET_ROUTE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_planning);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initHandler();
        baiduMap = bmapView.getMap();
        initLocation();
        //initTextData();
        //initTextRoute();
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case CANT_GET_ROUTE:
                        String route = (String) msg.obj;
                        TextUtilTools.myToast(getApplicationContext(), "抱歉没有得到关于\n" + route + "的路线", 1
                        );
                        break;
                }
            }
        };
    }

    private void initTextRoute() {
        BikingRoutePlanOption routePlanOption = new BikingRoutePlanOption().from(PlanNode.withCityNameAndPlaceName("安顺市", "南山村"))
                .to(PlanNode.withCityNameAndPlaceName("安顺市", "小屯立交"))
                .ridingType(0);
        initRoute(routePlanOption, "路线");
    }

    private void initTextData() {
//        myAddressList = TextUtilTools.fromToJson("[{\"userLatitude\":26.189235,\"userLongtitude\":105.88126,\"shopLatitude\":26.18924,\"shopLongtitude\":105.881248,\"userAddr\":\"小屯立交\",\"shopAddr\":\"小屯村\"},{\"userLatitude\":26.189235,\"userLongtitude\":105.88126,\"shopLatitude\":26.189213,\"shopLongtitude\":105.881254,\"userAddr\":\"小屯立交\",\"shopAddr\":\"南山村\"}]", new TypeToken<List<MyAddress>>() {
//        }.getType());
        myAddressList = TextUtilTools.fromToJson("[{\"userLatitude\":26.1892,\"userLongtitude\":105.881247,\"shopLatitude\":26.18924,\"shopLongtitude\":105.881248,\"userAddr\":\"小屯立交\",\"shopAddr\":\"小屯村\"},{\"userLatitude\":26.1892,\"userLongtitude\":105.881247,\"shopLatitude\":26.189213,\"shopLongtitude\":105.881254,\"userAddr\":\"小屯立交\",\"shopAddr\":\"南山村\"},{\"userLatitude\":26.1892,\"userLongtitude\":105.881247,\"shopLatitude\":26.189213,\"shopLongtitude\":105.881254,\"userAddr\":\"小屯立交\",\"shopAddr\":\"南山村\"}]",
                new TypeToken<List<MyAddress>>() {
                }.getType());
    }


    private boolean coverIt(List<String> limt, String str) {
        for (String s : limt) {
            if (s.equals(str))
                return true;
        }
        return false;
    }

    private void initLocation() {
        baiduMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(UpdateRiderLocationService.la, UpdateRiderLocationService.lo);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(16f);
        baiduMap.animateMapStatus(update);

        MyLocationData locData = new MyLocationData.Builder()
                .latitude(UpdateRiderLocationService.la)
                .longitude(UpdateRiderLocationService.lo).build();
        baiduMap.setMyLocationData(locData);

    }

    private void initRoute(BikingRoutePlanOption brp, String content) {
        mSearch = RoutePlanSearch.newInstance();
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
                BikingRouteOverlay overlay = new BikingRouteOverlay(baiduMap);
                if (bikingRouteResult.getRouteLines() != null && bikingRouteResult.getRouteLines()
                        .size() > 0) {
                    overlay.setData(bikingRouteResult.getRouteLines().get(0));
                    overlay.addToMap();
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = CANT_GET_ROUTE;
                    msg.obj = content;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

        };
        mSearch.setOnGetRoutePlanResultListener(listener);
        if (brp != null) {
            mSearch.bikingSearch(brp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bmapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bmapView.onDestroy();
        if(mSearch!=null)
        mSearch.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapView.onPause();
    }

    @OnClick({R.id.btn_up, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_up:
                try {
                    baiduMap.clear();
                    getGoodsNotes();
                    for (int i = 0; i < planNodeList.size(); i++) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (planNodeList.size() == 1)
                    TextUtilTools.myToast(getApplicationContext(), "您已经在目的地", 1);
                else
                    getGoodsRoute();
                break;
            case R.id.btn_next:
                try {
                    baiduMap.clear();
                    getSendNotes();
                    for (int i = 0; i < planNodeList2.size(); i++) {
                    }
                    if (planNodeList2.size() == 1)
                        TextUtilTools.myToast(getApplicationContext(), "您已经在目的地", 1);
                    else
                        getSendRoute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void getSendRoute() {
        StringBuffer sb = new StringBuffer(256);
        for (int i = 0; i < planNodeList2.size() - 1; i++) {
            sb.append(planNodeList2.get(i).getName()).append("->");
        }
        sb.append(planNodeList2.get(planNodeList2.size() - 1).getName());
        initRoute(getBrp(planNodeList2), sb.toString());
    }

    private void getGoodsRoute() {
        StringBuffer sb = new StringBuffer(256);
        for (int i = 0; i < planNodeList.size() - 1; i++) {
            sb.append(planNodeList.get(i).getName()).append("->");
        }
        sb.append(planNodeList.get(planNodeList.size() - 1).getName());
        initRoute(getBrp(planNodeList), sb.toString());
    }

    private BikingRoutePlanOption getBrp(List<PlanNode> planNodes) {
        BikingRoutePlanOption brp = new BikingRoutePlanOption();
        brp.from(planNodes.get(0));
        for (int i = 1; i < planNodes.size(); i++) {
            brp.to(planNodes.get(i));
        }
        brp.ridingType(0);
        return brp;
    }

    private void getSendNotes() {
        List<String> limt = new ArrayList<>();
        planNodeList2 = new ArrayList<>();
        planNodeList2.add(PlanNode.withCityNameAndPlaceName(city, UpdateRiderLocationService.street));
        limt.add(UpdateRiderLocationService.street);
        for (int i = 0; i < myAddressList.size(); i++) {
            String userAddr = myAddressList.get(i).getUserAddr();
            if (!coverIt(limt, userAddr)) {
                planNodeList2.add(PlanNode.withCityNameAndPlaceName(city, userAddr));
                limt.add(userAddr);
            }
        }
    }

    private void getGoodsNotes() {
        List<String> limt = new ArrayList<>();
        planNodeList = new ArrayList<>();
        planNodeList.add(PlanNode.withCityNameAndPlaceName(city, UpdateRiderLocationService.street));
        limt.add(UpdateRiderLocationService.street);
        for (int i = 0; i < myAddressList.size(); i++) {
            String shopUser = myAddressList.get(i).getShopAddr();
            if (!coverIt(limt, shopUser)) {
                planNodeList.add(PlanNode.withCityNameAndPlaceName(city, shopUser));
                limt.add(shopUser);
            }
        }
    }


}
