package com.paic.crm.sdk.ucmcore.utils.faceUtils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.paic.crm.sdk.ucmcore.R;

import java.util.ArrayList;
import java.util.List;

public class MsgQQFaceUtils {
//    public static String[] faceImgNames = new String[]{
//            "[微笑]", "[撇嘴]", "[色]", "[发呆]", "[得意]", "[流泪]", "[害羞]", "[闭嘴]", "[睡]", "[大哭]"
//            , "[尴尬]", "[发怒]", "[调皮]", "[呲牙]", "[惊讶]", "[难过]", "[酷]", "[冷汗]", "[抓狂]", "[吐]"
//            , "[偷笑]", "[愉快]", "[白眼]", "[傲慢]", "[饥饿]", "[困]", "[惊恐]", "[流汗]", "[憨笑]", "[悠闲]"
//            , "[奋斗]", "[咒骂]", "[疑问]", "[嘘]", "[晕]", "[疯了]", "[衰]", "[骷髅]", "[敲打]", "[再见]"
//            , "[擦汗]", "[抠鼻]", "[鼓掌]", "[糗大了]", "[坏笑]", "[左哼哼]", "[右哼哼]", "[哈欠]", "[鄙视]", "[委屈]"
//            , "[快哭了]", "[阴险]", "[亲亲]", "[吓]", "[可怜]", "[菜刀]", "[西瓜]", "[啤酒]", "[篮球]", "[乒乓]"
//            , "[咖啡]", "[饭]", "[猪头]", "[玫瑰]", "[凋谢]", "[嘴唇]", "[爱心]", "[心碎]", "[蛋糕]", "[闪电]"
//            , "[炸弹]", "[刀]", "[足球]", "[瓢虫]", "[便便]", "[月亮]", "[太阳]", "[礼物]", "[拥抱]", "[强]"
//            , "[弱]", "[握手]", "[胜利]", "[抱拳]", "[勾引]", "[拳头]", "[差劲]", "[爱你]", "[NO]", "[OK]"
//            , "[爱情]", "[飞吻]", "[跳跳]", "[发抖]", "[怄火]", "[转圈]", "[磕头]", "[回头]", "[跳绳]", "[投降]"
//            , "[激动]", "[乱舞]", "[献吻]", "[左太极]", "[右太极]"};
//
//    public static int[] faceImgs = new int[]{
//            R.drawable.face1, R.drawable.face2, R.drawable.face3, R.drawable.face4, R.drawable.face5, R.drawable.face6, R.drawable.face7, R.drawable.face8, R.drawable.face9, R.drawable.face10
//            , R.drawable.face11, R.drawable.face12, R.drawable.face13, R.drawable.face14, R.drawable.face15, R.drawable.face16, R.drawable.face17, R.drawable.face18, R.drawable.face19, R.drawable.face20
//            , R.drawable.face21, R.drawable.face22, R.drawable.face23, R.drawable.face24, R.drawable.face25, R.drawable.face26, R.drawable.face27, R.drawable.face28, R.drawable.face29, R.drawable.face30
//            , R.drawable.face31, R.drawable.face32, R.drawable.face33, R.drawable.face34, R.drawable.face35, R.drawable.face36, R.drawable.face37, R.drawable.face38, R.drawable.face39, R.drawable.face40
//            , R.drawable.face41, R.drawable.face42, R.drawable.face43, R.drawable.face44, R.drawable.face45, R.drawable.face46, R.drawable.face47, R.drawable.face48, R.drawable.face49, R.drawable.face50
//            , R.drawable.face51, R.drawable.face52, R.drawable.face53, R.drawable.face54, R.drawable.face55, R.drawable.face56, R.drawable.face57, R.drawable.face58, R.drawable.face59, R.drawable.face60
//            , R.drawable.face61, R.drawable.face62, R.drawable.face63, R.drawable.face64, R.drawable.face65, R.drawable.face66, R.drawable.face67, R.drawable.face68, R.drawable.face69, R.drawable.face70
//            , R.drawable.face71, R.drawable.face72, R.drawable.face73, R.drawable.face74, R.drawable.face75, R.drawable.face76, R.drawable.face77, R.drawable.face78, R.drawable.face79, R.drawable.face80
//            , R.drawable.face81, R.drawable.face82, R.drawable.face83, R.drawable.face84, R.drawable.face85, R.drawable.face86, R.drawable.face87, R.drawable.face88, R.drawable.face89, R.drawable.face90
//            , R.drawable.face91, R.drawable.face92, R.drawable.face93, R.drawable.face94, R.drawable.face95, R.drawable.face96, R.drawable.face97, R.drawable.face98, R.drawable.face99, R.drawable.face100
//            , R.drawable.face101, R.drawable.face102, R.drawable.face103, R.drawable.face104, R.drawable.face105};

