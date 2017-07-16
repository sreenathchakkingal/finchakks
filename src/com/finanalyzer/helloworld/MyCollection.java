package com.finanalyzer.helloworld;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyCollection<T> {
	
	private Comparator comparator;
	List<T> list = new ArrayList<T>();
	
	public MyCollection(Comparator comparator)
	{
		this.comparator = comparator;
	}
	
	
	public void add(T o) {

	//linearSearchAndInsert(o);
      binarySearchAndInsert(o);
		
		System.out.println("added " + o);
    }


	private void binarySearchAndInsert(T o) {
		if(list.isEmpty())
		   {
			   list.add(o);
		   }
		   else
		   {
			    int first  = 0;
			    int last   = list.size() - 1;
			    int middle = (first + last)/2;
			    int position=-1;

			    while( first <= last )
			    {
			      if ( this.comparator.compare(list.get(middle), o)<0)
			        first = middle + 1;    
			      else if (this.comparator.compare(list.get(middle), o)==0 ) 
			      {
			    	  break;
			      }
			      else
			         last = middle - 1;
			 
			      middle = (first + last)/2;
			   }

			   if(this.comparator.compare(o, list.get(middle))<=0)
			   {
				   position=middle;
				   list.add(position, o);
			   }
			   else
			   {
				   position=middle+1;
				   list.add(position, o);
			   }
		   }
	}
	
	public void linearSearch(T o)
	{
		boolean foundPosition=true;
		for (int i=0;i<list.size();i++)
		{
			T eachElement = list.get(i);
			boolean oLessThanCurrentElement = this.comparator.compare(o, eachElement)<0;
			if (oLessThanCurrentElement)
			{
				list.add(i, o);
				foundPosition=false;
				break;
			}
		}
		if(foundPosition)
		{
			list.add(o);
		}
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if(list.isEmpty())
		{
			return "[]";
		}
		sb.append('[');
		for (T each : list)
		{
			 sb.append(each).append(',').append(' ');	
		}
		return sb.append(']').toString();
	}
}
