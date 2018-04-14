package com.mrbattery.encounter;

public class Constant {
    //当前用户ID
    private static int currUserID;

    //十二星座
    private static String unknowConstellation = "位置星座";
    private static final String Aries = "白羊座";
    private static final String Taurus = "金牛座";
    private static final String Gemini = "双子座";
    private static final String Cancer = "巨蟹座";
    private static final String Leo = "狮子座";
    private static final String Virgo = "处女座";
    private static final String Libra = "天秤座";
    private static final String Scorpio = "天蝎座";
    private static final String Sagittarius = "射手座";
    private static final String Capricorn = "摩羯座";
    private static final String Aquarius = "水瓶座";
    private static final String Pisces = "双鱼座";

    //性别
    private static String male = "男";
    private static String female = "女";
    private static String unknowGender = "未知性别";
    public static final int MALE = 1;
    public static final int FEMALE = 2;
    public static final int UNKNOWGENDER = 0;

    //性别图标资源
    private static int maleSrc = R.drawable.ic_gender_male;
    private static int femaleSrc = R.drawable.ic_gender_female;

    /**
     * 性别序号转换为性别名
     *
     * @param gender 性别序号
     * @return
     */
    public static String getGender(int gender) {
        switch (gender) {
            case 1:
                return male;
            case 2:
                return female;
            default:
                return unknowGender;
        }
    }

    /**
     * 性别序号转换为性别图标资源
     *
     * @param gender 性别序号
     * @return
     */
    public static int getGenderSrc(int gender) {
        switch (gender) {
            case 1:
                return maleSrc;
            case 2:
                return femaleSrc;
            default:
                return maleSrc;
        }

    }

    /**
     * 星座序号转换为星座名
     *
     * @param constellation 星座序号
     * @return 星座名
     */
    public static String getConstellationName(int constellation) {
        switch (constellation) {
            case 1:
                return Aries;
            case 2:
                return Taurus;
            case 3:
                return Gemini;
            case 4:
                return Cancer;
            case 5:
                return Leo;
            case 6:
                return Virgo;
            case 7:
                return Libra;
            case 8:
                return Scorpio;
            case 9:
                return Sagittarius;
            case 10:
                return Capricorn;
            case 11:
                return Aquarius;
            case 12:
                return Pisces;
            default:
                return unknowConstellation;
        }
    }

    public static int getConstellationIndex(String constellationName){
        switch (constellationName){
            case Aries:
                return 1;
            case Taurus:
                return 2;
            case Gemini:
                return 3;
            case Cancer:
                return 4;
            case Leo:
                return 5;
            case Virgo:
                return 6;
            case Libra:
                return 7;
            case Scorpio:
                return 8;
            case Sagittarius:
                return 9;
            case Capricorn:
                return 10;
            case Aquarius:
                return 11;
            case Pisces:
                return 12;
            default:
                return 0;
        }
    }

    public static int getCurrUserID() {
        return currUserID;
    }

    public static void setCurrUserID(int currUserID) {
        Constant.currUserID = currUserID;
    }

    //String类型的userID的set方法
    public static void setCurrUserID(String currUserID) {
        int temp = Integer.valueOf(currUserID);
        Constant.currUserID = temp;
    }
}
