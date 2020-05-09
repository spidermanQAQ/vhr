package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Employee;

import java.util.List;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    //@Param接收多个参数，给参数取别名，否则好像是按顺序什么的
    List<Employee> getEmployeeByPage(@Param("page") Integer page, @Param("size") Integer size,@Param("keyword") String keyword);

    Long getTotal(String keyword);

    Integer maxWorkId();

    Integer insertEmployees(@Param("list") List<Employee> list);

    Employee getEmployeeById(Integer id);

    Integer deleteEmps(@Param("ids") Integer[] ids);

    List<Employee> getEmpWithSalaryByPage(@Param("page") Integer page,@Param("size") Integer size);
}