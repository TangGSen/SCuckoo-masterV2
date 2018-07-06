package com.ourcompany.utils;

import android.support.v7.util.DiffUtil;

import com.ourcompany.bean.bmob.Comment;

import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/3/23 18:55
 * Des    :
 */

public class MDiffCallback extends DiffUtil.Callback {
        private List<Comment> oldList;
        private List<Comment> newList;

        public MDiffCallback(List<Comment> oldList, List<Comment> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getObjectId() .equals( newList.get(newItemPosition).getObjectId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Comment beanOld = oldList.get(oldItemPosition);
            Comment beanNew = newList.get(newItemPosition);
            if (!beanOld.getContent().equals(beanNew.getContent())) {
                return false;//如果有内容不同，就返回false
            }
            return true;
        }

}
