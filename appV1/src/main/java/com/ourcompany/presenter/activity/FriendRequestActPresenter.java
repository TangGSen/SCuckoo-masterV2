package com.ourcompany.presenter.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.mob.ums.FriendRequest;
import com.mob.ums.OperationCallback;
import com.ourcompany.bean.FriendRequestItem;
import com.ourcompany.manager.MServiceManager;
import com.ourcompany.utils.Constant;
import com.ourcompany.utils.LogUtils;
import com.ourcompany.view.activity.FriendRequestActvityView;

import java.util.ArrayList;
import java.util.List;

import company.com.commons.framework.presenter.MvpBasePresenter;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/2/23 21:09
 * Des    :
 */

public class FriendRequestActPresenter extends MvpBasePresenter<FriendRequestActvityView> {

    public FriendRequestActPresenter(Context context) {
        super(context);
    }


    /**
     * 获取好友的请求列表
     */
    public void getAddFriendRequests(int offest) {
        MServiceManager.getInstance().getAddFriendRequests(offest, Constant.IM_PAGESIZE, new OperationCallback<ArrayList<FriendRequest>>() {
            @Override
            public void onSuccess(ArrayList<FriendRequest> friendRequests) {
                super.onSuccess(friendRequests);
                LogUtils.e("sen", "onSuccess");
                //这个还在子线程跑
                FriendRequest friendRequest =new  FriendRequest();
                friendRequest.status.set( FriendRequest.RequestStatus.REFUSE);
                friendRequest.message.set( "hahah");
                friendRequest.requesterId.set( "9iooewp0");
                friendRequests.add(friendRequest);



                FriendRequest friendRequest1 =new  FriendRequest();
                friendRequest1.status.set( FriendRequest.RequestStatus.UNPROCESSED);
                friendRequest1.message.set( "请教一个问题");
                friendRequest1.requesterId.set( "uies837");
                friendRequests.add(friendRequest1);

                FriendRequest friendRequest2 =new  FriendRequest();
                friendRequest2.status.set( FriendRequest.RequestStatus.ACCEPT);
                friendRequest2.message.set( "我想加你");
                friendRequest2.requesterId.set("uie213333");
                friendRequests.add(friendRequest2);
                for (int i = 0; i < 4; i++) {
                    friendRequests.addAll(friendRequests);
                }
                final List<FriendRequestItem> items = new ArrayList<>(friendRequests.size());
                for (int i = 0; i < friendRequests.size(); i++) {
                    FriendRequestItem item = new FriendRequestItem();
                    item.setFriendRequst(friendRequests.get(i));
//                    item.setUserName(friendRequests.get(i).requesterId.get().substring(0, 5) + "*" + i);
                    item.setUserName(friendRequests.get(i).requesterId.get()+ "*" + i);
                    items.add(i, item);
                }
                //其实还应该将获取的id ，再次请求获取user,合并数据，这里应该使用rxjava 更方便
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (items.size() > 0) {
                            getView().showContent(items);
                        } else {
                            getView().showEptyView();
                        }
                    }
                });


            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                getView().showErrorView();
            }
        });
    }

    public void replyFriendRequesting(final int position , String requesterId, final boolean isAccpet) {
        MServiceManager.getInstance().replyFriendRequesting( requesterId,  isAccpet,new OperationCallback<Void>(){
            @Override
            public void onSuccess(Void aVoid) {
                super.onSuccess(aVoid);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isAccpet){
                            //接受成功
                            getView().friendRequestAccpetOk(position);
                        }else{
                            //拒绝成功
                            getView().friendRequestRefuseOk(position);
                        }
                    }
                });
            }

            @Override
            public void onFailed(Throwable throwable) {
                super.onFailed(throwable);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                       getView().friendRequestFaild();
                    }
                });
            }
        });
    }
}
