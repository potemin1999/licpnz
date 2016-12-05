package org.licpnz.api.news;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Ilya on 05.11.2016.
 */

public final class New implements Serializable {

    public Title mT;
    public Message mM;
    public Info mI;
    public int mID;
    public HashMap<String,Object> mObjects;

    public New(){
        super();
        mObjects = new HashMap<String,Object>();
        mT = new Title();
        mM = new Message();
        mI = new Info();
    }

    public class Message{
        public String mText;
        public String mAttributes;
        public String[] mRefs;
        public String[] mAttached;
    }

    public class Title{
        public String mText;
        public String mAttributes;
        public String mRef;
    }

    public class Info{
        public String mCategory;
        public String mCategoryRef;
        public long mReadsCount;
        public String mAuthor;
        public String mCommentsCount;
        public String mCommentsRef;
        public float mRating;
    }

    public String toString(){
        return new StringBuilder()
                .append("id:").append(mID).append("\n")
                .append("title:").append(mT.mText).append("\n")
                .append("href:").append(mT.mRef).append("\n")
                .append("srcs:").append(mM.mAttached.length).append("")
                .toString();
    }
}
