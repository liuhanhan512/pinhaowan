package com.alibaba.laiwang.tide.share.business;

import com.laiwang.sdk.message.IILWMessage;

import java.util.List;
import java.util.Map;

// 分享接口
public interface IIShareProvider {
	
	/**
	 * 分享到动态
	 * @param iLWMessage
	 */
	public void shareToHome(String shareTo, String shareFrom, String shareKey, final IILWMessage iLWMessage);
	
	/**
	 * 分享到好友
	 * @param iLWMessage
	 * @param conversionId
	 * @param receiverId
	 * @param flag
	 * @param callback
	 */
	public void shareToFriends(String shareTo, String shareFrom, String shareKey, IILWMessage iLWMessage, String conversionId,
                               List<String> receiverId, String flag,
                               Callback<Map<String, Object>> callback);
	
	/**
	 * 转发到好友
	 * @param iLWMessage
	 * @param convertionId
	 * @param receiverIds
	 * @param uuid
	 * @param messageId
	 * @param msgownUid
	 * @param callback
	 */
	public void forwardToFriends(String shareTo, String shareFrom, String shareKey, IILWMessage iLWMessage, String convertionId,
                                 List<String> receiverIds, String uuid, String messageId,
                                 String msgownUid, Callback<Map<String, Object>> callback);
	
	/**
	 * 转达到动态
	 * @param iLWMessage	分享数据
	 * @param convertionId	会话id
	 * @param receiverIds	接受者
	 * @param eventId		动态ID,扎堆ID
	 * @param messageId		消息ID
	 * @param msgownUid		消息发送者
	 */
	public void forwardToPost(String shareTo, String shareFrom, String shareKey, IILWMessage iLWMessage, String convertionId, List<String> receiverIds,
                              String eventId, String messageId, String msgownUid);
}
