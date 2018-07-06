package com.ourcompany.manager;

import android.text.TextUtils;
import android.util.SparseArray;

import com.ourcompany.bean.bmob.SUser;
import com.ourcompany.bean.json.CuckooServiceJson;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bmob.v3.BmobQuery;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/5/3 16:46
 * Des    :
 */

public class ClassSerachService {
    private volatile static ClassSerachService instance;
    private static final String KEY_CLASS_INDEX = "class_index";

    private ClassSerachService() {
    }

    private List<CuckooServiceJson.CuckooServiceBean> mKeyWordList = new ArrayList<>();
    //需要比对看是否需要加载
    private Set<Integer> currentKeyIndexs = new HashSet<>();
    private Set<Integer> lastKeyIndexs = new HashSet<>();
    //只保存一个值
    private Map<String, Integer> currentClassIndexs = new HashMap<>();
    private Map<String, Integer> lastClassIndexs = new HashMap<>();
    //这个是搜索keyWord的
    private SparseArray<BmobQuery<SUser>> mKeyWordConditionList = new SparseArray<>();

    public List<CuckooServiceJson.CuckooServiceBean> getKeyWordList() {
        return mKeyWordList;
    }

    public void setKeyWordList(List<CuckooServiceJson.CuckooServiceBean> mkeyWordList) {
        this.mKeyWordList = mkeyWordList;
    }

    public List<CuckooServiceJson.CuckooClassBean> getClassList() {
        return mClassList;
    }

    public void setClassList(List<CuckooServiceJson.CuckooClassBean> mClassList) {
        this.mClassList = mClassList;
    }

    private List<CuckooServiceJson.CuckooClassBean> mClassList = new ArrayList<>();

    public static ClassSerachService getInstance() {
        if (instance == null) {
            synchronized (ClassSerachService.class) {
                if (instance == null) {
                    instance = new ClassSerachService();
                }
            }
        }
        return instance;
    }

    /**
     * 如果有选择那么就添加
     *
     * @param keyIndex
     */
    private void addKeyList(int keyIndex) {
        currentKeyIndexs.add(keyIndex);
    }

    /**
     * 如果不选那么就删除
     *
     * @param keyIndex
     */
    private void removeKeyList(int keyIndex) {
        synchronized (ClassSerachService.class) {
            currentKeyIndexs.remove(keyIndex);
        }
    }

    /**
     * 修改搜索的条件
     *
     * @param classIndex
     */
    private void addClassList(int classIndex) {
        synchronized (ClassSerachService.class) {
            currentClassIndexs.put(KEY_CLASS_INDEX, classIndex);
        }
    }

    private void addLastClassList(int classIndex) {
        synchronized (ClassSerachService.class) {
            lastClassIndexs.put(KEY_CLASS_INDEX, classIndex);
        }
    }

    /**
     * 当Activity 退出很其他情况需要清除当前的所有筛选条件
     */
    public void clear() {
        if (mKeyWordList != null && mKeyWordList.size() > 0) mKeyWordList.clear();
        if (mClassList != null && mClassList.size() > 0) mClassList.clear();
        if (currentKeyIndexs != null) currentKeyIndexs.clear();
        if (lastKeyIndexs != null) lastKeyIndexs.clear();
        if (lastClassIndexs != null) lastClassIndexs.clear();
        if (currentClassIndexs != null) currentClassIndexs.clear();
    }


    /**
     * 提供判断是否可以进行加载，就是上次搜索跟这次修改的搜索条件是否相同
     *
     * @return
     */
    public boolean isAnbleLoadding() {
        boolean isAnble = false;
        if (currentClassIndexs != null && lastClassIndexs != null) {
            //两次不相等，那必定重新加载
            if (currentClassIndexs.size() > 0 && currentClassIndexs.size() != lastClassIndexs.size()) {
                isAnble = true;
                return isAnble;
            } else if (currentClassIndexs.size() <= 0 && lastClassIndexs.size() > 0) {
                //也就是这次并没改变什么
                isAnble = false;
            } else if (currentClassIndexs.size() > 0 && lastClassIndexs.size() > 0 && currentClassIndexs.size() == lastClassIndexs.size()) {
                //如果集合大小相等,那么判断两个队列的元素是否都相同
                int currentIndex = currentClassIndexs.get(KEY_CLASS_INDEX);
                int lastIndex = lastClassIndexs.get(KEY_CLASS_INDEX);
                if (currentIndex == lastIndex) {
                    //相等不需要加载
                    isAnble = false;
                } else {
                    //不相等那么的重新加载
                    isAnble = true;
                    return isAnble;
                }
            }
        }

        //对key word 进行判断
        if (currentKeyIndexs != null && lastKeyIndexs != null) {
            if (currentKeyIndexs.size() != lastKeyIndexs.size()) {
                LogUtils.e("sen", "****0");
                isAnble = true;
                return isAnble;
            } else if (currentKeyIndexs.size() <= 0 && lastKeyIndexs.size() <= 0) {
                //也就是这次并没改变什么
                LogUtils.e("sen", "****1");
                isAnble = false;
            } else if (currentKeyIndexs.size() > 0 && lastKeyIndexs.size() > 0 && currentKeyIndexs.size() == lastKeyIndexs.size()) {
                //那么就判断上次的保存是否跟这个有
                //专业去重
                Set<Integer> set = new HashSet<>(lastKeyIndexs);
                int size = lastKeyIndexs.size();
                for (Integer integer : currentKeyIndexs) {
                    set.add(integer);
                }
                //判断set 是否有新增的元素
                if (set.size() > size) {
                    LogUtils.e("sen", "****3");
                    isAnble = true;
                } else {
                    LogUtils.e("sen", "****4");
                    isAnble = false;
                }
            }
        }
        return isAnble;
    }

