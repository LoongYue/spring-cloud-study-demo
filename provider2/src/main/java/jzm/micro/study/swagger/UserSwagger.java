package jzm.micro.study.swagger;
import jzm.micro.study.core.Result;
import jzm.micro.study.model.User;
import io.swagger.annotations.*;
import springfox.documentation.annotations.ApiIgnore;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;


/**
 * Created by CodeGenerator on 2018/02/05.
 */
@Api(tags = "用户信息管理")
public interface UserSwagger {

     @ApiOperation(value="创建User", notes="创建User")
     public Result add(User user, BindingResult bindingResult);


     @ApiOperation(value="删除", notes="以ID删除User")
     @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Integer")
     public Result delete(@PathVariable Integer id);


     @ApiOperation(value="更新", notes="更新user")
     public Result update(@RequestBody User user);


     @ApiOperation(value="详情", notes="获取详情")
     public Result detail(@PathVariable Integer id);


     @ApiOperation(value="列表", notes="")
     public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) ;

     @ApiOperation(value="按条件查找", notes="按条件查找")
     public Result query(@RequestBody User obj, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size);
}