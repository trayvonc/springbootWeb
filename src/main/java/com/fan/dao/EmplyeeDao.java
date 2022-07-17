//package com.fan.dao;
//
//import com.fan.pojo.Department;
//import com.fan.pojo.Employee;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//@Repository
//public class EmplyeeDao {
//    private static Map<Integer, Employee> employees=null;
//    @Autowired
//    private DepartmentDao departmentDao;
//    static {
//        employees=new HashMap<Integer, Employee>();
//        employees.put(1001,new Employee(1001,"AA","123@qq.com",1,new Department(101,"教学部")));
//        employees.put(1002,new Employee(1002,"BB","123@qq.com",0,new Department(102,"市场部")));
//        employees.put(1003,new Employee(1003,"CC","123@qq.com",1,new Department(103,"教研部")));
//        employees.put(1004,new Employee(1004,"DD","123@qq.com",0,new Department(104,"运营部")));
//        employees.put(1005,new Employee(1005,"EE","123@qq.com",1,new Department(105,"后勤部")));
//    }
//    private static Integer initId=1006;
//    public void save(Employee employee){
//        if(employee.getId()==null){
//            employee.setId(initId++);
//        }
//        //再次从数据库查询
//        employee.setDepartment(departmentDao.getDepartmentById(employee.getDepartment().getId()));
//        employees.put(employee.getId(),employee);
//    }
//    public Collection<Employee> getAll(){
//        return employees.values();
//    }
//    public Employee getEmployeeById(Integer id){
//        return employees.get(id);
//    }
//    public void delete(Integer id){
//        employees.remove(id);
//    }
//}
