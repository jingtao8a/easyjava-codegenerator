package org.jingtao8a.easypan.entity.po;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
@Description:用户信息
@Date:2024-01-26
*/
@Data
@ToString
public class UserInfo implements Serializable {
	/**
	 * 用户id
	*/
	private String userId;
	/**
	 * 用户昵称
	*/
	private String nickName;
	/**
	 * 邮箱
	*/
	@JsonIgnore
	private String email;
	/**
	 * qqOpenId
	*/
	@JsonIgnore
	private String qqOpenId;
	/**
	 * qq头像
	*/
	private String qqAvatar;
	/**
	 * 密码
	*/
	private String password;
	/**
	 * 注册时间
	*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date joinTime;
	/**
	 * 最后一次登入时间
	*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;
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