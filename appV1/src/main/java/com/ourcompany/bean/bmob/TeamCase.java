package com.ourcompany.bean.bmob;

import android.text.TextUtils;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/7 18:08
 * Des    :团队的案例，或者是公司的案例
 */

public class TeamCase extends BmobObject {
    private String content;// 帖子内容

    private TeamMember teamMember;

    private List<String> imageUrls;//帖子图片

    private List<String> styleLabel;
    //组装成字符串
    private String styleLabelString;
    //所shu那个公司或这个人的案例
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStyleString() {
        if (TextUtils.isEmpty(styleLabelString)) {
            int size = styleLabel.size();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size; i++) {

                builder.append(styleLabel.get(i));
                if(i!=size-1){
                    builder.append("/");
                }
            }

            styleLabelString = builder.toString();
        }
        return styleLabelString;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
