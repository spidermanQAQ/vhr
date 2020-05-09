package org.javaboy.vhr.controller.emp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.javaboy.vhr.model.*;
import org.javaboy.vhr.service.*;
import org.javaboy.vhr.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.List;


@RequestMapping("/employee/basic")
@RestController
public class EmpBasicController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    NationService nationService;

    @Autowired
    PoliticsstatusService politicsstatusService;

    @Autowired
    JobLevelService jobLevelService;

    @Autowired
    PositionService positionService;

    @Autowired
    DepartmentService departmentService;

    @ApiOperation("查询所有员工信息")
    @GetMapping("/")
    //@RequestParam   接收普通请求参  * http://localhost:8080/hello/show16?name=linuxsir
    //@PathVariable("xxx")                @RequestMapping(value=”user/{id}/{name}”  请求路径：http://localhost:8080/hello/show5/1/james
    public RespPageBean getEmployeeByPage(@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "10") Integer size,String keyword){
        return employeeService.getEmployeeByPage(page,size,keyword);
    }

    @ApiOperation("添加员工")
    @PostMapping("/")
    public RespBean addEmp(@RequestBody Employee employee){
        if(employeeService.addEmp(employee)==1){
            return RespBean.ok("添加成功");
        }else {
            return RespBean.error("添加失败");
        }
    }

    @GetMapping("/nations")
    public List<Nation>getAllNations(){
        return nationService.getAllNations();
    }

    @GetMapping("/politicsstatus")
    public List<Politicsstatus>getAllPoliticsstatus(){
        return politicsstatusService.getAllPoliticsstatus();
    }

    @GetMapping("/joblevels")
    public List<JobLevel> getAllJobLevel(){
        return jobLevelService.getAllJobLevels();
    }

    @GetMapping("/positions")
    public List<Position> getAllPoSitions(){
        return positionService.getAllPositions();
    }

    @GetMapping("/maxWorkId")
    public RespBean maxWorkId(){
        RespBean respBean=RespBean.build().setStatus(200).setObject(String.format("%08d",employeeService.maxWorkId()+1));
        return respBean;
    }

    @GetMapping("/getAllDeps")
    public List<Department>getAllDeps(){
        return departmentService.getAllDepartments();
    }

    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable Integer id){
        if (employeeService.delete(id)==1){
            return RespBean.ok("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @DeleteMapping("/")
    public RespBean deleteEmps(Integer[] ids){
        if (employeeService.deleteEmps(ids)==ids.length){
            return RespBean.ok("删除成功");
        }
        return RespBean.error("删除失败");
    }


    @PutMapping("/")
    public RespBean editEmp(@RequestBody Employee employee){
        if (employeeService.editEmp(employee)==1){
            return RespBean.ok("修改成功");
        }
        return RespBean.error("修改成功");
    }

    @GetMapping("/export")
    //@ResponseBody可以直接返回Json结果，
    //ResponseEntity不仅可以返回json结果，还可以定义返回的HttpHeaders和HttpStatus
    public ResponseEntity<byte[]>exportDate(){
        List<Employee> list = (List<Employee>) employeeService.getEmployeeByPage(null, null, null).getData();
        return POIUtils.employee2Excel(list);
    }

    @PostMapping("/import")
    public RespBean importData(MultipartFile file) throws IOException {
        //file.transferTo(new File("E:\\xjh.xls"));
        List<Employee>list=POIUtils.excel2Employee(file,nationService.getAllNations(),politicsstatusService.getAllPoliticsstatus(),departmentService.getAllDepartmentsWithoutChildren(),positionService.getAllPositions(),jobLevelService.getAllJobLevels());
        for (Employee e:list) {
            System.out.println(e);
        }
        if (employeeService.insertEmployees(list)==list.size()){
            return RespBean.ok("上传成功");
        }
        return RespBean.error("上传失败");
    }
}
