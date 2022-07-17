package com.fan.mapper;

import com.fan.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Mapper
@Repository//component万能
public interface EmployeeMapper {
    void save(Employee employee);
    Collection<Employee> getAll();
    Employee getEmployeeById(Integer id);
    void delete(Integer id);
    void updateEmployee(Employee employee);

}
