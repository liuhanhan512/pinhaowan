package com.alibaba.laiwang.tide.share.business;

import android.content.Context;
import android.text.TextUtils;


import com.laiwang.sdk.message.IILWMessage;
import com.laiwang.sdk.message.LWMessage;
import com.laiwang.sdk.openapi.LWAPIDefine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分享组件,构件器
 * @author i-mac
 *
 */
public abstract class AbsShareProvider implements IIShareProvider  {
	
	public static int SHARE_TO_POST = 0;
	public static int SHARE_TO_FIRENDS = 1;
	public static int FORWARD_TO_FRIENDS = 2;
	public static int FORWARD_TO_POST = 3;
	
	protected IILWMessage mIILWMessage=null;
	protected List<Map<String, Object>> mAttachments;
	protected Map<String, Object> mHashmap = new HashMap<String, Object>();
	protected int mShareTo;
	protected Context mContext;
//    protected FeedVO mFeedVO=null;
	protected List<String> mReceiverIds = null;
	protected String mConversionId = null;
	protected String mFlag = null;
	protected String mUuid = null;
	protected String mMessageId = null;
	protected String mMsgownerId = null;
	protected String mEventId = null;
	protected String shareTo,shareFrom,shareKey;
	
    private String taskId;

    private String picFilePathHistory;
	protected abstract void initParameter();
	protected abstract void sendSuccess(Map<String, Object> result);
	protected abstract void sendToPost(String shareTo,String shareFrom, String shareKey,Callback<Map<String, Object>> callback);
	protected abstract void sendToFriends(String shareTo,String shareFrom, String shareKey,Callback<Map<String, Object>> callback);
	protected abstract void forwardToPost(String shareTo,String shareFrom, String shareKey,Callback<Map<String, Object>> callback);
	protected abstract void forwardToFriends(String shareTo,String shareFrom, String shareKey,Callback<Map<String, Object>> callback);
	
	@Override
	public void shareToFriends(String shareTo,String shareFrom, String shareKey,IILWMessage iLWMessage, String conversionId,
			List<String> receiverId, String flag,
			Callback<Map<String, Object>> callback) {
		
		mShareTo = SHARE_TO_FIRENDS;
		mIILWMessage = iLWMessage;
		mConversionId =  conversionId;
		mReceiverIds = receiverId;
		mFlag = flag;
		sendToFriends(shareTo,shareFrom,shareKey,callback);
	}
	
	@Override
	public void shareToHome(String shareTo,String shareFrom, String shareKey,final IILWMessage iLWMessage) {
        /**
		mShareTo = SHARE_TO_POST;
		mIILWMessage = iLWMessage;
		mAttachments = new ArrayList<Map<String, Object>>();
		mHashmap = new HashMap<String, Object>();
		LWMessage lwMessage = (LWMessage) iLWMessage;
		mHashmap.put("clientId", lwMessage.getAppkey());
		mHashmap.put("title", lwMessage.getMessageTitle());
		mHashmap.put("description", lwMessage.getMessageText());
		mHashmap.put("link", lwMessage.getMessageLinkUrl());
		mHashmap.put("content", lwMessage.getMessageChat());
		
		initParameter();
		
		mAttachments.add(mHashmap);

		shareToMessage(shareTo,shareFrom,shareKey,mSharePostCallback );
         */
	}
	
