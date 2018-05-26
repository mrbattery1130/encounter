package com.mrbattery.encounter.constant;

import com.mrbattery.encounter.entity.User;

public class EPQTest {
    public static final int E = 0;
    public static final int N = 1;
    public static final int P = 2;
    public static final int L = 3;

    public static final int positive = 1;
    public static final int negative = -1;
    public static final int neutral = 0;

    public static final int highScore = 0;
    public static final int middleScore = 1;
    public static final int lowScore = 2;

    public static final int M = 0;
    public static final int SD = 1;

    public static final double[][] NORM = {{2.73, 2.05}, {7.50, 2.84}, {4.42, 2.95}, {6.19, 2.96}};

    public static final String[] EPQ = {
            "你的情绪是否时起时落？ ",
            "当你看到小孩（或动物）受折磨时是否感到难受？ ",
            "你是个健谈的人吗？ ",
            "如果你说了要做什么事，是否不论此事可能不顺利你都总能遵守诺言？ ",
            "你是否会无言无故地感到“很惨”？ ",
            "欠债会使你感到忧虑吗？ ",
            "你是个生气勃勃的人吗？ ",
            "你是否曾贪图过超过你应得的分外之物？",
            "你是个容易被激怒的人吗？ ",
            "你会服用能产生奇异或危险效果的药物吗？ ",
            "你愿意认识陌生人吗？ ",
            "你是否曾经有过明知自己做错了事却责备别人的情况？",
            "你的感情容易受伤害吗？ ",
            "你是否愿意按照自己的方式行事，而不愿意按照规则办事？ ",
            "在热闹的聚会中你能使自己放得开，使自己玩得开心吗？ ",
            "你所有的习惯是否都是好的？",
            "你是否时常感到“极其厌倦”？ ",
            "良好的举止和整洁对你来说很重要吗？ ",
            "在结交新朋友时，你经常是积极主动的吗？",
            "你是否有过随口骂人的时候？ ",
            "你认为自己是一个胆怯不安的人吗？ ",
            "你是否认为婚姻是不合时宜的，应该废除？",
            "你能否很容易地给一个沉闷的聚会注入活力？",
            "你曾毁坏或丢失过别人的东西吗？ ",
            "你是个忧心忡忡的人吗？ ",
            "你爱和别人合作吗？ ",
            "在社交场合你是否倾向于呆在不显眼的地方？ ",
            "如果在你的工作中出现了错误，你知道后会感到忧虑吗？ ",
            "你讲过别人的坏话或脏话吗？ ",
            "你认为自己是个神经紧张或“弦绷得过紧”的人吗？ ",
            "你是否觉得人们为了未来有保障，而在储蓄和保险方面花费的时间太多了？",
            "你是否喜欢和人们相处在一起？ ",
            "当你还是个小孩子的时候，你是否曾有过对父母耍赖或不听话的行为？ ",
            "在经历了一次令人难堪的事之后，你是否会为此烦恼很长时间？ ",
            "你是否努力使自己对人不粗鲁？ ",
            "你是否喜欢在自己周围有许多热闹和令人兴奋的事情？ ",
            "你曾在玩游戏时作过弊吗？ ",
            "你是否因自己的“神经过敏”而感到痛苦？ ",
            "你愿意别人怕你吗？ ",
            "你曾利用过别人吗？ ",
            "你是否喜欢说笑话和谈论有趣的事？ ",
            "你是否时常感到孤独？ ",
            "你是否认为遵循社会规范比按照个人方式行事更好一些？ ",
            "在别人眼里你总是充满活力的吗？ ",
            "你总能做到言行一致吗？",
            "你是否时常被负疚感所困扰？ ",
            "你有时将今天该做的事情拖到明天去做吗？",
            "你能使一个聚会顺利进行下去吗？"
    };

    public static final int[][] scoreRule = {
            {0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0},
            {0, -1, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0, -1, 0, -1, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, 1, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, 0, -1, 0, 0, -1, 0, 0, 0, 0, 1, 0, -1, 0}
    };

    public static final String[][] resultSet = {
            {"人格外向，好交际、渴望刺激和冒险，情感易于冲动。",
                    "人格偏内向但是有一定的交际欲望，情绪偶尔冲动，总体还算稳定。",
                    "人格内向，富于内省，不喜欢刺激，喜欢有秩序的生活方式，情绪比较稳定。"},
            {"可能焦虑、担心、常常郁郁不乐、忧心忡忡，有强烈的情绪反应。",
                    "有时会焦虑担心，比较容易恢复平静，情绪反应不算太强烈，有一定的自我控制能力。",
                    "情绪反应缓慢且轻缓，很容易恢复平静，稳重、性情温和、善于自我控制。"},
            {"可能孤独、不关心他人，难以适应外部环境，不近人情，喜欢干奇特的事情，并且不顾危险。",
                    "一般情况下能适应外部环境，态度温和，有时可能不善于理解他人。",
                    "能与人相处，能较好地适应环境，态度温和、不粗暴、善从人意。"}

    };

    public static String resultAnalyse(double eScore, double nScore, double pScore, double lScore) {

        if (eScore != 0 || nScore != 0 || pScore != 0 | lScore != 0) {
            return resultSet[E][judgeScore(eScore)]
                    + resultSet[N][judgeScore(nScore)]
                    + resultSet[P][judgeScore(pScore)];
        } else {
            return "还没有进行测试";
        }
    }


    public static double NormConverse(int ENPL, double score) {
        score = 50 + 10 * (score - NORM[ENPL][M]) / NORM[ENPL][SD];
        return score;

    }

    private static int judgeScore(double score) {
        if (score > 56.7) {
            return highScore;
        } else if (score > 43.3) {
            return middleScore;
        } else {
            return lowScore;
        }
    }


}
