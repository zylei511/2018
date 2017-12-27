package com.example.zylei_library.uihelper.entity;

import com.example.zylei_library.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * emoji表情的实体类
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/11.
 */

public class EmojiFaceEntity extends BaseFaceEntity {


    private static final String PATTERN_EMOJI_FACE_NAME = "[\\ue000-\\uefff]";

    private static final String[] faceNames = new String[]{"[1f60a]", "[1f60c]", "[1f60f]", "[1f601]", "[1f604]",
            "[1f609]", "[1f612]", "[1f614]", "[1f616]", "[1f618]", "[1f621]", "[1f628]", "[1f630]", "[1f631]",
            "[1f633]", "[1f637]", "[1f603]", "[1f61e]", "[1f620]", "[1f61c]", "[1f60d]", "[1f613]", "[1f61d]",
            "[1f62d]", "[1f602]", "[1f622]", "[1f61a]", "[1f623]", "[1f632]", "[1f62a]", "[263a]", "[1f47f]",
            "[1f4aa]", "[1f44a]", "[1f44d]", "[1f44e]", "[1f44f]", "[1f64f]", "[1f446]", "[1f447]", "[261d]", "[270c]",
            "[1f448]", "[1f449]", "[1f44c]", "[270b]", "[270a]", "[1f440]", "[1f443]", "[1f444]", "[1f442]", "[1f35a]",
            "[1f35d]", "[1f35c]", "[1f35e]", "[1f35f]", "[1f359]", "[1f363]", "[1f382]", "[1f367]", "[1f37a]",
            "[1f366]", "[1f34e]", "[1f34a]", "[1f353]", "[1f349]", "[1f354]", "[1f37b]", "[1f48a]", "[1f378]",
            "[1f373]", "[2615]", "[1f6ac]", "[1f384]", "[1f389]", "[1f380]", "[1f388]", "[1f451]", "[1f494]",
            "[1f334]", "[1f49d]", "[1f339]", "[1f31f]", "[2728]", "[1f48d]", "[1f462]", "[1f3c0]", "[1f3ca]",
            "[1f4a3]", "[1f4a6]", "[1f4a8]", "[1f4a4]", "[1f4a2]", "[1f004]", "[1f469]", "[1f468]", "[1f467]",
            "[1f466]", "[1f437]", "[1f435]", "[1f419]", "[1f42e]", "[1f414]", "[1f438]", "[1f424]", "[1f428]",
            "[1f41b]", "[1f420]", "[1f436]", "[1f42f]", "[1f3b5]", "[1f3b8]", "[1f3be]", "[1f484]", "[1f40d]",
            "[1f42c]", "[1f42d]", "[1f427]", "[1f433]", "[1f457]", "[1f452]", "[1f455]", "[1f459]", "[2614]", "[2601]",
            "[2600]", "[26a1]", "[1f319]", "[2b55]", "[274c]", "[1f463]", "[1f525]", "[1f691]", "[1f692]", "[1f693]",
            "[00a9]", "[00ae]", "[1f493]", "[1f47b]", "[1f480]", "[303d]", "[1f489]", "[1f460]", "[1f3e0]", "[1f3e5]",
            "[1f3e6]", "[1f3ea]", "[1f3e8]", "[1f3e7]", "[1f4a9]", "[1f4b0]", "[1f6b9]", "[1f6ba]", "[1f6bd]",
            "[1f6c0]", "[1f41a]", "[1f45c]", "[1f45f]", "[1f47c]", "[1f197]", "[1f340]", "[1f683]", "[1f684]",
            "[1f697]", "[26ea]", "[2122]", "[2708]", "[1f47e]", "[2755]", "[1f52b]", "[26a0]", "[1f488]", "[1f4bb]",
            "[1f4f1]", "[2668]", "[1f4f7]", "[1f4de]", "[1f3c6]", "[1f3b0]", "[1f40e]", "[1f6a4]", "[1f6b2]",
            "[1f6a7]", "[1f3a5]", "[1f4e0]", "[1f6a5]", "[1f302]", "[1f512]", "[26c4]", "[26bd]", "[1f4eb]", "[1f4bf]",
            "[1f3a4]", "[1f680]", "[26f5]", "[1f511]", "[2663]", "[3297]"};

