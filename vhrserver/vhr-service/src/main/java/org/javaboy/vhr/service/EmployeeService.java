package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.EmployeeMapper;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.RespPageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    public static final Logger logger= LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;
    public RespPageBean getEmployeeByPage(Integer page, Integer size, String keyword) {
        //为了获取当前页的员工信息，因为mysql limit第一个参数为偏移量，第二个参数为记录行的数目，如limit 5，10指6-15
        if (page!=null&&size!=null){
            page=(page-1)*size;
        }
        //当前页的员工信息，vue只需要这两个参数
        List<Employee>data=employeeMapper.getEmployeeByPage(page,size,keyword);
        Long total=employeeMapper.getTotal(keyword);
        RespPageBean respPageBean=new RespPageBean();
        respPageBean.setData(data);
        respPageBean.setTotal(total);
        return respPageBean;
    }

    public Integer addEmp(Employee employee) {
        int result = employeeMapper.insertSelective(employee);
        //用到了主键回填，否则你刚插入employee是id得不到的
        if (result==1){
            Employee emp=employeeMapper.getEmployeeById(employee.getId());
            System.out.println(emp.toString());
            rabbitTemplate.convertAndSend("javaboy.mail",emp);
        }
        return result;
    }

    public Integer maxWorkId() {
        return employeeMapper.maxWorkId();
    }

    public Integer delete(Integer id) {
        return employeeMapper.deleteByPrimaryKey(id);
    }

    public Integer editEmp(Employee employee) {
        return employeeMapper.updateByPrimaryKeySelective(employee);
    }

    public Integer insertEmployees(List<Employee> list) {
        return employeeMapper.insertEmployees(list);
    }

    public Integer deleteEmps(Integer[] ids) {
        return employeeMapper.deleteEmps(ids);
    }

    public RespPageBean getEmpWithSalaryByPage(Integer page, Integer size) {
        if (page!=null&&size!=null){
            page=(page-1)*size;
        }
        List<Employee>list=employeeMapper.getEmpWithSalaryByPage(page,size);
        Long total = employeeMapper.getTotal(null);
        RespPageBean respPageBean=new RespPageBean();
        respPageBean.setData(list);
        respPageBean.setTotal(total);
        return respPageBean;
    }
}
