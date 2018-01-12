package com.paic.crm.inputhelper.entity;

import com.paic.crm.inputhelper.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * QQ表情的实体类
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/11.
 */

public class QQFaceEntity extends BaseFaceEntity {

    private static final String PATTERN_QQ_FACE_NAME = "\\[[\\u4e00-\\u9fa5|A-Z]{1,}\\]";
    private static final String PATTERN_QQ_FACE_SYMBOL = "/::\\)" + "|/::~" + "|/::B" + "|/::\\|" + "|/:8\\-\\)" +
            "|/::<" + "|/::\\$" + "|/::X" + "|/::Z" + "|/::'\\(" + "|/::\\-\\|" + "|/::@" + "|/::P" +
            "|/::D" + "|/::O" + "|/::\\(" + "|/::\\+" + "|/:\\-\\-b" + "|/::Q" + "|/::T" + "|/:,@P" +
            "|/:,@\\-D" + "|/::d" + "|/:,@o" + "|/::g" + "|/:\\|\\-\\)" + "|/::!" + "|/::L" + "|/::>" +
            "|/::,@" + "|/:,@f" + "|/::\\-S" + "|/:\\?" + "|/:,@x" + "|/:,@@" + "|/::8" + "|/:,@!" +
            "|/:!!!" + "|/:xx" + "|/:bye" + "|/:wipe" + "|/:dig" + "|/:handclap" + "|/:&\\-\\(" +
            "|/:B\\-\\)" + "|/:<@" + "|/:@>" + "|/::\\-O" + "|/:>\\-\\|" + "|/:P\\-\\(" + "|/::'\\|" +
            "|/:X\\-\\)" + "|/::\\*" + "|/:@x" + "|/:8\\*" + "|/:pd" + "|/:<W>" + "|/:beer" + "|/:basketb" +
            "|/:oo" + "|/:coffee" + "|/:eat" + "|/:pig" + "|/:rose" + "|/:fade" + "|/:showlove" + "|/:heart" +
            "|/:break" + "|/:cake" + "|/:li" + "|/:bome" + "|/:kn" + "|/:footb" + "|/:ladybug" + "|/:shit" +
            "|/:moon" + "|/:sun" + "|/:gift" + "|/:hug" + "|/:strong" + "|/:weak" + "|/:share" + "|/:v" +
            "|/:@\\)" + "|/:jj" + "|/:@@" + "|/:bad" + "|/:ivu" + "|/:no" + "|/:ok" + "|/:love" + "|/:<L>" +
            "|/:jump" + "|/:shake" + "|/:<O>" + "|/:circle" + "|/:kotow" + "|/:turn" + "|/:skip" + "|/:oY" +
            "|/:#\\-0" + "|/:hiphot" + "|/:kiss" + "|/:<&" + "|/:&>";

    private static final String[] faceNames = new String[]{
            "[微笑]", "[撇嘴]", "[色]", "[发呆]", "[得意]",
            "[流泪]", "[害羞]", "[闭嘴]", "[睡]", "[大哭]",
            "[尴尬]", "[发怒]", "[调皮]", "[呲牙]", "[惊讶]",
            "[难过]", "[酷]", "[冷汗]", "[抓狂]", "[吐]",
            "[偷笑]", "[愉快]", "[白眼]", "[傲慢]", "[饥饿]",
            "[困]", "[惊恐]", "[流汗]", "[憨笑]", "[悠闲]",
            "[奋斗]", "[咒骂]", "[疑问]", "[嘘]", "[晕]",
            "[疯了]", "[衰]", "[骷髅]", "[敲打]", "[再见]",
            "[擦汗]", "[抠鼻]", "[鼓掌]", "[糗大了]", "[坏笑]",
            "[左哼哼]", "[右哼哼]", "[哈欠]", "[鄙视]", "[委屈]",
            "[快哭了]", "[阴险]", "[亲亲]", "[吓]", "[可怜]",
            "[菜刀]", "[西瓜]", "[啤酒]", "[篮球]", "[乒乓]",
            "[咖啡]", "[饭]", "[猪头]", "[玫瑰]", "[凋谢]",
            "[嘴唇]", "[爱心]", "[心碎]", "[蛋糕]", "[闪电]",
            "[炸弹]", "[刀]", "[足球]", "[瓢虫]", "[便便]",
            "[月亮]", "[太阳]", "[礼物]", "[拥抱]", "[强]",
            "[弱]", "[握手]", "[胜利]", "[抱拳]", "[勾引]",
            "[拳头]", "[差劲]", "[爱你]", "[NO]", "[OK]",
            "[爱情]", "[飞吻]", "[跳跳]", "[发抖]", "[怄火]",
            "[转圈]", "[磕头]", "[回头]", "[跳绳]", "[投降]",
            "[激动]", "[乱舞]", "[献吻]", "[左太极]", "[右太极]"};