    public static final int[] faceIds = new int[]{R.drawable.emoji_1f60a, R.drawable.emoji_1f60c, R.drawable.emoji_1f60f,
            R.drawable.emoji_1f601, R.drawable.emoji_1f604, R.drawable.emoji_1f609, R.drawable.emoji_1f612,
            R.drawable.emoji_1f614, R.drawable.emoji_1f616, R.drawable.emoji_1f618, R.drawable.emoji_1f621,
            R.drawable.emoji_1f628, R.drawable.emoji_1f630, R.drawable.emoji_1f631, R.drawable.emoji_1f633,
            R.drawable.emoji_1f637, R.drawable.emoji_1f603, R.drawable.emoji_1f61e, R.drawable.emoji_1f620,
            R.drawable.emoji_1f61c, R.drawable.emoji_1f60d, R.drawable.emoji_1f613, R.drawable.emoji_1f61d,
            R.drawable.emoji_1f62d, R.drawable.emoji_1f602, R.drawable.emoji_1f622, R.drawable.emoji_1f61a,
            R.drawable.emoji_1f623, R.drawable.emoji_1f632, R.drawable.emoji_1f62a, R.drawable.emoji_263a,
            R.drawable.emoji_1f47f, R.drawable.emoji_1f4aa, R.drawable.emoji_1f44a, R.drawable.emoji_1f44d,
            R.drawable.emoji_1f44e, R.drawable.emoji_1f44f, R.drawable.emoji_1f64f, R.drawable.emoji_1f446,
            R.drawable.emoji_1f447, R.drawable.emoji_261d, R.drawable.emoji_270c, R.drawable.emoji_1f448,
            R.drawable.emoji_1f449, R.drawable.emoji_1f44c, R.drawable.emoji_270b, R.drawable.emoji_270a,
            R.drawable.emoji_1f440, R.drawable.emoji_1f443, R.drawable.emoji_1f444, R.drawable.emoji_1f442,
            R.drawable.emoji_1f35a, R.drawable.emoji_1f35d, R.drawable.emoji_1f35c, R.drawable.emoji_1f35e,
            R.drawable.emoji_1f35f, R.drawable.emoji_1f359, R.drawable.emoji_1f363, R.drawable.emoji_1f382,
            R.drawable.emoji_1f367, R.drawable.emoji_1f37a, R.drawable.emoji_1f366, R.drawable.emoji_1f34e,
            R.drawable.emoji_1f34a, R.drawable.emoji_1f353, R.drawable.emoji_1f349, R.drawable.emoji_1f354,
            R.drawable.emoji_1f37b, R.drawable.emoji_1f48a, R.drawable.emoji_1f378, R.drawable.emoji_1f373,
            R.drawable.emoji_2615, R.drawable.emoji_1f6ac, R.drawable.emoji_1f384, R.drawable.emoji_1f389,
            R.drawable.emoji_1f380, R.drawable.emoji_1f388, R.drawable.emoji_1f451, R.drawable.emoji_1f494,
            R.drawable.emoji_1f334, R.drawable.emoji_1f49d, R.drawable.emoji_1f339, R.drawable.emoji_1f31f,
            R.drawable.emoji_2728, R.drawable.emoji_1f48d, R.drawable.emoji_1f462, R.drawable.emoji_1f3c0,
            R.drawable.emoji_1f3ca, R.drawable.emoji_1f4a3, R.drawable.emoji_1f4a6, R.drawable.emoji_1f4a8,
            R.drawable.emoji_1f4a4, R.drawable.emoji_1f4a2, R.drawable.emoji_1f004, R.drawable.emoji_1f469,
            R.drawable.emoji_1f468, R.drawable.emoji_1f467, R.drawable.emoji_1f466, R.drawable.emoji_1f437,
            R.drawable.emoji_1f435, R.drawable.emoji_1f419, R.drawable.emoji_1f42e, R.drawable.emoji_1f414,
            R.drawable.emoji_1f438, R.drawable.emoji_1f424, R.drawable.emoji_1f428, R.drawable.emoji_1f41b,
            R.drawable.emoji_1f420, R.drawable.emoji_1f436, R.drawable.emoji_1f42f, R.drawable.emoji_1f3b5,
            R.drawable.emoji_1f3b8, R.drawable.emoji_1f3be, R.drawable.emoji_1f484, R.drawable.emoji_1f40d,
            R.drawable.emoji_1f42c, R.drawable.emoji_1f42d, R.drawable.emoji_1f427, R.drawable.emoji_1f433,
            R.drawable.emoji_1f457, R.drawable.emoji_1f452, R.drawable.emoji_1f455, R.drawable.emoji_1f459,
            R.drawable.emoji_2614, R.drawable.emoji_2601, R.drawable.emoji_2600, R.drawable.emoji_26a1,
            R.drawable.emoji_1f319, R.drawable.emoji_2b55, R.drawable.emoji_274c, R.drawable.emoji_1f463,
            R.drawable.emoji_1f525, R.drawable.emoji_1f691, R.drawable.emoji_1f692, R.drawable.emoji_1f693,
            R.drawable.emoji_00a9, R.drawable.emoji_00ae, R.drawable.emoji_1f493, R.drawable.emoji_1f47b,
            R.drawable.emoji_1f480, R.drawable.emoji_303d, R.drawable.emoji_1f489, R.drawable.emoji_1f460,
            R.drawable.emoji_1f3e0, R.drawable.emoji_1f3e5, R.drawable.emoji_1f3e6, R.drawable.emoji_1f3ea,
            R.drawable.emoji_1f3e8, R.drawable.emoji_1f3e7, R.drawable.emoji_1f4a9, R.drawable.emoji_1f4b0,
            R.drawable.emoji_1f6b9, R.drawable.emoji_1f6ba, R.drawable.emoji_1f6bd, R.drawable.emoji_1f6c0,
            R.drawable.emoji_1f41a, R.drawable.emoji_1f45c, R.drawable.emoji_1f45f, R.drawable.emoji_1f47c,
            R.drawable.emoji_1f197, R.drawable.emoji_1f340, R.drawable.emoji_1f683, R.drawable.emoji_1f684,
            R.drawable.emoji_1f697, R.drawable.emoji_26ea, R.drawable.emoji_2122, R.drawable.emoji_2708,
            R.drawable.emoji_1f47e, R.drawable.emoji_2755, R.drawable.emoji_1f52b, R.drawable.emoji_26a0,
            R.drawable.emoji_1f488, R.drawable.emoji_1f4bb, R.drawable.emoji_1f4f1, R.drawable.emoji_2668,
            R.drawable.emoji_1f4f7, R.drawable.emoji_1f4de, R.drawable.emoji_1f3c6, R.drawable.emoji_1f3b0,
            R.drawable.emoji_1f40e, R.drawable.emoji_1f6a4, R.drawable.emoji_1f6b2, R.drawable.emoji_1f6a7,
            R.drawable.emoji_1f3a5, R.drawable.emoji_1f4e0, R.drawable.emoji_1f6a5, R.drawable.emoji_1f302,
            R.drawable.emoji_1f512, R.drawable.emoji_26c4, R.drawable.emoji_26bd, R.drawable.emoji_1f4eb,
            R.drawable.emoji_1f4bf, R.drawable.emoji_1f3a4, R.drawable.emoji_1f680, R.drawable.emoji_26f5,
            R.drawable.emoji_1f511, R.drawable.emoji_2663, R.drawable.emoji_3297};


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
        return null;
    }

    @Override
    public Pattern getNamePattern() {
        return Pattern.compile(PATTERN_EMOJI_FACE_NAME);
    }

    @Override
    public Pattern getSymbolPattern() {
        return null;
    }

}
