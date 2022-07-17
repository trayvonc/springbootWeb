package com.fan.controller;

import com.fan.mapper.DepartmentMapper;
import com.fan.mapper.EmployeeMapper;
import com.fan.pojo.Department;
import com.fan.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private DepartmentMapper departmentMapper;

    @RequestMapping("/emps")
    public String list(Model model){

        Collection<Employee> employees=employeeMapper.getAll();
        System.out.println(employees);
        model.addAttribute("emps",employees);
        Collection<Department> departments = departmentMapper.getDepartments();
        model.addAttribute("departments",departments);
        return "emp/list";
    }
    @GetMapping("/emp")
    public String toAddpage(Model model){//这里显示提交页面
        //查出所有部门信息
        Collection<Department> departments = departmentMapper.getDepartments();
        model.addAttribute("departments",departments);
        return "emp/add";
    }
    @PostMapping("/emp")//restful风格，get和post不冲突，这里提交表单
    public String addEmp(Employee employee){
        employeeMapper.save(employee);
        return "redirect:/emps";
    }
    @GetMapping("/emp/{id}")
    public String toUpdateEmp(@PathVariable("id")Integer id,Model model){//根据路径id查询员工
        Employee employeeById = employeeMapper.getEmployeeById(id);
        model.addAttribute("emp",employeeById);
        //查出所有部门信息
        Collection<Department> departments = departmentMapper.getDepartments();
        model.addAttribute("departments",departments);
        return "emp/update";//数据加载后展示到html
    }
    @PostMapping("/update")
    public String updateEmp(Employee employee){
        employeeMapper.updateEmployee(employee);
        return "redirect:/emps";
    }

    @GetMapping("/dele/{id}")
    public String deleEmp(@PathVariable("id") Integer id,Model model){
        employeeMapper.delete(id);
        return "redirect:/emps";

    }
    @RequestMapping("/empqiandao/{id}")
    public String toQianDao(@PathVariable("id") String name){
        return "emp/qiandao";
    }
}
