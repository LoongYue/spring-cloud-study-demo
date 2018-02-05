package ${basePackage}.controller;

import ${basePackage}.core.Result;
import ${basePackage}.core.ResultGenerator;
import ${basePackage}.core.BaseController;
import ${basePackage}.model.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import ${basePackage}.swagger.${modelNameUpperCamel}Swagger;
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
* Created by ${author} on ${date}.
*/
@RestController
@RequestMapping("${baseRequestMapping}")
public class ${modelNameUpperCamel}Controller extends BaseController implements ${modelNameUpperCamel}Swagger {
    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @PostMapping("/add")
    public Result add(@Validated @RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}, BindingResult bindingResult) {
    	if(bindingResult.hasErrors()){
    		StringBuffer errorSb = new StringBuffer();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
            	errorSb.append(fieldError.getDefaultMessage());
            }
            return fail(errorSb.toString());
        }
        ${modelNameLowerCamel}Service.save(${modelNameLowerCamel});
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        ${modelNameLowerCamel}Service.deleteById(id);
        return success();
    }

    @PutMapping
    public Result update(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.update(${modelNameLowerCamel});
        return success();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.findById(id);
        return success(${modelNameLowerCamel});
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return success(pageInfo);
    }

    @PostMapping("/query")
    public Result query(@RequestBody ${modelNameUpperCamel} obj, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        Condition condition = new Condition(${modelNameUpperCamel}.class);
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

        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.findByCondition(condition);
        PageInfo pageInfo = new PageInfo(list);
        return success(pageInfo);
    }
}
