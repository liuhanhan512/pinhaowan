package com.alibaba.laiwang.tide.share.business.excutor.common;

public interface Constants {
	/**
	 * 获取用的id
	 * @return
	 */
	public String getUserID();
	 /** 
	  * sina 的 APP_KEY，
	  */
    public String getSInaAppKey();
    /** 
     * sina应用的回调页
     * 
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public String getSinaRedirectRrl();
    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * 
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * 
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public String getSinaScope();
    
    /**
     * QQ的appid 
     */
    public String getQQAppID();
    
    /**
     * 微信的appid 
     */
    public String getWXAppID();
    
    /**
     * 来往的token
     */
    public String getLWToken();
    
    /**
     * 来往id
     */
    public String getLWSercetID();

    /**
     * 应用名
     * @return
     */
    public String getAppName();

    /**
     * 获取应用package名字
     * @return
     */
    public String getPackageName();
}