    public static List<String>faceImageNames = new ArrayList<>();
    static {
        faceImageNames.add("[微笑]");
        faceImageNames.add("[撇嘴]");
        faceImageNames.add("[色]");
        faceImageNames.add("[发呆]");
        faceImageNames.add("[得意]");
        faceImageNames.add("[流泪]");
        faceImageNames.add("[害羞]");
        faceImageNames.add("[闭嘴]");
        faceImageNames.add("[睡]");
        faceImageNames.add("[大哭]");
        faceImageNames.add("[尴尬]");
        faceImageNames.add("[发怒]");
        faceImageNames.add("[调皮]");
        faceImageNames.add("[呲牙]");
        faceImageNames.add("[惊讶]");
        faceImageNames.add("[难过]");
        faceImageNames.add("[酷]");
        faceImageNames.add("[冷汗]");
        faceImageNames.add("[抓狂]");
        faceImageNames.add("[吐]");
        faceImageNames.add("[偷笑]");
        faceImageNames.add("[愉快]");
        faceImageNames.add("[白眼]");
        faceImageNames.add("[傲慢]");
        faceImageNames.add("[饥饿]");
        faceImageNames.add("[困]");
        faceImageNames.add("[惊恐]");
        faceImageNames.add("[流汗]");
        faceImageNames.add("[憨笑]");
        faceImageNames.add("[悠闲]");
        faceImageNames.add("[奋斗]");
        faceImageNames.add("[咒骂]");
        faceImageNames.add("[疑问]");
        faceImageNames.add("[嘘]");
        faceImageNames.add("[晕]");
        faceImageNames.add("[疯了]");
        faceImageNames.add("[衰]");
        faceImageNames.add("[骷髅]");
        faceImageNames.add("[敲打]");
        faceImageNames.add("[再见]");
        faceImageNames.add("[擦汗]");
        faceImageNames.add("[抠鼻]");
        faceImageNames.add("[鼓掌]");
        faceImageNames.add("[糗大了]");
        faceImageNames.add("[坏笑]");
        faceImageNames.add("[左哼哼]");
        faceImageNames.add("[右哼哼]");
        faceImageNames.add("[哈欠]");
        faceImageNames.add("[鄙视]");
        faceImageNames.add("[委屈]");
        faceImageNames.add("[快哭了]");
        faceImageNames.add("[阴险]");
        faceImageNames.add("[亲亲]");
        faceImageNames.add("[吓]");
        faceImageNames.add("[可怜]");
        faceImageNames.add("[菜刀]");
        faceImageNames.add("[西瓜]");
        faceImageNames.add("[啤酒]");
        faceImageNames.add("[篮球]");
        faceImageNames.add("[乒乓]");
        faceImageNames.add("[咖啡]");
        faceImageNames.add("[饭]");
        faceImageNames.add("[猪头]");
        faceImageNames.add("[玫瑰]");
        faceImageNames.add("[凋谢]");
        faceImageNames.add("[嘴唇]");
        faceImageNames.add("[爱心]");
        faceImageNames.add("[心碎]");
        faceImageNames.add("[蛋糕]");
        faceImageNames.add("[闪电]");
        faceImageNames.add("[炸弹]");
        faceImageNames.add("[刀]");
        faceImageNames.add("[足球]");
        faceImageNames.add("[瓢虫]");
        faceImageNames.add("[便便]");
        faceImageNames.add("[月亮]");
        faceImageNames.add("[太阳]");
        faceImageNames.add("[礼物]");
        faceImageNames.add("[拥抱]");
        faceImageNames.add("[强]");
        faceImageNames.add("[弱]");
        faceImageNames.add("[握手]");
        faceImageNames.add("[胜利]");
        faceImageNames.add("[抱拳]");
        faceImageNames.add("[勾引]");
        faceImageNames.add("[拳头]");
        faceImageNames.add("[差劲]");
        faceImageNames.add("[爱你]");
        faceImageNames.add("[NO]");
        faceImageNames.add("[OK]");
        faceImageNames.add("[爱情]");
        faceImageNames.add("[飞吻]");
        faceImageNames.add("[跳跳]");
        faceImageNames.add("[发抖]");
        faceImageNames.add("[怄火]");
        faceImageNames.add("[转圈]");
        faceImageNames.add("[磕头]");
        faceImageNames.add("[回头]");
        faceImageNames.add("[跳绳]");
        faceImageNames.add("[投降]");
        faceImageNames.add("[激动]");
        faceImageNames.add("[乱舞]");
        faceImageNames.add("[献吻]");
        faceImageNames.add("[左太极]");
        faceImageNames.add("[右太极]");
    }

