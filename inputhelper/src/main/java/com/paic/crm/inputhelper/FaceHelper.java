package com.paic.crm.inputhelper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.paic.crm.inputhelper.adapter.FacesAdapter;
import com.paic.crm.inputhelper.adapter.ViewPagerAdapter;
import com.paic.crm.inputhelper.entity.BaseFaceEntity;
import com.paic.crm.inputhelper.entity.MsgFaceModle;
import com.paic.crm.inputhelper.util.FaceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ex-zhangyuelei001 on 2017/12/13.
 */

public class FaceHelper implements ViewPager.OnPageChangeListener {
    /**
     * 每页最大表情数
     */
    private static final int MAX_PAGE_SIZE = 23;
    /**
     * 需要填充的ViewPager
     */
    private ViewPager viewPager;
    private Context context;
    /**
     * 储存表情的集合
     */
    private List<BaseFaceEntity> faceEntities = new ArrayList<>();

    /**
     * 存放MsgFaceModle的集合
     */
    private List<MsgFaceModle> modleList = new ArrayList<>();
    /**
     * 储存表情页面的的集合
     */
    private List<View> viewList = new ArrayList<>();
    public ViewGroup viewGroup;
    private ViewPagerPoint pagerPoint;
    private OnFaceOprateListener onFaceOprateListener;

    private FaceHelper(){}

    public static FaceHelper newInstance() {
        return FaceHelperHolder.faceHelper;
    }

    static class FaceHelperHolder{
        static FaceHelper faceHelper = new FaceHelper();
    }


