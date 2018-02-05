package jzm.micro.study.controller;

import jzm.micro.study.core.Result;
import jzm.micro.study.core.ResultGenerator;
import jzm.micro.study.core.BaseController;
import jzm.micro.study.model.User;
import jzm.micro.study.service.UserService;
import jzm.micro.study.swagger.UserSwagger;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.Transient;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;

/**
* Created by CodeGenerator on 2018/02/05.
*/
@RestController
@RequestMapping("/user")
public class UserController extends BaseController implements UserSwagger {
    @Resource
    private UserService userService;

    @PostMapping("/add")
    public Result add(@Validated @RequestBody User user, BindingResult bindingResult) {
    	if(bindingResult.hasErrors()){
    		StringBuffer errorSb = new StringBuffer();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
            	errorSb.append(fieldError.getDefaultMessage());
            }
            return fail(errorSb.toString());
        }
        userService.save(user);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userService.deleteById(id);
        return success();
    }

    @PutMapping
    public Result update(@RequestBody User user) {
        userService.update(user);
        return success();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        User user = userService.findById(id);
        return success(user);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<User> list = userService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return success(pageInfo);
    }

    @PostMapping("/query")
    public Result query(@RequestBody User obj, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        Condition condition = new Condition(User.class);
        Condition.Criteria criteria = condition.createCriteria();

        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for(int i=0; i < fields.length; i++){
            Field f = fields[i];
            f.setAccessible(true);
			if(f.isAnnotationPresent(Transient.class)) {
            	continue;
            }
            try {
            	criteria.andEqualTo(f.getName(), f.get(obj));
            }catch (Exception e){
                fail(e.getMessage());
            }
        }

        List<User> list = userService.findByCondition(condition);
        PageInfo pageInfo = new PageInfo(list);
        return success(pageInfo);
    }
}