    public static List<Integer> faceImageIds = new ArrayList<>();
    static {
        faceImageIds.add(R.drawable.face1);
        faceImageIds.add(R.drawable.face2);
        faceImageIds.add(R.drawable.face3);
        faceImageIds.add(R.drawable.face4);
        faceImageIds.add(R.drawable.face5);
        faceImageIds.add(R.drawable.face6);
        faceImageIds.add(R.drawable.face7);
        faceImageIds.add(R.drawable.face8);
        faceImageIds.add(R.drawable.face9);
        faceImageIds.add(R.drawable.face10);

        faceImageIds.add(R.drawable.face11);
        faceImageIds.add(R.drawable.face12);
        faceImageIds.add(R.drawable.face13);
        faceImageIds.add(R.drawable.face14);
        faceImageIds.add(R.drawable.face15);
        faceImageIds.add(R.drawable.face16);
        faceImageIds.add(R.drawable.face17);
        faceImageIds.add(R.drawable.face18);
        faceImageIds.add(R.drawable.face19);
        faceImageIds.add(R.drawable.face20);

        faceImageIds.add(R.drawable.face21);
        faceImageIds.add(R.drawable.face22);
        faceImageIds.add(R.drawable.face23);
        faceImageIds.add(R.drawable.face24);
        faceImageIds.add(R.drawable.face25);
        faceImageIds.add(R.drawable.face26);
        faceImageIds.add(R.drawable.face27);
        faceImageIds.add(R.drawable.face28);
        faceImageIds.add(R.drawable.face29);
        faceImageIds.add(R.drawable.face30);

        faceImageIds.add(R.drawable.face31);
        faceImageIds.add(R.drawable.face32);
        faceImageIds.add(R.drawable.face33);
        faceImageIds.add(R.drawable.face34);
        faceImageIds.add(R.drawable.face35);
        faceImageIds.add(R.drawable.face36);
        faceImageIds.add(R.drawable.face37);
        faceImageIds.add(R.drawable.face38);
        faceImageIds.add(R.drawable.face39);
        faceImageIds.add(R.drawable.face40);

        faceImageIds.add(R.drawable.face41);
        faceImageIds.add(R.drawable.face42);
        faceImageIds.add(R.drawable.face43);
        faceImageIds.add(R.drawable.face44);
        faceImageIds.add(R.drawable.face45);
        faceImageIds.add(R.drawable.face46);
        faceImageIds.add(R.drawable.face47);
        faceImageIds.add(R.drawable.face48);
        faceImageIds.add(R.drawable.face49);
        faceImageIds.add(R.drawable.face50);

        faceImageIds.add(R.drawable.face51);
        faceImageIds.add(R.drawable.face52);
        faceImageIds.add(R.drawable.face53);
        faceImageIds.add(R.drawable.face54);
        faceImageIds.add(R.drawable.face55);
        faceImageIds.add(R.drawable.face56);
        faceImageIds.add(R.drawable.face57);
        faceImageIds.add(R.drawable.face58);
        faceImageIds.add(R.drawable.face59);
        faceImageIds.add(R.drawable.face60);
        faceImageIds.add( R.drawable.face61);
        faceImageIds.add( R.drawable.face62);
        faceImageIds.add( R.drawable.face63);
        faceImageIds.add( R.drawable.face64);
        faceImageIds.add( R.drawable.face65);
        faceImageIds.add( R.drawable.face66);
        faceImageIds.add( R.drawable.face67);
        faceImageIds.add( R.drawable.face68);
        faceImageIds.add( R.drawable.face69);
        faceImageIds.add( R.drawable.face70);
        faceImageIds.add( R.drawable.face71);
        faceImageIds.add( R.drawable.face72 );
        faceImageIds.add( R.drawable.face73);
        faceImageIds.add( R.drawable.face74 );
        faceImageIds.add( R.drawable.face75 );
        faceImageIds.add( R.drawable.face76 );
        faceImageIds.add( R.drawable.face77 );
        faceImageIds.add( R.drawable.face78 );
        faceImageIds.add( R.drawable.face79 );
        faceImageIds.add( R.drawable.face80);
        faceImageIds.add( R.drawable.face81 );
        faceImageIds.add( R.drawable.face82);
        faceImageIds.add( R.drawable.face83);
        faceImageIds.add( R.drawable.face84);
        faceImageIds.add( R.drawable.face85 );
        faceImageIds.add( R.drawable.face86 );
        faceImageIds.add( R.drawable.face87);
        faceImageIds.add( R.drawable.face88);
        faceImageIds.add( R.drawable.face89 );
        faceImageIds.add( R.drawable.face90);
        faceImageIds.add( R.drawable.face91 );
        faceImageIds.add( R.drawable.face92 );
        faceImageIds.add( R.drawable.face93);
        faceImageIds.add( R.drawable.face94 );
        faceImageIds.add( R.drawable.face95 );
        faceImageIds.add( R.drawable.face96 );
        faceImageIds.add( R.drawable.face97 );
        faceImageIds.add( R.drawable.face98 );
        faceImageIds.add( R.drawable.face99 );
        faceImageIds.add( R.drawable.face100);
        faceImageIds.add( R.drawable.face101);
        faceImageIds.add( R.drawable.face102);
        faceImageIds.add( R.drawable.face103);
        faceImageIds.add( R.drawable.face104);
        faceImageIds.add( R.drawable.face105);
    }

