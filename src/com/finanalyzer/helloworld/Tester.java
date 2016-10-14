package com.finanalyzer.helloworld;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.finanalyzer.db.jdo.JdoDbOperations;
import com.finanalyzer.domain.builder.StopLossDbObjectBuilder;
import com.finanalyzer.domain.jdo.StopLossDbObject;
import com.finanalyzer.util.DateUtil;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.ListIterate;


public class Tester {
	public static void main(String[] args) throws Exception{
		Employee e1 = new Employee(true, "a");
		Employee e2 = new Employee(true, "b");
		
		final PartitionMutableList<Employee> partition = ListIterate.partition(FastList.newListWith(e1, e2),  new Predicate<Employee>() {

			@Override
			public boolean accept(Employee employee) {
				return employee.isGood();
			}
		});
		
		final EmployeeWrapper employeeWrapper = new EmployeeWrapper(partition.getSelected(), partition.getRejected());
		
		if(employeeWrapper.getBadEmployees()==null)
		{
			System.out.println("null: "+partition.getRejected());	
		}
		else
		{
			System.out.println("is not null: "+partition.getRejected());
		}
		
		
	}
}