	private void shareToMessage(final String shareTo,final String shareFrom,final String shareKey,final Callback<Map<String, Object>> callback) {
		this.shareKey=shareKey;
		this.shareFrom=shareFrom;
		this.shareTo=shareTo;
		final LWMessage lwMessage = (LWMessage) mIILWMessage;
		String picUrl = lwMessage.getMessageThumb();
		
		if (TextUtils.isEmpty(picUrl) || picUrl.startsWith("http://") || picUrl.startsWith("https://")) {
			mHashmap.put("picture", picUrl);
			mHashmap.put("thumbnail", picUrl);
			
			if (mShareTo == FORWARD_TO_FRIENDS) {
				forwardToFriends(shareTo,shareFrom,shareKey,callback);
			} else if (mShareTo == FORWARD_TO_POST) {
				forwardToPost(shareTo,shareFrom,shareKey,callback);
			} else {
				sendToPost(shareTo,shareFrom,shareKey,callback);
			}
			
			
		} else {
            /**
			if (picUrl.startsWith("file://")) {
				picUrl = picUrl.replace("file://", "");
			}
            if (!picUrl.equals(picFilePathHistory)) {
                picFilePathHistory = picUrl;
                LaiwangCacheHelper.getInstance().deleteUploaderExtra(taskId);
                taskId = "tmp_" + System.currentTimeMillis();
            }
			 SliceUploadHelper.uploadImage(picUrl, taskId, SliceUploadHelper.POST_UPLOAD_WITH_MEDIAID, new LaiwangCallback<Map<String, String>>() {
				@Override
				public void onSuccess(Map<String, String> result) {
					
					final String bigImage = result.get("up-uri");
					mHashmap.put("picture", bigImage);
					mHashmap.put("thumbnail", result.get("up-thumb"));
					
					lwMessage.setMessageImageURL(bigImage);
					
					if (mShareTo == FORWARD_TO_FRIENDS) {
						forwardToFriends(shareTo,shareFrom,shareKey,callback);
					} else if (mShareTo == FORWARD_TO_POST) {
						forwardToPost(shareTo,shareFrom,shareKey,callback);
					} else {
						sendToPost(shareTo,shareFrom,shareKey,callback);
					}
				}
			});
             */
		}
	}
	
	@Override
	public void forwardToPost(String shareTo,String shareFrom, String shareKey,IILWMessage iLWMessage, String convertionId, List<String> receiverIds,
			String eventId, String messageId, String msgownUid) {
        /**
		this.shareKey=shareKey;
		this.shareFrom=shareFrom;
		this.shareTo=shareTo;
		mShareTo = FORWARD_TO_POST;
		mIILWMessage = iLWMessage;
		mFeedVO = new FeedVO();
		mAttachments = new ArrayList<Map<String, Object>>();
		mHashmap = new HashMap<String, Object>();
		LWMessage lwMessage = (LWMessage) iLWMessage;
		mHashmap.put("clientId", lwMessage.getAppkey());
		mHashmap.put("title", lwMessage.getMessageTitle());
		mHashmap.put("description", lwMessage.getMessageText());
		mHashmap.put("link", lwMessage.getMessageLinkUrl());
		mHashmap.put("content", lwMessage.getMessageChat());

		initParameter();
		
		mAttachments.add(mHashmap);
		mFeedVO.setAttachments(mAttachments);
		mFeedVO.setOriginal(true);
		mFeedVO.setScope("firends");
		mFeedVO.setPublisher(LoginHelper.getInstance().getCurrentUserVO());
		mFeedVO.setComments(new ArrayList<CommentVO>());
		
		mConversionId =  convertionId;
		mReceiverIds = receiverIds;
		mEventId = eventId;
		mMessageId = messageId;
		mMsgownerId = msgownUid;
		shareToMessage(shareTo,shareFrom,shareKey,mSharePostCallback);
         */
	}

	// 好友转发好友
	@Override
	public void forwardToFriends(String shareTo,String shareFrom, String shareKey,IILWMessage iLWMessage, String convertionId,
			List<String> receiverIds, String uuid, String messageId,
			String msgownUid, Callback<Map<String, Object>> callback) {
		this.shareKey=shareKey;
		this.shareFrom=shareFrom;
		this.shareTo=shareTo;
		mIILWMessage = iLWMessage;
		mShareTo = FORWARD_TO_FRIENDS;
		mConversionId =  convertionId;
		mReceiverIds = receiverIds;
		mUuid = uuid;
		mMessageId = messageId;
		mMsgownerId = msgownUid;
		shareToMessage(shareTo,shareFrom,shareKey,callback);

	}
	

}
