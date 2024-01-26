package org.jingtao8a.easypan.entity.query;
import lombok.Data;
import lombok.ToString;
import java.util.Date;
/**
@Description:用户信息
@Date:2024-01-26
*/
@Data
@ToString
public class UserInfoQuery extends BaseQuery {
	/**
	 * 用户id
	*/
	private String userId;
	private String userIdFuzzy;

	/**
	 * 用户昵称
	*/
	private String nickName;
	private String nickNameFuzzy;

	/**
	 * 邮箱
	*/
	private String email;
	private String emailFuzzy;

	/**
	 * qqOpenId
	*/
	private String qqOpenId;
	private String qqOpenIdFuzzy;

	/**
	 * qq头像
	*/
	private String qqAvatar;
	private String qqAvatarFuzzy;

	/**
	 * 密码
	*/
	private String password;
	private String passwordFuzzy;

	/**
	 * 注册时间
	*/
	private Date joinTime;
	private String joinTimeStart;
	private String joinTimeEnd;

	/**
	 * 最后一次登入时间
	*/
	private Date lastLoginTime;
	private String lastLoginTimeStart;
	private String lastLoginTimeEnd;

	/**
	 * 0:禁用 1:启用
	*/
	private Integer status;
	/**
	 * 使用空间 byte
	*/
	private Integer userSpace;
	/**
	 * 总空间
	*/
	private Integer totalSpace;
}