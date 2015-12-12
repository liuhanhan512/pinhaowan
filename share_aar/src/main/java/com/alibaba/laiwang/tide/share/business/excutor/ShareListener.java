package com.alibaba.laiwang.tide.share.business.excutor;

public interface ShareListener {
	public void onSuccess();
	public void onException(String message);
	public void onCancel();
}
