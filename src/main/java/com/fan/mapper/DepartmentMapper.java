package com.fan.mapper;

import com.fan.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Mapper
@Repository//component万能
public interface DepartmentMapper {
    Collection<Department> getDepartments();
    Department getDepartmentById(Integer id);
}
