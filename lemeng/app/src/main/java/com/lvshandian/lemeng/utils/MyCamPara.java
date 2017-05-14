package com.lvshandian.lemeng.utils;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 程序首先对预览尺寸的list进行升序排序，因为实际中发现，有的手机提供的是升序有的是降序。
 * 通过equalRate(Size s, float rate)保证Size的长宽比率。一般而言这个比率为1.333/1.7777即通常说的4:3和16:9比率。
 * 在getPreviewSize()函数里增加判断if((s.width > th) && equalRate(s, 1.33f))，除保证比率外，还保证用户需要设置的尺寸宽度最小值。这个大家根据需要随便改。
 */
public class MyCamPara {
    private static final String tag = "yan";
    private CameraSizeComparator sizeComparator = new CameraSizeComparator();
    private static MyCamPara myCamPara = null;
    private MyCamPara(){

    }
    public static MyCamPara getInstance(){
        if(myCamPara == null){
            myCamPara = new MyCamPara();
            return myCamPara;
        }
        else{
            return myCamPara;
        }
    }

    public  Size getPreviewSize(List<Camera.Size> list, int th){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Size s:list){
            if((s.width > th) && equalRate(s, 1.778f)){
                Log.i(tag, "最终设置预览尺寸:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }
        if (i == list.size()) {
            i = list.size() - 1;//如果没找到，就选最大的size
        }
        return list.get(i);
    }
    public Size getPictureSize(List<Camera.Size> list, int th){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Size s:list){
            if((s.width > th) && equalRate(s, 1.778f)){
                Log.i(tag, "最终设置图片尺寸:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }

        return list.get(i);
    }

    public boolean equalRate(Size s, float rate){
        float r = (float)(s.width)/(float)(s.height);
        if(Math.abs(r - rate) <= 0.2)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public  class CameraSizeComparator implements Comparator<Camera.Size>{
        //按升序排列
        public int compare(Size lhs, Size rhs) {
            // TODO Auto-generated method stub
            if(lhs.width == rhs.width){
                return 0;
            }
            else if(lhs.width > rhs.width){
                return 1;
            }
            else{
                return -1;
            }
        }

    }
}