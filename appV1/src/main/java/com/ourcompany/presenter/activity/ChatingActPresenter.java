package com.ourcompany.presenter.activity;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.model.IMMessage;
import com.mob.imsdk.model.IMUser;
import com.ourcompany.im.model.MsgItem;
import com.ourcompany.im.utils.ChatUtils;
import com.ourcompany.utils.Constant;
import com.ourcompany.view.activity.ChatingActivityView;

import java.util.ArrayList;
import java.util.List;

import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/27 10:04
 * Des    :
 */

public class ChatingActPresenter extends MvpBasePresenter<ChatingActivityView> {
    //上次收到的时间
    private long lastRevTime = 0;

    public ChatingActPresenter(Context context) {
        super(context);
    }

    /**
     * 根据id ,来获取IM User
     *
     * @param id
     */
    public void getImUserById(String id) {
        MobIM.getUserManager().getUserInfo(id, new MobIMCallback<IMUser>() {
            @Override
            public void onSuccess(final IMUser imUser) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().setIMUser(imUser);
                    }
                });

            }

            @Override
            public void onError(int i, String s) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().getIMUserError();
                    }
                });
            }
        });
    }

    public void addMessage(IMMessage msg) {
        msg.setCreateTime(System.currentTimeMillis());
        msg.setStatus(IMMessage.STATUS_SEND_ING);
        final SparseArray<Object> objectSparseArray = new SparseArray<Object>();

        MsgItem msgitem = MsgItem.getMsgItemFromImMessage(msg);

        objectSparseArray.append(msgitem.getMsg_type(), msgitem);

        List<SparseArray<Object>> nowdata = new ArrayList<SparseArray<Object>>();
        nowdata.add(objectSparseArray);

        beforeAddMsg(msg);
        //将item message 放在最后
        //添加到RecycleView 中
//        qickAdapter.addData(nowdata, false);
//        lstChats.setSelection(qickAdapter.getCount() - 1);
//        final int insertIdx = qickAdapter.getCount() - 1;
//		String sendId = null;
//		int chatType = -1;

        MobIM.getChatManager().sendMessage(msg, new MobIMCallback<Void>() {
            public void onSuccess(Void result) {
//                getActivity().runOnUiThread(new Runnable() {
//
//                    public void run() {
//                        ChatUtils.setMsgStatusView(IMMessage.STATUS_SUCCESS, qickAdapter, insertIdx);
//                    }
//                });


            }

            public void onError(int code, String message) {
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        ChatUtils.setMsgStatusView(IMMessage.STATUS_FAILED, qickAdapter, insertIdx);
//                    }
//                });
            }
        });
    }


    private void beforeAddMsg(IMMessage msg) {
        if (msg.getAttach() == null) {
        } else {
            if (msg.getAttach().getType() == IMMessage.Attach.TEXT) {
            } else if (msg.getAttach().getType() == IMMessage.Attach.AUDIO) {
            } else if (msg.getAttach().getType() == IMMessage.Attach.IMAGE) {
                String picpath = msg.getAttach().getLocalPath();
                if (picpath != null && !TextUtils.isEmpty(picpath)) {
                } else {
                    picpath = msg.getAttach().getBody();
                }
                ChatUtils.attachpaths.add(picpath);
            } else if (msg.getAttach().getType() == IMMessage.Attach.VIDEO) {
                String picpath = msg.getAttach().getLocalPath();
                if (picpath != null && !TextUtils.isEmpty(picpath)) {
                } else {
                    picpath = msg.getAttach().getBody();
                }
                ChatUtils.attachpaths.add(picpath);
            } else if (msg.getAttach().getType() == IMMessage.Attach.FILE) {
            }
        }

        if (msg.getType() != IMMessage.TYPE_WARN) {
            long dif = Math.abs(lastRevTime - msg.getCreateTime());
            if (dif >= Constant.MIN_CHAT_OFFET_TIME) {
                addTimeShow(msg.getCreateTime());
                lastRevTime = msg.getCreateTime();
            }
        }
    }

    private void addTimeShow(long createTime) {

    }

}