    public static List<String> faceSymbol = new ArrayList<>();
    static {
        faceSymbol.add("/::)");
        faceSymbol.add("/::~");
        faceSymbol.add("/::B");
        faceSymbol.add("/::|");
        faceSymbol.add("/:8-)");
        faceSymbol.add("/::<");
        faceSymbol.add("/::$");
        faceSymbol.add("/::X");
        faceSymbol.add("/::Z");
        faceSymbol.add("/::'(");
        faceSymbol.add("/::-|");
        faceSymbol.add("/::@");
        faceSymbol.add("/::P");
        faceSymbol.add("/::D");
        faceSymbol.add("/::O");
        faceSymbol.add("/::(");
        faceSymbol.add("/::+");
        faceSymbol.add("/:--b");
        faceSymbol.add("/::Q");
        faceSymbol.add("/::T");
        faceSymbol.add("/:,@P");
        faceSymbol.add("/:,@-D");
        faceSymbol.add("/::d");
        faceSymbol.add("/:,@o");
        faceSymbol.add("/::g");
        faceSymbol.add("/:|-)");
        faceSymbol.add("/::!");
        faceSymbol.add("/::L");
        faceSymbol.add("/::>");
        faceSymbol.add("/::,@");
        faceSymbol.add("/:,@f");
        faceSymbol.add("/::-S");
        faceSymbol.add("/:?");
        faceSymbol.add("/:,@x");
        faceSymbol.add("/:,@@");
        faceSymbol.add("/::8");
        faceSymbol.add("/:,@!");
        faceSymbol.add("/:!!!");
        faceSymbol.add("/:xx");
        faceSymbol.add("/:bye");
        faceSymbol.add("/:wipe");
        faceSymbol.add("/:dig");
        faceSymbol.add("/:handclap");
        faceSymbol.add("/:&-(");
        faceSymbol.add("/:B-)");
        faceSymbol.add("/:<@");
        faceSymbol.add("/:@>");
        faceSymbol.add("/::-O");
        faceSymbol.add("/:>-|");
        faceSymbol.add("/:P-(");
        faceSymbol.add("/::'|");
        faceSymbol.add("/:X-)");
        faceSymbol.add("/::*");
        faceSymbol.add("/:@x");
        faceSymbol.add("/:8*");
        faceSymbol.add("/:pd");
        faceSymbol.add("/:<W>");
        faceSymbol.add("/:beer");
        faceSymbol.add("/:basketb");
        faceSymbol.add("/:oo");
        faceSymbol.add("/:coffee");
        faceSymbol.add("/:eat");
        faceSymbol.add("/:pig");
        faceSymbol.add("/:rose");
        faceSymbol.add("/:fade");
        faceSymbol.add("/:showlove");
        faceSymbol.add("/:heart");
        faceSymbol.add("/:break");
        faceSymbol.add("/:cake");
        faceSymbol.add("/:li");
        faceSymbol.add("/:bome");
        faceSymbol.add("/:kn");
        faceSymbol.add("/:footb");
        faceSymbol.add("/:ladybug");
        faceSymbol.add("/:shit");
        faceSymbol.add("/:moon");
        faceSymbol.add("/:sun");
        faceSymbol.add("/:gift");
        faceSymbol.add("/:hug");
        faceSymbol.add("/:strong");
        faceSymbol.add("/:weak");
        faceSymbol.add("/:share");
        faceSymbol.add("/:v");
        faceSymbol.add("/:@)");
        faceSymbol.add("/:jj");
        faceSymbol.add("/:@@");
        faceSymbol.add("/:bad");
        faceSymbol.add("/:ivu");
        faceSymbol.add("/:no");
        faceSymbol.add("/:ok");
        faceSymbol.add("/:love");
        faceSymbol.add("/:<L>");
        faceSymbol.add("/:jump");
        faceSymbol.add("/:shake");
        faceSymbol.add("/:<O>");
        faceSymbol.add("/:circle");
        faceSymbol.add("/:kotow");
        faceSymbol.add("/:turn");
        faceSymbol.add("/:skip");
        faceSymbol.add("/:oY");
        faceSymbol.add("/:#-0");
        faceSymbol.add("/:hiphot");
        faceSymbol.add("/:kiss");
        faceSymbol.add("/:<&");
        faceSymbol.add("/:&>");
    }
    //TODO 预先加载了bitmap，是以空间换时间的写法，可能会有内存溢出问题
    public static SparseArray<Bitmap> bitmaps = new SparseArray<>();

    public static void initExpression(Context context) {
        for (int i = 0 ;i < faceImageIds.size();i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), faceImageIds.get(i));
            bitmaps.put(faceImageIds.get(i),bitmap);
        }
    }
}
