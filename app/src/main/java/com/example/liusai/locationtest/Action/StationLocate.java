package com.example.liusai.locationtest.Action;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.TextView;

import com.example.liusai.locationtest.Entity.LatLngAddrEntity;
import com.example.liusai.locationtest.Entity.StationEntity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by liusai on 16/5/12.
 */
public class StationLocate extends AsyncTask<Void, StationEntity, LatLngAddrEntity>{

    private Context context;
    private TextView cellText, locationText;
    ProgressDialog mProgressDialog = null;
    private static final String TAG = StationLocate.class.getSimpleName();
    private static final String TAG_ERRCODE = "errcode";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LON = "lon";
    private static final String TAG_RADIUS = "radius";
    private static final String TAG_ADDRESS = "address";


    public StationLocate(Context context, TextView cellText, TextView locationText) {
        this.context = context;
        this.cellText = cellText;
        this.locationText = locationText;
        mProgressDialog = new ProgressDialog(context);
    }

    private StationEntity getCellInfo() throws Exception{
        StationEntity cell = new StationEntity();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //移动联通选择GsmCellCollocation， 电信选择CdmaCellCollocation
        GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
        String operator = telephonyManager.getNetworkOperator();

        //手机没有sim卡？
        if (location == null || operator == null) {
            throw new Exception("获取基站信息失败！");
        }

        int mcc = Integer.parseInt(operator.substring(0,3));
        int mnc = Integer.parseInt(operator.substring(3));
        int lac = location.getLac();
        int cid = location.getCid();

        cell.setMCC(mcc);
        cell.setMNC(mnc);
        cell.setLAC(lac);
        cell.setCID(cid);

        return cell;
    }

    private LatLngAddrEntity getItude(StationEntity cell) {
        LatLngAddrEntity itude = null;

        try {
            StringBuilder urlPath = new StringBuilder("http://api.cellocation.com/cell/?");
            urlPath.append("mcc=" + cell.getMCC() + "&mnc=" + cell.getMNC() + "&lac=" + cell.getLAC() + "&ci=" + cell.getCID() + "&output=json");
            URL url = new URL(urlPath.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection() ;
            httpURLConnection.setRequestMethod("GET");

            int responseCode = httpURLConnection.getResponseCode();
            if (httpURLConnection.HTTP_OK == responseCode) {
                StringBuilder result = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();

                itude = getAddFromJSON(result.toString());

                Log.d(TAG, "返回的内容：\n" + result.toString());
            } else {
                Log.d(TAG, "返回码出错！");
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return itude;
    }

    private LatLngAddrEntity getAddFromJSON(String result) {
        LatLngAddrEntity itude = null;
        try {
            JSONObject jsonObj = new JSONObject(result);
            String errcode = jsonObj.getString(TAG_ERRCODE);
            if (!errcode.equals("0")){
                return null;
            }
            itude = new LatLngAddrEntity();
            itude.setAddress(jsonObj.getString(TAG_ADDRESS));
            itude.setLatitude(jsonObj.getDouble(TAG_LAT));
            itude.setLongitude(jsonObj.getDouble(TAG_LON));
            itude.setRadius(jsonObj.getInt(TAG_RADIUS));
        } catch (Exception e) {
            Log.e(TAG, "解析JSON出错");
        }

        return itude;
    }

    protected void onPreExecute() {

        mProgressDialog.setMessage("定位中...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

    }

    protected LatLngAddrEntity doInBackground(Void... params) {
        LatLngAddrEntity itude = null;
        try {
            StationEntity cell = getCellInfo();
            publishProgress(cell);

            if (!(cell.getMNC() == 0 || cell.getMNC() == 1)){
                return null;
            }

            itude = getItude(cell);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return itude;
    }

    protected void onProgressUpdate(StationEntity... cell) {
        cellText.setText(String.format("基站信息：mcc:%d, mnc:%d, lac:%d, cid:%d",
                cell[0].getMCC(), cell[0].getMNC(), cell[0].getLAC(), cell[0].getCID()));
    }

    protected void onPostExecute(LatLngAddrEntity itude) {
        StringBuilder sb = new StringBuilder();
        if (null == itude) {
            sb.append("暂时无法提供手机定位，需要联网\n");
        } else {
            sb.append("纬度：" + itude.getLatitude() + "\n");
            sb.append("经度：" + itude.getLongitude() + "\n");
            sb.append("物理位置：" + itude.getAddress() + "\n");
        }

        locationText.setText(sb.toString());

        mProgressDialog.dismiss();
    }
}
