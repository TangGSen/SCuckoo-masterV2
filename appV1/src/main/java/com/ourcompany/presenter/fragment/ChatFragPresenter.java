package com.ourcompany.presenter.fragment;

import android.content.Context;
import android.text.TextUtils;

import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.MobIMMessageReceiver;
import com.mob.imsdk.MobIMReceiver;
import com.mob.imsdk.model.IMConversation;
import com.mob.imsdk.model.IMMessage;
import com.mob.ums.OperationCallback;
import com.ourcompany.R;
import com.ourcompany.app.MApplication;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.ResourceUtils;
import com.ourcompany.utils.Utils;
import com.ourcompany.view.fragment.ChatFrameView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/1/18 15:04
 * Des    :
 */

public class ChatFragPresenter extends MvpBasePresenter<ChatFrameView> {
    private MobIMCallback<List<IMConversation>> conversationCallback;


    public ChatFragPresenter(Context context) {
        super(context);
    }

    public void initIMReceiver() {
        MobIMMessageReceiver messageReceiver = new MobIMMessageReceiver() {
            public void onMessageReceived(List<IMMessage> messageList) {
                //接收到消息，则刷新界面
                refreshData();
                //更新未读消息总数
                //  ((MainActivity) getActivity()).freshUnreadMessageCount();
            }
        };

        MobIMReceiver generalReceiver = new MobIMReceiver() {
            public void onConnected() {
                setIMConnectStatus(0);
                //连接im成功后，刷新会话列表
                refreshData();
            }

            public void onConnecting() {
                setIMConnectStatus(1);
            }

            public void onDisconnected(int error) {
                setIMConnectStatus(error);
            }
        };
        MobIM.addMessageReceiver(messageReceiver);
        MobIM.addGeneralReceiver(generalReceiver);
    }

    public void refreshData() {
        if (Constant.CURRENT_USER == null) {
            //加载失败
            getView().showLoadingFailed();
            return;
        }
        //加载本地会话
        MobIM.getChatManager().getAllLocalConversations(initConversationCallback());
    }

    //本地会话回调对象
    private MobIMCallback<List<IMConversation>> initConversationCallback() {
        if (conversationCallback == null) {
            conversationCallback = new MobIMCallback<List<IMConversation>>() {
                public void onSuccess(final List<IMConversation> list) {
                    getView().mergeData(list);
                }

                public void onError(int code, String message) {
                    getView().showLoadingFailed();
                }
            };
        }
        return conversationCallback;
    }

    private void setIMConnectStatus(int error) {

    }

    /**
     * 获取新朋友的列表,个数
     */
    public void getNewFriendCount() {
        MServiceManager.getInstance().getNewFriend(new OperationCallback<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> strings) {
                super.onSuccess(strings);
                if (strings.size() > 0) {
                    getView().showNewFriendCount(strings.size());
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
            }
        });
    }


    public String getMessage(IMMessage lastMessage) {
        if (lastMessage == null) {
            return "";
        } else {
            IMMessage.Attach attach = lastMessage.getAttach();
            if (attach == null) {
                String body = lastMessage.getBody();
                if (TextUtils.isEmpty(body)) {
                    return "";
                } else {
                    return Utils.changeStrToWithEmoji(MApplication.mContext, lastMessage.getBody()).toString();
                }
            } else if (attach.getType() == IMMessage.Attach.AUDIO) {
                return ResourceUtils.getString(R.string.message_audio);
            } else if (attach.getType() == IMMessage.Attach.FILE) {
                return ResourceUtils.getString(R.string.message_file);
            } else if (attach.getType() == IMMessage.Attach.IMAGE) {
                return ResourceUtils.getString(R.string.message_image);
            } else if (attach.getType() == IMMessage.Attach.VIDEO) {
                return ResourceUtils.getString(R.string.message_video);
            } else {
                return "";
            }
        }
    }

    //处理数据
    public void mergeData(final List<IMConversation> mMessages, final List<IMConversation> get) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                if (get == null || get.size() == 0) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getView().showNotData();
                        }
                    });
                    return;
                }

                if (mMessages.size() == 0) {
                    //第一次加载
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getView().showContentView(get);
                        }
                    });

                } else {
                    int currentSize = mMessages.size();
                    List<Integer> existIndexs = new ArrayList<>();
                    final List<IMConversation> newItem = new ArrayList<>();
                    final Map<Integer, IMConversation> updateItem = new HashMap<>();
                    int listSize = get.size();
                    //找出相同的项，以conversationId 为准
                    for (int i = 0; i < currentSize; i++) {
                        for (int j = 0; j < listSize; j++) {
                            if (mMessages.get(i).getOtherInfo().getId().equals(get.get(j).getOtherInfo().getId())) {
                                updateItem.put(i, get.get(j));
                                existIndexs.add(j);
                            }
                        }
                    }
                    //添加新的对话
                    final int size = existIndexs.size();
                    for (int i = 0; i < size; i++) {
                        if (i != existIndexs.get(i)) {
                            newItem.add(i, get.get(existIndexs.get(i)));
                        }
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getView().showContentView(newItem, updateItem);
                        }
                    });

                }
            }
        });
    }
}