    /**
     * 添加ViewPager
     *
     * @param viewPager
     */
    public FaceHelper addViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        return this;
    }

    public FaceHelper addViewPagerCursor(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
        return this;
    }

    public List<BaseFaceEntity> getFaceEntities() {
        return faceEntities;
    }

    /**
     * 添加需要使用的表情类
     *
     * @param context
     * @param tClasses
     * @return
     */
    public FaceHelper addFace(Context context, ArrayList<Class<?extends BaseFaceEntity>> tClasses) {
        this.context = context;
        try {
            for (Class<? extends BaseFaceEntity> tClass:tClasses ){
                BaseFaceEntity baseFaceEntity = tClass.newInstance();
                faceEntities.add(baseFaceEntity);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public FaceHelper setOnFaceOprateListener(OnFaceOprateListener onFaceOprateListener) {
        this.onFaceOprateListener = onFaceOprateListener;
        return this;
    }

    public FaceHelper init() {
        for (BaseFaceEntity faceEntity : faceEntities) {
            //初始化viewpager的数据
            initData(faceEntity);
        }
        FaceUtil.getInstance().setFaceEntities(faceEntities);
        //添加删除按钮
        addDelBtn();
        //初始化viewpager中的单个页面
        initViews(modleList);

        //初始化游标
        pagerPoint = new ViewPagerPoint();
        pagerPoint.initPoint(context, getPageNum());

        //给viewpager填充数据
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        return this;
    }


    /**
     * 初始化表情数据
     *
     * @param faceEntity
     * @return
     */
    private void initData(BaseFaceEntity faceEntity) {
        //存放表情和删除图标的list
        for (int i = 0; i < faceEntity.getFaceImgIds().size(); i++) {
            MsgFaceModle faceModle = new MsgFaceModle();
            faceModle.setId(faceEntity.getFaceImgIds().get(i));
            faceModle.setCharacter(faceEntity.getFaceImgNames().get(i));
            modleList.add(faceModle);
        }
    }

    private void addDelBtn() {
        //添加删除图标
        for (int i = getPageNum(); i > 0; i--) {
            //计算删除图标的位置
            int delPosition = i * MAX_PAGE_SIZE;

            //如果是最后一页，设置空白表情
            if (delPosition > modleList.size()) {
                for (int j = modleList.size(); j < delPosition; j++) {
                    MsgFaceModle modle = new MsgFaceModle();
                    modleList.add(j, modle);
                }
            }

            MsgFaceModle faceModle = new MsgFaceModle();
            faceModle.setId(delPosition);
            faceModle.setId(R.drawable.crm_sdk_face_delete_select);
            modleList.add(delPosition, faceModle);
        }
    }


    /**
     * 计算当前有多少页
     *
     * @return
     */
    private int getPageNum() {
        return modleList.size() / (MAX_PAGE_SIZE + 1) + 1;
    }


    private void initViews(List<MsgFaceModle> dataList) {
        for (int i = 0; i < getPageNum() - 1; i++) {
            int from = i * (MAX_PAGE_SIZE + 1);
            int to = (i + 1) * (MAX_PAGE_SIZE + 1);
            List<MsgFaceModle> list = dataList.subList(from, to);
            ViewPagerItemView pagerItemView = new ViewPagerItemView(context, list);
            GridView gridView = pagerItemView.initView();
            viewList.add(gridView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        pagerPoint.drawPoint(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class ViewPagerItemView implements AdapterView.OnItemClickListener {
        private Context itemContext;
        private List<MsgFaceModle> list;

        /**
         * 初始化GridView控件
         *
         * @return
         */
        ViewPagerItemView(Context context, List<MsgFaceModle> list) {
            this.itemContext = context;
            this.list = list;
        }

        GridView initView() {
            FacesAdapter facesAdapter = new FacesAdapter(itemContext, list);
            GridView gridView = new GridView(itemContext);
            int commonSpacing = (int) itemContext.getResources().getDimension(R.dimen.common_height_5);
            gridView.setHorizontalSpacing(commonSpacing);
            gridView.setVerticalSpacing(commonSpacing);
            gridView.setNumColumns(6);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(commonSpacing, commonSpacing, commonSpacing, commonSpacing);
            gridView.setLayoutParams(layoutParams);
            gridView.setAdapter(facesAdapter);
            gridView.setOnItemClickListener(this);
            return gridView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (null == onFaceOprateListener) {
                return;
            }

            if (position == MAX_PAGE_SIZE) {
                onFaceOprateListener.onFaceDeleted();
            } else {
                MsgFaceModle faceModle = list.get(position);
                SpannableString span = FaceUtil.getInstance().getExpression(context, new SpannableString(faceModle.getCharacter()));
                onFaceOprateListener.onFaceSelected(span);
            }
        }
    }

    /**
     * Created by ex-zhangyuelei001 on 2017/12/14.
     */

    class ViewPagerPoint {
        List<ImageView> pointViews = new ArrayList<ImageView>();

        /**
         * 绘制游标背景
         */
        void drawPoint(int index) {
            for (int i = 0; i < pointViews.size(); i++) {
                int resId = index == i ? R.drawable.icon_jw_face_index_prs : R.drawable.icon_jw_face_index_nor;
                pointViews.get(i).setBackgroundResource(resId);
            }
        }

        /**
         * 初始化游标
         */
        void initPoint(Context context, int pageNum) {


            ImageView imageView;
            for (int i = 0; i < pageNum - 1; i++) {
                imageView = new ImageView(context);
                imageView.setBackgroundResource(R.drawable.icon_jw_face_index_nor);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                layoutParams.leftMargin = 10;
                layoutParams.rightMargin = 10;
                layoutParams.width = 8;
                layoutParams.height = 8;
                viewGroup.addView(imageView, layoutParams);
                //默认设置第一个圆点为黄色
                if (i == 0) {
                    imageView.setBackgroundResource(R.drawable.icon_jw_face_index_prs);
                }
                pointViews.add(imageView);

            }
        }
    }

    /**
     * Created by ex-zhangyuelei001 on 2017/12/20.
     */

    public interface OnFaceOprateListener {
        void onFaceSelected(SpannableString spanEmojiStr);

        void onFaceDeleted();
    }
}
