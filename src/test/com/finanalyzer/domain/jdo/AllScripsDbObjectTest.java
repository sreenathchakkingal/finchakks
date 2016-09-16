package test.com.finanalyzer.domain.jdo;

import java.util.List;

import com.finanalyzer.domain.jdo.AllScripsDbObject;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;

import junit.framework.TestCase;

public class AllScripsDbObjectTest extends TestCase{
	
	public void testMergeRatings()
	{
		List<String> ratings = FastList.newListWith("Dummy Rating Name1", "Good",
				"Dummy Rating Name2", "Average", "Dummy Rating Name3", "Bad");
		final AllScripsDbObject allScripsDbObject = new AllScripsDbObject();
		allScripsDbObject.setRatingNameToValueFromList(ratings);

		List<String> ratingsToBeMerged = FastList.newListWith("Dummy Rating Name3", "Average", "Dummy Rating Name4", "Not Rated");
		allScripsDbObject.mergeRatings(ratingsToBeMerged);
		
		List<String> expectedList = FastList.newListWith("Dummy Rating Name1", "Good",
				"Dummy Rating Name2", "Average", "Dummy Rating Name3", "Average", "Dummy Rating Name4", "Not Rated");
		Verify.assertListsEqual(expectedList, allScripsDbObject.getRatingNameToValueAsList());
	}
	
}