    public static final int[] faceIds = new int[]{
            R.drawable.face1, R.drawable.face2, R.drawable.face3, R.drawable.face4, R.drawable.face5,
            R.drawable.face6, R.drawable.face7, R.drawable.face8, R.drawable.face9, R.drawable.face10,
            R.drawable.face11, R.drawable.face12, R.drawable.face13, R.drawable.face14, R.drawable.face15,
            R.drawable.face16, R.drawable.face17, R.drawable.face18, R.drawable.face19, R.drawable.face20,
            R.drawable.face21, R.drawable.face22, R.drawable.face23, R.drawable.face24, R.drawable.face25,
            R.drawable.face26, R.drawable.face27, R.drawable.face28, R.drawable.face29, R.drawable.face30,
            R.drawable.face31, R.drawable.face32, R.drawable.face33, R.drawable.face34, R.drawable.face35,
            R.drawable.face36, R.drawable.face37, R.drawable.face38, R.drawable.face39, R.drawable.face40,
            R.drawable.face41, R.drawable.face42, R.drawable.face43, R.drawable.face44, R.drawable.face45,
            R.drawable.face46, R.drawable.face47, R.drawable.face48, R.drawable.face49, R.drawable.face50,
            R.drawable.face51, R.drawable.face52, R.drawable.face53, R.drawable.face54, R.drawable.face55,
            R.drawable.face56, R.drawable.face57, R.drawable.face58, R.drawable.face59, R.drawable.face60,
            R.drawable.face61, R.drawable.face62, R.drawable.face63, R.drawable.face64, R.drawable.face65,
            R.drawable.face66, R.drawable.face67, R.drawable.face68, R.drawable.face69, R.drawable.face70,
            R.drawable.face71, R.drawable.face72, R.drawable.face73, R.drawable.face74, R.drawable.face75,
            R.drawable.face76, R.drawable.face77, R.drawable.face78, R.drawable.face79, R.drawable.face80,
            R.drawable.face81, R.drawable.face82, R.drawable.face83, R.drawable.face84, R.drawable.face85,
            R.drawable.face86, R.drawable.face87, R.drawable.face88, R.drawable.face89, R.drawable.face90,
            R.drawable.face91, R.drawable.face92, R.drawable.face93, R.drawable.face94, R.drawable.face95,
            R.drawable.face96, R.drawable.face97, R.drawable.face98, R.drawable.face99, R.drawable.face100,
            R.drawable.face101, R.drawable.face102, R.drawable.face103, R.drawable.face104, R.drawable.face105
    };

    public static String[] faceSymbols = new String[]{
            "/::)", "/::~", "/::B", "/::|", "/:8-)",
            "/::<", "/::$", "/::X", "/::Z", "/::'(",
            "/::-|", "/::@", "/::P", "/::D", "/::O",
            "/::(", "/::+", "/:--b", "/::Q", "/::T",
            "/:,@P", "/:,@-D", "/::d", "/:,@o", "/::g",
            "/:|-)", "/::!", "/::L", "/::>", "/::,@",
            "/:,@f", "/::-S", "/:?", "/:,@x", "/:,@@",
            "/::8", "/:,@!", "/:!!!", "/:xx", "/:bye",
            "/:wipe", "/:dig", "/:handclap", "/:&-(", "/:B-)",
            "/:<@", "/:@>", "/::-O", "/:>-|", "/:P-(",
            "/::'|", "/:X-)", "/::*", "/:@x", "/:8*",
            "/:pd", "/:<W>", "/:beer", "/:basketb", "/:oo",
            "/:coffee", "/:eat", "/:pig", "/:rose", "/:fade",
            "/:showlove", "/:heart", "/:break", "/:cake", "/:li",
            "/:bome", "/:kn", "/:footb", "/:ladybug", "/:shit",
            "/:moon", "/:sun", "/:gift", "/:hug", "/:strong",
            "/:weak", "/:share", "/:v", "/:@)", "/:jj",
            "/:@@", "/:bad", "/:ivu", "/:no", "/:ok",
            "/:love", "/:<L>", "/:jump", "/:shake", "/:<O>",
            "/:circle", "/:kotow", "/:turn", "/:skip", "/:oY",
            "/:#-0", "/:hiphot", "/:kiss", "/:<&", "/:&>"
    };

    @Override
    public List<String> getFaceImgNames() {
        List<String> list = new ArrayList<>();
        for (String name : faceNames) {
            list.add(name);
        }
        return list;
    }

    @Override
    public List<Integer> getFaceImgIds() {
        List<Integer> list = new ArrayList<>();
        for (Integer id : faceIds) {
            list.add(id);
        }
        return list;
    }

    @Override
    public List<String> getFaceSymbols() {
        List<String> list = new ArrayList<>();
        for (String symbol : faceSymbols) {
            list.add(symbol);
        }
        return list;
    }

    @Override
    public Pattern getNamePattern() {
        return Pattern.compile(PATTERN_QQ_FACE_NAME);
    }

    @Override
    public Pattern getSymbolPattern() {
        return Pattern.compile(PATTERN_QQ_FACE_SYMBOL);
    }
}
