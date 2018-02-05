package jzm.micro.study.core;
/**
 * @Description:redis的key
 * @author tangjiabin
 * @date 2017年10月27日 下午2:40:25
 */
public interface RedisCode {
	
	public static final long TIME_OUT = 60*60;//超时时间 (秒)
	public static final String  PRIVILEGE_CODE = "privilege_code";//权限
	public static final String  USER_ROLE_CODE = "user_role_code";//角色
	public static final String  ROLE_PRIVILEGE_CODE = "role_privilege_code";//用户权限
}