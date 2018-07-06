package com.ourcompany.bean.bmob;

import android.text.TextUtils;

import com.ourcompany.R;
import com.ourcompany.utils.ResourceUtils;

import cn.bmob.v3.BmobObject;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/7 20:10
 * Des    :
 */

public class TeamMember extends BmobObject {

    private String userId;//teamMesber 所属的SUser id
    private String memberName;
    private Integer caseCount;//案例的数量
    private Integer workAge;//工作年限
    private String memberImage;//成员的用户url
    private Integer memberType;//0 表示设计团队的，1 表示施工团队的

    private String otherInfo;//其他信息
    public String getOtherInfo(){
        if(TextUtils.isEmpty(otherInfo)){
            StringBuilder builder = new StringBuilder();
            builder.append(ResourceUtils.getString(R.string.str_case));
            builder.append(" ");
            if(caseCount==null || (caseCount!=null &&caseCount<0)){
                builder.append("0");
            }else{
                builder.append(caseCount);
            }
            builder.append(" ");
            builder.append(ResourceUtils.getString(R.string.str_work_age));
            builder.append(" ");
            if(workAge==null || (workAge!=null &&workAge<0)){
                builder.append("0");
            }else{
                builder.append(workAge);
            }
            otherInfo = builder.toString();
        }
        return otherInfo;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getCaseCount() {
        if(caseCount==null || (caseCount!=null &&caseCount<0)){
            return 0;
        }
        return caseCount;
    }

    public void setCaseCount(Integer caseCount) {
        this.caseCount = caseCount;
    }

    public Integer getWorkAge() {
        if(workAge==null || (workAge!=null &&workAge<0)){
            return 0;
        }
        return workAge;
    }

    public void setWorkAge(Integer workAge) {
        this.workAge = workAge;
    }

    public String getMemberImage() {
        return memberImage;
    }

    public void setMemberImage(String memberImage) {
        this.memberImage = memberImage;
    }
}
