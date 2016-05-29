package com.devesh.tapestry.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.MixinClasses;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.services.StringValueEncoder;

import com.devesh.tapestry.mixins.PaletteObserve;

public class Index
{
	@Property
	private final StringValueEncoder stringValueEncoder = new StringValueEncoder();

	@Property
	@Persist(PersistenceConstants.FLASH)
	private List<String> firstPaletteSelectedValues;


	@Property
	private List<String> categories = Arrays.asList("1", "2", "3");

	@Component(parameters = {"model=categories", "selected=firstPaletteSelectedValues", "encoder=stringValueEncoder",
			"paletteObserve.zone=secondPaletteZone", "clientEvent=literal:t5:palette:didChange", "event=literal:filter"})
	@MixinClasses(PaletteObserve.class)
	@Property
	private Palette firstPalette;


	@InjectComponent
	private Zone secondPaletteZone;

	@SetupRender
	void setup()
	{
		if (firstPaletteSelectedValues == null)
		{
			firstPaletteSelectedValues = new ArrayList<String>();
		}

		if(selectModels == null)
			selectModels = new ArrayList<String>();
	}

	@Property
	@Persist
	private List<String> selectModels;

	@Property
	@Persist
	private List<String> selectedValue;


	public Object onFilter(String context)
	{

		System.out.println("Filter called..");
		
		updateSelectBoxModel(context);
		return secondPaletteZone;
	}

	private void updateSelectBoxModel(String context) {
		selectModels.clear();
		
		String[] subCategories = context.split(",");
		for (String string : subCategories) {
			selectModels.add(string);
		}
	}

}
