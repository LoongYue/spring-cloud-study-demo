package jzm.micro.study.core;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Adam.jiang on 2017/8/11. test
 */
public abstract class BaseController {
    
//    @Resource
//    private RbacUserService rbacUserService;
//    
//    protected RbacUser user = null;
//    
//    protected List<RbacRolePrivilege> rbacRolePrivileges = null;

	protected static final Map<String, List<Integer>> MAP = new HashMap<>();

	protected Result success(Object objs) {
		return ResultGenerator.genSuccessResult(objs);
	}

	protected Result success(Object... objs) {
		return ResultGenerator.genSuccessResult(objs);
	}

	protected Result success() {
		return ResultGenerator.genSuccessResult();
	}

	protected Result fail(String message) {
		return ResultGenerator.genFailResult(message);
	}


}