    /**
     * 提供修改keywordlist 的数据
     *
     * @param position
     */
    public void changeKeyWordList(int position) {
        mKeyWordList.get(position).setSeleted(!mKeyWordList.get(position).isSeleted());
        if (mKeyWordList.get(position).isSeleted()) {
            addKeyList(position);
        } else {
            removeKeyList(position);
        }
    }

    /**
     * 修改分类的index 数据
     *
     * @param target
     */
    public void changeClassData(int target, boolean isAddLast) {
        if (target >= 0 && mClassList != null && target < mClassList.size()) {
            int size = mClassList.size();
            mClassList.get(target).setSeleted(true);
            for (int i = 0; i < size; i++) {
                if (i != target) {
                    mClassList.get(i).setSeleted(false);
                }
            }
            if (isAddLast) {
                addLastClassList(target);
            } else {
                addClassList(target);
            }

        }

    }

    /**
     * 搜索当前的,创建Bmob 的搜索条件并保存在SpareArray中
     */
    public void getCurrentSerach() {
        String str = "当前搜搜：";
        for (Integer integer : currentKeyIndexs) {
            if (integer >= 0 && integer < mKeyWordList.size()) {

                str += mKeyWordList.get(integer).getKey() + "--";
            }
        }

        LogUtils.e("sen", str);
        Integer integer = currentClassIndexs.get(KEY_CLASS_INDEX);
        if (integer != null && integer >= 0 && integer < mClassList.size()) {
            LogUtils.e("sen", "当前搜分类：" + mClassList.get(integer).getClassType() + "\n当前大小：" + currentClassIndexs.size());
        }
    }

    /**
     * 保存当前搜索的
     */
    public void saveCurrentSerach() {
        lastKeyIndexs.clear();
        lastKeyIndexs.addAll(currentKeyIndexs);

        lastClassIndexs.clear();
        lastClassIndexs.putAll(currentClassIndexs);
        //重置完毕
    }

    /**
     * 重置当前搜索的
     */
    public void resetCurrentSerach() {
        synchronized (ClassSerachService.class) {
            if (currentKeyIndexs != null) {
                currentKeyIndexs.clear();
            }

            if (currentClassIndexs != null) {
                currentClassIndexs.clear();
            }

            if (mKeyWordList != null) {
                int size = mKeyWordList.size();
                for (int i = 0; i < size; i++) {
                    mKeyWordList.get(i).setSeleted(false);
                }
            }
            if (mClassList != null) {
                int size = mClassList.size();
                for (int i = 0; i < size; i++) {
                    if (i == 0) {
                        mClassList.get(i).setSeleted(true);
                    } else {
                        mClassList.get(i).setSeleted(false);
                    }
                }
            }
        }
    }

    /**
     * 将当前的搜索条件装换为Bmob 的搜索条件
     */
    public BmobQuery<SUser> getKeyWordCondition() {
        //使用当前的currenKeyword serach
        if (mKeyWordList == null ||currentKeyIndexs==null ||currentKeyIndexs.size()<=0) {
            return null;
        }
        BmobQuery<SUser> query = new BmobQuery<SUser>();
        List<String> stringList = new ArrayList<>();
        for (Integer integer:currentKeyIndexs) {
            if(integer!=null && integer<mKeyWordList.size() && integer>=0){
                stringList.add(mKeyWordList.get(integer).getKey());
            }
        }
        query.addWhereContainsAll(Constant.KEY_CUCKOO_SERVICE_ARRAY, stringList);
        return query;
    }
    //分类的搜索条件
    public BmobQuery<SUser> getClassCondition() {
        //使用当前的currenKeyword serach
        if (mClassList == null ||currentClassIndexs==null ||currentClassIndexs.size()<=0) {
            return null;
        }
        //条件2
        Integer classIndex = currentClassIndexs.get(KEY_CLASS_INDEX);
        //如果是全部就不需要添加这个条件
        List<String> stringList = new ArrayList<>();
        if (classIndex != null && classIndex != 0 && classIndex<mClassList.size()) {
            BmobQuery<SUser> query= new BmobQuery<SUser>();
            stringList.add(mClassList.get(classIndex).getClassType());
            query.addWhereContainsAll(Constant.KEY_USER_FIRST_CLASS, stringList);
            return query;
        }
        return null;
    }

    public BmobQuery<SUser> getSecondClassCondition(String mTitle) {
        if(TextUtils.isEmpty(mTitle)){
            return null;
        }
        BmobQuery<SUser> query= new BmobQuery<SUser>();
        List<String> stringList = new ArrayList<>();
        stringList.add(mTitle);
        query.addWhereContainsAll(Constant.KEY_USER_SECOND_CLASS, stringList);
        return query;
    }
}
