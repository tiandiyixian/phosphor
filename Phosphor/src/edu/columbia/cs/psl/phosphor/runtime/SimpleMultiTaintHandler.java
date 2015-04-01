package edu.columbia.cs.psl.phosphor.runtime;

import edu.columbia.cs.psl.phosphor.struct.ArrayList;
import edu.columbia.cs.psl.phosphor.struct.ControlTaintTagStack;
import edu.columbia.cs.psl.phosphor.struct.LinkedList;
import edu.columbia.cs.psl.phosphor.struct.Tainted;
import edu.columbia.cs.psl.phosphor.struct.TaintedWithIntTag;
import edu.columbia.cs.psl.phosphor.struct.TaintedWithObjTag;

public class SimpleMultiTaintHandler {
	static ArrayList<Tag> tags = new ArrayList<SimpleMultiTaintHandler.Tag>();
	static{
		tags.add(new Tag());
	}
	public static void combineTagsFromObject(Object o, ControlTaintTagStack tags)
	{
		if(o instanceof TaintedWithIntTag)
		{
			((TaintedWithIntTag) o).setPHOSPHOR_TAG(combineTags(((TaintedWithIntTag)o).getPHOSPHOR_TAG(), tags));
		}
		else if(o instanceof TaintedWithObjTag)
		{
			((TaintedWithObjTag) o).setPHOSPHOR_TAG(combineTags(((TaintedWithObjTag)o).getPHOSPHOR_TAG(), tags));

		}
	}
	public static Object combineTags(Object t1, ControlTaintTagStack tags){
		return t1;
	}
	public static Object combineTags(Object t1, Object t2)
	{
		return t1;
	}
	public static int combineTags(int t1, int t2){
		if(t1 == 0 && t2 == 0)
			return 0;
		else if(t1 == 0)
		{
			return t2;
		}
		else if(t2 == 0)
		{
			return t1;
		}
		else
		{
			Tag newT = new Tag();
			synchronized (tags) {
				newT.dependentOn.add(tags.get(t1));
				newT.dependentOn.add(tags.get(t2));
				tags.add(newT);
				return tags.size()-1;
			}
		}
	}
	public static int combineTags(int t1, ControlTaintTagStack controlTags){
		if(t1 == 0 && controlTags.isEmpty())
			return 0;
		else if(t1 == 0)
		{
			return controlTags.getTag();
		}
		else if(controlTags.isEmpty())
		{
			return t1;
		}
		else
		{
			Tag newT = new Tag();
			synchronized (tags) {
				newT.dependentOn.add(tags.get(t1));
				newT.dependentOn.add(tags.get(controlTags.getTag()));
				tags.add(newT);
				return tags.size()-1;
			}
		}
	}
	static class Tag{
		LinkedList<Tag> dependentOn = new LinkedList<SimpleMultiTaintHandler.Tag>();
		String label;
	}
	public static int nextTag(String tag2) {
		synchronized (tags) {
			Tag tag = new Tag();
			tags.add(tag);
			tag.label=tag2;
			return tags.size()-1;
		}
	}
}