package com.leon.jinanbus.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.leon.jinanbus.R;
import com.leon.jinanbus.domain.BusInfo;
import com.leon.jinanbus.domain.BusLine;
import com.leon.jinanbus.utils.LocationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 线路图View
 * <p/>
 * Modify from 2016.5.29 By Leon
 */
public class BusLineView extends View {
    private int mRowHeight;
    private int mRowWidth;
    private int mAddHeight;

    private int mLinkSize;
    private int mNodeTextSize;
    private int mNodeTextColor;
    private int mLinkColor;

    private boolean mShowBusId;

    private Bitmap busOri;
    private Bitmap busLuxury;
    private Bitmap stationOri;
    private Bitmap stationStart;
    private Bitmap stationEnd;

    private BusLine.Result mBusLine;
    private List<BusInfo.Result> mBusInfos;

    public BusLineView(Context context) {
        this(context, null);
    }

    public BusLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(SCROLLBAR_POSITION_DEFAULT, null);
        }

        Resources resources = getResources();

        mRowHeight = resources.getDimensionPixelSize(R.dimen.busline_view_row_height);
        mLinkSize = resources.getDimensionPixelSize(R.dimen.busline_view_link_size);
        mNodeTextSize = resources.getDimensionPixelSize(R.dimen.busline_view_node_text_size);
        mAddHeight = resources.getDimensionPixelSize(R.dimen.busline_view_busid_add_height);

        mNodeTextColor = resources.getColor(R.color.black_text);
        mLinkColor = resources.getColor(R.color.busline_view_link_color);

        busOri = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bus_original);
        busLuxury = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bus_luxury);
        stationStart = BitmapFactory.decodeResource(getResources(), R.drawable.ic_staiton_start);
        stationEnd = BitmapFactory.decodeResource(getResources(), R.drawable.ic_staiton_end);
        stationOri = BitmapFactory.decodeResource(getResources(), R.drawable.ic_staiton_original);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRowWidth = getMeasuredWidth() / 6; // 获取到父控件宽度的1/6，也就是一行分成6块，显示5个车站

        if (mBusLine != null) {
            // 应该绘制的行数
            int lineSize = ((mBusLine.stations.size() + 5 - 1) / 5) + 1;
            // 根据车站的数量设置高度
            setMeasuredDimension(getMeasuredWidth(), (mRowHeight * lineSize) - mRowHeight / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBusLine != null) {
            drawLine(canvas);
            drawStation(canvas);
        }

        if (mBusInfos != null && mBusInfos.size() > 0) {
            drawBusLocation(canvas);
        }
    }

    /**
     * 绘制路线
     */
    private void drawLine(Canvas canvas) {
        canvas.save(); // 保存canvas的初始状态

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); //设置抗锯齿
        paint.setStrokeWidth(mLinkSize); // 设置笔画宽度
        paint.setColor(mLinkColor);

        int stationsCount = mBusLine.stations.size();
        int lineCount = ((stationsCount + 5) - 1) / 5; // 计算要绘制路线的行数

        canvas.translate(mRowWidth, mRowHeight); // 移动坐标到路线开始位置

        for (int j = 0; j < lineCount; j++) {
            int min = Math.min(stationsCount - (j * 5), 5) - 1;
            int i = j % 2 != 0 ? min * (-mRowWidth) : min * mRowWidth;

            canvas.drawLine(0.0f, 0.0f, i, 0, paint);
            canvas.translate(i, 0);

            if (j < lineCount - 1) {
                i = mRowHeight;
                canvas.drawLine(0.0f, 0.0f, 0, i, paint);
                canvas.translate(0, i);
            }
        }

        canvas.restore();  // 还原到canvas的初始状态
    }

    /**
     * 绘制站点
     */
    private void drawStation(Canvas canvas) {
        canvas.save();

        int stationCount = mBusLine.stations.size(); // 所有站的数量

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(mNodeTextSize);
        paint.setColor(mNodeTextColor);

        canvas.translate(mRowWidth, 0); // 将坐标x轴相对移动至开始位置，y轴不变

        // 开始绘制站点
        for (int index = 0; index < stationCount; index++) {
            int translateX = 0;
            int translateY = 0;

            if (index % 5 != 0) {
                translateX = (index / 5) % 2 != 0 ? -mRowWidth : mRowWidth;
            } else {
                // 绘制坐标点是原始高度
                translateY = mRowHeight;
            }

            // 移动绘制坐标到当前绘制的站点位置
            canvas.translate(translateX, translateY);

            if (index == 0) {
                // 当前是第一个站， 绘制起点图标
                canvas.drawBitmap(stationStart, (-stationStart.getWidth()) / 2, (-stationStart.getHeight()) / 2, null);
            } else if (index == stationCount - 1) {
                // 当前是最后一个站， 绘制终点图标
                canvas.drawBitmap(stationEnd, (-stationEnd.getWidth()) / 2, (-stationEnd.getHeight()) / 2, null);
            } else {
                // 绘制普通站点图标
                canvas.drawBitmap(stationOri, (-stationOri.getWidth()) / 2, (-stationOri.getHeight()) / 2, null);
            }

            // 获得当前站点的名字
            String stationName = mBusLine.stations.get(index).stationName;
            drawStationText(stationName, canvas, paint);
        }

        canvas.restore();
    }

    /**
     * 绘制当前站点名字
     *
     * @param stationName 站点名
     */
    private void drawStationText(String stationName, Canvas canvas, Paint paint) {
        // 计算30度角所对应的弧度
        double radian = Math.toRadians(30);
        // 计算文字可以显示的最大宽度
        int maxWidth = (int) (mRowWidth * Math.acos(radian));

        List<String> list = new ArrayList<>();
        int index = 0;

        while (index < stationName.length()) {
            // 当前宽度可以显示的最大文字的个数
            int breakText = paint.breakText(stationName, index, stationName.length(), true, maxWidth, new float[]{});
            // 将当前文字根据最大个数截取后添加到List
            list.add(stationName.substring(index, index + breakText));
            index += breakText;
        }

        Path path = new Path();

        if (list.size() == 1) {
            // 只有一行文字
            String str = list.get(0);
            path.moveTo(-str.length() * mNodeTextSize / 2, -mRowHeight / 4);
            path.lineTo(mRowWidth, (float) (-mRowWidth * Math.tan(radian)));

            canvas.drawTextOnPath(str, path, 0, 14f, paint);
        } else if (list.size() > 1) {
            // 有多行文字
            for (int i = 0; i < list.size(); i++) {
                String str = list.get(i);
                path.moveTo(-str.length() * mNodeTextSize / 2, -mRowHeight / 4);
                path.lineTo(mRowWidth, (float) (-mRowWidth * Math.tan(radian)));

                canvas.drawTextOnPath(list.get(i), path, 0, ((i + 1) * 35) - (list.size() * 22), paint);
            }
        }
    }

    /**
     * 绘制车辆位置
     */
    private void drawBusLocation(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); //设置抗锯齿
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(mNodeTextSize);
        paint.setColor(mNodeTextColor);

        for (BusInfo.Result busInfo : mBusInfos) {
            float stationRatio = busInfo.stationRatio;

            if (stationRatio != -1) {
                // 数据准确的才绘制
                int basis = (int) Math.floor(stationRatio);
                float loss = stationRatio - basis;

                // 要绘制到的范围的宽度（无论左右）
                float measWidth = mRowWidth + ((basis % 5) * mRowWidth);
                // 要绘制到的范围的高度
                float measHeight = mRowHeight + ((basis / 5) * mRowHeight);

                float offsetWidth;
                float offsetHeight;

                if ((basis + 1) % 5 != 0) {
                    // 正常点
                    offsetWidth = (measWidth + (loss * mRowWidth));
                    offsetHeight = measHeight;
                } else {
                    // 点在一行的最后（无论左右）
                    offsetWidth = measWidth;
                    offsetHeight = loss * mRowHeight + measHeight;
                }

                if ((basis / 5) % 2 != 0) {
                    offsetWidth = getMeasuredWidth() - offsetWidth;
                }

                Bitmap bitmap = busInfo.isLuxury ? busLuxury : busOri;
                canvas.drawBitmap(bitmap, offsetWidth - (busOri.getWidth() / 2),
                        offsetHeight - (busOri.getHeight() / 2), null);

                if (mShowBusId) {
                    String busId = busInfo.busId;
                    Rect rect = new Rect();
                    paint.getTextBounds(busId, 0, busId.length(), rect);

                    canvas.drawText(busId, offsetWidth - (rect.width() / 2),
                            offsetHeight + rect.height() + mAddHeight, paint);
                }
            }
        }
    }

    /**
     * 外部调用，设置BusLine，设置后会刷新界面
     */
    public void setBusLine(BusLine busLine) {
        if (busLine.result != null) {
            this.mBusLine = addStationDistances(busLine.result);
            requestLayout();
        }
    }

    /**
     * 外部调用，设置BusInfo，设置后会刷新界面
     */
    public void setBusInfo(BusInfo busInfo) {
        if (busInfo.result != null && busInfo.result.size() != 0) {
            this.mBusInfos = addBusAttr(busInfo.result);
            invalidate();
        }
    }

    /**
     * 外部调用，设置是否显示车辆自编号
     */
    public void setShowBusId(boolean value) {
        mShowBusId = value;
    }

    /**
     * 设置每个站与第一个站之间的距离
     */
    private BusLine.Result addStationDistances(BusLine.Result busLine) {
        if (!TextUtils.isEmpty(busLine.linePoints)) {
            List<BusLine.Result.Stations> stations = busLine.stations;
            float[] floats = new float[stations.size()];

            for (int index = 0; index < stations.size(); index++) {
                if (index == 0) {
                    // 第一个站，初始距离为0
                    floats[index] = 0;
                } else {
                    BusLine.Result.Stations currentStation = stations.get(index);
                    BusLine.Result.Stations beforeStation = stations.get(index - 1);

                    // 计算当前站和第一个站之间的距离
                    floats[index] = LocationUtils.distanceBetween(beforeStation.lat, beforeStation.lng,
                            currentStation.lat, currentStation.lng) + floats[index - 1];
                }
            }

            busLine.stationDistances = floats;
        }

        return busLine;
    }

    /**
     * 添加是否为空调车，已经经过了的站点数和车辆在线路图的偏移量
     */
    private List<BusInfo.Result> addBusAttr(List<BusInfo.Result> busInfos) {
        for (BusInfo.Result busInfo : busInfos) {
            // 将世界标准坐标转换为百度坐标
            double[] doubles = LocationUtils.inter2Baidu(busInfo.lat, busInfo.lng);
            busInfo.lat = doubles[0];
            busInfo.lng = doubles[1];

            if (mBusLine != null) {
                if (mBusLine.area == 370100) {
                    // 判断是普通车还是空调车
                    busInfo.isLuxury = busInfo.busId.trim().toLowerCase().startsWith("k");
                }

                busInfo.arriveNum = addArriveNum(busInfo);
                busInfo.stationRatio = calculateRatio(busInfo);
            }
        }

        return busInfos;
    }

    /**
     * 计算已经经过了的站点数
     */
    private int addArriveNum(BusInfo.Result busInfo) {
        int arriveNum = busInfo.stationSeqNum - 1;

        if (!TextUtils.isEmpty(mBusLine.linePoints)) {
            if (busInfo.isArrvLft == null || arriveNum < 0 || arriveNum >= mBusLine.stations.size()) {
                // arriveNum不准，手动计算
                List<BusLine.Result.Stations> stations = mBusLine.stations;
                float localDistance = 0;
                int localArriveNum = 0;
                int index = 0;

                while (index < stations.size()) {
                    BusLine.Result.Stations station = stations.get(index);
                    float b2sDistance = LocationUtils.distanceBetween(busInfo.lat, busInfo.lng, station.lat, station.lng);

                    if (index == 0) {
                        localDistance = b2sDistance;
                        localArriveNum = 0;
                    } else if (b2sDistance < localDistance) {
                        localDistance = b2sDistance;
                        localArriveNum = index;
                    }

                    index++;
                }

                return localArriveNum;
            }
        } else if (arriveNum < 0 || arriveNum >= mBusLine.stations.size()) {
            // arriveNum极度不准，手动赋值0
            arriveNum = 0;
        }

        return arriveNum;
    }

    /**
     * 计算车辆在线路图的偏移量
     */
    private float calculateRatio(BusInfo.Result busInfo) {
        int arriveNum = busInfo.arriveNum;

        List<BusLine.Result.Stations> stations = mBusLine.stations;
        float[] stationDistances = mBusLine.stationDistances;
        BusLine.Result.Stations localStation;

        float b2sDistance;
        float s2sDistance;

        if (arriveNum <= 0) {
            localStation = stations.get(1);

            b2sDistance = LocationUtils.distanceBetween(busInfo.lat, busInfo.lng, localStation.lat, localStation.lng);
            s2sDistance = stationDistances[1];

            if (b2sDistance > s2sDistance) {
                // 数据不准，返回-1
                return -1;
            }
        } else if (arriveNum < stations.size() - 1) {
            // 获得还未经过的最近的下一个站
            localStation = stations.get(arriveNum + 1);

            b2sDistance = LocationUtils.distanceBetween(busInfo.lat, busInfo.lng, localStation.lat, localStation.lng);
            s2sDistance = stationDistances[arriveNum + 1] - stationDistances[arriveNum];

            if (b2sDistance > s2sDistance) {
                // arriveNum的站点还未到达，向前移动一个站
                arriveNum -= 1;
                localStation = stations.get(arriveNum);

                b2sDistance = LocationUtils.distanceBetween(busInfo.lat, busInfo.lng, localStation.lat, localStation.lng);
                s2sDistance = stationDistances[arriveNum + 1] - stationDistances[arriveNum];

                if (b2sDistance > s2sDistance) return arriveNum;
            }
        } else {
            int lastIndex = stations.size() - 1;
            // 获得最后一个站
            localStation = stations.get(lastIndex);

            b2sDistance = LocationUtils.distanceBetween(busInfo.lat, busInfo.lng, localStation.lat, localStation.lng);
            s2sDistance = stationDistances[lastIndex] - stationDistances[lastIndex - 1];

            if (b2sDistance > s2sDistance) {
                // 所有站点已经走完
                return lastIndex;
            } else {
                // 数据错误，当前经过的是倒数第二个站点。。妈的，事真多啊。。ค(TㅅT)
                arriveNum -= 1;
            }
        }

        return arriveNum + ((s2sDistance - b2sDistance) / s2sDistance);
    }
}