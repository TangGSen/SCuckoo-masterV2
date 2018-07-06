package com.ourcompany.utils;

import com.mob.ums.User;
import com.ourcompany.app.MApplication;

import java.io.File;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/26 11:21
 * Des    :
 */

public class Constant {
    //读取文件相关的
    public static final String APK_DIR = "apk";
    public static final String URI_FOR_FILE = "com.ourcompany.fileprovider";

    //获取验证码的时间间隔，单位秒
    public static final int SAFETY_CODE_TIME_INTERVAL = 30;
    public static final String COUNTRY_CODE = "86";

    //Actiivity bundle 字段
    public static final String ACT_FROM = "act_from";

    public static final String ACT_FROM_RESIGISTER = "act_from_resigister";
    public static final String ACT_LOGIN_BUNDLE = "act_login_bundle";
    public static final String ACT_LOGIN_PHONE = "act_login_phone";

    //来自登陆注册成功跳转到HomeActivity
    public static final String ACT_FROM_LOGIN_SUCCESS = "act_from_login_success";

    //SearchActivity ->UserInfoActivity
    public static final String ACT_SEARCH_BUNDLE = "act_search_bundle";
    public static final String KEY_ITEM_USER = "key_item_user";

    /**
     * 数量相关
     */
    //im 每页加加载10
    public static final int IM_PAGESIZE = 10;

    //添加好友时，捎一句话的数量
    public static final int MAX_ADD_FRIEND_MESSAGE = 20;
    //注册时，用户名的输入位数，目前是手机号码+两个空格符
    public static final int MAX_INPUT_FOR_USERNAME = 13;
    //最大的
    public static final int MAX_IMAGE_COUNT = 9;
    //    public static final String CACHE_DIR = MApplication.mContext.getCacheDir().getAbsolutePath()+ File.separator;
    public static final String CACHE_DIR = MApplication.mContext.getExternalCacheDir().getAbsolutePath() + File.separator;
    public static final java.lang.String BMOB_APPKEY = "2db1fc7ea509d3ea6639495a3a24066d";
    //view pager 的size
    public static final int VIEWPAGER_SIZE = 1;
    //关闭加载更多的UI时间
    public static final int CLOSE_LOAD_TIME = 500;


    public static LocationOption.MLocation CURRENT_CITY = null;
    //最多5颗星
    public static final int START_COUNT = 5;
    //保存当前的user
    public static User CURRENT_USER = null;
    //由于Activity 传递User ，但User 并不可以序列化
    public static User CURRENT_ITEM_USER = null;

    //聊天相隔2分钟就显示时间
    public static long MIN_CHAT_OFFET_TIME = 120000;
    public static String test_user_image = "http://img4.imgtn.bdimg.com/it/u=2925929882,1071278294&fm=27&gp=0.jpg";


    /**
     * UMSDK 相关的
     */
    //自定义字段，第三方id 的key
    public static final String UMSDK_COMSTOR_KEY_THRID_ID = "third_party_id";

    //自定义字段，是否存在第三id key
    public static final String UMSDK_COMSTOR_KEY_EXIST_ID = "isExistThirdId";
    public static final String UMSDK_COMSTOR_KEY_USER_TYPE = "user_type_name";
    public static final String UMSDK_COMSTOR_KEY_USER_TYPE_VALUE = "user_type_value";
    /**
     * bmob sdk 相关的
     */

    public static final String BMOB_CREATE = "createdAt";
    public static final String BMOB_UPDATEAT = "updateAt";
    public static final String BMOB_ORDER_DESCENDING = "-";
    //写法 bmob+表名+字段名，如果修改的话，这里也得修改，否则查不到
    public static final String BMOB_POST_USER = "user";
    public static final String BMOB_OBJECT_ID = "objectId";
    public static final String BMOB_SUSER_ID = "userId";
    public static final String BMOB_POST = "post";
    public static final String BMOB_COMMENT = "post";

    //表名
    public static final String BMOB_TABLE_SUSER = "SUser";
    //评论喜欢的字段
    public static final String BMOB_LIKES = "likes";
    //post 喜欢的总数
    public static final String BMOB_LIKE_COUNT = "likeCount";
    //Post 评论的总数
    public static final String BMOB_COMMENT_COUNT = "commentCount";
    //评论的总数
    public static final int BMOB_ADD_1 = 1;
    public static final int BMOB_REMOVE_1 = -1;
    //分两种
    public static final int POST_TEXT = 1;
    public static final int POST_IMAGES = 2;
    //Bundle UserInfo
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_POST_ID = "post_id";
    public static final String KEY_USEROBJECT_ID = "user_object_id";
    //SUser 的布谷服务数组字段
    public static final String KEY_CUCKOO_SERVICE_ARRAY = "cuckooService";
    public static final String KEY_USER_FIRST_CLASS = "fristClass";
    public static final String KEY_USER_SECOND_CLASS = "secondClass";

    //team meneber 相关的
    public static final String KEY_TEAM_TYPE = "team_type";
    public static final int TEAM_TYPE_DESINGE = 0;
    public static final int TEAM_TYPE_WORKER = 1;
    public static final String BMOB_CASECOUNT = "caseCount";
    public static final String BMOB_MEMBERTYPE = "memberType";

    public static final String BMOB_USER_TEAM_MEMBER = "teamMember";

    /**
     * 下面是各种app 获取json 的查询key
     */
    /**
     * 获取布谷服务的json
     */
    public static final String KEY_BMOB_APP_SETTING = "key";

    public static final String KEY_BMOB_CUCKOO_SERVICE = "cuckoo_service";

    public static final String KEY_BMOB_USERTYPE = "user_type";
    public static final String KEY_BMOB_MAIN_PAGE_AD = "main_page_ad";


}
