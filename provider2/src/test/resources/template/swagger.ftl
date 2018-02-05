package ${basePackage}.swagger;
import ${basePackage}.core.Result;
import ${basePackage}.model.${modelNameUpperCamel};
import io.swagger.annotations.*;
import springfox.documentation.annotations.ApiIgnore;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;


/**
 * Created by ${author} on ${date}.
 */
@Api(tags = "${apiInfo}管理")
public interface ${modelNameUpperCamel}Swagger {

     @ApiOperation(value="创建${modelNameUpperCamel}", notes="创建${modelNameUpperCamel}")
     public Result add(${modelNameUpperCamel} ${modelNameLowerCamel}, BindingResult bindingResult);


     @ApiOperation(value="删除", notes="以ID删除${modelNameUpperCamel}")
     @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Integer")
     public Result delete(@PathVariable Integer id);


     @ApiOperation(value="更新", notes="更新${modelNameLowerCamel}")
     public Result update(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel});


     @ApiOperation(value="详情", notes="获取详情")
     public Result detail(@PathVariable Integer id);


     @ApiOperation(value="列表", notes="")
     public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) ;

     @ApiOperation(value="按条件查找", notes="按条件查找")
     public Result query(@RequestBody ${modelNameUpperCamel} obj, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size);
}